package com.smate.web.mobile.v8pub.vo.pubfulltext;

import java.io.Serializable;
import java.util.Date;

import com.smate.web.mobile.v8pub.po.PubSnsPO;

/**
 * 成果全文匹配推荐Form.
 * 
 * @author pwl
 * 
 */
public class PubFulltextPsnRcmdVO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8242609988105949573L;

  private Long id;
  private Long rcmdId;
  /** 个人成果库成果. */
  private Long pubId;
  /** 成果拥有者. */
  private Long psnId;
  private Long fulltextFileId;
  /** 哪里来的全文：0个人库，1ISI库. */
  private Integer dbId;
  /** 匹配类型：1、sourceId，2、title. */
  private Integer matchType;
  /** 推荐时间. */
  private Date rcmdDate;
  /** 来源成果. */
  private Long srcPubId;
  private String srcDes3Id;
  /** 来源人员. */
  private Long srcPsnId;
  private String srcPsnDes3Id;
  private String srcPsnName;
  private String srcPsnAvatars;
  private String srcPsnTitolo;

  private PubFulltextPsnRcmd psnRcmd;

  private PubSnsPO pub;

  private Integer curShowCount = 0;

  private String des3Id;

  private String downloadUrl;

  private String ids;

  private String curIds;

  private String fullTextImagePath;// 全文图标

  private String pubShortUrl; // 成果短地址

  private String fulltextTitle; // 全文标题

  public PubFulltextPsnRcmdVO() {}

  public PubFulltextPsnRcmdVO(Long rcmdId, Long pubId, Long psnId, Long fulltextFileId, Integer dbId, Integer matchType,
      Date rcmdDate, Long srcPubId, Long srcPsnId) {
    this.rcmdId = rcmdId;
    this.pubId = pubId;
    this.psnId = psnId;
    this.fulltextFileId = fulltextFileId;
    this.dbId = dbId;
    this.matchType = matchType;
    this.rcmdDate = rcmdDate;
    this.srcPubId = srcPubId;
    this.srcPsnId = srcPsnId;
  }

  public PubFulltextPsnRcmdVO(Long id, Long rcmdId, Long pubId, Long psnId, Long fulltextFileId, Integer dbId,
      Integer matchType, Date rcmdDate, Long srcPubId, Long srcPsnId, PubSnsPO pub) {
    this.id = id;
    this.rcmdId = rcmdId;
    this.pubId = pubId;
    this.psnId = psnId;
    this.fulltextFileId = fulltextFileId;
    this.dbId = dbId;
    this.matchType = matchType;
    this.rcmdDate = rcmdDate;
    this.srcPubId = srcPubId;
    this.srcPsnId = srcPsnId;
    this.pub = pub;
  }

  public PubFulltextPsnRcmdVO(PubFulltextPsnRcmd psnRcmd, PubSnsPO pub) {
    this.psnRcmd = psnRcmd;
    this.pub = pub;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getRcmdId() {
    return rcmdId;
  }

  public void setRcmdId(Long rcmdId) {
    this.rcmdId = rcmdId;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getFulltextFileId() {
    return fulltextFileId;
  }

  public void setFulltextFileId(Long fulltextFileId) {
    this.fulltextFileId = fulltextFileId;
  }

  public Integer getDbId() {
    return dbId;
  }

  public void setDbId(Integer dbId) {
    this.dbId = dbId;
  }

  public Integer getMatchType() {
    return matchType;
  }

  public void setMatchType(Integer matchType) {
    this.matchType = matchType;
  }

  public Date getRcmdDate() {
    return rcmdDate;
  }

  public void setRcmdDate(Date rcmdDate) {
    this.rcmdDate = rcmdDate;
  }

  public Long getSrcPubId() {
    return srcPubId;
  }

  public void setSrcPubId(Long srcPubId) {
    this.srcPubId = srcPubId;
  }

  public Long getSrcPsnId() {
    return srcPsnId;
  }

  public void setSrcPsnId(Long srcPsnId) {
    this.srcPsnId = srcPsnId;
  }

  public Integer getCurShowCount() {
    return curShowCount;
  }

  public void setCurShowCount(Integer curShowCount) {
    this.curShowCount = curShowCount;
  }

  public PubFulltextPsnRcmd getPsnRcmd() {
    return psnRcmd;
  }

  public void setPsnRcmd(PubFulltextPsnRcmd psnRcmd) {
    this.psnRcmd = psnRcmd;
  }

  public PubSnsPO getPub() {
    return pub;
  }

  public void setPub(PubSnsPO pub) {
    this.pub = pub;
  }

  public String getSrcPsnDes3Id() {
    return srcPsnDes3Id;
  }

  public void setSrcPsnDes3Id(String srcPsnDes3Id) {
    this.srcPsnDes3Id = srcPsnDes3Id;
  }

  public String getSrcPsnName() {
    return srcPsnName;
  }

  public void setSrcPsnName(String srcPsnName) {
    this.srcPsnName = srcPsnName;
  }

  public String getSrcPsnAvatars() {
    return srcPsnAvatars;
  }

  public void setSrcPsnAvatars(String srcPsnAvatars) {
    this.srcPsnAvatars = srcPsnAvatars;
  }

  public String getSrcPsnTitolo() {
    return srcPsnTitolo;
  }

  public void setSrcPsnTitolo(String srcPsnTitolo) {
    this.srcPsnTitolo = srcPsnTitolo;
  }

  public String getSrcDes3Id() {
    return srcDes3Id;
  }

  public void setSrcDes3Id(String srcDes3Id) {
    this.srcDes3Id = srcDes3Id;
  }

  public String getDes3Id() {
    return des3Id;
  }

  public void setDes3Id(String des3Id) {
    this.des3Id = des3Id;
  }

  public String getDownloadUrl() {
    return downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }

  public String getIds() {
    return ids;
  }

  public void setIds(String ids) {
    this.ids = ids;
  }

  public String getCurIds() {
    return curIds;
  }

  public void setCurIds(String curIds) {
    this.curIds = curIds;
  }

  public String getFullTextImagePath() {
    return fullTextImagePath;
  }

  public void setFullTextImagePath(String fullTextImagePath) {
    this.fullTextImagePath = fullTextImagePath;
  }

  public String getPubShortUrl() {
    return pubShortUrl;
  }

  public void setPubShortUrl(String pubShortUrl) {
    this.pubShortUrl = pubShortUrl;
  }

  public String getFulltextTitle() {
    return fulltextTitle;
  }

  public void setFulltextTitle(String fulltextTitle) {
    this.fulltextTitle = fulltextTitle;
  }

}
