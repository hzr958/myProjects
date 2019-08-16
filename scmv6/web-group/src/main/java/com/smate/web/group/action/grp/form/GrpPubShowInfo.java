package com.smate.web.group.action.grp.form;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.security.Des3Utils;

/**
 * 群组成果显示对象
 * 
 * @author tsz
 *
 */
public class GrpPubShowInfo {

  private Long pubId;
  private String des3PubId;
  private Integer canDelete = 0; // 是否可以删除 1是 0否
  private Integer canMark = 0;// 是否可以标记 1是 0否
  private Integer canEdit = 0; // 是否可以编辑 1是 0否
  private String zhTitle;
  private String enTitle;
  private String showTitle;
  private String zhBrif;
  private String enBrif;
  private String showBrif;
  private Integer publishYear;// 出版年份
  private String authors;
  private Integer labeld; // 是否标注 0成果未标注；1成果已标注；标注即成果资助基金信息与群组基金信息匹配
  private Integer relevance;// 相关度 相关度：成果关键词与群组关键词匹配数
  private Integer isProjectPub; // 是否项目成果 是否项目成果 (0否)（1是）
  private Integer hasFulltext; // 是否 有全文 1是 0否
  private String fullTextField;
  private String fullTextImaUrl; // 全文图片路径
  private String fullTextUrl; // 全文下载地址
  private Integer citedTimes;// 引用次数

  private String des3RecvPsnId; // 成果拥有者加密Id
  private Integer awardCount = 0;
  private Integer shareCount = 0;
  private Integer commentCount = 0;
  private Integer isAward = 0; // 是否赞过 1 是 0否

  private Integer isImport; // 是否 已经导入 1是 0否
  private String pubIndexUrl;// 成果短地址
  private Integer updateMark;
  private Integer existGrpPub;// 是否是已存在群组的成果 1=是
  private Integer isOwn = 0;// 是否是自己的成果 1=是，0=不是
  private String fundInfo;
  private String fundInfoComplete;
  private Integer fullTextPermission;// 全文权限
  public String noneHtmlLableAuthorNames;// 去除html标签的authorNames
  private Integer permission;// 成果权限 7公开，4私有

