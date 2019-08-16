package com.smate.web.group.model.group.invit;

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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.string.ServiceUtil;


/**
 * 群组与人员的邀请关系表.
 * 
 * @author tsz
 * 
 */
@Entity
@Table(name = "GROUP_INVITE_PSN")
public class GroupInvitePsn implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8097734372808153245L;
  // 群组与人员的关系ID
  private Long invitePsnId;

  // 群组邮件邀请ID
  private Long inviteId;
  // 群组ID
  private Long groupId;

  // 人员ID（准备加入群组的人员ID）
  private Long psnId;
  // 加密人员ID（准备加入群组的人员ID）
  private String des3PsnId;

  // 是否同意加入群组[1=是,0=否]
  private String isAccept = "1";
  /* 修改群组个人设置的默认值为 0-不勾选_MJG_SCM-3898 */
  // 是否显示邮件[1=是,0=否]
  private String isShowEmail = "0";
  // 是否显示电话[1=是,0=否]
  private String isShowTel = "0";
  // 是否显示手机[1=是,0=否]
  private String isShowPhone = "0";
  // 是否显示qq[1=是,0=否]
  private String isShowQq = "0";
  // 是否显示msn[1=是,0=否]
  private String isShowMsn = "0";
  // 是否显示skype[1=是,0=否]
  private String isShowSkype = "0";
  // 是否新资源邮件/信息通知[1=是,0=否]
  private String isResNotice = "1";
  // 新讨论邮件/信息通知[1=是,0=否]
  private String isDiscussNotice = "1";
  // 群组中的角色[1=创建人,2=管理员, 3=组员]
  private String groupRole = "3";
  // 创建人psn_id
  private Long createPsnId;
  // 创建时间
  private Date createDate;
  // 群组状态[01=正常,99=删除]
  private String status = "01";

  // 是否已经激活[1=激活,0=未激活]
  private String isActivity = "0";
  // 头衔
  private String titolo;

  private String email;
  private String tel;
  private String mobile;
  private String qqNo;
  private String msnNo;
  private String skype;
  private String position;
  private String avatars;
  private String psnName;
  private String psnFirstName;
  private String psnLastName;
  // 群组成员拥有的公开成果数量

  private Long openPubSum;


  @Id
  @Column(name = "INVITE_PSN_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_GROUP_INVITE_PSN", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getInvitePsnId() {
    return invitePsnId;
  }

  @Column(name = "INVITE_ID")
  public Long getInviteId() {
    return inviteId;
  }

  @Column(name = "GROUP_ID")
  public Long getGroupId() {
    return groupId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "IS_ACCEPT")
  public String getIsAccept() {
    return isAccept;
  }

  @Column(name = "IS_SHOW_EMAIL")
  public String getIsShowEmail() {
    return isShowEmail;
  }

  @Column(name = "IS_SHOW_TEL")
  public String getIsShowTel() {
    return isShowTel;
  }

  @Column(name = "IS_SHOW_PHONE")
  public String getIsShowPhone() {
    return isShowPhone;
  }

  @Column(name = "IS_RES_NOTICE")
  public String getIsResNotice() {
    return isResNotice;
  }

  @Column(name = "IS_DISCUSS_NOTICE")
  public String getIsDiscussNotice() {
    return isDiscussNotice;
  }

  @Column(name = "GROUP_ROLE")
  public String getGroupRole() {
    return groupRole;
  }

  @Column(name = "CREATE_PSN_ID")
  public Long getCreatePsnId() {
    return createPsnId;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  @Column(name = "STATUS")
  public String getStatus() {
    return status;
  }

  public void setInvitePsnId(Long invitePsnId) {
    this.invitePsnId = invitePsnId;
  }

  public void setInviteId(Long inviteId) {
    this.inviteId = inviteId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setIsAccept(String isAccept) {
    this.isAccept = isAccept;
  }

  public void setIsShowEmail(String isShowEmail) {
    this.isShowEmail = isShowEmail;
  }

  public void setIsShowTel(String isShowTel) {
    this.isShowTel = isShowTel;
  }

  public void setIsShowPhone(String isShowPhone) {
    this.isShowPhone = isShowPhone;
  }

  public void setIsResNotice(String isResNotice) {
    this.isResNotice = isResNotice;
  }

  public void setIsDiscussNotice(String isDiscussNotice) {
    this.isDiscussNotice = isDiscussNotice;
  }

  public void setGroupRole(String groupRole) {
    this.groupRole = groupRole;
  }

  public void setCreatePsnId(Long createPsnId) {
    this.createPsnId = createPsnId;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public void setStatus(String status) {
    this.status = status;
  }


  @Column(name = "IS_SHOW_QQ")
  public String getIsShowQq() {
    return isShowQq;
  }

  @Column(name = "IS_SHOW_MSN")
  public String getIsShowMsn() {
    return isShowMsn;
  }

  @Column(name = "IS_SHOW_SKYPE")
  public String getIsShowSkype() {
    return isShowSkype;
  }

  public void setIsShowQq(String isShowQq) {
    this.isShowQq = isShowQq;
  }

  public void setIsShowMsn(String isShowMsn) {
    this.isShowMsn = isShowMsn;
  }

  public void setIsShowSkype(String isShowSkype) {
    this.isShowSkype = isShowSkype;
  }



  @Column(name = "IS_ACTIVITY")
  public String getIsActivity() {
    return isActivity;
  }

  public void setIsActivity(String isActivity) {
    this.isActivity = isActivity;
  }



  @Column(name = "EMAIL")
  public String getEmail() {
    return email;
  }

  @Column(name = "TEL")
  public String getTel() {
    return tel;
  }

  @Column(name = "QQ_NO")
  public String getQqNo() {
    return qqNo;
  }

  @Column(name = "MSN_NO")
  public String getMsnNo() {
    return msnNo;
  }

  @Column(name = "SKYPE")
  public String getSkype() {
    return skype;
  }

  @Column(name = "POSITION")
  public String getPosition() {
    return position;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  public void setQqNo(String qqNo) {
    this.qqNo = qqNo;
  }

  public void setMsnNo(String msnNo) {
    this.msnNo = msnNo;
  }

  public void setSkype(String skype) {
    this.skype = skype;
  }

  public void setPosition(String position) {
    this.position = position;
  }



  @Column(name = "AVATARS")
  public String getAvatars() {
    return avatars;
  }

  public void setAvatars(String avatars) {
    this.avatars = avatars;
  }

  @Column(name = "NAME")
  public String getPsnName() {
    return psnName;
  }

  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  @Column(name = "MOBILE")
  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }



  @Column(name = "TITOLO")
  public String getTitolo() {
    return titolo;
  }

  public void setTitolo(String titolo) {
    this.titolo = titolo;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Column(name = "FIRST_NAME")
  public String getPsnFirstName() {
    return psnFirstName;
  }

  public void setPsnFirstName(String psnFirstName) {
    this.psnFirstName = psnFirstName;
  }

  @Column(name = "LAST_NAME")
  public String getPsnLastName() {
    return psnLastName;
  }

  public void setPsnLastName(String psnLastName) {
    this.psnLastName = psnLastName;
  }

  @Transient
  public Long getOpenPubSum() {
    return openPubSum;
  }

  public void setOpenPubSum(Long openPubSum) {
    this.openPubSum = openPubSum;
  }


  public GroupInvitePsn() {

  }

  public GroupInvitePsn(Long psnId, String groupRole, String avatars, String psnName) {
    this.psnId = psnId;
    this.groupRole = groupRole;
    this.avatars = avatars;
    this.psnName = psnName;
  }

  public GroupInvitePsn(Long invitePsnId, Long psnId, String groupRole, String avatars, String psnName) {
    this.invitePsnId = invitePsnId;
    this.psnId = psnId;
    this.groupRole = groupRole;
    this.avatars = avatars;
    this.psnName = psnName;
  }

  @Transient
  public String getDes3PsnId() {
    if (StringUtils.isBlank(des3PsnId) && this.psnId != null) {
      this.des3PsnId = ServiceUtil.encodeToDes3(this.psnId.toString());
    }
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

}
