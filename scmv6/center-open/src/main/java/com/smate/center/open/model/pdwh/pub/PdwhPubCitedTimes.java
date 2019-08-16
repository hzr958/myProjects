package com.smate.center.open.model.pdwh.pub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 
 * @author zjh 成果引用次数表
 *
 */
@Entity
@Table(name = "PDWH_PUB_CITED_TIMES")
public class PdwhPubCitedTimes implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -2297723976704566732L;
  private Long pdwhPubId;// 成果id
  private Integer citedTimes;// 引用次数
  private Date updateDate;// 更新的日期
  private Integer type;// 手动更新1，后台更新0
  private Long id;
  private Integer dbid;// ISI(15,16,17)在这里统一记录为99,cnkipat 21,rainpat 31 统一记录为 98

  public PdwhPubCitedTimes() {
    super();
  }

  public PdwhPubCitedTimes(Long pdwhPubId, Integer citedTimes, Date updateDate, Integer type) {
    super();
    this.pdwhPubId = pdwhPubId;
    this.citedTimes = citedTimes;
    this.updateDate = updateDate;
    this.type = type;
  }

  @Id
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PDWH_PUB_CITED_TIMES", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "DB_ID")
  public Integer getDbid() {
    return dbid;
  }

  public void setDbid(Integer dbid) {
    this.dbid = dbid;
  }

  @Column(name = "PDWH_PUB_ID")
  public Long getPdwhPubId() {
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
  }

  @Column(name = "CITED_TIMES")
  public int getCitedTimes() {
    return citedTimes;
  }

  public void setCitedTimes(Integer citedTimes) {
    this.citedTimes = citedTimes;
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  @Column(name = "TYPE")
  public int getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }


}
