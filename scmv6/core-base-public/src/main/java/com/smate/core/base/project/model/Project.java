package com.smate.core.base.project.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.smate.core.base.project.vo.ProjectInfo;
import com.smate.core.base.utils.string.IrisStringUtils;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 项目主表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PROJECT")
public class Project implements Serializable {

  private static final long serialVersionUID = 7810018652395859877L;
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
  private String fulltextFileId;
  // 全文附件后缀
  private String fulltextExt;
  // 全文附件节点ID
  private Integer fulltextNodeId;
  // 全文
  private String fulltextUrl;
  // 状态1: 已删除，0: 未删除
  private Integer status;
  // 加密Id.
  private String des3Id;
  // 查询表格显示用
  private String htmlAbstract;
  // 总评分
  private Integer prjStart;
  // 评分总人数
  private Integer prjStartPsns;
  // 评价人数
  private Integer prjReviews;

  private GroupPrjs groupPrjs;
  // 对应V2.6项目ID
  private Long oldPubId;
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
  // 项目信息所在的节点Id
  private Integer nodeId;
  // 是否赞
  private boolean isAward;
  private Long awardIds;

  private Long shareTimes;

  // 全文下载标志
  private boolean downUploadFulltext = true;// true标示可以下载，false标示不能下载

  // 科学关键词名字;
  private String disName;

  // 是否是自己的项目
  private boolean isMine;

  // 群组id
  private Long groupId;

  // 群组节点
  private Integer groupNodeId;

  // 群组加密id
  private String des3GroupId;

  // 群组名称
  private String groupName;

  // 是否显示项目-群组标识
  private boolean showPrjGroup = false;

  private int proIsShowComment = 0;// "评论"是否显示链接

  private Integer relPubCount;// 相关成果数
  // 是否是代表性项目
  private Integer isRepresentPrj;

  // 全文图片路径
  private String fullTextImgPath;

  public Project() {
    super();
  }

  public Project(Long id) {
    super();
    this.id = id;
  }

  public Project(String state, Long id) {
    super();
    this.state = state;
    this.id = id;
  }

  public Project(Long id, String zhTitle, String enTitle, String authorNames, String authorNamesEn, String briefDesc,
      String briefDescEn, String fulltextField) {
    super();
    this.id = id;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.authorNames = authorNames;
    this.authorNamesEn = authorNamesEn;
    this.briefDesc = briefDesc;
    this.briefDescEn = briefDescEn;
    this.fulltextFileId = fulltextField;
  }

  public Project(Long id, String externalNo, String zhTitle, String enTitle, String authorNames, String authorNamesEn,
      String briefDesc, String briefDescEn, String fulltextField) {
    super();
    this.id = id;
    this.externalNo = externalNo;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.authorNames = authorNames;
    this.authorNamesEn = authorNamesEn;
    this.briefDesc = briefDesc;
    this.briefDescEn = briefDescEn;
    this.fulltextFileId = fulltextField;
  }

  public Project(Long id, String externalNo) {
    super();
    this.id = id;
    this.externalNo = externalNo;
  }


  public Project(Long id, String externalNo, Integer status) {
    super();
    this.id = id;
    this.externalNo = externalNo;
    this.status = status;
  }

  public Project(Long id, Integer fundingYear, BigDecimal amount, String amountUnit) {
    super();
    this.id = id;
    this.fundingYear = fundingYear;
    this.amount = amount;
    this.amountUnit = amountUnit;
  }

