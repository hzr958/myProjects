package com.smate.sie.core.base.utils.model.prj;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author yxs
 * @descript 项目类别常量
 */
@Entity
@Table(name = "CONST_PRJ_SCHEME")
public class SiePrjSchemeConst {

  @Id
  @Column(name = "SCHEME_ID")
  private Long id;
  @Column(name = "SCHEME_NAME")
  private String zhName;
  @Column(name = "SCHEME_EN_NAME")
  private String enName;
  @Column(name = "PRJ_FROM_ID")
  private Long prjFromId;

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

  public Long getPrjFromId() {
    return prjFromId;
  }

  public void setPrjFromId(Long prjFromId) {
    this.prjFromId = prjFromId;
  }

}
