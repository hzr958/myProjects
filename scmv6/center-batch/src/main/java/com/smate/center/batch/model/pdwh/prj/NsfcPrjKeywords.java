package com.smate.center.batch.model.pdwh.prj;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "NSFC_PROJECT_KW")
public class NsfcPrjKeywords implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 2282203261475351056L;

  private Long Id;
  private Long prjId;
  private Integer year;
  private Integer language;// 1英文，2中文
  private String nsfcCategroy;
  private Integer kwType; // 1自填关键词，2关联成果关键词
  private Long kwHashValue;
  private String kwStr;

  public NsfcPrjKeywords() {
    super();
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", allocationSize = 1, sequenceName = "SEQ_PDWH_PUB_ADDR_INS_RECORD")
  @GeneratedValue(generator = "SEQ_STORE", strategy = GenerationType.SEQUENCE)
  public Long getId() {
    return Id;
  }

  public void setId(Long id) {
    Id = id;
  }

  @Column(name = "PRJ_ID")
  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  @Column(name = "YEAR")
  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  @Column(name = "LANGUAGE")
  public Integer getLanguage() {
    return language;
  }

  public void setLanguage(Integer language) {
    this.language = language;
  }

  @Column(name = "KW_TYPE")
  public Integer getKwType() {
    return kwType;
  }

  public void setKwType(Integer kwType) {
    this.kwType = kwType;
  }

  @Column(name = "KW_HASHVALUE")
  public Long getKwHashValue() {
    return kwHashValue;
  }

  public void setKwHashValue(Long kwHashValue) {
    this.kwHashValue = kwHashValue;
  }

  @Column(name = "KW_STR")
  public String getKwStr() {
    return kwStr;
  }

  public void setKwStr(String kwStr) {
    this.kwStr = kwStr;
  }

  @Column(name = "NSFC_CATEGORY")
  public String getNsfcCategroy() {
    return nsfcCategroy;
  }

  public void setNsfcCategroy(String nsfcCategroy) {
    this.nsfcCategroy = nsfcCategroy;
  }


}
