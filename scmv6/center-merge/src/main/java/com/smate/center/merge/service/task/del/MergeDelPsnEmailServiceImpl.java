package com.smate.center.merge.service.task.del;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.merge.dao.person.PersonEmailDao;
import com.smate.center.merge.model.sns.person.PersonEmail;
import com.smate.center.merge.model.sns.task.AccountsMergeData;
import com.smate.center.merge.service.task.MergeBaseService;

/**
 * 删除psn_email人员多邮件表信息服务.
 * 
 * @author yhx
 *
 * @date 2019年2月26日
 */
@Transactional(rollbackFor = Exception.class)
public class MergeDelPsnEmailServiceImpl extends MergeBaseService {
  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PersonEmailDao personEmailDao;

  @Override
  public boolean checkRun(Long savePsnId, Long delPsnId) throws Exception {
    List<PersonEmail> personEmailList = personEmailDao.findListByPersonId(delPsnId);
    if (personEmailList != null && personEmailList.size() > 0) {
      return true;
    }
    return false;
  }

  @Override
  public boolean dealMerge(Long savePsnId, Long delPsnId) throws Exception {
    try {
      List<PersonEmail> personEmailList = personEmailDao.findListByPersonId(delPsnId);
      for (PersonEmail personEmail : personEmailList) {
        try {
          String desc = "删除人员邮件信息 psn_email ";
          AccountsMergeData accountsMergeData =
              super.saveAccountsMergeData(savePsnId, delPsnId, desc, super.DEAL_TYPE_DEL, personEmail);
          personEmailDao.delete(personEmail);
          super.updateAccountsMergeDataStatus(accountsMergeData);
        } catch (Exception e) {
          logger.error("帐号合并->删除人员邮件信息出错 psn_email , personEmail=[" + personEmail + "], savePsnId=" + savePsnId
              + ",delPsnId=" + delPsnId, e);
          throw new Exception("帐号合并->删除人员邮件信息出错 psn_email , personEmail=[" + personEmail + "], savePsnId=" + savePsnId
              + ",delPsnId=" + delPsnId, e);
        }
      }
    } catch (Exception e) {
      logger.error("合并帐号->删除人员邮件信息出错 psn_email ,  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
      throw new Exception("合并帐号->删除人员邮件信息出错 psn_email ,  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
    }
    return true;
  }
}
