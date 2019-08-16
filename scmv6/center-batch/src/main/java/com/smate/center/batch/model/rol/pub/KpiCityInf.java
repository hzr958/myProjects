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
 * 市级地区统计表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "KPI_CITY_INF")
public class KpiCityInf implements Serializable {



  /**
   * 
   */
  private static final long serialVersionUID = 8046437125341369183L;
  // 主键，地区ID
  private Long cyId;
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
  private String img = "/images/city_logo/default.gif";
  private ConstCnCity constCnCity;

  public KpiCityInf() {
    super();
  }

  public KpiCityInf(Long cyId) {
    super();
    this.cyId = cyId;
  }

  @Id
  @Column(name = "CY_ID")
  public Long getCyId() {
    return cyId;
  }

  public void setCyId(Long cyId) {
    this.cyId = cyId;
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

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(insertable = false, updatable = false, name = "cyId")
  public ConstCnCity getConstCnCity() {
    return constCnCity;
  }

  public void setConstCnCity(ConstCnCity constCnCity) {
    this.constCnCity = constCnCity;
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
