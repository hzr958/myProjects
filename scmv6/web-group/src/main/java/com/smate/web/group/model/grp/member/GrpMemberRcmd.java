package com.smate.web.group.model.grp.member;

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
 * 群组成员推荐实体类
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "V_GRP_MEMBER_RCMD")
public class GrpMemberRcmd implements Serializable {

  private static final long serialVersionUID = 4770865880440976052L;
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_V_GRP_RECOMMEND", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;// 主键
  @Column(name = "GRP_ID")
  private Long grpId;// 群组Id
  @Column(name = "RECOMMEND_PSN_ID")
  private Long recommendPsnId;// 被推荐人ID
  @Column(name = "RECOMMEND_TYPE")
  private String recommendType;// 推荐来源
  @Column(name = "SCORE")
  private Integer score;// 推荐得分
  @Column(name = "OPERATION_DATE")
  private Date operationDate;// 操作时间
  @Column(name = "OPERATOR_ID")
  private Long operatorId;// 操作人Id
  @Column(name = "CREATE_DATE")
  private Date createDate;// 创建时间
  @Column(name = "IS_ACCEPT")
  private Integer isAccept;// 是否邀请推荐人员加入群组（2=待邀请,1=已邀请,0=否）

  public GrpMemberRcmd() {}

  public GrpMemberRcmd(Long id, Long recommendPsnId) {
    this.id = id;
    this.recommendPsnId = recommendPsnId;
  }

  public Integer getIsAccept() {
    return isAccept;
  }

  public void setIsAccept(Integer isAccept) {
    this.isAccept = isAccept;
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

  public Long getRecommendPsnId() {
    return recommendPsnId;
  }

  public void setRecommendPsnId(Long recommendPsnId) {
    this.recommendPsnId = recommendPsnId;
  }

  public String getRecommendType() {
    return recommendType;
  }

  public void setRecommendType(String recommendType) {
    this.recommendType = recommendType;
  }

  public Integer getScore() {
    return score;
  }

  public void setScore(Integer score) {
    this.score = score;
  }

  public Date getOperationDate() {
    return operationDate;
  }

  public void setOperationDate(Date operationDate) {
    this.operationDate = operationDate;
  }

  public Long getOperatorId() {
    return operatorId;
  }

  public void setOperatorId(Long operatorId) {
    this.operatorId = operatorId;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

}
