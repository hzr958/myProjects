package com.smate.core.base.utils.service.security;

import javax.servlet.http.HttpServletRequest;

/**
 * 自动登录信息 服务接口
 * 
 * @author tsz
 *
 */
public interface AutoLoginOauthInfoService {

  /**
   * 判断 自动登录 有没有过期
   * 
   * @param AID
   * @return psnId,token
   */
  public String checkAutoLoginOauth(String AID);

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
   * 上面的保存方法中getRequest() 方法有些地方调用的话会报错， 所以新弄了一个带request参数的方法
   * 
   * @param request
   * @param psnId
   * @param remoteIp
   * @param sys
   * @param type
   */
  public void saveAutoLoginLogWithReq(HttpServletRequest request, Long psnId, String remoteIp, String sys, int type);

  /**
   * 让AID失效
   * 
   * @param AID
   */
  public void invalidateAid(String AID);
}
