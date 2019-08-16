package com.smate.web.psn.model.psninfo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 人员信息冗余刷新.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PSN_REFRESH_USER_INFO")
public class PsnRefreshUserInfo implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5520137916911015717L;
  // 主键id
  private Long id;
  // 人员ID
  private Long psnId;
  // 见表PSN_REFRESH_PUB_INFO刷新
  private Integer pub = 0;
  // 自填关键词刷新
  private Integer kwZt = 0;
  // 人员单位信息
  private Integer ins = 0;
  // 人员职称信息
  private Integer position = 0;
  // 状态：0待处理，9错误
  private Integer status = 0;
  // 用户姓名刷新
  private Integer nameFlag = 0;
  // 用户EMAIL刷新
  private Integer emailFlag = 0;
  // 用户工作经历刷新
  private Integer workFlag = 0;
  // lgk:人员与成果信息
  private Integer pubToPdwh = 0;
  // 学位
  private Integer degree = 0;
  // 人员领域大类
  private Integer areaClf = 0;
  // 人员参考文献刷新
  private Integer refc = 0;

  // 所教课程刷新
  private Integer courseFlag = 0;

  public PsnRefreshUserInfo() {
    super();
  }

  public PsnRefreshUserInfo(Long psnId) {
    super();
    this.psnId = psnId;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_REFRESH_USER_INFO", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "PUB")
  public Integer getPub() {
    return pub;
  }

  @Column(name = "KW_ZT")
  public Integer getKwZt() {
    return kwZt;
  }

  @Column(name = "INS")
  public Integer getIns() {
    return ins;
  }

  @Column(name = "POSITION")
  public Integer getPosition() {
    return position;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  @Column(name = "DEGREE")
  public Integer getDegree() {
    return degree;
  }

  @Column(name = "AREA_CLF")
  public Integer getAreaClf() {
    return areaClf;
  }

  @Column(name = "REFC")
  public Integer getRefc() {
    return refc;
  }

  @Column(name = "COURSE_FLAG")
  public Integer getCourseFlag() {
    return courseFlag;
  }

  public void setCourseFlag(Integer courseFlag) {
    this.courseFlag = courseFlag;
  }

  public void setRefc(Integer refc) {
    this.refc = refc;
  }

  public void setAreaClf(Integer areaClf) {
    this.areaClf = areaClf;
  }

  public void setDegree(Integer degree) {
    this.degree = degree;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setPub(Integer pub) {
    this.pub = pub;
  }

  public void setKwZt(Integer kwZt) {
    this.kwZt = kwZt;
  }

  public void setIns(Integer ins) {
    this.ins = ins;
  }

  public void setPosition(Integer position) {
    this.position = position;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(name = "NAME_FLAG")
  public Integer getNameFlag() {
    return nameFlag;
  }

  @Column(name = "EMAIL_FLAG")
  public Integer getEmailFlag() {
    return emailFlag;
  }

  @Column(name = "WORK_FLAG")
  public Integer getWorkFlag() {
    return workFlag;
  }

  public void setNameFlag(Integer nameFlag) {
    this.nameFlag = nameFlag;
  }

  public void setEmailFlag(Integer emailFlag) {
    this.emailFlag = emailFlag;
  }

  public void setWorkFlag(Integer workFlag) {
    this.workFlag = workFlag;
  }

  @Column(name = "PUB_TO_PDWH")
  public Integer getPubToPdwh() {
    return pubToPdwh;
  }

  public void setPubToPdwh(Integer pubToPdwh) {
    this.pubToPdwh = pubToPdwh;
  }

}
