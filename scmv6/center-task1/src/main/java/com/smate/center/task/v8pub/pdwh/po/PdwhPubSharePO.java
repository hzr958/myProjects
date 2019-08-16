package com.smate.center.task.v8pub.pdwh.po;

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
 * 基准库成果 分享
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */
@Entity
@Table(name = "V_PUB_PDWH_SHARE")
public class PdwhPubSharePO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5193034110349348861L;

  @Id
  @SequenceGenerator(name = "V_SEQ_PUB_PDWH_SHARE", sequenceName = "V_SEQ_PUB_PDWH_SHARE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "V_SEQ_PUB_PDWH_SHARE")
  @Column(name = "SHARE_ID")
  private Long shareId; // 主键

  @Column(name = "PDWH_PUB_ID")
  private Long pdwhPubId; // 成果id

  @Column(name = "PSN_ID")
  private Long psnId; // 分享人员id

  @Column(name = "COMMENTS")
  private String comment; // 分享成果时，的评论

  @Column(name = "PLATFORM")
  private Integer platform; // 分享平台 ； 微信；微博；等 见：SharePlatformEnum

  @Column(name = "STATUS")
  private Integer status; // 状态 0=正常 ； 9=删除

  @Column(name = "GMT_CREATE")
  private Date gmtCreate; // 分享时间

  @Column(name = "GMT_MODIFIED")
  private Date gmtModified; // 更新时间

  @Column(name = "SHARE_PSN_GROUP_ID")
  private Long sharePsnGroupId;// 被分享人或群组Id

  public PdwhPubSharePO() {
    super();
  }

  public Long getShareId() {
    return shareId;
  }

  public void setShareId(Long shareId) {
    this.shareId = shareId;
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

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public Integer getPlatform() {
    return platform;
  }

  public void setPlatform(Integer platform) {
    this.platform = platform;
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

  public Long getSharePsnGroupId() {
    return sharePsnGroupId;
  }

  public void setSharePsnGroupId(Long sharePsnGroupId) {
    this.sharePsnGroupId = sharePsnGroupId;
  }

}
