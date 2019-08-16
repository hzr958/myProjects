package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 成果智能指派分数配置.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "SETTING_PUBASSIGN_CNIPRSCORE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SettingPubAssignCniprScore implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1703391035434750166L;
  // 数据库ID
  private Long dbId;
  // 用户名称匹配分数
  private Float name;
  // 用户部门匹配分数
  private Float dept;
  // 用户关键词匹配分数
  private Float kewords;
  // 合作者姓名最大匹配分数
  private Float coname;
  // 成果年份匹配分数
  private Float pubyear;
  // 单位匹配分数
  private Float noMatchInst;
  // 指派阀值
  private Float matchBound;

  @Id
  @Column(name = "DBID")
  public Long getDbId() {
    return dbId;
  }

  @Column(name = "NAME")
  public Float getName() {
    return name;
  }

  public void setName(Float name) {
    this.name = name;
  }

  @Column(name = "DEPT")
  public Float getDept() {
    return dept;
  }

  @Column(name = "KEYWORDS")
  public Float getKewords() {
    return kewords;
  }

  @Column(name = "CONAME")
  public Float getConame() {
    return coname;
  }

  @Column(name = "PUBYEAR")
  public Float getPubyear() {
    return pubyear;
  }

  @Column(name = "NOMATCH_INST")
  public Float getNoMatchInst() {
    return noMatchInst;
  }

  @Column(name = "MATCH_BOUND")
  public Float getMatchBound() {
    return matchBound;
  }

  public void setMatchBound(Float matchBound) {
    this.matchBound = matchBound;
  }

  public void setDbId(Long dbId) {
    this.dbId = dbId;
  }

  public void setDept(Float dept) {
    this.dept = dept;
  }

  public void setKewords(Float kewords) {
    this.kewords = kewords;
  }

  public void setConame(Float coname) {
    this.coname = coname;
  }

  public void setPubyear(Float pubyear) {
    this.pubyear = pubyear;
  }

  public void setNoMatchInst(Float inst) {
    this.noMatchInst = inst;
  }

}
