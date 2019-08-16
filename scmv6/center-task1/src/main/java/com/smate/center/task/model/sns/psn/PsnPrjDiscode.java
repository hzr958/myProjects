package com.smate.center.task.model.sns.psn;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 个人申请基金代码.
 * 
 * @author liangguokeng
 * 
 */
@Entity
@Table(name = "PSN_PRJ_DISCODE")
public class PsnPrjDiscode implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 2992481254124379141L;
  private Long id;
  private Long psnId;
  private String disCode;
  private int type;

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_PRJ_DISCODE", allocationSize = 1)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "DIS_CODE")
  public String getDisCode() {
    return disCode;
  }

  @Column(name = "TYPE")
  public int getType() {
    return type;
  }

  public void setDisCode(String disCode) {
    this.disCode = disCode;
  }

  public void setType(int type) {
    this.type = type;
  }

}
