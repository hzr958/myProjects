package com.smate.sie.core.base.utils.pub.dom;

import java.io.Serializable;

public class PatAppliersBean implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4806776587700123059L;
  private Long seqNo;
  private Long applierId;
  private String applierName = new String();

  public Long getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Long seqNo) {
    this.seqNo = seqNo;
  }

  public Long getApplierId() {
    return applierId;
  }

  public String getApplierName() {
    return applierName;
  }

  public void setApplierId(Long applierId) {
    this.applierId = applierId;
  }

  public void setApplierName(String applierName) {
    this.applierName = applierName;
  }

}
