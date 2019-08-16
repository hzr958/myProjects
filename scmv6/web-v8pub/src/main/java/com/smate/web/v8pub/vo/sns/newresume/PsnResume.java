package com.smate.web.v8pub.vo.sns.newresume;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 新的简历主表
 * 
 * @author wsn
 *
 */
@Entity
@Table(name = "V_PSN_RESUME")
public class PsnResume implements Serializable {

  private static final long serialVersionUID = -4216445657464307316L;
  private Long resumeId; // 主键， 简历ID
  private Long ownerPsnId; // 人员ID
  private String resumeName; // 简历名称
  private String resumeUrl; // 简历地址
  private Date createDate; // 创建时间
  private Integer resumeType; // 简历类型
  private Date updateDate; // 最近更新时间

  public PsnResume(Long resumeId, Long ownerPsnId, String resumeName, String resumeUrl, Date createDate,
      Integer resumeType, Date updateDate) {
    super();
    this.resumeId = resumeId;
    this.ownerPsnId = ownerPsnId;
    this.resumeName = resumeName;
    this.resumeUrl = resumeUrl;
    this.createDate = createDate;
    this.resumeType = resumeType;
    this.updateDate = updateDate;
  }

  public PsnResume() {
    super();
  }

  @Id
  @Column(name = "CV_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_RESUME", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getResumeId() {
    return resumeId;
  }

  public void setResumeId(Long resumeId) {
    this.resumeId = resumeId;
  }

  @Column(name = "OWNER_PSN_ID")
  public Long getOwnerPsnId() {
    return ownerPsnId;
  }

  public void setOwnerPsnId(Long ownerPsnId) {
    this.ownerPsnId = ownerPsnId;
  }

  @Column(name = "CV_NAME")
  public String getResumeName() {
    return resumeName;
  }

  public void setResumeName(String resumeName) {
    this.resumeName = resumeName;
  }

  @Column(name = "CV_URL")
  public String getResumeUrl() {
    return resumeUrl;
  }

  public void setResumeUrl(String resumeUrl) {
    this.resumeUrl = resumeUrl;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  @Column(name = "CV_TYPE")
  public Integer getResumeType() {
    return resumeType;
  }

  public void setResumeType(Integer resumeType) {
    this.resumeType = resumeType;
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

}
