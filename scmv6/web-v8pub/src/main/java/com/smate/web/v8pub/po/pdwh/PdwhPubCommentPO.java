package com.smate.web.v8pub.po.pdwh;

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
 * 基准成果评论
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */

@Entity
@Table(name = "V_PUB_PDWH_COMMENT")
public class PdwhPubCommentPO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6800127904581002366L;

  @Id
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "V_SEQ_PUB_PDWH_COMMENT", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @Column(name = "COMMENT_ID")
  private Long commentId; // 主键

  @Column(name = "PDWH_PUB_ID")
  private Long pdwhPubId; // 成果id

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

  public Long getCommentId() {
    return commentId;
  }

  public void setCommentId(Long commentId) {
    this.commentId = commentId;
  }

  public Long getPdwhPubId() {
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
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

  public Date getGmtModified() {
    return gmtModified;
  }

  public void setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
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
