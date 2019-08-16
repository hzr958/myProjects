package com.smate.web.dyn.model.mongodb.dynamic.group;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 动态内容记录表
 * 
 * @author tsz
 *
 */
@Document(collection = "GroupDynamicContent")
public class GroupDynamicContent {

  @Id
  @Indexed
  public String dynId;// 动态id
  public String dynContentEn;// 英文 动态
  public String dynContentZh;// 中文动态
  public String resDetails;// 动态详细信息（参数信息）

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

}
