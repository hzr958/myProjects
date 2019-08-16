package com.smate.web.v8pub.service.oauth;


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

}
