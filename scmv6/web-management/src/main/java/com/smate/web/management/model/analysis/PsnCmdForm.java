package com.smate.web.management.model.analysis;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import com.smate.core.base.utils.string.ServiceUtil;


/**
 * 人员推荐Form.
 * 
 * @author pwl
 * 
 */
public class PsnCmdForm implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -4786030749867047672L;
  private Long psnId;
  private String des3PsnId;
  private String name;
  private String firstName;
  private String lastName;
  /** 人员显示名称. */
  private String viewName;
  /** 人员他头衔. */
  private String titolo;
  /** 人员头像. */
  private String avatars;
  /** 单位名称. */
  private String insName;
  private int insFlag;
  private int knowFlag;
  private int isFriend;
  private Long superDiscId;
  private Long subDiscId;
  /** 阅读次数. */
  private Long readCount;
  /** 成果数. */
  private Long pubCount;
  /** 引用次数. */
  private int citeCount;
  /** 匹配度. */
  private Double matchingDegree;

  /** 关键词json字符串. */
  private String keywordJson;
  /** 研究领域id. */
  private Long discId = 0L;
  /** 是否重新加载推荐人员信息. */
  private Integer loadFlag = 0;
  // 是否关注
  private int isAttPerson;

  private String psnViewName;// 人员显示名称_MJG_SCM-5707.

  private Integer hIndex; // 人员h指数

  private Integer scoreLevel; // 人员相关性评分

  // 关键词分布分析
  private String keywordsDistributionJson;

  // 关键词年份统计
  private String keywordsCountByYearJson;

  private String mmId;
  private String selectedId;

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getDes3PsnId() {
    if (this.psnId != null && des3PsnId == null) {
      des3PsnId = ServiceUtil.encodeToDes3(this.psnId.toString());
    }
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getViewName() {
    String locale = LocaleContextHolder.getLocale().toString();
    if ("zh_CN".equals(locale)) {
      viewName = StringUtils.isNotBlank(name) ? name : firstName + " " + lastName;
    } else {
      viewName =
          (StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(lastName)) ? firstName + " " + lastName : name;
    }
    return viewName;
  }

  public void setViewName(String viewName) {
    this.viewName = viewName;
  }

  public String getTitolo() {
    return titolo;
  }

  public void setTitolo(String titolo) {
    this.titolo = titolo;
  }

  public String getAvatars() {
    return avatars;
  }

  public void setAvatars(String avatars) {
    this.avatars = avatars;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public int getInsFlag() {
    return insFlag;
  }

  public void setInsFlag(int insFlag) {
    this.insFlag = insFlag;
  }

  public int getKnowFlag() {
    return knowFlag;
  }

  public void setKnowFlag(int knowFlag) {
    this.knowFlag = knowFlag;
  }

  public int getIsFriend() {
    return isFriend;
  }

  public void setIsFriend(int isFriend) {
    this.isFriend = isFriend;
  }

  public Long getSuperDiscId() {
    return superDiscId;
  }

  public void setSuperDiscId(Long superDiscId) {
    this.superDiscId = superDiscId;
  }

  public Long getSubDiscId() {
    return subDiscId;
  }

  public void setSubDiscId(Long subDiscId) {
    this.subDiscId = subDiscId;
  }

  public Long getReadCount() {
    return readCount;
  }

  public void setReadCount(Long readCount) {
    this.readCount = readCount;
  }

  public Long getPubCount() {
    return pubCount;
  }

  public void setPubCount(Long pubCount) {
    this.pubCount = pubCount;
  }

  public int getCiteCount() {
    return citeCount;
  }

  public void setCiteCount(int citeCount) {
    this.citeCount = citeCount;
  }

  public Double getMatchingDegree() {
    return matchingDegree;
  }

  public void setMatchingDegree(Double matchingDegree) {
    this.matchingDegree = matchingDegree;
  }

  public String getKeywordJson() {
    return keywordJson;
  }

  public void setKeywordJson(String keywordJson) {
    this.keywordJson = keywordJson;
  }

  public Long getDiscId() {
    return discId;
  }

  public void setDiscId(Long discId) {
    this.discId = discId;
  }

  public Integer getLoadFlag() {
    return loadFlag;
  }

  public void setLoadFlag(Integer loadFlag) {
    this.loadFlag = loadFlag;
  }

  public int getIsAttPerson() {
    return isAttPerson;
  }

  public void setIsAttPerson(int isAttPerson) {
    this.isAttPerson = isAttPerson;
  }

  public String getPsnViewName() {
    return psnViewName;
  }

  public void setPsnViewName(String psnViewName) {
    this.psnViewName = psnViewName;
  }

  public Integer getScoreLevel() {
    return scoreLevel;
  }

  public void setScoreLevel(Integer scoreLevel) {
    this.scoreLevel = scoreLevel;
  }

  public Integer gethIndex() {
    return hIndex;
  }

  public void sethIndex(Integer hIndex) {
    this.hIndex = hIndex;
  }

  public String getKeywordsDistributionJson() {
    return keywordsDistributionJson;
  }

  public void setKeywordsDistributionJson(String keywordsDistributionJson) {
    this.keywordsDistributionJson = keywordsDistributionJson;
  }

  public String getKeywordsCountByYearJson() {
    return keywordsCountByYearJson;
  }

  public void setKeywordsCountByYearJson(String keywordsCountByYearJson) {
    this.keywordsCountByYearJson = keywordsCountByYearJson;
  }

  public String getMmId() {
    return mmId;
  }

  public void setMmId(String mmId) {
    this.mmId = mmId;
  }

  public String getSelectedId() {
    return selectedId;
  }

  public void setSelectedId(String selectedId) {
    this.selectedId = selectedId;
  }

}
