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
 * scopus成果匹配-成果指派拆分成果作者名称.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PUB_ASSIGN_SPSAUTHOR")
public class PubAssignSpsAuthor implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4781256039477991234L;
  private Long id;
  // 成果ID
  private Long pubId;
  // 成果所属单位ID
  private Long pubInsId;
  // name前缀
  private String prefixName;
  // 用户名
  private String name;
  // 作者匹配到的单位ID，多个只取本单位，无本单位随机选一个
  private Long insId;
  // 0：作者fistname只有一个字母，1：作者fistname有多个字母
  private Integer type = 0;
  // 用户序号
  private Integer seqNo;

  public PubAssignSpsAuthor() {
    super();
  }

  public PubAssignSpsAuthor(Long pubId, Long pubInsId, String prefixName, String name, Long insId, Integer seqNo,
      Integer type) {
    super();
    this.pubId = pubId;
    this.pubInsId = pubInsId;
    this.insId = insId;
    this.seqNo = seqNo;
    this.prefixName = prefixName;
    this.name = name;
    this.type = type;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_ASSIGN_SPSAUTHOR", allocationSize = 1)
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

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "NAME")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "SEQ_NO")
  public Integer getSeqNo() {
    return seqNo;
  }

  @Column(name = "TYPE")
  public Integer getType() {
    return type;
  }

  @Column(name = "PREFIX_NAME")
  public String getPrefixName() {
    return prefixName;
  }

  public void setPrefixName(String prefixName) {
    this.prefixName = prefixName;
  }

  public void setType(Integer type) {
    this.type = type;
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

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

}
