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
 * 
 * @author hd
 *
 */
@Entity
@Table(name = "KPI_IMPACT_BASE_AWARD")
public class KpiImpactBaseAward implements Serializable {

  private static final long serialVersionUID = -7561948179000309680L;
  // 主键
  private Long id;
  // 数据所属单位
  private Long insId;
  // 数据所属单位名
  private String insName;
  // 社交行为记录时间，yyyy-MM-dd hh24：mm：ss
  private Date timeRecords;
  // 年
  private Integer tiemYear;
  // 月
  private Integer timeMon;
  // 日
  private Integer timeDay;
  // 数据类型：1项目2论文3专利
  private Integer keyType;
  // 数据主键
  private Long keyCode;
  // 数据标题
  private String title;
  // IP
  private String ip;
  // 国家名
  private String country;
  // 省份或地区名
  private String prov;
  // 城市名
  private String city;
  // 社交行为操作人
  private Long psnId;
  // 社交行为操作人姓名
  private String psnName;
  // 社交行为操作人所在单位名
  private String psnInsName;
  // 社交行为操作人所在地
  private Long psnRegId;
  // 社交行为操作人职称
  private String psnPos;
  // 社交行为操作人领域
  private String psnSub;

  public KpiImpactBaseAward() {
    super();
  }


  public KpiImpactBaseAward(Long insId, Date timeRecords, Integer keyType, Long keyCode, String title, String ip,
      String country, String prov, String city, Long psnId) {
    super();
    this.insId = insId;
    this.timeRecords = timeRecords;
    this.keyType = keyType;
    this.keyCode = keyCode;
    this.title = title;
    this.ip = ip == null ? ip : ip.trim();
    this.country = country == null ? country : country.trim();
    this.prov = prov == null ? prov : prov.trim();
    this.city = city == null ? city : city.trim();
    this.psnId = psnId;
  }



  @Id
  @Column(name = "id")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_KPI_IMPACT_BASE_VIEW", allocationSize = 1)
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

  @Column(name = "INS_NAME")
  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  @Column(name = "TIME")
  public Date getTimeRecords() {
    return timeRecords;
  }

  public void setTimeRecords(Date timeRecords) {
    this.timeRecords = timeRecords;
  }

  @Column(name = "TIME_YEAR")
  public Integer getTiemYear() {
    return tiemYear;
  }

  public void setTiemYear(Integer tiemYear) {
    this.tiemYear = tiemYear;
  }

  @Column(name = "TIME_MONTH")
  public Integer getTimeMon() {
    return timeMon;
  }

  public void setTimeMon(Integer timeMon) {
    this.timeMon = timeMon;
  }

  @Column(name = "TIME_DAY")
  public Integer getTimeDay() {
    return timeDay;
  }

  public void setTimeDay(Integer timeDay) {
    this.timeDay = timeDay;
  }

  @Column(name = "KEY_TYPE")
  public Integer getKeyType() {
    return keyType;
  }

  public void setKeyType(Integer keyType) {
    this.keyType = keyType;
  }

  @Column(name = "KEY_CODE")
  public Long getKeyCode() {
    return keyCode;
  }

  public void setKeyCode(Long keyCode) {
    this.keyCode = keyCode;
  }

  @Column(name = "TITLE")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Column(name = "IP")
  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  @Column(name = "COUNTRY")
  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  @Column(name = "PROVINCE")
  public String getProv() {
    return prov;
  }

  public void setProv(String prov) {
    this.prov = prov;
  }

  @Column(name = "CITY")
  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "PSN_NAME")
  public String getPsnName() {
    return psnName;
  }

  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  @Column(name = "PSN_INS_NAME")
  public String getPsnInsName() {
    return psnInsName;
  }

  public void setPsnInsName(String psnInsName) {
    this.psnInsName = psnInsName;
  }

  @Column(name = "PSN_REGION_ID")
  public Long getPsnRegId() {
    return psnRegId;
  }


  public void setPsnRegId(Long psnRegId) {
    this.psnRegId = psnRegId;
  }


  @Column(name = "PSN_POS")
  public String getPsnPos() {
    return psnPos;
  }

  public void setPsnPos(String psnPos) {
    this.psnPos = psnPos;
  }

  @Column(name = "PSN_SUBJECT")
  public String getPsnSub() {
    return psnSub;
  }

  public void setPsnSub(String psnSub) {
    this.psnSub = psnSub;
  }



}
