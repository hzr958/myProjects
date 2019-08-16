package com.smate.web.management.model.journal;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 应用-期刊推荐.
 * 
 * @author cwli
 * 
 */
@Entity
@Table(name = "PSN_APP_JNL_TEMP_SCORE")
public class PsnCommendJnlScore implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -2930835479923809292L;
  private Long id;
  private Long psnId;
  // 基础期刊jid
  private Long jnlId;
  // 关键词
  private Integer keyScore;
  // 同事(部门相同)发表过
  private Integer workScore;
  // 合作者(成果，项目)发表过
  private Integer coopScore;
  // 同学(博士)发表过
  private Integer eduScore;
  // 人员最高影响因子
  private Integer ifScore;
  // 期刊总数分
  private Integer totaiScore;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_APP_COMMEND_SCORE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "JNL_ID")
  public Long getJnlId() {
    return jnlId;
  }

  public void setJnlId(Long jnlId) {
    this.jnlId = jnlId;
  }

  @Column(name = "KEY_SCORE")
  public Integer getKeyScore() {
    return keyScore;
  }

  public void setKeyScore(Integer keyScore) {
    this.keyScore = keyScore;
  }

  @Column(name = "WORK_SCORE")
  public Integer getWorkScore() {
    return workScore;
  }

  public void setWorkScore(Integer workScore) {
    this.workScore = workScore;
  }

  @Column(name = "COOP_SCORE")
  public Integer getCoopScore() {
    return coopScore;
  }

  public void setCoopScore(Integer coopScore) {
    this.coopScore = coopScore;
  }

  @Column(name = "EDU_SCORE")
  public Integer getEduScore() {
    return eduScore;
  }

  public void setEduScore(Integer eduScore) {
    this.eduScore = eduScore;
  }

  @Column(name = "IF_SCORE")
  public Integer getIfScore() {
    return ifScore;
  }

  public void setIfScore(Integer ifScore) {
    this.ifScore = ifScore;
  }

  @Column(name = "TOTAI_SCORE")
  public Integer getTotaiScore() {
    return totaiScore;
  }

  public void setTotaiScore(Integer totaiScore) {
    this.totaiScore = totaiScore;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((jnlId == null) ? 0 : jnlId.hashCode());
    result = prime * result + ((psnId == null) ? 0 : psnId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    PsnCommendJnlScore other = (PsnCommendJnlScore) obj;
    if (jnlId == null) {
      if (other.jnlId != null)
        return false;
    } else if (!jnlId.equals(other.jnlId))
      return false;
    if (psnId == null) {
      if (other.psnId != null)
        return false;
    } else if (!psnId.equals(other.psnId))
      return false;
    return true;
  }

}
