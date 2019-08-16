package com.smate.center.task.model.innocity;

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
 * 类说明
 * 
 * @author yxs
 *
 * @since 2016年11月4日
 */
@Entity
@Table(name = "REQUIREMENT")
public class InnoCityRequirement implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = 3559656036298952031L;
  @Id
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_REQUIREMENT", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @Column(name = "ID")
  private Long id; // pk
  @Column(name = "TITLE")
  private String title; // 需求名称
  @Column(name = "CLASSIFY_ID")
  private Long classifyId; // 领域分类
  @Column(name = "EXPIRY_DATE")
  private String expiryDate; // 有效截止日期
  @Column(name = "MONEY")
  private Long money; // 预算经费
  @Column(name = "COOP_WAY")
  private Integer coopWay; // 合作方式，1：合作 2：入股 3：购买
  @Column(name = "PSN_ID")
  private Long psnId; // 发布人
  @Column(name = "INS_ID")
  private Long insId; // 发布单位id
  @Column(name = "INS_NAME")
  private String insName; // 发布单位
  @Column(name = "CREATE_DATE")
  private Date createDate; // 发布时间
  @Column(name = "DESCRIPTION")
  private String description; // 需求描述
  @Column(name = "STATUS")
  private Integer status; // 需求状态,0:需求中 1：已完成 2已取消
  @Column(name = "IMAGE_ID")
  private Long imageId; // 图片id
  @Column(name = "IMAGE_ID_PATH")
  private String imageIdPath; // 图片路径
  @Column(name = "FILE_NAME")
  private String fileName;
  @Column(name = "AUDIT_STATUS")
  private Integer auditStatus;// 审核状态，0：需求审核中 1：审核成功 2：审核拒绝 9 : 需求下架
  @Column(name = "REVIEW_COMMENTS")
  private String comments;// 审核意见
  @Column(name = "AUDIT_DATE")
  private Date auditDate; // 审核日期
  @Column(name = "MATCH_STATUS")
  private Integer matchStatus; // 撮合状态

  @Transient
  private String issuerName;
  @Transient
  private Long collectStatus; // 该需求的征集状态（用于展会征集）

  @Transient
  private String phone; // 联系人电话

  @Transient
  private int commentTotal;// 评论总数

  @Transient
  private int pageNum;// 分页每条数据位置

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public Date getAuditDate() {
    return auditDate;
  }

  public void setAuditDate(Date auditDate) {
    this.auditDate = auditDate;
  }

  public Integer getMatchStatus() {
    return matchStatus;
  }

  public void setMatchStatus(Integer matchStatus) {
    this.matchStatus = matchStatus;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public Integer getAuditStatus() {
    return auditStatus;
  }

  public void setAuditStatus(Integer auditStatus) {
    this.auditStatus = auditStatus;
  }

  // 参加展会联系人名字
  @Transient
  private String conectName;
  @Transient
  private int awardCount;// 赞数量
  @Transient
  private Long connectId; // 需求活动展会对接
  @Transient
  private String des3Id; // 需求加密id

  public int getPageNum() {
    return pageNum;
  }

  public void setPageNum(int pageNum) {
    this.pageNum = pageNum;
  }

  public String getConectName() {
    return conectName;
  }

  public void setConectName(String conectName) {
    this.conectName = conectName;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Long getClassifyId() {
    return classifyId;
  }

  public void setClassifyId(Long classifyId) {
    this.classifyId = classifyId;
  }

  public String getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(String expiryDate) {
    this.expiryDate = expiryDate;
  }

  public Long getMoney() {
    return money;
  }

  public void setMoney(Long money) {
    this.money = money;
  }

  public Integer getCoopWay() {
    return coopWay;
  }

  public void setCoopWay(Integer coopWay) {
    this.coopWay = coopWay;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Long getImageId() {
    return imageId;
  }

  public void setImageId(Long imageId) {
    this.imageId = imageId;
  }

  public String getImageIdPath() {
    return imageIdPath;
  }

  public void setImageIdPath(String imageIdPath) {
    this.imageIdPath = imageIdPath;
  }

  public int getAwardCount() {
    return awardCount;
  }

  public void setAwardCount(int awardCount) {
    this.awardCount = awardCount;
  }

  public Long getConnectId() {
    return connectId;
  }

  public void setConnectId(Long connectId) {
    this.connectId = connectId;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getDes3Id() {
    return des3Id;
  }

  public void setDes3Id(String des3Id) {
    this.des3Id = des3Id;
  }

  public Long getCollectStatus() {
    return collectStatus;
  }

  public void setCollectStatus(Long collectStatus) {
    this.collectStatus = collectStatus;
  }

  public int getCommentTotal() {
    return commentTotal;
  }

  public void setCommentTotal(int commentTotal) {
    this.commentTotal = commentTotal;
  }

  public static void main(String[] args) {

  }

  public String getIssuerName() {
    return issuerName;
  }

  public void setIssuerName(String issuerName) {
    this.issuerName = issuerName;
  }

}
