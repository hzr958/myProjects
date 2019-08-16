package com.smate.center.batch.service.pub.mq;



/**
 * 成果提交，成果同步接收成果批准状态.
 * 
 * @author LY
 * 
 */
public class PubSubmissionInsSyncMessage {

  private Long psnId;
  private Long insId;
  private Long pubId;
  private Integer nodeId;
  // 单位：批准成果1，拒绝成果2，拒绝申请撤销3，同意申请撤销4
  // 个人：申请撤销成果5,撤销申请撤销6
  private Integer status;

  public PubSubmissionInsSyncMessage() {
    super();
  }

  public PubSubmissionInsSyncMessage(Long psnId, Long insId, Long pubId, Integer status) {
    super();
    this.psnId = psnId;
    this.insId = insId;
    this.pubId = pubId;
    this.status = status;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getNodeId() {
    return nodeId;
  }

  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public Long getInsId() {
    return insId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

}
