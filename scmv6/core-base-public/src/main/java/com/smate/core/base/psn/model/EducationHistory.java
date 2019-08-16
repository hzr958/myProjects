package com.smate.core.base.psn.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.smate.core.base.utils.common.HtmlUtils;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 教育经历.
 * 
 * @author zt
 * @version 3.0
 */
@Entity
@Table(name = "PSN_EDU_HISTORY")
public class EducationHistory implements Serializable {

  private static final long serialVersionUID = 5854947717929011867L;
  /**
   * 主键.
   */
  private Long eduId;

  // 用户ID
  private Long psnId;

  private Long insId;
  /**
   * 机构名.
   */
  private String insName;
  /**
   * 专业.
   */
  private String study;
  /**
   * 学位.
   */
  private String degree;

  /**
   * 学位名称.
   */
  private String degreeName;

  /**
   * 开始年份.
   */
  private Long fromYear;
  /**
   * 结束月份.
   */
  private Long fromMonth;
  /**
   * 结束年份.
   */
  private Long toYear;
  /**
   * 结束月份.
   */
  private Long toMonth;

  /**
   * 加密ID.
   */
  private String des3Id;

  /**
   * 是否为首要单位.
   */
  private Long isPrimary;

  /**
   * 教育描述.
   */
  private String description;

  /**
   * 隐私设置：0公开，1好友，2自己.
   */
  private String authority = "0";

  private String forwardUrl;

  private Integer anyUser;

  // 编辑教育经历时使用.
  private Map<Long, String> degreeList;
  private String escapeInsName;
  // 错误信息
  private String insNameError;
  private String studyError;
  private String degreeError;
  private String errorMsg;
  private String des3PsnId; // 加密的人员ID
  private String eduDesc; // 除单位外，其他信息拼接成的字符串
  private String insImgPath; // 单位对应的图片

  private Long isActive; // 至今

  public EducationHistory() {
    super();
  }

  public EducationHistory(Long eduId, Long psnId, Long insId, String insName) {
    super();
    this.eduId = eduId;
    this.psnId = psnId;
    this.insId = insId;
    this.insName = insName;
  }

  public EducationHistory(Long insId, String insName) {
    super();
    this.insId = insId;
    this.insName = insName;
  }

  public EducationHistory(Long insId, String insName, String study, String degreeName) {
    super();
    this.insId = insId;
    this.insName = insName;
    this.study = study;
    this.degreeName = degreeName;
  }

  /**
   * 教育经历去重复的
   * 
   * @param insId
   * @param insName
   * @param toYear
   * @param toMonth
   */
  public EducationHistory(Long insId, String insName, Long toYear, Long toMonth) {
    super();
    this.insId = insId;
    this.insName = insName;
    this.toYear = toYear;
    this.toMonth = toMonth;
  }

  public EducationHistory(Long eduId, Long psnId, String insName, String study, String degreeName, Long fromYear,
      Long fromMonth, Long toYear, Long toMonth) {
    super();
    this.eduId = eduId;
    this.psnId = psnId;
    this.insName = insName;
    this.study = study;
    this.degreeName = degreeName;
    this.fromYear = fromYear;
    this.fromMonth = fromMonth;
    this.toYear = toYear;
    this.toMonth = toMonth;
  }

  /**
   * @return educationId
   */
  @Id
  @Column(name = "EDU_ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_EDU_HISTORY", allocationSize = 1)
  public Long getEduId() {
    return eduId;
  }

