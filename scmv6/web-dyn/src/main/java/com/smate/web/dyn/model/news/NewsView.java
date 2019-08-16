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
 * 新闻 访问记录表
 *
 * @author aijiangbin
 * @create 2019-05-15 14:21
 **/
/**
 * @author yhx
 *
 */
@Entity
@Table(name = "V_NEWS_VIEW")
public class NewsView {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_V_NEWS_VIEW", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id; // 主键

  @Column(name = "NEWS_ID")
  private Long newsId; // 新闻主键

  @Column(name = "VIEW_PSN_ID")
  private Long viewPsnId; // 赞人员id

  @Column(name = "IP")
  private String ip = ""; // 访问ip


  @Column(name = "GMT_CREATE")
  private Date gmtCreate; // 创建时间

  @Column(name = "FORMATE_DATE")
  private Long formateDate;// 格式化的日期，方便查询（不带时分秒）

  @Column(name = "TOTAL_COUNT")
  private Long totalCount;// 所有的浏览次数



  public NewsView() {}



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

  public Long getViewPsnId() {
    return viewPsnId;
  }

  public void setViewPsnId(Long viewPsnId) {
    this.viewPsnId = viewPsnId;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public Long getFormateDate() {
    return formateDate;
  }

  public void setFormateDate(Long formateDate) {
    this.formateDate = formateDate;
  }

  public Long getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(Long totalCount) {
    this.totalCount = totalCount;
  }



  public Date getGmtCreate() {
    return gmtCreate;
  }



  public void setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
  }
}
