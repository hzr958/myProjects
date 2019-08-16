package com.smate.web.institution.model;

import javax.persistence.*;

/**
 * 新兴产业代码
 *
 * @author aijiangbin
 * @create 2019-07-02 14:45
 **/

@Entity
@Table (name = "CONST_CLASSIFY_CSEI")
public class ConstClassifyCsei {


  @Id
  @Column(name = "DIS_CODE")
  private String  code ; // 代码

  @Column(name = "ZH_NAME")
  private String zhName ; //代码中文名


  @Column(name = "SUPER_CODE")
  private String superCode ; //父代码


  @Column(name = "LEV")
  private Integer lev ; //

  @Transient
  private String showCode = "";
  @Transient
  private String showName = "";
  @Transient
  private boolean added; // 是否已经添加

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

  public String getSuperCode() {
    return superCode;
  }

  public void setSuperCode(String superCode) {
    this.superCode = superCode;
  }

  public Integer getLev() {
    return lev;
  }

  public void setLev(Integer lev) {
    this.lev = lev;
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

  public boolean isAdded() {
    return added;
  }

  public void setAdded(boolean added) {
    this.added = added;
  }
}
