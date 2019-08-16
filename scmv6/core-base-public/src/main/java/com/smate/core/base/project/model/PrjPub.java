package com.smate.core.base.project.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 项目成果表
 * 
 * @author yhx
 * @date 2019年8月5日
 *
 */
@Entity
@Table(name = "V_PRJ_PUB")
public class PrjPub {
  @Id
  @SequenceGenerator(name = "V_SEQ_PRJ_PUB", sequenceName = "V_SEQ_PRJ_PUB", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "V_SEQ_PRJ_PUB")
  @Column(name = "ID")
  private Long id; // 主键id

  @Column(name = "PRJ_ID")
  private Long prjId;// 项目ID

  @Column(name = "PUB_ID")
  private Long pubId; // 成果编号

  @Column(name = "PUB_FROM")
  private Integer pubFrom;// 成果来源(0基准库 1个人库)

  @Column(name = "STATUS", columnDefinition = "INT default 0", nullable = false)
  private Integer status;// 状态 0: 未删除，1: 已删除

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

  public Integer getPubFrom() {
    return pubFrom;
  }

  public void setPubFrom(Integer pubFrom) {
    this.pubFrom = pubFrom;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public void setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
  }

  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

}
