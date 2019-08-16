package com.smate.center.batch.model.rol.pub;

import java.util.List;

import com.smate.center.batch.model.sns.pub.ConstPubType;


/**
 * 成果form，供成果action使用.
 * 
 * @author liqinghua
 * 
 */
public class PublicationRolForm extends BasePublicationRolForm {

  /**
   * 
   */
  private static final long serialVersionUID = -4717548203587202115L;

  private Long pubId;
  private String des3Id;
  private List<ConstPubType> typeList;
  private Integer typeId;
  private String typeZhName;
  private String typeEnName;
  private String pubXml;
  private String oldXml;
  private Integer articleType;
  private String enterUrl;
  private Integer tab;
  private String prePage;
  private String articleName;
  // 用于区分是否是单位、个人站点
  private String webContextType = "scmwebrol";
  // 卡片序号
  private String tabIndex;

  private Long currentInsId;

  private Integer currentNodeId;

  private String currentDomain;
  // 单位名称
  private String currentInsName;
  private String des3PsnId;
  private Long enterForPsnId;
  // 0、返回堆栈 1、成果文献列表2、详情页面
  private Integer backType;

  public void copyForm(PublicationRolForm result) {
    this.pubId = result.getPubId();
    this.typeId = result.getTypeId();
    this.typeZhName = result.getTypeZhName();
    this.typeEnName = result.getTypeEnName();
    this.pubXml = result.getPubXml();
    this.articleType = result.getArticleType();
    this.currentInsId = result.getCurrentInsId();
    this.currentNodeId = result.getCurrentNodeId();
    this.currentDomain = result.getCurrentDomain();
    this.currentInsName = result.getCurrentInsName();
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getTabIndex() {
    return tabIndex;
  }

  public void setTabIndex(String tabIndex) {
    this.tabIndex = tabIndex;
  }

  public List<ConstPubType> getTypeList() {
    return typeList;
  }

  public void setTypeList(List<ConstPubType> typeList) {
    this.typeList = typeList;
  }

  public Integer getTypeId() {
    return typeId;
  }

  public void setTypeId(Integer typeId) {
    this.typeId = typeId;
  }

  public String getPubXml() {
    return pubXml;
  }

  public void setPubXml(String pubXml) {
    this.pubXml = pubXml;
  }

  public Integer getArticleType() {
    return articleType;
  }

  public void setArticleType(Integer articleType) {
    this.articleType = articleType;
  }

  public String getEnterUrl() {
    return enterUrl;
  }

  public void setEnterUrl(String enterUrl) {
    this.enterUrl = enterUrl;
  }

  public String getOldXml() {
    return oldXml;
  }

  public void setOldXml(String oldXml) {
    this.oldXml = oldXml;
  }

  public Integer getTab() {
    return tab;
  }

  public void setTab(Integer tab) {
    this.tab = tab;
  }

  public String getPrePage() {
    return prePage;
  }

  public void setPrePage(String prePage) {
    this.prePage = prePage;
  }

  public String getDes3Id() {
    return des3Id;
  }

  public void setDes3Id(String des3Id) {
    this.des3Id = des3Id;
  }

  public String getTypeZhName() {
    return typeZhName;
  }

  public void setTypeZhName(String typeZhName) {
    this.typeZhName = typeZhName;
  }

  public String getTypeEnName() {
    return typeEnName;
  }

  public void setTypeEnName(String typeEnName) {
    this.typeEnName = typeEnName;
  }

  public String getArticleName() {
    return articleName;
  }

  public void setArticleName(String articleName) {
    this.articleName = articleName;
  }

  public String getWebContextType() {
    return webContextType;
  }

  public Long getCurrentInsId() {
    return currentInsId;
  }

  public void setCurrentInsId(Long currentInsId) {
    this.currentInsId = currentInsId;
  }

  public String getCurrentDomain() {
    return currentDomain;
  }

  public void setCurrentDomain(String currentDomain) {
    this.currentDomain = currentDomain;
  }

  public String getCurrentInsName() {
    return currentInsName;
  }

  public void setCurrentInsName(String currentInsName) {
    this.currentInsName = currentInsName;
  }

  public Integer getCurrentNodeId() {
    return currentNodeId;
  }

  public void setCurrentNodeId(Integer currentNodeId) {
    this.currentNodeId = currentNodeId;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public Long getEnterForPsnId() {
    return enterForPsnId;
  }

  public void setEnterForPsnId(Long enterForPsnId) {
    this.enterForPsnId = enterForPsnId;
  }

  public Integer getBackType() {
    return backType == null ? 0 : backType;
  }

  public void setBackType(Integer backType) {
    this.backType = backType;
  }

}
