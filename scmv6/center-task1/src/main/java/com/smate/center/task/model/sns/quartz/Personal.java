package com.smate.center.task.model.sns.quartz;

import java.util.List;

import com.smate.core.base.psn.model.profile.PsnDiscipline;
import com.smate.core.base.psn.model.profile.PsnDisciplineKey;

/**
 * 个人专长.
 * 
 * @author zt
 * 
 */
public class Personal implements java.io.Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7431584151127430858L;
  private Long psnId;
  private String disIds;
  private String strDisc;
  private List<PsnDiscipline> pdList;
  private List<PsnDisciplineKey> keyList;// lgk ,拆分学科领域与关键词
  // 研究领域关键词
  private List<KeywordIdentificationForm> keywordList;

  // 基本信息下方显示的关键词 需要控制长度。 tsz
  private List<PsnDisciplineKey> baseKeyList;
  // 是否有学科，更新档案完整度时使用.
  private boolean discExit = false;
  // 学科代码JSON
  private String disJSON;

  // 学科代码设置的权限
  private Integer permission;

  public Long getPsnId() {
    return psnId;
  }

  public String getDisIds() {
    return disIds;
  }

  public boolean isDiscExit() {
    return discExit;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setDisIds(String disIds) {
    this.disIds = disIds;
  }

  public void setDiscExit(boolean discExit) {
    this.discExit = discExit;
  }

  public List<PsnDiscipline> getPdList() {
    return pdList;
  }

  public String getDisJSON() {
    return disJSON;
  }

  public void setPdList(List<PsnDiscipline> pdList) {
    this.pdList = pdList;
  }

  public void setDisJSON(String disJSON) {
    this.disJSON = disJSON;
  }

  public String getStrDisc() {
    return strDisc;
  }

  public void setStrDisc(String strDisc) {
    this.strDisc = strDisc;
  }

  public List<PsnDisciplineKey> getKeyList() {
    return keyList;
  }

  public void setKeyList(List<PsnDisciplineKey> keyList) {
    this.keyList = keyList;
  }

  public Integer getPermission() {
    return permission;
  }

  public void setPermission(Integer permission) {
    this.permission = permission;
  }

  public List<KeywordIdentificationForm> getKeywordList() {
    return keywordList;
  }

  public void setKeywordList(List<KeywordIdentificationForm> keywordList) {
    this.keywordList = keywordList;
  }

  public List<PsnDisciplineKey> getBaseKeyList() {
    return baseKeyList;
  }

  public void setBaseKeyList(List<PsnDisciplineKey> baseKeyList) {
    this.baseKeyList = baseKeyList;
  }

}
