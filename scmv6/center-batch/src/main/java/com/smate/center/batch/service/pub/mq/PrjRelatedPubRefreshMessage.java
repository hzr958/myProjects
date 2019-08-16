package com.smate.center.batch.service.pub.mq;


/**
 * 项目相关成果刷新同步消息.
 * 
 * @author xys
 * 
 */
public class PrjRelatedPubRefreshMessage {


  private Long prjId;
  private Long pubId;
  private Long psnId;
  private Integer refreshSource;
  private Integer status = 0;

  public PrjRelatedPubRefreshMessage() {
    super();
  }

  public PrjRelatedPubRefreshMessage(Integer fromNodeId, Long prjId, Long pubId, Long psnId, Integer refreshSource,
      Integer status) {
    super();
    this.prjId = prjId;
    this.pubId = pubId;
    this.psnId = psnId;
    this.refreshSource = refreshSource;
    this.status = status;
  }

  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Integer getRefreshSource() {
    return refreshSource;
  }

  public void setRefreshSource(Integer refreshSource) {
    this.refreshSource = refreshSource;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
