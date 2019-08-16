package com.smate.center.batch.service.pub.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.batch.constant.PubAssignSyncMessageEnum;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rcmd.pub.PubConfirmRolPub;
import com.smate.center.batch.model.sns.pub.PubAssignSyncMessage;
import com.smate.center.batch.model.sns.pub.PubDupFields;
import com.smate.core.base.utils.security.SecurityUtils;


/**
 * 成果指派，删除指派MQ消息，包括成果XML.
 * 
 * @author liqinghua
 * 
 */
@Component("pubAssignSyncMessageProducer")
public class PubAssignSyncMessageProducer {

  /**
   * 
   */
  private static final long serialVersionUID = -6381283385616778111L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private final String queueName = "pubAssignSync";

  @Autowired
  private PubAssignSyncMessageConsumer pubAssignSyncMessageConsumer;

  /**
   * 发布成果指派数据.
   * 
   * @param psnId
   * @param insId
   * @param assignId
   * @param insPubId
   * @param seqNo
   * @param detail
   * @param assignScore
   * @throws ServiceException
   */
  public void publishAssign(Long psnId, Long insId, Long assignId, Long insPubId, Long pmId, Integer seqNo,
      PubConfirmRolPub detail, PubDupFields dupFields, Float assignScore, int isSaveRcmdPubConfirm)
      throws ServiceException {
    try {
      Integer nodeId = 0;
      PubAssignSyncMessage msg = new PubAssignSyncMessage(psnId, insId, assignId, insPubId, detail,
          PubAssignSyncMessageEnum.ASSIGN, pmId, seqNo, nodeId);
      msg.setPubDupFields(dupFields);
      msg.setAssignScore(assignScore);
      msg.setIsSaveRcmdPubConfirm(isSaveRcmdPubConfirm);
      this.pubAssignSyncMessageConsumer.receive(msg);
    } catch (Exception e) {
      logger.error("发布成果指派XML错误.", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 发布成果指派同步XML失败.
   * 
   * @param psnId
   * @param insId
   * @param insPubId
   * @param errorType 1单位删除了指派关系 、2成果人员被删除
   * @throws ServiceException
   */
  public void publishAssignError(Long psnId, Long insId, Long insPubId, Integer errorType) throws ServiceException {
    try {
      Integer nodeId = 0;
      PubAssignSyncMessage msg = new PubAssignSyncMessage(psnId, insId, insPubId,
          PubAssignSyncMessageEnum.SYC_ASSIGN_XML_ERROR, errorType, nodeId);
      this.pubAssignSyncMessageConsumer.receive(msg);
    } catch (Exception e) {
      logger.error("发布成果指派XML错误.", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 发布成果指派XML.
   * 
   * @throws ServiceException
   */
  public void publishAssignXml(Long psnId, Long insId, Long insPubId, String pubXml) throws ServiceException {
    try {
      Integer nodeId = 0;
      PubAssignSyncMessage msg =
          new PubAssignSyncMessage(psnId, insId, insPubId, pubXml, PubAssignSyncMessageEnum.SYNC_ASSIGN_XML, nodeId);
      this.pubAssignSyncMessageConsumer.receive(msg);
    } catch (Exception e) {
      logger.error("发布成果指派XML错误.", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 发布删除成果指派.
   * 
   * @throws ServiceException
   */
  public void publishDisAssign(Long psnId, Long insId, Long assignId, Long insPubId) throws ServiceException {
    try {
      Integer nodeId = 0;
      PubAssignSyncMessage msg =
          new PubAssignSyncMessage(psnId, insId, assignId, insPubId, PubAssignSyncMessageEnum.DIS_ASSIGN, nodeId);
      this.pubAssignSyncMessageConsumer.receive(msg);
    } catch (Exception e) {
      logger.error("发布删除成果指派错误.", e);
      throw new ServiceException(e);
    }
  }

}
