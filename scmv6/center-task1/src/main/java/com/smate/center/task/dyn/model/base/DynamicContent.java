package com.smate.center.task.dyn.model.base;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 动态初始信息数据
 * 
 * @author zk
 *
 */
@Document(collection = "DynamicContent")
public class DynamicContent implements Serializable {

  private static final long serialVersionUID = -3535165612383791403L;

  @Id
  @Indexed
  private String dynId; // 动态原始信息表Id
  private String dynContentEn; // 动态英文内容
  private String dynContentZh; // 动态中文内容
  private String resDetails;// 动态详细信息
  private String mobileDynContentEn; // 移动端英文内容
  private String mobileDynContentZh; // 移动端中文内容

  public DynamicContent() {}

  public DynamicContent(String dynId, String dynContentEn, String dynContentZh, String resDetails) {
    this.dynId = dynId;
    this.dynContentZh = dynContentZh;
    this.dynContentEn = dynContentEn;
    this.resDetails = resDetails;
  }

  public String getDynId() {
    return dynId;
  }

  public void setDynId(String dynId) {
    this.dynId = dynId;
  }

  public String getDynContentEn() {
    return dynContentEn;
  }

  public void setDynContentEn(String dynContentEn) {
    this.dynContentEn = dynContentEn;
  }

  public String getDynContentZh() {
    return dynContentZh;
  }

  public void setDynContentZh(String dynContentZh) {
    this.dynContentZh = dynContentZh;
  }

  public String getResDetails() {
    return resDetails;
  }

  public void setResDetails(String resDetails) {
    this.resDetails = resDetails;
  }

  public String getMobileDynContentEn() {
    return mobileDynContentEn;
  }

  public void setMobileDynContentEn(String mobileDynContentEn) {
    this.mobileDynContentEn = mobileDynContentEn;
  }

  public String getMobileDynContentZh() {
    return mobileDynContentZh;
  }

  public void setMobileDynContentZh(String mobileDynContentZh) {
    this.mobileDynContentZh = mobileDynContentZh;
  }
}
