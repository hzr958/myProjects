package com.smate.center.task.model.sns.quartz;

/**
 * 待认领成果数消息.
 * 
 * @author zjh
 * 
 */
public class PendingCfmPubNumMessage {

  /**
   * 
   */
  private static final long serialVersionUID = 8111283434919321937L;
  private Long psnId;
  private Integer pcfPubNum;

  public PendingCfmPubNumMessage() {
    super();
  }

  public PendingCfmPubNumMessage(Long psnId, Integer pcfPubNum, Integer fromNodeId) {
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
