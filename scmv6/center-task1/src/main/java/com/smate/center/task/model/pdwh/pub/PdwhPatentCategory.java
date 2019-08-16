package com.smate.center.task.model.pdwh.pub;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 基准库专利分类信息表
 * 
 * @author LIJUN
 *
 */
@Table(name = "PDWH_PATENT_CATEGORY")
@Entity
public class PdwhPatentCategory {
  @Id
  @Column(name = "PUB_ID")
  private Long pubId;
  @Column(name = "CATEGORY")
  private Integer category;// 51,52,53
  @Column(name = "MAIN_CATEGORY_NO")
  private String mainCategoryNo;
  @Column(name = "CATEGORY_NO")
  private String categoryNo;

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Integer getCategory() {
    return category;
  }

  public void setCategory(Integer category) {
    this.category = category;
  }

  public String getMainCategoryNo() {
    return mainCategoryNo;
  }

  public void setMainCategoryNo(String mainCategoryNo) {
    this.mainCategoryNo = mainCategoryNo;
  }

  public String getCategoryNo() {
    return categoryNo;
  }

  public void setCategoryNo(String categoryNo) {
    this.categoryNo = categoryNo;
  }

  public PdwhPatentCategory(Long pubId, Integer category, String mainCategoryNo, String categoryNo) {
    super();
    this.pubId = pubId;
    this.category = category;
    this.mainCategoryNo = mainCategoryNo;
    this.categoryNo = categoryNo;
  }

  public PdwhPatentCategory() {
    super();
  }

}
