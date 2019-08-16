package com.smate.web.management.model.journal;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * cwli基础期刊分类排名表.
 */
@Entity
@Table(name = "BASE_JOURNAL_CATEGORY_RANK")
public class BaseJnlCategoryRank implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -8469623930523445645L;

  // 主键
  private Long id;
  // BASE_JOURNAL_CATEGORY表主键
  private Long jnlCatId;
  // 年份
  private Integer year;
  // 分类中期刊总数
  private Integer count;
  // 期刊排名
  private Integer no;
  // 期刊等级
  private String rank;

  public BaseJnlCategoryRank() {
    super();
  }

  public BaseJnlCategoryRank(Long jnlCatId, Integer year) {
    super();
    this.jnlCatId = jnlCatId;
    this.year = year;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_BASE_JOUNRAL_CATEGORY_RANK", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "JNL_CAT_ID")
  public Long getJnlCatId() {
    return jnlCatId;
  }

  public void setJnlCatId(Long jnlCatId) {
    this.jnlCatId = jnlCatId;
  }

  @Column(name = "YEAR")
  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  @Column(name = "COUNT")
  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  @Column(name = "NO")
  public Integer getNo() {
    return no;
  }

  public void setNo(Integer no) {
    this.no = no;
  }

  @Column(name = "RANK")
  public String getRank() {
    return rank;
  }

  public void setRank(String rank) {
    this.rank = rank;
  }

}
