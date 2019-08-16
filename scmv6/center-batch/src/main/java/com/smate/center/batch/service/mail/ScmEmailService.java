package com.smate.center.batch.service.mail;

import java.io.Serializable;
import java.util.Map;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.PsnResSend;
import com.smate.core.base.utils.model.security.Person;


/**
 * 科研之友发送邮件service.
 * 
 * @author pwl
 * 
 */
public interface ScmEmailService extends Serializable {

  /**
   * 发送站内邮件.
   * 
   * @param receiverId
   * @param mailParam
   * @throws ServiceException
   */
  public void sendInsideMail(Long receiverId, Map<String, Object> mailParam) throws ServiceException;

  /**
   * 发送应用推广类邮件给站外用户.
   * 
   * @param email
   * @param mailParam
   * @throws ServiceException
   */
  public void sendAppTypeMail(String email, Map<String, Object> mailParam) throws ServiceException;

  /**
   * 发送分享成果邮件.
   * 
   * @param receiverId
   * @param email
   * @param languageVersion
   * @param resRecId
   * @param psnResSend
   * @throws ServiceException
   */
  public void sendSharePubMail(Long receiverId, String email, String languageVersion, Long resRecId,
      PsnResSend psnResSend) throws ServiceException;

  /**
   * 发送分享文件邮件.
   * 
   * @param receiverId
   * @param email
   * @param languageVersion
   * @param resRecId
   * @param psnResSend
   * @throws ServiceException
   */
  public void sendShareFileMail(Long receiverId, String email, String languageVersion, Long resRecId,
      PsnResSend psnResSend) throws ServiceException;

  /**
   * 获取用户接收邮件的语言.
   * 
   * @param person
   * @return
   * @throws ServiceException
   */
  public String getReceiverLanguage(Person person) throws ServiceException;
}
