package com.smate.center.task.psn.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 人员信息完整度计分刷新model.
 * 
 * @author chenxiangrong
 * 
 */
@Entity
@Table(name = "PSN_SCORE_REFRESH")
public class PsnScoreRefresh implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 6825028161691835201L;
  private Long psnId;
  private Date updateDate;

  @Id
  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }
}
