package com.smate.web.dyn.service.msg;

import com.smate.web.dyn.form.msg.MsgShowForm;

/**
 * 
 * @author Administrator
 *
 */
public interface OptMsgService {

  /**
   * 发送全文请求
   * 
   * @param form
   * @throws Exception
   */
  void sendFulltextRequest(MsgShowForm form) throws Exception;

  /**
   * 成果分享给好友消息
   * 
   * @param form
   * @throws Exception
   */
  void sendPubShareToFriend(MsgShowForm form) throws Exception;

  void dealEmailForPsn(MsgShowForm form) throws Exception;

  void addShareRecord(MsgShowForm form) throws Exception;

  /**
   * 发送纯文本消息
   * 
   * @param form
   * @throws Exception
   */
  void sendTextMsg(MsgShowForm form) throws Exception;

}
