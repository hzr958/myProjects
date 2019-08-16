package com.smate.center.merge.service.task.del;

import com.smate.center.merge.dao.person.PersonDao;
import com.smate.center.merge.model.sns.person.Person;
import com.smate.center.merge.model.sns.task.AccountsMergeData;
import com.smate.center.merge.service.task.MergeBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * 删除person人员信息服务.
 * 
 * @author tsz
 *
 * @date 2018年9月11日
 */
@Transactional(rollbackFor = Exception.class)
public class MergeDelPersonServiceImpl extends MergeBaseService {
  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PersonDao personDao;

  @Override
  public boolean checkRun(Long savePsnId, Long delPsnId) throws Exception {
    Person person = personDao.get(delPsnId);
    if (person != null) {
      return true;
    }
    return false;
  }

  @Override
  public boolean dealMerge(Long savePsnId, Long delPsnId) throws Exception {
    try {
      Person person = personDao.get(delPsnId);
      String desc = "删除个人信息 person ";
      AccountsMergeData accountsMergeData =
          super.saveAccountsMergeData(savePsnId, delPsnId, desc, super.DEAL_TYPE_DEL, person);
      personDao.delete(person);
      super.updateAccountsMergeDataStatus(accountsMergeData);
    } catch (Exception e) {
      logger.error("合并帐号->删除个人信息出错 person ,  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
      throw new Exception("合并帐号->删除个人信息出错 person ,  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
    }
    return true;
  }
}
