package com.smate.center.batch.model.sns.prj;

import java.io.Serializable;

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
 * 我的群组成员.
 * 
 * @author zhuangyanming
 * 
 */
@Entity
@Table(name = "PERSON")
public class GroupMember implements Serializable {

  private static final long serialVersionUID = 7118657966133182919L;
  private Long psnId;
  private String firstName;
  private String lastName;
  private String email;
  private String tel;
  private String mobile;
  private String qqNo;
  private String msnNo;
  private String insName;
  private String position;
  private String avatars;
  private String psnName;
  // 性别1男,0女
  private Integer sex;
  private GroupInvitePsn groupInvitePsn;
  private Long friendFriendCount;
  private Long friendAppraisalCount;
  private String des3PsnId;
  // 头衔
  private String titolo;

  @Id
  @Column(name = "PSN_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PERSON", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "FIRST_NAME")
  public String getFirstName() {
    return firstName;
  }

  @Column(name = "LAST_NAME")
  public String getLastName() {
    return lastName;
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

  @Column(name = "POSITION")
  public String getPosition() {
    return position;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
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

  public void setPosition(String position) {
    this.position = position;
  }

  @Transient
  public GroupInvitePsn getGroupInvitePsn() {
    return groupInvitePsn;
  }

  public void setGroupInvitePsn(GroupInvitePsn groupInvitePsn) {
    this.groupInvitePsn = groupInvitePsn;
  }

  @Column(name = "INS_NAME")
  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
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

  @Transient
  public Long getFriendFriendCount() {
    return friendFriendCount;
  }

  @Transient
  public Long getFriendAppraisalCount() {
    return friendAppraisalCount;
  }

  public void setFriendFriendCount(Long friendFriendCount) {
    this.friendFriendCount = friendFriendCount;
  }

  public void setFriendAppraisalCount(Long friendAppraisalCount) {
    this.friendAppraisalCount = friendAppraisalCount;
  }

  @Transient
  public String getDes3PsnId() {
    if (this.psnId != null && des3PsnId == null) {
      des3PsnId = ServiceUtil.encodeToDes3(this.psnId.toString());
    }
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  @Column(name = "SEX")
  public Integer getSex() {
    return sex;
  }

  public void setSex(Integer sex) {
    this.sex = sex;
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

}
