package com.smate.core.base.utils.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 统一定义id的entity基类.
 * 
 * 基类统一定义id的属性名称、数据类型、列名映射及生成策略. 子类可重载getId()函数重定义id的列名映射和生成策略.
 * 
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
// JPA Entity基类的标识
@MappedSuperclass
public abstract class IdEntity implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1327422648259385637L;
  private Long id;

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
