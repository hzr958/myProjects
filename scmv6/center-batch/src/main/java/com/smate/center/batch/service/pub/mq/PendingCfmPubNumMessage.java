package com.smate.center.batch.service.pub.mq;

/**
 * 待认领成果数消息.
 * 
 * @author liqinghua
 * 
 */
public class PendingCfmPubNumMessage {

  private Long psnId;
  private Integer pcfPubNum;

  public PendingCfmPubNumMessage() {
    super();
  }

  public PendingCfmPubNumMessage(Long psnId, Integer pcfPubNum) {
    super();
    this.psnId = psnId;
    this.pcfPubNum = pcfPubNum;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Integer getPcfPubNum() {
    return pcfPubNum;
  }

  public void setPcfPubNum(Integer pcfPubNum) {
    this.pcfPubNum = pcfPubNum;
  }

}
