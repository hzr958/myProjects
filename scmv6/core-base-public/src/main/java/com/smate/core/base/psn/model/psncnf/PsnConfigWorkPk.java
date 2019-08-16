package com.smate.core.base.psn.model.psncnf;



import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 个人配置：工作经历主键
 * 
 * @author zhuangyanming
 * 
 */
@Embeddable
public class PsnConfigWorkPk implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 7973906744117497616L;

  // 配置主键
  private Long cnfId;

  // 人员工作经历work_id
  private Long workId;

  public PsnConfigWorkPk() {}

  public PsnConfigWorkPk(Long cnfId) {
    this.cnfId = cnfId;
  }

  public PsnConfigWorkPk(Long cnfId, Long workId) {
    this.cnfId = cnfId;
    this.workId = workId;
  }

  @Column(name = "CNF_ID")
  public Long getCnfId() {
    return cnfId;
  }

  @Column(name = "WORK_ID")
  public Long getWorkId() {
    return workId;
  }

  public void setCnfId(Long cnfId) {
    this.cnfId = cnfId;
  }

  public void setWorkId(Long workId) {
    this.workId = workId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((cnfId == null) ? 0 : cnfId.hashCode());
    result = prime * result + ((workId == null) ? 0 : workId.hashCode());
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
    if (!(obj instanceof PsnConfigWorkPk)) {
      return false;
    }
    PsnConfigWorkPk other = (PsnConfigWorkPk) obj;
    if (cnfId == null) {
      if (other.cnfId != null) {
        return false;
      }
    } else if (!cnfId.equals(other.cnfId)) {
      return false;
    }
    if (workId == null) {
      if (other.workId != null) {
        return false;
      }
    } else if (!workId.equals(other.workId)) {
      return false;
    }
    return true;
  }

}
