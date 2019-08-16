package com.smate.center.task.model.sns.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 关键词分组表
 * 
 * @author zjh
 *
 */
@Entity
@Table(name = "psn_kw_ept_gid")
public class PsnKwEptGid implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -809102002812011450L;
  private Long id;
  private Long psnId;
  private Long gid;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_psn_kw_ept_gid", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "GID")
  public Long getGid() {
    return gid;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setGid(Long gid) {
    this.gid = gid;
  }
}
