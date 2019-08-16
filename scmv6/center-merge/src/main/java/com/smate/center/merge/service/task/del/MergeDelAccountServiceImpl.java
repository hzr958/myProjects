package com.smate.center.merge.service.task.del;

import com.smate.center.merge.dao.account.AccountDao;
import com.smate.center.merge.model.cas.account.Account;
import com.smate.center.merge.model.sns.task.AccountsMergeData;
import com.smate.center.merge.service.task.MergeBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * 删除帐号服务(sys_user).
 * 
 * @author tsz
 *
 * @date 2018年9月11日
 */
@Transactional(rollbackFor = Exception.class)
public class MergeDelAccountServiceImpl extends MergeBaseService {
  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private AccountDao accountDao;

  @Override
  public boolean checkRun(Long savePsnId, Long delPsnId) throws Exception {
    Account account = accountDao.get(delPsnId);
    if (account != null) {
      return true;
    }
    return false;
  }

  @Override
  public boolean dealMerge(Long savePsnId, Long delPsnId) throws Exception {
    try {
      Account account = accountDao.get(delPsnId);
      String desc = "删除帐号信息  sys_user ";
      AccountsMergeData accountsMergeData =
          super.saveAccountsMergeData(savePsnId, delPsnId, desc, super.DEAL_TYPE_DEL, account);
      accountDao.delete(account);
      super.updateAccountsMergeDataStatus(accountsMergeData);
    } catch (Exception e) {
      logger.error("合并帐号->删除个人信息出错 sys_user ,  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
      throw new Exception("合并帐号->删除个人信息出错 sys_user ,  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
    }
    return true;
  }
}
