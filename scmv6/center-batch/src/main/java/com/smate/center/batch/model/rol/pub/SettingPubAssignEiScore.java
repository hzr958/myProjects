package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * EI成果智能指派分数配置.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "SETTING_PUBASSIGN_EISCORE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SettingPubAssignEiScore implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -2416177925802710506L;
  // 数据库ID
  private Long dbId;
  // 用户全称匹配分数
  private Float fullName;
  // 用户email匹配分数
  private Float email;
  // 用户部门匹配分数
  private Float dept;
  // 用户关键词匹配分数
  private Float kewords;
  // 合作者姓名最大匹配分数
  private Float coname;
  // 期刊文章匹配最大分数
  private Float journal;
  // 会议论文最大匹配分数
  private Float conference;
  // 成果年份匹配分数
  private Float pubyear;
  // 机构未匹配上(为空的情况)
  private Float noMatchInst;
  // 作者fullname没有，匹配到init_name的分数
  private Float initFullName;
  // 用户简称称匹配分数
  private Float initName;
  // 用户名称单词匹配上的最大匹配分数
  private Float nameWord;
  // 指派阀值
  private Float matchBound;

  @Id
  @Column(name = "DBID")
  public Long getDbId() {
    return dbId;
  }

  @Column(name = "FULL_NAME")
  public Float getFullName() {
    return fullName;
  }

  public void setFullName(Float fullName) {
    this.fullName = fullName;
  }

  @Column(name = "EMAIL")
  public Float getEmail() {
    return email;
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

  @Column(name = "JOURNAL")
  public Float getJournal() {
    return journal;
  }

  @Column(name = "CONFERENCE")
  public Float getConference() {
    return conference;
  }

  @Column(name = "PUBYEAR")
  public Float getPubyear() {
    return pubyear;
  }

  @Column(name = "NOMATCH_INST")
  public Float getNoMatchInst() {
    return noMatchInst;
  }

  @Column(name = "INIT_NAME")
  public Float getInitName() {
    return initName;
  }

  @Column(name = "NAME_WORD")
  public Float getNameWord() {
    return nameWord;
  }

  @Column(name = "MATCH_BOUND")
  public Float getMatchBound() {
    return matchBound;
  }

  @Column(name = "INITFULL_NAME")
  public Float getInitFullName() {
    return initFullName;
  }

  public void setInitFullName(Float initFullName) {
    this.initFullName = initFullName;
  }

  public void setInitName(Float initName) {
    this.initName = initName;
  }

  public void setNameWord(Float nameWord) {
    this.nameWord = nameWord;
  }

  public void setMatchBound(Float matchBound) {
    this.matchBound = matchBound;
  }

  public void setDbId(Long dbId) {
    this.dbId = dbId;
  }

  public void setEmail(Float email) {
    this.email = email;
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

  public void setJournal(Float journal) {
    this.journal = journal;
  }

  public void setConference(Float conference) {
    this.conference = conference;
  }

  public void setPubyear(Float pubyear) {
    this.pubyear = pubyear;
  }

  public void setNoMatchInst(Float inst) {
    this.noMatchInst = inst;
  }


}
