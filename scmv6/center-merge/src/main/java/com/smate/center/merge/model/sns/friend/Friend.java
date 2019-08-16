package com.smate.center.merge.model.sns.friend;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 个人好友.
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "PSN_FRIEND")
public class Friend implements Serializable {

  private static final long serialVersionUID = 8406216618944570415L;
  // pk
  private Long id;
  // 个人psnId
  private Long psnId;
  // 好友psnId
  private Long friendPsnId;
  // 好友节点
  private Integer friendNode;
  // 对好友的备注
  private String friendNote;
  private String des3FreindPsnId;
  private Integer status;
  // 好友所在的分组
  private String friendGroupList;

  // 页面用的名字（按语言版本显示中文或英文）
  private String name;
  private String friendName;
  private String friendFirstName;
  private String friendLastName;
  private String friendTel;
  private String friendMobile;
  private String friendQQ;
  private String friendMsn;
  private String friendEmail;
  private String friendSkype;
  private String friendHeadUrl;
  private String friendTitle;
  private Long friendFriendCount;
  private Long friendAppraisalCount;

  private Long freindGroupId;
  private String insIdOrNames;
  private Long regionId;
  private boolean friendRecent;
  private String lettersType;
  private Long friendPrjNum;
  private Long friendPubNum;
  private Long friendISI;
  private Long friendHindex;
  private String friendDiscipline;
  private Date createDate;
  private List<Long> recentFriendIds;

  // 查询好友的好友的时候，此字段判断此人是否已经是我的好友
  private int isFriend;

  public Friend() {
    super();
    this.friendTitle = "";
    this.friendEmail = "";
    this.friendMobile = "";
    this.friendMsn = "";
    this.friendQQ = "";
    this.friendTel = "";
    this.friendName = "";
    this.friendSkype = "";
    this.friendFriendCount = 0L;
    this.friendAppraisalCount = 0L;
    this.status = 0;
    this.createDate = new Date();
  }

