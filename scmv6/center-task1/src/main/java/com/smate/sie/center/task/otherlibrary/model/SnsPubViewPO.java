package com.smate.sie.center.task.otherlibrary.model;

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
 * 成果 查看记录
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */
@Entity
@Table(name = "V_PUB_VIEW")
public class SnsPubViewPO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5674446981041616249L;

  @Id
  @SequenceGenerator(name = "V_SEQ_PUB_VIEW", sequenceName = "V_SEQ_PUB_VIEW", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "V_SEQ_PUB_VIEW")
  @Column(name = "ID")
  private Long id; // 主键

  @Column(name = "PUB_ID")
  private Long pubId; // 成果id

  @Column(name = "VIEW_PSN_ID")
  private Long viewPsnId; // 查看人员id

  @Column(name = "IP")
  private String ip; // 访问者IP

  @Column(name = "GMT_CREATE")
  private Date gmtCreate; // 访问时间

  @Column(name = "FORMATE_DATE")
  private Long formateDate;// 格式化的日期，方便查询（不带时分秒）

  @Column(name = "TOTAL_COUNT")
  private Long totalCount;// 所有的浏览次数

  public SnsPubViewPO() {
    super();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Long getViewPsnId() {
    return viewPsnId;
  }

  public void setViewPsnId(Long viewPsnId) {
    this.viewPsnId = viewPsnId;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public void setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
  }

  public Long getFormateDate() {
    return formateDate;
  }

  public void setFormateDate(Long formateDate) {
    this.formateDate = formateDate;
  }

  public Long getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(Long totalCount) {
    this.totalCount = totalCount;
  }

}
