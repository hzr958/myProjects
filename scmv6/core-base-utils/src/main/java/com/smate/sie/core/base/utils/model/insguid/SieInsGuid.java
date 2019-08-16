package com.smate.sie.core.base.utils.model.insguid;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 单位guid对照表.
 * 
 * @author zhangtinggeng
 */
@Entity
@Table(name = "INS_GUID")
public class SieInsGuid implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -2366198609535031592L;

  /**
   * 
   */

  private Long insId;

  private String guid;

  public SieInsGuid() {}

  public SieInsGuid(Long insId, String guid) {
    this.insId = insId;
    this.guid = guid;
  }

  @Id
  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Column(name = "GUID")
  public String getGuid() {
    return guid;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }

}
