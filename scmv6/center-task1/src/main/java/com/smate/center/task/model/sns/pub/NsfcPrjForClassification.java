package com.smate.center.task.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "NSFC_PRJ_CLASSIFICATION")
public class NsfcPrjForClassification implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = 9014179531371498846L;

  @Id
  @Column(name = "ID")
  private Long id;

  @Column(name = "PRJ_NO")
  private String prjNo;

  @Column(name = "TITLE")
  private String title;

  @Column(name = "NSFC_CATEGORY_CODE")
  private String nsfcCategoryCode;

  @Column(name = "PSN_ID")
  private Long psnId; // 项目负责人

  @Column(name = "STATUS")
  private Integer status;

  public NsfcPrjForClassification() {
    super();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPrjNo() {
    return prjNo;
  }

  public void setPrjNo(String prjNo) {
    this.prjNo = prjNo;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getNsfcCategoryCode() {
    return nsfcCategoryCode;
  }

  public void setNsfcCategoryCode(String nsfcCategoryCode) {
    this.nsfcCategoryCode = nsfcCategoryCode;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

}
