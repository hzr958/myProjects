package com.smate.core.base.project.model;

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
 * 项目阅读记录变
 * 
 * @author YJ
 *
 *         2019年8月7日
 */
@Entity
@Table(name = "V_PRJ_VIEW")
public class ProjectView implements Serializable {

  private static final long serialVersionUID = 6718947015995762603L;

  @Id
  @SequenceGenerator(sequenceName = "SEQ_V_PRJ_VIEW", name = "SEQ_STORE", allocationSize = 1)
  @GeneratedValue(generator = "SEQ_STORE", strategy = GenerationType.SEQUENCE)
  @Column(name = "ID")
  private Long id; // 主键

  @Column(name = "PRJ_ID")
  private Long prjId; // 成果id

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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
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
