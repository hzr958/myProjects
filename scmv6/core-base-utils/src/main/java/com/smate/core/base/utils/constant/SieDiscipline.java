package com.smate.core.base.utils.constant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author yxs
 * @descript 学科类
 */
@Entity
@Table(name = "CONST_DISCIPLINE_SMATE")
public class SieDiscipline {

  @Id
  @Column(name = "ID")
  private Long id;
  @Column(name = "DIS_CODE")
  private String disCode;
  @Column(name = "ZH_NAME")
  private String zhName;
  @Column(name = "EN_NAME")
  private String enName;
  @Column(name = "SUPER_CODE")
  private String superCode;
  @Column(name = "SUPER_ID")
  private String superId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDisCode() {
    return disCode;
  }

  public void setDisCode(String disCode) {
    this.disCode = disCode;
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

  public String getSuperCode() {
    return superCode;
  }

  public void setSuperCode(String superCode) {
    this.superCode = superCode;
  }

  public String getSuperId() {
    return superId;
  }

  public void setSuperId(String superId) {
    this.superId = superId;
  }

}
