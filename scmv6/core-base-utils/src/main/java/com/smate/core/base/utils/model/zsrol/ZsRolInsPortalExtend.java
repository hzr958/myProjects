package com.smate.core.base.utils.model.zsrol;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 单位域名附表，让每个单位支持更多的域名.
 * 
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Entity
@Table(name = "INS_PORTAL_EXTEND")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ZsRolInsPortalExtend implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1046869200966310880L;

  private Long id;
  // 单位ID
  private Long insId;
  // 单位域名
  private String domain;

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "DOMAIN")
  public String getDomain() {
    return domain;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

}
