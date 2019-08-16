package com.smate.web.v8pub.po.pdwh;

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
 * 基准库成果 查看记录
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */
@Entity
@Table(name = "V_PUB_PDWH_VIEW")
public class PdwhPubViewPO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3820791352301288448L;

  @Id
  @SequenceGenerator(name = "V_SEQ_PUB_PDWH_VIEW", sequenceName = "V_SEQ_PUB_PDWH_VIEW", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "V_SEQ_PUB_PDWH_VIEW")
  @Column(name = "ID")
  private Long id; // 主键

  @Column(name = "PDWH_PUB_ID")
  private Long pdwhPubId; // 成果id

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

  public PdwhPubViewPO() {
    super();
  }

  public Long getPdwhPubId() {
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
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
