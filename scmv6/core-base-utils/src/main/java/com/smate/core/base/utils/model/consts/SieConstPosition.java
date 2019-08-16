package com.smate.core.base.utils.model.consts;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * 职务常量.
 * 
 * @author hd
 * 
 */
@Entity
@Table(name = "CONST_POSITION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SieConstPosition implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7385328410412639039L;
  private Long id;
  private String code;// 职称CODE
  private String zhName;
  private String enName;
  private String superCode;
  private Long superId;
  private String posGrades;
  private String name;

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  @Column(name = "CODE")
  public String getCode() {
    return code;
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

  @Column(name = "POS_GRADES")
  public String getPosGrades() {
    return posGrades;
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


  public void setId(Long id) {
    this.id = id;
  }

  public void setCode(String code) {
    this.code = code;
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


  public void setPosGrades(String posGrades) {
    this.posGrades = posGrades;
  }

  public void setName(String name) {
    this.name = name;
  }


}
