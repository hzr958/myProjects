package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 推荐系统同步人与群组信息
 * 
 * @author zk
 *
 */
@Entity
@Table(name = "RCMD_SYNC_GROUPINFO")
public class RcmdSyncGroupInfo implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8702574326985720630L;
  private Long groupId; // 群组id
  private Long psnId; // 人员id
  private Integer isDel = 0; // 关系,0:新增,1:删除
  private Integer status = 0; // 处理结果 ,0:待处理,9:出错

  public RcmdSyncGroupInfo() {
    super();
  }

  public RcmdSyncGroupInfo(Long groupId, Long psnId, Integer isDel) {
    super();
    this.groupId = groupId;
    this.psnId = psnId;
    this.isDel = isDel;
    this.status = 0;
  }

  @Id
  @Column(name = "group_id")
  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  @Column(name = "psn_id")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "is_del")
  public Integer getIsDel() {
    return isDel;
  }

  public void setIsDel(Integer isDel) {
    this.isDel = isDel;
  }

  @Column(name = "status")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }


}

