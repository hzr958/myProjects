package com.smate.center.batch.model.dynamic;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * 机构新闻类
 * 
 * @author hzr
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Entity
@Table(name = "V_INSPG_NEWS")
public class InspgNews implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7318541082748500956L;
  private Long id; // 新闻id
  private Long inspgId; // 机构主页id
  private Long psnId; // 新增用户id
  private String title; // 新闻标题
  private String fromUrl; // 新闻来源
  private Integer type; // 来源类别 0-自己创建或编写 1-导入
  private Date createTime; // 创建时间 yyyy-MM-dd hh:mm:ss
  private Date updateTime; // 更新时间 yyyy-MM-dd hh:mm:ss
  private List<Long> newsSortIdList; // 新闻所性的类别

  public InspgNews() {
    super();
  }

  public InspgNews(Long id, Long inspgId, Long psnId, String title, String fromUrl, Integer type, Date createTime,
      Date updateTime) {
    super();
    this.id = id;
    this.inspgId = inspgId;
    this.psnId = psnId;
    this.title = title;
    this.fromUrl = fromUrl;
    this.type = type;
    this.createTime = createTime;
    this.updateTime = updateTime;
  }


  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "V_SEQ_INSPG_NEWS", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "INSPG_ID")
  public Long getInspgId() {
    return inspgId;
  }

  public void setInspgId(Long inspgId) {
    this.inspgId = inspgId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "TITLE")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Column(name = "FROM_URL")
  public String getFromUrl() {
    return fromUrl;
  }

  public void setFromUrl(String fromUrl) {
    this.fromUrl = fromUrl;
  }

  @Column(name = "TYPE")
  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  @Column(name = "CREATE_TIME")
  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  @Column(name = "UPDATE_TIME")
  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  @Transient
  public List<Long> getNewsSortIdList() {
    return newsSortIdList;
  }

  public void setNewsSortIdList(List<Long> newsSortIdList) {
    this.newsSortIdList = newsSortIdList;
  }

}
