package com.smate.sie.center.task.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 动态信息，资源详情表
 * 
 * @author yxy
 */
@Entity
@Table(name = "DYN_RES_DETAIL")
public class SieDynResDetail {

  @Id
  @Column(name = "DYN_ID")
  private Long dynId;

  @Column(name = "RES_DETAIL")
  private String resDetail;

  public Long getDynId() {
    return dynId;
  }

  public String getResDetail() {
    return resDetail;
  }

  public void setDynId(Long dynId) {
    this.dynId = dynId;
  }

  public void setResDetail(String resDetail) {
    this.resDetail = resDetail;
  }

}
