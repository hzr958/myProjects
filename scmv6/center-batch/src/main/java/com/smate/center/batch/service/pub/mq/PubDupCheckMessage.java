package com.smate.center.batch.service.pub.mq;

/**
 * 成果查重消息.
 * 
 * @author yamingd
 * 
 */
public class PubDupCheckMessage extends BaseLocalQueneMessage {

  /**
   * 
   */
  private static final long serialVersionUID = -6569008287573197665L;

  private Long insId;
  private Long pubId;

  public PubDupCheckMessage() {
    super();
  }

  /**
   * 构造函数.
   * 
   * @param insId 单位ID
   * @param pubId 单位成果ID
   * @param opPsnId 操作人.
   */
  public PubDupCheckMessage(Long insId, Long pubId, Long opPsnId) {
    super(opPsnId);
    this.insId = insId;
    this.pubId = pubId;
  }

  /**
   * @return the insId
   */
  public Long getInsId() {
    return insId;
  }

  /**
   * @param insId the insId to set
   */
  public void setInsId(Long insId) {
    this.insId = insId;
  }

  /**
   * @return the pubId
   */
  public Long getPubId() {
    return pubId;
  }

  /**
   * @param pubId the pubId to set
   */
  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }
}
