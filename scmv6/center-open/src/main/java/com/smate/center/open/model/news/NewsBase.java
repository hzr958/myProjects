package com.smate.center.open.model.news;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 新闻主表
 *
 * @author aijiangbin
 * @create 2019-05-15 14:21
 **/
@Entity
@Table( name = "V_NEWS_BASE")
public class NewsBase {
  private static final long serialVersionUID = 1L;

  @Id
  @Column (name = "ID")
  private Long id ;  //主键

  @Column (name = "TITLE")
  private String title = ""; //标题

  @Column (name = "BRIEF")
  private String brief ="" ; //简介

  @Column (name = "IMAGE")
  private String image = ""; // 图片

  @Column (name = "CONTENT")
  private String content = "" ;  // 正文

  @Column (name = "STATUS")
  private Integer status  = 0;  //  0=未发布； 1=已发布； 9=删除

  @Column (name = "GMT_CREATE")
  private Date gmtCreate  ;   //  创建时间

  @Column (name = "GMT_UPDATE")
  private Date gmtUpdate ;   // 更新时间

  @Column (name = "GMT_PUBLISH")
  private Date gmtPublish ;  //  发布时间

  @Column (name = "HEAT")
  private Integer heat = 0  ; // 热度

  @Column (name = "seq_no")
  private Long  seqNo  ;  //  管理员列表排序 字段

  @Column(name = "CREATE_PSN_ID")
  private Long createPsnId;


  public NewsBase() {
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

  public String getBrief() {
    return brief;
  }

  public void setBrief(String brief) {
    this.brief = brief;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
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

  public Date getGmtUpdate() {
    return gmtUpdate;
  }

  public void setGmtUpdate(Date gmtUpdate) {
    this.gmtUpdate = gmtUpdate;
  }

  public Date getGmtPublish() {
    return gmtPublish;
  }

  public void setGmtPublish(Date gmtPublish) {
    this.gmtPublish = gmtPublish;
  }

  public Integer getHeat() {
    return heat;
  }

  public void setHeat(Integer heat) {
    this.heat = heat;
  }

  public Long getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Long seqNo) {
    this.seqNo = seqNo;
  }

  public Long getCreatePsnId() {
    return createPsnId;
  }



  public void setCreatePsnId(Long createPsnId) {
    this.createPsnId = createPsnId;
  }
}
