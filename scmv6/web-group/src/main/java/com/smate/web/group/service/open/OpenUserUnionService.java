package com.smate.web.group.service.open;


/**
 * open关联服务
 * 
 * @author tsz
 *
 */
public interface OpenUserUnionService {

  /**
   * 根据人员id获取openId
   * 
   * @param psnId
   * @return
   * @throws Exception
   */
  public Long getOpenIdByPsnId(Long psnId) throws Exception;


  /**
   * 构建openId
   * 
   * @return
   * @throws Exception
   */

  public Long buildOpenId(Long psnId, String token) throws Exception;
}
