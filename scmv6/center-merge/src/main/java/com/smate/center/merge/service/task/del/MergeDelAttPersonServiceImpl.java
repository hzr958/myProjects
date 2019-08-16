package com.smate.center.merge.service.task.del;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.merge.dao.person.AttPersonDao;
import com.smate.center.merge.model.sns.person.AttPerson;
import com.smate.center.merge.model.sns.task.AccountsMergeData;
import com.smate.center.merge.service.task.MergeBaseService;

/**
 * 删除attPerson关注人员信息服务.
 * 
 * @author yhx
 *
 * @date 2019年2月26日
 */
@Transactional(rollbackFor = Exception.class)
public class MergeDelAttPersonServiceImpl extends MergeBaseService {
  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private AttPersonDao attPersonDao;

  @Override
  public boolean checkRun(Long savePsnId, Long delPsnId) throws Exception {
    List<AttPerson> attPersonList = attPersonDao.findAttPsnIds(delPsnId);
    if (attPersonList != null && attPersonList.size() > 0) {
      return true;
    }
    return false;
  }

  @Override
  public boolean dealMerge(Long savePsnId, Long delPsnId) throws Exception {
    try {
      List<AttPerson> attPersonList = attPersonDao.findAttPsnIds(delPsnId);
      for (AttPerson attPerson : attPersonList) {
        try {
          String desc = "删除关注人员信息 att_person ";
          AccountsMergeData accountsMergeData =
              super.saveAccountsMergeData(savePsnId, delPsnId, desc, super.DEAL_TYPE_DEL, attPerson);
          attPersonDao.delete(attPerson);
          super.updateAccountsMergeDataStatus(accountsMergeData);
        } catch (Exception e) {
          logger.error("帐号合并->删除关注人员信息出错 att_person , arrPerson=[" + attPerson + "], savePsnId=" + savePsnId
              + ",delPsnId=" + delPsnId, e);
          throw new Exception("帐号合并->删除关注人员信息出错 att_person , arrPerson=[" + attPerson + "], savePsnId=" + savePsnId
              + ",delPsnId=" + delPsnId, e);
        }
      }
    } catch (Exception e) {
      logger.error("合并帐号->删除关注人员信息出错 att_person ,  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
      throw new Exception("合并帐号->删除关注人员信息出错 att_person ,  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
    }
    return true;
  }
}
