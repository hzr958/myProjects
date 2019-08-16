package com.smate.web.group.model.group.pub;

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
 * 群组成果实体类
 * 
 * @author tsz
 *
 */
@Entity
@Table(name = "V_GRP_PUBS")
public class GroupPubPO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3677837243542527009L;
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_V_GRP_PUBS", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id; // 主键id
  @Column(name = "GRP_ID")
  private Long grpId; // 群组id
  @Column(name = "PUB_ID")
  private Long pubId; // 成果 id
  @Column(name = "LABELED")
  private Integer labeled; // 是否标注 0成果未标注；1成果已标注；标注即成果资助基金信息与群组基金信息匹配
  @Column(name = "RELEVANCE")
  private Integer relevance;// 相关度 相关度：成果关键词与群组关键词匹配数
  @Column(name = "CREATE_PSN_ID")
  private Long createPsnId; // 创建人id(导入人id)
  @Column(name = "CREATE_DATE")
  private Date createDate; // 创建日期 （导入日期）
  @Column(name = "UPDATE_DATE")
  private Date updateDate; // 更新时间
  @Column(name = "UPDATE_PSN_ID")
  private Long updatePsnId; // 更新人 包括 标记成 修改成果，删除成果
  @Column(name = "IS_PROJECT_PUB")
  private Integer isProjectPub; // 项目成果1 项目文献0
  @Column(name = "STATUS")
  private Integer status; // 状态 ) 状态 : (0正常)(1删除 (只是在群组里面移除) )
  @Column(name = "OWNER_PSN_ID")
  private Long ownerPsnId; // 成果拥有者

  public GroupPubPO() {
    super();
  }

  public GroupPubPO(Long id, Long grpId, Long pubId, Integer labeled, Integer relevance, Long createPsnId,
      Date createDate, Date updateDate, Long updatePsnId, Integer isProjectPub, Integer status, Long ownerPsnId) {
    super();
    this.id = id;
    this.grpId = grpId;
    this.pubId = pubId;
    this.labeled = labeled;
    this.relevance = relevance;
    this.createPsnId = createPsnId;
    this.createDate = createDate;
    this.updateDate = updateDate;
    this.updatePsnId = updatePsnId;
    this.isProjectPub = isProjectPub;
    this.status = status;
    this.ownerPsnId = ownerPsnId;
  }

  public GroupPubPO(Long id, Long grpId, Long pubId, Integer labeled, Integer relevance, Long createPsnId,
      Date createDate, Date updateDate, Integer isProjectPub, Integer status) {
    super();
    this.id = id;
    this.grpId = grpId;
    this.pubId = pubId;
    this.labeled = labeled;
    this.relevance = relevance;
    this.createPsnId = createPsnId;
    this.createDate = createDate;
    this.updateDate = updateDate;
    this.isProjectPub = isProjectPub;
    this.status = status;
  }

  public GroupPubPO(Long pubId, Integer labeled, Integer relevance, Long createPsnId, Date createDate, Date updateDate,
      Long updatePsnId, Integer isProjectPub, Integer status) {
    super();
    this.pubId = pubId;
    this.labeled = labeled;
    this.relevance = relevance;
    this.createPsnId = createPsnId;
    this.createDate = createDate;
    this.updateDate = updateDate;
    this.updatePsnId = updatePsnId;
    this.isProjectPub = isProjectPub;
    this.status = status;
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

  public Integer getLabeled() {
    return labeled;
  }

  public void setLabeled(Integer labeled) {
    this.labeled = labeled;
  }

  public Integer getRelevance() {
    return relevance;
  }

  public void setRelevance(Integer relevance) {
    this.relevance = relevance;
  }

  public Long getCreatePsnId() {
    return createPsnId;
  }

  public void setCreatePsnId(Long createPsnId) {
    this.createPsnId = createPsnId;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public Long getUpdatePsnId() {
    return updatePsnId;
  }

  public void setUpdatePsnId(Long updatePsnId) {
    this.updatePsnId = updatePsnId;
  }

  public Integer getIsProjectPub() {
    return isProjectPub;
  }

  public void setIsProjectPub(Integer isProjectPub) {
    this.isProjectPub = isProjectPub;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Long getOwnerPsnId() {
    return ownerPsnId;
  }

  public void setOwnerPsnId(Long ownerPsnId) {
    this.ownerPsnId = ownerPsnId;
  }

  @Override
  public String toString() {
    return "GroupPubPO [id=" + id + ", grpId=" + grpId + ", pubId=" + pubId + ", labeled=" + labeled + ", relevance="
        + relevance + ", createPsnId=" + createPsnId + ", createDate=" + createDate + ", updateDate=" + updateDate
        + ", updatePsnId=" + updatePsnId + ", isProjectPub=" + isProjectPub + ", status=" + status + ", ownerPsnId="
        + ownerPsnId + "]";
  }

}
