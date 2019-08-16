package com.smate.center.task.model.pdwh.quartz;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "PUB_CATEGORY_PATENT_TEMP")
public class PubCategoryPatentTemp implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "ADDR_ID")
  private Long addrId;
  @Column(name = "PUB_ID")
  private Long pubId;
  @Column(name = "DB_ID")
  private Long dbId;
  @Column(name = "SCM_CATEGORY_IDS")
  private String scmCategoryIds;
  @Column(name = "CATEGORY_NO")
  private String categoryNo;
  @Column(name = "IPC_CODE")
  private String ipcCode;
  @Column(name = "DEAL_STATUS")
  private Integer dealStatus;
  @Column(name = "CREATE_DATE")
  private Date createDate;


  public Long getDbId() {
    return dbId;
  }

  public void setDbId(Long dbId) {
    this.dbId = dbId;
  }

  public String getScmCategoryIds() {
    return scmCategoryIds;
  }

  public void setScmCategoryIds(String scmCategoryIds) {
    this.scmCategoryIds = scmCategoryIds;
  }

  public Long getAddrId() {
    return addrId;
  }

  public void setAddrId(Long addrId) {
    this.addrId = addrId;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getCategoryNo() {
    return categoryNo;
  }

  public void setCategoryNo(String categoryNo) {
    this.categoryNo = categoryNo;
  }

  public String getIpcCode() {
    return ipcCode;
  }

  public void setIpcCode(String ipcCode) {
    this.ipcCode = ipcCode;
  }

  public Integer getDealStatus() {
    return dealStatus;
  }

  public void setDealStatus(Integer dealStatus) {
    this.dealStatus = dealStatus;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }


}
