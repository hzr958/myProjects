package com.smate.web.psn.model.personal.resume;



import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.smate.core.base.utils.string.ServiceUtil;



/**
 * 我的简历表.
 * 
 * @author zhuangyanming
 * 
 */
@Entity
@Table(name = "PERSONAL_RESUME")
public class PersonalResume implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6409658743196728552L;
  // 简历主键，10位随机数字
  private Long resumeId;
  // 简历Des3加密主键
  private String des3ResumeId;
  // 人员psn_id
  private Long psnId;
  // 简历名称
  private String resumeName;
  // 我的简历用途关联const_category.category='resume_purpose'
  private String purposeConf;
  // 我的简历主体关联const_category.category='resume_body'，多个用逗号隔开
  private String bodyConf;
  // 我的简历边栏关联const_category.category='resume_sidebar'，多个用逗号隔开
  private String sidebarConf;
  // 查看权限1=公开，2=凭密码查看
  private Integer viewAuthority;
  // 密码（用明文保存，有查看原始密码的功能）
  private String password;
  // 简历对外访问路径
  private String resumeUrl;
  // 公开简历修改后的地址
  private String preUrlPath;
  // 创建时间
  private Date createDate;
  // 更新时间
  private Date updateDate;
  // 语言
  private String language;
  // pdf版简历对外访问路径
  private String resumePdf;
  // word版简历对外访问路径
  private String resumeWord;
  // 简历共享说明
  private String recommendReason;
  // 设置是否为公开简历
  private Integer isPublicCV;
  // 0=简历图片重新生成；1=简历图片已生成(访问简历时触发)
  private Integer barimgUpdate = 0;
  // 用户修改的简历地址名称
  private String pubResumeUrl;

  @Id
  @Column(name = "RESUME_ID")
  public Long getResumeId() {
    return resumeId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "RESUME_NAME")
  public String getResumeName() {
    return resumeName;
  }

  @Column(name = "PURPOSE_CONF")
  public String getPurposeConf() {
    return purposeConf;
  }

  @Column(name = "BODY_CONF")
  public String getBodyConf() {
    return bodyConf;
  }

  @Column(name = "SIDEBAR_CONF")
  public String getSidebarConf() {
    return sidebarConf;
  }

  @Column(name = "VIEW_AUTHORITY")
  public Integer getViewAuthority() {
    return viewAuthority;
  }

  @Column(name = "PASSWORD")
  public String getPassword() {
    return password;
  }

  @Column(name = "RESUME_URL")
  public String getResumeUrl() {
    return resumeUrl;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  @Column(name = "LANGUAGE")
  public String getLanguage() {
    return language;
  }

  @Column(name = "RESUME_PDF")
  public String getResumePdf() {
    return resumePdf;
  }

  @Column(name = "RESUME_WORD")
  public String getResumeWord() {
    return resumeWord;
  }

  @Column(name = "RECOMMEND_REASON")
  public String getRecommendReason() {
    return recommendReason;
  }

  @Column(name = "ISPUBLIC_CV")
  public Integer getIsPublicCV() {
    return isPublicCV;
  }

  @Transient
  public String getPreUrlPath() {
    return preUrlPath;
  }

  public void setIsPublicCV(Integer isPublicCV) {
    this.isPublicCV = isPublicCV;
  }

  @Column(name = "BARIMG_UPDATE")
  public Integer getBarimgUpdate() {
    return barimgUpdate;
  }

  @Column(name = "PUBRESUME_URL")
  public String getPubResumeUrl() {
    return pubResumeUrl;
  }

  public void setResumeId(Long resumeId) {
    this.resumeId = resumeId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setResumeName(String resumeName) {
    this.resumeName = resumeName;
  }

  public void setPurposeConf(String purposeConf) {
    this.purposeConf = purposeConf;
  }

  public void setBodyConf(String bodyConf) {
    this.bodyConf = bodyConf;
  }

  public void setSidebarConf(String sidebarConf) {
    this.sidebarConf = sidebarConf;
  }

  public void setViewAuthority(Integer viewAuthority) {
    this.viewAuthority = viewAuthority;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setResumeUrl(String resumeUrl) {
    this.resumeUrl = resumeUrl;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public void setResumePdf(String resumePdf) {
    this.resumePdf = resumePdf;
  }

  public void setResumeWord(String resumeWord) {
    this.resumeWord = resumeWord;
  }

  public void setRecommendReason(String recommendReason) {
    this.recommendReason = recommendReason;
  }

  public void setPreUrlPath(String preUrlPath) {
    this.preUrlPath = preUrlPath;
  }

  public void setBarimgUpdate(Integer barimgUpdate) {
    this.barimgUpdate = barimgUpdate;
  }

  public void setPubResumeUrl(String pubResumeUrl) {
    this.pubResumeUrl = pubResumeUrl;
  }

  @Transient
  public String getDes3ResumeId() {
    if (resumeId != null && des3ResumeId == null) {
      des3ResumeId = ServiceUtil.encodeToDes3(resumeId.toString());
    }
    return des3ResumeId;
  }

  public void setDes3ResumeId(String des3ResumeId) {
    this.des3ResumeId = des3ResumeId;
  }


}
