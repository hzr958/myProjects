package com.smate.center.batch.service.pub.pubsubmission;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.pub.mq.PubSubmissionInsSyncMessage;

/**
 * 成果提交模块..
 * 
 * @author Ly
 * 
 */
public interface PubSubmissionService {
  /**
   * 获取已提交成果数.
   * 
   * @param insId
   * @param psnId
   * @return
   * @throws ServiceException
   */
  Long getSubmitTotal(Long insId, Long psnId) throws ServiceException;

  /**
   * 接收单位的成果批准结果.
   * 
   * @param message
   */
  public void receiverPubSubmissionSyncMessage(PubSubmissionInsSyncMessage message) throws ServiceException;
}
