package com.smate.web.management.model.analysis.sns;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 人员期刊等级.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PSN_JOURNAL_GRADE")
public class PsnJournalGrade implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = 8010664062360884269L;
  private Long psnId;
  private Integer grade;

  public PsnJournalGrade() {
    super();
  }

  public PsnJournalGrade(Long psnId, Integer grade) {
    super();
    this.psnId = psnId;
    this.grade = grade;
  }

  @Id
  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "GRADE")
  public Integer getGrade() {
    return grade;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setGrade(Integer grade) {
    this.grade = grade;
  }

}
