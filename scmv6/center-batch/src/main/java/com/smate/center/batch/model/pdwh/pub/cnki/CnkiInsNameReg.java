package com.smate.center.batch.model.pdwh.pub.cnki;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 单位CNKI检索式正则表达式表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "CNKI_INS_NAME_REG")
public class CnkiInsNameReg implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3168395089368239607L;

  private Long id;
  // 单位ID
  private Long insId;
  // 单位检索式正则表达式
  private String nameReg;
  // 正则表达式的长度
  private Integer regLength;

  public CnkiInsNameReg() {
    super();
  }

  public CnkiInsNameReg(Long id, Long insId, String nameReg, Integer regLength) {
    super();
    this.id = id;
    this.insId = insId;
    this.nameReg = nameReg;
    this.regLength = regLength;
  }

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "NAME_REG")
  public String getNameReg() {
    return nameReg;
  }

  @Column(name = "REG_LENGTH")
  public Integer getRegLength() {
    return regLength;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setNameReg(String nameReg) {
    this.nameReg = nameReg;
  }

  public void setRegLength(Integer regLength) {
    this.regLength = regLength;
  }

}
