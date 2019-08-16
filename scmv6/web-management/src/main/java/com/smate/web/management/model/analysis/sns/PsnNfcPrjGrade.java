package com.smate.web.management.model.analysis.sns;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 人员项目等级表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PSN_NSFCPRJ_GRADE")
public class PsnNfcPrjGrade implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -5350307829271952446L;
  // 人员ID
  private Long psnId;
  // 项目等级
  private Integer prjGrade;

  @Id
  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "PRJ_GRADE")
  public Integer getPrjGrade() {
    return prjGrade;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setPrjGrade(Integer prjGrade) {
    this.prjGrade = prjGrade;
  }

}
