package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

/**
 * 成果智能指派分数配置.
 * 
 * @author liqinghua
 * 
 */
public class SettingPubAssignScoreWrap implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7566224903567462596L;
  // 数据库ID
  private Long dbId;
  // 用户名称
  private Float name;
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
  // 单位匹配分数
  private Float noMatchInst;
  // 用户简称称匹配分数
  private Float initName;
  // 作者fullname没有，匹配到init_name的分数
  private Float initFullName;
  // 用户名称单词匹配上的最大匹配分数
  private Float nameWord;
  // 指派阀值
  private Float matchBound;

  public SettingPubAssignScoreWrap() {
    super();
  }

  public SettingPubAssignScoreWrap(SettingPubAssignScore score) {
    super();
    this.dbId = score.getDbId();
    this.fullName = score.getFullName();
    this.email = score.getEmail();
    this.dept = score.getDept();
    this.kewords = score.getKewords();
    this.coname = score.getConame();
    this.journal = score.getJournal();
    this.conference = score.getConference();
    this.pubyear = score.getPubyear();
    this.noMatchInst = score.getNoMatchInst();
    this.initName = score.getInitName();
    this.nameWord = score.getNameWord();
    this.matchBound = score.getMatchBound();
    this.initFullName = score.getInitFullName();
  }

  public SettingPubAssignScoreWrap(SettingPubAssignEiScore score) {
    super();
    this.dbId = score.getDbId();
    this.fullName = score.getFullName();
    this.email = score.getEmail();
    this.dept = score.getDept();
    this.kewords = score.getKewords();
    this.coname = score.getConame();
    this.journal = score.getJournal();
    this.conference = score.getConference();
    this.pubyear = score.getPubyear();
    this.noMatchInst = score.getNoMatchInst();
    this.initName = score.getInitName();
    this.nameWord = score.getNameWord();
    this.matchBound = score.getMatchBound();
    this.initFullName = score.getInitFullName();
  }

  public SettingPubAssignScoreWrap(SettingPubAssignCnkiScore score) {
    super();
    this.dbId = score.getDbId();
    this.name = score.getName();
    this.dept = score.getDept();
    this.kewords = score.getKewords();
    this.coname = score.getConame();
    this.journal = score.getJournal();
    this.conference = score.getConference();
    this.pubyear = score.getPubyear();
    this.noMatchInst = score.getNoMatchInst();
    this.matchBound = score.getMatchBound();
  }

  public SettingPubAssignScoreWrap(SettingPubAssignCniprScore score) {
    super();
    this.dbId = score.getDbId();
    this.name = score.getName();
    this.dept = score.getDept();
    this.kewords = score.getKewords();
    this.coname = score.getConame();
    this.pubyear = score.getPubyear();
    this.noMatchInst = score.getNoMatchInst();
    this.matchBound = score.getMatchBound();
  }

  public SettingPubAssignScoreWrap(SettingPubAssignCnkiPatScore score) {
    super();
    this.dbId = score.getDbId();
    this.name = score.getName();
    this.dept = score.getDept();
    this.kewords = score.getKewords();
    this.coname = score.getConame();
    this.pubyear = score.getPubyear();
    this.noMatchInst = score.getNoMatchInst();
    this.matchBound = score.getMatchBound();
  }

  public SettingPubAssignScoreWrap(SettingPubAssignSpsScore score) {
    super();
    this.dbId = score.getDbId();
    this.fullName = score.getFullName();
    this.email = score.getEmail();
    this.dept = score.getDept();
    this.kewords = score.getKewords();
    this.coname = score.getConame();
    this.journal = score.getJournal();
    this.conference = score.getConference();
    this.pubyear = score.getPubyear();
    this.noMatchInst = score.getNoMatchInst();
    this.initName = score.getInitName();
    this.matchBound = score.getMatchBound();
  }

  public SettingPubAssignScoreWrap(SettingPubAssignPubMedScore score) {
    super();
    this.dbId = score.getDbId();
    this.fullName = score.getFullName();
    this.email = score.getEmail();
    this.dept = score.getDept();
    this.kewords = score.getKewords();
    this.coname = score.getConame();
    this.journal = score.getJournal();
    this.conference = score.getConference();
    this.pubyear = score.getPubyear();
    this.noMatchInst = score.getNoMatchInst();
    this.initName = score.getInitName();
    this.nameWord = score.getNameWord();
    this.matchBound = score.getMatchBound();
    this.initFullName = score.getInitFullName();
  }

  public Float getName() {
    return name;
  }

  public void setName(Float name) {
    this.name = name;
  }

  public Long getDbId() {
    return dbId;
  }

  public Float getFullName() {
    return fullName;
  }

  public Float getEmail() {
    return email;
  }

  public Float getDept() {
    return dept;
  }

  public Float getKewords() {
    return kewords;
  }

  public Float getConame() {
    return coname;
  }

  public Float getJournal() {
    return journal;
  }

  public Float getConference() {
    return conference;
  }

  public Float getPubyear() {
    return pubyear;
  }

  public Float getNoMatchInst() {
    return noMatchInst;
  }

  public void setDbId(Long dbId) {
    this.dbId = dbId;
  }

  public void setFullName(Float fullName) {
    this.fullName = fullName;
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

  public Float getInitName() {
    return initName;
  }

  public Float getNameWord() {
    return nameWord;
  }

  public Float getMatchBound() {
    return matchBound;
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

  public Float getInitFullName() {
    return initFullName;
  }

  public void setInitFullName(Float initFullName) {
    this.initFullName = initFullName;
  }

}
