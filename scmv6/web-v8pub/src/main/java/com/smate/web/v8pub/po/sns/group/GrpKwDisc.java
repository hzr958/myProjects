
package com.smate.web.v8pub.po.sns.group;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 群组 关键词研究领域
 * 
 * @author AiJiangBin
 *
 */

@Entity
@Table(name = "V_GRP_KW_DISC")
public class GrpKwDisc implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1639902215269588565L;

  @Id
  @Column(name = "GRP_ID")
  private Long grpId;// 群组id 主键

  @Column(name = "DISCIPLINES")
  private String disciplines; // 学科领域 ，逗号分隔 ， 记录是常量编码

  @Column(name = "KEYWORDS")
  private String keywords; // 关键词 ，分号分隔

  @Column(name = "FIRST_CATEGORY_ID")
  private Integer firstCategoryId; // 学科 一级领域 ，常量

  @Column(name = "SECOND_CATEGORY_ID")
  private Integer secondCategoryId; // 学科 二级领域 ，，常量

  @Column(name = "NSFC_CATEGORY_ID")
  private String nsfcCategoryId; // 学科代码

  public String getNsfcCategoryId() {
    return nsfcCategoryId;
  }

  public void setNsfcCategoryId(String nsfcCategoryId) {
    this.nsfcCategoryId = nsfcCategoryId;
  }

  public Long getGrpId() {
    return grpId;
  }

  public void setGrpId(Long grpId) {
    this.grpId = grpId;
  }

  public String getDisciplines() {
    return disciplines;
  }

  public void setDisciplines(String disciplines) {
    this.disciplines = disciplines;
  }

  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  public Integer getFirstCategoryId() {
    return firstCategoryId;
  }

  public void setFirstCategoryId(Integer firstCategoryId) {
    this.firstCategoryId = firstCategoryId;
  }

  public Integer getSecondCategoryId() {
    return secondCategoryId;
  }

  public void setSecondCategoryId(Integer secondCategoryId) {
    this.secondCategoryId = secondCategoryId;
  }


}
