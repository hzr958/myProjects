package com.smate.core.base.utils.service.msg;

/**
 * @author aijiangbin
 * @create 2018-12-18 10:17
 **/
public class MobileMessageForm {
  public Long smsCode;
  public String destId; //手机号 ，必填
  public String content;//发送的内容，必填  例如：【科研之友】验证码:658741，10分钟内有效。
  public Integer priority;
  public Long smsType; //消息类型
  public Long psnId; //用户id

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getSmsType() {
    return smsType;
  }

  public void setSmsType(Long smsType) {
    this.smsType = smsType;
  }

  public Long getSmsCode() {
    return smsCode;
  }

  public void setSmsCode(Long smsCode) {
    this.smsCode = smsCode;
  }

  public String getDestId() {
    return destId;
  }

  public void setDestId(String destId) {
    this.destId = destId;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Integer getPriority() {
    return priority;
  }

  public void setPriority(Integer priority) {
    this.priority = priority;
  }
}
