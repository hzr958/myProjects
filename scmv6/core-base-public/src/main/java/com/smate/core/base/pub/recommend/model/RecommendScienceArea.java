package com.smate.core.base.pub.recommend.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 人员科技领域
 *
 * @author wsn
 * @createTime 2017年3月14日 下午6:01:37
 *
 */

@Entity
@Table(name = "RECOMMEND_SCIENCE_AREA")
public class RecommendScienceArea implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7172583112181040355L;
  private Long id; // 主键
  private Integer scienceAreaId; // 科技领域ID,对应category_map_base
  private String scienceArea; // 科技领域名称
  private Long psnId; // 人员ID
  private String enScienceArea; // 英文科技领域名称
  private Integer areaOrder;// 领域排序
  private Date updateDate;// 更新时间
  private String showScienceArea; // 页面显示的科技领域名称

  public RecommendScienceArea() {
    super();
  }

  public RecommendScienceArea(Integer scienceAreaId, String scienceArea, String enScienceArea) {
    this.scienceAreaId = scienceAreaId;
    this.scienceArea = scienceArea;
    this.enScienceArea = enScienceArea;
  }

  public RecommendScienceArea(Long id, Long psnId, String scienceArea, Integer scienceAreaId) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.scienceArea = scienceArea;
    this.scienceAreaId = scienceAreaId;
    // this.status = status;
  }

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_REC_SCIENCE_AREA", allocationSize = 1)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "SCIENCE_AREA")
  public String getScienceArea() {
    return scienceArea;
  }

  public void setScienceArea(String scienceArea) {
    this.scienceArea = scienceArea;
  }

  @Column(name = "SCIENCE_AREA_ID")
  public Integer getScienceAreaId() {
    return scienceAreaId;
  }

  public void setScienceAreaId(Integer scienceAreaId) {
    this.scienceAreaId = scienceAreaId;
  }

  @Column(name = "SCIENCE_AREA_EN")
  public String getEnScienceArea() {
    return enScienceArea;
  }

  public void setEnScienceArea(String enScienceArea) {
    this.enScienceArea = enScienceArea;
  }

  @Transient
  public String getShowScienceArea() {
    return showScienceArea;
  }

  public void setShowScienceArea(String showScienceArea) {
    this.showScienceArea = showScienceArea;
  }

  @Column(name = "AREAORDER")
  public Integer getAreaOrder() {
    return areaOrder;
  }

  public void setAreaOrder(Integer areaOrder) {
    this.areaOrder = areaOrder;
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

}
