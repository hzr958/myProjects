package com.smate.web.prj.form;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProjectForm implements Serializable {

  private static final long serialVersionUID = 4354521843865958597L;
  // 项目id
  private Long id;
  // 项目加密id
  private String des3Id;
  // "本机构中使用的项目编号
  private String internalNo;
  // "资助机构定义的项目编号
  private String externalNo;
  // 资金总数
  private BigDecimal amount;
  // 资金单位
  private String amountUnit;
  // 参与方式：主导单位1/参与单位0
  private Integer isPrincipalIns;
  // 项目关键词
  private String keywords;
  // 项目状态01进行中/02完成/03其他,04:申请项目
  private String state;
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
  private Integer shareCount;
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
  // 所有者(对应 person的psn_id)
  private Long psnId;
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
  private String title;
  // 中文标题
  private String zhTitle;
  // 外文标题
  private String enTitle;
  // 中文标题hash_code，查重时使用
  private Integer zhTitleHash;
  // 英文标题hash_code，查重时使用
  private Integer enTitleHash;
  // 作者
  private String authorNames;
  // 作者
  private String authorNamesEn;
  // 来源
  private String briefDesc;
  // 来源
  private String briefDescEn;
  // 全文，fulltext_fileid为空，则取fulltext_url
  private String fulltextField;
  // 全文附件后缀
  private String fulltextExt;
  // 全文附件节点ID
  private Integer fulltextNodeId;

  // 加密的群组id
  private String des3GroupId;
  // 是否显示项目-群组标识
  private boolean showPrjGroup = false;
  // 项目群组ID
  private Long groupId;
  // 对应V2.6项目ID
  private Long oldPubId;
  // 项目描述
  private String htmlAbstract;
  // 全文
  private String fulltextUrl;
  // 状态1: 已删除，0: 未删除
  private Integer status;
  // 总评分
  private Integer prjStart;
  // 评分总人数
  private Integer prjStartPsns;
  // 评价人数
  private Integer prjReviews;
  // 项目文件的描述
  private String fileDesc;
  // 项目上传文件名字file_name
  private String fileName;

  // 项目摘要
  private String abstracts;

  // 项目权限设置
  private String permission = "7";// // 用户查看权限(位运算任意组合使用)：1陌生人、2好友和4本人

  private String noneHtmlLableAuthorNames;

  private String downloadUrl;

  private String availableTotal; // 可用总金额
  private Long pubCount;
  private Integer reportType;

  private Map disciplineMap = new HashMap<>();

  public Map getDisciplineMap() {
    return disciplineMap;
  }

  public void setDisciplineMap(Map disciplineMap) {
    this.disciplineMap = disciplineMap;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDes3Id() {
    return des3Id;
  }

  public void setDes3Id(String des3Id) {
    this.des3Id = des3Id;
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

  public String getAmountUnit() {
    return amountUnit;
  }

  public void setAmountUnit(String amountUnit) {
    this.amountUnit = amountUnit;
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

  public String getEnAgencyName() {
    return enAgencyName;
  }

  public void setEnAgencyName(String enAgencyName) {
    this.enAgencyName = enAgencyName;
  }

  public Long getAgencyId() {
    return agencyId;
  }

  public void setAgencyId(Long agencyId) {
    this.agencyId = agencyId;
  }

  public Long getSchemeId() {
    return schemeId;
  }

  public void setSchemeId(Long schemeId) {
    this.schemeId = schemeId;
  }

  public String getSchemeName() {
    return schemeName;
  }

  public void setSchemeName(String schemeName) {
    this.schemeName = schemeName;
  }

  public String getEnSchemeName() {
    return enSchemeName;
  }

  public void setEnSchemeName(String enSchemeName) {
    this.enSchemeName = enSchemeName;
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

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public Long getUpdatePsnId() {
    return updatePsnId;
  }

  public void setUpdatePsnId(Long updatePsnId) {
    this.updatePsnId = updatePsnId;
  }

  public Long getCreatePsnId() {
    return createPsnId;
  }

  public void setCreatePsnId(Long createPsnId) {
    this.createPsnId = createPsnId;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Integer getVersionNo() {
    return versionNo;
  }

  public void setVersionNo(Integer versionNo) {
    this.versionNo = versionNo;
  }

  public Integer getRecordFrom() {
    return recordFrom;
  }

  public void setRecordFrom(Integer recordFrom) {
    this.recordFrom = recordFrom;
  }

  public Integer getIsValid() {
    return isValid;
  }

  public void setIsValid(Integer isValid) {
    this.isValid = isValid;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
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

  public Integer getZhTitleHash() {
    return zhTitleHash;
  }

  public void setZhTitleHash(Integer zhTitleHash) {
    this.zhTitleHash = zhTitleHash;
  }

  public Integer getEnTitleHash() {
    return enTitleHash;
  }

  public void setEnTitleHash(Integer enTitleHash) {
    this.enTitleHash = enTitleHash;
  }

  public String getAuthorNames() {
    return authorNames;
  }

  public void setAuthorNames(String authorNames) {
    this.authorNames = authorNames;
  }

  public String getAuthorNamesEn() {
    return authorNamesEn;
  }

  public void setAuthorNamesEn(String authorNamesEn) {
    this.authorNamesEn = authorNamesEn;
  }

  public String getBriefDesc() {
    return briefDesc;
  }

  public void setBriefDesc(String briefDesc) {
    this.briefDesc = briefDesc;
  }

  public String getBriefDescEn() {
    return briefDescEn;
  }

  public void setBriefDescEn(String briefDescEn) {
    this.briefDescEn = briefDescEn;
  }

  public String getFulltextField() {
    return fulltextField;
  }

  public void setFulltextField(String fulltextField) {
    this.fulltextField = fulltextField;
  }

  public String getFulltextExt() {
    return fulltextExt;
  }

  public void setFulltextExt(String fulltextExt) {
    this.fulltextExt = fulltextExt;
  }

  public Integer getFulltextNodeId() {
    return fulltextNodeId;
  }

  public void setFulltextNodeId(Integer fulltextNodeId) {
    this.fulltextNodeId = fulltextNodeId;
  }

  public String getFulltextUrl() {
    return fulltextUrl;
  }

  public void setFulltextUrl(String fulltextUrl) {
    this.fulltextUrl = fulltextUrl;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getPrjStart() {
    return prjStart;
  }

  public void setPrjStart(Integer prjStart) {
    this.prjStart = prjStart;
  }

  public Integer getPrjStartPsns() {
    return prjStartPsns;
  }

  public void setPrjStartPsns(Integer prjStartPsns) {
    this.prjStartPsns = prjStartPsns;
  }

  public Integer getPrjReviews() {
    return prjReviews;
  }

  public void setPrjReviews(Integer prjReviews) {
    this.prjReviews = prjReviews;
  }

  public String getDes3GroupId() {
    return des3GroupId;
  }

  public void setDes3GroupId(String des3GroupId) {
    this.des3GroupId = des3GroupId;
  }

  public boolean isShowPrjGroup() {
    return showPrjGroup;
  }

  public void setShowPrjGroup(boolean showPrjGroup) {
    this.showPrjGroup = showPrjGroup;
  }

  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public Long getOldPubId() {
    return oldPubId;
  }

  public void setOldPubId(Long oldPubId) {
    this.oldPubId = oldPubId;
  }

  public String getHtmlAbstract() {
    return htmlAbstract;
  }

  public void setHtmlAbstract(String htmlAbstract) {
    this.htmlAbstract = htmlAbstract;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  public String getFileDesc() {
    return fileDesc;
  }

  public void setFileDesc(String fileDesc) {
    this.fileDesc = fileDesc;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getPermission() {
    return permission;
  }

  public void setPermission(String permission) {
    this.permission = permission;
  }

  public String getAbstracts() {
    return abstracts;
  }

  public void setAbstracts(String abstracts) {
    this.abstracts = abstracts;
  }

  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  public Integer getShareCount() {
    return shareCount;
  }

  public void setShareCount(Integer shareCount) {
    this.shareCount = shareCount;
  }

  public String getNoneHtmlLableAuthorNames() {
    return noneHtmlLableAuthorNames;
  }

  public void setNoneHtmlLableAuthorNames(String noneHtmlLableAuthorNames) {
    this.noneHtmlLableAuthorNames = noneHtmlLableAuthorNames;
  }

  public String getDownloadUrl() {
    return downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }

  public String getAvailableTotal() {
    return availableTotal;
  }

  public void setAvailableTotal(String availableTotal) {
    this.availableTotal = availableTotal;
  }

  public Long getPubCount() {
    return pubCount;
  }

  public void setPubCount(Long pubCount) {
    this.pubCount = pubCount;
  }

  public Integer getReportType() {
    return reportType;
  }

  public void setReportType(Integer reportType) {
    this.reportType = reportType;
  }

}
