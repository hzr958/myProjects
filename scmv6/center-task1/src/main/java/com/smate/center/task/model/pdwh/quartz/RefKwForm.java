package com.smate.center.task.model.pdwh.quartz;

import java.io.Serializable;
import java.util.List;

/**
 * 给人员推荐基准文献：关键词匹配所用form.
 */

public class RefKwForm implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6215103868445907032L;
  private Long puballId;
  private Long pubId;
  private Integer dbid;
  private Long jnlId;
  private String issn;
  // 关键词匹配次数
  private Integer kwtf;
  // 期刊级别，默认为4级[1,2,3,4]
  private Integer grade;
  // 匹配的关键词
  private List<String> matchKws;
  // 推荐文献的关键词
  private List<String> refKws;

  public RefKwForm() {
    super();
    this.grade = 4;
  }

  public Long getPuballId() {
    return puballId;
  }

  public void setPuballId(Long puballId) {
    this.puballId = puballId;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Integer getDbid() {
    return dbid;
  }

  public void setDbid(Integer dbid) {
    this.dbid = dbid;
  }

  public Long getJnlId() {
    return jnlId;
  }

  public void setJnlId(Long jnlId) {
    this.jnlId = jnlId;
  }

  public String getIssn() {
    return issn;
  }

  public void setIssn(String issn) {
    this.issn = issn;
  }

  public Integer getKwtf() {
    return kwtf;
  }

  public void setKwtf(Integer kwtf) {
    this.kwtf = kwtf;
  }

  public Integer getGrade() {
    return grade;
  }

  public void setGrade(Integer grade) {
    this.grade = grade;
  }

  public List<String> getMatchKws() {
    return matchKws;
  }

  public void setMatchKws(List<String> matchKws) {
    this.matchKws = matchKws;
  }

  public List<String> getRefKws() {
    return refKws;
  }

  public void setRefKws(List<String> refKws) {
    this.refKws = refKws;
  }

}
