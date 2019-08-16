package com.smate.web.v8pub.dom;

import java.io.Serializable;

public class ScienceAreaBean implements Serializable {

  private static final long serialVersionUID = -6829676425926065481L;

  private Long pubId; // 成果id
  private Integer scienceAreaId; // 科技领域Id


  public Integer getScienceAreaId() {
    return scienceAreaId;
  }

  public void setScienceAreaId(Integer scienceAreaId) {
    this.scienceAreaId = scienceAreaId;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }
}
