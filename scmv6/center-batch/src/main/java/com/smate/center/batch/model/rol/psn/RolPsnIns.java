package com.smate.center.batch.model.rol.psn;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.smate.core.base.utils.model.security.InsRole;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 个人基本信息.
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "PSN_INS")
public class RolPsnIns implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -7423038333770065255L;
  /** 主键. */
  private RolPsnInsPk pk;
  /** 在职状态. */
  private Long isIns = 0L;
  /** 在职状态. */
  private String isInsName;
  /** 职务. */
  private String duty;
  /** 职称. */
  private String title;
  /** 职称. */
  private String titleName;
  /** 人员状态. */
  private String psnApplyStatus;
  /** 部门Id. */
  private Long unitId;
  /** 部门Id. */
  private String unitName;
  // 父部门ID
  private Long superUnitId;
  /** 人员状态，0申请加入、1已加入、3拒绝加入、9删除. */
  private Integer status;
  /** 人员名. */
  private String psnName;
  private String zhName;
  private String enName;

  // 职称
  private String position;
  private Long posId;
  private Integer posGrades;

  /** 人员邮件. */
  private String psnEmail;
  /** 指派必要字段. */
  private String firstName;
  private String lastName;
  private String otherName;
  /** 部门管理-科研联系人或者科研管理员ID. */
  private Long unitManageOrContactId;
  /** 人员联系方式电话或者手机. */
  private String tel;
  // psnId,特殊用途，程序将pk.psnId拷贝到此
  private Long cpPsnId;

  private Integer isLogin;

  private Date createAt;

  private Long rolId;
  // 是否允许ROL-R提交成果
  private Integer allowSubmitPub;

  private Integer toSnsNodeId;

  private Long sendLoginPsnId;

  private String des3PsnId;
  // 是否是管理员
  private boolean manager;
  // 是否是部门联系人
  private boolean contact;
  // 用户所在的节点WEB url.
  private String webUrl;
  // 头像地址
  private String avatars;
  // 性别，1男，0女
  private Integer sex;
  // 是否匹配成果给人员标记位(之前叫项目负责人标记)
  private Integer prjPi;
  // 省市ID
  private Long prvId;
  private Long cyId;
  // 所在市级管辖区ID（冗余机构所在市级管辖区ID）
  private Long disId;
  // 单位管理员 0-否 1-是
  private int admin;
  // 图书馆用户 0-否 1-是
  private int librarian;
  // 单位领导
  private int head;
  // 用户角色
  private List<InsRole> roleList;
  // 用户来源类别（1：申请与在研项目相关的人，2：历年在基金委中填写过项目结题报告的人）
  private Integer userType;
  // 关键词是否已经投过票（1：已投，0：未投）
  private Integer isVote = 0;
  // 默认0，登录帐号跟邮件不一致的用户标记1，排除RO
  private Integer emailFlag = 0;
  // 默认0，是否是常用人员，常用人员的范围是：申请、在研项目参与人、负责人，去掉学生，
  // 科研处录入或导入或批准的人员默认是常用人员
  // 1:常用人员 0：非常用人员
  private Integer cyFlag = 0;

  private String initials;
  private String website;
  private String cityuTitle;
  private String remark;
  private Integer isActive = 0;// 激活状态，默认未机会
  private Long sid;
  // 人员公开情况：0不公开，1公开
  private Integer isPublic = 1;

  // 更新sns个人信息后 同步sie 工作经历用
  private Long syncInsId;
  private Integer orderByLevel = 0; // 排序条件 (ROL-1879)
  // sie协会版-人员附属单位
  private String affiliatedIns;
  // sie协会版的Association Role
  private String associationRole;

  private int hIndex;// H指数
  private String insName;// 单位名称

  public RolPsnIns() {
    super();
    // 默认允许提交
    this.allowSubmitPub = 1;
    this.status = 0;
    this.createAt = new Date();
  }

  public RolPsnIns(Long psnId, Long insId) {
    super();
    this.pk = new RolPsnInsPk(psnId, insId);
    // 默认允许提交
    this.allowSubmitPub = 1;
    this.status = 0;
    this.createAt = new Date();
  }

  /**
   * 供自动提示查询用.
   * 
   * @param isIns
   * @param unitId
   * @param psnName
   * @param insId
   */
  public RolPsnIns(Long psnId, Long unitId, String zhName, String enName) {
    super();
    cpPsnId = psnId;
    this.unitId = unitId;
    this.zhName = zhName;
    this.enName = enName;
    this.pk = new RolPsnInsPk();
    this.pk.setPsnId(psnId);
  }

  public RolPsnIns(Long psnId, String zhName, String firstName, String lastName) {
    this.pk = new RolPsnInsPk();
    this.pk.setPsnId(psnId);
    this.zhName = zhName;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  /**
   * 供自动提示查询用.
   * 
   * @param isIns
   * @param unitId
   * @param psnName
   * @param insId
   */
  public RolPsnIns(Long psnId, Long unitId, String zhName, String enName, String psnEmail) {
    super();
    cpPsnId = psnId;
    this.unitId = unitId;
    this.zhName = zhName;
    this.enName = enName;
    this.pk = new RolPsnInsPk();
    this.pk.setPsnId(psnId);
    this.psnEmail = psnEmail;
  }

  public RolPsnIns(Long psnId, Long unitId, Long superUnitId, String zhName, String enName, String psnEmail) {
    super();
    cpPsnId = psnId;
    this.unitId = unitId;
    this.superUnitId = superUnitId;
    this.zhName = zhName;
    this.enName = enName;
    this.pk = new RolPsnInsPk();
    this.pk.setPsnId(psnId);
    this.psnEmail = psnEmail;
  }

  public RolPsnIns(RolPsnInsPk pk, String firstName, String lastName) {
    super();
    this.pk = pk;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public RolPsnIns(RolPsnInsPk pk, String firstName, String lastName, String otherName) {
    super();
    this.pk = pk;
    this.firstName = firstName;
    this.lastName = lastName;
    this.otherName = otherName;
  }

  public RolPsnIns(RolPsnInsPk pk, String firstName, String lastName, String otherName, String zhName) {
    super();
    this.pk = pk;
    this.firstName = firstName;
    this.lastName = lastName;
    this.otherName = otherName;
    this.zhName = zhName;
  }

  public RolPsnIns(RolPsnInsPk pk, Long unitId, Long superUnitId) {
    super();
    this.pk = pk;
    this.unitId = unitId;
    this.superUnitId = superUnitId;
  }

  public RolPsnIns(RolPsnInsPk pk, String psnName) {
    super();
    this.pk = pk;
    this.psnName = psnName;
  }

  public RolPsnIns(RolPsnInsPk pk, Integer status) {
    super();
    this.pk = pk;
    this.status = status;
  }

  public RolPsnIns(RolPsnInsPk pk, Integer status, Integer isPublic) {
    super();
    this.pk = pk;
    this.status = status;
    this.isPublic = isPublic;
  }

  public RolPsnIns(RolPsnInsPk pk, Integer status, Integer isPublic, Date createAt) {
    super();
    this.pk = pk;
    this.status = status;
    this.isPublic = isPublic;
    this.createAt = createAt;
  }

  /**
   * @return pk
   */
  @EmbeddedId
  public RolPsnInsPk getPk() {
    return pk;
  }

  /**
   * @param pk
   */
  public void setPk(RolPsnInsPk pk) {
    this.pk = pk;
  }

  /**
   * @return isIns
   */
  @Column(name = "NOT_IN_JOB")
  public Long getIsIns() {
    return isIns;
  }

  @Transient
  public Long getCpPsnId() {
    return cpPsnId;
  }

  public void setCpPsnId(Long cpPsnId) {
    this.cpPsnId = cpPsnId;
  }

  /**
   * @return unitId
   */
  @Column(name = "UNIT_ID")
  public Long getUnitId() {
    return unitId;
  }

  /**
   * @param unitId
   */
  public void setUnitId(Long unitId) {
    this.unitId = unitId;
  }

  /**
   * @param isIns
   */
  public void setIsIns(Long isIns) {
    this.isIns = isIns;
  }

  /**
   * @return duty
   */
  @Column(name = "DUTY")
  public String getDuty() {
    return duty;
  }

  /**
   * @param duty
   */
  public void setDuty(String duty) {
    this.duty = duty;
  }

  /**
   * @return title
   */
  @Column(name = "TITLE")
  public String getTitle() {
    return title;
  }

  @Column(name = "PRJ_PI")
  public Integer getPrjPi() {
    return prjPi;
  }

  public void setPrjPi(Integer prjPi) {
    this.prjPi = prjPi;
  }

  /**
   * @param title
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * @return psnApplyStatus
   */
  @Transient
  public String getPsnApplyStatus() {
    return psnApplyStatus;
  }

  /**
   * @param psnApplyStatus
   */
  public void setPsnApplyStatus(String psnApplyStatus) {
    this.psnApplyStatus = psnApplyStatus;
  }

  /**
   * @return psnName
   */
  @Transient
  public String getPsnName() {
    return psnName;
  }

  /**
   * @param psnName
   */
  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  @Column(name = "ZH_NAME")
  public String getZhName() {
    return zhName;
  }

  @Column(name = "EN_NAME")
  public String getEnName() {
    return enName;
  }

  @Column(name = "POSITION")
  public String getPosition() {
    return position;
  }

  @Column(name = "POS_ID")
  public Long getPosId() {
    return posId;
  }

  @Column(name = "POS_GRADES")
  public Integer getPosGrades() {
    return posGrades;
  }

  @Column(name = "DIS_ID")
  public Long getDisId() {
    return disId;
  }

  public void setDisId(Long disId) {
    this.disId = disId;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public void setPosId(Long posId) {
    this.posId = posId;
  }

  public void setPosGrades(Integer posGrades) {
    this.posGrades = posGrades;
  }

  /**
   * @return psnEmail
   */
  @Column(name = "PSN_EMAIL")
  public String getPsnEmail() {
    return psnEmail;
  }

  /**
   * @param psnEmail
   */
  public void setPsnEmail(String psnEmail) {
    this.psnEmail = psnEmail;
  }

  /**
   * @return the status
   */
  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(Integer status) {
    this.status = status;
  }

  @Transient
  public String getIsInsName() {
    return isInsName;
  }

  public void setIsInsName(String isInsName) {
    this.isInsName = isInsName;
  }

  @Transient
  public Long getRolId() {
    return rolId;
  }

  public void setRolId(Long rolId) {
    this.rolId = rolId;
  }

  @Transient
  public String getUnitName() {
    return unitName;
  }

  public void setUnitName(String unitName) {
    this.unitName = unitName;
  }

  @Transient
  public Long getUnitManageOrContactId() {
    return unitManageOrContactId;
  }

  public void setUnitManageOrContactId(Long unitManageOrContactId) {
    this.unitManageOrContactId = unitManageOrContactId;
  }

  @Column(name = "TEL")
  public String getTel() {
    return tel;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  @Column(name = "IS_LOGIN")
  public Integer getIsLogin() {
    return isLogin;
  }

  public void setIsLogin(Integer isLogin) {
    this.isLogin = isLogin;
  }

  @Transient
  public String getTitleName() {
    return titleName;
  }

  public void setTitleName(String titleName) {
    this.titleName = titleName;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  /**
   * @return the createAt
   */
  @Column(name = "CREATE_DATE")
  public Date getCreateAt() {
    return createAt;
  }

  @Column(name = "ALLOW_SUBMIT_PUB")
  public Integer getAllowSubmitPub() {
    return allowSubmitPub;
  }

  public void setAllowSubmitPub(Integer allowSubmitPub) {
    this.allowSubmitPub = allowSubmitPub;
  }

  /**
   * @param createAt the createAt to set
   */
  public void setCreateAt(Date createAt) {
    this.createAt = createAt;
  }

  /**
   * @return the firstName
   */
  @Column(name = "FIRST_NAME")
  public String getFirstName() {
    return firstName;
  }

  /**
   * @param firstName the firstName to set
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * @return the lastName
   */
  @Column(name = "LAST_NAME")
  public String getLastName() {
    return lastName;
  }

  /**
   * @param lastName the lastName to set
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * @return the otherName
   */
  @Column(name = "OTHER_NAME")
  public String getOtherName() {
    return otherName;
  }

  @Column(name = "AVATARS")
  public String getAvatars() {
    return avatars;
  }

  public void setAvatars(String avatars) {
    this.avatars = avatars;
  }

  @Column(name = "SEX")
  public Integer getSex() {
    return sex;
  }

  @Column(name = "SUPER_UNIT_ID")
  public Long getSuperUnitId() {
    return superUnitId;
  }

  @Column(name = "PRV_ID")
  public Long getPrvId() {
    return prvId;
  }

  @Column(name = "CY_ID")
  public Long getCyId() {
    return cyId;
  }

  public void setSuperUnitId(Long superUnitId) {
    this.superUnitId = superUnitId;
  }

  public void setPrvId(Long prvId) {
    this.prvId = prvId;
  }

  public void setCyId(Long cyId) {
    this.cyId = cyId;
  }

  public void setSex(Integer sex) {
    this.sex = sex;
  }

  /**
   * @param otherName the otherName to set
   */
  public void setOtherName(String otherName) {
    this.otherName = otherName;
  }

  /**
   * @return the sendLoginPsnId
   */
  @Transient
  public Long getSendLoginPsnId() {
    return sendLoginPsnId;
  }

  /**
   * @param sendLoginPsnId the sendLoginPsnId to set
   */
  public void setSendLoginPsnId(Long sendLoginPsnId) {
    this.sendLoginPsnId = sendLoginPsnId;
  }

  /**
   * @return the toSnsNodeIds
   */
  @Transient
  public Integer getToSnsNodeId() {
    return toSnsNodeId;
  }

  /**
   * @param toSnsNodeIds the toSnsNodeIds to set
   */
  public void setToSnsNodeId(Integer toSnsNodeId) {
    this.toSnsNodeId = toSnsNodeId;
  }

  @Transient
  public String getDes3PsnId() {
    if (this.getPsnId() != null && des3PsnId == null) {
      des3PsnId = ServiceUtil.encodeToDes3(getPsnId().toString());
    }
    return des3PsnId;
  }

  @Transient
  public Long getPsnId() {
    if (this.pk != null) {
      return this.pk.getPsnId();
    }
    return null;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  @Transient
  public boolean isManager() {
    return manager;
  }

  public void setManager(boolean manager) {
    this.manager = manager;
  }

  @Transient
  public boolean isContact() {
    return contact;
  }

  public void setContact(boolean contact) {
    this.contact = contact;
  }

  @Transient
  public String getWebUrl() {
    return webUrl;
  }

  public void setWebUrl(String webUrl) {
    this.webUrl = webUrl;
  }

  /**
   * @return the admin
   */
  @Transient
  public int getAdmin() {
    return admin;
  }

  /**
   * @param admin the admin to set
   */
  public void setAdmin(int admin) {
    this.admin = admin;
  }

  /**
   * @return the head
   */
  @Transient
  public int getHead() {
    return head;
  }

  /**
   * @param head the head to set
   */
  public void setHead(int head) {
    this.head = head;
  }

  @Transient
  public List<InsRole> getRoleList() {
    return roleList;
  }

  public void setRoleList(List<InsRole> roleList) {
    this.roleList = roleList;
  }

  @Column(name = "USER_TYPE")
  public Integer getUserType() {
    return userType;
  }

  public void setUserType(Integer userType) {
    this.userType = userType;
  }

  @Column(name = "IS_VOTE")
  public Integer getIsVote() {
    return isVote;
  }

  public void setIsVote(Integer isVote) {
    this.isVote = isVote;
  }

  @Column(name = "EMAIL_FLAG")
  public Integer getEmailFlag() {
    return emailFlag;
  }

  public void setEmailFlag(Integer emailFlag) {
    this.emailFlag = emailFlag;
  }

  @Column(name = "CY_FLAG")
  public Integer getCyFlag() {
    return cyFlag;
  }

  public void setCyFlag(Integer cyFlag) {
    this.cyFlag = cyFlag;
  }

  @Transient
  public int getLibrarian() {
    return librarian;
  }

  public void setLibrarian(int librarian) {
    this.librarian = librarian;
  }

  @Column(name = "INITIALS")
  public String getInitials() {
    return initials;
  }

  @Column(name = "WEBSITE")
  public String getWebsite() {
    return website;
  }

  @Column(name = "CITYU_TITLE")
  public String getCityuTitle() {
    return cityuTitle;
  }

  @Column(name = "REMARK")
  public String getRemark() {
    return remark;
  }

  public void setInitials(String initials) {
    this.initials = initials;
  }

  public void setWebsite(String website) {
    this.website = website;
  }

  public void setCityuTitle(String cityuTitle) {
    this.cityuTitle = cityuTitle;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  @Column(name = "IS_ACTIVE")
  public Integer getIsActive() {
    return isActive;
  }

  public void setIsActive(Integer isActive) {
    this.isActive = isActive;
  }

  @Column(name = "SID")
  public Long getSid() {
    return sid;
  }

  public void setSid(Long sid) {
    this.sid = sid;
  }

  /**
   * @return isPublic
   */
  @Column(name = "IS_PUBLIC")
  public Integer getIsPublic() {
    return isPublic;
  }

  /**
   * @param isPublic 要设置的 isPublic
   */
  public void setIsPublic(Integer isPublic) {
    this.isPublic = isPublic;
  }

  @Transient
  public Long getSyncInsId() {
    return syncInsId;
  }

  public void setSyncInsId(Long syncInsId) {
    this.syncInsId = syncInsId;
  }

  @Column(name = "ORDERBY_LEVEL")
  public Integer getOrderByLevel() {
    return orderByLevel;
  }

  public void setOrderByLevel(Integer orderByLevel) {
    this.orderByLevel = orderByLevel;
  }

  @Column(name = "AFFILIATED_INS")
  public String getAffiliatedIns() {
    return affiliatedIns;
  }

  public void setAffiliatedIns(String affiliatedIns) {
    this.affiliatedIns = affiliatedIns;
  }


  @Column(name = "ASSOCIATION_ROLE")
  public String getAssociationRole() {
    return associationRole;
  }

  public void setAssociationRole(String associationRole) {
    this.associationRole = associationRole;
  }

  @Transient
  public int gethIndex() {
    return hIndex;
  }

  public void sethIndex(int hIndex) {
    this.hIndex = hIndex;
  }

  @Transient
  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((pk == null) ? 0 : pk.hashCode());
    return result;
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    RolPsnIns other = (RolPsnIns) obj;
    if (pk == null) {
      if (other.pk != null)
        return false;
    } else if (!pk.equals(other.pk))
      return false;
    return true;
  }

}
