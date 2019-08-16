package com.smate.core.base.pub.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.smate.core.base.pub.enums.PubDbEnum;
import com.smate.core.base.pub.model.PubMemberInfo;
import com.smate.core.base.pub.model.PubMemberPO;
import com.smate.core.base.utils.security.Des3Utils;

/**
 * 查询的成果信息
 * 
 * @author aijiangbin
 * @date 2018年7月17日
 */
@JsonIgnoreType
public class PubInfo implements Serializable, Comparable<PubInfo> {
  private static final long serialVersionUID = 1L;
  public Long pubId;
  public String des3PubId;
  public Long ownerPsnId;
  public String des3OwnerPsnId;
  private Integer seqNo;// 序号
  /**
   * 标题
   */
  public String title;
  public String pubkeyWords;// 成果关键词 ; 分好隔开
  /**
   * 作者
   */
  public String authorNames;
  public String noneHtmlLableAuthorNames;// 去除html标签的authorNames
  private String awardAuthorList; // 奖励作者列表
  /**
   * 简介
   */
  public String briefDesc;
  /**
   * 来源 值参考:PubSnsRecordFromEnum
   */
  public Integer recordFrom;
  /**
   * 成果短地址
   */
  public String pubIndexUrl = "";
  /**
   * 是否有全文 0=没有 ； 1=有
   */
  public Integer hasFulltext = 0;
  /**
   * 全文 文件id
   */
  public Long fullTextFieId;
  /**
   * 全文下载url
   */
  public String fullTextDownloadUrl;
  /**
   * 全文图片url
   */
  public String fullTextImgUrl;
  /**
   * 全文图片后缀
   */
  public String fulltextExt;
  /**
   * 成果已经删除
   */
  public boolean pubHasDel = false;
  /**
   * 全文权限
   */
  public Integer fullTextPermission;
  /**
   * 全文图片名称
   */
  public String fullTextName;
  /**
   * 全文id 主键
   */
  public Long fullTextId;
  /**
   * 当前成果是否存在于 群组成果
   */
  public boolean existGrpPub = false;
  /**
   * 分享数
   */
  public Integer shareCount;
  /**
   * 赞数
   */
  public Integer awardCount = 0;
  private Integer readCount = 0; // 阅读次数
  private Integer citedTimes = 0; // 引用数
  public Integer commentCount = 0;
  public Long downloadCount = 0L;
  /**
   * 0=没有赞， 1=赞了
   */
  public Integer isAward = 0;
  public Integer isCollected = 0;
  private Integer articleType = 1;// 内容类型//
  // publication=1,reference=2,file=3,project=4
  /**
   * 是否是代表成果 1=是 0=不是
   */
  public Integer isRepresentPub = 0;
  /**
   * 成果状态
   */
  public Integer status;
  private PubDbEnum pubDb;// 成果所属的库
  private Integer publishYear;// 出版年份
  private Integer publishMonth;
  private Integer publishDay;
  private Integer citations;
  private Integer pubType;
  private String PubTypeName;// 成果类型名称
  private Integer isAnyUser = 7;// 成果是否公开-->7公开和4私密
  /**
   * 群组成果信息
   */
  public Integer labeled;// 是否标注 0成果未标注；1成果已标注；标注即成果资助基金信息与群组基金信息匹配
  public Integer relevance;// 相关度 相关度：成果关键词与群组关键词匹配数
  private Date collectDate; // 收藏时间
  public Date gmtModified; // 成果更新时间
  private String gmtModifiedStr;// 被修改时间
  private Long pubConfirmId;// 成果指派记录表的主键id
  private Integer confirmResult;
  private String DOI; // 成果DOI
  private String ISIId; // ISI所属库的ID(唯一标志)
  private Integer dbid;
  private String applicationNo;
  private String publicationOpenNo;
  private String standardNo;
  private String registerNo;
  private boolean mySelfPub; // 是否是自己的成果
  private Integer isInsert;
  private Long dupPubId;
  private Integer listEi = 0;
  private Integer listSci = 0;
  private Integer listIstp = 0;
  private Integer listSsci = 0;
  private Integer authermatch;
  private Integer wordHrefSeq; // word里面设置链接用的序号
  private String authorFlag; // 作者标识
  private List<PubMemberPO> memberList; // 作者列表
  private Integer srcDbId; // 来源ID
  private String sourceId; // 来源
  private Integer updateMark;
  private Integer existRepGrpPub;
  private String authorNamesHtml;
  private Integer canDownloadFulltext = 0; // 是否能下载全文， 1：能，0：不能
  private List<PubMemberInfo> memberInfo; // 作者信息

