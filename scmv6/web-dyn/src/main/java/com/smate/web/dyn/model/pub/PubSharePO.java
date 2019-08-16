package com.smate.web.dyn.model.pub;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 成果 分享
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */
@Entity
@Table(name = "V_PUB_SHARE")
public class PubSharePO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3661501843401345396L;

  @Id
  @SequenceGenerator(name = "V_SEQ_PUB_SHARE", sequenceName = "V_SEQ_PUB_SHARE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "V_SEQ_PUB_SHARE")
  @Column(name = "SHARE_ID")
  private Long shareId; // 主键

  @Column(name = "PUB_ID")
  private Long pubId; // 成果id

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

  public PubSharePO() {
    super();
  }

  public Long getShareId() {
    return shareId;
  }

  public void setShareId(Long shareId) {
    this.shareId = shareId;
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

  public Date getGmtModified() {
    return gmtModified;
  }

  public void setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
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

  public Long getSharePsnGroupId() {
    return sharePsnGroupId;
  }

  public void setSharePsnGroupId(Long sharePsnGroupId) {
    this.sharePsnGroupId = sharePsnGroupId;
  }

}
