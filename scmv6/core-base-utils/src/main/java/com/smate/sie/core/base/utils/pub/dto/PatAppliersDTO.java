package com.smate.sie.core.base.utils.pub.dto;

import java.io.Serializable;

public class PatAppliersDTO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4806776587700123059L;
  private Long applierId;
  private String applierName;

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
