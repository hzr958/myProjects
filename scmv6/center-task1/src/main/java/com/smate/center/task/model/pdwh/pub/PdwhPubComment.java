package com.smate.center.task.model.pdwh.pub;

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
 * 成果评论
 * 
 * @author zx
 *
 */
@Entity
@Table(name = "PDWH_PUB_COMMENTS")
public class PdwhPubComment implements Serializable {

  private static final long serialVersionUID = 1335850154662062551L;

  private Long commentsId;// 主键ID
  private Long pubId;// 成果ID
  private Long psnId;// 评论人ID
  private Integer dbId;// 网站ID
  private String commentsContent;// 评论内容
  private Date createDate;// 创建日期
  private String psnName; // 评论人名字
  private String psnAvatars;// 评论人的头像
  private String dateTimes;// 用于页面的一些格式化时间

  public PdwhPubComment() {
    super();
  }

  @Id
  @Column(name = "COMMENT_ID")
  @SequenceGenerator(sequenceName = "SEQ_PDWH_PUB_COMMENTS", name = "SEQ_STORE", allocationSize = 1)
  @GeneratedValue(generator = "SEQ_STORE", strategy = GenerationType.SEQUENCE)
  public Long getCommentsId() {
    return commentsId;
  }

  public void setCommentsId(Long commentsId) {
    this.commentsId = commentsId;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Column(name = "COMMENT_PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "DB_ID")
  public Integer getDbId() {
    return dbId;
  }

  public void setDbId(Integer dbId) {
    this.dbId = dbId;
  }

  @Column(name = "COMMENTS_CONTENT")
  public String getCommentsContent() {
    return commentsContent;
  }

  public void setCommentsContent(String commentsContent) {
    this.commentsContent = commentsContent;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  @Transient
  public String getPsnName() {
    return psnName;
  }

  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  @Transient
  public String getPsnAvatars() {
    return psnAvatars;
  }

  public void setPsnAvatars(String psnAvatars) {
    this.psnAvatars = psnAvatars;
  }

  @Transient
  public String getDateTimes() {
    return dateTimes;
  }

  public void setDateTimes(String dateTimes) {
    this.dateTimes = dateTimes;
  }

}
