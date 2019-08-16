package com.smate.center.task.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "NAME_SPLIT")
public class NameSplit implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -9010911006079397273L;

  @Id
  @Column(name = "PSN_ID")
  private Long psnId;

  @Column(name = "NAME_ZH")
  private String nameZh;

  @Column(name = "FIRST_NAME_ZH")
  private String firstNameZh;

  @Column(name = "LAST_NAME_ZH")
  private String lastNameZh;

  @Column(name = "STATUS")
  private Integer status;

  public NameSplit() {
    super();
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getNameZh() {
    return nameZh;
  }

  public void setNameZh(String nameZh) {
    this.nameZh = nameZh;
  }

  public String getFirstNameZh() {
    return firstNameZh;
  }

  public void setFirstNameZh(String firstNameZh) {
    this.firstNameZh = firstNameZh;
  }

  public String getLastNameZh() {
    return lastNameZh;
  }

  public void setLastNameZh(String lastNameZh) {
    this.lastNameZh = lastNameZh;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
