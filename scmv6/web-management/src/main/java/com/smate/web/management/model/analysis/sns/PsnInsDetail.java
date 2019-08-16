package com.smate.web.management.model.analysis.sns;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 人员首要单位信息(如果科研之友没有，则使用科研在线信息补充，取等级最高的一个).
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PSN_INS_DETAIL")
public class PsnInsDetail implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -1948835151128665596L;
  // 人员ID
  private Long psnId;
  // 人员单位ID
  private Long insId;
  // 单位中文名
  private String zhName;
  // 单位英文名
  private String enName;
  // 单位部门中文名
  private String deptZhName;
  // 单位部门英名
  private String deptEnName;
  private Long deptZhHash;
  private Long deptEnHash;
  // 单位等级
  private Integer grade = 4;

  @Id
  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "ZH_NAME")
  public String getZhName() {
    return zhName;
  }

  @Column(name = "EN_NAME")
  public String getEnName() {
    return enName;
  }

  @Column(name = "GRADE")
  public Integer getGrade() {
    return grade;
  }

  @Column(name = "DEPT_ZHNAME")
  public String getDeptZhName() {
    return deptZhName;
  }

  @Column(name = "DEPT_ENNAME")
  public String getDeptEnName() {
    return deptEnName;
  }

  @Column(name = "DEPT_ZHHASH")
  public Long getDeptZhHash() {
    return deptZhHash;
  }

  @Column(name = "DEPT_ENHASH")
  public Long getDeptEnHash() {
    return deptEnHash;
  }

  public void setDeptZhHash(Long deptZhHash) {
    this.deptZhHash = deptZhHash;
  }

  public void setDeptEnHash(Long deptEnHash) {
    this.deptEnHash = deptEnHash;
  }

  public void setDeptZhName(String deptZhName) {
    this.deptZhName = deptZhName;
  }

  public void setDeptEnName(String deptEnName) {
    this.deptEnName = deptEnName;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  public void setGrade(Integer grade) {
    this.grade = grade;
  }

}
