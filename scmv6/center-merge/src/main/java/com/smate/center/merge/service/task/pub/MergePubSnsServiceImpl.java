package com.smate.center.merge.service.task.pub;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.merge.dao.pub.PubSnsDao;
import com.smate.center.merge.model.sns.pub.PubSns;
import com.smate.center.merge.model.sns.task.AccountsMergeData;
import com.smate.center.merge.service.task.MergeBaseService;

/**
 * 成果个人库合并处理.
 * 
 * @author tsz
 *
 * @date 2018年9月11日
 */
@Transactional(rollbackFor = Exception.class)
public class MergePubSnsServiceImpl extends MergeBaseService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubSnsDao pubSnsDao;

  @Override
  public boolean checkRun(Long savePsnId, Long delPsnId) throws Exception {
    List<PubSns> list = pubSnsDao.getPubsByCreatePsnId(delPsnId);
    if (CollectionUtils.isEmpty(list)) {
      return false;
    }
    return true;
  }

  @Override
  public boolean dealMerge(Long savePsnId, Long delPsnId) throws Exception {
    try {
      List<PubSns> list = pubSnsDao.getPubsByCreatePsnId(delPsnId);
      for (PubSns pubSns : list) {
        try {
          // 保存备份记录
          String desc = "个人库成果合并 v_pub_sns ";
          AccountsMergeData accountsMergeData =
              super.saveAccountsMergeData(savePsnId, delPsnId, desc, super.DEAL_TYPE_MERGE, pubSns);
          pubSns.setCreatePsnId(savePsnId);
          pubSns.setGmtModified(new Date());
          pubSnsDao.save(pubSns);
          // 更新备份数据状态
          super.updateAccountsMergeDataStatus(accountsMergeData);
        } catch (Exception e) {
          logger.error("帐号合并->成果合并->个人库成果合并 出错    pubId=" + pubSns.getPubId() + ", savePsnId=" + savePsnId
              + ",delPsnId=" + delPsnId, e);
          throw new Exception("帐号合并->成果合并->个人库成果合并 出错     pubId=" + pubSns.getPubId() + ", savePsnId=" + savePsnId
              + ",delPsnId=" + delPsnId, e);
        }
      }
    } catch (Exception e) {
      logger.error("帐号合并->成果合并->个人库成果合并 出错   savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
      throw new Exception("帐号合并->成果合并->个人库成果合并 出错  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
    }
    return true;
  }
}