  /**
   * @param educationId
   */
  public void setEduId(Long eduId) {
    this.eduId = eduId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  /**
   * @return the insId
   */
  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  /**
   * @param insId the insId to set
   */
  public void setInsId(Long insId) {
    this.insId = insId;
  }

  /**
   * @return institutionName
   */
  @Column(name = "INS_NAME")
  public String getInsName() {
    return insName;
  }

  /**
   * @param institutionName
   */
  public void setInsName(String insName) {
    this.insName = insName;
  }

  /**
   * @return study
   */
  @Column(name = "STUDY")
  public String getStudy() {
    return study;
  }

  /**
   * @param study
   */
  public void setStudy(String study) {
    this.study = study;
  }

  /**
   * @return degree
   */
  @Column(name = "DEGREE")
  public String getDegree() {
    return degree;
  }

  /**
   * @param degree
   */
  public void setDegree(String degree) {
    this.degree = degree;
  }

  /**
   * @return fromYear
   */
  @Column(name = "FROM_YEAR")
  public Long getFromYear() {
    return fromYear;
  }

  /**
   * @param fromYear
   */
  public void setFromYear(Long fromYear) {
    this.fromYear = fromYear;
  }

  /**
   * @return fromMonth
   */
  @Column(name = "FROM_MONTH")
  public Long getFromMonth() {
    return fromMonth;
  }

  /**
   * @param fromMonth
   */
  public void setFromMonth(Long fromMonth) {
    this.fromMonth = fromMonth;
  }

  /**
   * @return toYear
   */
  @Column(name = "TO_YEAR")
  public Long getToYear() {
    return toYear;
  }

  /**
   * @param toYear
   */
  public void setToYear(Long toYear) {
    this.toYear = toYear;
  }

  /**
   * @return toMonth
   */
  @Column(name = "TO_MONTH")
  public Long getToMonth() {
    return toMonth;
  }

  /**
   * @param toMonth
   */
  public void setToMonth(Long toMonth) {
    this.toMonth = toMonth;
  }

  @Column(name = "DEGREE_NAME")
  public String getDegreeName() {
    return degreeName;
  }

  public void setDegreeName(String degreeName) {
    this.degreeName = degreeName;
  }

  @Transient
  public String getDes3Id() {

    if (this.eduId != null && des3Id == null) {
      try {
        des3Id = URLEncoder.encode(ServiceUtil.encodeToDes3(this.eduId.toString()), "utf-8");
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }
    return des3Id;
  }

  @Transient
  public Map<Long, String> getDegreeList() {
    return degreeList;
  }

  public void setDegreeList(Map<Long, String> degreeList) {
    this.degreeList = degreeList;
  }

  public void setDes3Id(String des3Id) {
    this.des3Id = des3Id;
  }

  @Column(name = "IS_PRIMARY")
  public Long getIsPrimary() {
    return isPrimary;
  }

  public void setIsPrimary(Long isPrimary) {
    this.isPrimary = isPrimary;
  }

  @Transient
  public String getEscapeInsName() {
    if (this.insName != null)
      escapeInsName = HtmlUtils.toHtml(this.insName);
    return escapeInsName;
  }

  public void setEscapeInsName(String escapeInsName) {
    this.escapeInsName = escapeInsName;
  }

  @Column(name = "DESCRIPTION")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Transient
  public String getAuthority() {
    return authority;
  }

  public void setAuthority(String authority) {
    this.authority = authority;
  }

  @Transient
  public String getInsNameError() {
    return insNameError;
  }

  public void setInsNameError(String insNameError) {
    this.insNameError = insNameError;
  }

  @Transient
  public String getStudyError() {
    return studyError;
  }

  public void setStudyError(String studyError) {
    this.studyError = studyError;
  }

  @Transient
  public String getDegreeError() {
    return degreeError;
  }

  public void setDegreeError(String degreeError) {
    this.degreeError = degreeError;
  }

  @Transient
  public String getErrorMsg() {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  @Transient
  public String getForwardUrl() {
    return forwardUrl;
  }

  public void setForwardUrl(String forwardUrl) {
    this.forwardUrl = forwardUrl;
  }

  @Transient
  public Integer getAnyUser() {
    return anyUser;
  }

  public void setAnyUser(Integer anyUser) {
    this.anyUser = anyUser;
  }

  @Transient
  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  @Transient
  public String getEduDesc() {
    return eduDesc;
  }

  public void setEduDesc(String eduDesc) {
    this.eduDesc = eduDesc;
  }

  @Transient
  public String getInsImgPath() {
    return insImgPath;
  }

  public void setInsImgPath(String insImgPath) {
    this.insImgPath = insImgPath;
  }

  @Column(name = "IS_ACTIVE")
  public Long getIsActive() {
    return isActive;
  }

  public void setIsActive(Long isActive) {
    this.isActive = isActive;
  }

}
