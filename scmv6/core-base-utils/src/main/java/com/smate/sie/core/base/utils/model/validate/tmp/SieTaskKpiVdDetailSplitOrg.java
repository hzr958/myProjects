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
@Table(name = "TASK_KPI_VDDETAIL_SPLIT_ORG")
public class SieTaskKpiVdDetailSplitOrg implements Serializable {



  /**
   * 
   */
  private static final long serialVersionUID = -3021014358814461640L;



  @Id
  @Column(name = "DETAIL_ID")
  private Long detailId;

  @Column(name = "UUID")
  private String uuId;

  @Column(name = "NAME")
  private String name;

  @Column(name = "ORG_NO")
  private String orgNo;

  @Column(name = "V_UNIFORM")
  private Integer vUniform;

  @Column(name = "C_NAME1")
  private String cName1;

  @Column(name = "C_UNIFORM1")
  private String cUniform1;

  @Column(name = "C_NAME2")
  private String cName2;

  @Column(name = "C_UNIFORM2")
  private String cUniform2;

  @Column(name = "STATUS")
  private Integer status;

  @Column(name = "INTERFACE_RESULT")
  private String interfaceResult;



  public SieTaskKpiVdDetailSplitOrg() {
    super();
  }



  public SieTaskKpiVdDetailSplitOrg(Long detailId, String uuId, String name, String orgNo, Integer vUniform, String cName1,
      String cUniform1, String cName2, String cUniform2, Integer status, String interfaceResult) {
    super();
    this.detailId = detailId;
    this.uuId = uuId;
    this.name = name;
    this.orgNo = orgNo;
    this.vUniform = vUniform;
    this.cName1 = cName1;
    this.cUniform1 = cUniform1;
    this.cName2 = cName2;
    this.cUniform2 = cUniform2;
    this.status = status;
    this.interfaceResult = interfaceResult;
  }



  public Long getDetailId() {
    return detailId;
  }

  public void setDetailId(Long detailId) {
    this.detailId = detailId;
  }

  public String getUuId() {
    return uuId;
  }

  public void setUuId(String uuId) {
    this.uuId = uuId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getOrgNo() {
    return orgNo;
  }

  public void setOrgNo(String orgNo) {
    this.orgNo = orgNo;
  }

  public Integer getvUniform() {
    return vUniform;
  }

  public void setvUniform(Integer vUniform) {
    this.vUniform = vUniform;
  }

  public String getcName1() {
    return cName1;
  }

  public void setcName1(String cName1) {
    this.cName1 = cName1;
  }

  public String getcUniform1() {
    return cUniform1;
  }

  public void setcUniform1(String cUniform1) {
    this.cUniform1 = cUniform1;
  }



  public String getcName2() {
    return cName2;
  }



  public void setcName2(String cName2) {
    this.cName2 = cName2;
  }



  public String getcUniform2() {
    return cUniform2;
  }

  public void setcUniform2(String cUniform2) {
    this.cUniform2 = cUniform2;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getInterfaceResult() {
    return interfaceResult;
  }

  public void setInterfaceResult(String interfaceResult) {
    this.interfaceResult = interfaceResult;
  }
}
