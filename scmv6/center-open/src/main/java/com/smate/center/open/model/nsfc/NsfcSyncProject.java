package com.smate.center.open.model.nsfc;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * NSFC项目..
 * 
 * @author cwli
 */
public class NsfcSyncProject implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8959646713922489577L;
  // 项目编号
  private String prjcode;
  // 项目批准号
  private String pno;
  // 项目标题
  private String ctitle;
  // 报告年度
  private String rptyear;
  // 学科代码1
  private String disno1;
  // 部门代码
  private String divno;
  // 申请单位名称
  private String orgname;
  // scm人员psnid
  private String psnid;
  // 人员guid
  private String psnGuid;
  // 冗余单位id
  private Long insId;
  // 报告ID
  private String nsfcRptId;
  // 报告类型
  private String rptType;
  // 报告状态 0：可以修改 1：不可以修改
  private String status;

  public NsfcSyncProject() {
    super();
  }

  public String getPrjcode() {
    return prjcode;
  }

  public void setPrjcode(String prjcode) {
    this.prjcode = prjcode;
  }

  public String getPno() {
    return pno;
  }

  public void setPno(String pno) {
    this.pno = pno;
  }

  public String getCtitle() {
    return ctitle;
  }

  public void setCtitle(String ctitle) {
    this.ctitle = ctitle;
  }

  public String getRptyear() {
    return rptyear;
  }

  public void setRptyear(String rptyear) {
    this.rptyear = rptyear;
  }

  public String getDisno1() {
    return disno1;
  }

  public void setDisno1(String disno1) {
    this.disno1 = disno1;
  }

  public String getDivno() {
    return divno;
  }

  public void setDivno(String divno) {
    this.divno = divno;
  }

  public String getOrgname() {
    return orgname;
  }

  public void setOrgname(String orgname) {
    this.orgname = orgname;
  }

  public String getPsnid() {
    return psnid;
  }

  public void setPsnid(String psnid) {
    this.psnid = psnid;
  }

  public String getPsnGuid() {
    return psnGuid;
  }

  public void setPsnGuid(String psnGuid) {
    this.psnGuid = psnGuid;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public String getNsfcRptId() {
    return nsfcRptId;
  }

  public void setNsfcRptId(String nsfcRptId) {
    this.nsfcRptId = nsfcRptId;
  }

  public String getRptType() {
    return rptType;
  }

  public void setRptType(String rptType) {
    this.rptType = rptType;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

}
