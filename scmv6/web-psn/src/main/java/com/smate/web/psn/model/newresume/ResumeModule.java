package com.smate.web.psn.model.newresume;

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
 * 简历模块实体
 * 
 * @author wsn
 *
 */
@Entity
@Table(name = "V_RESUME_MODULE")
public class ResumeModule implements Serializable {

  private static final long serialVersionUID = 4091626076350564715L;

  private Long id; // 主键
  private Long resumeId; // 简历ID
  private Integer moduleId; // 模块ID
  private Integer moduleSeq; // 模块顺序，暂时用不到
  private String moduleTitle; // 模块标题
  private Date updateTime; // 模块信息更新时间
  private Long moduleInfoId; // 模块信息表的主键
  private Integer status = 0; // 模块状态默认有效为0，1为无效

  public ResumeModule() {
    super();
  }

  public ResumeModule(Long id, Long resumeId, Integer moduleId, Integer moduleSeq, String moduleTitle, Date updateTime,
      Long moduleInfoId, Integer status) {
    super();
    this.id = id;
    this.resumeId = resumeId;
    this.moduleId = moduleId;
    this.moduleSeq = moduleSeq;
    this.moduleTitle = moduleTitle;
    this.updateTime = updateTime;
    this.moduleInfoId = moduleInfoId;
    this.status = status;
  }

  public ResumeModule(Long resumeId, Integer moduleId, Integer status) {
    super();
    this.resumeId = resumeId;
    this.moduleId = moduleId;
    this.status = status;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_RESUME_MODULE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "CV_ID")
  public Long getResumeId() {
    return resumeId;
  }

  public void setResumeId(Long resumeId) {
    this.resumeId = resumeId;
  }

  @Column(name = "MODULE_ID")
  public Integer getModuleId() {
    return moduleId;
  }

  public void setModuleId(Integer moduleId) {
    this.moduleId = moduleId;
  }

  @Column(name = "MODULE_SEQ")
  public Integer getModuleSeq() {
    return moduleSeq;
  }

  public void setModuleSeq(Integer moduleSeq) {
    this.moduleSeq = moduleSeq;
  }

  @Column(name = "MODULE_TITLE")
  public String getModuleTitle() {
    return moduleTitle;
  }

  public void setModuleTitle(String moduleTitle) {
    this.moduleTitle = moduleTitle;
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  @Column(name = "MODULE_INFO_ID")
  public Long getModuleInfoId() {
    return moduleInfoId;
  }

  public void setModuleInfoId(Long moduleInfoId) {
    this.moduleInfoId = moduleInfoId;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
