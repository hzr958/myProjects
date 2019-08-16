package com.smate.web.management.model.journal;

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
 * @author cwli 期刊实体.
 */
@Entity
@Table(name = "JOURNAL")
public class PubJournal implements java.io.Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 5158977602395249380L;
  private Long id;
  /**
   * 期刊中文名.
   */
  private String zhName;
  /**
   * 期刊英文名.
   */
  private String enName;
  /**
   * ISSN.
   */
  private String pissn;
  /**
   * 状态.
   */
  private Long status;

  /**
   * 来源.
   */
  private String fromFlag;
  // 导入时间
  private Date regDate;

  @Id
  @Column(name = "JID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_JOURNAL", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "ZH_NAME")
  public String getZhName() {
    return zhName;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  @Column(name = "EN_NAME")
  public String getEnName() {
    return enName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  @Column(name = "ISSN")
  public String getPissn() {
    return pissn;
  }

  public void setPissn(String pissn) {
    this.pissn = pissn;
  }

  @Column(name = "STATUS")
  public Long getStatus() {
    return status;
  }

  public void setStatus(Long status) {
    this.status = status;
  }

  @Column(name = "FROM_FLAG")
  public String getFromFlag() {
    return fromFlag;
  }

  public void setFromFlag(String fromFlag) {
    this.fromFlag = fromFlag;
  }

  @Column(name = "REG_DATE")
  public Date getRegDate() {
    return regDate;
  }

  public void setRegDate(Date regDate) {
    this.regDate = regDate;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
