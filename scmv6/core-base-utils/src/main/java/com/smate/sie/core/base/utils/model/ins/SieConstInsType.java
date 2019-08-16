package com.smate.sie.core.base.utils.model.ins;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 机构类型常量表.
 * 
 * @author xr
 *
 */
@Entity
@Table(name = "CONST_INS_TYPE")
public class SieConstInsType {

  // 机构类型编号
  private Long nature;
  // 中文名
  private String zhName;
  // 英文名
  private String enName;
  // 描述
  private String description;

  public SieConstInsType() {
    super();
  }

  public SieConstInsType(Long nature, String zhName, String enName, String description) {
    super();
    this.nature = nature;
    this.zhName = zhName;
    this.enName = enName;
    this.description = description;
  }

  @Id
  @Column(name = "NATURE")
  public Long getNature() {
    return nature;
  }

  public void setNature(Long nature) {
    this.nature = nature;
  }

  @Column(name = "ZH_NAME")
  public String getZhName() {
    return zhName;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  @Column(name = "EN_NAME")
  public String getEnName() {
    return enName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  @Column(name = "DESCRIPTION")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
