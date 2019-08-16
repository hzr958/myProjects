package com.smate.center.task.model.pdwh.pub;

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
 * 基准库成果分享记录表
 * 
 * @author lhd
 *
 */
@Entity
@Table(name = "PDWH_PUB_SHARE")
public class PdwhPubShare implements Serializable {

  private static final long serialVersionUID = -1511036608885184039L;

  @Id
  @Column(name = "RECORD_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PDWH_PUB_SHARE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long recordId;// 主键
  @Column(name = "SHARE_TITLE_ZH")
  private String shareTitleZh;// 中文分享标题
  @Column(name = "SHARE_TITLE_EN")
  private String shareTitleEn;// 英文分享标题
  @Column(name = "SHARE_PSN_ID")
  private Long sharePsnId;// 分享人员ID
  @Column(name = "SHARE_DATE")
  private Date shareDate;// 分享时间
  @Column(name = "RES_ID")
  private Long resId;// 资源id
  @Column(name = "RES_TYPE")
  private Integer resType;// 资源类型 1:成果

  public Long getRecordId() {
    return recordId;
  }

  public void setRecordId(Long recordId) {
    this.recordId = recordId;
  }

  public String getShareTitleZh() {
    return shareTitleZh;
  }

  public void setShareTitleZh(String shareTitleZh) {
    this.shareTitleZh = shareTitleZh;
  }

  public String getShareTitleEn() {
    return shareTitleEn;
  }

  public void setShareTitleEn(String shareTitleEn) {
    this.shareTitleEn = shareTitleEn;
  }

  public Long getSharePsnId() {
    return sharePsnId;
  }

  public void setSharePsnId(Long sharePsnId) {
    this.sharePsnId = sharePsnId;
  }

  public Date getShareDate() {
    return shareDate;
  }

  public void setShareDate(Date shareDate) {
    this.shareDate = shareDate;
  }

  public PdwhPubShare() {}

  public Long getResId() {
    return resId;
  }

  public void setResId(Long resId) {
    this.resId = resId;
  }

  public Integer getResType() {
    return resType;
  }

  public void setResType(Integer resType) {
    this.resType = resType;
  }

}
