package com.smate.web.v8pub.dto;

import com.smate.core.base.utils.security.Des3Utils;

import java.io.Serializable;

public class PubCitationsDTO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private Long des3PubId; // 加密的pubId
  private String sourceDbId; // 引用的机构dbId
  private Integer citations; // 引用次数
  private Integer citedType; // 引用更新类型

  public Long getDes3PubId() {
    return des3PubId;
  }

  public void setDes3PubId(Long des3PubId) {
    this.des3PubId = des3PubId;
  }

  public Integer getCitations() {
    return citations;
  }

  public void setCitations(Integer citations) {
    this.citations = citations;
  }

  public Integer getCitedType() {
    return citedType;
  }

  public void setCitedType(Integer citedType) {
    this.citedType = citedType;
  }

  public String getSourceDbId() {
    return sourceDbId;
  }

  public void setSourceDbId(String sourceDbId) {
    this.sourceDbId = sourceDbId;
  }

  public static void main(String[] args) {
    System.out.println(Des3Utils.encodeToDes3("0"));
  }
}
