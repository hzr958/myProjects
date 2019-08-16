package com.smate.sie.core.base.utils.model.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 专利详情表
 * 
 * @author ZSJ
 *
 * @date 2019年2月1日
 */
@Entity
@Table(name = "PAT_JSON")
public class PatJsonPO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 834528249473785255L;

  public PatJsonPO() {
    super();
  }

  private Long patId;

  private String patJson;

  @Id
  @Column(name = "PAT_ID")
  public Long getPatId() {
    return patId;
  }

  @Column(name = "PAT_JSON")
  public String getPatJson() {
    return patJson;
  }

  public void setPatId(Long patId) {
    this.patId = patId;
  }

  public void setPatJson(String patJson) {
    this.patJson = patJson;
  }

}
