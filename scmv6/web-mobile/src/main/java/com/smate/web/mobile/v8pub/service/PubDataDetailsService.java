package com.smate.web.mobile.v8pub.service;

/**
 * @description 个人成果是否隐私检查服务接口
 * @author xiexing
 * @date 2019年2月23日
 */
public interface PubDataDetailsService {
  /**
   * 通过成果id来获取对应的所属者id
   * 
   * @param pubId
   * @return
   */
  public Long getOwnerPsnId(Long pubId) throws Exception;

  /**
   * 根据人员配置id和成果id检查当前成果是否隐私
   * 
   * @param id
   * @return
   */
  public Integer getAnyUser(Long pubId, Long cnfId) throws Exception;

  /**
   * 根据人员id获取配置id
   * 
   * @param psnId
   * @return
   */
  public Long getCnfId(Long psnId);
}
