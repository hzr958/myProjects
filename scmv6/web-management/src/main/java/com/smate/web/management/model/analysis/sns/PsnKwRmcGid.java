package com.smate.web.management.model.analysis.sns;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 人员特征关键词分组.
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "PSN_KW_RMC_GID")
public class PsnKwRmcGid implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = 8079089110567491821L;
  @Id
  @Column(name = "ID")
  private Long id;
  @Column(name = "PSN_ID")
  private Long psnId;
  @Column(name = "KW_GID")
  private Long gid;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getGid() {
    return gid;
  }

  public void setGid(Long gid) {
    this.gid = gid;
  }

}
