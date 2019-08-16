package com.smate.center.batch.model.sns.prj;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 项目相关成果刷新实体.
 * 
 * @author xys
 * 
 */
@Entity
@Table(name = "PRJ_RELATED_PUB_REFRESH")
public class PrjRelatedPubRefresh implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1788907399637803618L;
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PRJ_RELATED_PUB_REFRESH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;
  @Column(name = "PRJ_ID")
  private Long prjId;// 项目id
  @Column(name = "PUB_ID")
  private Long pubId;// 成果id
  @Column(name = "PSN_ID")
  private Long psnId;// 项目/成果所有者id
  @Column(name = "REFRESH_SOURCE")
  private Integer refreshSource;// 刷新来源：1-项目更新，2-成果更新
  @Column(name = "STATUS")
  private Integer status = 0;// 0-待刷新，1-刷新成功，99-刷新失败

  public PrjRelatedPubRefresh() {
    super();
  }

  public PrjRelatedPubRefresh(Long prjId, Long pubId, Long psnId, Integer refreshSource, Integer status) {
    super();
    this.prjId = prjId;
    this.pubId = pubId;
    this.psnId = psnId;
    this.refreshSource = refreshSource;
    this.status = status;
  }

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

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Integer getRefreshSource() {
    return refreshSource;
  }

  public void setRefreshSource(Integer refreshSource) {
    this.refreshSource = refreshSource;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
