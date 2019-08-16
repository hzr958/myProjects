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

/**
 * 单位首页分享记录表
 * 
 * @author hd
 *
 */
@Entity
@Table(name = "BH_INDEX_SHARE")
public class SieBhIndexShare implements Serializable {

  private static final long serialVersionUID = -1348800365111943302L;
  // 主键
  private Long id;
  // 单位主键
  private Long insId;
  // 分享主页的人员ID
  private Long psnId;
  private Long psnRegionId;
  // 记录创建日期
  private Date creDate;
  // 格式化过的创建日期
  private Long fmtDate;
  // IP地址
  private String ip;
  // IP所在国家
  private String ipCountry;
  // IP所在省
  private String ipProv;
  // IP所在市
  private String ipCity;
  // 分享平台：1：微信，2：新浪微博，3：Facebook，4：Linkedin,5：邮件,6：复制链接
  private Integer platForm;
  /*
   * 信息来源:1 机构主页 - 机构版 2 个人版
   */
  private Integer dataFrom;


  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_BH_INDEX_SHARE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
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

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "PSN_REGION_ID")
  public Long getPsnRegionId() {
    return psnRegionId;
  }

  public void setPsnRegionId(Long psnRegionId) {
    this.psnRegionId = psnRegionId;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreDate() {
    return creDate;
  }

  public void setCreDate(Date creDate) {
    this.creDate = creDate;
  }

  @Column(name = "FORMMATE_DATE")
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

  @Column(name = "PLATFORM")
  public Integer getPlatForm() {
    return platForm;
  }

  public void setPlatForm(Integer platForm) {
    this.platForm = platForm;
  }

  @Column(name = "DATA_FROM")
  public Integer getDataFrom() {
    return dataFrom;
  }

  public void setDataFrom(Integer dataFrom) {
    this.dataFrom = dataFrom;
  }

}
