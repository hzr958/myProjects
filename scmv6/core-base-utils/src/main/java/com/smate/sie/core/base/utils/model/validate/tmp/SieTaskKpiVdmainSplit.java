package com.smate.sie.core.base.utils.model.validate.tmp;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author ztg
 *
 */
@Entity
@Table(name = "TASK_KPI_VDMAIN_SPLIT")
public class SieTaskKpiVdmainSplit implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7860347165106570750L;

  @Id
  @Column(name = "UUID")
  private String uuId;

  @Column(name = "TITLE")
  private String title;

  @Column(name = "ORG_NAME")
  private String orgName;

  @Column(name = "PSN_NAME")
  private String psnName;

  @Column(name = "dept_name")
  private String deptName;

  @Column(name = "grant_name")
  private String grantName;

  public SieTaskKpiVdmainSplit() {
    super();
  }

  public SieTaskKpiVdmainSplit(String uuId, String title, String orgName, String psnName) {
    super();
    this.uuId = uuId;
    this.title = title;
    this.orgName = orgName;
    this.psnName = psnName;
  }



  public SieTaskKpiVdmainSplit(String uuId, String title, String orgName, String psnName, String deptName,
      String grantName) {
    super();
    this.uuId = uuId;
    this.title = title;
    this.orgName = orgName;
    this.psnName = psnName;
    this.deptName = deptName;
    this.grantName = grantName;
  }

  public String getDeptName() {
    return deptName;
  }

  public void setDeptName(String deptName) {
    this.deptName = deptName;
  }

  public String getGrantName() {
    return grantName;
  }

  public void setGrantName(String grantName) {
    this.grantName = grantName;
  }

  public String getUuId() {
    return uuId;
  }

  public void setUuId(String uuId) {
    this.uuId = uuId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getOrgName() {
    return orgName;
  }

  public void setOrgName(String orgName) {
    this.orgName = orgName;
  }

  public String getPsnName() {
    return psnName;
  }

  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  @Override
  public String toString() {
    return "TmpKpiVdmainSplit [uuId=" + uuId + ", title=" + title + ", orgName=" + orgName + ", psnName=" + psnName
        + "]";
  }



}
