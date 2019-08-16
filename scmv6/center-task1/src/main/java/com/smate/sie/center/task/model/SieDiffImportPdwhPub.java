package com.smate.sie.center.task.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 基准库导入数据变化表
 * 
 * @author yxy
 *
 */
@Entity
@Table(name = "DIFF_IMPORT_PDWH_PUB")
public class SieDiffImportPdwhPub {

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_DIFF_IMPORT_PDWH_PUB", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long diffId;

  @Column(name = "INS_ID")
  private Long insId;

  @Column(name = "ZH_NAME")
  private String zhName;

  @Column(name = "PUB_INSERT_COUNT")
  private Long pubInsertCount;

  @Column(name = "PUB_UPDATE_COUNT")
  private Long pubUpdateCount;

  @Column(name = "PAT_INSERT_COUNT")
  private Long patInsertCount;

  @Column(name = "PAT_UPDATE_COUNT")
  private Long patUpdateCount;

  @Column(name = "CREATE_DATE")
  private Date createDate;

  public Long getDiffId() {
    return diffId;
  }

  public Long getInsId() {
    return insId;
  }

  public String getZhName() {
    return zhName;
  }

  public Long getPubInsertCount() {
    return pubInsertCount;
  }

  public Long getPubUpdateCount() {
    return pubUpdateCount;
  }

  public Long getPatInsertCount() {
    return patInsertCount;
  }

  public Long getPatUpdateCount() {
    return patUpdateCount;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setDiffId(Long diffId) {
    this.diffId = diffId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  public void setPubInsertCount(Long pubInsertCount) {
    this.pubInsertCount = pubInsertCount;
  }

  public void setPubUpdateCount(Long pubUpdateCount) {
    this.pubUpdateCount = pubUpdateCount;
  }

  public void setPatInsertCount(Long patInsertCount) {
    this.patInsertCount = patInsertCount;
  }

  public void setPatUpdateCount(Long patUpdateCount) {
    this.patUpdateCount = patUpdateCount;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

}
