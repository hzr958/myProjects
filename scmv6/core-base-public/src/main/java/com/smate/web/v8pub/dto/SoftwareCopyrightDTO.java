package com.smate.web.v8pub.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smate.core.base.pub.enums.PubSCAcquisitionTypeEnum;
import com.smate.core.base.pub.enums.PubSCScopeTypeEnum;

/**
 * 软件著作权信息
 * 
 * @author YJ
 *
 *         2019年5月20日
 */


@JsonIgnoreProperties(ignoreUnknown = true)
public class SoftwareCopyrightDTO extends PubTypeInfoDTO implements Serializable {

  private static final long serialVersionUID = -7791648198767548822L;

  private String registerNo; // 登记号
  private String registerDate; // 登记日期
  private String firstPublishDate; // 首次发表日期
  private String publicityDate; // 公示日期
  private String categoryNo; // 分类号
  private PubSCAcquisitionTypeEnum acquisitionType; // 权利获得方式
  private PubSCScopeTypeEnum scopeType; // 权利范围

  public PubSCAcquisitionTypeEnum getAcquisitionType() {
    return acquisitionType;
  }

  public void setAcquisitionType(PubSCAcquisitionTypeEnum acquisitionType) {
    this.acquisitionType = acquisitionType;
  }

  public PubSCScopeTypeEnum getScopeType() {
    return scopeType;
  }

  public void setScopeType(PubSCScopeTypeEnum scopeType) {
    this.scopeType = scopeType;
  }

  public String getRegisterNo() {
    return registerNo;
  }

  public void setRegisterNo(String registerNo) {
    this.registerNo = registerNo;
  }

  public String getRegisterDate() {
    return registerDate;
  }

  public void setRegisterDate(String registerDate) {
    this.registerDate = registerDate;
  }

  public String getFirstPublishDate() {
    return firstPublishDate;
  }

  public void setFirstPublishDate(String firstPublishDate) {
    this.firstPublishDate = firstPublishDate;
  }

  public String getCategoryNo() {
    return categoryNo;
  }

  public void setCategoryNo(String categoryNo) {
    this.categoryNo = categoryNo;
  }

  public String getPublicityDate() {
    return publicityDate;
  }

  public void setPublicityDate(String publicityDate) {
    this.publicityDate = publicityDate;
  }

  @Override
  public String toString() {
    return "SoftwareCopyrightBean{" + "acquisitionType=" + acquisitionType + ", scopeType=" + scopeType + '}';
  }
}
