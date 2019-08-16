package com.smate.web.dyn.form.dynamic;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.security.Des3Utils;

/**
 * 动态成果显示对象
 * 
 * @author yhx
 */
public class DynPubInfoForm {

  public Long pubId;
  public String des3PubId;

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
   * 成果短地址
   */
  public String pubIndexUrl = "";
  /**
   * 是否有全文 0=没有 ； 1=有
   */
  public Integer hasFulltext = 0;

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

}
