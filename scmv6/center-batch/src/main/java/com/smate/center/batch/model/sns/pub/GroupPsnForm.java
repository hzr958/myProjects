package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.smate.center.batch.model.sns.prj.GroupInvitePsn;
import com.smate.center.batch.model.sns.prj.GroupMember;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 群组Form.
 * 
 * 
 * 
 */

public class GroupPsnForm implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 2515281157842104617L;
  // 群组ID
  private Long groupId;
  // 加密群组ID
  private String des3GroupId;
  // 群组名称
  private String groupName;
  // 群组介绍
  private String groupDescription;
  // 群组分类
  private String groupCategory;
  // 学科领域
  private String disciplines;
  // 学科代码1
  private Long discipline1;

  // =====================中文关键字===================
  // 关键字1
  private String keyWords1;

  private String keyWords;

  private List<String[]> kewWordsList;
  // ==================================================

  // =====================英文关键字===================
  // 英文关键字1
  private String enKeyWords1;

  private String enKeyWords;
  // 英文关键字集
  private List<String[]> enKeyWordsList;
  // ==================================================

  // 群组公告
  private String groupAnnouncement;
  // Email
  private String email;
  // 联系电话
  private String tel;
  // 联系地址
  private String address;
  // 项目组成员是否可以查看其他群组成员的科研项目和成果[1=是,0=否]
  private String isGroupMemberView = "1";
  // 是否支持讨论板[1=是,0=否]
  private String isDiscuss = "0";
  // 是否支持文件共享[1=是,0=否]
  private String isShareFile = "0";
  // 文件共享权限类型[1=所有成员,0=管理员]
  private String shareFileType = "1";
  // 是否支持群组项目[1=是,0=否]
  private String isPrjView = "0";
  // 群组项目权限类型[1=所有成员,0=管理员]
  private String prjViewType = "1";
  // 是否支持群组成果[1=是,0=否]
  private String isPubView = "0";
  // 群组成果权限类型[1=所有成员,0=管理员]
  private String pubViewType = "1";
  // 是否支持群组参考文献[1=是,0=否]
  private String isRefView = "0";
  // 参考文献权限类型[1=所有成员,0=管理员]
  private String refViewType = "1";

  // 是否支持作业[1=是,0=否]
  private String isWorkView = "0";
  // 作业权限类型[1=所有成员,0=管理员]
  // private String workViewType = "0";
  // 是否支持教学课件[1=是,0=否]
  private String isMaterialView = "0";
  // 作业权限类型[1=所有成员,0=管理员]
  // private String materialViewType = "0";

  // 公开类型[O=开放,H=半开放,P=保密]
  private String openType = "H";
  // 是否允许普通成员发表话题[1=是,0=否]
  private String isMemberPublish = "1";
  // 图片文件编号
  private Long fileId;
  // 群组图片
  private String groupImgUrl;
  // 群组主页地址
  private String groupPageUrl;
  // 群组主页是否开放[1=是,0=否]
  private String isPageOpen = "1";
  // 主页是否显示群组介绍[1=是,0=否]
  private String isPageDescOpen = "1";
  // 主页是否显示群组成员[1=是,0=否]
  private String isPageMemberOpen = "1";
  // 主页是否显示科研项目[1=是,0=否]
  private String isPagePrjOpen = "1";
  // 主页是否显示科研成果[1=是,0=否]
  private String isPagePubOpen = "1";
  // 主页是否显示科研文献[1=是,0=否]
  private String isPageRefOpen = "1";
  // 主页是否显示文件[1=是,0=否]
  private String isPageFileOpen = "1";

  // 主页是否显示作业[1=是,0=否]
  private String isPageWorkOpen = "0";
  // 主页是否显示教学课件[1=是,0=否]
  private String isPageMaterialOpen = "0";

  // 群组成员数
  private Integer sumMembers = 0;
  // 待确认群组成员数
  private Integer sumToMembers = 0;
  // 群组话题数
  private Integer sumSubjects = 0;
  // 群组成果数
  private Integer sumPubs = 0;
  // 群组项目数
  private Integer sumPrjs = 0;
  // 群组文献数
  private Integer sumRefs = 0;
  // 群组文件数
  private Integer sumFiles = 0;

  // 群组作业数
  private Integer sumWorks = 0;
  // 群组教学课件数
  private Integer sumMaterials = 0;

  // 群组拥有者的psn_id
  private Long ownerPsnId;
  // 创建时间
  private Date createDate;
  // 更新时间
  private Date updateDate;
  // 群组状态[01=正常,99=删除]
  private String status = "01";

  // 群组介绍截取
  private String groupDescriptionSub;

  // 群组介绍超长字段
  private String groupDescriptionClob;

  // 学科代码JSON
  private String disJSON;

  // 群组与人员的邀请关系表
  private List<GroupInvitePsn> groupInvitePsnList = new ArrayList<GroupInvitePsn>();

  private GroupInvitePsn groupInvitePsn;

  // 跳转action
  private String navAction;
  // 是否已经进行保存
  private String isSave;

  // 群组所在节点
  private Integer groupNodeId;
  // 群组加密NodeId
  private String des3GroupNodeId;

  // 群组好友
  private List<GroupMember> groupMemberFriends;
  // 群组好友名称
  private String memberFriends;
  // 群组名称（用于群组页面标题）
  private String navGroupName;

  // 当前登录人的群组角色
  private String currentGroupRole;

  // 群组成果数(文件夹未分类)
  private Integer sumPubsNfolder = 0;
  // 群组项目数(文件夹未分类)
  private Integer sumPrjsNfolder = 0;
  // 群组文献数(文件夹未分类)
  private Integer sumRefsNfolder = 0;
  // 群组文件数(文件夹未分类)
  private Integer sumFilesNfolder = 0;

  // 群组作业数(文件夹未分类)
  private Integer sumWorksNfolder = 0;
  // 群组教学课件数(文件夹未分类)
  private Integer sumMaterialsNfolder = 0;

  // 从V2.6导入的群组ID
  private Long oldGroupId;

  // 学到代码
  private String discCodes;
  // 加入群组的方式:被邀请加入群组：invited;申请加入群组：apply
  private String joinInGroupStyle;

  // 操作是否同意加入组的操作者Id
  private Long exeIsJoinInGroupPsnId;

  // webservice 创建群组时，传递的guid
  private String isisGuid;

  private Integer isIsisPrj;

  private String tempPageUrl;// 群组主页拼装的地址动态部分.

  private String groupUrl;// 群组的菜单链接地址_MJG_2012-12-05.
  private Map<String, List<KeywordsHot>> rdKeywords;// 群组推荐关键词_MJG_SCM-3206.
  // 科研群组-资助类别SCM-3666
  private String fundingTypes;
  // 群组编号
  private Long groupNo;
  // 群组认证码
  private String groupCode;

  public GroupPsnForm() {

  }

  public Long getGroupId() {
    return groupId;
  }

  public String getGroupName() {
    return groupName;
  }

  public String getGroupDescription() {
    return groupDescription;
  }

  public String getGroupCategory() {
    return groupCategory;
  }

  public String getDisciplines() {
    List<Long> tempList = new ArrayList<Long>();
    if (discipline1 != null) {
      tempList.add(discipline1);
    }
    if (tempList.size() > 0) {
      disciplines = "," + StringUtils.join(tempList, ",") + ",";
    }
    return disciplines;
  }

  public String getGroupAnnouncement() {
    return groupAnnouncement;
  }

  public String getEmail() {
    return email;
  }

  public String getTel() {
    return tel;
  }

  public String getAddress() {
    return address;
  }

  public String getIsGroupMemberView() {
    return isGroupMemberView;
  }

  public String getIsDiscuss() {
    return isDiscuss;
  }

  public String getIsShareFile() {
    return isShareFile;
  }

  public String getShareFileType() {
    return shareFileType;
  }

  public String getIsPrjView() {
    return isPrjView;
  }

  public String getPrjViewType() {
    return prjViewType;
  }

  public String getIsPubView() {
    return isPubView;
  }

  public String getPubViewType() {
    return pubViewType;
  }

  public String getIsRefView() {
    return isRefView;
  }

  public String getRefViewType() {
    return refViewType;
  }

  public String getOpenType() {
    return openType;
  }

  public String getIsMemberPublish() {
    return isMemberPublish;
  }

  public Long getFileId() {
    return fileId;
  }

  public String getGroupImgUrl() {
    return groupImgUrl;
  }

  public String getGroupPageUrl() {
    return groupPageUrl;
  }

  public String getIsPageOpen() {
    return isPageOpen;
  }

  public String getIsPageDescOpen() {
    return isPageDescOpen;
  }

  public String getIsPageMemberOpen() {
    return isPageMemberOpen;
  }

  public String getIsPagePrjOpen() {
    return isPagePrjOpen;
  }

  public String getIsPagePubOpen() {
    return isPagePubOpen;
  }

  public String getIsPageRefOpen() {
    return isPageRefOpen;
  }

  public String getIsPageFileOpen() {
    return isPageFileOpen;
  }

  public Integer getSumMembers() {
    return sumMembers;
  }

  public Integer getSumSubjects() {
    return sumSubjects;
  }

  public Integer getSumPubs() {
    return sumPubs;
  }

  public Integer getSumPrjs() {
    return sumPrjs;
  }

  public Integer getSumRefs() {
    return sumRefs;
  }

  public Integer getSumFiles() {
    return sumFiles;
  }

  public Long getOwnerPsnId() {
    return ownerPsnId;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public String getStatus() {
    return status;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public void setGroupDescription(String groupDescription) {
    this.groupDescription = groupDescription;
  }

  public void setGroupCategory(String groupCategory) {
    this.groupCategory = groupCategory;
  }

  public void setDisciplines(String disciplines) {
    this.disciplines = disciplines;
  }

  public void setGroupAnnouncement(String groupAnnouncement) {
    this.groupAnnouncement = groupAnnouncement;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public void setIsGroupMemberView(String isGroupMemberView) {
    this.isGroupMemberView = isGroupMemberView;
  }

  public void setIsDiscuss(String isDiscuss) {
    this.isDiscuss = isDiscuss;
  }

  public void setIsShareFile(String isShareFile) {
    this.isShareFile = isShareFile;
  }

  public void setShareFileType(String shareFileType) {
    this.shareFileType = shareFileType;
  }

  public void setIsPrjView(String isPrjView) {
    this.isPrjView = isPrjView;
  }

  public void setPrjViewType(String prjViewType) {
    this.prjViewType = prjViewType;
  }

  public void setIsPubView(String isPubView) {
    this.isPubView = isPubView;
  }

  public void setPubViewType(String pubViewType) {
    this.pubViewType = pubViewType;
  }

  public void setIsRefView(String isRefView) {
    this.isRefView = isRefView;
  }

  public void setRefViewType(String refViewType) {
    this.refViewType = refViewType;
  }

  public void setOpenType(String openType) {
    this.openType = openType;
  }

  public void setIsMemberPublish(String isMemberPublish) {
    this.isMemberPublish = isMemberPublish;
  }

  public void setFileId(Long fileId) {
    this.fileId = fileId;
  }

  public void setGroupImgUrl(String groupImgUrl) {
    this.groupImgUrl = groupImgUrl;
  }

  public void setGroupPageUrl(String groupPageUrl) {
    this.groupPageUrl = groupPageUrl;
  }

  public void setIsPageOpen(String isPageOpen) {
    this.isPageOpen = isPageOpen;
  }

  public void setIsPageDescOpen(String isPageDescOpen) {
    this.isPageDescOpen = isPageDescOpen;
  }

  public void setIsPageMemberOpen(String isPageMemberOpen) {
    this.isPageMemberOpen = isPageMemberOpen;
  }

  public void setIsPagePrjOpen(String isPagePrjOpen) {
    this.isPagePrjOpen = isPagePrjOpen;
  }

  public void setIsPagePubOpen(String isPagePubOpen) {
    this.isPagePubOpen = isPagePubOpen;
  }

  public void setIsPageRefOpen(String isPageRefOpen) {
    this.isPageRefOpen = isPageRefOpen;
  }

  public void setIsPageFileOpen(String isPageFileOpen) {
    this.isPageFileOpen = isPageFileOpen;
  }

  public void setSumMembers(Integer sumMembers) {
    this.sumMembers = sumMembers;
  }

  public void setSumSubjects(Integer sumSubjects) {
    this.sumSubjects = sumSubjects;
  }

  public void setSumPubs(Integer sumPubs) {
    this.sumPubs = sumPubs;
  }

  public void setSumPrjs(Integer sumPrjs) {
    this.sumPrjs = sumPrjs;
  }

  public void setSumRefs(Integer sumRefs) {
    this.sumRefs = sumRefs;
  }

  public void setSumFiles(Integer sumFiles) {
    this.sumFiles = sumFiles;
  }

  public void setOwnerPsnId(Long ownerPsnId) {
    this.ownerPsnId = ownerPsnId;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getGroupDescriptionSub() {

    return groupDescriptionSub;
  }

  public void setGroupDescriptionSub(String groupDescriptionSub) {
    this.groupDescriptionSub = groupDescriptionSub;
  }

  public String getDisJSON() {
    return disJSON;
  }

  public void setDisJSON(String disJSON) {
    this.disJSON = disJSON;
  }

  public Long getDiscipline1() {
    return discipline1;
  }

  public void setDiscipline1(Long discipline1) {
    this.discipline1 = discipline1;
  }

  //
  //

  public List<GroupInvitePsn> getGroupInvitePsnList() {
    return groupInvitePsnList;
  }

  public void setGroupInvitePsnList(List<GroupInvitePsn> groupInvitePsnList) {
    this.groupInvitePsnList = groupInvitePsnList;
  }

  public String getDes3GroupId() {
    if (this.groupId != null && des3GroupId == null) {
      des3GroupId = ServiceUtil.encodeToDes3(this.groupId.toString());
    }
    return des3GroupId;
  }

  public String getDes3GroupNodeId() {
    if (this.groupNodeId != null && des3GroupNodeId == null) {
      des3GroupNodeId = ServiceUtil.encodeToDes3(this.groupNodeId.toString());
    }
    return des3GroupNodeId;
  }

  public void setDes3GroupNodeId(String des3GroupNodeId) {
    if (this.groupNodeId == null && StringUtils.isNotBlank(des3GroupNodeId)) {
      this.groupNodeId = Integer.valueOf(ServiceUtil.decodeFromDes3(des3GroupNodeId));
    }
    this.des3GroupNodeId = des3GroupNodeId;
  }

  public void setDes3GroupId(String des3GroupId) {
    if (this.groupId == null && StringUtils.isNotBlank(des3GroupId)) {
      this.groupId = Long.valueOf(ServiceUtil.decodeFromDes3(des3GroupId));
    }
    this.des3GroupId = des3GroupId;
  }

  public String getNavAction() {
    return navAction;
  }

  public void setNavAction(String navAction) {
    this.navAction = navAction;
  }

  public Integer getGroupNodeId() {
    return groupNodeId;
  }

  public void setGroupNodeId(Integer groupNodeId) {
    this.groupNodeId = groupNodeId;
  }

  public String getIsSave() {
    return isSave;
  }

  public void setIsSave(String isSave) {
    this.isSave = isSave;
  }

  public GroupInvitePsn getGroupInvitePsn() {
    return groupInvitePsn;
  }

  public void setGroupInvitePsn(GroupInvitePsn groupInvitePsn) {
    this.groupInvitePsn = groupInvitePsn;
  }

  public List<GroupMember> getGroupMemberFriends() {
    return groupMemberFriends;
  }

  public void setGroupMemberFriends(List<GroupMember> groupMemberFriends) {
    this.groupMemberFriends = groupMemberFriends;
  }

  public String getMemberFriends() {
    return memberFriends;
  }

  public void setMemberFriends(String memberFriends) {
    this.memberFriends = memberFriends;
  }

  public String getNavGroupName() {
    if (StringUtils.isBlank(navGroupName)) {
      navGroupName = this.groupName;
    }
    return navGroupName;
  }

  public void setNavGroupName(String navGroupName) {
    this.navGroupName = navGroupName;
  }

  public String getCurrentGroupRole() {
    return currentGroupRole;
  }

  public void setCurrentGroupRole(String currentGroupRole) {
    this.currentGroupRole = currentGroupRole;
  }

  public Integer getSumPubsNfolder() {
    return sumPubsNfolder;
  }

  public Integer getSumPrjsNfolder() {
    return sumPrjsNfolder;
  }

  public Integer getSumRefsNfolder() {
    return sumRefsNfolder;
  }

  public Integer getSumFilesNfolder() {
    return sumFilesNfolder;
  }

  public void setSumPubsNfolder(Integer sumPubsNfolder) {
    this.sumPubsNfolder = sumPubsNfolder;
  }

  public void setSumPrjsNfolder(Integer sumPrjsNfolder) {
    this.sumPrjsNfolder = sumPrjsNfolder;
  }

  public void setSumRefsNfolder(Integer sumRefsNfolder) {
    this.sumRefsNfolder = sumRefsNfolder;
  }

  public void setSumFilesNfolder(Integer sumFilesNfolder) {
    this.sumFilesNfolder = sumFilesNfolder;
  }

  public Integer getSumToMembers() {
    return sumToMembers;
  }

  public Long getOldGroupId() {
    return oldGroupId;
  }

  public void setOldGroupId(Long oldGroupId) {
    this.oldGroupId = oldGroupId;
  }

  public void setSumToMembers(Integer sumToMembers) {
    this.sumToMembers = sumToMembers;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  public String getDiscCodes() {
    return discCodes;
  }

  public void setDiscCodes(String discCodes) {
    this.discCodes = discCodes;
  }

  public Integer getIsIsisPrj() {
    return isIsisPrj;
  }

  public void setIsIsisPrj(Integer isIsisPrj) {
    this.isIsisPrj = isIsisPrj;
  }

  public String getKeyWords1() {
    return keyWords1;
  }

  public String getKeyWords() {
    List<String> tempList = new ArrayList<String>();
    if (StringUtils.isNotEmpty(groupName)) {
      tempList.add(groupName);
    }
    if (StringUtils.isNotEmpty(keyWords1)) {
      tempList.add(keyWords1);
    }
    if (tempList.size() > 0) {
      keyWords = ";" + StringUtils.join(tempList, ";") + ";";
    }
    return keyWords;
  }

  public void setKeyWords1(String keyWords1) {
    this.keyWords1 = keyWords1;
  }

  public void setKeyWords(String keyWords) {
    this.keyWords = keyWords;
  }

  public List<String[]> getKewWordsList() {

    if (kewWordsList == null) {
      kewWordsList = new ArrayList<String[]>();
      kewWordsList.add(StringUtils.isBlank(keyWords1) ? null : ObjectUtils.toString(keyWords1).split(";"));
    }
    return kewWordsList;
  }

  public String getEnKeyWords1() {
    return enKeyWords1;
  }

  public String getEnKeyWords() {
    List<String> tempList = new ArrayList<String>();
    if (StringUtils.isNotEmpty(groupName)) {
      tempList.add(groupName);
    }
    if (StringUtils.isNotEmpty(enKeyWords1)) {
      tempList.add(enKeyWords1);
    }

    if (tempList.size() > 0) {
      enKeyWords = ";" + StringUtils.join(tempList, ";") + ";";
    }
    return enKeyWords;
  }

  public List<String[]> getEnKeyWordsList() {
    if (enKeyWordsList == null) {
      enKeyWordsList = new ArrayList<String[]>();
      enKeyWordsList.add(StringUtils.isBlank(enKeyWords1) ? null : ObjectUtils.toString(enKeyWords1).split(";"));
    }
    return enKeyWordsList;
  }

  public void setEnKeyWords1(String enKeyWords1) {
    this.enKeyWords1 = enKeyWords1;
  }

  public void setEnKeyWords(String enKeyWords) {
    this.enKeyWords = enKeyWords;
  }

  public void setEnKeyWordsList(List<String[]> enKeyWordsList) {
    this.enKeyWordsList = enKeyWordsList;
  }

  public String getJoinInGroupStyle() {
    return joinInGroupStyle;
  }

  public void setJoinInGroupStyle(String joinInGroupStyle) {
    this.joinInGroupStyle = joinInGroupStyle;
  }

  public void setKewWordsList(List<String[]> kewWordsList) {
    this.kewWordsList = kewWordsList;
  }

  public Long getExeIsJoinInGroupPsnId() {
    return exeIsJoinInGroupPsnId;
  }

  public void setExeIsJoinInGroupPsnId(Long exeIsJoinInGroupPsnId) {
    this.exeIsJoinInGroupPsnId = exeIsJoinInGroupPsnId;
  }

  public String getIsisGuid() {
    return isisGuid;
  }

  public void setIsisGuid(String isisGuid) {
    this.isisGuid = isisGuid;
  }

  public String getIsWorkView() {
    return isWorkView;
  }

  public String getIsMaterialView() {
    return isMaterialView;
  }

  public String getIsPageWorkOpen() {
    return isPageWorkOpen;
  }

  public String getIsPageMaterialOpen() {
    return isPageMaterialOpen;
  }

  public Integer getSumWorks() {
    return sumWorks;
  }

  public Integer getSumMaterials() {
    return sumMaterials;
  }

  public Integer getSumWorksNfolder() {
    return sumWorksNfolder;
  }

  public Integer getSumMaterialsNfolder() {
    return sumMaterialsNfolder;
  }

  public String getTempPageUrl() {
    try {
      tempPageUrl = URLEncoder.encode(ServiceUtil.encodeToDes3(String.valueOf(groupId)), "utf-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return tempPageUrl;
  }

  public void setIsWorkView(String isWorkView) {
    this.isWorkView = isWorkView;
  }

  public void setIsMaterialView(String isMaterialView) {
    this.isMaterialView = isMaterialView;
  }

  public void setIsPageWorkOpen(String isPageWorkOpen) {
    this.isPageWorkOpen = isPageWorkOpen;
  }

  public void setIsPageMaterialOpen(String isPageMaterialOpen) {
    this.isPageMaterialOpen = isPageMaterialOpen;
  }

  public void setSumWorks(Integer sumWorks) {
    this.sumWorks = sumWorks;
  }

  public void setSumMaterials(Integer sumMaterials) {
    this.sumMaterials = sumMaterials;
  }

  public void setSumWorksNfolder(Integer sumWorksNfolder) {
    this.sumWorksNfolder = sumWorksNfolder;
  }

  public void setSumMaterialsNfolder(Integer sumMaterialsNfolder) {
    this.sumMaterialsNfolder = sumMaterialsNfolder;
  }

  public void setTempPageUrl(String tempPageUrl) {
    if (tempPageUrl != null && !"".equals(tempPageUrl)) {
      this.tempPageUrl = tempPageUrl;
    } else {
      try {
        this.tempPageUrl = URLEncoder.encode(ServiceUtil.encodeToDes3(String.valueOf(groupId)), "utf-8");
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }
  }

  public String getGroupUrl() {
    return groupUrl;
  }

  public void setGroupUrl(String groupUrl) {
    this.groupUrl = groupUrl;
  }

  public String getGroupDescriptionClob() {
    return groupDescriptionClob;
  }

  public void setGroupDescriptionClob(String groupDescriptionClob) {
    this.groupDescriptionClob = groupDescriptionClob;
  }

  public Map<String, List<KeywordsHot>> getRdKeywords() {
    return rdKeywords;
  }

  public void setRdKeywords(Map<String, List<KeywordsHot>> rdKeywords) {
    this.rdKeywords = rdKeywords;
  }

  public String getFundingTypes() {
    return fundingTypes;
  }

  public void setFundingTypes(String fundingTypes) {
    this.fundingTypes = fundingTypes;
  }

  public Long getGroupNo() {
    return groupNo;
  }

  public void setGroupNo(Long groupNo) {
    this.groupNo = groupNo;
  }

  public String getGroupCode() {
    return groupCode;
  }

  public void setGroupCode(String groupCode) {
    this.groupCode = groupCode;
  }

}
