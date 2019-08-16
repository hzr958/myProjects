package com.smate.core.base.project.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 群组关系表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "GROUP_PRJS")
public class GroupPrjs implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3410340547855908648L;
  // 群组项目ID
  private Long groupPrjsId;
  // 加密群组项目ID
  private String des3GroupPrjsId;
  // 群组ID
  private Long groupId;
  // 加密群组ID
  private String des3GroupId;
  // 项目ID
  private Long prjId;
  // "本机构中使用的项目编号
  private String internalNo;
  // "资助机构定义的项目编号
  private String externalNo;
  // 资金总数
  private BigDecimal amount;
  // 资金单位
  private String amountUnit;
  // 项目状态01进行中/02完成/03其他,04:申请项目
  private String state;
  // 项目年度
  private Integer fundingYear;
  // 资助机构名称
  private String agencyName;
  // 资助机构ID
  private Long agencyId;
  // 资助类别id
  private Long schemeId;
  // 资助类别名称
  private String schemeName;
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
  // 中文标题
  private String zhTitle;
  // 外文标题
  private String enTitle;
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
  // 查询表格显示用,该字段标记为
  private String htmlAbstract;
  // 项目加密ID
  private String des3Id;
  // 项目节点ID
  private Integer nodeId;
  // 群组文件夹ID，多个用逗号隔开
  private String groupFolderIds;
  // 全文附件节点ID
  private Integer fulltextNodeId;
  private int groupProIsShowComment = 0;// "评论"是否要显示链接

  public GroupPrjs() {}

  public GroupPrjs(Long prjId, Integer nodeId) {
    super();
    this.prjId = prjId;
    this.nodeId = nodeId;
  }

  public GroupPrjs(Long groupPrjsId, String des3GroupPrjsId, Long groupId, String des3GroupId, Long prjId,
      String internalNo, String externalNo, BigDecimal amount, String amountUnit, String state, Integer fundingYear,
      String agencyName, Long agencyId, Long schemeId, String schemeName, String type, String typeName,
      Integer startYear, Integer startMonth, Integer startDay, Integer endYear, Integer endMonth, Integer endDay,
      Long insId, String insName, Long psnId, String zhTitle, String enTitle, String authorNames, String briefDesc,
      String briefDescEn, String fulltextField, String fulltextExt, String fulltextUrl, String htmlAbstract,
      String des3Id, Integer nodeId, String groupFolderIds, Integer fulltextNodeId) {
    super();
    this.groupPrjsId = groupPrjsId;
    this.des3GroupPrjsId = des3GroupPrjsId;
    this.groupId = groupId;
    this.des3GroupId = des3GroupId;
    this.prjId = prjId;
    this.internalNo = internalNo;
    this.externalNo = externalNo;
    this.amount = amount;
    this.amountUnit = amountUnit;
    this.state = state;
    this.fundingYear = fundingYear;
    this.agencyName = agencyName;
    this.agencyId = agencyId;
    this.schemeId = schemeId;
    this.schemeName = schemeName;
    this.type = type;
    this.typeName = typeName;
    this.startYear = startYear;
    this.startMonth = startMonth;
    this.startDay = startDay;
    this.endYear = endYear;
    this.endMonth = endMonth;
    this.endDay = endDay;
    this.insId = insId;
    this.insName = insName;
    this.psnId = psnId;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.authorNames = authorNames;
    this.briefDesc = briefDesc;
    this.briefDescEn = briefDescEn;
    this.fulltextField = fulltextField;
    this.fulltextExt = fulltextExt;
    this.fulltextUrl = fulltextUrl;
    this.htmlAbstract = htmlAbstract;
    this.des3Id = des3Id;
    this.nodeId = nodeId;
    this.groupFolderIds = groupFolderIds;
    this.fulltextNodeId = fulltextNodeId;
  }

  @Id
  @Column(name = "GROUP_PRJS_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_GROUP_PRJS", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getGroupPrjsId() {
    return groupPrjsId;
  }

  @Column(name = "PRJ_INTERNAL_NO")
  public String getInternalNo() {
    return internalNo;
  }

  @Column(name = "FULLTEXT_NODEID")
  public Integer getFulltextNodeId() {
    return fulltextNodeId;
  }

  public void setFulltextNodeId(Integer fulltextNodeId) {
    this.fulltextNodeId = fulltextNodeId;
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

  @Column(name = "OWNER_PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "ZH_TITLE")
  public String getZhTitle() {
    return zhTitle;
  }

  @Column(name = "EN_TITLE")
  public String getEnTitle() {
    return enTitle;
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

  @Column(name = "FULLTEXT_URL")
  public String getFulltextUrl() {
    return fulltextUrl;
  }

  @Transient
  public String getHtmlAbstract() {
    return htmlAbstract;
  }

  public void setGroupPrjsId(Long groupPrjsId) {
    this.groupPrjsId = groupPrjsId;
  }

  @Column(name = "GROUP_ID")
  public Long getGroupId() {
    return groupId;
  }

  @Column(name = "PRJ_ID")
  public Long getPrjId() {
    return prjId;
  }

  @Column(name = "GROUP_FOLDER_IDS")
  public String getGroupFolderIds() {
    return groupFolderIds;
  }

  public void setGroupFolderIds(String groupFolderIds) {
    this.groupFolderIds = groupFolderIds;
  }

  @Column(name = "FULLTEXT_FILEEXT")
  public String getFulltextExt() {
    return fulltextExt;
  }

  public void setFulltextExt(String fulltextExt) {
    this.fulltextExt = fulltextExt;
  }

  @Transient
  public String getDes3Id() {
    if (this.prjId != null && des3Id == null) {
      des3Id = ServiceUtil.encodeToDes3(this.prjId.toString());
    }
    return des3Id;
  }

  public void setDes3Id(String des3Id) {
    if (this.prjId == null && StringUtils.isNotBlank(des3Id)) {
      this.prjId = Long.valueOf(ServiceUtil.decodeFromDes3(des3Id));
    }
    this.des3Id = des3Id;
  }

  @Transient
  public String getDes3GroupId() {
    if (this.groupId != null && des3GroupId == null) {
      des3GroupId = ServiceUtil.encodeToDes3(this.groupId.toString());
    }
    return des3GroupId;
  }

  public void setDes3GroupId(String des3GroupId) {
    if (this.groupId == null && StringUtils.isNotBlank(des3GroupId)) {
      this.groupId = Long.valueOf(ServiceUtil.decodeFromDes3(des3GroupId));
    }
    this.des3GroupId = des3GroupId;
  }

  @Transient
  public String getDes3GroupPrjsId() {
    if (this.groupPrjsId != null && des3GroupPrjsId == null) {
      des3GroupPrjsId = ServiceUtil.encodeToDes3(this.groupPrjsId.toString());
    }
    return des3GroupPrjsId;
  }

  @Column(name = "NODE_ID")
  public Integer getNodeId() {
    return nodeId;
  }

  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  public void setDes3GroupPrjsId(String des3GroupPrjsId) {
    if (this.groupPrjsId == null && StringUtils.isNotBlank(des3GroupPrjsId)) {
      this.groupPrjsId = Long.valueOf(ServiceUtil.decodeFromDes3(des3GroupPrjsId));
    }
    this.des3GroupPrjsId = des3GroupPrjsId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
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

  public void setZhTitle(String zhTitle) {
    this.zhTitle = zhTitle;
  }

  public void setEnTitle(String enTitle) {
    this.enTitle = enTitle;
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

  public void setHtmlAbstract(String htmlAbstract) {
    this.htmlAbstract = htmlAbstract;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  public void setBriefDescEn(String briefDescEn) {
    this.briefDescEn = briefDescEn;
  }

  @Transient
  public int getGroupProIsShowComment() {
    return groupProIsShowComment;
  }

  public void setGroupProIsShowComment(int groupProIsShowComment) {
    this.groupProIsShowComment = groupProIsShowComment;
  }

}
