package com.smate.sie.center.task.model;

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
 * 会议名称自动提示.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PUB_MEETING_NAME")
public class PubMeetingName implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7663418200332641686L;
  // 主键
  private Long code;
  // 会议名称，显示用
  private String name;
  // 添加日期
  private Date createAt;
  // 奖励等级，查询用（全小写）
  private String query;

  @Id
  @Column(name = "AC_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_MEETING_NAME", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getCode() {
    return code;
  }

  public void setCode(Long code) {
    this.code = code;
  }

  @Column(name = "CONF_NAME")
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
