package com.smate.core.base.utils.model.prjform;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author yxs
 * @descript 项目来源实体
 */
@Entity
@Table(name = "CONST_PRJ_FROM")
public class PrjFrom {

  @Id
  @Column(name = "ID")
  private Long id;// 项目来源
  @Column(name = "NAME_ZH")
  private String nameZh;// 中文名称
  @Column(name = "NAME_EN")
  private String nameEn;// 英文名称

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNameZh() {
    return nameZh;
  }

  public void setNameZh(String nameZh) {
    this.nameZh = nameZh;
  }

  public String getNameEn() {
    return nameEn;
  }

  public void setNameEn(String nameEn) {
    this.nameEn = nameEn;
  }

}
