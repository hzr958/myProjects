package com.smate.center.task.model.pdwh.pub;

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
 * 基准库成果单位和scm单位地址常量信息匹配记录表
 * 
 * @author LIJUN
 * @date 2018年3月15日
 */
@Entity
@Table(name = "PDWH_PUB_ADDR_INS_RECORD")
public class PdwhPubAddrInsRecord implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3135373934628952411L;
  private Long id;// 主键id
  private Long constId;// 和PDWH_INS_ADDR_CONST表主键相同，便于PDWH_INS_ADDR_CONST表insid合并后更新记录
  private Long pubId;// 成果id
  private Long insId;// 单位id
  private String insName;// 单位名
  private Long insNameHash;// 单位名
  private Integer status;// 0表示系统匹配，用户确认后状态为1（暂时没有确认流程，预留）
  private Date updateTime;
  private Long regionId; // 地区id

  public PdwhPubAddrInsRecord() {
    super();
  }

  public PdwhPubAddrInsRecord(Long constId, Long pubId, Long insId, String insName, Long insNameHash, Integer status,
      Date updateTime, Long regionId) {
    super();
    this.constId = constId;
    this.pubId = pubId;
    this.insId = insId;
    this.insName = insName;
    this.insNameHash = insNameHash;
    this.status = status;
    this.updateTime = updateTime;
    this.regionId = regionId;
  }

  public PdwhPubAddrInsRecord(Long insId, String insName) {
    super();
    this.insId = insId;
    this.insName = insName;
  }

  public PdwhPubAddrInsRecord(Long pubId, Long insId, String insName, Integer status) {
    super();
    this.pubId = pubId;
    this.insId = insId;
    this.insName = insName;
    this.status = status;
  }

  @Id
  @Column(name = "Id")
  @SequenceGenerator(name = "SEQ_STORE", allocationSize = 1, sequenceName = "SEQ_PDWH_PUB_ADDR_INS_RECORD")
  @GeneratedValue(generator = "SEQ_STORE", strategy = GenerationType.SEQUENCE)
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

  @Column(name = "INS_NAME")
  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(name = "CONST_ID")
  public Long getConstId() {
    return constId;
  }

  public void setConstId(Long constId) {
    this.constId = constId;
  }

  @Column(name = "UPDATE_TIME")
  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  @Column(name = "INS_NAME_HASH")
  public Long getInsNameHash() {
    return insNameHash;
  }

  public void setInsNameHash(Long insNameHash) {
    this.insNameHash = insNameHash;
  }

  @Column(name = "REGION_ID")
  public Long getRegionId() {
    return regionId;
  }

  public void setRegionId(Long regionId) {
    this.regionId = regionId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((insId == null) ? 0 : insId.hashCode());
    result = prime * result + ((pubId == null) ? 0 : pubId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    PdwhPubAddrInsRecord other = (PdwhPubAddrInsRecord) obj;
    if (insId == null) {
      if (other.insId != null)
        return false;
    } else if (!insId.equals(other.insId))
      return false;
    if (pubId == null) {
      if (other.pubId != null)
        return false;
    } else if (!pubId.equals(other.pubId))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "PdwhPubAddrInsRecord{" + "id=" + id + ", constId=" + constId + ", pubId=" + pubId + ", insId=" + insId
        + ", insName='" + insName + '\'' + ", insNameHash=" + insNameHash + ", status=" + status + ", updateTime="
        + updateTime + '}';
  }
}
