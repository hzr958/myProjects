package com.smate.core.base.psn.model.psncnf;



import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 个人配置：成果主键
 * 
 * @author zhuangyanming
 * 
 */
@Embeddable
public class PsnConfigPubPk implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6496362015409059418L;

  // 配置主键
  private Long cnfId;

  // 人员成果pub_id
  private Long pubId;

  public PsnConfigPubPk() {}

  public PsnConfigPubPk(Long cnfId) {
    this.cnfId = cnfId;
  }

  public PsnConfigPubPk(Long cnfId, Long pubId) {
    this.cnfId = cnfId;
    this.pubId = pubId;
  }

  @Column(name = "CNF_ID")
  public Long getCnfId() {
    return cnfId;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setCnfId(Long cnfId) {
    this.cnfId = cnfId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((cnfId == null) ? 0 : cnfId.hashCode());
    result = prime * result + ((pubId == null) ? 0 : pubId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof PsnConfigPubPk)) {
      return false;
    }
    PsnConfigPubPk other = (PsnConfigPubPk) obj;
    if (cnfId == null) {
      if (other.cnfId != null) {
        return false;
      }
    } else if (!cnfId.equals(other.cnfId)) {
      return false;
    }
    if (pubId == null) {
      if (other.pubId != null) {
        return false;
      }
    } else if (!pubId.equals(other.pubId)) {
      return false;
    }
    return true;
  }

}
