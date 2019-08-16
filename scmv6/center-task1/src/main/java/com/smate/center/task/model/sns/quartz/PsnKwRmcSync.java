package com.smate.center.task.model.sns.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 同步人员特征关键词回科研之友任务标记.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "psn_kw_rmc_sync")
public class PsnKwRmcSync implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -7088421394801364054L;
  private Long psnId;

  @Id
  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }
}
