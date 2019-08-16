package com.smate.center.task.model.rol.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ROL_PUB_ID_TMP")
public class RolPubIdTmp implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3552291175507500785L;
  // 成果编号
  private Long pubId;
  private Integer status;

  @Id
  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Id
  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }
}
