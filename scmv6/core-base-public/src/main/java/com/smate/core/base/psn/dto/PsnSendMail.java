package com.smate.core.base.psn.dto;

import java.io.Serializable;

/**
 * 邮件发送
 * 
 * @author lhd
 *
 */
public class PsnSendMail implements Serializable {

  private static final long serialVersionUID = 985971939510232000L;
  private String psnId;
  // private String sendPsnId;// 收件人psnId
  private String receiverId;// 收件人psnId
  private String sendPsnName;// 收件人名称
  private String sendPsnTitle;
  private String email;
  private String msg;
  private String des3SendPsnId;
  private String isSend;

  public String getPsnId() {
    return psnId;
  }

  public void setPsnId(String psnId) {
    this.psnId = psnId;
  }

  public String getSendPsnName() {
    return sendPsnName;
  }

  public void setSendPsnName(String sendPsnName) {
    this.sendPsnName = sendPsnName;
  }

  public String getSendPsnTitle() {
    return sendPsnTitle;
  }

  public void setSendPsnTitle(String sendPsnTitle) {
    this.sendPsnTitle = sendPsnTitle;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public String getDes3SendPsnId() {
    return des3SendPsnId;
  }

  public void setDes3SendPsnId(String des3SendPsnId) {
    this.des3SendPsnId = des3SendPsnId;
  }

  public String getIsSend() {
    return isSend;
  }

  public void setIsSend(String isSend) {
    this.isSend = isSend;
  }

  public String getReceiverId() {
    return receiverId;
  }

  public void setReceiverId(String receiverId) {
    this.receiverId = receiverId;
  }

}
