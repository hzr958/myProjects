package com.smate.sie.center.task.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

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
  @Column(name = "STATUS")
  private Integer status;// 处理状态：0-待处理；1-处理成功；9-处理失败
  @Transient
  private Long sieUnitId;// SIE部门ID

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

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Long getSieUnitId() {
    return sieUnitId;
  }

  public void setSieUnitId(Long sieUnitId) {
    this.sieUnitId = sieUnitId;
  }

}
