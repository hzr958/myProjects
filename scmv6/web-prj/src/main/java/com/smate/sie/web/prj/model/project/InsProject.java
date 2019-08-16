package com.smate.sie.web.prj.model.project;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * @author yxs
 * @descript 项目实体类
 */
@Entity
@Table(name = "PROJECT")
public class InsProject {

  @Id
  @Column(name = "PRJ_ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_INS_PROJECT", allocationSize = 1)
  private Long id;
  // 中文标题
  @Column(name = "ZH_TITLE")
  private String zhTitle;
  // 外文标题
  @Column(name = "EN_TITLE")
  private String enTitle;
  // "本机构中使用的项目编号
  @Column(name = "PRJ_INNER_NO")
  private String internalNo;
  // "资助机构定义的项目编号
  @Column(name = "PRJ_EXTER_NO")
  private String externalNo;
  // 中文标题hash_code，查重时使用
  @Column(name = "ZH_TITLE_HASH")
  private Integer zhTitleHash;
  // 英文标题hash_code，查重时使用
  @Column(name = "EN_TITLE_HASH")
  private Integer enTitleHash;
  // 项目组成员
  @Column(name = "AUTHOR_NAMES")
  private String authorNames;
  // 项目金额
  @Column(name = "AMT_TOTAL")
  private BigDecimal amount;
  // 立项年度
  @Column(name = "STAT_YEAR")
  private Integer statYear;
  // 立项月份
  @Column(name = "STAT_MONTH")
  private Integer statMonth;
  // 立项日
  @Column(name = "STAT_DAY")
  private Integer statDay;
  // 开始年
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
  // 项目状态
  @Column(name = "PRJ_STATUS")
  private String prjStatus;
  // 流程状态
  @Column(name = "STATUS")
  private Integer status;
  // 最后编辑时间
  @Column(name = "UPDATE_DATE")
  private Date updateDate;
  // 最后编辑人
  @Column(name = "UPDATE_PSN_ID")
  private Long updatePsnId;
  // 创建人
  @Column(name = "CREATE_PSN_ID")
  private Long createPsnId;
  // 创建日期
  @Column(name = "CREATE_DATE")
  private Date createDate;
  // 依托单位id
  @Column(name = "INS_ID")
  private Long insId;
  // 资助机构ID
  @Column(name = "GRANT_ORG_ID")
  private Long grantOrgId;
  // 资助机构ZH_NAME
  @Column(name = "GRANT_ORG_ZH_NAME")
  private String grantOrgZhName;
  // 资助机构EN_NAME
  @Column(name = "GRANT_ORG_EN_NAME")
  private String grantOrgEnName;
  // 资助类别ID
  @Column(name = "GRANT_CODE")
  private Long grantCode;
  // 资助类别ZH_NAME
  @Column(name = "GRANT_ZH_NAME")
  private String grantZhName;
  // 资助类别EN_NAME
  @Column(name = "GRANT_EN_NAME")
  private String grantEnName;
  // 负责人ID
  @Column(name = "PSN_ID")
  private Long psnId;
  // 负责人NAME
  @Transient
  private String psnName;
  // 负责人EMAIL
  @Column(name = "PSN_EMAIL")
  private String psnEmail;
  // 二级单位ID
  @Column(name = "UNIT_ID")
  private Long unitId;
  // 二级单位NAME
  @Column(name = "UNIT_NAME")
  private String unitName;
  // 项目来源ID
  @Column(name = "PRJ_FROM_ID")
  private Long prjFromId;
  // 项目来源NAME
  @Column(name = "PRJ_FROM_NAME")
  private String prjFromName;
  // 数据来源：0: 手工输入，1:数据库导入，2:文件导入
  @Column(name = "DATA_FROM")
  private Integer dataFrom;
  // 0/1数据是否完整;0：数据不完整，1：数据完整
  @Column(name = "IS_VALID")
  private Integer isValid;
  // 领域ID
  // @Column(name = "CATEGRY_ID")
  // private Long categryId;
  // 来源（中文）
  @Column(name = "BRIEF_DESC")
  private String briefDesc;
  // 来源（英文）
  @Column(name = "BRIEF_DESC_EN")
  private String briefDescEn;
  // 是否公开：0不公开1公开，默认1
  @Column(name = "IS_PUBLIC")
  private int isPublic = 1;
  @Transient
  private String startEndDate;
  @Transient
  private Integer pubSum;// 成果数
  @Transient
  private String kewords;
  @Transient
  private String kewordsText;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public Integer getStatYear() {
    return statYear;
  }

