package com.smate.core.base.app.service;

import java.util.Date;

import com.smate.core.base.app.model.AppAuthToken;

public interface AppAuthTokenService {
  /**
   * 保存
   * 
   * @param auth
   */
  public void saveToken(AppAuthToken auth);

  /**
   * 获取token
   * 
   * @param psnId
   */
  public AppAuthToken getToken(Long psnId);

  /**
   * 获取token
   * 
   * @param psnId
   */
  public AppAuthToken getToken(Long psnId, String token);

  /**
   * 更新token和生效日期
   * 
   * @param psnId
   */
  public void updateToken(Long psnId, String token, Date date);

  /**
   * 创建
   * 
   * @param psnId
   * @return
   */
  public String createToken(Long psnId);

}
