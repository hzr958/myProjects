package com.smate.center.open.model.group;

public class QuickCreateGroup {
  public String groupCategory;// 群组分类
  public String groupName; // :群组名称
  public String groupDescription; // :群组简介
  public String disciplines; // 研究领域
  public String keyWords; // ： 关键词 //:群组名称
  public String openType = "H"; // 公开类型[O=开放,H=半开放,P=保密]
  public String fundingTypes; // 资助类别

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public String getGroupDescription() {
    return groupDescription;
  }

  public void setGroupDescription(String groupDescription) {
    this.groupDescription = groupDescription;
  }

  public String getDisciplines() {
    return disciplines;
  }

  public void setDisciplines(String disciplines) {
    this.disciplines = disciplines;
  }

  public String getKeyWords() {
    return keyWords;
  }

  public void setKeyWords(String keyWords) {
    this.keyWords = keyWords;
  }

  public String getOpenType() {
    return openType;
  }

  public void setOpenType(String openType) {
    this.openType = openType;
  }

  public String getGroupCategory() {
    return groupCategory;
  }

  public void setGroupCategory(String groupCategory) {
    this.groupCategory = groupCategory;
  }

  public String getFundingTypes() {
    return fundingTypes;
  }

  public void setFundingTypes(String fundingTypes) {
    this.fundingTypes = fundingTypes;
  }



}
