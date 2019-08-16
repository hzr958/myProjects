package com.smate.center.task.model.common;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 单位来源表
 * 
 * @author hd
 *
 */
@Entity
@Table(name = "INS_DATA_FROM")
public class InsDataFrom implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3846546879330332880L;
  // 主键
  private InsDataFromId id;
  // 单位id
  private Long insId;
  // 创建日期
  private Date createDate;


  public InsDataFrom() {
    super();
  }

  public InsDataFrom(Long insId, Long orgCode, String token, Date createDate) {
    super();
    this.insId = insId;
    this.id = new InsDataFromId(orgCode, token);
    this.createDate = createDate;
  }

  public InsDataFrom(InsDataFromId id, Date createDate) {
    super();
    this.id = id;
    this.createDate = createDate;
  }

  @Id
  @AttributeOverrides({@AttributeOverride(name = "orgCode", column = @Column(name = "ORG_CODE")),
      @AttributeOverride(name = "token", column = @Column(name = "TOKEN"))})
  public InsDataFromId getId() {
    return id;
  }

  public void setId(InsDataFromId id) {
    this.id = id;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
