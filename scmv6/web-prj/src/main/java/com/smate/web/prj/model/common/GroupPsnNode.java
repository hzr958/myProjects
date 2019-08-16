package com.smate.web.prj.model.common;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.smate.core.base.utils.common.HtmlUtils;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 所有节点上的群组.
 * 
 * @author zhuangyanming
 * 
 */
@Entity
@Table(name = "GROUP_PSN_NODE")
public class GroupPsnNode implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5862222396519428543L;
  // 群组ID
  private Long groupId;

  // 群组名称
  private String groupName;
  // 加密群组ID
  private String des3GroupId;

  // 节点
  private Integer nodeId;

  // 学科领域
  private String disciplines;

  // 群组分类
  private String groupCategory;

  // 公开类型[O=开放,H=半开放,P=保密]
  private String openType = "H";

  // 学科领域代码
  private String discCodes;

  // 关键字(关键字1+关键字2+关键字3)
  private String keyWords;

  // webservice 创建群组时，传递的guid
  private String isisGuid;

  private int num;

  // 是否支持群组项目[1=是,0=否]
  private String isPrjView = "1";

  // 是否支持文件共享[1=是,0=否]
  private String isShareFile = "1";

  private String escapeGroupName;

  // 群组成员数
  private Integer sumMembers = 1;

  // 创建时间
  private Date createDate;
  private Integer count;

  // 群组中的角色[1=创建人,2=管理员, 3=组员][冗余]
  private String groupRole = "3";

  public GroupPsnNode() {
    super();
    // TODO Auto-generated constructor stub
  }

  public GroupPsnNode(Long groupId, String groupName, Integer nodeId) {
    super();
    this.groupId = groupId;
    this.groupName = groupName;
    this.nodeId = nodeId;
  }

  public GroupPsnNode(Long groupId, String groupName, Integer nodeId, String groupRole) {
    super();
    this.groupId = groupId;
    this.groupName = groupName;
    this.nodeId = nodeId;
    this.groupRole = groupRole;
  }

  public GroupPsnNode(Long groupId, String groupName, Integer nodeId, String disciplines, String groupCategory,
      String openType, String groupRole) {
    super();
    this.groupId = groupId;
    this.groupName = groupName;
    this.nodeId = nodeId;
    this.disciplines = disciplines;
    this.groupCategory = groupCategory;
    this.openType = openType;
    this.groupRole = groupRole;
  }

  public GroupPsnNode(Long groupId, String groupName, Integer nodeId, String disciplines, String groupCategory,
      String openType, String discCodes, String keyWords, String isisGuid, Integer sumMembers, Date createDate) {
    super();
    this.groupId = groupId;
    this.groupName = groupName;
    this.nodeId = nodeId;
    this.disciplines = disciplines;
    this.groupCategory = groupCategory;
    this.openType = openType;
    this.discCodes = discCodes;
    this.keyWords = keyWords;
    this.isisGuid = isisGuid;
    this.sumMembers = sumMembers;
    this.createDate = createDate;
  }

  @Id
  @Column(name = "GROUP_ID")
  public Long getGroupId() {
    return groupId;
  }

  @Column(name = "GROUP_NAME")
  public String getGroupName() {
    return groupName;
  }

  @Column(name = "NODE_ID")
  public Integer getNodeId() {
    return nodeId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  public void setDes3GroupId(String des3GroupId) {
    if (this.groupId == null && StringUtils.isNotBlank(des3GroupId)) {
      this.groupId = Long.valueOf(ServiceUtil.decodeFromDes3(des3GroupId));
    }
    this.des3GroupId = des3GroupId;
  }

  @Transient
  public String getDes3GroupId() {
    if (this.groupId != null && des3GroupId == null) {
      des3GroupId = ServiceUtil.encodeToDes3(this.groupId.toString());
    }
    return des3GroupId;
  }

  @Column(name = "DISCIPLINES")
  public String getDisciplines() {
    return disciplines;
  }

  public void setDisciplines(String disciplines) {
    this.disciplines = disciplines;
  }

  @Column(name = "GROUP_CATEGORY")
  public String getGroupCategory() {
    return groupCategory;
  }

  public void setGroupCategory(String groupCategory) {
    this.groupCategory = groupCategory;
  }

  @Column(name = "OPEN_TYPE")
  public String getOpenType() {
    return openType;
  }

  public void setOpenType(String openType) {
    this.openType = openType;
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

  @Column(name = "KEY_WORDS")
  public String getKeyWords() {
    return keyWords;
  }

  public void setKeyWords(String keyWords) {
    this.keyWords = keyWords;
  }

  @Column(name = "ISIS_GUID")
  public String getIsisGuid() {
    return isisGuid;
  }

  public void setIsisGuid(String isisGuid) {
    this.isisGuid = isisGuid;
  }

  @Transient
  public int getNum() {
    return num;
  }

  public void setNum(int num) {
    this.num = num;
  }

  @Transient
  public String getIsPrjView() {
    return isPrjView;
  }

  public void setIsPrjView(String isPrjView) {
    this.isPrjView = isPrjView;
  }

  @Transient
  public String getIsShareFile() {
    return isShareFile;
  }

  public void setIsShareFile(String isShareFile) {
    this.isShareFile = isShareFile;
  }

  @Transient
  public String getEscapeGroupName() {
    if (this.groupName != null) {
      escapeGroupName = HtmlUtils.toHtml(groupName);
    }
    return escapeGroupName;
  }

  public void setEscapeGroupName(String escapeGroupName) {
    this.escapeGroupName = escapeGroupName;
  }

  @Column(name = "SUM_MEMBERS")
  public Integer getSumMembers() {
    return sumMembers;
  }

  public void setSumMembers(Integer sumMembers) {
    this.sumMembers = sumMembers;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  @Transient
  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  @Transient
  public String getGroupRole() {
    return groupRole;
  }

  public void setGroupRole(String groupRole) {
    this.groupRole = groupRole;
  }

}
