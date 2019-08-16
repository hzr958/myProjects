package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 用户确认成果会议记录.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PSN_PM_CONFERENCE")
public class PsnPmConference implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7018968270941349704L;

  private Long id;
  // 会议名称(小写)
  private String name;
  // 会议名称hash
  private Integer nameHash;
  // 用户ID
  private Long psnId;
  // 用户确认会议次数
  private int ccount = 1;

  public PsnPmConference() {
    super();
  }

  public PsnPmConference(String name, Integer nameHash, Long psnId) {
    super();
    this.name = name;
    this.nameHash = nameHash;
    this.psnId = psnId;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_PM_CONFERENCE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "NAME")
  public String getName() {
    return name;
  }

  @Column(name = "NAME_HASH")
  public Integer getNameHash() {
    return nameHash;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "CCOUNT")
  public int getCcount() {
    return ccount;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setNameHash(Integer nameHash) {
    this.nameHash = nameHash;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setCcount(int ccount) {
    this.ccount = ccount;
  }

}
