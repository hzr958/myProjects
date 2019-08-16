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
 * 成功拉取成果日志
 * 
 * @author tsz
 *
 */
@Entity
@Table(name = "V_OPEN_INC_GET_PUB_LOG")
public class InterconnectionGetPubLog {
  /**
   * 该类不需要主要 也不要索引 免得数据过大
   */
  @Id
  @Column(name = "LOG_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "seq_V_OPEN_INC_GET_PUB_LOG", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long logId;
  @Column(name = "OPEN_ID")
  private Long openId; // 个人openid
  @Column(name = "TOKEN")
  private String token; // 系统授权码
  @Column(name = "ACCESS_TYPE")
  private Integer accessType; // 拉取成果的类型 (1 个人成果、2 群组成果)
  @Column(name = "PUB_NUMBERS")
  private Integer pubNumbers; // 该次拉取 拉取的成果数
  @Column(name = "ACCESS_DATE")
  private Date accessDate; // 拉取时间
  @Column(name = "DESCRIBE")
  private String describe; // 描述

  public InterconnectionGetPubLog() {
    super();
  }

  public InterconnectionGetPubLog(Long openId, String token, Integer accessType, Integer pubNumbers, Date accessDate,
      String describe) {
    super();
    this.openId = openId;
    this.token = token;
    this.accessType = accessType;
    this.pubNumbers = pubNumbers;
    this.accessDate = accessDate;
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

  public Integer getAccessType() {
    return accessType;
  }

  public void setAccessType(Integer accessType) {
    this.accessType = accessType;
  }

  public Integer getPubNumbers() {
    return pubNumbers;
  }

  public void setPubNumbers(Integer pubNumbers) {
    this.pubNumbers = pubNumbers;
  }

  public Date getAccessDate() {
    return accessDate;
  }

  public void setAccessDate(Date accessDate) {
    this.accessDate = accessDate;
  }

  public String getDescribe() {
    return describe;
  }

  public void setDescribe(String describe) {
    this.describe = describe;
  }

}
