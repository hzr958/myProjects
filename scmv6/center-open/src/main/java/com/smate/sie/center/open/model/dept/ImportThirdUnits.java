package com.smate.sie.center.open.model.dept;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 第三方部门信息.
 * 
 * @author xys
 *
 */
@Entity
@Table(name = "IMPORT_THIRD_UNITS")
public class ImportThirdUnits implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6961192339198933818L;

  @EmbeddedId
  private ImportThirdUnitsPK pk;// 主键
  @Column(name = "ZH_NAME")
  private String zhName;// 部门中文名称
  @Column(name = "EN_NAME")
  private String enName;// 部门英文名称
  @Column(name = "SUPER_UNIT_ID")
  private String superUnitId;// 第三方上级部门ID
  @Column(name = "CREATE_DATE")
  private Date createDate;// 创建日期

  public ImportThirdUnitsPK getPk() {
    return pk;
  }

  public void setPk(ImportThirdUnitsPK pk) {
    this.pk = pk;
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

  public String getSuperUnitId() {
    return superUnitId;
  }

  public void setSuperUnitId(String superUnitId) {
    this.superUnitId = superUnitId;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

}
