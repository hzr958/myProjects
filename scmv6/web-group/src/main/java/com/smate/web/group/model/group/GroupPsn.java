package com.smate.web.group.model.group;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.annotations.GenericGenerator;

import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 群组实体类.
 * 
 * @author tsz
 * 
 */
@Entity
@Table(name = "GROUP_PSN")
public class GroupPsn implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3637477081385726190L;
  // 群组ID
  private Long groupId;
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
  // ==================================================

  // =====================英文关键字===================
  // 英文关键字1
  private String enKeyWords1;
  private String enKeyWords;
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
  // 是否支持文件共享[1=是,0=否]（zzx以前默认是0，群组改造后，没有设置，修改默认为1）
  private String isShareFile = "1";
  // 文件共享权限类型[1=所有成员,0=管理员]
  private String shareFileType = "1";
  // 是否支持群组项目[1=是,0=否]
  private String isPrjView = "0";
  // 群组项目权限类型[1=所有成员,0=管理员]
  private String prjViewType = "1";
  // 是否支持群组成果[1=是,0=否]（zzx以前默认是0，群组改造后，没有设置，修改默认为1）
  private String isPubView = "1";
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

  private Integer isIsisPrj;
  // 群组最后一次访问日期
  private Date lastVisitDate;
  // 群组访问统计
  private Long visitCount;
  // 科研群组-资助类别SCM-3666
  private String fundingTypes;
  // 群组编号
  private Long groupNo;
  // 群组认证码
  private String groupCode;


  public GroupPsn() {

  }

  public GroupPsn(String groupName, Long groupId, Integer sumMembers) {
    this.groupName = groupName;
  }

  public GroupPsn(String groupName, String groupImgUrl) {
    this.groupName = groupName;
    this.groupImgUrl = groupImgUrl;
  }

  public GroupPsn(String groupName, String groupImgUrl, String groupCategory, String openType, String isPubView,
      String isRefView, String isShareFile) {
    this.groupName = groupName;
    this.groupImgUrl = groupImgUrl;
    this.groupCategory = groupCategory;
    this.openType = openType;
    this.isPubView = isPubView;
    this.isRefView = isRefView;
    this.isShareFile = isShareFile;
  }

  @Id
  @Column(name = "GROUP_ID")
  @GenericGenerator(name = "SEQ_STORE", strategy = "assigned")
  @GeneratedValue(generator = "SEQ_STORE")
  public Long getGroupId() {
    if (groupId == null) {
      if (StringUtils.isNotBlank(this.des3GroupId))
        this.groupId = NumberUtils.toLong(ServiceUtil.decodeFromDes3(this.des3GroupId));
    } else {

    }
    return groupId;
  }

  @Column(name = "GROUP_NAME")
  public String getGroupName() {
    return groupName;
  }

  @Column(name = "GROUP_DESCRIPTION")
  public String getGroupDescription() {
    return groupDescription;
  }

  @Column(name = "GROUP_CATEGORY")
  public String getGroupCategory() {
    return groupCategory;
  }

  @Column(name = "DISCIPLINES")
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

  @Column(name = "GROUP_ANNOUNCEMENT")
  public String getGroupAnnouncement() {
    return groupAnnouncement;
  }

  @Column(name = "EMAIL")
  public String getEmail() {
    return email;
  }

  @Column(name = "TEL")
  public String getTel() {
    return tel;
  }

  @Column(name = "ADDRESS")
  public String getAddress() {
    return address;
  }

  @Column(name = "IS_GROUP_MEMBER_VIEW")
  public String getIsGroupMemberView() {
    return isGroupMemberView;
  }

  @Column(name = "IS_DISCUSS")
  public String getIsDiscuss() {
    return isDiscuss;
  }

  @Column(name = "IS_SHARE_FILE")
  public String getIsShareFile() {
    return isShareFile;
  }

  @Column(name = "SHARE_FILE_TYPE")
  public String getShareFileType() {
    return shareFileType;
  }

  @Column(name = "IS_PRJ_VIEW")
  public String getIsPrjView() {
    return isPrjView;
  }

  @Column(name = "PRJ_VIEW_TYPE")
  public String getPrjViewType() {
    return prjViewType;
  }

  @Column(name = "IS_PUB_VIEW")
  public String getIsPubView() {
    return isPubView;
  }

  @Column(name = "PUB_VIEW_TYPE")
  public String getPubViewType() {
    return pubViewType;
  }

  @Column(name = "IS_REF_VIEW")
  public String getIsRefView() {
    return isRefView;
  }

  @Column(name = "REF_VIEW_TYPE")
  public String getRefViewType() {
    return refViewType;
  }

  @Column(name = "OPEN_TYPE")
  public String getOpenType() {
    return openType;
  }

  @Column(name = "IS_MEMBER_PUBLISH")
  public String getIsMemberPublish() {
    return isMemberPublish;
  }

  @Column(name = "FILE_ID")
  public Long getFileId() {
    return fileId;
  }

  @Column(name = "GROUP_IMG_URL")
  public String getGroupImgUrl() {
    return groupImgUrl;
  }

  @Column(name = "GROUP_PAGE_URL")
  public String getGroupPageUrl() {
    return groupPageUrl;
  }

  @Column(name = "IS_PAGE_OPEN")
  public String getIsPageOpen() {
    return isPageOpen;
  }

  @Column(name = "IS_PAGE_DESC_OPEN")
  public String getIsPageDescOpen() {
    return isPageDescOpen;
  }

  @Column(name = "IS_PAGE_MEMBER_OPEN")
  public String getIsPageMemberOpen() {
    return isPageMemberOpen;
  }

  @Column(name = "IS_PAGE_PRJ_OPEN")
  public String getIsPagePrjOpen() {
    return isPagePrjOpen;
  }

  @Column(name = "IS_PAGE_PUB_OPEN")
  public String getIsPagePubOpen() {
    return isPagePubOpen;
  }

  @Column(name = "IS_PAGE_REF_OPEN")
  public String getIsPageRefOpen() {
    return isPageRefOpen;
  }

  @Column(name = "IS_PAGE_FILE_OPEN")
  public String getIsPageFileOpen() {
    return isPageFileOpen;
  }

  @Column(name = "SUM_MEMBERS")
  public Integer getSumMembers() {
    return sumMembers;
  }

  @Column(name = "SUM_SUBJECTS")
  public Integer getSumSubjects() {
    return sumSubjects;
  }

  @Column(name = "SUM_PUBS")
  public Integer getSumPubs() {
    return sumPubs;
  }

  @Column(name = "SUM_PRJS")
  public Integer getSumPrjs() {
    return sumPrjs;
  }

  @Column(name = "SUM_REFS")
  public Integer getSumRefs() {
    return sumRefs;
  }

  @Column(name = "SUM_FILES")
  public Integer getSumFiles() {
    return sumFiles;
  }

  @Column(name = "OWNER_PSN_ID")
  public Long getOwnerPsnId() {
    return ownerPsnId;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  @Column(name = "STATUS")
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



  @Column(name = "DISCIPLINE_1")
  public Long getDiscipline1() {
    return discipline1;
  }

  public void setDiscipline1(Long discipline1) {
    this.discipline1 = discipline1;
  }


  @Column(name = "SUM_PUBS_NFOLDER")
  public Integer getSumPubsNfolder() {
    return sumPubsNfolder;
  }

  @Column(name = "SUM_PRJS_NFOLDER")
  public Integer getSumPrjsNfolder() {
    return sumPrjsNfolder;
  }

  @Column(name = "SUM_REFS_NFOLDER")
  public Integer getSumRefsNfolder() {
    return sumRefsNfolder;
  }

  @Column(name = "SUM_FILES_NFOLDER")
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

  @Column(name = "SUM_TO_MEMBERS")
  public Integer getSumToMembers() {
    return sumToMembers;
  }

  @Column(name = "OLD_GROUP_ID")
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

  @Column(name = "DISC_CODES")
  public String getDiscCodes() {
    return discCodes;
  }

  public void setDiscCodes(String discCodes) {
    this.discCodes = discCodes;
  }

  @Column(name = "IS_ISIS_PRJ")
  public Integer getIsIsisPrj() {
    return isIsisPrj;
  }

  public void setIsIsisPrj(Integer isIsisPrj) {
    this.isIsisPrj = isIsisPrj;
  }

  @Column(name = "KEY_WORDS_1")
  public String getKeyWords1() {
    return keyWords1;
  }

  @Column(name = "KEY_WORDS")
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

  @Column(name = "EN_KEY_WORDS_1")
  public String getEnKeyWords1() {
    return enKeyWords1;
  }

  @Column(name = "EN_KEY_WORDS")
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

  public void setEnKeyWords1(String enKeyWords1) {
    this.enKeyWords1 = enKeyWords1;
  }

  public void setEnKeyWords(String enKeyWords) {
    this.enKeyWords = enKeyWords;
  }


  @Column(name = "IS_WORK_VIEW")
  public String getIsWorkView() {
    return isWorkView;
  }

  @Column(name = "IS_MATERIAL_VIEW")
  public String getIsMaterialView() {
    return isMaterialView;
  }

  @Column(name = "IS_PAGE_WORK_OPEN")
  public String getIsPageWorkOpen() {
    return isPageWorkOpen;
  }

  @Column(name = "IS_PAGE_MATERIAL_OPEN")
  public String getIsPageMaterialOpen() {
    return isPageMaterialOpen;
  }

  @Column(name = "SUM_WORKS")
  public Integer getSumWorks() {
    return sumWorks;
  }

  @Column(name = "SUM_MATERIALS")
  public Integer getSumMaterials() {
    return sumMaterials;
  }

  @Column(name = "SUM_WORKS_NFOLDER")
  public Integer getSumWorksNfolder() {
    return sumWorksNfolder;
  }

  @Column(name = "SUM_MATERIALS_NFOLDER")
  public Integer getSumMaterialsNfolder() {
    return sumMaterialsNfolder;
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



  @Column(name = "LAST_VISIT_DATE")
  public Date getLastVisitDate() {
    return lastVisitDate;
  }

  public void setLastVisitDate(Date lastVisitDate) {
    this.lastVisitDate = lastVisitDate;
  }

  @Column(name = "VISIT_COUNT")
  public Long getVisitCount() {
    return visitCount;
  }

  public void setVisitCount(Long visitCount) {
    this.visitCount = visitCount;
  }

  @Column(name = "FUNDING_TYPES")
  public String getFundingTypes() {
    return fundingTypes;
  }

  public void setFundingTypes(String fundingTypes) {
    this.fundingTypes = fundingTypes;
  }

  @Column(name = "GROUP_NO")
  public Long getGroupNo() {
    return groupNo;
  }

  public void setGroupNo(Long groupNo) {
    this.groupNo = groupNo;
  }

  @Column(name = "GROUP_CODE")
  public String getGroupCode() {
    return groupCode;
  }

  public void setGroupCode(String groupCode) {
    this.groupCode = groupCode;
  }

  public String getDes3GroupId() {

    return des3GroupId;
  }

  public void setDes3GroupId(String des3GroupId) {
    this.des3GroupId = des3GroupId;
  }



}
