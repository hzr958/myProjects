package com.smate.center.task.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * 
 * 人员分类目标表
 * 
 * 
 */
@Entity
@Table(name = "PSN_DISCIPLINE_FROM_NSFC2")
public class PsnDiscNsfc implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 5329359991809606581L;

  @Id
  @Column(name = "PSN_ID")
  private Long psnId;

  @Column(name = "NAME")
  private String name;

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
