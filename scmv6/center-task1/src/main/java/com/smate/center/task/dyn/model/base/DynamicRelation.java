package com.smate.center.task.dyn.model.base;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 动态关系,谁能查看动态
 * 
 * @author zk
 *
 */
@Entity
@Table(name = "V_DYNAMIC_RELATION")
public class DynamicRelation implements Serializable {

  private static final long serialVersionUID = -57223799706786627L;
  DynamicRelationPk id; // 关系联合主键

  @Id
  public DynamicRelationPk getId() {
    return id;
  }

  public void setId(DynamicRelationPk id) {
    this.id = id;
  }

  public Integer dealStatus; // 处理状态

  @Column(name = "DEAL_STATUS")
  public Integer getDealStatus() {
    return dealStatus;
  }

  public void setDealStatus(Integer dealStatus) {
    this.dealStatus = dealStatus;
  }

}
