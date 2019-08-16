package com.smate.center.batch.service.mail;

import java.io.Serializable;
import java.util.Map;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.mail.InsideMailBox;
import com.smate.center.batch.model.sns.pub.Message;



/**
 * 短信Service，Context(应用场景)．
 * 
 * @author pwl
 * 
 */
public interface InsideMessageService extends Serializable {

  /**
   * 只发短消息.
   * 
   * @param message
   * @param msgType
   * @throws ServiceException
   */
  void sendMessage(Message message) throws ServiceException;

  /**
   * 发送短消息和邮件.
   * 
   * @param message
   * @throws ServiceException
   */
  void sendMessageAndMail(Message message) throws ServiceException;

  /**
   * <ul>
   * <li>发送短消息和邮件（支持直接对邮箱的发送，但是没有匹配到系统用户不发送站内消息）.</li>
   * <li>支持邮箱的收件人格式：psnId:'','':email. 只是支持psnId的收件人格式：psnId,psnId.</li>
   * </ul>
   * 
   * @param message
   * @throws ServiceException
   */
  void sendMessageAndMailSupportEmail(Message message) throws ServiceException;

  /**
   * 保存同步的站内消息到收件箱并发送消息.
   * 
   * @param reviever
   * @param insideMailBox
   * @param mailParam
   * @throws ServiceException
   */
  void saveSyncInsideMessageAndSendMail(Long receiverId, InsideMailBox insideMailBox, Map<String, Object> mailParam)
      throws ServiceException;

}
