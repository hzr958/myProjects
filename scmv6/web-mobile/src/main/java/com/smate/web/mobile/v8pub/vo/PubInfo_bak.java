package com.smate.web.mobile.v8pub.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.smate.core.base.utils.security.Des3Utils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * 查询的成果信息
 * 
 * @author aijiangbin
 * @date 2018年7月17日
 */
@JsonIgnoreType
public class PubInfo_bak {

  public Long pubId;

  public String des3PubId;

  public Long ownerPsnId;
  /**
   * 标题
   */
  public String title;
  /**
   * 作者
   */
  public String authorNames;
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
  // private PubDbEnum pubDb;// 成果所属的库
  private String pubDb;
  private Integer publishYear;// 出版年份
  private Integer citations;
  private Integer pubType;

  /**
   * 群组成果信息
   */
  public Integer labeled;// 是否标注 0成果未标注；1成果已标注；标注即成果资助基金信息与群组基金信息匹配
  public Integer relevance;// 相关度 相关度：成果关键词与群组关键词匹配数

  public Date gmtModified; // 成果更新时间
  private String gmtModifiedStr;// 被修改时间
  private Long pubConfirmId;// 成果指派记录表的主键id
  private Integer confirmResult;
  private String DOI; // 成果DOI
  private String ISIId; // ISI所属库的ID(唯一标志)
  private Integer dbid;
  private boolean mySelfPub; // 是否是自己的成果

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

  public String getPubDb() {
    return pubDb;
  }

  public void setPubDb(String pubDb) {
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

}