  public Long getPubId() {
    if (pubId == null && StringUtils.isNotBlank(des3PubId)) {
      pubId = Long.parseLong(Des3Utils.decodeFromDes3(des3PubId));
    }
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Integer getCanDelete() {
    return canDelete;
  }

  public void setCanDelete(Integer canDelete) {
    this.canDelete = canDelete;
  }

  public Integer getCanMark() {
    return canMark;
  }

  public void setCanMark(Integer canMark) {
    this.canMark = canMark;
  }

  public String getAuthors() {
    return authors;
  }

  public void setAuthors(String authors) {
    this.authors = authors;
  }

  public Integer getLabeld() {
    return labeld;
  }

  public void setLabeld(Integer labeld) {
    this.labeld = labeld;
  }

  public Integer getRelevance() {
    return relevance;
  }

  public void setRelevance(Integer relevance) {
    this.relevance = relevance;
  }

  public Integer getIsProjectPub() {
    return isProjectPub;
  }

  public void setIsProjectPub(Integer isProjectPub) {
    this.isProjectPub = isProjectPub;
  }

  public Integer getHasFulltext() {
    return hasFulltext;
  }

  public void setHasFulltext(Integer hasFulltext) {
    this.hasFulltext = hasFulltext;
  }

  public String getFullTextImaUrl() {
    return fullTextImaUrl;
  }

  public void setFullTextImaUrl(String fullTextImaUrl) {
    this.fullTextImaUrl = fullTextImaUrl;
  }

  public String getZhTitle() {
    return zhTitle;
  }

  public void setZhTitle(String zhTitle) {
    this.zhTitle = zhTitle;
  }

  public String getZhBrif() {
    return zhBrif;
  }

  public void setZhBrif(String zhBrif) {
    this.zhBrif = zhBrif;
  }

  public String getEnBrif() {
    return enBrif;
  }

  public void setEnBrif(String enBrif) {
    this.enBrif = enBrif;
  }

  public String getEnTitle() {
    return enTitle;
  }

  public void setEnTitle(String enTitle) {
    this.enTitle = enTitle;
  }

  public Integer getCanEdit() {
    return canEdit;
  }

  public void setCanEdit(Integer canEdit) {
    this.canEdit = canEdit;
  }

  public Integer getCitedTimes() {
    return citedTimes;
  }

  public void setCitedTimes(Integer citedTimes) {
    this.citedTimes = citedTimes;
  }

  public Integer getIsImport() {
    return isImport;
  }

  public void setIsImport(Integer isImport) {
    this.isImport = isImport;
  }

  public Integer getIsAward() {
    return isAward;
  }

  public void setIsAward(Integer isAward) {
    this.isAward = isAward;
  }

  public String getFullTextField() {
    return fullTextField;
  }

  public void setFullTextField(String fullTextField) {
    this.fullTextField = fullTextField;
  }

  public String getPubIndexUrl() {
    return pubIndexUrl;
  }

  public void setPubIndexUrl(String pubIndexUrl) {
    this.pubIndexUrl = pubIndexUrl;
  }

  public String getDes3PubId() {
    if (StringUtils.isBlank(des3PubId) && pubId != null) {
      des3PubId = Des3Utils.encodeToDes3(pubId.toString());
    }
    return des3PubId;
  }

  public void setDes3PubId(String des3PubId) {
    this.des3PubId = des3PubId;
  }

  /**
   * @return fullTextUrl
   */
  public String getFullTextUrl() {
    return fullTextUrl;
  }

  /**
   * @param fullTextUrl 要设置的 fullTextUrl
   */
  public void setFullTextUrl(String fullTextUrl) {
    this.fullTextUrl = fullTextUrl;
  }

  public Integer getUpdateMark() {
    return updateMark;
  }

  public void setUpdateMark(Integer updateMark) {
    this.updateMark = updateMark;
  }

  public Integer getExistGrpPub() {
    return existGrpPub;
  }

  public void setExistGrpPub(Integer existGrpPub) {
    this.existGrpPub = existGrpPub;
  }

  public String getShowTitle() {
    return showTitle;
  }

  public void setShowTitle(String showTitle) {
    this.showTitle = showTitle;
  }

  public String getShowBrif() {
    return showBrif;
  }

  public void setShowBrif(String showBrif) {
    this.showBrif = showBrif;
  }

  public String getDes3RecvPsnId() {
    return des3RecvPsnId;
  }

  public void setDes3RecvPsnId(String des3RecvPsnId) {
    this.des3RecvPsnId = des3RecvPsnId;
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

  public Integer getIsOwn() {
    return isOwn;
  }

  public void setIsOwn(Integer isOwn) {
    this.isOwn = isOwn;
  }

  public String getFundInfo() {
    return fundInfo;
  }

  public void setFundInfo(String fundInfo) {
    this.fundInfo = fundInfo;
  }

  public String getFundInfoComplete() {
    return fundInfoComplete;
  }

  public void setFundInfoComplete(String fundInfoComplete) {
    this.fundInfoComplete = fundInfoComplete;
  }

  public Integer getFullTextPermission() {
    return fullTextPermission;
  }

  public void setFullTextPermission(Integer fullTextPermission) {
    this.fullTextPermission = fullTextPermission;
  }

  public String getNoneHtmlLableAuthorNames() {
    return noneHtmlLableAuthorNames;
  }

  public void setNoneHtmlLableAuthorNames(String noneHtmlLableAuthorNames) {
    this.noneHtmlLableAuthorNames = noneHtmlLableAuthorNames;
  }

  public Integer getPermission() {
    return permission;
  }

  public void setPermission(Integer permission) {
    this.permission = permission;
  }

  public Integer getPublishYear() {
    return publishYear;
  }

  public void setPublishYear(Integer publishYear) {
    this.publishYear = publishYear;
  }

  public Integer getCommentCount() {
    return commentCount;
  }

  public void setCommentCount(Integer commentCount) {
    this.commentCount = commentCount;
  }

}
