package com.smate.center.merge.service.task.pub;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.merge.dao.pub.PsnPubDao;
import com.smate.center.merge.model.sns.pub.PsnPub;
import com.smate.center.merge.model.sns.task.AccountsMergeData;
import com.smate.center.merge.service.task.MergeBaseService;

/**
 * 个人成果合并处理.
 * 
 * @author tsz
 *
 * @date 2018年9月11日
 */
@Transactional(rollbackFor = Exception.class)
public class MergePsnPubServiceImpl extends MergeBaseService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnPubDao psnPubDao;

  @Override
  public boolean checkRun(Long savePsnId, Long delPsnId) throws Exception {
    List<PsnPub> list = psnPubDao.getPubsByOwnerPsnId(delPsnId);
    if (CollectionUtils.isEmpty(list)) {
      return false;
    }
    return true;
  }

  @Override
  public boolean dealMerge(Long savePsnId, Long delPsnId) throws Exception {
    try {
      List<PsnPub> list = psnPubDao.getPubsByOwnerPsnId(delPsnId);
      for (PsnPub psnPub : list) {
        try {
          // 保存备份记录
          String desc = "合成果拥有者合并 v_psn_pub ";
          AccountsMergeData accountsMergeData =
              super.saveAccountsMergeData(savePsnId, delPsnId, desc, super.DEAL_TYPE_MERGE, psnPub);
          psnPub.setOwnerPsnId(savePsnId);
          // 重新记录更新时间
          psnPub.setGmtModified(new Date());
          psnPubDao.save(psnPub);
          // 更新备份数据状态
          super.updateAccountsMergeDataStatus(accountsMergeData);
        } catch (Exception e) {
          logger.error("帐号合并->成果合并->成果拥有者合并 出错    pubId=" + psnPub.getPubId() + ", savePsnId=" + savePsnId
              + ",delPsnId=" + delPsnId, e);
          throw new Exception("帐号合并->成果合并->成果拥有者合并 出错     pubId=" + psnPub.getPubId() + ", savePsnId=" + savePsnId
              + ",delPsnId=" + delPsnId, e);
        }
      }
    } catch (Exception e) {
      logger.error("帐号合并->成果合并->成果拥有者合并 出错   savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
      throw new Exception("帐号合并->成果合并->成果拥有者合并 出错  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
    }
    return true;
  }
}
