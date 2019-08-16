package com.smate.web.group.model.grp.v8pub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * 成果基础类
 * 
 * @author houchuanjie
 * @date 2018/06/01 15:36
 */
@MappedSuperclass
public class PubPO implements Serializable {

  private static final long serialVersionUID = -315566658858742974L;

  /**
   * 成果类型
   */
  @Column(name = "PUB_TYPE")
  private Integer pubType;

  /**
   * 发表日期
   */
  @Column(name = "PUBLISH_YEAR")
  protected Integer publishYear;

  /**
   * 来源国家地区id
   */
  @Column(name = "REGION_ID")
  protected Long countryId;

  /**
   * 成果标题
   */
  @Column(name = "TITLE")
  protected String title;
  /**
   * 作者名称列表
   */
  @Column(name = "AUTHOR_NAMES")
  protected String authorNames;

  /**
   * 引用次数
   */
  @Column(name = "CITATIONS")
  protected Integer citations;

  /**
   * 简短描述
   */
  @Column(name = "BRIEF_DESC")
  protected String briefDesc;

  /**
   * 更新标记
   */
  @Column(name = "UPDATE_MARK")
  protected Integer updateMark;

  /**
   * 创建人psnId
   */
  @Column(name = "CREATE_PSN_ID")
  protected Long createPsnId;
  /**
   * 创建时间
   */
  @Column(name = "GMT_CREATE")
  protected Date gmtCreate;
  /**
   * 被修改时间
   */
  @Column(name = "GMT_MODIFIED")
  protected Date gmtModified;

  public Integer getPublishYear() {
    return publishYear;
  }

  public void setPublishYear(Integer publishYear) {
    this.publishYear = publishYear;
  }

  public Long getCountryId() {
    return countryId;
  }

  public void setCountryId(Long countryId) {
    this.countryId = countryId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthorNames() {
    return authorNames;
  }

  public void setAuthorNames(String authorNames) {
    this.authorNames = authorNames;
  }

  public String getBriefDesc() {
    return briefDesc;
  }

  public void setBriefDesc(String briefDesc) {
    this.briefDesc = briefDesc;
  }

  public Long getCreatePsnId() {
    return createPsnId;
  }

  public void setCreatePsnId(Long createPsnId) {
    this.createPsnId = createPsnId;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public void setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
  }

  public Date getGmtModified() {
    return gmtModified;
  }

  public void setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
  }

  public Integer getPubType() {
    return pubType;
  }

  public void setPubType(Integer pubType) {
    this.pubType = pubType;
  }

  public Integer getCitations() {
    return citations;
  }

  public void setCitations(Integer citations) {
    this.citations = citations;
  }

  public Integer getUpdateMark() {
    return updateMark;
  }

  public void setUpdateMark(Integer updateMark) {
    this.updateMark = updateMark;
  }

}
