package com.smate.center.task.model.rol.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * PUBMED成果匹配-成果指派拆分成果作者名称.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PUB_ASSIGN_PUBMEDAUTHOR")
public class PubAssignPubMedAuthor implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6702014474579948011L;
  private Long id;
  // 成果ID
  private Long pubId;
  // 成果所属单位ID
  private Long pubInsId;
  // 简写前缀
  private String prefixName;
  // 用户名简称(小写)
  private String initName;
  // 用户名全称(小写)
  private String fullName;
  // 作者匹配到的单位ID，多个只取本单位，无本单位随机选一个
  private Long insId;
  // 用户序号
  private Integer seqNo;

  public PubAssignPubMedAuthor() {
    super();
  }

  public PubAssignPubMedAuthor(Long pubId, Long pubInsId, String prefixName, String initName, String fullName,
      Long insId, Integer seqNo) {
    super();
    this.pubId = pubId;
    this.pubInsId = pubInsId;
    this.prefixName = prefixName;
    this.initName = initName;
    this.fullName = fullName;
    this.insId = insId;
    this.seqNo = seqNo;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_ASSIGN_PUBMEDAUTHOR", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "PUB_INS_ID")
  public Long getPubInsId() {
    return pubInsId;
  }

  @Column(name = "INIT_NAME")
  public String getInitName() {
    return initName;
  }

  @Column(name = "FULL_NAME")
  public String getFullName() {
    return fullName;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "SEQ_NO")
  public Integer getSeqNo() {
    return seqNo;
  }

  @Column(name = "PREFIX_NAME")
  public String getPrefixName() {
    return prefixName;
  }

  public void setPrefixName(String prefixName) {
    this.prefixName = prefixName;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setPubInsId(Long pubInsId) {
    this.pubInsId = pubInsId;
  }

  public void setInitName(String initName) {
    this.initName = initName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }
}
