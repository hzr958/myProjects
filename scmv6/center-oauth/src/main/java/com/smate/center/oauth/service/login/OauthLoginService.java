package com.smate.center.oauth.service.login;

import com.smate.center.oauth.exception.OauthException;
import com.smate.core.base.oauth.model.OauthLoginForm;

/**
 * oauth系统 登录验证服务接口
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public interface OauthLoginService {
  /**
   * 登录验证 方法
   * 
   * @param form
   */
  public void oauthLogin(OauthLoginForm form) throws OauthException;

  /**
   * 重新 构造重定向 目标地址 加session id参数
   * 
   * @param token
   * @return
   */
  public String oauthRebuildTargetUrl(String token);

  /**
   * 构造重定向目标地址
   * 
   * @param url
   * @param sessionId
   * @return
   */
  public String oauthRebuildTargetUrl(String url, String sessionId);

  /**
   * 获取AID
   * 
   * @param openId
   * @return
   */
  public String getAutoLoginAID(Long openId, String autoLoginType);

  /**
   * 获取人员openId
   * 
   * @param token
   * @param psnId
   * @param createType
   * @return
   */
  public Long getOpenId(String token, Long psnId, int createType);

  /**
   * 更新自动登录的使用次数
   * 
   * @param aid
   */
  public void updateAutoLoinUserTime(String aid);

  /**
   * @param psnId 人员id
   * @param remoteIp 远程ip
   * @param sys 来源系统
   * @param type 登录类型
   */
  public void saveAutoLoginLog(Long psnId, String remoteIp, String sys, int type);

  /**
   * 校验手机验证码登录参数
   * 
   * @param form
   * @return
   */
  public boolean checkMobileCodeLogin(OauthLoginForm form);

  /**
   * 登录验证 方法
   * 
   * @param form
   */
  public void checkOauthLogin(OauthLoginForm form) throws OauthException;
}
