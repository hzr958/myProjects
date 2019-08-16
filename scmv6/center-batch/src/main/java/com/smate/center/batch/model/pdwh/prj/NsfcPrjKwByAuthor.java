package com.smate.center.batch.model.pdwh.prj;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "NSFC_PROJECT_PUB_KWS")
public class NsfcPrjKwByAuthor implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = 7882543427739603495L;

  private Long prjId;
  private Integer year;
  private String nsfcCategroy;
  private String kwStrZh;
  private String kwStrEn;

  public NsfcPrjKwByAuthor() {
    super();
  }

  @Id
  @Column(name = "PRJ_ID")
  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  @Column(name = "PRJ_YEAR")
  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  @Column(name = "NSFC_CATEGORY")
  public String getNsfcCategroy() {
    return nsfcCategroy;
  }

  public void setNsfcCategroy(String nsfcCategroy) {
    this.nsfcCategroy = nsfcCategroy;
  }

  @Column(name = "ZH_KW")
  public String getKwStrZh() {
    return kwStrZh;
  }

  public void setKwStrZh(String kwStrZh) {
    this.kwStrZh = kwStrZh;
  }

  @Column(name = "EN_KW")
  public String getKwStrEn() {
    return kwStrEn;
  }

  public void setKwStrEn(String kwStrEn) {
    this.kwStrEn = kwStrEn;
  }

}
