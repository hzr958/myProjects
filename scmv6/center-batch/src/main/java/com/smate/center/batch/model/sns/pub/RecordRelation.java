/**
 * 
 */
package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author Administrator
 *
 */
@Entity
@Table(name = "RECORD_REL")
public class RecordRelation implements Serializable {

  /**
   * 记录关系表
   */
  private static final long serialVersionUID = 3567532860728682923L;

  // 关系表的主键
  private long id;
  // 被引用的表的Id
  private long relId;
  // 引用表的Id
  private long inviteId;
  // 应用的类型 默认为0
  private int type;

  public RecordRelation() {
    super();
  }

  public RecordRelation(long relId, long inviteId, int type) {
    this.relId = relId;
    this.inviteId = inviteId;
    this.type = type;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_RECORD_REL", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Column(name = "REL_ID")
  public long getRelId() {
    return relId;
  }

  public void setRelId(long relId) {
    this.relId = relId;
  }

  @Column(name = "INVITE_ID")
  public long getInviteId() {
    return inviteId;
  }

  public void setInviteId(long inviteId) {
    this.inviteId = inviteId;
  }

  @Column(name = "TYPE")
  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }
}
