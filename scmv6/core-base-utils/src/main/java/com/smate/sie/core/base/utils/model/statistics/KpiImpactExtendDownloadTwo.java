package com.smate.sie.core.base.utils.model.statistics;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 单位分析，下载数拓展表2
 * 
 * @author hd
 *
 */
@Entity
@Table(name = "KPI_IMPACT_EXTEND_DOWNLOAD2")
public class KpiImpactExtendDownloadTwo implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8843556036133706137L;
  // 主键
  private Long id;
  // 统计日期，YYYY / MM
  private String time;
  // 年
  private Integer timeYear;
  // 月
  private Integer timeMon;
  // 数据主键
  private Long keyCode;
  // 数据类型：1项目2论文3专利
  private Integer keyType;
  // 数据标题
  private String title;
  // 总数
  private Long cnt;
  // 单位主键
  private Long insId;

  public KpiImpactExtendDownloadTwo() {
    super();
  }

  public KpiImpactExtendDownloadTwo(Long keyCode, Integer keyType, Long cnt, Long insId) {
    super();
    this.keyCode = keyCode;
    this.keyType = keyType;
    this.cnt = cnt;
    this.insId = insId;
  }



  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_KPI_IMPACT_EXTEND_LOAD2", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "TIME")
  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  @Column(name = "TIME_YEAR")
  public Integer getTimeYear() {
    return timeYear;
  }

  public void setTimeYear(Integer timeYear) {
    this.timeYear = timeYear;
  }

  @Column(name = "TIME_MONTH")
  public Integer getTimeMon() {
    return timeMon;
  }

  public void setTimeMon(Integer timeMon) {
    this.timeMon = timeMon;
  }

  @Column(name = "KEY_CODE")
  public Long getKeyCode() {
    return keyCode;
  }

  public void setKeyCode(Long keyCode) {
    this.keyCode = keyCode;
  }

  @Column(name = "KEY_TYPE")
  public Integer getKeyType() {
    return keyType;
  }

  public void setKeyType(Integer keyType) {
    this.keyType = keyType;
  }

  @Column(name = "TITLE")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Column(name = "CNT")
  public Long getCnt() {
    return cnt;
  }

  public void setCnt(Long cnt) {
    this.cnt = cnt;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }



}
