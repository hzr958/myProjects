package com.smate.center.open.model.group;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 群组基础信息表(页面查询显示用).
 * 
 * @author mjg
 * @since 2014-11-28
 */
@Entity
@Table(name = "GROUP_BASEINFO")
public class GroupBaseInfo implements Serializable {

  private static final long serialVersionUID = -3158400867514791560L;
  // 群组ID
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_GROUP_BASEINFO", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;
  @Column(name = "GROUP_ID")
  private Long groupId;
  // 群组编号
  @Column(name = "GROUP_NO")
  private Long groupNo;
  // 群组名称
  @Column(name = "GROUP_NAME")
  private String groupName;
  // 群组介绍
  @Column(name = "GROUP_DESCRIPTION")
  private String groupDescription;
  // 群组分类
  @Column(name = "GROUP_CATEGORY")
  private String groupCategory;
  // 群组公告
  @Column(name = "GROUP_ANNOUNCEMENT")
  private String groupAnnouncement;
  // Email
  @Column(name = "EMAIL")
  private String email;
  // 联系电话
  @Column(name = "TEL")
  private String tel;
  // 联系地址
  @Column(name = "ADDRESS")
  private String address;
  // 科研群组-资助类别SCM-3666
  @Column(name = "FUNDING_TYPES")
  private String fundingTypes;
  // 图片文件编号
  @Column(name = "FILE_ID")
  private Long fileId;
  // 群组图片
  @Column(name = "GROUP_IMG_URL")
  private String groupImgUrl;
  // 群组主页地址
  @Column(name = "GROUP_PAGE_URL")
  private String groupPageUrl;
  // 创建时间
  @Column(name = "CREATE_DATE")
  private Date createDate;
  // 更新时间
  @Column(name = "UPDATE_DATE")
  private Date updateDate;
  // 群组最后一次访问日期
  @Column(name = "LAST_VISIT_DATE")
  private Date lastVisitDate;

  // 如下为我的群组列表所需参数_MJG_SCM-6192.
  @Transient
  private Long psnId;
  @Transient
  private String isAccept;
  @Transient
  private String groupRole;
  @Transient
  private Integer sumToMembers;
  @Transient
  private String categoryName;
  @Transient
  private int nodeId;
  @Transient
  private String des3GroupId;

  // 如下为检索群组列表所需参数_MJG_SCM-6192.
  @Transient
  private String disciplines;
  @Transient
  private String openType;
  @Transient
  private String discCodes;
  @Transient
  private String keyWords;
  @Transient
  private String isisGuid;
  @Transient
  private Integer sumMembers;
  @Transient
  private String groupNoOrder;

  public GroupBaseInfo() {
    super();
  }



  public GroupBaseInfo(Long groupId, String groupName, String groupImgUrl, Date createDate) {
    super();
    this.groupId = groupId;
    this.groupName = groupName;
    this.groupImgUrl = groupImgUrl;
    this.createDate = createDate;
  }

  public GroupBaseInfo(Long groupId, String groupName) {
    super();
    this.groupId = groupId;
    this.groupName = groupName;
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

  public Long getGroupNo() {
    return groupNo;
  }

  public void setGroupNo(Long groupNo) {
    this.groupNo = groupNo;
  }

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

  public String getGroupCategory() {
    return groupCategory;
  }

  public void setGroupCategory(String groupCategory) {
    this.groupCategory = groupCategory;
  }

  public String getGroupAnnouncement() {
    return groupAnnouncement;
  }

  public void setGroupAnnouncement(String groupAnnouncement) {
    this.groupAnnouncement = groupAnnouncement;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getTel() {
    return tel;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getFundingTypes() {
    return fundingTypes;
  }

  public void setFundingTypes(String fundingTypes) {
    this.fundingTypes = fundingTypes;
  }

  public Long getFileId() {
    return fileId;
  }

  public void setFileId(Long fileId) {
    this.fileId = fileId;
  }

  public String getGroupImgUrl() {
    return groupImgUrl;
  }

  public void setGroupImgUrl(String groupImgUrl) {
    this.groupImgUrl = groupImgUrl;
  }

  public String getGroupPageUrl() {
    return groupPageUrl;
  }

  public void setGroupPageUrl(String groupPageUrl) {
    this.groupPageUrl = groupPageUrl;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public Date getLastVisitDate() {
    return lastVisitDate;
  }

  public void setLastVisitDate(Date lastVisitDate) {
    this.lastVisitDate = lastVisitDate;
  }

  public String getIsAccept() {
    return isAccept;
  }

  public void setIsAccept(String isAccept) {
    this.isAccept = isAccept;
  }

  public String getGroupRole() {
    return groupRole;
  }

  public void setGroupRole(String groupRole) {
    this.groupRole = groupRole;
  }

  public Integer getSumToMembers() {
    return sumToMembers;
  }

  public void setSumToMembers(Integer sumToMembers) {
    this.sumToMembers = sumToMembers;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public int getNodeId() {
    return nodeId;
  }

  public void setNodeId(int nodeId) {
    this.nodeId = nodeId;
  }

  public String getDes3GroupId() {
    return des3GroupId;
  }

  public void setDes3GroupId(String des3GroupId) {
    this.des3GroupId = des3GroupId;
  }

  public String getDisciplines() {
    return disciplines;
  }

  public void setDisciplines(String disciplines) {
    this.disciplines = disciplines;
  }

  public String getOpenType() {
    return openType;
  }

  public void setOpenType(String openType) {
    this.openType = openType;
  }

  public String getDiscCodes() {
    return discCodes;
  }

  public void setDiscCodes(String discCodes) {
    this.discCodes = discCodes;
  }

  public String getKeyWords() {
    return keyWords;
  }

  public void setKeyWords(String keyWords) {
    this.keyWords = keyWords;
  }

  public String getIsisGuid() {
    return isisGuid;
  }

  public void setIsisGuid(String isisGuid) {
    this.isisGuid = isisGuid;
  }

  public Integer getSumMembers() {
    return sumMembers;
  }

  public void setSumMembers(Integer sumMembers) {
    this.sumMembers = sumMembers;
  }

  public String getGroupNoOrder() {
    return groupNoOrder;
  }

  public void setGroupNoOrder(String groupNoOrder) {
    this.groupNoOrder = groupNoOrder;
  }

}
