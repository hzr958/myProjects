package com.smate.web.v8pub.dto;

import java.io.Serializable;

public class ScienceAreaDTO implements Serializable {
  public ScienceAreaDTO() {}

  private static final long serialVersionUID = -6829676425926065481L;

  private Long pubId; // 成果id
  private Long scienceAreaId; // 科技领域Id
  public String scienceAreaName; // 科技领域名
  public String scienceAreaName_En; // 科技领域英文名

  public ScienceAreaDTO(Long scienceAreaId, String scienceAreaName, String scienceAreaName_En) {
    super();
    this.scienceAreaId = scienceAreaId;
    this.scienceAreaName = scienceAreaName;
    this.scienceAreaName_En = scienceAreaName_En;
  }

  public Long getScienceAreaId() {
    return scienceAreaId;
  }

  public void setScienceAreaId(Long scienceAreaId) {
    this.scienceAreaId = scienceAreaId;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getScienceAreaName() {
    return scienceAreaName;
  }

  public void setScienceAreaName(String scienceAreaName) {
    this.scienceAreaName = scienceAreaName;
  }

  public String getScienceAreaName_En() {
    return scienceAreaName_En;
  }

  public void setScienceAreaName_En(String scienceAreaName_En) {
    this.scienceAreaName_En = scienceAreaName_En;
  }


}
