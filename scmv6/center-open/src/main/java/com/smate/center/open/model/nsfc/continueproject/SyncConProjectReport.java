package com.smate.center.open.model.nsfc.continueproject;

import java.io.Serializable;

/**
 * 延续报告同步model
 * 
 * @author tsz
 * 
 */
public class SyncConProjectReport implements Serializable {

  private static final long serialVersionUID = -7651387936728950885L;

  // 项目编号
  private Long nsfcPrjCode;
  // 项目批准号
  private String pno;
  // 项目标题
  private String ctitle;
  // 报告年度
  private Integer rptYear;
  // 学科代码1
  private String disno1;
  // 人员guid
  private String psnGuid;
  // 系统标识ID，基金委：2565
  private Long rolId;
  // 依托单位
  private String orgName;
  // isis系统中结题或者进展报告的ID
  private Long nsfcRptId;
  // 报告类型
  private Integer rptType;
  // 报告状态 0：允许修改成果信息 1：不允许修改成果信息
  private Integer status = 0;

  public SyncConProjectReport() {
    super();
  }

  public SyncConProjectReport(Long nsfcPrjCode, String pno, String ctitle, Integer rptYear, String disno1,
      String psnGuid, Long rolId, String orgName, Long nsfcRptId, Integer rptType, Integer status) {
    super();
    this.nsfcPrjCode = nsfcPrjCode;
    this.pno = pno;
    this.ctitle = ctitle;
    this.rptYear = rptYear;
    this.disno1 = disno1;
    this.psnGuid = psnGuid;
    this.rolId = rolId;
    this.nsfcRptId = nsfcRptId;
    this.rptType = rptType;
    this.status = status;
    this.orgName = orgName;
  }

  public Long getNsfcPrjCode() {
    return nsfcPrjCode;
  }

  public String getPno() {
    return pno;
  }

  public String getCtitle() {
    return ctitle;
  }

  public Integer getRptYear() {
    return rptYear;
  }

  public String getDisno1() {
    return disno1;
  }

  public String getPsnGuid() {
    return psnGuid;
  }

  public Long getRolId() {
    return rolId;
  }

  public Integer getRptType() {
    return rptType;
  }

  public Integer getStatus() {
    return status;
  }

  public void setNsfcPrjCode(Long nsfcPrjCode) {
    this.nsfcPrjCode = nsfcPrjCode;
  }

  public void setPno(String pno) {
    this.pno = pno;
  }

  public void setCtitle(String ctitle) {
    this.ctitle = ctitle;
  }

  public void setRptYear(Integer rptYear) {
    this.rptYear = rptYear;
  }

  public void setDisno1(String disno1) {
    this.disno1 = disno1;
  }

  public void setPsnGuid(String psnGuid) {
    this.psnGuid = psnGuid;
  }

  public void setRolId(Long rolId) {
    this.rolId = rolId;
  }

  public Long getNsfcRptId() {
    return nsfcRptId;
  }

  public void setNsfcRptId(Long nsfcRptId) {
    this.nsfcRptId = nsfcRptId;
  }

  public void setRptType(Integer rptType) {
    this.rptType = rptType;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getOrgName() {
    return orgName;
  }

  public void setOrgName(String orgName) {
    this.orgName = orgName;
  }

}
