package com.smate.center.merge.service.task.del;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.merge.dao.friend.PsnCopartnerDao;
import com.smate.center.merge.model.sns.friend.PsnCopartner;
import com.smate.center.merge.model.sns.task.AccountsMergeData;
import com.smate.center.merge.service.task.MergeBaseService;

/**
 * 删除PSN_COPARTNER人员合作者服务.
 * 
 * @author yhx
 *
 * @date 2019年4月19日
 */
@Transactional(rollbackFor = Exception.class)
public class MergeDelPsnCoPartnerServiceImpl extends MergeBaseService {
  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnCopartnerDao psnCopartnerDao;

  @Override
  public boolean checkRun(Long savePsnId, Long delPsnId) throws Exception {
    List<PsnCopartner> coPsnList = psnCopartnerDao.findByCoPsnId(delPsnId);
    if (coPsnList != null && coPsnList.size() > 0) {
      return true;
    }
    return false;
  }

  @Override
  public boolean dealMerge(Long savePsnId, Long delPsnId) throws Exception {
    try {
      List<PsnCopartner> coPsnList = psnCopartnerDao.findByCoPsnId(delPsnId);
      for (PsnCopartner psnCopartner : coPsnList) {
        try {
          String desc = "删除人员合作者信息 psn_copartner ";
          AccountsMergeData accountsMergeData =
              super.saveAccountsMergeData(savePsnId, delPsnId, desc, super.DEAL_TYPE_DEL, psnCopartner);
          psnCopartnerDao.delete(psnCopartner);
          super.updateAccountsMergeDataStatus(accountsMergeData);
        } catch (Exception e) {
          logger.error("帐号合并->删除人员合作者信息 psn_copartner , psnCopartner=[" + psnCopartner + "], savePsnId=" + savePsnId
              + ",delPsnId=" + delPsnId, e);
          throw new Exception("帐号合并->删除人员合作者信息 psn_copartner , psnCopartner=[" + psnCopartner + "], savePsnId="
              + savePsnId + ",delPsnId=" + delPsnId, e);
        }
      }
    } catch (Exception e) {
      logger.error("合并帐号->删除人员合作者信息 psn_copartner ,  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
      throw new Exception("合并帐号->删除人员合作者信息 psn_copartner ,  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
    }
    return true;
  }
}
