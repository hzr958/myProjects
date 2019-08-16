package com.smate.center.task.model.group;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 群组groupcode实体类
 * 
 * @author JunLi
 *
 *         2017年11月1日
 */
@Entity
@Table(name = "V_OPEN_GROUP_CODE_TEMP")
public class OpenGrpCodeTemp {

  private Long groupId;// 群组ID
  private Long createOpenId;// 群组创建人openId
  private String groupCode;

  @Id
  @Column(name = "GROUP_ID")
  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  @Column(name = "CREATE_OPENID")
  public Long getCreateOpenId() {
    return createOpenId;
  }

  public void setCreateOpenId(Long createOpenId) {
    this.createOpenId = createOpenId;
  }

  @Column(name = "GROUP_CODE")
  public String getGroupCode() {
    return groupCode;
  }

  public void setGroupCode(String groupCode) {
    this.groupCode = groupCode;
  }

  public OpenGrpCodeTemp() {
    super();
  }

  public OpenGrpCodeTemp(Long groupId, Long createOpenId, String groupCode) {
    super();
    this.groupId = groupId;
    this.createOpenId = createOpenId;
    this.groupCode = groupCode;
  }

}
