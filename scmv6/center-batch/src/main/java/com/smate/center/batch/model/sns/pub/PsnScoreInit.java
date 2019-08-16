package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 人员信息完整度计分初始化model.
 * 
 * @author chenxiangrong
 * 
 */
@Entity
@Table(name = "PSN_SCORE_INIT")
public class PsnScoreInit implements Serializable {
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
