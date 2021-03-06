package com.smate.center.data.model.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "HADOOP_PRJ_KEYWORD")
public class HadoopPrjKeyword implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_HADOOP_PRJ_KEYWORD", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;
  @Column(name = "KEYWORD")
  private String keyword;
  @Column(name = "COUNTS")
  private Integer counts;
  @Column(name = "APPROVE_CODE")
  private String approveCode;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public Integer getCounts() {
    return counts;
  }

  public void setCounts(Integer counts) {
    this.counts = counts;
  }

  public String getApproveCode() {
    return approveCode;
  }

  public void setApproveCode(String approveCode) {
    this.approveCode = approveCode;
  }

}
