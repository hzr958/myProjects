package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PSN_PUB_STAT")
public class PsnPubStat implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -4220067821379590725L;

  private Long psnId;
  private Integer total;
  private Integer totalConfirm;
  private Date lastUpdate;

  public PsnPubStat() {
    super();
  }

  public PsnPubStat(Long psnId) {
    super();
    this.psnId = psnId;
  }

  public PsnPubStat(Long psnId, Integer total) {
    super();
    this.psnId = psnId;
    this.total = total;
  }

  /**
   * @return the psnId
   */
  @Id
  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  /**
   * @param psnId the psnId to set
   */
  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  /**
   * @return the total
   */
  @Column(name = "TOTAL")
  public Integer getTotal() {
    return total;
  }

  /**
   * @param total the total to set
   */
  public void setTotal(Integer total) {
    this.total = total;
  }

  /**
   * @return the totalConfirm
   */
  @Column(name = "TOTAL_CONFIRM")
  public Integer getTotalConfirm() {
    return totalConfirm;
  }

  /**
   * @param totalConfirm the totalConfirm to set
   */
  public void setTotalConfirm(Integer totalConfirm) {
    this.totalConfirm = totalConfirm;
  }

  /**
   * @return the lastUpdate
   */
  @Column(name = "LAST_UPDATE")
  public Date getLastUpdate() {
    return lastUpdate;
  }

  /**
   * @param lastUpdate the lastUpdate to set
   */
  public void setLastUpdate(Date lastUpdate) {
    this.lastUpdate = lastUpdate;
  }
}
