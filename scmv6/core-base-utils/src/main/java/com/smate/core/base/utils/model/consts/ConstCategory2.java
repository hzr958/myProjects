package com.smate.core.base.utils.model.consts;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 常量类别.
 * 
 * @author new
 * 
 */
@Entity
@Table(name = "CONST_CATEGORY")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ConstCategory2 implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5834164417692493247L;

  /**
   * 常量类别名.
   */
  private String category;

  /**
   * 说明.
   */
  private String description;
  /**
   * 查询语句.
   */
  private String querySql;

  /**
   * default constuct method.
   */
  public ConstCategory2() {}

  public ConstCategory2(String category, String description) {
    super();
    this.category = category;
    this.description = description;
  }

  /**
   * @return category
   */
  @Id
  @Column(name = "CATEGORY")
  public String getCategory() {
    return category;
  }

  /**
   * @param category
   */
  public void setCategory(String category) {
    this.category = category;
  }

  @Column(name = "DESCRIPTION")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * @return querySql
   */
  @Column(name = "QUERY_SQL")
  public String getQuerySql() {
    return querySql;
  }

  /**
   * @param querySql
   */
  public void setQuerySql(String querySql) {
    this.querySql = querySql;
  }

}
