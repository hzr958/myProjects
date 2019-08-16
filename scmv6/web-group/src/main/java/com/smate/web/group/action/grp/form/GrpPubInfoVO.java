package com.smate.web.group.action.grp.form;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.security.Des3Utils;

/**
 * 群组成果显示对象
 * 
 * @author aijiangbin
 * @date 2018年8月8日
 */
public class GrpPubInfoVO {

  public Long pubId;
  public String des3PubId;

  public Long ownerPsnId;
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
   * 当前成果是否存在于 群组成果
   */
  public boolean existGrpPub = false;
  /**
   * 是否存在标题相同的重复成果 1=是
   */
  private Integer existRepGrpPub;

  public Integer updateMark;
  /**
   * 全文权限
   */
  public Integer fullTextPermission;

  public Integer getUpdateMark() {
    return updateMark;
  }

  public void setUpdateMark(Integer updateMark) {
    this.updateMark = updateMark;
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

  public Integer getExistRepGrpPub() {
    return existRepGrpPub;
  }

  public void setExistRepGrpPub(Integer existRepGrpPub) {
    this.existRepGrpPub = existRepGrpPub;
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

}
