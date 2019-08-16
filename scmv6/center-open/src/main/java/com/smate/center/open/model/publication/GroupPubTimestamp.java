package com.smate.center.open.model.publication;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 群组成果时间戳
 * 
 * @author AiJiangBin
 *
 */
@Entity
@Table(name = "V_OPEN_GROUP_PUB_TIMESTAMP")
public class GroupPubTimestamp {
  private Long groupId;
  private Long time_stamp;


  @Id
  @Column(name = "GROUPID")
  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  @Column(name = "TIME_STAMP")
  public Long getTime_stamp() {
    return time_stamp;
  }



  public void setTime_stamp(Long time_stamp) {
    this.time_stamp = time_stamp;
  }

  public GroupPubTimestamp(Long groupId, Long time_stamp) {
    super();
    this.groupId = groupId;
    this.time_stamp = time_stamp;
  }

  public GroupPubTimestamp() {
    super();
  }



}
