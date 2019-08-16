package com.smate.center.open.model.group;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 群组控制开关表.
 * 
 * @author mjg
 * @since 2014-11-28
 */
@Entity
@Table(name = "GROUP_CONTROL")
public class GroupControl implements Serializable {

  private static final long serialVersionUID = 2515644238927448000L;
  // 群组ID
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_GROUP_CONTROL", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;
  @Column(name = "GROUP_ID")
  private Long groupId;
  // 项目组成员是否可以查看其他群组成员的科研项目和成果[1=是,0=否]
  @Column(name = "IS_GROUP_MEMBER_VIEW")
  private String isGroupMemberView = "1";
  // 是否支持讨论板[1=是,0=否]
  @Column(name = "IS_DISCUSS")
  private String isDiscuss = "0";
  // 是否支持文件共享[1=是,0=否]
  @Column(name = "IS_SHARE_FILE")
  private String isShareFile = "0";
  // 文件共享权限类型[1=所有成员,0=管理员]
  @Column(name = "SHARE_FILE_TYPE")
  private String shareFileType = "1";
  // 是否支持群组项目[1=是,0=否]
  @Column(name = "IS_PRJ_VIEW")
  private String isPrjView = "0";
  // 群组项目权限类型[1=所有成员,0=管理员]
  @Column(name = "PRJ_VIEW_TYPE")
  private String prjViewType = "1";
  // 是否支持群组成果[1=是,0=否]
  @Column(name = "IS_PUB_VIEW")
  private String isPubView = "0";
  // 群组成果权限类型[1=所有成员,0=管理员]
  @Column(name = "PUB_VIEW_TYPE")
  private String pubViewType = "1";
  // 是否支持群组参考文献[1=是,0=否]
  @Column(name = "IS_REF_VIEW")
  private String isRefView = "0";
  // 参考文献权限类型[1=所有成员,0=管理员]
  @Column(name = "REF_VIEW_TYPE")
  private String refViewType = "1";
  // 是否支持作业[1=是,0=否]
  @Column(name = "IS_WORK_VIEW")
  private String isWorkView = "0";
  // 作业权限类型[1=所有成员,0=管理员]
  // private String workViewType = "0";
  // 是否支持教学课件[1=是,0=否]
  @Column(name = "IS_MATERIAL_VIEW")
  private String isMaterialView = "0";
  // 作业权限类型[1=所有成员,0=管理员]
  // private String materialViewType = "0";

  // 是否允许普通成员发表话题[1=是,0=否]
  @Column(name = "IS_MEMBER_PUBLISH")
  private String isMemberPublish = "1";
  // 群组主页是否开放[1=是,0=否]
  @Column(name = "IS_PAGE_OPEN")
  private String isPageOpen = "1";
  // 主页是否显示群组介绍[1=是,0=否]
  @Column(name = "IS_PAGE_DESC_OPEN")
  private String isPageDescOpen = "1";
  // 主页是否显示群组成员[1=是,0=否]
  @Column(name = "IS_PAGE_MEMBER_OPEN")
  private String isPageMemberOpen = "1";
  // 主页是否显示科研项目[1=是,0=否]
  @Column(name = "IS_PAGE_PRJ_OPEN")
  private String isPagePrjOpen = "1";
  // 主页是否显示科研成果[1=是,0=否]
  @Column(name = "IS_PAGE_PUB_OPEN")
  private String isPagePubOpen = "1";
  // 主页是否显示科研文献[1=是,0=否]
  @Column(name = "IS_PAGE_REF_OPEN")
  private String isPageRefOpen = "1";
  // 主页是否显示文件[1=是,0=否]
  @Column(name = "IS_PAGE_FILE_OPEN")
  private String isPageFileOpen = "1";

  // 主页是否显示作业[1=是,0=否]
  @Column(name = "IS_PAGE_WORK_OPEN")
  private String isPageWorkOpen = "0";
  // 主页是否显示教学课件[1=是,0=否]
  @Column(name = "IS_PAGE_MATERIAL_OPEN")
  private String isPageMaterialOpen = "0";
  // 是否已经进行保存
  @Transient
  private String isSave;
  @Column(name = "IS_ISIS_PRJ")
  private Integer isIsisPrj;

  public GroupControl() {
    super();
  }

