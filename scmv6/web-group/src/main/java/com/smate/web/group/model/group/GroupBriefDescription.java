package com.smate.web.group.model.group;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 群组简介
 * 
 * @author lhd
 *
 */
@Deprecated
@Entity
@Table(name = "GROUP_BRIEF_DESCRIPTION")
public class GroupBriefDescription implements Serializable {

  private static final long serialVersionUID = 4388414578006310005L;
  @Id
  @Column(name = "GROUP_ID")
  private Long groupId;// 群组id
  @Column(name = "GROUP_BRIEF_DESCRIPTION")
  private String groupBriefDescription;// 群组简介
  @Column(name = "CDT")
  private Date cdt;// 创建时间
  @Column(name = "EDT")
  private Date edt;// 更新时间


  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public String getGroupBriefDescription() {
    return groupBriefDescription;
  }

  public void setGroupBriefDescription(String groupBriefDescription) {
    this.groupBriefDescription = groupBriefDescription;
  }

  public Date getCdt() {
    return cdt;
  }

  public void setCdt(Date cdt) {
    this.cdt = cdt;
  }

  public Date getEdt() {
    return edt;
  }


  public void setEdt(Date edt) {
    this.edt = edt;
  }

  public GroupBriefDescription() {

  }

  public GroupBriefDescription(Long groupId, String groupBriefDescription) {
    super();
    this.groupId = groupId;
    this.groupBriefDescription = groupBriefDescription;
  }


}
