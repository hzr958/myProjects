package com.smate.sie.core.base.utils.service.psn;

import java.util.Map;

import com.smate.core.base.utils.exception.SysServiceException;

/**
 * 关联帐号或取消关联帐号接口服务
 * 
 * @author hd
 *
 */
public interface InsPersonAddorDelRelationService {

  /**
   * 添加关联
   * 
   * @param psnId
   * @param targettoken
   * @return
   * @throws SysServiceException
   */
  public Map<String, Object> addRelation(Long psnId, String targettoken) throws SysServiceException;

  /**
   * 取消关联
   * 
   * @param psnId
   * @param targettoken
   * @return
   * @throws SysServiceException
   */
  public Map<String, Object> delRelation(Long psnId, String targettoken) throws SysServiceException;

  /**
   * 获取单位token.
   * 
   * @param insId
   * @return
   * @throws SysServiceException
   */
  public String getInsToken(Long insId) throws SysServiceException;

}
