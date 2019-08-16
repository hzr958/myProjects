package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * scopus用户合作者前缀记录表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PSN_PM_SPSCOPRENAME")
public class PsnPmSpsCoPreName implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1018214201725821844L;
  private Long id;
  // 用户名(小写)
  private String preName;
  // 用户ID
  private Long psnId;

  public PsnPmSpsCoPreName() {
    super();
  }

  public PsnPmSpsCoPreName(String preName, Long psnId) {
    super();
    this.preName = preName;
    this.psnId = psnId;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_PM_SPSCOPRENAME", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "PRE_NAME")
  public String getPreName() {
    return preName;
  }

  public void setPreName(String preName) {
    this.preName = preName;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

}
