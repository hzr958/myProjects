package com.smate.web.prj.model.common;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 个人配置：项目主键
 * 
 * @author zhuangyanming
 * 
 */
@Embeddable
public class PsnConfigPrjPk implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8914685174543013931L;

  // 配置主键
  private Long cnfId;

  // 人员项目pub_id
  private Long prjId;

  public PsnConfigPrjPk() {}

  public PsnConfigPrjPk(Long cnfId) {
    this.cnfId = cnfId;
  }

  public PsnConfigPrjPk(Long cnfId, Long prjId) {
    this.cnfId = cnfId;
    this.prjId = prjId;
  }

  @Column(name = "CNF_ID")
  public Long getCnfId() {
    return cnfId;
  }

  @Column(name = "PRJ_ID")
  public Long getPrjId() {
    return prjId;
  }

  public void setCnfId(Long cnfId) {
    this.cnfId = cnfId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((cnfId == null) ? 0 : cnfId.hashCode());
    result = prime * result + ((prjId == null) ? 0 : prjId.hashCode());
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
    if (!(obj instanceof PsnConfigPrjPk)) {
      return false;
    }
    PsnConfigPrjPk other = (PsnConfigPrjPk) obj;
    if (cnfId == null) {
      if (other.cnfId != null) {
        return false;
      }
    } else if (!cnfId.equals(other.cnfId)) {
      return false;
    }
    if (prjId == null) {
      if (other.prjId != null) {
        return false;
      }
    } else if (!prjId.equals(other.prjId)) {
      return false;
    }
    return true;
  }

}
