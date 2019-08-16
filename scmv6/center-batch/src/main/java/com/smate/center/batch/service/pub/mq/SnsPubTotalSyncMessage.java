package com.smate.center.batch.service.pub.mq;


/**
 * 个人成果数量同步到单位.
 * 
 * @author liqinghua
 * 
 */
public class SnsPubTotalSyncMessage {

  /**
   * 
   */

  private Long psnId;
  private Long insId;
  private Long total;
  private Long submitTotal;

  public SnsPubTotalSyncMessage() {
    super();
  }

  public SnsPubTotalSyncMessage(Long psnId, Long insId, Long total, Long submitTotal, Integer fromNodeId) {
    this.psnId = psnId;
    this.insId = insId;
    this.total = total;
    this.submitTotal = submitTotal;
  }

  public Long getPsnId() {
    return psnId;
  }

  public Long getInsId() {
    return insId;
  }

  public Long getTotal() {
    return total;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setTotal(Long total) {
    this.total = total;
  }

  public Long getSubmitTotal() {
    return submitTotal;
  }

  public void setSubmitTotal(Long submitTotal) {
    this.submitTotal = submitTotal;
  }

}
