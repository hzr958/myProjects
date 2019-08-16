package com.smate.center.task.model.sns.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "psn_kw_rmc_ext")
public class PsnKwRmcExt implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3403834179165170694L;
  private Long id;
  // PSN_KW_RMC主键
  private Long rmcId;
  // 人员ID
  private Long psnId;
  // 关键词
  private String keyword;
  // 关键词TXT
  private String kwTxt;
  // 1:翻译，2同义词
  private Integer type;

  private Integer kwGid;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_DYNAMIC_RECOMMEND_HTML", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "RMC_ID")
  public Long getRmcId() {
    return rmcId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "KEYWORD")
  public String getKeyword() {
    return keyword;
  }

  @Column(name = "KW_TXT")
  public String getKwTxt() {
    return kwTxt;
  }

  @Column(name = "TYPE")
  public Integer getType() {
    return type;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setRmcId(Long rmcId) {
    this.rmcId = rmcId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public void setKwTxt(String kwTxt) {
    this.kwTxt = kwTxt;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  @Column(name = "KW_GID")
  public Integer getKwGid() {
    return kwGid;
  }

  public void setKwGid(Integer kwGid) {
    this.kwGid = kwGid;
  }
}
