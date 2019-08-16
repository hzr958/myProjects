package com.smate.center.batch.model.sns.pub;

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
 * 颁发单位自动提示.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "AC_THESIS_ORG")
public class AcThesisOrg implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1102459410858496417L;
  // 主键
  private Long code;
  // 学位论文颁发单位，显示用
  private String name;
  // 添加日期
  private Date createAt;
  // 颁发单位，查询用（全小写）
  private String query;

  @Id
  @Column(name = "AC_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_AC_THESIS_ORG", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getCode() {
    return code;
  }

  public void setCode(Long code) {
    this.code = code;
  }

  @Column(name = "THESIS_ORG")
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
}
