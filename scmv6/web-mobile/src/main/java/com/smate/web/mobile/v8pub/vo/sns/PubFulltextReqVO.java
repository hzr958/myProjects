package com.smate.web.mobile.v8pub.vo.sns;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;

public class PubFulltextReqVO {
  /**
   * 请求人员id
   */
  private Long reqPsnId;

  /**
   * 被请求人员id
   */
  private Long recvPsnId;

  /**
   * 请求全文的成果id
   */
  private Long pubId;

  /**
   * 请求处理状态标志，1--同意, 2--忽略/拒绝
   */
  private Integer dealStatus;

  private String des3RecvPsnId;

  private String des3PubId;

  /**
   * 成果类型，个人成果还是基准库成果。值为: sns/pdwh
   */
  private String pubType;

  /**
   * 全文请求记录id，只有在全文请求保存后才会有值
   */
  private Long reqId;

  private Long msgId;

  private String des3MsgId;
  private Long currentPsnId; // 当前操作人员ID
  private String des3CurrentPsnId; // 加密的当前操作人员ID

  public Long getReqPsnId() {
    if (reqPsnId == null || reqPsnId == 0L) {
      this.reqPsnId = SecurityUtils.getCurrentUserId();
    }
    return reqPsnId;
  }

  public void setReqPsnId(Long reqPsnId) {
    this.reqPsnId = reqPsnId;
  }

  public Long getRecvPsnId() {
    if ((recvPsnId == null || recvPsnId == 0L) && StringUtils.isNotBlank(des3RecvPsnId)) {
      this.recvPsnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(this.des3RecvPsnId));
    }
    return recvPsnId;
  }

  public void setRecvPsnId(Long recvPsnId) {
    this.recvPsnId = recvPsnId;
  }

  public Long getPubId() {
    if ((pubId == null || pubId == 0L) && StringUtils.isNotBlank(des3PubId)) {
      this.pubId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3PubId));
    }
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getDes3RecvPsnId() {
    return des3RecvPsnId;
  }

  public void setDes3RecvPsnId(String des3RecvPsnId) {
    this.des3RecvPsnId = des3RecvPsnId;
  }

  public String getDes3PubId() {
    return des3PubId;
  }

  public void setDes3PubId(String des3PubId) {
    this.des3PubId = des3PubId;
  }

  public String getPubType() {
    return pubType;
  }

  public void setPubType(String pubType) {
    this.pubType = pubType;
  }

  public Long getReqId() {
    return reqId;
  }

  public void setReqId(Long reqId) {
    this.reqId = reqId;
  }

  public Integer getDealStatus() {
    return dealStatus;
  }

  public void setDealStatus(Integer dealStatus) {
    this.dealStatus = dealStatus;
  }

  public Long getMsgId() {
    if ((msgId == null || msgId == 0L) && StringUtils.isNotBlank(des3MsgId)) {
      msgId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3MsgId));
    }
    return msgId;
  }

  public void setMsgId(Long msgId) {
    this.msgId = msgId;
  }

  public String getDes3MsgId() {
    return des3MsgId;
  }

  public void setDes3MsgId(String des3MsgId) {
    this.des3MsgId = des3MsgId;
  }

  public Long getCurrentPsnId() {
    if ((this.currentPsnId == null || this.currentPsnId == 0L) && StringUtils.isNotBlank(this.des3CurrentPsnId)) {
      msgId = NumberUtils.toLong(Des3Utils.decodeFromDes3(this.des3CurrentPsnId));
    }
    return currentPsnId;
  }

  public void setCurrentPsnId(Long currentPsnId) {
    this.currentPsnId = currentPsnId;
  }

  public String getDes3CurrentPsnId() {
    return des3CurrentPsnId;
  }

  public void setDes3CurrentPsnId(String des3CurrentPsnId) {
    this.des3CurrentPsnId = des3CurrentPsnId;
  }
}
