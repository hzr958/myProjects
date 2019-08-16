package com.smate.center.open.service.login;

import com.smate.center.open.exception.OpenException;
import com.smate.center.open.form.OpenLoginForm;

/**
 * open系统 登录验证服务接口
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public interface ThirdLoginService {
  /**
   * 登录验证 方法
   * 
   * @param form
   */
  public void openLogin(OpenLoginForm form) throws OpenException;

  /**
   * 取第三方系统名称方法 没有正确取出名字 直接跳转没有权限的页面
   * 
   * @param form
   */
  public void getThirdSysNameByToken(OpenLoginForm form) throws OpenException;

  public Long getOpenId(String token, Long psnId, int createType) throws OpenException;

}
