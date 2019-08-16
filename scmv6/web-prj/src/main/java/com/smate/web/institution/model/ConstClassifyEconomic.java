package com.smate.web.institution.model;

import javax.persistence.*;

/**
 * 国民经济行业代码
 *
 * @author aijiangbin
 * @create 2019-07-02 14:45
 **/

@Entity
@Table (name = "CONST_CLASSIFY_ECONOMIC")
public class ConstClassifyEconomic {

  @Id
  @Column(name = "ID")
  private Integer id ;  //  主键

  @Column(name = "CODE")
  private String  code ; // 代码

  @Column(name = "ZH_NAME")
  private String zhName ; //代码中文名

  @Column(name = "EN_NAME")
  private String enEame ; //代码中文名拼音

  @Column(name = "SUPER_CODE")
  private String superCode ; //父代码

  @Column(name = "SUPER_ID")
  private Integer super_id ; //父id

  @Column(name = "LEV")
  private Integer lev ; //

  @Transient
  private String showCode = "";
  @Transient
  private String showName = "";
  @Transient
  private boolean added; // 是否已经添加



  public boolean isAdded() {
    return added;
  }

  public void setAdded(boolean added) {
    this.added = added;
  }

  public String getShowCode() {
    return showCode;
  }

  public void setShowCode(String showCode) {
    this.showCode = showCode;
  }

  public String getShowName() {
    return showName;
  }

  public void setShowName(String showName) {
    this.showName = showName;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getZhName() {
    return zhName;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  public String getEnEame() {
    return enEame;
  }

  public void setEnEame(String enEame) {
    this.enEame = enEame;
  }

  public String getSuperCode() {
    return superCode;
  }

  public void setSuperCode(String superCode) {
    this.superCode = superCode;
  }

  public Integer getSuper_id() {
    return super_id;
  }

  public void setSuper_id(Integer super_id) {
    this.super_id = super_id;
  }

  public Integer getLev() {
    return lev;
  }

  public void setLev(Integer lev) {
    this.lev = lev;
  }
}
