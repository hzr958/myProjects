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
 * 访问model
 * 
 * @author zx
 * 
 */
@Entity
@Table(name = "VIST_STATISTICS")
public class VistStatistics implements Serializable {

  private static final long serialVersionUID = 7229914234358641245L;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_VIST_STATISTICS", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;

  @Column(name = "PSN_ID")
  private Long psnId;

  @Column(name = "VIST_PSN_ID")
  private Long vistPsnId;

  // 被访问的东西的主键 访问主页的话就是psnId,成果的话就是pubId,项目的话就是项目Id
  @Column(name = "ACTION_KEY")
  private Long actionKey;

  // 被访问东西的类型 详情请看DynamicConstant.java
  @Column(name = "ACTION_TYPE")
  private Integer actionType;

  // 操作日期
  @Column(name = "CREATE_DATE")
  private Date createDate;

  // 格式化的日期，方便查询（不带时分秒）
  @Column(name = "FORMATE_DATE")
  private Long formateDate;

  @Column(name = "COUNT")
  private Long count;

  // IP地址
  @Column(name = "IP")
  private String ip;

  @Column(name = "PROVINCE_REGION_ID")
  private Long provinceRegionId;

  public VistStatistics() {
    super();
  }

  public VistStatistics(Long psnId, Long formateDate) {
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

  public Long getVistPsnId() {
    return vistPsnId;
  }

  public void setVistPsnId(Long vistPsnId) {
    this.vistPsnId = vistPsnId;
  }

  public Long getActionKey() {
    return actionKey;
  }

  public void setActionKey(Long actionKey) {
    this.actionKey = actionKey;
  }

  public Integer getActionType() {
    return actionType;
  }

  public void setActionType(Integer actionType) {
    this.actionType = actionType;
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

  public Long getCount() {
    return count;
  }

  public void setCount(Long count) {
    this.count = count;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public Long getProvinceRegionId() {
    return provinceRegionId;
  }

  public void setProvinceRegionId(Long provinceRegionId) {
    this.provinceRegionId = provinceRegionId;
  }

}
