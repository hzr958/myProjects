package com.smate.web.v8pub.service.fulltext;

import java.util.Map;

import com.smate.core.base.pub.enums.PubFullTextReqStatusEnum;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.vo.PubFulltextReqVO;

/**
 * 成果全文请求记录服务类
 * 
 * @author yhx
 *
 */
public interface PubFullTextReqService {
  /**
   * 全文请求
   * 
   * @param req
   * @throws ServiceException
   */
  Map<String, Object> dealRequest(PubFulltextReqVO req) throws ServiceException;

  /**
   * 发送全文请求站内信
   * 
   * @param req
   * @return
   * @throws ServiceException
   */
  Long sendFullTextRequestMsg(PubFulltextReqVO req) throws ServiceException;

  /**
   * 更新全文请求处理状态
   * 
   * @param msgRelationId
   * @param pubId
   * @param dealStatus
   * @throws ServiceException
   */
  void updateStatus(Long msgRelationId, Long pubId, PubFullTextReqStatusEnum dealStatus, Long currentPsnId)
      throws ServiceException;

  Long getPubOwnerPsnId(PubFulltextReqVO req);
}
