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
 * CNKI成果匹配-成果指派拆分成果会议名称.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PUB_ASSIGN_CNKICONFERENCE")
public class PubAssignCnkiConference implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -9048119957690201425L;
  private Long id;
  // 会议名称(小写)
  private String name;
  // 名称hash
  private Integer nameHash;
  // 成果ID
  private Long pubId;
  // 成果所属单位ID
  private Long pubInsId;

  public PubAssignCnkiConference() {
    super();
  }

  public PubAssignCnkiConference(String name, Integer nameHash, Long pubId, Long pubInsId) {
    super();
    this.name = name;
    this.nameHash = nameHash;
    this.pubId = pubId;
    this.pubInsId = pubInsId;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_ASSIGN_CNKICONFERENCE", allocationSize = 1)
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

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "PUB_INS_ID")
  public Long getPubInsId() {
    return pubInsId;
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

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setPubInsId(Long pubInsId) {
    this.pubInsId = pubInsId;
  }
}
