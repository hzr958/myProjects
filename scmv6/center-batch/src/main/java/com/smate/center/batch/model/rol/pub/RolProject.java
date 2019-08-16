package com.smate.center.batch.model.rol.pub;

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

import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 项目主表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PROJECT")
public class RolProject implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1512762279039859465L;
  // 项目id
  private Long id;
  // "本机构中使用的项目编号
  private String internalNo;
  // "资助机构定义的项目编号
  private String externalNo;
  // 资金总数
  private BigDecimal amount;
  // 资金单位
  private String amountUnit;
  // 资金总数显示
  private String amountShow;
  // 参与方式：主导单位1/参与单位0
  private Integer isPrincipalIns;
  // 显示使用
  private String isPrincipalInsName;
  // 项目状态01进行中/02完成/03其他,04:申请项目
  private String state;
  // 项目状态名，显示使用
  private String stateName;
  // 项目年度
  private Integer fundingYear;
  // 资助机构名称
  private String agencyName;
  // 资助机构名称(英文)
  private String enAgencyName;
  // 资助机构ID
  private Long agencyId;
  // 资助类别id
  private Long schemeId;
  // 资助类别名称
  private String schemeName;
  // 资助类别名称(英文)
  private String enSchemeName;
  // 项目类型,1：内部项目，0:外部项目
  private String type;
  // 项目类型名称，用于列表显示
  private String typeName;
  // 开始年份
  private Integer startYear;
  // 开始月份
  private Integer startMonth;
  // 开始日
  private Integer startDay;
  // 结束年份
  private Integer endYear;
  // 结束月份
  private Integer endMonth;
  // 结束日
  private Integer endDay;
  // 依托单位id
  private Long insId;
  // 依托单位名称
  private String insName;
  // 项目属于哪个单位
  private Long ownerInsId;
  // 对应的外部数据库 refrence to const_ref_db
  private Integer dbId;
  // 最后编辑时间
  private Date updateDate;
  // 最后编辑人
  private Long updatePsnId;
  // 创建人
  private Long createPsnId;
  // 创建日期
  private Date createDate;
  // 版本号码
  private Integer versionNo;
  // 数据来源：0: 手工输入，1:数据库导入，2:文件导入
  private Integer recordFrom;
  // 0/1数据是否完整;0：数据不完整，1：数据完整
  private Integer isValid;
  // 中文标题
  private String zhTitle;
  // 外文标题
  private String enTitle;
  // 中文标题小写，用于查询
  private String zhTitleText;
  // 外文标题小写，用于查询
  private String enTitleText;
  // 中文标题hash_code，查重时使用
  private Integer zhTitleHash;
  // 英文标题hash_code，查重时使用
  private Integer enTitleHash;
  // 作者
  private String authorNames;
  // 来源
  private String briefDesc;
  // 来源
  private String briefDescEn;
  // 全文，fulltext_fileid为空，则取fulltext_url
  private String fulltextField;
  // 全文附件后缀
  private String fulltextExt;
  // 全文
  private String fulltextUrl;
  // 状态1: 已删除，0: 未删除
  private Integer status;
  // 加密Id.
  private String des3Id;
  // 查询表格显示用
  private String htmlAbstract;
  // 对应V2.6项目ID
  private Long oldPubId;

  // 全文附件节点ID
  private Integer fulltextNodeId;

  // 中文摘要，导出使用
  private String zhAbstract;
  // 外文文摘要，导出使用
  private String enAbstract;
  // 中文关键词，导出使用
  private String zhKeywords;
  // 外文关键词，导出使用
  private String enKeywords;
  // 备注，导出使用
  private String remark;
  // 科学关键词名字;
  private String disName;

  private String viewZhHtml;// 页面显示中文内容(页面查询显示用).
  private String viewEnHtml;// 页面显示英文内容(页面查询显示用).
  private List<RolPrjMember> showPrjMembers;// 项目的成员列表(查缺项页面查询显示用)_MJG_ROL-935.
  private Integer showPrjMemSize;// 项目的成员数(查缺项页面查询显示用)_MJG_ROL-935.
  private Integer prjReleaseStatus;// 项目是否公开状态显示内容.
  private Integer pubNum = 0;

  public RolProject() {
    super();
  }

  public RolProject(Long id) {
    super();
    this.id = id;
  }

  /**
   * 项目查缺项显示标签属性构造器_MJG_SIE项目查缺项功能改造_2014-08-12.
   * 
   * @param id
   * @param fundingYear
   * @param zhTitle
   * @param enTitle
   * @param authorNames
   * @param briefDesc
   * @param briefDescEn
   */
  public RolProject(Long id, Integer fundingYear) {
    super();
    this.id = id;
    this.fundingYear = fundingYear;
  }

  public RolProject(Long id, Integer fundingYear, String zhTitle, String enTitle) {
    super();
    this.id = id;
    this.fundingYear = fundingYear;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
  }

  /**
   * 项目列表显示标签属性构造器_MJG_SIE项目功能改造_2014-07-29.
   * 
   * @param id
   * @param fundingYear
   * @param zhTitle
   * @param enTitle
   * @param authorNames
   * @param briefDesc
   * @param briefDescEn
   */
  public RolProject(Long id, Integer fundingYear, String zhTitle, String enTitle, String authorNames, String briefDesc,
      String briefDescEn) {
    super();
    this.id = id;
    this.fundingYear = fundingYear;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.authorNames = authorNames;
    this.briefDesc = briefDesc;
    this.briefDescEn = briefDescEn;
  }

  @Id
  @Column(name = "PROJECT_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PROJECT", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "FULLTEXT_NODEID")
  public Integer getFulltextNodeId() {
    return fulltextNodeId;
  }

  public void setFulltextNodeId(Integer fulltextNodeId) {
    this.fulltextNodeId = fulltextNodeId;
  }

  @Column(name = "PRJ_INTERNAL_NO")
  public String getInternalNo() {
    return internalNo;
  }

  @Column(name = "PRJ_EXTERNAL_NO")
  public String getExternalNo() {
    return externalNo;
  }

  @Column(name = "AMOUNT")
  public BigDecimal getAmount() {
    return amount;
  }

  @Column(name = "AMOUNT_UNIT")
  public String getAmountUnit() {
    return amountUnit;
  }

  public void setAmountUnit(String amountUnit) {
    this.amountUnit = amountUnit;
  }

  @Column(name = "IS_PRINCIPAL_INS")
  public Integer getIsPrincipalIns() {
    return isPrincipalIns;
  }

  @Transient
  public String getIsPrincipalInsName() {
    return isPrincipalInsName;
  }

  public void setIsPrincipalInsName(String isPrincipalInsName) {
    this.isPrincipalInsName = isPrincipalInsName;
  }

  @Column(name = "PRJ_STATE")
  public String getState() {
    return state;
  }

  @Column(name = "FUNDING_YEAR")
  public Integer getFundingYear() {
    return fundingYear;
  }

  @Column(name = "SCHEME_AGENCY_NAME")
  public String getAgencyName() {
    return agencyName;
  }

  @Column(name = "SCHEME_AGENCY_ID")
  public Long getAgencyId() {
    return agencyId;
  }

  public void setAgencyId(Long agencyId) {
    this.agencyId = agencyId;
  }

  @Column(name = "SCHEME_ID")
  public Long getSchemeId() {
    return schemeId;
  }

  @Column(name = "SCHEME_NAME")
  public String getSchemeName() {
    return schemeName;
  }

  @Column(name = "PRJ_TYPE")
  public String getType() {
    return type;
  }

  @Column(name = "START_YEAR")
  public Integer getStartYear() {
    return startYear;
  }

  @Transient
  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  @Column(name = "START_MONTH")
  public Integer getStartMonth() {
    return startMonth;
  }

  @Column(name = "START_DAY")
  public Integer getStartDay() {
    return startDay;
  }

  @Column(name = "END_YEAR")
  public Integer getEndYear() {
    return endYear;
  }

  @Column(name = "END_MONTH")
  public Integer getEndMonth() {
    return endMonth;
  }

  @Column(name = "END_DAY")
  public Integer getEndDay() {
    return endDay;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "INS_NAME")
  public String getInsName() {
    return insName;
  }

  @Column(name = "SOURCE_DB_ID")
  public Integer getDbId() {
    return dbId;
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  @Column(name = "UPDATE_PSN_ID")
  public Long getUpdatePsnId() {
    return updatePsnId;
  }

  @Column(name = "CREATE_PSN_ID")
  public Long getCreatePsnId() {
    return createPsnId;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  @Column(name = "VERSION_NO")
  public Integer getVersionNo() {
    return versionNo;
  }

  @Column(name = "RECORD_FROM")
  public Integer getRecordFrom() {
    return recordFrom;
  }

  @Column(name = "IS_VALID")
  public Integer getIsValid() {
    return isValid;
  }

  @Column(name = "ZH_TITLE")
  public String getZhTitle() {
    return zhTitle;
  }

  @Column(name = "EN_TITLE")
  public String getEnTitle() {
    return enTitle;
  }

  @Column(name = "ZH_TITLE_HASH")
  public Integer getZhTitleHash() {
    return zhTitleHash;
  }

  @Column(name = "EN_TITLE_HASH")
  public Integer getEnTitleHash() {
    return enTitleHash;
  }

  @Column(name = "AUTHOR_NAMES")
  public String getAuthorNames() {
    return authorNames;
  }

  @Column(name = "BRIEF_DESC")
  public String getBriefDesc() {
    return briefDesc;
  }

  @Column(name = "BRIEF_DESC_EN")
  public String getBriefDescEn() {
    return briefDescEn;
  }

  @Column(name = "FULLTEXT_FILEID")
  public String getFulltextField() {
    return fulltextField;
  }

  @Column(name = "FULLTEXT_FILEEXT")
  public String getFulltextExt() {
    return fulltextExt;
  }

  public void setFulltextExt(String fulltextExt) {
    this.fulltextExt = fulltextExt;
  }

  @Column(name = "FULLTEXT_URL")
  public String getFulltextUrl() {
    return fulltextUrl;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  @Column(name = "OWNER_INS_ID")
  public Long getOwnerInsId() {
    return ownerInsId;
  }

  public void setOwnerInsId(Long ownerInsId) {
    this.ownerInsId = ownerInsId;
  }

  @Column(name = "OLD_PUB_ID")
  public Long getOldPubId() {
    return oldPubId;
  }

  @Column(name = "ZH_TITLE_TEXT")
  public String getZhTitleText() {
    return zhTitleText;
  }

  @Column(name = "EN_TITLE_TEXT")
  public String getEnTitleText() {
    return enTitleText;
  }

  public void setZhTitleText(String zhTitleText) {
    this.zhTitleText = zhTitleText;
  }

  public void setEnTitleText(String enTitleText) {
    this.enTitleText = enTitleText;
  }

  public void setOldPubId(Long oldPubId) {
    this.oldPubId = oldPubId;
  }

  @Transient
  public String getDes3Id() {

    if (this.id != null && des3Id == null) {
      des3Id = ServiceUtil.encodeToDes3(this.id.toString());
    }
    return des3Id;
  }

  @Transient
  public String getHtmlAbstract() {
    return htmlAbstract;
  }

  public void setHtmlAbstract(String htmlAbstract) {
    this.htmlAbstract = htmlAbstract;
  }

  public void setDes3Id(String des3Id) {
    this.des3Id = des3Id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setInternalNo(String internalNo) {
    this.internalNo = internalNo;
  }

  public void setExternalNo(String externalNo) {
    this.externalNo = externalNo;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public void setIsPrincipalIns(Integer isPrincipalIns) {
    this.isPrincipalIns = isPrincipalIns;
  }

  public void setState(String state) {
    this.state = state;
  }

  public void setFundingYear(Integer fundingYear) {
    this.fundingYear = fundingYear;
  }

  public void setAgencyName(String agencyName) {
    this.agencyName = agencyName;
  }

  public void setSchemeId(Long schemeId) {
    this.schemeId = schemeId;
  }

  public void setSchemeName(String schemeName) {
    this.schemeName = schemeName;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setStartYear(Integer startYear) {
    this.startYear = startYear;
  }

  public void setStartMonth(Integer startMonth) {
    this.startMonth = startMonth;
  }

  public void setStartDay(Integer startDay) {
    this.startDay = startDay;
  }

  public void setEndYear(Integer endYear) {
    this.endYear = endYear;
  }

  public void setEndMonth(Integer endMonth) {
    this.endMonth = endMonth;
  }

  public void setEndDay(Integer endDay) {
    this.endDay = endDay;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public void setDbId(Integer dbId) {
    this.dbId = dbId;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public void setUpdatePsnId(Long updatePsnId) {
    this.updatePsnId = updatePsnId;
  }

  public void setCreatePsnId(Long createPsnId) {
    this.createPsnId = createPsnId;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public void setVersionNo(Integer versionNo) {
    this.versionNo = versionNo;
  }

  public void setRecordFrom(Integer recordFrom) {
    this.recordFrom = recordFrom;
  }

  public void setIsValid(Integer isValid) {
    this.isValid = isValid;
  }

  public void setZhTitle(String zhTitle) {
    this.zhTitle = zhTitle;
  }

  public void setEnTitle(String enTitle) {
    this.enTitle = enTitle;
  }

  public void setZhTitleHash(Integer zhTitleHash) {
    this.zhTitleHash = zhTitleHash;
  }

  public void setEnTitleHash(Integer enTitleHash) {
    this.enTitleHash = enTitleHash;
  }

  public void setAuthorNames(String authorNames) {
    this.authorNames = authorNames;
  }

  public void setBriefDesc(String briefDesc) {
    this.briefDesc = briefDesc;
  }

  public void setFulltextField(String fulltextField) {
    this.fulltextField = fulltextField;
  }

  public void setFulltextUrl(String fulltextUrl) {
    this.fulltextUrl = fulltextUrl;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Transient
  public String getStateName() {
    return stateName;
  }

  public void setStateName(String stateName) {
    this.stateName = stateName;
  }

  @Transient
  public String getAmountShow() {
    return amountShow;
  }

  public void setAmountShow(String amountShow) {
    this.amountShow = amountShow;
  }

  @Transient
  public String getZhAbstract() {
    return zhAbstract;
  }

  @Transient
  public String getEnAbstract() {
    return enAbstract;
  }

  @Transient
  public String getZhKeywords() {
    return zhKeywords;
  }

  @Transient
  public String getEnKeywords() {
    return enKeywords;
  }

  @Transient
  public String getRemark() {
    return remark;
  }

  public void setZhAbstract(String zhAbstract) {
    this.zhAbstract = zhAbstract;
  }

  public void setEnAbstract(String enAbstract) {
    this.enAbstract = enAbstract;
  }

  public void setZhKeywords(String zhKeywords) {
    this.zhKeywords = zhKeywords;
  }

  public void setEnKeywords(String enKeywords) {
    this.enKeywords = enKeywords;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public void setBriefDescEn(String briefDescEn) {
    this.briefDescEn = briefDescEn;
  }

  @Column(name = "SCHEME_AGENCY_NAME_EN")
  public String getEnAgencyName() {
    return enAgencyName;
  }

  public void setEnAgencyName(String enAgencyName) {
    this.enAgencyName = enAgencyName;
  }

  @Column(name = "SCHEME_NAME_EN")
  public String getEnSchemeName() {
    return enSchemeName;
  }

  public void setEnSchemeName(String enSchemeName) {
    this.enSchemeName = enSchemeName;
  }

  @Transient
  public String getDisName() {
    return disName;
  }

  public void setDisName(String disName) {
    this.disName = disName;
  }

  @Transient
  public String getViewZhHtml() {
    return viewZhHtml;
  }

  public void setViewZhHtml(String viewZhHtml) {
    this.viewZhHtml = viewZhHtml;
  }

  @Transient
  public String getViewEnHtml() {
    return viewEnHtml;
  }

  public void setViewEnHtml(String viewEnHtml) {
    this.viewEnHtml = viewEnHtml;
  }

  @Transient
  public List<RolPrjMember> getShowPrjMembers() {
    return showPrjMembers;
  }

  public void setShowPrjMembers(List<RolPrjMember> showPrjMembers) {
    this.showPrjMembers = showPrjMembers;
  }

  @Transient
  public Integer getShowPrjMemSize() {
    return showPrjMemSize;
  }

  public void setShowPrjMemSize(Integer showPrjMemSize) {
    this.showPrjMemSize = showPrjMemSize;
  }

  @Transient
  public Integer getPrjReleaseStatus() {
    return prjReleaseStatus;
  }

  public void setPrjReleaseStatus(Integer prjReleaseStatus) {
    this.prjReleaseStatus = prjReleaseStatus;
  }

  @Transient
  public Integer getPubNum() {
    return pubNum;
  }

  public void setPubNum(Integer pubNum) {
    this.pubNum = pubNum;
  }

}
