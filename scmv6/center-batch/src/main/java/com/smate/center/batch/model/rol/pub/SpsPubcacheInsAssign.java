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
 * 单位端SCOPUS成果匹配到单位记录.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "SPS_PUBCACHE_INS_ASSIGN")
public class SpsPubcacheInsAssign implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 5584126347204958346L;
  private Long id;
  private Long xmlId;
  private Long pubId;
  private Long insId;
  // 是否重新导入新成果（重复不导入）0\1
  private Integer imported = 1;

  public SpsPubcacheInsAssign() {
    super();
  }

  public SpsPubcacheInsAssign(Long xmlId, Long pubId, Long insId, Integer imported) {
    super();
    this.xmlId = xmlId;
    this.pubId = pubId;
    this.insId = insId;
    this.imported = imported;
  }

  @Id
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_SPS_PUBCACHE_INS_ASSIGN", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @Column(name = "ASSIGN_ID")
  public Long getId() {
    return id;
  }

  @Column(name = "XML_ID")
  public Long getXmlId() {
    return xmlId;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "IMPORTED")
  public Integer getImported() {
    return imported;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setXmlId(Long xmlId) {
    this.xmlId = xmlId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setImported(Integer imported) {
    this.imported = imported;
  }

}
