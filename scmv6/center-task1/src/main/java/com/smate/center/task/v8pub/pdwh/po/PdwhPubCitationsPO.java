package com.smate.center.task.v8pub.pdwh.po;

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
 * 基准库成果引用次数记录实体
 * 
 * @author YJ
 *
 *         2018年7月12日
 */
@Entity
@Table(name = "V_PDWH_CITATIONS")
public class PdwhPubCitationsPO implements Serializable {

  private static final long serialVersionUID = 6333719366836832719L;

  /**
   * 逻辑主键ID
   */
  @Id
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PDWH_CITATIONS_ID", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @Column(name = "ID")
  private Long id;

  /**
   * 基准库成果id
   */
  @Column(name = "PDWH_PUB_ID")
  private Long pdwhPubId;

  /**
   * 来源ID
   */
  @Column(name = "DB_ID")
  private Integer dbId;

  /**
   * 引用次数
   */
  @Column(name = "CITATIONS")
  private Integer citations;

  /**
   * 引用更新时间
   */
  @Column(name = "GMT_MODIFIED")
  private Date gmtModified;

  /**
   * 引用更新类型 手动更新1，后台更新0
   */
  @Column(name = "TYPE")
  private Integer type;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getDbId() {
    return dbId;
  }

  public void setDbId(Integer dbId) {
    this.dbId = dbId;
  }

  public Long getPdwhPubId() {
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
  }

  public int getCitations() {
    return citations;
  }

  public void setCitations(Integer citations) {
    this.citations = citations;
  }

  public Date getGmtModified() {
    return gmtModified;
  }

  public void setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
  }

  public int getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return "PubCitationsPO{" + "id='" + id + '\'' + ", pdwhPubId='" + pdwhPubId + '\'' + ", citations='" + citations
        + '\'' + ", dbId='" + dbId + '\'' + ", gmtModified='" + gmtModified + '\'' + ", type='" + type + '\'' + '}';
  }
}
