package com.smate.center.task.model.sns.psn;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 个人好友系统智能推荐.
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "PSN_FRIEND_RECOMMEND_LOG")
public class FriendSysRecommendLog implements Serializable {

  private static final long serialVersionUID = 8485203851484332882L;
  // pk
  private Long id;
  private Long startPsnId;
  private Long lastPsnId;
  private String workMatch;
  private String eduMatch;
  private String forfMatch;
  private String pubMatch;
  private String prjMatch;
  private String disMatch;
  private String recommendMatch;
  private Date startDate;
  private Date lastDate;

  public FriendSysRecommendLog() {
    super();
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_FRIEND_RECOMMEND", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "START_PSN_ID")
  public Long getStartPsnId() {
    return startPsnId;
  }

  public void setStartPsnId(Long startPsnId) {
    this.startPsnId = startPsnId;
  }

  @Column(name = "LAST_PSN_ID")
  public Long getLastPsnId() {
    return lastPsnId;
  }

  public void setLastPsnId(Long lastPsnId) {
    this.lastPsnId = lastPsnId;
  }

  @Column(name = "WORK_MATCH")
  public String getWorkMatch() {
    return workMatch;
  }

  public void setWorkMatch(String workMatch) {
    this.workMatch = workMatch;
  }

  @Column(name = "EDU_MATCH")
  public String getEduMatch() {
    return eduMatch;
  }

  public void setEduMatch(String eduMatch) {
    this.eduMatch = eduMatch;
  }

  @Column(name = "FORF_MATCH")
  public String getForfMatch() {
    return forfMatch;
  }

  public void setForfMatch(String forfMatch) {
    this.forfMatch = forfMatch;
  }

  @Column(name = "PUB_MATCH")
  public String getPubMatch() {
    return pubMatch;
  }

  public void setPubMatch(String pubMatch) {
    this.pubMatch = pubMatch;
  }

  @Column(name = "PRJ_MATCH")
  public String getPrjMatch() {
    return prjMatch;
  }

  public void setPrjMatch(String prjMatch) {
    this.prjMatch = prjMatch;
  }

  @Column(name = "DIS_MATCH")
  public String getDisMatch() {
    return disMatch;
  }

  public void setDisMatch(String disMatch) {
    this.disMatch = disMatch;
  }

  @Column(name = "RECOMMEND_MATCH")
  public String getRecommendMatch() {
    return recommendMatch;
  }

  public void setRecommendMatch(String recommendMatch) {
    this.recommendMatch = recommendMatch;
  }

  @Column(name = "START_DATE")
  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  @Column(name = "LAST_DATE")
  public Date getLastDate() {
    return lastDate;
  }

  public void setLastDate(Date lastDate) {
    this.lastDate = lastDate;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
