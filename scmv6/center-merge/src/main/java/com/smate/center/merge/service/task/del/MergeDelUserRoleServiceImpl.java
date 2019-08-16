package com.smate.center.merge.service.task.del;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.merge.dao.account.UserRoleDao;
import com.smate.center.merge.model.sns.person.UserRole;
import com.smate.center.merge.model.sns.task.AccountsMergeData;
import com.smate.center.merge.service.task.MergeBaseService;

/**
 * 删除SYS_USER_ROLE用户角色表.
 * 
 * @author yhx
 *
 * @date 2019年07月08日
 */
@Transactional(rollbackFor = Exception.class)
public class MergeDelUserRoleServiceImpl extends MergeBaseService {
  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private UserRoleDao userRoleDao;

  @Override
  public boolean checkRun(Long savePsnId, Long delPsnId) throws Exception {
    List<UserRole> userRoleList = userRoleDao.getUserRole(delPsnId);
    if (userRoleList != null && userRoleList.size() > 0) {
      return true;
    }
    return false;
  }

  @Override
  public boolean dealMerge(Long savePsnId, Long delPsnId) throws Exception {
    try {
      List<UserRole> userRoleList = userRoleDao.getUserRole(delPsnId);
      for (UserRole userRole : userRoleList) {
        try {
          String desc = "删除用户角色信息表 sys_user_role ";
          AccountsMergeData accountsMergeData =
              super.saveAccountsMergeData(savePsnId, delPsnId, desc, super.DEAL_TYPE_DEL, userRole);
          userRoleDao.delete(userRole);
          super.updateAccountsMergeDataStatus(accountsMergeData);
        } catch (Exception e) {
          logger.error("帐号合并->删除用户角色信息表 sys_user_role , userRole=[" + userRole + "], savePsnId=" + savePsnId
              + ",delPsnId=" + delPsnId, e);
          throw new Exception("帐号合并->删除用户角色信息表 sys_user_role , userRole=[" + userRole + "], savePsnId=" + savePsnId
              + ",delPsnId=" + delPsnId, e);
        }
      }
    } catch (Exception e) {
      logger.error("合并帐号->删除用户角色信息表 sys_user_role ,  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
      throw new Exception("合并帐号->删除用户角色信息表 sys_user_role ,  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
    }
    return true;
  }
}

