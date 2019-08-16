package com.smate.sie.core.base.utils.model.psn;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 
 * @author yxs
 * @descript 人员实体
 */
@Entity
@Table(name = "PSN_INS")
public class SieInsPerson {
  /** 主键. */
  private SieInsPersonPk pk;

  private Long unitId;

  private String unitName;

  private Long superUnitId;

  private String duty;// 职务

  private String title;// 职称

  private String email;// 邮箱

  private Date updateDate;// 最后更新日期

  private int status;// 人员状态：0待审核（不可登录），1正常（可登录）9删除（不可登录）

  private String firstName;// CAS：姓

  private String lastName;// CAS：名

  private String zhName;// CAS：中文名

  private String enName;// CAS：英文名或全名FULL NAME

  private String position;// CAS：职称，CONST_POSITION

  private Long posId;// CAS：职称，CONST_POSITION

  private String posGrades;// CAS：职级，对应const_dictionary.category=const_grades

  private String tel;// CAS：联系电话

  private String mobile;// CAS：手机

  private String avatars;// CAS：头像地址

  private Integer sex;// CAS：性别，1男0女

  private int isPublic;// 是否公开：0不公开1公开，默认1

  private String remark;// 备注
  private Date createDate;// 创建日期

  private String disciName;
  private int psnSum; // 人员数
  private int prjSum;// 项目数
  private int pubSum;// 成果数
  private int ptSum;// 专利数
  private String name;
  private String des3PsnId;
  private Long regionId;
  private String idcard;
  private Integer highestEdu;

  public SieInsPerson() {
    super();
  }


  public SieInsPerson(Long psnId) {
    super();
    this.pk = new SieInsPersonPk(psnId, 0L);
  }


  public SieInsPerson(Long psnId, Long insId, String zhName, String enName, String email, String tel, String avatars,
      String position, Long unitId) {
    super();
    this.pk = new SieInsPersonPk(psnId, insId);
    this.zhName = zhName;
    this.enName = enName;
    this.email = email;
    this.tel = tel;
    this.avatars = avatars;
    this.position = position;
    this.unitId = unitId;
    this.des3PsnId = getDes3PsnId();
  }

  public SieInsPerson(Long psnId, Long insId, String zhName, String enName, String firstName, String lastName,
      String email, Long posId, String position, Long unitId, String unitName, int status, int isPublic) {
    super();
    this.pk = new SieInsPersonPk(psnId, insId);
    this.zhName = zhName;
    this.enName = enName;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.posId = posId;
    this.position = position;
    this.unitId = unitId;
    this.unitName = unitName;
    this.status = status;
    this.isPublic = isPublic;
    this.createDate = new Date();
  }

  public SieInsPerson(SieInsPersonPk pk, String psnName, String ename, String psnEmail, String firstName,
      String lastName, String tel, String avatars, Integer status, Long posId, String position, String posGrades) {
    super();
    this.pk = pk;
    this.zhName = psnName;
    this.enName = ename;
    this.email = psnEmail;
    this.firstName = firstName;
    this.lastName = lastName;
    this.tel = tel;
    this.status = status;
    this.avatars = avatars;
    this.posId = posId;
    this.position = position;
    this.posGrades = posGrades;
    this.isPublic = 1;
    this.createDate = new Date();
  }

  /**
   * 批量注册单位，新增
   * 
   * @param pk
   * @param psnName
   * @param ename
   * @param psnEmail
   * @param firstName
   * @param lastName
   * @param tel
   * @param avatars
   * @param status
   * @param posId
   * @param position
   * @param posGrades
   * @param mobile
   * @param regionId
   * @param sex
   */
  public SieInsPerson(SieInsPersonPk pk, String psnName, String ename, String psnEmail, String firstName,
      String lastName, String tel, String avatars, Integer status, Long posId, String position, String posGrades,
      String mobile, Long regionId, Integer sex) {
    super();
    this.pk = pk;
    this.zhName = psnName;
    this.enName = ename;
    this.email = psnEmail;
    this.firstName = firstName;
    this.lastName = lastName;
    this.tel = tel;
    this.status = status;
    this.avatars = avatars;
    this.posId = posId;
    this.position = position;
    this.posGrades = posGrades;
    this.isPublic = 1;
    this.createDate = new Date();
    this.mobile = mobile;
    this.regionId = regionId;
    this.sex = sex;
  }

