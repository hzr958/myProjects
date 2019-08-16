package com.smate.web.dyn.model.news;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 新闻 赞表
 *
 * @author aijiangbin
 * @create 2019-05-15 14:21
 **/
@Entity
@Table(name = "V_NEWS_LIKE")
public class NewsLike {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_V_NEWS_LIKE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id; // 主键

  @Column(name = "NEWS_ID")
  private Long newsId; // 新闻主键

  @Column(name = "LIKE_PSN_ID")
  private Long likePsnId; // 赞人员id

  @Column(name = "STATUS")
  private Integer status = 0; // 0 = 没有赞 ； 1=已赞

  @Column(name = "GMT_CREATE")
  private Date gmtCreate; // 创建时间

  @Column(name = "GMT_UPDATE")
  private Date gmtUpdate; // 更新时间


  public NewsLike() {}



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

  public Long getLikePsnId() {
    return likePsnId;
  }

  public void setLikePsnId(Long likePsnId) {
    this.likePsnId = likePsnId;
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

  public Date getGmtUpdate() {
    return gmtUpdate;
  }

  public void setGmtUpdate(Date gmtUpdate) {
    this.gmtUpdate = gmtUpdate;
  }



  public NewsLike(Long id, Long newsId, Long likePsnId, Integer status, Date gmtCreate, Date gmtUpdate) {
    super();
    this.id = id;
    this.newsId = newsId;
    this.likePsnId = likePsnId;
    this.status = status;
    this.gmtCreate = gmtCreate;
    this.gmtUpdate = gmtUpdate;
  }



  public NewsLike(Long newsId, Long likePsnId, Integer status, Date gmtCreate, Date gmtUpdate) {
    super();
    this.newsId = newsId;
    this.likePsnId = likePsnId;
    this.status = status;
    this.gmtCreate = gmtCreate;
    this.gmtUpdate = gmtUpdate;
  }
}
