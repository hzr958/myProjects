package com.smate.web.management.model.grp;

import com.smate.core.base.utils.string.StringUtils;

import java.io.Serializable;

/**
 * 群组信息
 *
 * @author aijiangbin
 * @create 2019-07-09 14:14
 **/
public class GrpItemInfo implements Serializable{

  private static final long serialVersionUID = -9190580399197052476L;
  public String groupName = "";//
  public String groupType = "" ;// 群组类型   10 = 课程 11 = 项目  12=兴趣群组
  public String breif = "" ;// 描述
  public String smateFirstCategoryId = "" ;//  科技领域一级
  public String smateSecondCategoryId = "" ;//  科技领域二级
  public String nsfcCategoryId = "" ;//  学科代码
  public String keyword = "" ;// 关键词  英文分号分开，超过10
  public String openType = "" ;//  g公开类型  O=公开   h=半公开   P=隐私
  public String openModule = "" ; //  暂时不处理
  public String ownerPsnId = "" ; // 群组拥有者，为空就取当前人



  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public String getGroupType() {
    return groupType;
  }

  public void setGroupType(String groupType) {
    this.groupType = groupType;
  }

  public String getBreif() {
    return breif;
  }

  public void setBreif(String breif) {
    this.breif = breif;
  }

  public String getSmateFirstCategoryId() {
    return smateFirstCategoryId;
  }

  public void setSmateFirstCategoryId(String smateFirstCategoryId) {
    this.smateFirstCategoryId = smateFirstCategoryId;
  }

  public String getSmateSecondCategoryId() {
    return smateSecondCategoryId;
  }

  public void setSmateSecondCategoryId(String smateSecondCategoryId) {
    this.smateSecondCategoryId = smateSecondCategoryId;
  }

  public String getNsfcCategoryId() {
    return nsfcCategoryId;
  }

  public void setNsfcCategoryId(String nsfcCategoryId) {
    this.nsfcCategoryId = nsfcCategoryId;
  }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public String getOpenType() {
    if(StringUtils.isNotBlank(openType)){
      openType = openType.toUpperCase();
    }
    return openType;
  }

  public void setOpenType(String openType) {
    this.openType = openType;
  }

  public String getOpenModule() {
    return openModule;
  }

  public void setOpenModule(String openModule) {
    this.openModule = openModule;
  }

  public String getOwnerPsnId() {
    return ownerPsnId;
  }

  public void setOwnerPsnId(String ownerPsnId) {
    this.ownerPsnId = ownerPsnId;
  }



  @Override
  public String toString() {
    return "GrpItemInfo{" + "groupName='" + groupName + '\'' + ", groupType='" + groupType + '\'' + ", breif='" + breif
        + '\'' + ", smateFirstCategoryId='" + smateFirstCategoryId + '\'' + ", smateSecondCategoryId='"
        + smateSecondCategoryId + '\'' + ", nsfcCategoryId='" + nsfcCategoryId + '\'' + ", keyword='" + keyword + '\''
        + ", openType='" + openType + '\'' + ", openModule='" + openModule + '\'' + ", ownerPsnId='" + ownerPsnId + '\''
        + '}';
  }
}
