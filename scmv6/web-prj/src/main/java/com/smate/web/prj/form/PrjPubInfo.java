package com.smate.web.prj.form;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.security.Des3Utils;

/**
 * 项目成果显示对象
 * 
 * @date 2019年8月7日
 */
public class PrjPubInfo {

  public Long pubId;
  public String des3PubId;

  private Integer publishYear;// 出版年份
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
   * 全文权限
   */
  public Integer fullTextPermission;

  public Integer awardCount = 0;
  public Integer shareCount = 0;

  /**
   * 0=没有赞， 1=赞了
   */
  public Integer isAward = 0;

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

  public String getDes3PubId() {
    if (StringUtils.isNotBlank(des3PubId) && pubId != null) {
      des3PubId = Des3Utils.encodeToDes3(pubId.toString());
    }
    return des3PubId;
  }

  public void setDes3PubId(String des3PubId) {
    this.des3PubId = des3PubId;
  }

  public Integer getFullTextPermission() {
    return fullTextPermission;
  }

  public void setFullTextPermission(Integer fullTextPermission) {
    this.fullTextPermission = fullTextPermission;
  }

  public Integer getPublishYear() {
    return publishYear;
  }

  public void setPublishYear(Integer publishYear) {
    this.publishYear = publishYear;
  }

  public Integer getAwardCount() {
    return awardCount;
  }

  public void setAwardCount(Integer awardCount) {
    this.awardCount = awardCount;
  }

  public Integer getShareCount() {
    return shareCount;
  }

  public void setShareCount(Integer shareCount) {
    this.shareCount = shareCount;
  }

  public Integer getIsAward() {
    return isAward;
  }

  public void setIsAward(Integer isAward) {
    this.isAward = isAward;
  }

}
