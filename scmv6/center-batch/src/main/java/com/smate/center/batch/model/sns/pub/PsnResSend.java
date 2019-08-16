package com.smate.center.batch.model.sns.pub;

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
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * cwli资源推荐主表.
 */
@Entity
@Table(name = "PSN_RES_SEND")
public class PsnResSend implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -7083711436790331225L;
  // 主键
  private Long sendId;
  // 推荐发起人
  private Long psnId;
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
  // 推荐理由
  private String recommendReason;
  // 共享状态
  private Integer status;
  // pdwh pub
  private Integer dbid;
  // 向一个或者多个人推荐
  private List<PsnResSendPsn> sendPsnList = new ArrayList<PsnResSendPsn>();
  // 推荐的成果文献
  private List<PsnResSendPubDetail> psnResSendPubDetailList = new ArrayList<PsnResSendPubDetail>();
  // 推荐的资源信息.
  private List<PsnResSendFileDetail> sendFileDetailList = new ArrayList<PsnResSendFileDetail>();

  private List<PsnResSendRes> psnResSendResList = new ArrayList<PsnResSendRes>();
  private boolean isSendEmailTask;

  @Id
  @Column(name = "RES_SEND_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_RES_SEND", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getSendId() {
    return sendId;
  }

  public void setSendId(Long sendId) {
    this.sendId = sendId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
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

  @Column(name = "BATCH_NO")
  public String getBatchNO() {
    return batchNO;
  }

  public void setBatchNO(String batchNO) {
    this.batchNO = batchNO;
  }

  @Column(name = "DEADLINE")
  public Date getDeadline() {
    return deadline;
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

  // @OneToMany(mappedBy = "resSend", cascade = CascadeType.ALL, fetch =
  // FetchType.LAZY)
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "RES_SEND_ID", insertable = true, updatable = true)
  @Fetch(FetchMode.SUBSELECT)
  @OrderBy("receivePsnId ASC")
  public List<PsnResSendPsn> getSendPsnList() {
    return sendPsnList;
  }

  public void setSendPsnList(List<PsnResSendPsn> sendPsnList) {
    this.sendPsnList = sendPsnList;
  }

  @Transient
  public List<PsnResSendPubDetail> getPsnResSendPubDetailList() {
    return psnResSendPubDetailList;
  }

  public void setPsnResSendPubDetailList(List<PsnResSendPubDetail> psnResSendPubDetail) {
    this.psnResSendPubDetailList = psnResSendPubDetail;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Transient
  public List<PsnResSendFileDetail> getSendFileDetailList() {
    return sendFileDetailList;
  }

  public void setSendFileDetailList(List<PsnResSendFileDetail> sendFileDetailList) {
    this.sendFileDetailList = sendFileDetailList;
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

  @Transient
  public Integer getDbid() {
    return dbid;
  }

  public void setDbid(Integer dbid) {
    this.dbid = dbid;
  }

  @Transient
  public boolean isSendEmailTask() {
    return isSendEmailTask;
  }

  public void setSendEmailTask(boolean isSendEmailTask) {
    this.isSendEmailTask = isSendEmailTask;
  }

  @Transient
  public List<PsnResSendRes> getPsnResSendResList() {
    return psnResSendResList;
  }

  public void setPsnResSendResList(List<PsnResSendRes> psnResSendResList) {
    this.psnResSendResList = psnResSendResList;
  }

}
