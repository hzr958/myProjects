package com.smate.sie.web.application.model.consts;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 基金委申请代码常量
 * 
 * @author sjzhou
 *
 */
@Entity
@Table(name = "CONST_DISCIPLINE_NSFC")
public class ConstDisciplineNsfc implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8422711375295342505L;
  private Long id;
  private String disCode;
  private String zhName;
  private String enName;
  private String superCode;
  private Long superId;

  public ConstDisciplineNsfc() {
    super();
  }

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  @Column(name = "DIS_CODE")
  public String getDisCode() {
    return disCode;
  }

  @Column(name = "ZH_NAME")
  public String getZhName() {
    return zhName;
  }

  @Column(name = "EN_NAME")
  public String getEnName() {
    return enName;
  }

  @Column(name = "SUPER_CODE")
  public String getSuperCode() {
    return superCode;
  }

  @Column(name = "SUPER_ID")
  public Long getSuperId() {
    return superId;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setDisCode(String disCode) {
    this.disCode = disCode;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  public void setSuperCode(String superCode) {
    this.superCode = superCode;
  }

  public void setSuperId(Long superId) {
    this.superId = superId;
  }

}
