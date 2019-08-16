package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 复姓.
 * 
 * @author LJ
 * 
 */
@Entity
@Table(name = "CONST_SURNAME")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ConstSurName implements Serializable {

  private static final long serialVersionUID = -4437137769419935183L;

  /**
   * 主键Id
   */
  private int id;
  /**
   * 复姓名称
   */
  private String name;

  @Id
  @Column(name = "ID")
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Column(name = "NAME")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
