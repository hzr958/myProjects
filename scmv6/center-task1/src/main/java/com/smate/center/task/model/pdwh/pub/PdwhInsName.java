package com.smate.center.task.model.pdwh.pub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 基准库机构信息
 * 
 * @author zjh
 *
 */
@Entity
@Table(name = "PDWH_INS_NAME")
public class PdwhInsName implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3924691791359419511L;
  private Long id;
  private Long insId;// 机构id
  private String insName;// 机构名称
  private Integer nameLength;// 名称长度
  private Integer frequence;// 匹配上的次数
  private Date lastUse;

  public PdwhInsName() {
    super();
    // TODO Auto-generated constructor stub
  }

  public PdwhInsName(Long id, Long insId, String insName, Integer nameLength) {
    super();
    this.id = id;
    this.insId = insId;
    this.insName = insName;
    this.nameLength = nameLength;
  }

  @Id
  @Column(name = "Id")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PDWH_INSNAME", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Column(name = "INS_NAME")
  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  @Column(name = "NAME_LENGTH")
  public Integer getNameLength() {
    return nameLength;
  }

  public void setNameLength(Integer nameLength) {
    this.nameLength = nameLength;
  }

  @Column(name = "FREQUENCE")
  public Integer getFrequence() {
    return frequence;
  }

  public void setFrequence(Integer frequence) {
    this.frequence = frequence;
  }

  @Column(name = "LAST_USE")
  public Date getLastUse() {
    return lastUse;
  }

  public void setLastUse(Date lastUse) {
    this.lastUse = lastUse;
  }

}
