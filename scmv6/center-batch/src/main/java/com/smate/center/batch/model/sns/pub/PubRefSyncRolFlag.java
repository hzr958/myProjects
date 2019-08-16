package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 文献同步ROL表.
 * 
 * @author WeiLong Peng
 *
 */
@Entity
@Table(name = "PUB_REF_SYNCROL_FLAG")
public class PubRefSyncRolFlag implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5230171275187399477L;

  private Long refId;
  private Integer isDel = 0;
  private Integer flag = 0;

  public PubRefSyncRolFlag() {}

  public PubRefSyncRolFlag(Long refId, Integer isDel, Integer flag) {
    this.refId = refId;
    this.isDel = isDel;
    this.flag = flag;
  }

  @Id
  @Column(name = "REF_ID")
  public Long getRefId() {
    return refId;
  }

  public void setRefId(Long refId) {
    this.refId = refId;
  }

  @Column(name = "DEL")
  public Integer getIsDel() {
    return isDel;
  }

  public void setIsDel(Integer isDel) {
    this.isDel = isDel;
  }

  @Column(name = "FLAG")
  public Integer getFlag() {
    return flag;
  }

  public void setFlag(Integer flag) {
    this.flag = flag;
  }

}