  public void setStatYear(Integer statYear) {
    this.statYear = statYear;
  }

  public Integer getStatMonth() {
    return statMonth;
  }

  public void setStatMonth(Integer statMonth) {
    this.statMonth = statMonth;
  }

  public Integer getStatDay() {
    return statDay;
  }

  public void setStatDay(Integer statDay) {
    this.statDay = statDay;
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

  public String getPrjStatus() {
    return prjStatus;
  }

  public void setPrjStatus(String prjStatus) {
    this.prjStatus = prjStatus;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
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

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public Long getUnitId() {
    return unitId;
  }

  public void setUnitId(Long unitId) {
    this.unitId = unitId;
  }

  public String getUnitName() {
    return unitName;
  }

  public void setUnitName(String unitName) {
    this.unitName = unitName;
  }

  public Long getPrjFromId() {
    return prjFromId;
  }

  public void setPrjFromId(Long prjFromId) {
    this.prjFromId = prjFromId;
  }

  public String getPrjFromName() {
    return prjFromName;
  }

  public void setPrjFromName(String prjFromName) {
    this.prjFromName = prjFromName;
  }

  public Long getGrantOrgId() {
    return grantOrgId;
  }

  public void setGrantOrgId(Long grantOrgId) {
    this.grantOrgId = grantOrgId;
  }

  public String getGrantOrgZhName() {
    return grantOrgZhName;
  }

  public void setGrantOrgZhName(String grantOrgZhName) {
    this.grantOrgZhName = grantOrgZhName;
  }

  public String getGrantOrgEnName() {
    return grantOrgEnName;
  }

  public void setGrantOrgEnName(String grantOrgEnName) {
    this.grantOrgEnName = grantOrgEnName;
  }

  public Long getGrantCode() {
    return grantCode;
  }

  public void setGrantCode(Long grantCode) {
    this.grantCode = grantCode;
  }

  public String getGrantZhName() {
    return grantZhName;
  }

  public void setGrantZhName(String grantZhName) {
    this.grantZhName = grantZhName;
  }

  public String getGrantEnName() {
    return grantEnName;
  }

  public void setGrantEnName(String grantEnName) {
    this.grantEnName = grantEnName;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getPsnName() {
    return psnName;
  }

  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  public String getPsnEmail() {
    return psnEmail;
  }

  public void setPsnEmail(String psnEmail) {
    this.psnEmail = psnEmail;
  }

  public Integer getDataFrom() {
    return dataFrom;
  }

  public void setDataFrom(Integer dataFrom) {
    this.dataFrom = dataFrom;
  }

  public Integer getIsValid() {
    return isValid;
  }

  public void setIsValid(Integer isValid) {
    this.isValid = isValid;
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

  public String getStartEndDate() {
    return startEndDate;
  }

  public void setStartEndDate(String startEndDate) {
    this.startEndDate = startEndDate;
  }

  public Integer getPubSum() {
    return pubSum;
  }

  public void setPubSum(Integer pubSum) {
    this.pubSum = pubSum;
  }

  public String getKewords() {
    return kewords;
  }

  public void setKewords(String kewords) {
    this.kewords = kewords;
  }

  public String getKewordsText() {
    return kewordsText;
  }

  public void setKewordsText(String kewordsText) {
    this.kewordsText = kewordsText;
  }

  @Column(name = "IS_PUBLIC")
  public int getIsPublic() {
    return isPublic;
  }

  public void setIsPublic(int isPublic) {
    this.isPublic = isPublic;
  }

}
