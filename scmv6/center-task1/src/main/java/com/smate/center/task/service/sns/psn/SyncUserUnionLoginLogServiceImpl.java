package com.smate.center.task.service.sns.psn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.open.OpenUserUnionDao;
import com.smate.center.task.dao.sns.psn.UserUnionLoginLogDao;
import com.smate.center.task.model.sns.psn.UserUnionLoginLog;
import com.smate.core.base.utils.dao.security.SysUserLoginDao;
import com.smate.core.base.utils.model.wechat.OpenUserUnion;

@Service("syncUserUnionLoginLogService")
@Transactional(rollbackFor = Exception.class)
public class SyncUserUnionLoginLogServiceImpl implements SyncUserUnionLoginLogService {
  @Autowired
  private SysUserLoginDao sysUserLoginDao;
  @Autowired
  private OpenUserUnionDao upenUserUnionDao;
  @Autowired
  private UserUnionLoginLogDao userUnionLoginLogDao;

  @Override
  public List<Long> getLoginPsnIds(Long lastPsnId) {

    return sysUserLoginDao.getLoginPsnIds(lastPsnId);
  }

  @Override
  public List<Long> getUnionPsnId() {
    return upenUserUnionDao.getUnionPsnId();
  }

  @Override
  public void saveUserUnionLoginLog(List<Long> psnIdList, boolean isLogin) {
    for (Long psnId : psnIdList) {
      UserUnionLoginLog userLog = userUnionLoginLogDao.get(psnId);
      if (userLog == null) {
        userLog = new UserUnionLoginLog(psnId);
      }
      if (isLogin) {// 登录过
        userLog.setIsLogin(1);
        // 判断是否有关联其他业务系统
        List<OpenUserUnion> OpenUserUnionList = upenUserUnionDao.getOpenUserUnion(psnId);
        if (OpenUserUnionList != null && OpenUserUnionList.size() > 0) {
          userLog.setIsUnion(1);
          userLog.setPsnFundScore(5);
        } else {
          userLog.setPsnFundScore(2);
        }
      } else {// 未登录过
        userLog.setIsUnion(1);
        userLog.setPsnFundScore(3);
      }
      userUnionLoginLogDao.saveOrUpdate(userLog);
    }
  }


}
