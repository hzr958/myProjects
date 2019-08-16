package com.smate.center.task.model.rol.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 机构统计数表
 * 
 * @author wsn
 *
 */
@Entity
@Table(name = "INS_STATISTICS")
public class InsStatisticsRol implements Serializable {

  private static final long serialVersionUID = 3961049497317065771L;
  private Integer pubSum;// 成果总数
  private Integer prjSum; // 项目总数
  private Integer psnSum; // 人员总数
  private Long insId; // 机构ID
  private String zhName; // 机构中文名称
  private String enName; // 机构英文名称
  private String insZhAdmins; // 单位管理员
  private String insEnAdmins; // 单位管理员

  public InsStatisticsRol() {
    super();
  }

  public InsStatisticsRol(Integer pubSum, Integer prjSum, Integer psnSum, Long insId, String zhName, String enName,
      String insZhAdmins, String insEnAdmins) {
    super();
    this.pubSum = pubSum;
    this.prjSum = prjSum;
    this.psnSum = psnSum;
    this.insId = insId;
    this.zhName = zhName;
    this.enName = enName;
    this.insZhAdmins = insZhAdmins;
    this.insEnAdmins = insEnAdmins;
  }

  @Id
  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
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

  @Column(name = "PUB_NUM")
  public Integer getPubSum() {
    return pubSum;
  }

  public void setPubSum(Integer pubSum) {
    this.pubSum = pubSum;
  }

  @Column(name = "PRJ_NUM")
  public Integer getPrjSum() {
    return prjSum;
  }

  public void setPrjSum(Integer prjSum) {
    this.prjSum = prjSum;
  }

  @Column(name = "PSN_NUM")
  public Integer getPsnSum() {
    return psnSum;
  }

  public void setPsnSum(Integer psnSum) {
    this.psnSum = psnSum;
  }

  @Column(name = "INS_ZH_ADMINS")
  public String getInsZhAdmins() {
    return insZhAdmins;
  }

  public void setInsZhAdmins(String insZhAdmins) {
    this.insZhAdmins = insZhAdmins;
  }

  @Column(name = "INS_EN_ADMINS")
  public String getInsEnAdmins() {
    return insEnAdmins;
  }

  public void setInsEnAdmins(String insEnAdmins) {
    this.insEnAdmins = insEnAdmins;
  }

}
