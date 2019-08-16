package com.smate.center.task.model.sns.psn;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 个人获得基金.
 * 
 * @author liangguokeng
 * 
 */
@Entity
@Table(name = "PSN_PRJ_GRANT")
public class PsnPrjGrant implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 5952283261370026172L;

  private Long psnId;

  private int prjRole;// 1=主持,0=参加

  private String nameZh;

  private String nameEn;

  private int type;

  @Id
  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Id
  @Column(name = "PRJ_ROLE")
  public int getPrjRole() {
    return prjRole;
  }

  @Id
  @Column(name = "NAME_ZH")
  public String getNameZh() {
    return nameZh;
  }

  @Id
  @Column(name = "NAME_EN")
  public String getNameEn() {
    return nameEn;
  }

  @Id
  @Column(name = "TYPE")
  public int getType() {
    return type;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setPrjRole(int prjRole) {
    this.prjRole = prjRole;
  }

  public void setNameZh(String nameZh) {
    this.nameZh = nameZh;
  }

  public void setNameEn(String nameEn) {
    this.nameEn = nameEn;
  }

  public void setType(int type) {
    this.type = type;
  }

}