  public Friend(Long id, Long psnId, Long friendPsnId, Integer friendNode, Integer status, String friendGroupList) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.friendPsnId = friendPsnId;
    this.friendNode = friendNode;
    this.status = status;
    this.friendGroupList = friendGroupList;
  }

  public Friend(Long id, Long psnId, Long friendPsnId, Integer friendNode) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.friendPsnId = friendPsnId;
    this.friendNode = friendNode;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_FRIEND", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "FRIEND_PSN_ID")
  public Long getFriendPsnId() {
    return friendPsnId;
  }

  public void setFriendPsnId(Long friendPsnId) {
    this.friendPsnId = friendPsnId;
  }

  @Column(name = "FRIEND_NODE")
  public Integer getFriendNode() {
    return friendNode;
  }

  public void setFriendNode(Integer friendNode) {
    this.friendNode = friendNode;
  }

  @Column(name = "FRIEND_NOTE")
  public String getFriendNote() {
    return friendNote;
  }

  public void setFriendNote(String friendNote) {
    this.friendNote = friendNote;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(name = "FRIEND_GROUPS")
  public String getFriendGroupList() {
    return friendGroupList;
  }

  public void setFriendGroupList(String friendGroupList) {
    this.friendGroupList = friendGroupList;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    if (createDate == null) {
      createDate = new Date();
    }
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  @Transient
  public String getFriendName() {
    return friendName;
  }

  public void setFriendName(String friendName) {
    this.friendName = friendName;
  }

  @Transient
  public String getFriendFirstName() {
    return friendFirstName;
  }

  public void setFriendFirstName(String friendFirstName) {
    this.friendFirstName = friendFirstName;
  }

  @Transient
  public String getFriendLastName() {
    return friendLastName;
  }

  public void setFriendLastName(String friendLastName) {
    this.friendLastName = friendLastName;
  }

  @Transient
  public String getFriendTel() {
    return friendTel;
  }

  public void setFriendTel(String friendTel) {
    this.friendTel = friendTel;
  }

  @Transient
  public String getFriendMobile() {
    return friendMobile;
  }

  public void setFriendMobile(String friendMobile) {
    this.friendMobile = friendMobile;
  }

  @Transient
  public String getFriendQQ() {
    return friendQQ;
  }

  public void setFriendQQ(String friendQQ) {
    this.friendQQ = friendQQ;
  }

  @Transient
  public String getFriendMsn() {
    return friendMsn;
  }

  public void setFriendMsn(String friendMsn) {
    this.friendMsn = friendMsn;
  }

  @Transient
  public String getFriendEmail() {
    return friendEmail;
  }

  public void setFriendEmail(String friendEmail) {
    this.friendEmail = friendEmail;
  }

  @Transient
  public String getFriendSkype() {
    return friendSkype;
  }

  public void setFriendSkype(String friendSkype) {
    this.friendSkype = friendSkype;
  }

  @Transient
  public String getFriendHeadUrl() {
    return friendHeadUrl;
  }

  public void setFriendHeadUrl(String friendHeadUrl) {
    this.friendHeadUrl = friendHeadUrl;
  }

  @Transient
  public Long getFriendPrjNum() {
    return friendPrjNum;
  }

  public void setFriendPrjNum(Long friendPrjNum) {
    this.friendPrjNum = friendPrjNum;
  }

  @Transient
  public Long getFriendPubNum() {
    return friendPubNum;
  }

  public void setFriendPubNum(Long friendPubNum) {
    this.friendPubNum = friendPubNum;
  }

  @Transient
  public Long getFriendISI() {
    return friendISI;
  }

  public void setFriendISI(Long friendISI) {
    this.friendISI = friendISI;
  }

  @Transient
  public Long getFriendHindex() {
    return friendHindex;
  }

  public void setFriendHindex(Long friendHindex) {
    this.friendHindex = friendHindex;
  }

  @Transient
  public String getFriendDiscipline() {
    return friendDiscipline;
  }

  public void setFriendDiscipline(String friendDiscipline) {
    this.friendDiscipline = friendDiscipline;
  }

  @Transient
  public String getFriendTitle() {
    return friendTitle;
  }

  public void setFriendTitle(String friendTitle) {
    this.friendTitle = friendTitle;
  }

  @Transient
  public Long getFriendFriendCount() {
    return friendFriendCount;
  }

  public void setFriendFriendCount(Long friendFriendCount) {
    this.friendFriendCount = friendFriendCount;
  }

  @Transient
  public Long getFriendAppraisalCount() {
    return friendAppraisalCount;
  }

  public void setFriendAppraisalCount(Long friendAppraisalCount) {
    this.friendAppraisalCount = friendAppraisalCount;
  }

  @Transient
  public Long getFreindGroupId() {
    return freindGroupId;
  }

  public void setFreindGroupId(Long freindGroupId) {
    this.freindGroupId = freindGroupId;
  }

  @Transient
  public boolean getFriendRecent() {
    return friendRecent;
  }

  public void setFriendRecent(boolean friendRecent) {
    this.friendRecent = friendRecent;
  }

  @Transient
  public List<Long> getRecentFriendIds() {
    return recentFriendIds;
  }

  public void setRecentFriendIds(List<Long> recentFriendIds) {
    this.recentFriendIds = recentFriendIds;
  }

  @Transient
  public String getDes3FreindPsnId() {
    if (this.friendPsnId != null && des3FreindPsnId == null) {
      des3FreindPsnId = ServiceUtil.encodeToDes3(this.friendPsnId.toString());
    }
    return des3FreindPsnId;
  }

  public void setDes3FreindPsnId(String des3FreindPsnId) {
    this.des3FreindPsnId = des3FreindPsnId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  // test
  public static void main(String[] args) {
    // 加密
    System.out.println(ServiceUtil.encodeToDes3("123"));
    // 解密
    System.out.println(ServiceUtil.decodeFromDes3("4iV3%2BU%2Fs4TY%3D"));
  }

  @Transient
  public String getInsIdOrNames() {
    return insIdOrNames;
  }

  public void setInsIdOrNames(String insIdOrNames) {
    this.insIdOrNames = insIdOrNames;
  }

  @Transient
  public Long getRegionId() {
    return regionId;
  }

  public void setRegionId(Long regionId) {
    this.regionId = regionId;
  }

  @Transient
  public String getLettersType() {
    return lettersType;
  }

  public void setLettersType(String lettersType) {
    this.lettersType = lettersType;
  }

  @Transient
  public int getIsFriend() {
    return isFriend;
  }

  public void setIsFriend(int isFriend) {
    this.isFriend = isFriend;
  }

  @Transient
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