  public SieInsPerson(Long psnId, Long insId) {
    super();
    this.pk = new SieInsPersonPk(psnId, insId);
    this.status = 0;
    this.createDate = new Date();
  }

  @Column(name = "UNIT_ID")
  public Long getUnitId() {
    return unitId;
  }

  public void setUnitId(Long unitId) {
    this.unitId = unitId;
  }

  @Column(name = "SUPER_UNIT_ID")
  public Long getSuperUnitId() {
    return superUnitId;
  }

  public void setSuperUnitId(Long superUnitId) {
    this.superUnitId = superUnitId;
  }

  @Column(name = "DUTY")
  public String getDuty() {
    return duty;
  }

  public void setDuty(String duty) {
    this.duty = duty;
  }

  @Column(name = "TITLE")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Column(name = "EMAIL")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  @Column(name = "STATUS")
  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  @Column(name = "FIRST_NAME")
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @Column(name = "LAST_NAME")
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Column(name = "ZH_NAME")
  public String getZhName() {
    return zhName;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  @Column(name = "EN_NAME")
  public String getEnName() {
    return enName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  @Column(name = "POSITION")
  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  @Column(name = "POS_ID")
  public Long getPosId() {
    return posId;
  }

  public void setPosId(Long posId) {
    this.posId = posId;
  }

  @Column(name = "POS_GRADES")
  public String getPosGrades() {
    return posGrades;
  }

  public void setPosGrades(String posGrades) {
    this.posGrades = posGrades;
  }

  @Column(name = "TEL")
  public String getTel() {
    return tel;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  @Column(name = "MOBILE")
  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
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

  public void setSex(Integer sex) {
    this.sex = sex;
  }

  @Column(name = "IS_PUBLIC")
  public int getIsPublic() {
    return isPublic;
  }

  public void setIsPublic(int isPublic) {
    this.isPublic = isPublic;
  }

  @Column(name = "REMARK")
  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  @EmbeddedId
  public SieInsPersonPk getPk() {
    return pk;
  }

  public void setPk(SieInsPersonPk pk) {
    this.pk = pk;
  }

  @Transient
  public String getDisciName() {
    return disciName;
  }

  public void setDisciName(String disciName) {
    this.disciName = disciName;
  }

  @Transient
  public int getPsnSum() {
    return psnSum;
  }

  public void setPsnSum(int psnSum) {
    this.psnSum = psnSum;
  }

  @Transient
  public int getPrjSum() {
    return prjSum;
  }

  public void setPrjSum(int prjSum) {
    this.prjSum = prjSum;
  }

  @Transient
  public int getPubSum() {
    return pubSum;
  }

  public void setPubSum(int pubSum) {
    this.pubSum = pubSum;
  }

  @Transient
  public int getPtSum() {
    return ptSum;
  }

  public void setPtSum(int ptSum) {
    this.ptSum = ptSum;
  }

  @Column(name = "UNIT_NAME")
  public String getUnitName() {
    return unitName;
  }

  public void setUnitName(String unitName) {
    this.unitName = unitName;
  }

  @Transient
  public String getDes3PsnId() {
    if (this.pk.getPsnId() != null) {
      des3PsnId = ServiceUtil.encodeToDes3(this.pk.getPsnId().toString());
    }
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  @Transient
  public String getName() {
    String language = LocaleContextHolder.getLocale().getLanguage();
    if ("zh".equals(language)) {
      name = StringUtils.isNotBlank(this.zhName) ? this.zhName : this.enName;
    } else {
      name = StringUtils.isNotBlank(this.enName) ? this.enName : this.zhName;
    }
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "REGION_ID")
  public Long getRegionId() {
    return regionId;
  }

  public void setRegionId(Long regionId) {
    this.regionId = regionId;
  }

  @Column(name = "IDCARD")
  public String getIdcard() {
    return idcard;
  }

  @Column(name = "HIGHEST_EDU")
  public Integer getHighestEdu() {
    return highestEdu;
  }

  public void setIdcard(String idcard) {
    this.idcard = idcard;
  }

  public void setHighestEdu(Integer highestEdu) {
    this.highestEdu = highestEdu;
  }
}
