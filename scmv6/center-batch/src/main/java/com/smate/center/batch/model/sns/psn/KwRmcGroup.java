package com.smate.center.batch.model.sns.psn;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 特征关键词分组.
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "PSN_KW_RMC_GROUP")
public class KwRmcGroup implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = 1006597188255769696L;
  @Id
  @Column(name = "ID")
  private Long id;
  @Column(name = "KW_TXT")
  private String kwTxt;
  @Column(name = "GID")
  private Long gid;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getKwTxt() {
    return kwTxt;
  }

  public void setKwTxt(String kwTxt) {
    this.kwTxt = kwTxt;
  }

  public Long getGid() {
    return gid;
  }

  public void setGid(Long gid) {
    this.gid = gid;
  }

}
