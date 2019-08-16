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
 * 首页单位人员.
 * 
 * @author lcw
 * 
 */
@Entity
@Table(name = "INS_INDEX_PSN")
public class InsIndexPsn implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 4211883681731033480L;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_INS_HEAD", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long id;
  @Column(name = "INS_ID")
  public Long insId;
  @Column(name = "PSN_ID")
  public Long psnId;
  @Column(name = "SQL_NO")
  private Integer sqlNo;
  // 是否人工设置过
  @Column(name = "FLAG")
  private Integer flag = 0;

  public InsIndexPsn() {
    super();
  }

  public InsIndexPsn(Long insId, Long psnId) {
    this.insId = insId;
    this.psnId = psnId;
    this.sqlNo = 0;
    this.flag = 0;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Integer getSqlNo() {
    return sqlNo;
  }

  public void setSqlNo(Integer sqlNo) {
    this.sqlNo = sqlNo;
  }

  public Integer getFlag() {
    return flag;
  }

  public void setFlag(Integer flag) {
    this.flag = flag;
  }

}
