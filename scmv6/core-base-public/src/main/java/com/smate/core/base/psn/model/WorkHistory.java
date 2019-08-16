package com.smate.core.base.psn.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.smate.core.base.utils.common.HtmlUtils;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 工作经历.
 * 
 * @author zt
 * 
 */
@Entity
@Table(name = "PSN_WORK_HISTORY")
public class WorkHistory implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -3124005936982418143L;
  /**
   * 主健.
   */
  private Long workId;

  private Long psnId;

  private Long insId;
  /**
   * 机构名称.
   */
  private String insName;
  /**
   * 职务.
   */
  private String position;
  private Long posId;
  private Integer posGrades;
  /**
   * 部门.
   */
  private String department;
  /**
   * 
   */
  private Long isActive;
  /**
   * 是否为首要工作单位.
   */
  private Long isPrimary;
  /**
   * 开始年份.
   */
  private Long fromYear;
  /**
   * 开始月份.
   */
  private Long fromMonth;
  /**
   * 截至年份.
   */
  private Long toYear;
  /**
   * 截至月份.
   */
  private Long toMonth;

  /**
   * 加密ID.
   */
  private String des3Id;
  private String escapeInsName;

  /**
   * 工作描述.
   */
  private String description;
  /**
   * 隐私设置：0公开，1好友，2自己.
   */
  private String authority = "0";
  /**
   * 更新头衔
   */
  private Long updateTitle;
  /**
   * 头衔
   */
  private String title;

  private Integer anyUser;

  private String forwardUrl;

  // 错误信息
  private String errorMsg;
  private String workInsNameError;
  private String positionError;
  private String departmentError;
  private String workDesc; // 工作经历除单位名称，其余信息拼接而成的字符串
  private String insImgPath; // 工作经历单位对应的图片路径
  private String workInsAddr; // 工作经历单位所在地区
  private String workInsInfoStr; // 工作经历单位、部门、职称拼接成的字符串
  private Long regionId; // 单位所在地区ID
  private String operateType; // 操作 addPrimary: 添加工作经历

  /**
   * 
   */
  public WorkHistory() {

  }

  /**
   * 自动提示的最小构造函数.
   * 
   * @param insId
   * @param insName
   */
  public WorkHistory(Long insId, String insName) {
    this.insId = insId;
    this.insName = insName;
  }

  public WorkHistory(Long insId, String insName, String department, String position) {
    this.insId = insId;
    this.insName = insName;
    this.department = department;
    this.position = position;
  }

  public WorkHistory(String insName, String department, String position) {
    this.insName = insName;
    this.department = department;
    this.position = position;
  }

  /**
   * 构造函数.
   * 
   * @param workId
   * @param psnId
   * @param insId
   * @param insName
   * @param profTitle
   * @param profTtitleName
   * @param position
   * @param department
   */
  public WorkHistory(Long workId, Long psnId, Long insId, String insName, String position, Long posId,
      Integer posGrades, String department) {
    this.workId = workId;
    this.psnId = psnId;
    this.insId = insId;
    this.insName = insName;
    this.position = position;
    this.posId = posId;
    this.posGrades = posGrades;
    this.department = department;
  }

  /**
   * 给我的评价，工作经历下拉列表.
   * 
   * @param workId
   * @param insId
   * @param insName
   */
  public WorkHistory(Long workId, Long insId, String insName) {
    super();
    this.workId = workId;
    this.insId = insId;
    this.insName = insName;
  }

  /**
   * 查找工作经历不重复的
   * 
   * @param insId
   * @param insName
   * @param toYear
   * @param toMonth
   */
  public WorkHistory(Long insId, String insName, Long toYear, Long toMonth) {
    super();
    this.insId = insId;
    this.insName = insName;
    this.toYear = toYear;
    this.toMonth = toMonth;
  }

  /**
   * @return id
   */
  @Id
  @Column(name = "WORK_ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_WORK_HISTORY", allocationSize = 1)
  public Long getWorkId() {
    return workId;
  }

  /**
   * @param id
   */
  public void setWorkId(Long workId) {
    this.workId = workId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  /**
   * @param person
   */
  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  /**
   * @param insId the insId to set
   */
  public void setInsId(Long insId) {
    this.insId = insId;
  }

  /**
   * @return the insId
   */
  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
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
   * @return department
   */
  @Column(name = "DEPARTMENT")
  public String getDepartment() {
    return department;
  }

  /**
   * @param department
   */
  public void setDepartment(String department) {
    this.department = department;
  }

  /**
   * @return isActive
   */
  @Column(name = "IS_ACTIVE")
  public Long getIsActive() {
    return isActive;
  }

  /**
   * @param isActive
   */
  public void setIsActive(Long isActive) {
    this.isActive = isActive;
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

  @Column(name = "POSITION")
  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  @Column(name = "POS_ID")
  public Long getPosId() {
    return posId;
  }

  @Column(name = "POS_GRADES")
  public Integer getPosGrades() {
    return posGrades;
  }

  public void setPosId(Long posId) {
    this.posId = posId;
  }

  public void setPosGrades(Integer posGrades) {
    this.posGrades = posGrades;
  }

  @Transient
  public String getDes3Id() {
    if (this.workId != null && des3Id == null) {
      try {
        des3Id = URLEncoder.encode(ServiceUtil.encodeToDes3(this.workId.toString()), "utf-8");
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }
    return des3Id;
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

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
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
  public Long getUpdateTitle() {
    return updateTitle;
  }

  public void setUpdateTitle(Long updateTitle) {
    this.updateTitle = updateTitle;
  }

  @Transient
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Transient
  public String getErrorMsg() {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  @Transient
  public String getWorkInsNameError() {
    return workInsNameError;
  }

  public void setWorkInsNameError(String insNameError) {
    this.workInsNameError = insNameError;
  }

  @Transient
  public String getPositionError() {
    return positionError;
  }

  public void setPositionError(String positionError) {
    this.positionError = positionError;
  }

  @Transient
  public String getDepartmentError() {
    return departmentError;
  }

  public void setDepartmentError(String departmentError) {
    this.departmentError = departmentError;
  }

  @Transient
  public Integer getAnyUser() {
    return anyUser;
  }

  public void setAnyUser(Integer anyUser) {
    this.anyUser = anyUser;
  }

  @Transient
  public String getForwardUrl() {
    return forwardUrl;
  }

  public void setForwardUrl(String forwardUrl) {
    this.forwardUrl = forwardUrl;
  }

  @Transient
  public String getWorkDesc() {
    return workDesc;
  }

  public void setWorkDesc(String workDesc) {
    this.workDesc = workDesc;
  }

  @Transient
  public String getInsImgPath() {
    return insImgPath;
  }

  public void setInsImgPath(String insImgPath) {
    this.insImgPath = insImgPath;
  }

  @Transient
  public String getWorkInsAddr() {
    return workInsAddr;
  }

  public void setWorkInsAddr(String workInsAddr) {
    this.workInsAddr = workInsAddr;
  }

  @Transient
  public String getWorkInsInfoStr() {
    return workInsInfoStr;
  }

  public void setWorkInsInfoStr(String workInsInfoStr) {
    this.workInsInfoStr = workInsInfoStr;
  }

  @Transient
  public Long getRegionId() {
    return regionId;
  }

  public void setRegionId(Long regionId) {
    this.regionId = regionId;
  }

  @Transient
  public String getOperateType() {
    return operateType;
  }

  public void setOperateType(String operateType) {
    this.operateType = operateType;
  }

}
