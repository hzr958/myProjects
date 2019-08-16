package com.smate.center.batch.model.psn.register;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * psn_id与sid对应关系.
 * 
 * @author tsz
 * 
 */
@Entity
@Table(name = "PSN_SID")
public class PsnSid implements Serializable {

  private static final long serialVersionUID = 7311504943342496253L;

  private Long psnId;
  private Long sid;

  public PsnSid() {}

  public PsnSid(Long psnId, Long sid) {
    super();
    this.psnId = psnId;
    this.sid = sid;
  }

  @Id
  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "SID")
  public Long getSid() {
    return sid;
  }

  public void setSid(Long sid) {
    this.sid = sid;
  }
}
