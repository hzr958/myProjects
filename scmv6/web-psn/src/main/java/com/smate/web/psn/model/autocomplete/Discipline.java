package com.smate.web.psn.model.autocomplete;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 研究领域表
 * 
 * @author zjh
 *
 */
@Entity
@Table(name = "DISCIPLINE")
public class Discipline implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6556210706278046997L;
  /**
   * 
   */

  private Long Id;// id
  private Long topCategoryId;// 一级学科id
  private Long secondCategoryId;// 二级学科id
  private String secondCategoryZhName;// 二级学科中文名
  private String secondCategoryEnName;// 二级学科英文名
  private String topCategoryZhName;// 一级学科中文名
  private String topCategoryEnName;// 一级学科英文名

  public Discipline() {

  }


  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "EM_DISCIPLINE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return Id;
  }

  public void setId(Long id) {
    Id = id;
  }

  @Column(name = "TOP_CATEGORY_ID")
  public Long getTopCategoryId() {
    return topCategoryId;
  }

  public void setTopCategoryId(Long topCategoryId) {
    this.topCategoryId = topCategoryId;
  }

  @Column(name = "SECOND_CATEGORY_ID")
  public Long getSecondCategoryId() {
    return secondCategoryId;
  }

  public void setSecondCategoryId(Long secondCategoryId) {
    this.secondCategoryId = secondCategoryId;
  }

  @Column(name = "SECOND_CATEGORY_ZHNAME")
  public String getSecondCategoryZhName() {
    return secondCategoryZhName;
  }

  public void setSecondCategoryZhName(String secondCategoryZhName) {
    this.secondCategoryZhName = secondCategoryZhName;
  }

  @Column(name = "SECOND_CATEGORY_ENNAME")
  public String getSecondCategoryEnName() {
    return secondCategoryEnName;
  }

  public void setSecondCategoryEnName(String secondCategoryEnName) {
    this.secondCategoryEnName = secondCategoryEnName;
  }

  @Column(name = "TOP_CATEGORY_ZHNAME")
  public String getTopCategoryZhName() {
    return topCategoryZhName;
  }

  public void setTopCategoryZhName(String topCategoryZhName) {
    this.topCategoryZhName = topCategoryZhName;
  }

  @Column(name = "TOP_CATEGORY_ENNAME")
  public String getTopCategoryEnName() {
    return topCategoryEnName;
  }


  public void setTopCategoryEnName(String topCategoryEnName) {
    this.topCategoryEnName = topCategoryEnName;
  }

}
