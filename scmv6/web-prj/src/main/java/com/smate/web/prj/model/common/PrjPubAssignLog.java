package com.smate.web.prj.model.common;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 项目成果指派记录表
 * 
 * @author yhx
 * @date 2019年8月9日
 *
 */
@Entity
@Table(name = "V_PRJ_PUB_ASSIGN_LOG")
public class PrjPubAssignLog {
  @Id
  @Column(name = "ID")
  private Long id; // 主键id

  @Column(name = "PRJ_ID")
  private Long prjId;// 项目ID

  @Column(name = "PUB_ID")
  private Long pubId; // 成果编号

  @Column(name = "CONFIRM_RESULT", columnDefinition = "INT default 0", nullable = false)
  private Integer confirmResult;// 认领结果 0：未认领，1：已认领，2：拒绝

  @Column(name = "CONFIRM_DATE")
  private Date confirmDate;// 认领时间

  @Column(name = "GMT_CREATE")
  private Date gmtCreate; // 创建日期

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


  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  public Integer getConfirmResult() {
    return confirmResult;
  }

  public void setConfirmResult(Integer confirmResult) {
    this.confirmResult = confirmResult;
  }

  public Date getConfirmDate() {
    return confirmDate;
  }

  public void setConfirmDate(Date confirmDate) {
    this.confirmDate = confirmDate;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public void setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
  }

}
