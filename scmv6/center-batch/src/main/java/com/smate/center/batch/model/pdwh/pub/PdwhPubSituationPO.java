package com.smate.center.batch.model.pdwh.pub;

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
 * 基准库成果被收录情况表
 * 
 * @author YJ
 *
 *         2018年5月31日
 */
@Entity
@Table(name = "V_PDWH_SITUATION")
public class PdwhPubSituationPO implements Serializable {

  private static final long serialVersionUID = 5757779071388356367L;

  @Id
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PDWH_SITUATION_ID", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @Column(name = "ID")
  private Long id; // 逻辑主键

  @Column(name = "PDWH_PUB_ID")
  private Long pdwhPubId; // 基准库成果id

  @Column(name = "LIBRARY_NAME")
  private String libraryName; // 收录机构名

  @Column(name = "SIT_STATUS", columnDefinition = "INT default 0")
  private Integer sitStatus; // 收录状态 0:未收录 ，1:收录

  @Column(name = "SIT_ORIGIN_STATUS", columnDefinition = "INT default 0")
  private Integer sitOriginStatus; // 原始收录状态 0:未收录 ，1:收录

  @Column(name = "SRC_URL")
  private String srcUrl; // 来源URL

  @Column(name = "SRC_DB_ID")
  private String srcDbId; // 来源dbid

  @Column(name = "SRC_ID")
  private String srcId; // 来源唯一标识

  @Column(name = "GMT_CREATE")
  private Date gmtCreate; // 创建时间

  @Column(name = "GMT_MODIFIED")
  private Date gmtModified; // 更新时间

  public PdwhPubSituationPO() {
    super();
  }

  public Long getPdwhPubId() {
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
  }

  public String getLibraryName() {
    return libraryName;
  }

  public void setLibraryName(String libraryName) {
    this.libraryName = libraryName;
  }

  public Integer getSitStatus() {
    return sitStatus;
  }

  public void setSitStatus(Integer sitStatus) {
    this.sitStatus = sitStatus;
  }

  public Integer getSitOriginStatus() {
    return sitOriginStatus;
  }

  public void setSitOriginStatus(Integer sitOriginStatus) {
    this.sitOriginStatus = sitOriginStatus;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public void setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
  }

  public Date getGmtModified() {
    return gmtModified;
  }

  public void setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
  }

  public String getSrcUrl() {
    return srcUrl;
  }

  public void setSrcUrl(String srcUrl) {
    this.srcUrl = srcUrl;
  }

  public String getSrcDbId() {
    return srcDbId;
  }

  public void setSrcDbId(String srcDbId) {
    this.srcDbId = srcDbId;
  }

  public String getSrcId() {
    return srcId;
  }

  public void setSrcId(String srcId) {
    this.srcId = srcId;
  }

  @Override
  public String toString() {
    return "PubSituationPO{" + "pdwhPubId='" + pdwhPubId + '\'' + ", libraryName='" + libraryName + '\''
        + ", sitStatus='" + sitStatus + '\'' + ", sitOriginStatus='" + sitOriginStatus + '\'' + ", gmtCreate='"
        + gmtCreate + '\'' + ", gmtModified='" + gmtModified + '\'' + '}';
  }
}
