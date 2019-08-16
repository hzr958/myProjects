package com.smate.center.batch.service.mail;

import java.io.Serializable;
import java.util.Map;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.pub.mq.SnsInviteMailMessage;
import com.smate.core.base.utils.model.security.Person;

/**
 * 收件箱-站内邀请业务逻辑实现接口.
 * 
 * @see 功能：邀请的业务流程控制.
 * @author maojianguo
 * 
 */
@SuppressWarnings("javadoc")
public interface InvitationBusinessService extends Serializable {

  // 邮件状态和邮件处理状态(对应invite_inbox表中的邮件状态status和邮件处理状态opt_status参数值).
  final Integer REQUEST_JOIN_INVITEIN_STATUS = 0;
  final Integer REQUEST_JOIN_INVITEIN_OPTSTATUS = 0;

  /**
   * 处理站内邀请发送端的业务逻辑.
   * 
   * @param person 发送发件邀请的人.
   * @param inviteMailParam 发件箱记录所需参数.
   * @param mailParam 邮件及其他相关记录所需参数.
   * @param messageType 操作类型(对应DynamicConstant类中RES_TYPE类型的参数值).
   * @return
   * @throws ServiceException
   */
  void dealSendInviteMessage(Person person, Map<String, Object> inviteMailParam, Map<String, Object> mailParam,
      int messageType) throws ServiceException;

  /**
   * 处理请求信息(存储请求收件信息并发送邀请邮件).
   * 
   * @throws Exception
   */
  void dealInviteMessage(SnsInviteMailMessage inviteMessage) throws ServiceException;

  /**
   * 将参数集合拼装为参数字符串.
   * 
   * @param paramMap
   * @return
   */
  public String assemParamStr(Map<String, String> paramMap);

  /**
   * 将邀请码解析为参数集合.
   * 
   * @param invitationCode 邀请码.
   * @return
   */
  public Map<String, String> splitInviteCode(String invitationCode, String key);
}
