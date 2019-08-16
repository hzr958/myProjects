package com.smate.sie.core.base.utils.model.statistics;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "BH_DOWNLOAD")
public class SieBhDownload implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -4981086076729881696L;

  private Long id;// 下载记录主键
  private Long keyId;// 被下载内容的主键
  private Long downloadPsnId;// 下载者 的id
  private Integer type;// 类型: 1 项目，2论文， 3专利
  private Date creDate; // 创建日期
  private Long fmtDate;// 格式化日期
  private String ip;// IP
  // IP所在国家
  private String ipCountry;
  // IP所在省
  private String ipProv;
  // IP所在市
  private String ipCity;
  private Long psnRegionId;



  public SieBhDownload() {
    super();
  }

  public SieBhDownload(Long keyId, Long downloadPsnId, Integer type, Date creDate, Long fmtDate, String ip) {
    super();
    this.keyId = keyId;
    this.downloadPsnId = downloadPsnId;
    this.type = type;
    this.creDate = creDate;
    this.fmtDate = fmtDate;
    this.ip = ip;
  }

  public SieBhDownload(Long id, Long keyId, Long downloadPsnId, Integer type, Date creDate, Long fmtDate, String ip) {
    super();
    this.id = id;
    this.keyId = keyId;
    this.downloadPsnId = downloadPsnId;
    this.type = type;
    this.creDate = creDate;
    this.fmtDate = fmtDate;
    this.ip = ip;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_BH_DOWNLOAD", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "KEY_ID")
  public Long getKeyId() {
    return keyId;
  }

  public void setKeyId(Long keyId) {
    this.keyId = keyId;
  }

  @Column(name = "DOWNLOAD_PSN_ID")
  public Long getDownloadPsnId() {
    return downloadPsnId;
  }

  public void setDownloadPsnId(Long downloadPsnId) {
    this.downloadPsnId = downloadPsnId;
  }

  @Column(name = "TYPE")
  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreDate() {
    return creDate;
  }

  public void setCreDate(Date creDate) {
    this.creDate = creDate;
  }

  @Column(name = "FORMATE_DATE")
  public Long getFmtDate() {
    return fmtDate;
  }

  public void setFmtDate(Long fmtDate) {
    this.fmtDate = fmtDate;
  }

  @Column(name = "IP")
  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  @Column(name = "IP_COUNTRY")
  public String getIpCountry() {
    return ipCountry;
  }

  public void setIpCountry(String ipCountry) {
    this.ipCountry = ipCountry;
  }

  @Column(name = "IP_PROV")
  public String getIpProv() {
    return ipProv;
  }

  public void setIpProv(String ipProv) {
    this.ipProv = ipProv;
  }

  @Column(name = "IP_CITY")
  public String getIpCity() {
    return ipCity;
  }

  public void setIpCity(String ipCity) {
    this.ipCity = ipCity;
  }

  @Column(name = "PSN_REGION_ID")
  public Long getPsnRegionId() {
    return psnRegionId;
  }

  public void setPsnRegionId(Long psnRegionId) {
    this.psnRegionId = psnRegionId;
  }
}
