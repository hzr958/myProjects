package com.smate.web.v8pub.dto;

import java.io.Serializable;

public class MemberInsDTO implements Serializable {

  private static final long serialVersionUID = 1L;
  private Long insId;
  private String insName;

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

}
