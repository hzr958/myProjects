package com.smate.web.fund.agency.model;

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
 * 资助机构分享记录
 * 
 * @author wsn
 * @date Nov 13, 2018
 */

@Entity
@Table(name = "V_AGENCY_SHARE")
public class FundAgencyShare implements Serializable {

  private static final long serialVersionUID = -3030664125518447075L;

  private Long shareId; // 主键
  private Long agencyId; // 资助机构ID
  private Long sharePsnId; // 操作分享的人员ID
  private Long receivePsnId; // 分享给联系人时接收的人员ID
  private Long groupId; // 分享到群组时的群组ID
  private Date createDate; // 记录创建时间
  private Integer shareToPlatform; // 分享到哪个地方，1:动态,2:联系人,3:群组,4:微信,5:新浪微博,6:Facebook,7:Linkedin
  private String comments; // 分享留言

  public FundAgencyShare() {
    super();
  }

  public FundAgencyShare(Long shareId, Long agencyId, Long sharePsnId, Long receivePsnId, Long groupId, Date createDate,
      Integer shareToPlatform, String comments) {
    super();
    this.shareId = shareId;
    this.agencyId = agencyId;
    this.sharePsnId = sharePsnId;
    this.receivePsnId = receivePsnId;
    this.groupId = groupId;
    this.createDate = createDate;
    this.shareToPlatform = shareToPlatform;
    this.comments = comments;
  }

  @Id
  @Column(name = "SHARE_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_FUND_AGENCY_SHARE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getShareId() {
    return shareId;
  }

  public void setShareId(Long shareId) {
    this.shareId = shareId;
  }

  @Column(name = "AGENCY_ID")
  public Long getAgencyId() {
    return agencyId;
  }

  public void setAgencyId(Long agencyId) {
    this.agencyId = agencyId;
  }

  @Column(name = "SHARE_PSN_ID")
  public Long getSharePsnId() {
    return sharePsnId;
  }

  public void setSharePsnId(Long sharePsnId) {
    this.sharePsnId = sharePsnId;
  }

  @Column(name = "RECEIVE_PSN_ID")
  public Long getReceivePsnId() {
    return receivePsnId;
  }

  public void setReceivePsnId(Long receivePsnId) {
    this.receivePsnId = receivePsnId;
  }

  @Column(name = "GROUP_ID")
  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  @Column(name = "SHARE_TO_PLATFORM")
  public Integer getShareToPlatform() {
    return shareToPlatform;
  }

  public void setShareToPlatform(Integer shareToPlatform) {
    this.shareToPlatform = shareToPlatform;
  }

  @Column(name = "COMMENTS")
  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

}
