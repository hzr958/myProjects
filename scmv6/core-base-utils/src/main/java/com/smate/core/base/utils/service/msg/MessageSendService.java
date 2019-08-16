package com.smate.core.base.utils.service.msg;

public interface MessageSendService {

  /**
   * 调用发送信息(HTTP接口).
   * 
   * @param destId 目的号码
   * @param content 短息内容
   * @return 返回发送结果码
   * @throws Exception
   */
  public String sendMessage(String destId, String content) throws Exception;
}
