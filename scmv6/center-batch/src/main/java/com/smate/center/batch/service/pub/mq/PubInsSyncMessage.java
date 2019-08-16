package com.smate.center.batch.service.pub.mq;

/**
 * pub-ins同步消息.
 * 
 */
public class PubInsSyncMessage {

  private Long snsPubId;
  private PubInsSyncActionEnum action;
  private Long psnId;
  private Integer publishYear;
  private Integer pubTypeId;
  private String zhTitle;
  private String enTitle;

  public PubInsSyncMessage(Integer fromNodeId, Long toInsId, Long snsPubId, Long psnId, PubInsSyncActionEnum action)
      throws Exception {
    super();
    this.snsPubId = snsPubId;
    this.action = action;
    this.psnId = psnId;
  }

  public Long getSnsPubId() {
    return snsPubId;
  }

  public void setSnsPubId(Long snsPubId) {
    this.snsPubId = snsPubId;
  }

  public PubInsSyncActionEnum getAction() {
    return action;
  }

  public void setAction(PubInsSyncActionEnum action) {
    this.action = action;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Integer getPubTypeId() {
    return pubTypeId;
  }

  public String getZhTitle() {
    return zhTitle;
  }

  public String getEnTitle() {
    return enTitle;
  }

  public void setPubTypeId(Integer pubTypeId) {
    this.pubTypeId = pubTypeId;
  }

  public void setZhTitle(String zhTitle) {
    this.zhTitle = zhTitle;
  }

  public void setEnTitle(String enTitle) {
    this.enTitle = enTitle;
  }

  /**
   * @param publishYear the publishYear to set
   */
  public void setPublishYear(Integer publishYear) {
    this.publishYear = publishYear;
  }

  /**
   * @return the publishYear
   */
  public Integer getPublishYear() {
    return publishYear;
  }

}
