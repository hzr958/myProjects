package com.smate.center.data.service.pub;

/**
 * 成果hash相关服务
 * 
 * @author lhd
 *
 */
public interface PubSimpleHashService {

  /**
   * 获取指定数量的成果哈希
   * 
   * @param size
   * @param startPubId
   * @param endPubId
   * @return
   */
  public Long getPubSimpleHash(Integer size, Long startPubId, Long endPubId) throws Exception;

}
