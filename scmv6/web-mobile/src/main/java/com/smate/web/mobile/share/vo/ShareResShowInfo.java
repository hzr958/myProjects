package com.smate.web.mobile.share.vo;

import java.io.Serializable;

/**
 * 资源分享时，在界面显示的信息
 * 
 * @author wsn
 * @date May 31, 2019
 */
public class ShareResShowInfo implements Serializable {

  private String title; // 标题
  private String authorNames; // 作者
  private String briefDesc; // 描述
  private String imgSrc; // 图片url
  private String hasFullText; // 是否有全文
  private String publishYear; // 发表年份
  private String agencyName; // 基金的资助机构名称
  private String address; // 资助机构的地址
  private String fileType; // 文件类型
  private String resDes3GrpId; // 群组资源所属的群组ID

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

  public String getImgSrc() {
    return imgSrc;
  }

  public void setImgSrc(String imgSrc) {
    this.imgSrc = imgSrc;
  }

  public String getHasFullText() {
    return hasFullText;
  }

  public void setHasFullText(String hasFullText) {
    this.hasFullText = hasFullText;
  }

  public String getPublishYear() {
    return publishYear;
  }

  public void setPublishYear(String publishYear) {
    this.publishYear = publishYear;
  }

  public String getAgencyName() {
    return agencyName;
  }

  public void setAgencyName(String agencyName) {
    this.agencyName = agencyName;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getFileType() {
    return fileType;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  public String getResDes3GrpId() {
    return resDes3GrpId;
  }

  public void setResDes3GrpId(String resDes3GrpId) {
    this.resDes3GrpId = resDes3GrpId;
  }



}
