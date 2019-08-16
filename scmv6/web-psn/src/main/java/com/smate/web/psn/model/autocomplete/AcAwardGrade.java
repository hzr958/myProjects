package com.smate.web.psn.model.autocomplete;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 奖励等级.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "AC_AWARD_GRADE")
public class AcAwardGrade implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7205334814193969156L;

  // 主键
  private Long code;
  // 奖励等级，显示用
  private String name;
  // 添加日期
  private Date createAt;
  // 奖励等级，查询用（全小写）
  private String query;
  // 序号
  private Integer seqNo;
  // 奖励等级，英文显示用
  private String nameEn;
  // 奖励等级，英文查询用（全小写）
  private String queryEn;


  @Id
  @Column(name = "AC_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_AC_AWARD_GRADE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getCode() {
    return code;
  }

  public void setCode(Long code) {
    this.code = code;
  }

  @Column(name = "AWARD_GRADE")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "CREATE_AT")
  public Date getCreateAt() {
    return createAt;
  }

  public void setCreateAt(Date createAt) {
    this.createAt = createAt;
  }

  @Column(name = "QUERY")
  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  @Column(name = "SEQ_NO")
  public Integer getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  @Column(name = "AWARD_GRADE_EN")
  public String getNameEn() {
    return nameEn;
  }

  public void setNameEn(String nameEn) {
    this.nameEn = nameEn;
  }

  @Column(name = "QUERY_En")
  public String getQueryEn() {
    return queryEn;
  }

  public void setQueryEn(String queryEn) {
    this.queryEn = queryEn;
  }
}
