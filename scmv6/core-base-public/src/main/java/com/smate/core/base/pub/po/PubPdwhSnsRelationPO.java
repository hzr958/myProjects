package com.smate.core.base.pub.po;

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
 * 基准库成果ID和SNS个人库成果ID的关系表
 * 
 * @author YJ 2018年5月31号
 */

@Entity
@Table(name = "V_PUB_PDWH_SNS_RELATION")
public class PubPdwhSnsRelationPO implements Serializable {

  private static final long serialVersionUID = -7913048931771947903L;

  @Id
  @SequenceGenerator(name = "SEQ_PUB_PDWH_SNS_RELATION_ID", sequenceName = "SEQ_PUB_PDWH_SNS_RELATION_ID",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PUB_PDWH_SNS_RELATION_ID")
  @Column(name = "ID")
  private Long id; // 逻辑id，主键

  @Column(name = "SNS_PUB_ID")
  private Long snsPubId; // sns个人库成果id

  @Column(name = "PDWH_PUB_ID")
  private Long pdwhPubId; // 基准库成果id

  @Column(name = "CREATE_DATE")
  private Date createDate;// 关联关系创建时间

  @Column(name = "UPDATE_DATE")
  private Date updateDate;// 关联关系更新时间

  public PubPdwhSnsRelationPO() {
    super();
  }

  public PubPdwhSnsRelationPO(Long snsPubId, Long pdwhPubId) {
    super();
    this.snsPubId = snsPubId;
    this.pdwhPubId = pdwhPubId;
  }

  public PubPdwhSnsRelationPO(Long snsPubId, Long pdwhPubId, Date createDate) {
    super();
    this.snsPubId = snsPubId;
    this.pdwhPubId = pdwhPubId;
    this.createDate = createDate;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getSnsPubId() {
    return snsPubId;
  }

  public void setSnsPubId(Long snsPubId) {
    this.snsPubId = snsPubId;
  }

  public Long getPdwhPubId() {
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
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

  @Override
  public String toString() {
    return "PubPdwhSnsRelationPO{" + "id='" + id + '\'' + ", snsPubId='" + snsPubId + '\'' + ", pdwhPubId='" + pdwhPubId
        + '\'' + '}';
  }


}
