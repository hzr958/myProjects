package com.smate.sie.center.task.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 人员与单位关系.
 * 
 * @author xys
 * 
 */
@Entity
@Table(name = "PSN_INS")
public class SiePsnIns implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -7423038333770065255L;

  @EmbeddedId
  private SiePsnInsPk pk;// 主键
  @Column(name = "UNIT_ID")
  private Long unitId;// 部门id
  @Column(name = "SUPER_UNIT_ID")
  private Long superUnitId;// 父部门id
  @Column(name = "DUTY")
  private String duty;// 职务
  @Column(name = "TITLE")
  private String title;// 职称/头衔
  @Column(name = "EMAIL")
  private String email;// 邮箱
  @Column(name = "CREATE_DATE")
  private Date createDate;// 创建日期
  @Column(name = "UPDATE_DATE")
  private Date upateDate;// 更新日期
  @Column(name = "STATUS")
  private Integer status;// 人员状态：0待审核，不可登录；1正常，可登录；9删除，不可登录
  @Column(name = "FIRST_NAME")
  private String firstName;
  @Column(name = "LAST_NAME")
  private String lastName;
  @Column(name = "ZH_NAME")
  private String zhName;// 中文名
  @Column(name = "EN_NAME")
  private String enName;// 英文名
  @Column(name = "POSITION")
  private String position;// 职称
  @Column(name = "POS_ID")
  private Long posId;// 职称id
  @Column(name = "POS_GRADES")
  private String posGrades;// 职称等级
  @Column(name = "TEL")
  private String tel;// 联系电话
  @Column(name = "MOBILE")
  private String mobile;// 手机
  @Column(name = "AVATARS")
  private String avatars;// 头像地址
  @Column(name = "SEX")
  private Integer sex;// 性别：1男，0女
  @Column(name = "IS_PUBLIC")
  private Integer isPublic = 1;// 是否公开：0不公开，1公开
  @Column(name = "REGION_ID")
  private Long regionId;
  @Column(name = "UNIT_NAME")
  private String unitName;

  public SiePsnIns() {
    super();
  }

  public SiePsnIns(Long psnId, Long insId, String zhName, String firstName, String lastName, String email, Long posId,
      String position, String posGrades, Long unitId, int status, int isPublic) {
    super();
    this.pk = new SiePsnInsPk(psnId, insId);
    this.zhName = zhName;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.posId = posId;
    this.position = position;
    this.posGrades = posGrades;
    this.unitId = unitId;
    this.status = status;
    this.isPublic = isPublic;
    this.createDate = new Date();
  }

  public SiePsnInsPk getPk() {
    return pk;
  }

  public void setPk(SiePsnInsPk pk) {
    this.pk = pk;
  }

  public Long getUnitId() {
    return unitId;
  }

  public void setUnitId(Long unitId) {
    this.unitId = unitId;
  }

  public Long getSuperUnitId() {
    return superUnitId;
  }

  public void setSuperUnitId(Long superUnitId) {
    this.superUnitId = superUnitId;
  }

  public String getDuty() {
    return duty;
  }

  public void setDuty(String duty) {
    this.duty = duty;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getUpateDate() {
    return upateDate;
  }

  public void setUpateDate(Date upateDate) {
    this.upateDate = upateDate;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getZhName() {
    return zhName;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  public String getEnName() {
    return enName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public Long getPosId() {
    return posId;
  }

  public void setPosId(Long posId) {
    this.posId = posId;
  }

  public String getPosGrades() {
    return posGrades;
  }

  public void setPosGrades(String posGrades) {
    this.posGrades = posGrades;
  }

  public String getTel() {
    return tel;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getAvatars() {
    return avatars;
  }

  public void setAvatars(String avatars) {
    this.avatars = avatars;
  }

  public Integer getSex() {
    return sex;
  }

  public void setSex(Integer sex) {
    this.sex = sex;
  }

  public Integer getIsPublic() {
    return isPublic;
  }

  public void setIsPublic(Integer isPublic) {
    this.isPublic = isPublic;
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
    SiePsnIns other = (SiePsnIns) obj;
    if (pk == null) {
      if (other.pk != null)
        return false;
    } else if (!pk.equals(other.pk))
      return false;
    return true;
  }

  public Long getRegionId() {
    return regionId;
  }

  public void setRegionId(Long regionId) {
    this.regionId = regionId;
  }

  public String getUnitName() {
    return unitName;
  }

  public void setUnitName(String unitName) {
    this.unitName = unitName;
  }

}
