package com.smate.center.batch.model.dynamic;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 动态关系联合主键
 * 
 * @author zk
 *
 */
@Embeddable
public class DynamicRelationPk implements Serializable {

  private static final long serialVersionUID = 3730101743946159340L;
  private Long dynId; // 动态id
  private Long receiver; // 动态接收人(谁能查看该动态)

  public DynamicRelationPk() {}

  public DynamicRelationPk(Long dynId, Long receiver) {
    this.dynId = dynId;
    this.receiver = receiver;
  }

  @Column(name = "DYN_ID")
  public Long getDynId() {
    return dynId;
  }

  public void setDynId(Long dynId) {
    this.dynId = dynId;
  }

  @Column(name = "RECEIVER")
  public Long getReceiver() {
    return receiver;
  }

  public void setReceiver(Long receiver) {
    this.receiver = receiver;
  }


}
