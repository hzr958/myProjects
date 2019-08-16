package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

/**
 * 参考文献期刊数据(用于阅读的期刊统计).
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PUB_REFC_JOURNAL")
public class PublicationRefcJournal implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -4421953683366597730L;
  // 成果ID
  private Long pubId;
  // 成果所有人
  private Long psnId;
  // ISSN
  private String issn;
  // ISSN_TXT
  private String issnTxt;
  // 期刊名
  private String jname;
  // 成果年份
  private Integer pubYear;
  // 期刊基准库ID
  private Long jnlId;

  public PublicationRefcJournal() {
    super();
  }

  public PublicationRefcJournal(Long pubId, Long psnId, String issn, String jname, Long jnlId, Integer pubYear) {
    super();
    this.pubId = pubId;
    this.psnId = psnId;
    this.issn = issn;
    this.issnTxt = StringUtils.lowerCase(issn);
    this.jname = jname;
    this.jnlId = jnlId;
    this.pubYear = pubYear;
  }

  @Id
  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "ISSN")
  public String getIssn() {
    return issn;
  }

  @Column(name = "JNAME")
  public String getJname() {
    return jname;
  }

  @Column(name = "PUB_YEAR")
  public Integer getPubYear() {
    return pubYear;
  }

  @Column(name = "JNL_ID")
  public Long getJnlId() {
    return jnlId;
  }

  @Column(name = "ISSN_TXT")
  public String getIssnTxt() {
    return issnTxt;
  }

  public void setIssnTxt(String issnTxt) {
    this.issnTxt = issnTxt;
  }

  public void setJnlId(Long jnlId) {
    this.jnlId = jnlId;
  }

  public void setPubYear(Integer pubYear) {
    this.pubYear = pubYear;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setIssn(String issn) {
    this.issn = issn;
  }

  public void setJname(String jname) {
    this.jname = jname;
  }

}