  public Project(Long id, Long psnId, Integer status) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.status = status;
  }

  public Project(Long id, String zhTitle, String enTitle, String authorNames, String authorNamesEn, String briefDesc,
      String briefDescEn) {
    super();
    this.id = id;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.authorNames = authorNames;
    this.authorNamesEn = authorNamesEn;
    this.briefDesc = briefDesc;
    this.briefDescEn = briefDescEn;
  }

  @Id
  @Column(name = "PROJECT_ID")
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
  @Type(type = "big_decimal")
  public BigDecimal getAmount() {
    return amount;
  }

  @Column(name = "AMOUNT_UNIT")
  public String getAmountUnit() {
    return amountUnit;
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

  @Column(name = "OLD_PUB_ID")
  public Long getOldPubId() {
    return oldPubId;
  }

  public void setOldPubId(Long oldPubId) {
    this.oldPubId = oldPubId;
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

  @Column(name = "OWNER_PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "SOURCE_DB_ID")
  public Integer getDbId() {
    return dbId;
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate == null ? new Date() : updateDate;
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
    return createDate == null ? new Date() : createDate;
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

  @Column(name = "AUTHOR_NAMES_EN")
  public String getAuthorNamesEn() {
    return authorNamesEn;
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
  public String getFulltextFileId() {
    return fulltextFileId;
  }

  @Column(name = "FULLTEXT_URL")
  public String getFulltextUrl() {
    return fulltextUrl;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  @Column(name = "PRJ_START")
  public Integer getPrjStart() {
    return prjStart;
  }

  @Column(name = "PRJ_START_PSNS")
  public Integer getPrjStartPsns() {
    return prjStartPsns;
  }

  @Column(name = "PRJ_REVIEWS")
  public Integer getPrjReviews() {
    return prjReviews;
  }

  @Column(name = "FULLTEXT_FILEEXT")
  public String getFulltextExt() {
    return fulltextExt;
  }

  public void setFulltextExt(String fulltextExt) {
    this.fulltextExt = fulltextExt;
  }

  public void setPrjStart(Integer prjStart) {
    this.prjStart = prjStart;
  }

  public void setPrjStartPsns(Integer prjStartPsns) {
    this.prjStartPsns = prjStartPsns;
  }

  public void setPrjReviews(Integer prjReviews) {
    this.prjReviews = prjReviews;
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

  public void setAmountUnit(String amountUnit) {
    this.amountUnit = amountUnit;
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

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
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
    if (Objects.nonNull(recordFrom))
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
    this.authorNames = IrisStringUtils.filterSupplementaryChars(authorNames);
  }

  public void setBriefDesc(String briefDesc) {
    this.briefDesc = briefDesc;
  }

  public void setBriefDescEn(String briefDescEn) {
    this.briefDescEn = briefDescEn;
  }

  public void setFulltextFileId(String fileId) {
    this.fulltextFileId = fileId;
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
  public GroupPrjs getGroupPrjs() {
    return groupPrjs;
  }

  public void setGroupPrjs(GroupPrjs groupPrjs) {
    this.groupPrjs = groupPrjs;
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

  @Transient
  public Integer getNodeId() {
    return nodeId;
  }

  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  @Transient
  public boolean getIsAward() {
    return isAward;
  }

  public void setIsAward(boolean isAward) {
    this.isAward = isAward;
  }

  @Transient
  public boolean getIsMine() {
    return isMine;
  }

  public void setIsMine(boolean isMine) {
    this.isMine = isMine;
  }

  @Transient
  public Long getAwardIds() {
    return awardIds;
  }

  public void setAwardIds(Long awardIds) {
    this.awardIds = awardIds;
  }

  @Transient
  public String getDisName() {
    return disName;
  }

  @Transient
  public Long getShareTimes() {
    return shareTimes;
  }

  public void setShareTimes(Long shareTimes) {
    this.shareTimes = shareTimes;
  }

  public void setDisName(String disName) {
    this.disName = disName;
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

  public void setAuthorNamesEn(String authorNamesEn) {
    this.authorNamesEn = IrisStringUtils.filterSupplementaryChars(authorNamesEn);
  }

  @Transient
  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  @Transient
  public Integer getGroupNodeId() {
    return groupNodeId;
  }

  public void setGroupNodeId(Integer groupNodeId) {
    this.groupNodeId = groupNodeId;
  }

  @Transient
  public String getDes3GroupId() {
    return des3GroupId;
  }

  public void setDes3GroupId(String des3GroupId) {
    this.des3GroupId = des3GroupId;
  }

  @Transient
  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  @Transient
  public boolean isShowPrjGroup() {
    return showPrjGroup;
  }

  public void setShowPrjGroup(boolean showPrjGroup) {
    this.showPrjGroup = showPrjGroup;
  }

  @Transient
  public int getProIsShowComment() {
    return proIsShowComment;
  }

  public void setProIsShowComment(int proIsShowComment) {
    this.proIsShowComment = proIsShowComment;
  }

  @Transient
  public Integer getRelPubCount() {
    return relPubCount;
  }

  public void setRelPubCount(Integer relPubCount) {
    this.relPubCount = relPubCount;
  }

  @Transient
  public boolean getDownUploadFulltext() {
    return downUploadFulltext;
  }

  public void setDownUploadFulltext(boolean downUploadFulltext) {
    this.downUploadFulltext = downUploadFulltext;
  }

  @Transient
  public Integer getIsRepresentPrj() {
    return isRepresentPrj;
  }

  public void setIsRepresentPrj(Integer isRepresentPrj) {
    this.isRepresentPrj = isRepresentPrj;
  }

  @Transient
  public String getFullTextImgPath() {
    return fullTextImgPath;
  }

  public void setFullTextImgPath(String fullTextImgPath) {
    this.fullTextImgPath = fullTextImgPath;
  }

  // 用来操作项目信息
  private ProjectInfo prjInfo;

  @Transient
  public ProjectInfo getPrjInfo() {
    return prjInfo;
  }

  public void setPrjInfo(ProjectInfo prjInfo) {
    this.prjInfo = prjInfo;
  }
}
