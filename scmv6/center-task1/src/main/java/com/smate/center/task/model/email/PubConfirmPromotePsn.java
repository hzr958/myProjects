package com.smate.center.task.model.email;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "PUB_CONFIRM_PROMOTE_PSN")
public class PubConfirmPromotePsn implements Serializable {

  /**
   * @author zk
   * 
   *         成果认领推广邮件 人员表
   */
  private static final long serialVersionUID = 249120417641776970L;

  private Long id;
  private Long psnId;
  private Date dealDt;
  private Integer status;

  @Id
  @Column(name = "id")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_CONFIRM_PROMOTE_PSN", allocationSize = 1)
  @GeneratedValue(generator = "SEQ_STORE", strategy = GenerationType.SEQUENCE)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "psn_id")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "deal_dt")
  public Date getDealDt() {
    return dealDt;
  }

  public void setDealDt(Date dealDt) {
    this.dealDt = dealDt;
  }

  @Column(name = "status")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
