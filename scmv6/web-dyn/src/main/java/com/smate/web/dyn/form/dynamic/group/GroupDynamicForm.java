package com.smate.web.dyn.form.dynamic.group;

import java.util.Map;

/**
 * 
 * @author tsz
 *
 */
public class GroupDynamicForm {
  private String locale;// 语言类型
  private Long psnId; // 动态创建人
  private Long groupId; // 群组id
  private Long groupDynId; // 动态id
  private String resType; // 动态资源类型
  private Long resId; // 动态资源id
  private String dynContent; // 动态内容
  private String tempType; // 动态模版类型
  private Long receiverGrpId; // 动态接收群组(产生动态的群组) 如果为空就取groupId
  private Integer databaseType = 1; // 区分个人库和基准库的分享 1 个人库 2基准库

  private String template; // 动态模版类型

  private Integer dbId;// 网站ID

  private Map<String, Object> pubSimpleMap;
  private String resInfoJson; // 资源信息json格式

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getGroupDynId() {
    return groupDynId;
  }

  public void setGroupDynId(Long groupDynId) {
    this.groupDynId = groupDynId;
  }

  public String getResType() {
    return resType;
  }

  public void setResType(String resType) {
    this.resType = resType;
  }

  public Long getResId() {
    return resId;
  }

  public void setResId(Long resId) {
    this.resId = resId;
  }

  public String getDynContent() {
    return dynContent;
  }

  public void setDynContent(String dynContent) {
    this.dynContent = dynContent;
  }

  public String getTempType() {
    return tempType;
  }

  public void setTempType(String tempType) {
    this.tempType = tempType;
  }

  public String getTemplate() {
    return template;
  }

  public void setTemplate(String template) {
    this.template = template;
  }

  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public Map<String, Object> getPubSimpleMap() {
    return pubSimpleMap;
  }

  public void setPubSimpleMap(Map<String, Object> pubSimpleMap) {
    this.pubSimpleMap = pubSimpleMap;
  }

  public Long getReceiverGrpId() {
    if (receiverGrpId == null) {
      return groupId;
    }
    return receiverGrpId;
  }

  public void setReceiverGrpId(Long receiverGrpId) {
    this.receiverGrpId = receiverGrpId;
  }

  public Integer getDbId() {
    return dbId;
  }

  public void setDbId(Integer dbId) {
    this.dbId = dbId;
  }

  public Integer getDatabaseType() {
    return databaseType;
  }

  public void setDatabaseType(Integer databaseType) {
    this.databaseType = databaseType;
  }

  public String getResInfoJson() {
    return resInfoJson;
  }

  public void setResInfoJson(String resInfoJson) {
    this.resInfoJson = resInfoJson;
  }

}