  public GroupControl(Long groupId, String isGroupMemberView, String isDiscuss, String isShareFile,
      String shareFileType, String isPrjView, String prjViewType, String isPubView, String pubViewType,
      String isRefView, String refViewType, String isWorkView, String isMaterialView, String isMemberPublish,
      String isPageOpen, String isPageDescOpen, String isPageMemberOpen, String isPagePrjOpen, String isPagePubOpen,
      String isPageRefOpen, String isPageFileOpen, String isPageWorkOpen, String isPageMaterialOpen, String isSave,
      Integer isIsisPrj) {
    super();
    this.groupId = groupId;
    this.isGroupMemberView = isGroupMemberView;
    this.isDiscuss = isDiscuss;
    this.isShareFile = isShareFile;
    this.shareFileType = shareFileType;
    this.isPrjView = isPrjView;
    this.prjViewType = prjViewType;
    this.isPubView = isPubView;
    this.pubViewType = pubViewType;
    this.isRefView = isRefView;
    this.refViewType = refViewType;
    this.isWorkView = isWorkView;
    this.isMaterialView = isMaterialView;
    this.isMemberPublish = isMemberPublish;
    this.isPageOpen = isPageOpen;
    this.isPageDescOpen = isPageDescOpen;
    this.isPageMemberOpen = isPageMemberOpen;
    this.isPagePrjOpen = isPagePrjOpen;
    this.isPagePubOpen = isPagePubOpen;
    this.isPageRefOpen = isPageRefOpen;
    this.isPageFileOpen = isPageFileOpen;
    this.isPageWorkOpen = isPageWorkOpen;
    this.isPageMaterialOpen = isPageMaterialOpen;
    this.isSave = isSave;
    this.isIsisPrj = isIsisPrj;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public String getIsGroupMemberView() {
    return isGroupMemberView;
  }

  public void setIsGroupMemberView(String isGroupMemberView) {
    this.isGroupMemberView = isGroupMemberView;
  }

  public String getIsDiscuss() {
    return isDiscuss;
  }

  public void setIsDiscuss(String isDiscuss) {
    this.isDiscuss = isDiscuss;
  }

  public String getIsShareFile() {
    return isShareFile;
  }

  public void setIsShareFile(String isShareFile) {
    this.isShareFile = isShareFile;
  }

  public String getShareFileType() {
    return shareFileType;
  }

  public void setShareFileType(String shareFileType) {
    this.shareFileType = shareFileType;
  }

  public String getIsPrjView() {
    return isPrjView;
  }

  public void setIsPrjView(String isPrjView) {
    this.isPrjView = isPrjView;
  }

  public String getPrjViewType() {
    return prjViewType;
  }

  public void setPrjViewType(String prjViewType) {
    this.prjViewType = prjViewType;
  }

  public String getIsPubView() {
    return isPubView;
  }

  public void setIsPubView(String isPubView) {
    this.isPubView = isPubView;
  }

  public String getPubViewType() {
    return pubViewType;
  }

  public void setPubViewType(String pubViewType) {
    this.pubViewType = pubViewType;
  }

  public String getIsRefView() {
    return isRefView;
  }

  public void setIsRefView(String isRefView) {
    this.isRefView = isRefView;
  }

  public String getRefViewType() {
    return refViewType;
  }

  public void setRefViewType(String refViewType) {
    this.refViewType = refViewType;
  }

  public String getIsWorkView() {
    return isWorkView;
  }

  public void setIsWorkView(String isWorkView) {
    this.isWorkView = isWorkView;
  }

  public String getIsMaterialView() {
    return isMaterialView;
  }

  public void setIsMaterialView(String isMaterialView) {
    this.isMaterialView = isMaterialView;
  }

  public String getIsMemberPublish() {
    return isMemberPublish;
  }

  public void setIsMemberPublish(String isMemberPublish) {
    this.isMemberPublish = isMemberPublish;
  }

  public String getIsPageOpen() {
    return isPageOpen;
  }

  public void setIsPageOpen(String isPageOpen) {
    this.isPageOpen = isPageOpen;
  }

  public String getIsPageDescOpen() {
    return isPageDescOpen;
  }

  public void setIsPageDescOpen(String isPageDescOpen) {
    this.isPageDescOpen = isPageDescOpen;
  }

  public String getIsPageMemberOpen() {
    return isPageMemberOpen;
  }

  public void setIsPageMemberOpen(String isPageMemberOpen) {
    this.isPageMemberOpen = isPageMemberOpen;
  }

  public String getIsPagePrjOpen() {
    return isPagePrjOpen;
  }

  public void setIsPagePrjOpen(String isPagePrjOpen) {
    this.isPagePrjOpen = isPagePrjOpen;
  }

  public String getIsPagePubOpen() {
    return isPagePubOpen;
  }

  public void setIsPagePubOpen(String isPagePubOpen) {
    this.isPagePubOpen = isPagePubOpen;
  }

  public String getIsPageRefOpen() {
    return isPageRefOpen;
  }

  public void setIsPageRefOpen(String isPageRefOpen) {
    this.isPageRefOpen = isPageRefOpen;
  }

  public String getIsPageFileOpen() {
    return isPageFileOpen;
  }

  public void setIsPageFileOpen(String isPageFileOpen) {
    this.isPageFileOpen = isPageFileOpen;
  }

  public String getIsPageWorkOpen() {
    return isPageWorkOpen;
  }

  public void setIsPageWorkOpen(String isPageWorkOpen) {
    this.isPageWorkOpen = isPageWorkOpen;
  }

  public String getIsPageMaterialOpen() {
    return isPageMaterialOpen;
  }

  public void setIsPageMaterialOpen(String isPageMaterialOpen) {
    this.isPageMaterialOpen = isPageMaterialOpen;
  }

  public String getIsSave() {
    return isSave;
  }

  public void setIsSave(String isSave) {
    this.isSave = isSave;
  }

  public Integer getIsIsisPrj() {
    return isIsisPrj;
  }

  public void setIsIsisPrj(Integer isIsisPrj) {
    this.isIsisPrj = isIsisPrj;
  }
}
