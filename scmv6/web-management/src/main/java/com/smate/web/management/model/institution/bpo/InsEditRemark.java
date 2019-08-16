package com.smate.web.management.model.institution.bpo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 编辑单位的备注信息
 * 
 * @author WeiLong Peng
 *
 */
@Entity
@Table(name = "INS_EDIT_REMARK")
public class InsEditRemark implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 9189111608030401224L;
  private Long id;
  /** 单位id. */
  private Long insId;
  /** 编辑用户id. */
  private Long psnId;
  /** 编辑日期. */
  private Date createDate;
  /** 备注. */
  private String remark;

  private String psnName;

  public InsEditRemark() {}

  public InsEditRemark(Long insId, Long psnId, Date createDate, String remark) {
    this.insId = insId;
    this.psnId = psnId;
    this.createDate = createDate;
    this.remark = remark;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_INS_EDIT_REMARK", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  @Column(name = "REMARK")
  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  @Transient
  public String getPsnName() {
    return psnName;
  }

  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

}
