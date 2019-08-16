package com.smate.web.dyn.model.pub;

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
 * 成果评论
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */

@Entity
@Table(name = "V_PUB_COMMENT")
public class PubCommentPO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 5951975180926630577L;

  @Id
  @SequenceGenerator(name = "V_SEQ_PUB_COMMENTS", sequenceName = "V_SEQ_PUB_COMMENTS", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "V_SEQ_PUB_COMMENTS")
  @Column(name = "COMMENT_ID")
  private Long commentId; // 主键

  @Column(name = "PUB_ID")
  private Long pubId; // 成果id

  @Column(name = "PSN_ID")
  private Long psnId; // 评论人员id

  @Column(name = "CONTENT")
  private String content; // 评论内容

  @Column(name = "STATUS")
  private Integer status; // 状态 0=正常 ； 9=删除

  @Column(name = "GMT_CREATE")
  private Date gmtCreate; // 创建/评论时间

  @Column(name = "GMT_MODIFIED")
  private Date gmtModified; // 更新时间

  public PubCommentPO() {
    super();
  }

  public Long getCommentId() {
    return commentId;
  }

  public Date getGmtModified() {
    return gmtModified;
  }

  public void setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
  }

  public void setCommentId(Long commentId) {
    this.commentId = commentId;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public void setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
  }

}
