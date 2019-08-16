package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 成果分组实体. 分组就是为了区分 相关成果
 * 
 * @author tsz
 * 
 */
@Entity
@Table(name = "pub_grouping")
public class PubGrouping implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8207481818339158324L;

  private Long id; // 主建 id
  private Long pubId; // 成果id
  private Long groupId; // 分组id 主建
  private Long psnId; // 成果所有人

  public PubGrouping() {
    super();
  }

  public PubGrouping(Long pubId, Long groupId) {
    super();
    this.pubId = pubId;
    this.groupId = groupId;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_pub_grouping", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "pub_id")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "group_id")
  public Long getGroupId() {
    return groupId;
  }

  @Column(name = "psn_id")
  public Long getPsnId() {
    return psnId;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

}
