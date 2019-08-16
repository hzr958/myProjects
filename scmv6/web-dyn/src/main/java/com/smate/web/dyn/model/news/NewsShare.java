package com.smate.web.dyn.model.news;

import javax.persistence.*;
import java.util.Date;

/**
 * 新闻分享
 *
 * @author aijiangbin
 * @create 2019-05-15 14:21
 **/
@Entity
@Table( name = "V_NEWS_SHARE")
public class NewsShare {
  private static final long serialVersionUID = 1L;

  @Id
  @Column (name = "ID")
  @SequenceGenerator (name = "SEQ_STORE", sequenceName = "SEQ_V_NEWS_SHARE", allocationSize = 1)
  @GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id ;  //主键

  @Column (name = "NEWS_ID")
  private Long newsId ;  //新闻主键

  @Column (name = "SHARE_PSN_ID")
  private Long sharePsnId ;  // 分享人员id

  @Column (name = "CONTENT")
  private String content = "" ; // 分享内容

  @Column(name = "STATUS")
  private Integer status; // 状态 0=正常 ； 9=删除

  @Column (name = "PLATFORM")
  private Integer platform  ; //  分享平台 见 SharePlatformEnum

  @Column (name = "BE_SHARED_ID")
  private Long beSharedId ;  // 被分享的主键， 例如 好友，群组

  @Column (name = "GMT_CREATE")
  private Date gmtCreate  ;   //  创建时间

  @Column (name = "GMT_UPDATE")
  private Date gmtUpdate ;   // 更新时间


  public NewsShare() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getNewsId() {
    return newsId;
  }

  public void setNewsId(Long newsId) {
    this.newsId = newsId;
  }

  public Long getSharePsnId() {
    return sharePsnId;
  }

  public void setSharePsnId(Long sharePsnId) {
    this.sharePsnId = sharePsnId;
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

  public Integer getPlatform() {
    return platform;
  }

  public void setPlatform(Integer platform) {
    this.platform = platform;
  }

  public Long getBeSharedId() {
    return beSharedId;
  }

  public void setBeSharedId(Long beSharedId) {
    this.beSharedId = beSharedId;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public void setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
  }

  public Date getGmtUpdate() {
    return gmtUpdate;
  }

  public void setGmtUpdate(Date gmtUpdate) {
    this.gmtUpdate = gmtUpdate;
  }
}
