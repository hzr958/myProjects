package com.smate.center.batch.service.pdwh.pubimport;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPubAssign;


/**
 * 基准库成果服务接口
 * 
 * @author zll
 *
 */
public interface PdwhPubAssignService {

  /**
   * 
   * 查询pubCacheAssign所需数据,包含xml
   * 
   * @param pubId,insId
   * 
   */

  PdwhPubAssign getPdwhPubAssign(Long pubId, Long insId);


  /**
   * 发送单位成果.
   * 
   * @param pdwhAssign
   * @param dbid
   * @throws ServiceException
   */

  void sendInsPub(PdwhPubAssign pdwhAssign, Integer dbid);

}
