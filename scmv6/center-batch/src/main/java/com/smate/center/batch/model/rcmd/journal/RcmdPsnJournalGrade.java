package com.smate.center.batch.model.rcmd.journal;

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
public class RcmdPsnJournalGrade implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = 355864656258261278L;
  private Long psnId;
  private Integer grade;

  public RcmdPsnJournalGrade() {
    super();
  }

  public RcmdPsnJournalGrade(Long psnId, Integer grade) {
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