  private Integer index = 0; // 成果序号

  public Integer getExistRepGrpPub() {
    return existRepGrpPub;
  }

  public void setExistRepGrpPub(Integer existRepGrpPub) {
    this.existRepGrpPub = existRepGrpPub;
  }

  public String getAuthorNamesHtml() {
    return authorNamesHtml;
  }

  public void setAuthorNamesHtml(String authorNamesHtml) {
    this.authorNamesHtml = authorNamesHtml;
  }

  public Integer getUpdateMark() {
    return updateMark;
  }

  public void setUpdateMark(Integer updateMark) {
    this.updateMark = updateMark;
  }

  public boolean isMySelfPub() {
    return mySelfPub;
  }

  public void setMySelfPub(boolean mySelfPub) {
    this.mySelfPub = mySelfPub;
  }

  public Integer getDbid() {
    return dbid;
  }

  public void setDbid(Integer dbid) {
    this.dbid = dbid;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Integer getCitedTimes() {
    return citedTimes;
  }

  public void setCitedTimes(Integer citedTimes) {
    this.citedTimes = citedTimes;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Long getOwnerPsnId() {
    return ownerPsnId;
  }

  public void setOwnerPsnId(Long ownerPsnId) {
    this.ownerPsnId = ownerPsnId;
  }

  public String getAuthorNames() {
    return authorNames;
  }

  public void setAuthorNames(String authorNames) {
    this.authorNames = authorNames;
  }

  public String getBriefDesc() {
    return briefDesc;
  }

  public void setBriefDesc(String briefDesc) {
    this.briefDesc = briefDesc;
  }

  public String getPubIndexUrl() {
    return pubIndexUrl;
  }

  public void setPubIndexUrl(String pubIndexUrl) {
    this.pubIndexUrl = pubIndexUrl;
  }

  public Integer getHasFulltext() {
    return hasFulltext;
  }

  public void setHasFulltext(Integer hasFulltext) {
    this.hasFulltext = hasFulltext;
  }

  public Long getFullTextFieId() {
    return fullTextFieId;
  }

  public void setFullTextFieId(Long fullTextFieId) {
    this.fullTextFieId = fullTextFieId;
  }

  public String getFullTextDownloadUrl() {
    return fullTextDownloadUrl;
  }

  public void setFullTextDownloadUrl(String fullTextDownloadUrl) {
    this.fullTextDownloadUrl = fullTextDownloadUrl;
  }

  public String getFullTextImgUrl() {
    return fullTextImgUrl;
  }

  public void setFullTextImgUrl(String fullTextImgUrl) {
    this.fullTextImgUrl = fullTextImgUrl;
  }

  public Integer getRecordFrom() {
    return recordFrom;
  }

  public void setRecordFrom(Integer recordFrom) {
    this.recordFrom = recordFrom;
  }

  public boolean isExistGrpPub() {
    return existGrpPub;
  }

  public void setExistGrpPub(boolean existGrpPub) {
    this.existGrpPub = existGrpPub;
  }

  public String getDes3PubId() {
    if (StringUtils.isNotBlank(des3PubId) && pubId != null) {
      des3PubId = Des3Utils.encodeToDes3(pubId.toString());
    }
    return des3PubId;
  }

  public void setDes3PubId(String des3PubId) {
    this.des3PubId = des3PubId;
  }

  public Integer getIsRepresentPub() {
    return isRepresentPub;
  }

  public void setIsRepresentPub(Integer isRepresentPub) {
    this.isRepresentPub = isRepresentPub;
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

  public Long getDownloadCount() {
    return downloadCount;
  }

  public void setDownloadCount(Long downloadCount) {
    this.downloadCount = downloadCount;
  }

  public Integer getAwardCount() {
    return awardCount;
  }

  public void setAwardCount(Integer awardCount) {
    this.awardCount = awardCount;
  }

  public Integer getIsAward() {
    return isAward;
  }

  public void setIsAward(Integer isAward) {
    this.isAward = isAward;
  }

  public Integer getIsCollected() {
    return isCollected;
  }

  public void setIsCollected(Integer isCollected) {
    this.isCollected = isCollected;
  }

  public Integer getFullTextPermission() {
    return fullTextPermission;
  }

  public void setFullTextPermission(Integer fullTextPermission) {
    this.fullTextPermission = fullTextPermission;
  }

  public boolean isPubHasDel() {
    return pubHasDel;
  }

  public void setPubHasDel(boolean pubHasDel) {
    this.pubHasDel = pubHasDel;
  }

  public String getFullTextName() {
    return fullTextName;
  }

  public void setFullTextName(String fullTextName) {
    this.fullTextName = fullTextName;
  }

  public Long getFullTextId() {
    return fullTextId;
  }

  public void setFullTextId(Long fullTextId) {
    this.fullTextId = fullTextId;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public PubDbEnum getPubDb() {
    return pubDb;
  }

  public void setPubDb(PubDbEnum pubDb) {
    this.pubDb = pubDb;
  }

  public Integer getPublishYear() {
    return publishYear;
  }

  public void setPublishYear(Integer publishYear) {
    this.publishYear = publishYear;
  }

  public Integer getReadCount() {
    return readCount;
  }

  public void setReadCount(Integer readCount) {
    this.readCount = readCount;
  }

  public Integer getCitations() {
    return citations;
  }

  public void setCitations(Integer citations) {
    this.citations = citations;
  }

  public Integer getArticleType() {
    return articleType;
  }

  public void setArticleType(Integer articleType) {
    this.articleType = articleType;
  }

  public Integer getPubType() {
    return pubType;
  }

  public void setPubType(Integer pubType) {
    this.pubType = pubType;
  }

  public Integer getLabeled() {
    return labeled;
  }

  public void setLabeled(Integer labeled) {
    this.labeled = labeled;
  }

  public Integer getRelevance() {
    return relevance;
  }

  public void setRelevance(Integer relevance) {
    this.relevance = relevance;
  }

  public Date getGmtModified() {
    return gmtModified;
  }

  public void setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
  }

  public String getGmtModifiedStr() {
    return gmtModifiedStr;
  }

  public void setGmtModifiedStr(String gmtModifiedStr) {
    this.gmtModifiedStr = gmtModifiedStr;
  }

  public Long getPubConfirmId() {
    return pubConfirmId;
  }

  public void setPubConfirmId(Long pubConfirmId) {
    this.pubConfirmId = pubConfirmId;
  }

  public Integer getConfirmResult() {
    return confirmResult;
  }

  public void setConfirmResult(Integer confirmResult) {
    this.confirmResult = confirmResult;
  }

  public String getDOI() {
    return DOI;
  }

  public void setDOI(String dOI) {
    DOI = dOI;
  }

  public String getISIId() {
    return ISIId;
  }

  public void setISIId(String iSIId) {
    ISIId = iSIId;
  }

  public String getDes3OwnerPsnId() {
    return des3OwnerPsnId;
  }

  public void setDes3OwnerPsnId(String des3OwnerPsnId) {
    this.des3OwnerPsnId = des3OwnerPsnId;
  }

  public Integer getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  public String getPubTypeName() {
    return PubTypeName;
  }

  public void setPubTypeName(String pubTypeName) {
    PubTypeName = pubTypeName;
  }

  public String getAwardAuthorList() {
    return awardAuthorList;
  }

  public void setAwardAuthorList(String awardAuthorList) {
    this.awardAuthorList = awardAuthorList;
  }

  @Override
  public int compareTo(PubInfo o) {
    if (o.getSeqNo() < this.getSeqNo()) {
      return 1;
    } else if (o.getSeqNo() > this.getSeqNo()) {
      return -1;
    }
    return 0;
  }

  public String getPubkeyWords() {
    return pubkeyWords;
  }

  public void setPubkeyWords(String pubkeyWords) {
    this.pubkeyWords = pubkeyWords;
  }

  public String getApplicationNo() {
    return applicationNo;
  }

  public void setApplicationNo(String applicationNo) {
    this.applicationNo = applicationNo;
  }

  public String getPublicationOpenNo() {
    return publicationOpenNo;
  }

  public void setPublicationOpenNo(String publicationOpenNo) {
    this.publicationOpenNo = publicationOpenNo;
  }

  public Date getCollectDate() {
    return collectDate;
  }

  public void setCollectDate(Date collectDate) {
    this.collectDate = collectDate;
  }

  public Long getDupPubId() {
    return dupPubId;
  }

  public void setDupPubId(Long dupPubId) {
    this.dupPubId = dupPubId;
  }

  public Integer getListEi() {
    return listEi;
  }

  public void setListEi(Integer listEi) {
    this.listEi = listEi;
  }

  public Integer getListSci() {
    return listSci;
  }

  public void setListSci(Integer listSci) {
    this.listSci = listSci;
  }

  public Integer getListIstp() {
    return listIstp;
  }

  public void setListIstp(Integer listIstp) {
    this.listIstp = listIstp;
  }

  public Integer getListSsci() {
    return listSsci;
  }

  public void setListSsci(Integer listSsci) {
    this.listSsci = listSsci;
  }

  public Integer getIsInsert() {
    return isInsert;
  }

  public void setIsInsert(Integer isInsert) {
    this.isInsert = isInsert;
  }

  public Integer getAuthermatch() {
    return authermatch;
  }

  public void setAuthermatch(Integer authermatch) {
    this.authermatch = authermatch;
  }

  public Integer getWordHrefSeq() {
    return wordHrefSeq;
  }

  public void setWordHrefSeq(Integer wordHrefSeq) {
    this.wordHrefSeq = wordHrefSeq;
  }

  public String getAuthorFlag() {
    return authorFlag;
  }

  public void setAuthorFlag(String authorFlag) {
    this.authorFlag = authorFlag;
  }

  public List<PubMemberPO> getMemberList() {
    return memberList;
  }

  public void setMemberList(List<PubMemberPO> memberList) {
    this.memberList = memberList;
  }

  public Integer getIsAnyUser() {
    return isAnyUser;
  }

  public void setIsAnyUser(Integer isAnyUser) {
    this.isAnyUser = isAnyUser;
  }

  public String getFulltextExt() {
    return fulltextExt;
  }

  public void setFulltextExt(String fulltextExt) {
    this.fulltextExt = fulltextExt;
  }

  public Integer getSrcDbId() {
    return srcDbId;
  }

  public void setSrcDbId(Integer srcDbId) {
    this.srcDbId = srcDbId;
  }

  public Integer getCanDownloadFulltext() {
    return canDownloadFulltext;
  }

  public void setCanDownloadFulltext(Integer canDownloadFulltext) {
    this.canDownloadFulltext = canDownloadFulltext;
  }

  public String getNoneHtmlLableAuthorNames() {
    return noneHtmlLableAuthorNames;
  }

  public void setNoneHtmlLableAuthorNames(String noneHtmlLableAuthorNames) {
    this.noneHtmlLableAuthorNames = noneHtmlLableAuthorNames;
  }

  public Integer getIndex() {
    return index;
  }

  public void setIndex(Integer index) {
    this.index = index;
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

  public List<PubMemberInfo> getMemberInfo() {
    return memberInfo;
  }

  public void setMemberInfo(List<PubMemberInfo> memberInfo) {
    this.memberInfo = memberInfo;
  }

  public String getStandardNo() {
    return standardNo;
  }

  public void setStandardNo(String standardNo) {
    this.standardNo = standardNo;
  }

  public String getRegisterNo() {
    return registerNo;
  }

  public void setRegisterNo(String registerNo) {
    this.registerNo = registerNo;
  }

  public String getSourceId() {
    return sourceId;
  }

  public void setSourceId(String sourceId) {
    this.sourceId = sourceId;
  }

}
