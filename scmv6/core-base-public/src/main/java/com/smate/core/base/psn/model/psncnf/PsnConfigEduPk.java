package com.smate.core.base.psn.model.psncnf;



import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 个人配置：教育经历主键
 * 
 * @author zhuangyanming
 * 
 */
@Embeddable
public class PsnConfigEduPk implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3002058837793675189L;

  // 配置主键
  private Long cnfId;

  // 人员教育经历edu_id
  private Long eduId;

  public PsnConfigEduPk() {}

  public PsnConfigEduPk(Long cnfId) {
    this.cnfId = cnfId;
  }

  public PsnConfigEduPk(Long cnfId, Long eduId) {
    this.cnfId = cnfId;
    this.eduId = eduId;
  }

  @Column(name = "CNF_ID")
  public Long getCnfId() {
    return cnfId;
  }

  @Column(name = "EDU_ID")
  public Long getEduId() {
    return eduId;
  }

  public void setCnfId(Long cnfId) {
    this.cnfId = cnfId;
  }

  public void setEduId(Long eduId) {
    this.eduId = eduId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((cnfId == null) ? 0 : cnfId.hashCode());
    result = prime * result + ((eduId == null) ? 0 : eduId.hashCode());
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
    if (!(obj instanceof PsnConfigEduPk)) {
      return false;
    }
    PsnConfigEduPk other = (PsnConfigEduPk) obj;
    if (cnfId == null) {
      if (other.cnfId != null) {
        return false;
      }
    } else if (!cnfId.equals(other.cnfId)) {
      return false;
    }
    if (eduId == null) {
      if (other.eduId != null) {
        return false;
      }
    } else if (!eduId.equals(other.eduId)) {
      return false;
    }
    return true;
  }

}
