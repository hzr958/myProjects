package com.smate.center.open.model.open;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * psn_id 与 open_id 关系表
 *
 * @author tsz
 *
 */
@Entity
@Table(name = "v_psn_open")
public class PersonOpen {

  @Id
  @Column(name = "psn_id")
  private Long psnId;
  @Column(name = "open_id")
  private Long openId;

  public PersonOpen() {
    super();
  }

  public PersonOpen(Long psnId, Long openId) {
    super();
    this.psnId = psnId;
    this.openId = openId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getOpenId() {
    return openId;
  }

  public void setOpenId(Long openId) {
    this.openId = openId;
  }

}
