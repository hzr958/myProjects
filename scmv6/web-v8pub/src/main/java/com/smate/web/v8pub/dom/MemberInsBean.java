package com.smate.web.v8pub.dom;

import java.io.Serializable;

public class MemberInsBean implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -649709047963514461L;
  private Long insId;
  private String insName = new String();


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
