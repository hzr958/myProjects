package com.smate.center.task.model.sns.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
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
  private static final long serialVersionUID = -3255799725690961498L;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_KW_RMC_GROUP", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
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
