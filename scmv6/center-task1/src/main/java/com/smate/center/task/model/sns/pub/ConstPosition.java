package com.smate.center.task.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 职务常量.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "CONST_POSITION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ConstPosition implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7385328410412639039L;
  private Long id;
  private String name;
  private String lang;
  private Integer grades;
  private Long seqNo;
  private Long code;// 职称CODE
  private String posType;// 职称级别，分A、B、C、D、O（OTHER）5个等级

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  @Column(name = "NAME")
  public String getName() {
    return name;
  }

  @Column(name = "GRADES")
  public Integer getGrades() {
    return grades;
  }

  @Column(name = "SEQ_NO")
  public Long getSeqNo() {
    return seqNo;
  }

  @Column(name = "CODE")
  public Long getCode() {
    return code;
  }

  public void setSeqNo(Long seqNo) {
    this.seqNo = seqNo;
  }

  public void setCode(Long code) {
    this.code = code;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setGrades(Integer grades) {
    this.grades = grades;
  }

  @Column(name = "POS_TYPE")
  public String getPosType() {
    return posType;
  }

  public void setPosType(String posType) {
    this.posType = posType;
  }

  @Column(name = "LANG")
  public String getLang() {
    return lang;
  }

  public void setLang(String lang) {
    this.lang = lang;
  }

}
