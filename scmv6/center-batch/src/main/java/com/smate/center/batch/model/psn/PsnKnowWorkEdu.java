package com.smate.center.batch.model.psn;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 推荐人员，人员工作经历，教育经历所有单位，定时更新.
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "PSN_KNOW_WORK_EDU")
public class PsnKnowWorkEdu implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5091557636590258209L;
  // pk
  private Long psnId;
  private String name;
  private String firstName;
  private String lastName;
  private String headUrl;// 推荐头像url
  private String viewTitel;// 推荐好友头衔:头衔->单位->国家地区
  private String workInsIds;
  private String eduInsIds;
  private String workInsNames;
  private String eduInsNames;
  private Long primaryInsId;
  // 注册时间
  private Date regDate;

  public PsnKnowWorkEdu() {
    super();
  }

  @Id
  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "NAME")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "FIRST_NAME")
  public String getFirstName() {
    if (StringUtils.isNotBlank(firstName) && firstName.length() > 50) {
      firstName = firstName.substring(0, 50);
    }
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @Column(name = "LAST_NAME")
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Column(name = "HEAD_URL")
  public String getHeadUrl() {
    return headUrl;
  }

  public void setHeadUrl(String headUrl) {
    this.headUrl = headUrl;
  }

  @Column(name = "VIEW_TITLE")
  public String getViewTitel() {
    return viewTitel;
  }

  public void setViewTitel(String viewTitel) {
    this.viewTitel = viewTitel;
  }

  @Column(name = "WORK_INSIDS")
  public String getWorkInsIds() {
    return workInsIds;
  }

  public void setWorkInsIds(String workInsIds) {
    this.workInsIds = workInsIds;
  }

  @Column(name = "EDU_INSIDS")
  public String getEduInsIds() {
    return eduInsIds;
  }

  public void setEduInsIds(String eduInsIds) {
    this.eduInsIds = eduInsIds;
  }

  @Column(name = "WORK_INSNAMES")
  public String getWorkInsNames() {
    return workInsNames;
  }

  public void setWorkInsNames(String workInsNames) {
    this.workInsNames = workInsNames;
  }

  @Column(name = "EDU_INSNAMES")
  public String getEduInsNames() {
    return eduInsNames;
  }

  public void setEduInsNames(String eduInsNames) {
    this.eduInsNames = eduInsNames;
  }

  @Column(name = "PRIMARY_INSID")
  public Long getPrimaryInsId() {
    return primaryInsId;
  }

  public void setPrimaryInsId(Long primaryInsId) {
    this.primaryInsId = primaryInsId;
  }

  @Column(name = "REG_DATE")
  public Date getRegDate() {
    return regDate;
  }

  public void setRegDate(Date regDate) {
    this.regDate = regDate;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
