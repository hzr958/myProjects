package com.smate.center.batch.model.rol.pub;

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
 * 群组合作情况统计
 * 
 * @author zyx
 *
 */
@Entity
@Table(name = "GROUP_COOPERATION_STATISTICS")
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
public class GroupCooperationStatistics implements Serializable {

  private static final long serialVersionUID = -5236942410359431982L;

  private Long id;
  private Long groupId;
  private Integer nodeId;
  private String groupName;
  private String groupCategory;
  private Date createDate;
  private Integer sumMemebers;
  private Integer sumActivity;
  private Integer sumBiz;
  private Long visitCount;
  private String categoryName;

  public GroupCooperationStatistics() {

  }

  public GroupCooperationStatistics(Long groupId, Integer nodeId) {
    super();
    this.groupId = groupId;
    this.nodeId = nodeId;
  }

  public GroupCooperationStatistics(Long id, Long groupId, Integer nodeId, String groupName, String groupCategory,
      Date createDate, Integer sumMemebers, Integer sumActivity, Integer sumBiz, Long visitCount) {
    super();
    this.id = id;
    this.groupId = groupId;
    this.nodeId = nodeId;
    this.groupName = groupName;
    this.groupCategory = groupCategory;
    this.createDate = createDate;
    this.sumMemebers = sumMemebers;
    this.sumActivity = sumActivity;
    this.sumBiz = sumBiz;
    this.visitCount = visitCount;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_GROUP_COOPERATION_STATIS", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "GROUP_ID")
  public Long getGroupId() {
    return groupId;
  }

  @Column(name = "NODE_ID")
  public Integer getNodeId() {
    return nodeId;
  }

  @Column(name = "GROUP_NAME")
  public String getGroupName() {
    return groupName;
  }

  @Column(name = "GROUP_CATEGORY")
  public String getGroupCategory() {
    return groupCategory;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  @Column(name = "SUM_MEMEBERS")
  public Integer getSumMemebers() {
    return sumMemebers;
  }

  @Column(name = "SUM_ACTIVITY")
  public Integer getSumActivity() {
    return sumActivity;
  }

  @Column(name = "SUM_BIZ")
  public Integer getSumBiz() {
    return sumBiz;
  }

  @Column(name = "VISIT_COUNT")
  public Long getVisitCount() {
    return visitCount;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public void setGroupCategory(String groupCategory) {
    this.groupCategory = groupCategory;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public void setSumMemebers(Integer sumMemebers) {
    this.sumMemebers = sumMemebers;
  }

  public void setSumActivity(Integer sumActivity) {
    this.sumActivity = sumActivity;
  }

  public void setSumBiz(Integer sumBiz) {
    this.sumBiz = sumBiz;
  }

  public void setVisitCount(Long visitCount) {
    this.visitCount = visitCount;
  }

  @Transient
  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

}
