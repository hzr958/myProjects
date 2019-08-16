package com.smate.center.batch.service.pub;

import java.io.Serializable;

import com.smate.center.batch.exception.pub.ServiceException;

/**
 * 文献同步ROL业务类(接口).
 * 
 * @author WeiLong Peng
 *
 */
public interface PubRefSyncRolService extends Serializable {

  /**
   * 标记需要同步更新到ROL的文献.
   * 
   * @param refId
   * @param isDel
   * @throws Exception
   */
  void markPubRefSync(Long refId, Integer isDel) throws ServiceException;



}
