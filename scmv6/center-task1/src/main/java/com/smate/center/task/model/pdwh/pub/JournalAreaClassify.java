package com.smate.center.task.model.pdwh.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 期刊领域大类.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "JOURNAL_AREA_CLASSIFY")
public class JournalAreaClassify implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -4623398992360544667L;
  private Long id;
  // 期刊ISSN
  private String issn;
  // ISSN文本
  private String issnTxt;
  // 期刊领域分类
  private String classify;

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  @Column(name = "ISSN")
  public String getIssn() {
    return issn;
  }

  @Column(name = "CLASSIFY")
  public String getClassify() {
    return classify;
  }

  @Column(name = "ISSN_TXT")
  public String getIssnTxt() {
    return issnTxt;
  }

  public void setIssnTxt(String issnTxt) {
    this.issnTxt = issnTxt;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setIssn(String issn) {
    this.issn = issn;
  }

  public void setClassify(String classify) {
    this.classify = classify;
  }

}

