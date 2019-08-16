package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 成果form，供成果action使用.
 * 
 * @author liqinghua
 * 
 */
public class PublicationForm implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 2875041231629134773L;
  private Long pubId;
  private String des3Id;
  private String specDes3Id;
  private List<ConstPubType> typeList;
  private Integer typeId;
  private String typeZhName;
  private String typeEnName;
  private String forwardUrl;
  private String pubXml;
  private String oldXml;
  private Integer articleType;
  private String enterUrl;
  private Integer tab;
  private String prePage;
  private String articleName;
  // 群组
  private String groupId;
  // 群组中的文件夹
  private String groupFolderId;
  // 节点，暂时用于群组.
  private Integer nodeId;
  // 卡片序号
  private String tabIndex;
  private Integer oldTypeId;

  private String currentDomain;
  private String from;
  private Long rptId;
  private Integer currentNodeId;
  private Long ownerPsnId;
  private String ownerPsnName;
  private String ownerPsnDes3Id;
  // 杰青guid
  private String isisGuid;
  private Long pId;
  private String briefDesc;
  private String briefDescEn;

  private boolean viewDetailOp;// 是否显示评分
  private boolean isNoMenu = false;
  // 0、返回堆栈 1、成果文献列表2、详情页面3、群组
  private Integer backType;
  // 成果隐私设置
  private String authority;
  // 成果单条与批量录入切换
  private int menuType;
  // 基准库成果对应的第三方数据库.
  private Integer dbid;
  // 分享接收主表主键.
  private String des3ResRecId;
  private String des3ResSendId;
  // 是否包含html标签
  private boolean isHtmlZhAbstract;
  private boolean isHtmlEnAbstract;


  /*
   * simpleSns增加
   */
  private List<PubSimple> vPubList; // 成果简单数据表集合

  // 新增单条成果XML
  private String newXmlData;
  // 新增多条成果XML
  private String batchXml;
  // 多条XML对象集合
  private List<PubXmlDocument> documentList;

  // 当前用户ID
  private Long psnId;

  // 返回的信息
  private Map<String, Object> returnMap;

  // leftMenuFolId
  private String leftMenuFolId;

  public Long getRptId() {
    return rptId;
  }

  public void setRptId(Long rptId) {
    this.rptId = rptId;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public Long getOwnerPsnId() {
    return ownerPsnId;
  }

  public void setOwnerPsnId(Long ownerPsnId) {
    this.ownerPsnId = ownerPsnId;
  }

  public Integer getNodeId() {
    return nodeId;
  }

  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  public Integer getOldTypeId() {
    return oldTypeId;
  }

  public void setOldTypeId(Integer oldTypeId) {
    this.oldTypeId = oldTypeId;
  }

  public void copyPublicationForm(PublicationForm form) {
    this.pubId = form.pubId;
    this.des3Id = form.des3Id;
    this.ownerPsnDes3Id = form.getOwnerPsnDes3Id();
    this.ownerPsnId = form.getOwnerPsnId();
    this.ownerPsnName = form.getOwnerPsnName();
    this.typeList = form.typeList;
    this.typeId = form.typeId;
    this.typeZhName = form.typeZhName;
    this.typeEnName = form.typeEnName;
    this.forwardUrl = form.forwardUrl;
    this.pubXml = form.pubXml;
    this.oldXml = form.oldXml;
    this.articleType = form.articleType;
    this.enterUrl = form.enterUrl;
    this.tab = form.tab;
    this.prePage = form.prePage;
    this.articleName = form.articleName;
    this.tabIndex = form.tabIndex;
    this.currentDomain = form.currentDomain;
    this.groupId = form.getGroupId();
    this.groupFolderId = form.getGroupFolderId();
    this.nodeId = form.getNodeId();
    this.isisGuid = form.getIsisGuid();
    this.briefDesc = StringUtils.isBlank(form.getBriefDesc()) ? form.getBriefDescEn() : form.getBriefDesc();
    this.briefDescEn = StringUtils.isBlank(form.getBriefDescEn()) ? form.getBriefDesc() : form.getBriefDescEn();
    this.pId = form.getPId();
    this.backType = form.getBackType();
    this.ownerPsnName = form.getOwnerPsnName();
    this.authority = form.getAuthority();
    this.isHtmlZhAbstract = form.isHtmlZhAbstract;
    this.isHtmlEnAbstract = form.isHtmlEnAbstract;
  }

  public String getSpecDes3Id() {
    return specDes3Id;
  }

  public void setSpecDes3Id(String specDes3Id) {
    this.specDes3Id = specDes3Id;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public List<ConstPubType> getTypeList() {
    return typeList;
  }

  public String getGroupId() {
    return groupId;
  }

  public String getGroupFolderId() {
    return groupFolderId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public void setGroupFolderId(String groupFolderId) {
    this.groupFolderId = groupFolderId;
  }

  public String getTabIndex() {
    return tabIndex;
  }

  public void setTabIndex(String tabIndex) {
    this.tabIndex = tabIndex;
  }

  public void setTypeList(List<ConstPubType> typeList) {
    this.typeList = typeList;
  }

  public String getForwardUrl() {
    return forwardUrl;
  }

  public void setForwardUrl(String forwardUrl) {
    this.forwardUrl = forwardUrl;
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

  /**
   * @return the pId
   */
  public Long getPId() {
    return pId;
  }

  public boolean getIsNoMenu() {
    return isNoMenu;
  }

  public void setIsNoMenu(boolean isNoMenu) {
    this.isNoMenu = isNoMenu;
  }

  /**
   * @param pId the pId to set
   */
  public void setPId(Long pId) {
    this.pId = pId;
  }

  public void setPubXml(String pubXml) {
    this.pubXml = pubXml;
  }

  /**
   * @return the isisGuid
   */
  public String getIsisGuid() {
    return isisGuid;
  }

  /**
   * @param isisGuid the isisGuid to set
   */
  public void setIsisGuid(String isisGuid) {
    this.isisGuid = isisGuid;
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

  public String getCurrentDomain() {
    return currentDomain;
  }

  public void setCurrentDomain(String currentDomain) {
    this.currentDomain = currentDomain;
  }

  public Integer getCurrentNodeId() {
    return currentNodeId;
  }

  public void setCurrentNodeId(Integer currentNodeId) {
    this.currentNodeId = currentNodeId;
  }

  public Long getCurrentPsnId() {
    return SecurityUtils.getCurrentUserId();
  }

  public boolean isViewDetailOp() {
    return viewDetailOp;
  }

  public void setViewDetailOp(boolean viewDetailOp) {
    this.viewDetailOp = viewDetailOp;
  }

  public String getBriefDesc() {
    return briefDesc;
  }

  public void setBriefDesc(String briefDesc) {
    this.briefDesc = briefDesc;
  }

  public String getBriefDescEn() {
    return briefDescEn;
  }

  public void setBriefDescEn(String briefDescEn) {
    this.briefDescEn = briefDescEn;
  }

  public Integer getBackType() {
    return backType == null ? 0 : backType;
  }

  public void setBackType(Integer backType) {
    this.backType = backType;
  }

  public String getOwnerPsnDes3Id() {
    return ownerPsnDes3Id;
  }

  public void setOwnerPsnDes3Id(String ownerPsnDes3Id) {
    this.ownerPsnDes3Id = ownerPsnDes3Id;
  }

  public String getOwnerPsnName() {
    return ownerPsnName;
  }

  public void setOwnerPsnName(String ownerPsnName) {
    this.ownerPsnName = ownerPsnName;
  }

  public String getAuthority() {
    return authority;
  }

  public void setAuthority(String authority) {
    this.authority = authority;
  }

  public int getMenuType() {
    return menuType;
  }

  public void setMenuType(int menuType) {
    this.menuType = menuType;
  }

  public Integer getDbid() {
    return dbid;
  }

  public void setDbid(Integer dbid) {
    this.dbid = dbid;
  }

  public String getDes3ResRecId() {
    return des3ResRecId;
  }

  public void setDes3ResRecId(String des3ResRecId) {
    this.des3ResRecId = des3ResRecId;
  }

  public String getDes3ResSendId() {
    return des3ResSendId;
  }

  public void setDes3ResSendId(String des3ResSendId) {
    this.des3ResSendId = des3ResSendId;
  }

  public boolean getIsHtmlZhAbstract() {
    return isHtmlZhAbstract;
  }

  public void setIsHtmlZhAbstract(boolean isHtmlZhAbstract) {
    this.isHtmlZhAbstract = isHtmlZhAbstract;
  }

  public boolean getIsHtmlEnAbstract() {
    return isHtmlEnAbstract;
  }

  public void setIsHtmlEnAbstract(boolean isHtmlEnAbstract) {
    this.isHtmlEnAbstract = isHtmlEnAbstract;
  }

  public List<PubSimple> getvPubList() {
    return vPubList;
  }

  public void setvPubList(List<PubSimple> vPubList) {
    this.vPubList = vPubList;
  }

  public String getNewXmlData() {
    return newXmlData;
  }

  public void setNewXmlData(String newXmlData) {
    this.newXmlData = newXmlData;
  }

  public String getBatchXml() {
    return batchXml;
  }

  public void setBatchXml(String batchXml) {
    this.batchXml = batchXml;
  }

  public List<PubXmlDocument> getDocumentList() {
    return documentList;
  }

  public void setDocumentList(List<PubXmlDocument> documentList) {
    this.documentList = documentList;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Map<String, Object> getReturnMap() {
    return returnMap;
  }

  public void setReturnMap(Map<String, Object> returnMap) {
    this.returnMap = returnMap;
  }

  public String getLeftMenuFolId() {
    return leftMenuFolId;
  }

  public void setLeftMenuFolId(String leftMenuFolId) {
    this.leftMenuFolId = leftMenuFolId;
  }


}
