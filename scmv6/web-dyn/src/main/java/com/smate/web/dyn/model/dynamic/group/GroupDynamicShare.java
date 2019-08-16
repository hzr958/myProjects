package com.smate.web.dyn.model.dynamic.group;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 群组动态分享记录
 * 
 * @author ZZX
 *
 */
@Entity
@Table(name = "V_GROUP_DYNAMIC_SHARE")
public class GroupDynamicShare {
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_V_GROUP_DYNAMIC_SHARE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;// 主键id
  @Column(name = "DYN_ID")
  private Long dynId;// 被分享的动态id
  @Column(name = "SHARE_PSN_ID")
  private Long sharePsnId;// 分享的人员id
  @Column(name = "SHARE_CONTENT")
  private String shareContent;// 分享内容
  @Column(name = "SHARE_DATE")
  private Date shareDate;// 分享或者删除分享时间
  @Column(name = "STATUS")
  private Integer status;// 状态 0：分享 1： 删除分享

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getDynId() {
    return dynId;
  }

  public void setDynId(Long dynId) {
    this.dynId = dynId;
  }

  public Long getSharePsnId() {
    return sharePsnId;
  }

  public void setSharePsnId(Long sharePsnId) {
    this.sharePsnId = sharePsnId;
  }

  public String getShareContent() {
    return shareContent;
  }

  public void setShareContent(String shareContent) {
    this.shareContent = shareContent;
  }

  public Date getShareDate() {
    return shareDate;
  }

  public void setShareDate(Date shareDate) {
    this.shareDate = shareDate;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
