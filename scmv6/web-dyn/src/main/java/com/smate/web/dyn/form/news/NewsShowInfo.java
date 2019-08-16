package com.smate.web.dyn.form.news;

/**
 * 新闻显示对象
 *
 * @author aijiangbin
 * @create 2019-05-15 16:33
 **/
public class NewsShowInfo {
  private Long id;
  private String title;
  private String brief;
  private String image;
  private Integer awardCount = 0;
  private Integer shareCount = 0;
  private Integer viewCount = 0;
  private boolean publish;

  private String gmtCreate;
  private String gmtPublish; // f发布时间
  private String gmtUpdate; // 更新时间
  private String des3NewsId;
  private String content;// 正文
  private Integer isAward = 0; // 是否赞过 1 是 0否
  private Long seqNo;


  public String getGmtUpdate() {
    return gmtUpdate;
  }

  public void setGmtUpdate(String gmtUpdate) {
    this.gmtUpdate = gmtUpdate;
  }

  public String getGmtPublish() {
    return gmtPublish;
  }

  public void setGmtPublish(String gmtPublish) {
    this.gmtPublish = gmtPublish;
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

  public Integer getAwardCount() {
    return awardCount;
  }

  public void setAwardCount(Integer awardCount) {
    this.awardCount = awardCount;
  }

  public Integer getShareCount() {
    return shareCount;
  }

  public void setShareCount(Integer shareCount) {
    this.shareCount = shareCount;
  }

  public Integer getViewCount() {
    return viewCount;
  }

  public void setViewCount(Integer viewCount) {
    this.viewCount = viewCount;
  }

  public String getGmtCreate() {
    return gmtCreate;
  }

  public void setGmtCreate(String gmtCreate) {
    this.gmtCreate = gmtCreate;
  }

  public boolean isPublish() {
    return publish;
  }

  public void setPublish(boolean publish) {
    this.publish = publish;
  }

  public String getDes3NewsId() {
    return des3NewsId;
  }

  public void setDes3NewsId(String des3NewsId) {
    this.des3NewsId = des3NewsId;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Integer getIsAward() {
    return isAward;
  }

  public void setIsAward(Integer isAward) {
    this.isAward = isAward;
  }

  public Long getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Long seqNo) {
    this.seqNo = seqNo;
  }
}
