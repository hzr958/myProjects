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
 * 赞记录表
 * 
 * @author hd
 *
 */
@Entity
@Table(name = "BH_AWARD")
public class SieBhAward implements Serializable {


  private static final long serialVersionUID = -5229080723341217036L;
  // 主键
  private Long id;
  // 被分享内容的主键
  private Long keyId;
  // 赞的人员id
  private Long awardPsnId;
  // 类型：1项目,2论文,3专利
  private Integer type;
  // 创建日期
  private Date creDate;
  // 格式化日期
  private Long fmtDate;
  // IP地址
  private String ip;
  // IP所在国家
  private String ipCountry;
  // IP所在省
  private String ipProv;
  // IP所在市
  private String ipCity;
  private Long psnRegionId;


  public SieBhAward() {
    super();
  }

  public SieBhAward(Long keyId, Long awardPsnId, Integer type, Date creDate, Long fmtDate, String ip,
      Long psnRegionId) {
    super();
    this.keyId = keyId;
    this.awardPsnId = awardPsnId;
    this.type = type;
    this.creDate = creDate;
    this.fmtDate = fmtDate;
    this.ip = ip;
    this.psnRegionId = psnRegionId;
  }


  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_BH_AWARD", allocationSize = 1)
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

  @Column(name = "AWARD_PSN_ID")
  public Long getAwardPsnId() {
    return awardPsnId;
  }

  public void setAwardPsnId(Long awardPsnId) {
    this.awardPsnId = awardPsnId;
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

  public void setIp(String ip) {
    this.ip = ip;
  }

  @Column(name = "PSN_REGION_ID")
  public Long getPsnRegionId() {
    return psnRegionId;
  }

  public void setPsnRegionId(Long psnRegionId) {
    this.psnRegionId = psnRegionId;
  }


}
