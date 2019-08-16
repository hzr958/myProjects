package com.smate.center.open.model.interconnection.log;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 业务系统 导入成果 记录日志
 * 
 * @author tsz
 *
 */
@Entity
@Table(name = "V_OPEN_INC_IMPORT_PUB_LOG")
public class InterconnectionImportPubLog {

  @Id
  @Column(name = "LOG_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "seq_V_OPEN_INC_IMPORT_PUB_LOG", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long logId;
  @Column(name = "OPEN_ID")
  private Long openId; // 人员openid
  @Column(name = "TOKEN")
  private String token; // 系统授权码
  @Column(name = "PUB_ID")
  private Long pubId; // 成果id
  @Column(name = "GROUP_ID")
  private Long groupId; // 群组id
  @Column(name = "IMPORT_DATE")
  private Date importDate; // 导入时间
  @Column(name = "DESCRIBE")
  private String describe; // 描述

  public InterconnectionImportPubLog() {
    super();
  }

  public InterconnectionImportPubLog(Long openId, String token, Long pubId, Long groupId, Date importDate,
      String describe) {
    super();
    this.openId = openId;
    this.token = token;
    this.pubId = pubId;
    this.groupId = groupId;
    this.importDate = importDate;
    this.describe = describe;
  }

  public Long getLogId() {
    return logId;
  }

  public void setLogId(Long logId) {
    this.logId = logId;
  }

  public Long getOpenId() {
    return openId;
  }

  public void setOpenId(Long openId) {
    this.openId = openId;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public Date getImportDate() {
    return importDate;
  }

  public void setImportDate(Date importDate) {
    this.importDate = importDate;
  }

  public String getDescribe() {
    return describe;
  }

  public void setDescribe(String describe) {
    this.describe = describe;
  }

}
