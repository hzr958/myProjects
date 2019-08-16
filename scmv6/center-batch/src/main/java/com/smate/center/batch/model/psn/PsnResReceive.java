package com.smate.center.batch.model.psn;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * cwli资源推荐接收主表.
 */
@Entity
@Table(name = "PSN_RES_RECEIVE")
public class PsnResReceive implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -4849968996636917838L;
  // 主键
  private Long resRecId;
  private Long resSendId;
  // 资源接收人
  private Long psnId;
  // 推荐发起人
  private Long sendPsnId;
  // 推资源荐类型， 0：其他，1：成果，2：文献， 3：工作文档；4：项目
  private Integer resType;
  // 推荐标题（邮件以及系统用户提示标题）
  private String title;
  // 对每次推荐，自动生成一个UUID批次号
  private String batchNO;
  // 推荐有效时间
  private Date deadline;
  // 推荐发起时间
  private Date createDate;
  // 推荐同步标识
  private String syncFlag;
  // 接收的资源
  private String commendPsnName;
  // 推荐理由
  private String recommendReason;
  // 共享状态
  private Integer status;
  private List<PsnResReceiveRes> receiveResList = new ArrayList<PsnResReceiveRes>();

  @Id
  @Column(name = "RES_REC_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_RES_RECEIVE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getResRecId() {
    return resRecId;
  }

  public void setResRecId(Long resRecId) {
    this.resRecId = resRecId;
  }

  @Column(name = "RES_SEND_ID")
  public Long getResSendId() {
    return resSendId;
  }

  public void setResSendId(Long resSendId) {
    this.resSendId = resSendId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "SEND_PSN_ID")
  public Long getSendPsnId() {
    return sendPsnId;
  }

  public void setSendPsnId(Long sendPsnId) {
    this.sendPsnId = sendPsnId;
  }

  @Column(name = "RES_TYPE")
  public Integer getResType() {
    return resType;
  }

  public void setResType(Integer resType) {
    this.resType = resType;
  }

  @Column(name = "TITLE")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Column(name = "DEADLINE")
  public Date getDeadline() {
    return deadline;
  }

  @Column(name = "BATCH_NO")
  public String getBatchNO() {
    return batchNO;
  }

  public void setBatchNO(String batchNO) {
    this.batchNO = batchNO;
  }

  public void setDeadline(Date deadline) {
    this.deadline = deadline;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  // @OneToMany(mappedBy = "resReceive", cascade = CascadeType.ALL, fetch =
  // FetchType.LAZY)
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "RES_REC_ID", insertable = true, updatable = true)
  @Fetch(FetchMode.SUBSELECT)
  @OrderBy("resId ASC")
  @JsonIgnore
  public List<PsnResReceiveRes> getReceiveResList() {
    return receiveResList;
  }

  public void setReceiveResList(List<PsnResReceiveRes> receiveResList) {
    this.receiveResList = receiveResList;
  }

  @Column(name = "COMMEND_PSNNAME")
  public String getCommendPsnName() {
    return commendPsnName;
  }

  @Column(name = "SYNC_FLAG")
  public String getSyncFlag() {
    return syncFlag;
  }

  public void setSyncFlag(String syncFlag) {
    this.syncFlag = syncFlag;
  }

  public void setCommendPsnName(String commendPsnName) {
    this.commendPsnName = commendPsnName;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Column(name = "RECOMMEND_REASON")
  public String getRecommendReason() {
    return recommendReason;
  }

  public void setRecommendReason(String recommendReason) {
    this.recommendReason = recommendReason;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
