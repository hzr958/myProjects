package com.smate.web.dyn.form.msg;


/**
 * 消息邮件表单
 * 
 * @author aijiangbin
 *
 */
public class MsgEmailForm {

  /**
   * 消息类型， file ， pub ，text
   */
  public String type = "";
  /**
   * 文本内容
   */
  public String content = "";
  /**
   * 发送者头像
   */
  public String avatars = "";
  /**
   * 发送者姓名
   */
  public String sendPsnName = "";
  /**
   * 发送者url
   */
  public String sendPsnUrl = "";

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getAvatars() {
    return avatars;
  }

  public void setAvatars(String avatars) {
    this.avatars = avatars;
  }

  public String getSendPsnName() {
    return sendPsnName;
  }

  public void setSendPsnName(String sendPsnName) {
    this.sendPsnName = sendPsnName;
  }

  public String getSendPsnUrl() {
    return sendPsnUrl;
  }

  public void setSendPsnUrl(String sendPsnUrl) {
    this.sendPsnUrl = sendPsnUrl;
  }



}
