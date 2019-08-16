package com.smate.sie.core.base.utils.model.prj;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author yxs
 * @descript 项目来源常量
 */
@Entity
@Table(name = "CONST_PRJ_FROM")
public class SiePrjFromConst {
  @Id
  @Column(name = "ID")
  private Long id;
  @Column(name = "NAME_ZH")
  private String zhName;
  @Column(name = "NAME_EN")
  private String enName;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

}
