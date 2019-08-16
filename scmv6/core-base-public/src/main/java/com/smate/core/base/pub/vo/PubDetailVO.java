package com.smate.core.base.pub.vo;
/**
 * 成果详情显示的 视图对象
 * 
 * @author aijiangbin
 * @date 2018年8月3日
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smate.core.base.pub.enums.PubSnsRecordFromEnum;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.dto.IndustryDTO;
import com.smate.web.v8pub.dto.PubFulltextDTO;
import com.smate.web.v8pub.dto.PubMemberDTO;
import com.smate.web.v8pub.dto.PubSituationDTO;
import com.smate.web.v8pub.dto.PubTypeInfoDTO;
import com.smate.web.v8pub.dto.ScienceAreaDTO;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PubDetailVO<T extends PubTypeInfoDTO> implements Serializable {
  private static final long serialVersionUID = 1L;
  private Long pubId;
  private Long pubConfirmId; // 认领成果主键id
  private String des3PubId = "";// 成果id
  private Integer pubGenre;// 成果的类别 1：个人成果 2：群组成果 3：基准库导入成果
  private Integer permission;// 7公开，4私有
  private Integer pubType;// 成果类型
  private Integer oldPubType;// 原来的成果类型,切换成果用
  private Integer pubSum;// 个人成果列表总数，用于控制滑动论文详情尾页的判断
  private String typeName = ""; //
  private String des3PsnId = "";// 成果的拥有者
  private String modifyDate = ""; // 最近修改的时间
  private String title = "";// 标题
  private String summary = ""; // 摘要，概要
  private String briefDesc = ""; // 简短描述
  private String keywords = "";// 关键词数组
  private String publishDate = ""; // 出版日期
  private Long countryId; //// 来源国家地区id
  private String fundInfo = ""; // 基金信息
  private Integer citations; // 引用次数
  private String citationUrl = "";
  private String sourceUrl = "";
  private String insSourceUrl = "";
  private String doi = "";
  private String srcFulltextUrl = ""; // 全文链接
  private String insFulltextUrl = "";
  private String organization = "";// 单位组织
  private List<String> keyWordsList;// 关键词list
  private PubSnsRecordFromEnum recordFrom; // 记录来源 0, "手工录入" 1, "数据库导入"
  // 2,"文件导入" 3,"基准库导入"
  private Integer updateMark;// 是否是在线导入或手工导入1=在线导入且未修改；2=在线导入且已修改；3=手工导入
  private String authorNames = "";
  private String authorNamesBak = "";// 名字备份
  private Integer readCount;
  private Integer refCount;
  private Integer isAward = 0; // 是否赞过 1 是 0否
  private Integer isCollection = 0;// 是否收藏过 1:收藏过，0:没收藏过
  private Integer awardCount; // 点赞次数
  private boolean isOwner; // 是否是成果拥有者
  private boolean isFriend; // 当前人与该成果拥有者是否是好友 true:是 false:否
  private Integer shareCount; // 分享次数
  private Integer commentCount; // 评论统计数
  private boolean isLogin; // 用户是否已登录
  private String psnAvatars = "";// 当前登录用户头像
  private String pubOwnerAvatars = "";// 成果拥有者头像
  private String pubOwnerName = "";// 成果拥有者人员名称
  private String pubOwnerTitle = "";// 成果拥有者人员职称信息
  private Integer pubOwnerhIndex = 0; // 成果拥有者H-index指数
  private Long psnId;// 当前登录用户
  private Long pubOwnerPsnId;// 成果拥有者信息
  private String psnIndexUrl = ""; // 人员站外短地址 6位
  private String pubIndexUrl = ""; // 成果短地址
  private String seoIndexUrl = "";// 短地址用与seo（如果有基准库的用基准库的短地址，没有就用个人库的）
  private Integer applicationStatus;// 申请状态
  private String applicationDate;// 申请时间
  private Date gmtModified; // 成果更新时间
  private Date gmtCreate; // 成果添加时间
  private Integer srcDbId; // 目前为了不影响功能 暂时添加一个字段 下个版本删除 sourceDbId20181127
  private String sourceId;
  private Integer sourceDbId;
  private PubFulltextDTO fullText;// 全文信息
  private String remarks = "";
  private String countryName = "";
  private String cityName = "";
  private String doiUrl = "";
  private String impactFactors = ""; // 影响因子
  private Long snsSimilarCount;// 其他类似全文条数
  private Long pdwhSimilarCount;// pdwh其他类似全文条数
  private List<Map<String, Object>> authorPsnInfoList;// 基准库作者信息
  private String firstAvatars = "";
  private Integer status = 0; // 1=删除
  private String authorName = "";
  private Integer matchStatus;// 1=匹配，0=未匹配
  private String fullTextDownloadUrl = ""; // 全文下载链接
  private String fulltextImageUrl = ""; // 全文图片链接
  private String pdwhFullTextDownloadUrl = ""; // 基准库全文下载链接
  private Integer isProjectPub; // 项目成果1 项目文献0
  private String des3GrpId = "";// 群组id
  private boolean isShowButton;// 是否直接显示 这是我的成果 按钮

  private String backURL = "";// 编辑页面返回的地址
  private List<Accessory> accessorys = new ArrayList<>(); // 附件

  private List<PubMemberDTO> members = new ArrayList<>(); // 成果成员

  private List<ScienceAreaDTO> scienceAreas = new ArrayList<>(); // 科技领域

  private List<IndustryDTO> industrys = new ArrayList<>(); // 行业

  private List<PubSituationDTO> situations = new ArrayList<>(); // 收录情况

  private T pubTypeInfo;

  private String listInfo;// 收录情况
  private String simpleDownLoadUrl;// 简单版详情页面下载所用地址

  private Integer relevance;
  private Integer labeled;

  private String insNames;// 单位信息

  @JsonProperty
  private Integer HCP = 0; // 高被引用文章 0未否，1为是
  @JsonProperty
  private Integer HP = 0; // 热门文章 0未否，1为是
  @JsonProperty
  private String OA = new String(); // Open Access

  private String countriesRegions = "";// 额外国家字段
  private String type;// 类型

  public String getCountriesRegions() {
    return countriesRegions;
  }

  public void setCountriesRegions(String countriesRegions) {
    this.countriesRegions = countriesRegions;
  }

  @JsonIgnore
  public Integer getHCP() {
    return HCP;
  }

  public void setHCP(Integer hCP) {
    HCP = hCP;
  }


  protected Integer publishYear; // 发表年份
  protected Integer publishMonth; // 发表月份
  protected Integer publishDay; // 发表日份
  private Long jid; // 期刊id

  private String originalMail = ""; // 成果的原始邮件

  public String getOriginalMail() {
    return originalMail;
  }

  public void setOriginalMail(String originalMail) {
    this.originalMail = originalMail;
  }

  public Long getJid() {
    return jid;
  }

  public void setJid(Long jid) {
    this.jid = jid;
  }

  public Integer getPublishYear() {
    return publishYear;
  }

  public void setPublishYear(Integer publishYear) {
    this.publishYear = publishYear;
  }

  public Integer getPublishMonth() {
    return publishMonth;
  }

  public void setPublishMonth(Integer publishMonth) {
    this.publishMonth = publishMonth;
  }

  public Integer getPublishDay() {
    return publishDay;
  }

  public void setPublishDay(Integer publishDay) {
    this.publishDay = publishDay;
  }

  @JsonIgnore
  public Integer getHP() {
    return HP;
  }

  public void setHP(Integer hP) {
    HP = hP;
  }

  @JsonIgnore
  public String getOA() {
    return OA;
  }

  public void setOA(String oA) {
    OA = oA;
  }

  public Integer getRelevance() {
    return relevance;
  }

  public void setRelevance(Integer relevance) {
    this.relevance = relevance;
  }

  public Integer getLabeled() {
    return labeled;
  }

  public void setLabeled(Integer labeled) {
    this.labeled = labeled;
  }

  public Long getPubId() {
    if (pubId == null && StringUtils.isNotBlank(des3PubId)) {
      pubId = Long.parseLong(Des3Utils.decodeFromDes3(des3PubId));
    }
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getDes3PubId() {
    return des3PubId;
  }

  public void setDes3PubId(String des3PubId) {
    this.des3PubId = des3PubId;
  }

  public Integer getPubType() {
    return pubType;
  }

  public void setPubType(Integer pubType) {
    this.pubType = pubType;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public String getBriefDesc() {
    return briefDesc;
  }

  public void setBriefDesc(String briefDesc) {
    this.briefDesc = briefDesc;
  }

  public String getPublishDate() {
    return publishDate;
  }

  public void setPublishDate(String publishDate) {
    this.publishDate = publishDate;
  }

  public Long getCountryId() {
    return countryId;
  }

  public void setCountryId(Long countryId) {
    this.countryId = countryId;
  }

  public String getFundInfo() {
    return fundInfo;
  }

  public void setFundInfo(String fundInfo) {
    this.fundInfo = fundInfo;
  }

  public String getCitationUrl() {
    return citationUrl;
  }

  public void setCitationUrl(String citationUrl) {
    this.citationUrl = citationUrl;
  }

  public String getSourceUrl() {
    return sourceUrl;
  }

  public void setSourceUrl(String sourceUrl) {
    this.sourceUrl = sourceUrl;
  }

  public String getInsSourceUrl() {
    return insSourceUrl;
  }

  public void setInsSourceUrl(String insSourceUrl) {
    this.insSourceUrl = insSourceUrl;
  }

  public Integer getCitations() {
    return citations;
  }

  public void setCitations(Integer citations) {
    this.citations = citations;
  }

  public String getDoi() {
    return doi;
  }

  public void setDoi(String doi) {
    this.doi = doi;
  }

  public String getSrcFulltextUrl() {
    return srcFulltextUrl;
  }

  public void setSrcFulltextUrl(String srcFulltextUrl) {
    this.srcFulltextUrl = srcFulltextUrl;
  }

  public String getInsFulltextUrl() {
    return insFulltextUrl;
  }

  public void setInsFulltextUrl(String insFulltextUrl) {
    this.insFulltextUrl = insFulltextUrl;
  }

  public String getOrganization() {
    return organization;
  }

  public void setOrganization(String organization) {
    this.organization = organization;
  }

  public List<Accessory> getAccessorys() {
    return accessorys;
  }

  public void setAccessorys(List<Accessory> accessorys) {
    this.accessorys = accessorys;
  }

  public void setPubTypeInfo(T pubTypeInfo) {
    this.pubTypeInfo = pubTypeInfo;
  }

  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  public PubFulltextDTO getFullText() {
    return fullText;
  }

  public void setFullText(PubFulltextDTO fullText) {
    this.fullText = fullText;
  }

  public T getPubTypeInfo() {
    return pubTypeInfo;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public String getSourceId() {
    return sourceId;
  }

  public void setSourceId(String sourceId) {
    this.sourceId = sourceId;
  }

  public String getModifyDate() {
    return modifyDate;
  }

  public void setModifyDate(String modifyDate) {
    this.modifyDate = modifyDate;
  }

  public List<String> getKeyWordsList() {
    return keyWordsList;
  }

  public void setKeyWordsList(List<String> keyWordsList) {
    this.keyWordsList = keyWordsList;
  }

  public PubSnsRecordFromEnum getRecordFrom() {
    return recordFrom;
  }

  public void setRecordFrom(PubSnsRecordFromEnum recordFrom) {
    this.recordFrom = recordFrom;
  }

  public String getAuthorNames() {
    return authorNames;
  }

  public void setAuthorNames(String authorNames) {
    if (StringUtils.isBlank(authorNamesBak)) {
      authorNamesBak = authorNames;
    }
    this.authorNames = authorNames;
  }

  public Integer getReadCount() {
    return readCount;
  }

  public void setReadCount(Integer readCount) {
    this.readCount = readCount;
  }

  public Integer getRefCount() {
    return refCount;
  }

  public void setRefCount(Integer refCount) {
    this.refCount = refCount;
  }

  public Integer getIsAward() {
    return isAward;
  }

  public void setIsAward(Integer isAward) {
    this.isAward = isAward;
  }

  public Integer getAwardCount() {
    return awardCount;
  }

  public void setAwardCount(Integer awardCount) {
    this.awardCount = awardCount;
  }

  public boolean getIsOwner() {
    return isOwner;
  }

  public void setOwner(boolean isOwner) {
    this.isOwner = isOwner;
  }

  public Integer getShareCount() {
    return shareCount;
  }

  public void setShareCount(Integer shareCount) {
    this.shareCount = shareCount;
  }

  public Integer getCommentCount() {
    return commentCount;
  }

  public void setCommentCount(Integer commentCount) {
    this.commentCount = commentCount;
  }

  public boolean getIsLogin() {
    return isLogin;
  }

  public void setIsLogin(boolean isLogin) {
    this.isLogin = isLogin;
  }

  public String getPsnAvatars() {
    return psnAvatars;
  }

  public void setPsnAvatars(String psnAvatars) {
    this.psnAvatars = psnAvatars;
  }

  public String getPubOwnerAvatars() {
    return pubOwnerAvatars;
  }

  public void setPubOwnerAvatars(String pubOwnerAvatars) {
    this.pubOwnerAvatars = pubOwnerAvatars;
  }

  public String getPubOwnerName() {
    return pubOwnerName;
  }

  public void setPubOwnerName(String pubOwnerName) {
    this.pubOwnerName = pubOwnerName;
  }

  public String getPubOwnerTitle() {
    return pubOwnerTitle;
  }

  public void setPubOwnerTitle(String pubOwnerTitle) {
    this.pubOwnerTitle = pubOwnerTitle;
  }

  public Integer getPubOwnerhIndex() {
    return pubOwnerhIndex;
  }

  public void setPubOwnerhIndex(Integer pubOwnerhIndex) {
    this.pubOwnerhIndex = pubOwnerhIndex;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getPubOwnerPsnId() {
    return pubOwnerPsnId;
  }

  public void setPubOwnerPsnId(Long pubOwnerPsnId) {
    this.pubOwnerPsnId = pubOwnerPsnId;
  }

  public boolean getIsFriend() {
    return isFriend;
  }

  public void setFriend(boolean isFriend) {
    this.isFriend = isFriend;
  }

  public String getPsnIndexUrl() {
    return psnIndexUrl;
  }

  public void setPsnIndexUrl(String psnIndexUrl) {
    this.psnIndexUrl = psnIndexUrl;
  }

  public Integer getIsCollection() {
    return isCollection;
  }

  public void setIsCollection(Integer isCollection) {
    this.isCollection = isCollection;
  }

  public Integer getSourceDbId() {
    return sourceDbId;
  }

  public void setSourceDbId(Integer sourceDbId) {
    this.sourceDbId = sourceDbId;
  }

  public Integer getSrcDbId() {
    return srcDbId;
  }

  public void setSrcDbId(Integer srcDbId) {
    this.srcDbId = srcDbId;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public String getCountryName() {
    return countryName;
  }

  public void setCountryName(String countryName) {
    this.countryName = countryName;
  }

  public String getCityName() {
    return cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  public String getDoiUrl() {
    return doiUrl;
  }

  public void setDoiUrl(String doiUrl) {
    this.doiUrl = doiUrl;
  }

  public Integer getPermission() {
    return permission;
  }

  public void setPermission(Integer permission) {
    this.permission = permission;
  }

  public Long getSnsSimilarCount() {
    return snsSimilarCount;
  }

  public void setSnsSimilarCount(Long snsSimilarCount) {
    this.snsSimilarCount = snsSimilarCount;
  }

  public Long getPdwhSimilarCount() {
    return pdwhSimilarCount;
  }

  public void setPdwhSimilarCount(Long pdwhSimilarCount) {
    this.pdwhSimilarCount = pdwhSimilarCount;
  }

  public List<Map<String, Object>> getAuthorPsnInfoList() {
    return authorPsnInfoList;
  }

  public void setAuthorPsnInfoList(List<Map<String, Object>> authorPsnInfoList) {
    this.authorPsnInfoList = authorPsnInfoList;
  }

  public Integer getMatchStatus() {
    return matchStatus;
  }

  public void setMatchStatus(Integer matchStatus) {
    this.matchStatus = matchStatus;
  }

  public String getFirstAvatars() {
    return firstAvatars;
  }

  public void setFirstAvatars(String firstAvatars) {
    this.firstAvatars = firstAvatars;
  }

  /**
   * 过时 用getAuthorNames() 代替
   * 
   * @return
   */
  @Deprecated
  public String getAuthorName() {
    if (StringUtils.isBlank(authorName)) {
      authorName = authorNames;
    }
    return authorName;
  }

  public void setAuthorName(String authorName) {
    this.authorName = authorName;
  }

  public String getPubIndexUrl() {
    return pubIndexUrl;
  }

  public void setPubIndexUrl(String pubIndexUrl) {
    this.pubIndexUrl = pubIndexUrl;
  }

  public Date getGmtModified() {
    return gmtModified;
  }

  public void setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public void setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
  }

  public String getFullTextDownloadUrl() {
    return fullTextDownloadUrl;
  }

  public void setFullTextDownloadUrl(String fullTextDownloadUrl) {
    this.fullTextDownloadUrl = fullTextDownloadUrl;
  }

  public String getPdwhFullTextDownloadUrl() {
    return pdwhFullTextDownloadUrl;
  }

  public void setPdwhFullTextDownloadUrl(String pdwhFullTextDownloadUrl) {
    this.pdwhFullTextDownloadUrl = pdwhFullTextDownloadUrl;
  }

  public String getFulltextImageUrl() {
    return fulltextImageUrl;
  }

  public void setFulltextImageUrl(String fulltextImageUrl) {
    this.fulltextImageUrl = fulltextImageUrl;
  }

  public String getBackURL() {
    return backURL;
  }

  public void setBackURL(String backURL) {
    this.backURL = backURL;
  }

  public Integer getPubGenre() {
    return pubGenre;
  }

  public void setPubGenre(Integer pubGenre) {
    this.pubGenre = pubGenre;
  }

  public Integer getIsProjectPub() {
    return isProjectPub;
  }

  public void setIsProjectPub(Integer isProjectPub) {
    this.isProjectPub = isProjectPub;
  }

  public String getDes3GrpId() {
    return des3GrpId;
  }

  public void setDes3GrpId(String des3GrpId) {
    this.des3GrpId = des3GrpId;
  }

  public List<PubMemberDTO> getMembers() {
    return members;
  }

  public void setMembers(List<PubMemberDTO> members) {
    this.members = members;
  }

  public List<ScienceAreaDTO> getScienceAreas() {
    return scienceAreas;
  }

  public void setScienceAreas(List<ScienceAreaDTO> scienceAreas) {
    this.scienceAreas = scienceAreas;
  }

  public List<PubSituationDTO> getSituations() {
    return situations;
  }

  public void setSituations(List<PubSituationDTO> situations) {
    this.situations = situations;
  }

  public Integer getOldPubType() {
    return oldPubType;
  }

  public void setOldPubType(Integer oldPubType) {
    this.oldPubType = oldPubType;
  }

  public void setImpactFactors(String impactFactors) {
    this.impactFactors = impactFactors;
  }

  public String getImpactFactors() {
    return impactFactors;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getPubSum() {
    return pubSum;
  }

  public void setPubSum(Integer pubSum) {
    this.pubSum = pubSum;
  }

  public Integer getUpdateMark() {
    return updateMark;
  }

  public void setUpdateMark(Integer updateMark) {
    this.updateMark = updateMark;
  }

  public String getListInfo() {
    return listInfo;
  }

  public void setListInfo(String listInfo) {
    this.listInfo = listInfo;
  }

  public String getSimpleDownLoadUrl() {
    return simpleDownLoadUrl;
  }

  public void setSimpleDownLoadUrl(String simpleDownLoadUrl) {
    this.simpleDownLoadUrl = simpleDownLoadUrl;
  }

  public String getSeoIndexUrl() {
    return seoIndexUrl;
  }

  public void setSeoIndexUrl(String seoIndexUrl) {
    this.seoIndexUrl = seoIndexUrl;
  }

  public Long getPubConfirmId() {
    return pubConfirmId;
  }

  public void setPubConfirmId(Long pubConfirmId) {
    this.pubConfirmId = pubConfirmId;
  }

  public String getApplicationDate() {
    return applicationDate;
  }

  public void setApplicationDate(String applicationDate) {
    this.applicationDate = applicationDate;
  }

  public Integer getApplicationStatus() {
    return applicationStatus;
  }

  public void setApplicationStatus(Integer applicationStatus) {
    this.applicationStatus = applicationStatus;
  }

  public String getInsNames() {
    return insNames;
  }

  public void setInsNames(String insNames) {
    this.insNames = insNames;
  }

  public String getAuthorNamesBak() {
    return authorNamesBak;
  }

  public void setAuthorNamesBak(String authorNamesBak) {
    this.authorNamesBak = authorNamesBak;
  }

  public boolean getIsShowButton() {
    return isShowButton;
  }

  public void setShowButton(boolean isShowButton) {
    this.isShowButton = isShowButton;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public List<IndustryDTO> getIndustrys() {
    return industrys;
  }

  public void setIndustrys(List<IndustryDTO> industrys) {
    this.industrys = industrys;
  }
}
