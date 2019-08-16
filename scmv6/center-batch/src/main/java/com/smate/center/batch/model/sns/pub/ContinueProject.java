package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 延续项目
 * 
 * @author Scy
 * 
 */
@Entity
@Table(name = "NSFC_CONTINUE_PROJECT")
public class ContinueProject implements Serializable {

  private static final long serialVersionUID = 87449818754194184L;

  // 基金委项目编号
  @Id
  @Column(name = "NSFC_PRJ_CODE")
  private Long nsfcPrjCode;

  // 项目批准号
  @Column(name = "PNO")
  private String pno;

  // 项目标题
  @Column(name = "CTITLE")
  private String ctitle;

  // 拥有者ID
  @Column(name = "PSN_ID")
  private Long psnId;

  // 依托单位
  @Column(name = "ORG_NAME")
  private String orgName;

  // 学科代码1
  @Column(name = "DISNO1")
  private String disno1;

  public ContinueProject() {
    super();
  }

  public ContinueProject(Long nsfcPrjCode, String pno, String ctitle, Long psnId, String orgName, String disno1) {
    super();
    this.nsfcPrjCode = nsfcPrjCode;
    this.pno = pno;
    this.ctitle = ctitle;
    this.psnId = psnId;
    this.orgName = orgName;
    this.disno1 = disno1;
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

  public String getDisno1() {
    return disno1;
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

  public void setDisno1(String disno1) {
    this.disno1 = disno1;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getOrgName() {
    return orgName;
  }

  public void setOrgName(String orgName) {
    this.orgName = orgName;
  }

}
