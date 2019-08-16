package com.smate.center.open.model.prj;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 第三方项目成员中间表
 * 
 * @author LXZ SEQ_V_OPEN_PROJECT
 * @since 6.0.1
 * @version 6.0.1
 * @return
 */
@Entity
@Table(name = "V_OPEN_PROJECT")
public class OpenProject implements Serializable {

  private static final long serialVersionUID = 8082043807881543377L;
  // 项目id
  @Id
  @Column(name = "PROJECT_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_V_OPEN_PROJECT", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;
  // 第三方TOKEN
  @Column(name = "TOKEN")
  private String token;
  // "本机构中使用的项目编号
  @Column(name = "PRJ_INTERNAL_NO")
  private String internalNo;
  // "资助机构定义的项目编号
  @Column(name = "PRJ_EXTERNAL_NO")
  private String externalNo;
  // 资金总数
  @Column(name = "AMOUNT")
  private BigDecimal amount;
  // 参与方式：主导单位1/参与单位0
  @Column(name = "IS_PRINCIPAL_INS")
  private Integer isPrincipalIns;
  // 项目状态01进行中/02完成/03其他,04:申请项目
  @Column(name = "PRJ_STATE")
  private String state;
  // 资助类别id
  @Column(name = "SCHEME_ID")
  private Long schemeId;
  // 项目年度
  @Column(name = "FUNDING_YEAR")
  private Integer fundingYear;
  // 资助机构名称
  @Column(name = "SCHEME_AGENCY_NAME")
  private String agencyName;
  // 资助类别名称
  @Column(name = "SCHEME_NAME")
  private String schemeName;
  // 项目类型,1：内部项目，0:外部项目
  @Column(name = "PRJ_TYPE")
  private String type;
  // 开始年份
  @Column(name = "START_YEAR")
  private Integer startYear;
  // 开始月份
  @Column(name = "START_MONTH")
  private Integer startMonth;
  // 开始日
  @Column(name = "START_DAY")
  private Integer startDay;
  // 结束年份
  @Column(name = "END_YEAR")
  private Integer endYear;
  // 结束月份
  @Column(name = "END_MONTH")
  private Integer endMonth;
  // 结束日
  @Column(name = "END_DAY")
  private Integer endDay;
  // 依托单位id
  @Column(name = "INS_ID")
  private Long insId;
  // 依托单位名称
  @Column(name = "INS_NAME")
  private String insName;
  // 所有者(对应 person的psn_id)
  @Column(name = "OWNER_PSN_ID")
  private Long psnId;
  // 对应的外部数据库 refrence to const_ref_db
  @Column(name = "SOURCE_DB_ID")
  private Integer dbId;
  // 中文标题
  @Column(name = "ZH_TITLE")
  private String zhTitle;
  // 外文标题
  @Column(name = "EN_TITLE")
  private String enTitle;
  // 作者
  @Column(name = "AUTHOR_NAMES")
  private String authorNames;
  // // 全文，fulltext_fileid为空，则取fulltext_url
  // @Column(name = "FULLTEXT_FILEID")
  // private String fulltextField;
  // // 全文
  // @Column(name = "FULLTEXT_URL")
  // private String fulltextUrl;
  // 资助机构ID
  @Column(name = "SCHEME_AGENCY_ID")
  private Long agencyId;
  // 资金单位
  @Column(name = "AMOUNT_UNIT")
  private String amountUnit;
  // 作者
  @Column(name = "AUTHOR_NAMES_EN")
  private String authorNamesEn;
  // 资助机构名称(英文)
  @Column(name = "SCHEME_AGENCY_NAME_EN")
  private String enAgencyName;
  // 资助类别名称(英文)
  @Column(name = "SCHEME_NAME_EN")
  private String enSchemeName;
  // 任务执行状态0:等待执行 1:重复 99:执行失败
  @Column(name = "task_status")
  private Integer taskStatus = 0;
  // openId
  @Column(name = "open_id")
  private Long openId;
  // 第三方系统中的唯一项目id
  @Column(name = "obj_id")
  private String objId;
  // 推送过来的时间
  @Column(name = "create_date")
  private Date createDate;

  /**
   * 非持久化
   */
  @Transient
  private List<OpenPrjMember> members;

  public OpenProject() {
    super();
  }

  public OpenProject(String objId, String token, String internalNo, String externalNo, Integer isPrincipalIns,
      String state, Long schemeId, Integer fundingYear, String agencyName, String schemeName, String type,
      Integer startYear, Integer startMonth, Integer startDay, Integer endYear, Integer endMonth, Integer endDay,
      Long insId, String insName, Long psnId, Integer dbId, String zhTitle, String enTitle, String authorNames,
      Long agencyId, BigDecimal amount, String amountUnit, String authorNamesEn, String enAgencyName,
      String enSchemeName, Long openId) {
    super();
    this.objId = objId;
    this.token = token;
    this.internalNo = internalNo;
    this.externalNo = externalNo;
    this.isPrincipalIns = isPrincipalIns;
    this.state = state;
    this.schemeId = schemeId;
    this.fundingYear = fundingYear;
    this.agencyName = agencyName;
    this.schemeName = schemeName;
    this.type = type;
    this.startYear = startYear;
    this.startMonth = startMonth;
    this.startDay = startDay;
    this.endYear = endYear;
    this.endMonth = endMonth;
    this.endDay = endDay;
    this.insId = insId;
    this.insName = insName;
    this.psnId = psnId;
    this.dbId = dbId;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.authorNames = authorNames;
    this.agencyId = agencyId;
    this.amount = amount;
    this.amountUnit = amountUnit;
    this.authorNamesEn = authorNamesEn;
    this.enAgencyName = enAgencyName;
    this.enSchemeName = enSchemeName;
    this.openId = openId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getInternalNo() {
    return internalNo;
  }

  public void setInternalNo(String internalNo) {
    this.internalNo = internalNo;
  }

  public String getExternalNo() {
    return externalNo;
  }

  public void setExternalNo(String externalNo) {
    this.externalNo = externalNo;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public Integer getIsPrincipalIns() {
    return isPrincipalIns;
  }

  public void setIsPrincipalIns(Integer isPrincipalIns) {
    this.isPrincipalIns = isPrincipalIns;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public Long getSchemeId() {
    return schemeId;
  }

  public void setSchemeId(Long schemeId) {
    this.schemeId = schemeId;
  }

  public Integer getFundingYear() {
    return fundingYear;
  }

  public void setFundingYear(Integer fundingYear) {
    this.fundingYear = fundingYear;
  }

  public String getAgencyName() {
    return agencyName;
  }

  public void setAgencyName(String agencyName) {
    this.agencyName = agencyName;
  }

  public String getSchemeName() {
    return schemeName;
  }

  public void setSchemeName(String schemeName) {
    this.schemeName = schemeName;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Integer getStartYear() {
    return startYear;
  }

  public void setStartYear(Integer startYear) {
    this.startYear = startYear;
  }

  public Integer getStartMonth() {
    return startMonth;
  }

  public void setStartMonth(Integer startMonth) {
    this.startMonth = startMonth;
  }

  public Integer getStartDay() {
    return startDay;
  }

  public void setStartDay(Integer startDay) {
    this.startDay = startDay;
  }

  public Integer getEndYear() {
    return endYear;
  }

  public void setEndYear(Integer endYear) {
    this.endYear = endYear;
  }

  public Integer getEndMonth() {
    return endMonth;
  }

  public void setEndMonth(Integer endMonth) {
    this.endMonth = endMonth;
  }

  public Integer getEndDay() {
    return endDay;
  }

  public void setEndDay(Integer endDay) {
    this.endDay = endDay;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Integer getDbId() {
    return dbId;
  }

  public void setDbId(Integer dbId) {
    this.dbId = dbId;
  }

  public String getZhTitle() {
    return zhTitle;
  }

  public void setZhTitle(String zhTitle) {
    this.zhTitle = zhTitle;
  }

  public String getEnTitle() {
    return enTitle;
  }

  public void setEnTitle(String enTitle) {
    this.enTitle = enTitle;
  }

  public String getAuthorNames() {
    return authorNames;
  }

  public void setAuthorNames(String authorNames) {
    this.authorNames = authorNames;
  }

  public Long getAgencyId() {
    return agencyId;
  }

  public void setAgencyId(Long agencyId) {
    this.agencyId = agencyId;
  }

  public String getAmountUnit() {
    return amountUnit;
  }

  public void setAmountUnit(String amountUnit) {
    this.amountUnit = amountUnit;
  }

  public String getAuthorNamesEn() {
    return authorNamesEn;
  }

  public void setAuthorNamesEn(String authorNamesEn) {
    this.authorNamesEn = authorNamesEn;
  }

  public String getEnAgencyName() {
    return enAgencyName;
  }

  public void setEnAgencyName(String enAgencyName) {
    this.enAgencyName = enAgencyName;
  }

  public String getEnSchemeName() {
    return enSchemeName;
  }

  public void setEnSchemeName(String enSchemeName) {
    this.enSchemeName = enSchemeName;
  }

  public Integer getTaskStatus() {
    return taskStatus;
  }

  public void setTaskStatus(Integer taskStatus) {
    this.taskStatus = taskStatus;
  }

  public String getObjId() {
    return objId;
  }

  public void setObjId(String objId) {
    this.objId = objId;
  }

  public List<OpenPrjMember> getMembers() {
    return members;
  }

  public void setMembers(List<OpenPrjMember> members) {
    this.members = members;
  }

  public Long getOpenId() {
    return openId;
  }

  public void setOpenId(Long openId) {
    this.openId = openId;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }
}
