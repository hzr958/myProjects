package com.smate.web.v8pub.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 重复成果显示信息类
 * 
 * @author zzx
 *
 */
public class RepeatPubInfo implements Serializable {

  private static final long serialVersionUID = 1L;
  private String des3pubSameItemId;
  private String des3RecordId;
  private String des3PubId;// 加密成果id
  private String title;// 标题
  private String authorNames;// 作者
  private String authorNamesNoTag;// 无strong标签作者
  private String briefDesc;// 来源
  private String fullTextImagePath; // 成果全文图片路径
  private String pubIndexUrl; // 成果全文短地址路径
  private String downloadUrl; // 成果下载地址
  private Date updateDate;// 最后编辑时间
  private Integer dealStatus;// 记录处理状态 0=未处理；1=已保留；2=已删除

  public String getDes3PubId() {
    return des3PubId;
  }

  public void setDes3PubId(String des3PubId) {
    this.des3PubId = des3PubId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
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

  public String getFullTextImagePath() {
    return fullTextImagePath;
  }

  public void setFullTextImagePath(String fullTextImagePath) {
    this.fullTextImagePath = fullTextImagePath;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public String getPubIndexUrl() {
    return pubIndexUrl;
  }

  public void setPubIndexUrl(String pubIndexUrl) {
    this.pubIndexUrl = pubIndexUrl;
  }

  public String getDownloadUrl() {
    return downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }

  public String getDes3pubSameItemId() {
    return des3pubSameItemId;
  }

  public void setDes3pubSameItemId(String des3pubSameItemId) {
    this.des3pubSameItemId = des3pubSameItemId;
  }

  public String getDes3RecordId() {
    return des3RecordId;
  }

  public void setDes3RecordId(String des3RecordId) {
    this.des3RecordId = des3RecordId;
  }

  public Integer getDealStatus() {
    return dealStatus;
  }

  public void setDealStatus(Integer dealStatus) {
    this.dealStatus = dealStatus;
  }

  public String getAuthorNamesNoTag() {
    return authorNamesNoTag;
  }

  public void setAuthorNamesNoTag(String authorNamesNoTag) {
    this.authorNamesNoTag = authorNamesNoTag;
  }

}
