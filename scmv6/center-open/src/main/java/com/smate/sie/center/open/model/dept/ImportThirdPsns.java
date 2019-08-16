package com.smate.sie.center.open.model.dept;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 第三方人员信息.
 * 
 * @author xys
 *
 */
@Entity
@Table(name = "IMPORT_THIRD_PSNS")
public class ImportThirdPsns implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8357767906021361653L;

  @EmbeddedId
  private ImportThirdPsnsPK pk;// 主键
  @Column(name = "ZH_NAME")
  private String zhName;// 中文名

  @Column(name = "UNIT_ID")
  private String unitId;// 部门ID
  @Column(name = "POSITION")
  private String position;// 头衔
  @Column(name = "CREATE_DATE")
  private Date createDate;// 创建日期

  @Transient
  private String firstName; // FIRST NAME
  @Transient
  private String lastName;// LAST NAME

  public ImportThirdPsnsPK getPk() {
    return pk;
  }

  public void setPk(ImportThirdPsnsPK pk) {
    this.pk = pk;
  }

  public String getZhName() {
    return zhName;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
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

  public String getUnitId() {
    return unitId;
  }

  public void setUnitId(String unitId) {
    this.unitId = unitId;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

}
