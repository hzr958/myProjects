package com.smate.center.task.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * 
 * 基金委项目详细信息，得到人员在基金委的分类
 * 
 * 
 */
@Entity
@Table(name = "PSN_DISCIPLINE_FROM_NSFC")
public class PsnNsfcInfo implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5290909837318500410L;

  @Id
  @Column(name = "PRJ_ID")
  private Long prjId;

  @Column(name = "CTITLE")
  private String zhTitle;

  @Column(name = "ETITLE")
  private String enTitle;

  @Column(name = "PI_PSN_ID")
  private Long psnPmId;

  @Column(name = "PSN_ID")
  private Long psnId;

  @Column(name = "NAME")
  private String name;

  @Column(name = "DIS_NO1")
  private String disNo1;

  @Column(name = "DIS_NO2")
  private String disNo2;

  public PsnNsfcInfo() {
    super();
  }

  public PsnNsfcInfo(Long psnId, String disNo1, String disNo2) {
    super();
    this.psnId = psnId;
    this.disNo1 = disNo1;
    this.disNo2 = disNo2;
  }

  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  public String getZhTitle() {
    return zhTitle;
  }

  public void setZhTitle(String zhTitle) {
    this.zhTitle = zhTitle;
  }

  public String getEnTitle() {
    return enTitle;
  }

  public void setEnTitle(String enTitle) {
    this.enTitle = enTitle;
  }

  public Long getPsnPmId() {
    return psnPmId;
  }

  public void setPsnPmId(Long psnPmId) {
    this.psnPmId = psnPmId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDisNo1() {
    return disNo1;
  }

  public void setDisNo1(String disNo1) {
    this.disNo1 = disNo1;
  }

  public String getDisNo2() {
    return disNo2;
  }

  public void setDisNo2(String disNo2) {
    this.disNo2 = disNo2;
  }

}
