package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 成果智能指派关键词权重.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "SETTING_PUBASSIGN_KEYWT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SettingPubAssignKwWt implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -937706765729451752L;

  private Long id;
  // 临界值，以大于等于该值为边界
  private Integer bound;
  // 权重
  private Float weight;

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  @Column(name = "BOUND")
  public Integer getBound() {
    return bound;
  }

  @Column(name = "WEIGHT")
  public Float getWeight() {
    return weight;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setBound(Integer bound) {
    this.bound = bound;
  }

  public void setWeight(Float weight) {
    this.weight = weight;
  }

}
