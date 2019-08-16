package com.smate.center.batch.model.sns.pub;

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
 * 群组资源收集
 * 
 * @author oyh
 * 
 */
@Entity
@Table(name = "TASK_GROUP_RES_NOTIFY")
public class TaskGroupResNotify implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3065077238163231892L;
  private Long id;
  private Long resKey;

  private Long groupId;

  private Long actionPsnId;
  private Integer resType;
  private Integer status;
  private Date actionDate;
  private Integer nodeId;
  private Integer count = 0;;
  private String locale;

  public TaskGroupResNotify() {
    super();
    // TODO Auto-generated constructor stub
  }

  public TaskGroupResNotify(Long resKey, Long groupId, Long actionPsnId, Integer resType, Integer status,
      Date actionDate, Integer nodeId) {
    super();
    this.resKey = resKey;
    this.groupId = groupId;
    this.actionPsnId = actionPsnId;
    this.resType = resType;
    this.status = status;
    this.actionDate = actionDate;
    this.nodeId = nodeId;
  }

  /**
   * @return the id
   */
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_TASK_GROUP_RES_NOTIFY", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @return the resKey
   */
  @Column(name = "RES_KEY")
  public Long getResKey() {
    return resKey;
  }

  /**
   * @param resKey the resKey to set
   */
  public void setResKey(Long resKey) {
    this.resKey = resKey;
  }

  /**
   * @return the groupId
   */
  @Column(name = "GROUP_ID")
  public Long getGroupId() {
    return groupId;
  }

  /**
   * @param groupId the groupId to set
   */
  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  /**
   * @return the actionPsnId
   */
  @Column(name = "ACTION_PSN_ID")
  public Long getActionPsnId() {
    return actionPsnId;
  }

  /**
   * @param actionPsnId the actionPsnId to set
   */
  public void setActionPsnId(Long actionPsnId) {
    this.actionPsnId = actionPsnId;
  }

  /**
   * @return the resType
   */
  @Column(name = "RES_TYPE")
  public Integer getResType() {
    return resType;
  }

  /**
   * @param resType the resType to set
   */
  public void setResType(Integer resType) {
    this.resType = resType;
  }

  /**
   * @return the status
   */
  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(Integer status) {
    this.status = status;
  }

  /**
   * @return the actionDate
   */
  @Column(name = "ACTION_DATE")
  public Date getActionDate() {
    return actionDate;
  }

  /**
   * @param actionDate the actionDate to set
   */
  public void setActionDate(Date actionDate) {
    this.actionDate = actionDate;
  }

  /**
   * @return the nodeId
   */
  @Column(name = "NODE_ID")
  public Integer getNodeId() {
    return nodeId;
  }

  /**
   * @param nodeId the nodeId to set
   */
  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  /**
   * @return the count
   */
  @Column(name = "COUNT")
  public Integer getCount() {
    return count;
  }

  /**
   * @param count the count to set
   */
  public void setCount(Integer count) {
    this.count = count;
  }

  /**
   * @return the locale
   */
  @Column(name = "LOCALE")
  public String getLocale() {
    return locale;
  }

  /**
   * @param locale the locale to set
   */
  public void setLocale(String locale) {
    this.locale = locale;
  }

}
