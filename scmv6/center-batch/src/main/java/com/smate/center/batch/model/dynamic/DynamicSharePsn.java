package com.smate.center.batch.model.dynamic;

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
 * 人员分享详情model.
 * 
 * @author chenxiangrong
 * 
 */
@Entity
@Table(name = "DYNAMIC_SHARE_PSN")
public class DynamicSharePsn implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5054919973300162256L;
  private Long recordId;
  private Long shareId;
  private String shareTitle;
  private String shareEnTitle;
  private Long sharerPsnId;
  private String sharerName;
  private String sharerEnName;
  private String sharerAvatar;
  private Date shareDate;

  @Id
  @Column(name = "RECORD_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_DYNAMIC_SHARE_PSN", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getRecordId() {
    return recordId;
  }

  public void setRecordId(Long recordId) {
    this.recordId = recordId;
  }

  @Column(name = "SHARE_ID")
  public Long getShareId() {
    return shareId;
  }

  public void setShareId(Long shareId) {
    this.shareId = shareId;
  }

  @Column(name = "SHARE_TITLE")
  public String getShareTitle() {
    return shareTitle;
  }

  public void setShareTitle(String shareTitle) {
    this.shareTitle = shareTitle;
  }

  @Column(name = "SHARE_ENTITLE")
  public String getShareEnTitle() {
    return shareEnTitle;
  }

  public void setShareEnTitle(String shareEnTitle) {
    this.shareEnTitle = shareEnTitle;
  }

  @Column(name = "SHARER_PSNID")
  public Long getSharerPsnId() {
    return sharerPsnId;
  }

  public void setSharerPsnId(Long sharerPsnId) {
    this.sharerPsnId = sharerPsnId;
  }

  @Column(name = "SHARER_NAME")
  public String getSharerName() {
    return sharerName;
  }

  public void setSharerName(String sharerName) {
    this.sharerName = sharerName;
  }

  @Column(name = "SHARER_ENNAME")
  public String getSharerEnName() {
    return sharerEnName;
  }

  public void setSharerEnName(String sharerEnName) {
    this.sharerEnName = sharerEnName;
  }

  @Column(name = "SHARER_AVATAR")
  public String getSharerAvatar() {
    return sharerAvatar;
  }

  public void setSharerAvatar(String sharerAvatar) {
    this.sharerAvatar = sharerAvatar;
  }

  @Column(name = "SHARE_DATE")
  public Date getShareDate() {
    return shareDate;
  }

  public void setShareDate(Date shareDate) {
    this.shareDate = shareDate;
  }
}
