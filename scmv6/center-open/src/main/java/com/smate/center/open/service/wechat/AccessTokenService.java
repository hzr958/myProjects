package com.smate.center.open.service.wechat;

import com.smate.center.open.exception.OpenException;

/**
 * 接口调用凭据服务.
 * 
 * @author xys
 *
 */
public interface AccessTokenService {

  /**
   * 获取公众号的全局唯一票据.
   * 
   * @author ChuanjieHou
   * @date 2017年9月13日
   * @param type
   * @return
   * @throws OpenException
   */
  public String getAccessToken(String type) throws OpenException;

  public String getAccessToken() throws OpenException;
}
