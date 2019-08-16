package com.smate.center.task.model.bdsp;

import com.smate.center.task.model.snsbak.bdsp.BdspProject;

public class BdspDataForm {

  private BdspProject bdspProject;

  private Long insId;

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public BdspProject getBdspProject() {
    return bdspProject;
  }

  public void setBdspProject(BdspProject bdspProject) {
    this.bdspProject = bdspProject;
  }

}
