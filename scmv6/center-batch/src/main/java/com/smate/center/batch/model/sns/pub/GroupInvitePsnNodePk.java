package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.smate.core.base.utils.string.ServiceUtil;

/**
 * GroupInvitePsnNode复合主键.
 * 
 * @author LY
 * 
 */
@Embeddable
public class GroupInvitePsnNodePk implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1899947436590932330L;
  // 群组ID
  private Long groupId;

  // 加密群组ID
  private String des3GroupId;

  // 人员ID
  private Long psnId;

  public GroupInvitePsnNodePk() {

  }

  public GroupInvitePsnNodePk(Long groupId, Long psnId) {
    this.groupId = groupId;
    this.psnId = psnId;
  }

  @Column(name = "GROUP_ID")
  public Long getGroupId() {
    return groupId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Transient
  public String getDes3GroupId() {
    if (this.groupId != null && des3GroupId == null) {
      des3GroupId = ServiceUtil.encodeToDes3(this.groupId.toString());
    }
    return des3GroupId;
  }

  public void setDes3GroupId(String des3GroupId) {
    if (this.groupId == null && StringUtils.isNotBlank(des3GroupId)) {
      this.groupId = Long.valueOf(ServiceUtil.decodeFromDes3(des3GroupId));
    }
    this.des3GroupId = des3GroupId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
    result = prime * result + ((psnId == null) ? 0 : psnId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    GroupInvitePsnNodePk other = (GroupInvitePsnNodePk) obj;
    if (groupId == null) {
      if (other.groupId != null)
        return false;
    } else if (!groupId.equals(other.groupId))
      return false;
    if (psnId == null) {
      if (other.psnId != null)
        return false;
    } else if (!psnId.equals(other.psnId))
      return false;
    return true;
  }
}
