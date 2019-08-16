package com.smate.center.open.model.group.prj;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 项目-群组关系表
 * 
 * @author xieyushou
 * 
 */
@Entity
@Table(name = "PRJ_GROUP_RELATION")
public class PrjGroupRelation implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8956724105057475879L;
  // 主键唯一标识
  private Long id;
  // 项目id
  private Long prjId;
  // 群组id
  private Long groupId;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PRJ_GROUP_RELATION", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PRJ_ID")
  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  @Column(name = "GROUP_ID")
  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

}
