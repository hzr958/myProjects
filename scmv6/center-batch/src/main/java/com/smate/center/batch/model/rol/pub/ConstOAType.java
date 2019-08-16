package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 开放存储类型.
 * 
 * @author xys
 * 
 */
@Entity
@Table(name = "CONST_OA_TYPE")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class ConstOAType implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8392780173729863159L;

  @Id
  @Column(name = "ID")
  private Long id;// pk
  @Column(name = "ROMEO_COLOUR_ZH")
  private String romeoColourZh;// 开放存储类型（中文）
  @Column(name = "ROMEO_COLOUR_EN")
  private String romeoColourEn;// 开放存储类型（英文）
  @Transient
  private String romeoColour;// 开放存储类型（真实使用的名称）
  @Transient
  private Integer count;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getRomeoColourZh() {
    return romeoColourZh;
  }

  public void setRomeoColourZh(String romeoColourZh) {
    this.romeoColourZh = romeoColourZh;
  }

  public String getRomeoColourEn() {
    return romeoColourEn;
  }

  public void setRomeoColourEn(String romeoColourEn) {
    this.romeoColourEn = romeoColourEn;
  }

  public String getRomeoColour() {
    return romeoColour;
  }

  public void setRomeoColour(String romeoColour) {
    this.romeoColour = romeoColour;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

}
