package com.smate.core.base.utils.service.security;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.dao.security.AutoLoginOauthInfoDao;
import com.smate.core.base.utils.dao.security.SysUserLoginLogDao;
import com.smate.core.base.utils.model.cas.security.AutoLoginOauthInfo;
import com.smate.core.base.utils.model.cas.security.SysUserLoginLog;
import com.smate.core.base.utils.string.SystemUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;

/**
 * 自动登录 服务实现
 * 
 * @author tsz
 *
 */
@Service("autoLoginOauthInfoService")
@Transactional(rollbackFor = Exception.class)
public class AutoLoginOauthInfoServiceImpl implements AutoLoginOauthInfoService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private AutoLoginOauthInfoDao autoLoginOauthInfoDao;
  @Autowired
  private SysUserLoginLogDao sysUserLoginLogDao;

  @Override
  public String checkAutoLoginOauth(String AID) {

    Date currentDate = new Date();
    AutoLoginOauthInfo autoInfo = autoLoginOauthInfoDao.getAutoLoginOauthInfoByTime(AID, currentDate);
    if (autoInfo != null) {
      return autoInfo.getPsnId() + "," + autoInfo.getLoginType();
    }
    return null;
  }

  /**
   * 更新自动登录的使用时间和次数
   */
  @Override
  public void updateAutoLoinUserTime(String AID) {
    Date currentDate = new Date();
    AutoLoginOauthInfo autoInfo = autoLoginOauthInfoDao.getAutoLoginOauthInfoByTime(AID, currentDate);
    if (autoInfo != null) {
      autoInfo.setUseTimes(autoInfo.getUseTimes() + 1);
      autoInfo.setLastUseTime(currentDate);
      autoLoginOauthInfoDao.save(autoInfo);
    }
  }

  /**
   * @param psnId 人员id
   * @param remoteIp 远程ip
   * @param sys 来源系统
   * @param type 登录类型
   */

  @Override
  public void saveAutoLoginLog(Long psnId, String remoteIp, String sys, int type) {
    // sys 去掉来源带的参数
    if (StringUtils.isNotBlank(sys)) {
      int index = sys.indexOf("?");
      if (index > 0) {
        sys = sys.substring(0, index);
      }
    }
    SysUserLoginLog sysUserLoginLog = new SysUserLoginLog(psnId, new Date(), remoteIp, sys, type);
    HttpServletRequest request = Struts2Utils.getRequest();
    sysUserLoginLog.setBrowserInfo(SystemUtils.getRequestBrowserInfo(request));
    sysUserLoginLog.setSystemInfo(SystemUtils.getRequestSystemInfo(request));
    sysUserLoginLogDao.save(sysUserLoginLog);
  }

  @Override
  public void saveAutoLoginLogWithReq(HttpServletRequest request, Long psnId, String remoteIp, String sys, int type) {
    // sys 去掉来源带的参数
    if (StringUtils.isNotBlank(sys)) {
      int index = sys.indexOf("?");
      if (index > 0) {
        sys = sys.substring(0, index);
      }
    }
    SysUserLoginLog sysUserLoginLog = new SysUserLoginLog(psnId, new Date(), remoteIp, sys, type);
    sysUserLoginLog.setBrowserInfo(SystemUtils.getRequestBrowserInfo(request));
    sysUserLoginLog.setSystemInfo(SystemUtils.getRequestSystemInfo(request));
    sysUserLoginLogDao.save(sysUserLoginLog);
  }

  /**
   * 让AID失效
   * 
   * @param AID
   */
  @Override
  public void invalidateAid(String AID) {
    AutoLoginOauthInfo autoInfo = autoLoginOauthInfoDao.get(AID);
    if (autoInfo != null) {
      autoInfo.setOverdueTime(new Date());
      autoLoginOauthInfoDao.save(autoInfo);
    }
  }
}
