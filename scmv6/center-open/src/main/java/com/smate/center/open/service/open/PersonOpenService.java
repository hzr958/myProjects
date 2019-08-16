package com.smate.center.open.service.open;

/**
 * 
 * personOpen服务接口
 * 
 * @author tsz
 *
 */
public interface PersonOpenService {

  /**
   * 创建openid
   * 
   * @param psnId
   * @throws Exception
   */
  public Long createOpenId(Long psnId) throws Exception;

  /**
   * 获取 openid
   * 
   * @param psnId
   * @return
   * @throws Exception
   */
  public Long getOpenIdByPsnId(Long psnId) throws Exception;

}
