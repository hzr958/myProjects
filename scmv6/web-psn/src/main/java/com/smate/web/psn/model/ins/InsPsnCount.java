package com.smate.web.psn.model.ins;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 个人工作、教育经历的机构信息统计
 * 
 * @author xiexing
 * @date 2019年1月24日
 */
@Entity
@Table(name = "V_INS_PSN_COUNT")
public class InsPsnCount implements Serializable {
  private static final long serialVersionUID = -2182110301081008460L;
  // 机构id
  private Long insId;
  // 机构名称拼音
  private String pinYin;
  // 机构名称首字母
  private String firstLetter;
  // 机构人数
  private Integer historyPsnCount;
  // 更新时间
  private Date updateDate;
  // 机构中文名称
  private String zhName;
  // 机构英文名称
  private String enName;


  @Id
  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Column(name = "PIN_YIN")
  public String getPinYin() {
    return pinYin;
  }

  public void setPinYin(String pinYin) {
    this.pinYin = pinYin;
  }

  @Column(name = "FIRST_LETTER")
  public String getFirstLetter() {
    return firstLetter;
  }

  public void setFirstLetter(String firstLetter) {
    this.firstLetter = firstLetter;
  }

  @Column(name = "HISTORY_PSN_COUNT")
  public Integer getHistoryPsnCount() {
    return historyPsnCount;
  }

  public void setHistoryPsnCount(Integer historyPsnCount) {
    this.historyPsnCount = historyPsnCount;
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  @Column(name = "ZH_NAME")
  public String getZhName() {
    return zhName;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  @Column(name = "EN_NAME")
  public String getEnName() {
    return enName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  public InsPsnCount(Long insId, String pinYin, String firstLetter, Integer historyPsnCount, Date updateDate,
      String zhName, String enName) {
    super();
    this.insId = insId;
    this.pinYin = pinYin;
    this.firstLetter = firstLetter;
    this.historyPsnCount = historyPsnCount;
    this.updateDate = updateDate;
    this.zhName = zhName;
    this.enName = enName;
  }

  public InsPsnCount() {
    super();
    // TODO Auto-generated constructor stub
  }
}
