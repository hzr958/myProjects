package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 * 机构统计表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "KPI_INS_INF")
public class KpiInsInf implements Serializable {



  /**
   * 
   */
  private static final long serialVersionUID = 927829820394326422L;
  // 主键，机构ID
  private Long insId;
  // 市级直辖区ID
  private Long disId;
  // 地区ID
  private Long cyId;
  // 省份ID
  private Long prvId;
  // 人员总数
  private Long psnNum = 0L;
  // 成果总数
  private Long pubNum = 0L;
  // 期刊总数
  private Long journalNum = 0L;
  // 会议总数
  private Long confNum = 0L;
  // 专利总数
  private Long patentNum = 0L;
  // 高校图片
  private String img = "/images/ins_logo/default.gif";
  private InstitutionRol institutionRol;

  public KpiInsInf() {
    super();
  }

  public KpiInsInf(Long insId, Long disId, Long cyId, Long prvId) {
    super();
    this.insId = insId;
    this.disId = disId;
    this.cyId = cyId;
    this.prvId = prvId;
  }

  @Id
  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "PSN_NUM")
  public Long getPsnNum() {
    return psnNum;
  }

  @Column(name = "PUB_NUM")
  public Long getPubNum() {
    return pubNum;
  }

  @Column(name = "JORNAL_NUM")
  public Long getJournalNum() {
    return journalNum;
  }

  @Column(name = "CONF_NUM")
  public Long getConfNum() {
    return confNum;
  }

  @Column(name = "PATENT_NUM")
  public Long getPatentNum() {
    return patentNum;
  }

  @Column(name = "IMG")
  public String getImg() {
    return img;
  }

  @Column(name = "DIS_ID")
  public Long getDisId() {
    return disId;
  }

  @Column(name = "CY_ID")
  public Long getCyId() {
    return cyId;
  }

  @Column(name = "PRV_ID")
  public Long getPrvId() {
    return prvId;
  }

  public void setDisId(Long disId) {
    this.disId = disId;
  }

  public void setCyId(Long cyId) {
    this.cyId = cyId;
  }

  public void setPrvId(Long prvId) {
    this.prvId = prvId;
  }

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(insertable = false, updatable = false, name = "insId")
  public InstitutionRol getInstitutionRol() {
    return institutionRol;
  }

  public void setInstitutionRol(InstitutionRol institutionRol) {
    this.institutionRol = institutionRol;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setPsnNum(Long psnNum) {
    this.psnNum = psnNum;
  }

  public void setPubNum(Long pubNum) {
    this.pubNum = pubNum;
  }

  public void setJournalNum(Long journalNum) {
    this.journalNum = journalNum;
  }

  public void setConfNum(Long confNum) {
    this.confNum = confNum;
  }

  public void setPatentNum(Long patentNum) {
    this.patentNum = patentNum;
  }

  public void setImg(String img) {
    this.img = img;
  }

}
