package com.smate.web.psn.model.statistics;



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
 * 关注人员统计model
 * 
 * @author Scy
 * 
 */
@Entity
@Table(name = "ATT_STATISTICS")
public class AttentionStatistics implements Serializable {

  private static final long serialVersionUID = 4427971914850041999L;

  // 主键
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_ATT_STATISTICS", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;

  // 关注人
  @Column(name = "PSN_ID")
  private Long psnId;

  // 被关注人
  @Column(name = "ATT_PSN_ID")
  private Long attPsnId;

  // 操作（0：取消关注 1：关注）
  @Column(name = "ACTION")
  private Integer action;

  // 操作日期
  @Column(name = "CREATE_DATE")
  private Date createDate;

  // 格式化的日期，方便查询（不带时分秒）
  @Column(name = "FORMATE_DATE")
  private Long formateDate;

  // IP地址
  @Column(name = "IP")
  private String ip;

  public AttentionStatistics() {
    super();
  }

  public AttentionStatistics(Long psnId, Long formateDate) {
    super();
    this.psnId = psnId;
    this.formateDate = formateDate;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getAttPsnId() {
    return attPsnId;
  }

  public void setAttPsnId(Long attPsnId) {
    this.attPsnId = attPsnId;
  }

  public Integer getAction() {
    return action;
  }

  public void setAction(Integer action) {
    this.action = action;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Long getFormateDate() {
    return formateDate;
  }

  public void setFormateDate(Long formateDate) {
    this.formateDate = formateDate;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

}
