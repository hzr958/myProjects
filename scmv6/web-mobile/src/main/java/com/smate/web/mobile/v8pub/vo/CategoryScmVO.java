package com.smate.web.mobile.v8pub.vo;

import java.io.Serializable;

/**
 * 科技领域VO
 * 
 * @author wsn
 * @date 2018年9月4日
 */
public class CategoryScmVO implements Serializable {

  private static final long serialVersionUID = 5402527447215113908L;
  private Long categoryId; // ID
  private String categoryZh; // 科技领域中文名称
  private String categoryEn; // 科技领域英文名称
  private Long parentCategroyId; // 上一级科技领域ID
  private String des3AreaId; // 加密的科技领域ID

  public CategoryScmVO(Long categoryId, String categoryZh, String categoryEn, Long parentCategroyId,
      String des3AreaId) {
    super();
    this.categoryId = categoryId;
    this.categoryZh = categoryZh;
    this.categoryEn = categoryEn;
    this.parentCategroyId = parentCategroyId;
    this.des3AreaId = des3AreaId;
  }

  public Long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  public String getCategoryZh() {
    return categoryZh;
  }

  public void setCategoryZh(String categoryZh) {
    this.categoryZh = categoryZh;
  }

  public String getCategoryEn() {
    return categoryEn;
  }

  public void setCategoryEn(String categoryEn) {
    this.categoryEn = categoryEn;
  }

  public Long getParentCategroyId() {
    return parentCategroyId;
  }

  public void setParentCategroyId(Long parentCategroyId) {
    this.parentCategroyId = parentCategroyId;
  }

  public String getDes3AreaId() {
    return des3AreaId;
  }

  public void setDes3AreaId(String des3AreaId) {
    this.des3AreaId = des3AreaId;
  }

}
