package com.smate.core.base.utils.model.shorturl;

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
 * 短地址实体类
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "V_OPEN_SHORT_URL")
public class OpenShortUrl implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_V_OPEN_SHORT_URL", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;// ID
  @Column(name = "TYPE")
  private String type;// 短地址业务类型AB...
  @Column(name = "SHORT_URL")
  private String shortUrl;// 短地址 不带类型的
  @Column(name = "REAL_URL_PARAMET")
  private String realUrlParamet;// 真实地址需要的参数 标准的json格式
  @Column(name = "REAL_URL_HASH")
  private Integer realUrlHash;// 真实地址需要的参数hash 方便查询
  @Column(name = "HAS_EXPIRATION_TIME")
  private Integer hasExpirationTime;// 是否可过期 0=长期 1=临时
  @Column(name = "EXPIRATION_TIME")
  private Date expirationTime;// 过期时间
  @Column(name = "HAS_TIMES_LIMIT")
  private Integer hasTimesLimit;// 是否有次数限制 0=没有 1=有
  @Column(name = "TIMES_LIMIT")
  private Integer timesLimit;// 可使用次数
  @Column(name = "TIMES_USED")
  private Integer timesUsed;// 已使用次数
  @Column(name = "CREATE_DATE")
  private Date createDate;// 创建时间
  @Column(name = "CREATE_PSN_ID")
  private Long createPsnId;// 创建人 0=系统 1=匿名用户

  public OpenShortUrl() {
    super();
  }

  public OpenShortUrl(Long id, String type, String shortUrl, String realUrlParamet, Integer realUrlHash,
      Integer hasExpirationTime, Date expirationTime, Integer hasTimesLimit, Integer timesLimit, Integer timesUsed,
      Date createDate, Long createPsnId) {
    super();
    this.id = id;
    this.type = type;
    this.shortUrl = shortUrl;
    this.realUrlParamet = realUrlParamet;
    this.realUrlHash = realUrlHash;
    this.hasExpirationTime = hasExpirationTime;
    this.expirationTime = expirationTime;
    this.hasTimesLimit = hasTimesLimit;
    this.timesLimit = timesLimit;
    this.timesUsed = timesUsed;
    this.createDate = createDate;
    this.createPsnId = createPsnId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getShortUrl() {
    return shortUrl;
  }

  public void setShortUrl(String shortUrl) {
    this.shortUrl = shortUrl;
  }

  public String getRealUrlParamet() {
    return realUrlParamet;
  }

  public void setRealUrlParamet(String realUrlParamet) {
    this.realUrlParamet = realUrlParamet;
  }

  public Integer getRealUrlHash() {
    return realUrlHash;
  }

  public void setRealUrlHash(Integer realUrlHash) {
    this.realUrlHash = realUrlHash;
  }

  public Integer getHasExpirationTime() {
    return hasExpirationTime;
  }

  public void setHasExpirationTime(Integer hasExpirationTime) {
    this.hasExpirationTime = hasExpirationTime;
  }

  public Date getExpirationTime() {
    return expirationTime;
  }

  public void setExpirationTime(Date expirationTime) {
    this.expirationTime = expirationTime;
  }

  public Integer getHasTimesLimit() {
    return hasTimesLimit;
  }

  public void setHasTimesLimit(Integer hasTimesLimit) {
    this.hasTimesLimit = hasTimesLimit;
  }

  public Integer getTimesLimit() {
    return timesLimit;
  }

  public void setTimesLimit(Integer timesLimit) {
    this.timesLimit = timesLimit;
  }

  public Integer getTimesUsed() {
    return timesUsed;
  }

  public void setTimesUsed(Integer timesUsed) {
    this.timesUsed = timesUsed;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Long getCreatePsnId() {
    return createPsnId;
  }

  public void setCreatePsnId(Long createPsnId) {
    this.createPsnId = createPsnId;
  }

  @Override
  public String toString() {
    return "OpenShortUrl [id=" + id + ", type=" + type + ", shortUrl=" + shortUrl + ", realUrlParamet=" + realUrlParamet
        + ", realUrlHash=" + realUrlHash + ", hasExpirationTime=" + hasExpirationTime + ", expirationTime="
        + expirationTime + ", hasTimesLimit=" + hasTimesLimit + ", timesLimit=" + timesLimit + ", timesUsed="
        + timesUsed + ", createDate=" + createDate + ", createPsnId=" + createPsnId + "]";
  }
}
