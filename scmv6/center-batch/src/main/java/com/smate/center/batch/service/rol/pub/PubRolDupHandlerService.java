package com.smate.center.batch.service.rol.pub;

import com.smate.center.batch.exception.pub.ServiceException;


/**
 * 异步处理成果查重服务.
 * 
 * @author yamingd
 * 
 */
public interface PubRolDupHandlerService {

  /**
   * 进行查重.
   * 
   * @param pubId
   * @throws ServiceException
   */
  void check(long insId, long pubId) throws ServiceException;

  /**
   * 把还在队里的成果放入MQ
   * 
   * @throws ServiceException
   */
  void loadQueuePubsIntoMQ() throws ServiceException;

  /**
   * 查重，并删除临时库的重复成果
   * 
   * @param insId
   * @param pubId
   * @throws ServiceException
   */
  void deleteDupTempPub(long insId, long pubId) throws ServiceException;
}
