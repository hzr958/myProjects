package com.smate.sie.core.base.utils.model.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果详情表
 * 
 * @author ZSJ
 *
 * @date 2019年2月1日
 */
@Entity
@Table(name = "PUB_JSON")
public class PubJsonPO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 834528249473785255L;

  public PubJsonPO() {
    super();
  }

  private Long pubId;

  private String pubJson;

  @Id
  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "PUB_JSON")
  public String getPubJson() {
    return pubJson;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setPubJson(String pubJson) {
    this.pubJson = pubJson;
  }

}
