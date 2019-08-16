package com.smate.web.group.model.grp.pub;

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
 * 群组推荐实体
 * 
 * @author tsz
 *
 */
@Entity
@Table(name = "V_GRP_PUB_RCMD")
public class GrpPubRcmd implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1998662169614237001L;
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_V_GRP_PUB_RCMD", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id; // 主键id
  @Column(name = "GRP_ID")
  private Long grpId; // 群组id
  @Column(name = "PUB_ID")
  private Long pubId; // 成果id 推荐成果id
  @Column(name = "CREATE_DATE")
  private Date createDate; // 推荐日期
  @Column(name = "STATUS")
  private Integer status; // 状态(0 未确认)(1已经确认)(9已经忽略)
  @Column(name = "UPDATE_PSN_ID")
  private Long updatePsnId; // 确认或者忽略操作人员
  @Column(name = "UPDATE_DATE")
  private Date updateDate; // 确认或忽略时间
  @Column(name = "RCMD_TYPE")
  private Integer rcmdType; // 成果推荐来源 (0认领成果)(1推荐成果)
  @Column(name = "PUBLISH_YEAR")
  private Integer publishYear;

  public GrpPubRcmd(Long id, Long grpId, Long pubId, Date createDate, Integer status, Long updatePsnId, Date updateDate,
      Integer rcmdType) {
    super();
    this.id = id;
    this.grpId = grpId;
    this.pubId = pubId;
    this.createDate = createDate;
    this.status = status;
    this.updatePsnId = updatePsnId;
    this.updateDate = updateDate;
    this.rcmdType = rcmdType;
  }

  public GrpPubRcmd() {
    super();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getGrpId() {
    return grpId;
  }

  public void setGrpId(Long grpId) {
    this.grpId = grpId;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Long getUpdatePsnId() {
    return updatePsnId;
  }

  public void setUpdatePsnId(Long updatePsnId) {
    this.updatePsnId = updatePsnId;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public Integer getRcmdType() {
    return rcmdType;
  }

  public void setRcmdType(Integer rcmdType) {
    this.rcmdType = rcmdType;
  }

  public Integer getPublishYear() {
    return publishYear;
  }

  public void setPublishYear(Integer publishYear) {
    this.publishYear = publishYear;
  }

  @Override
  public String toString() {
    return "GrpPubRcmd [id=" + id + ", grpId=" + grpId + ", pubId=" + pubId + ", createDate=" + createDate + ", status="
        + status + ", updatePsnId=" + updatePsnId + ", updateDate=" + updateDate + ", rcmdType=" + rcmdType + "]";
  }

}
