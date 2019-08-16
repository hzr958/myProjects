package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 群组动态参数表映射类_SCM-5912.
 * 
 * @author MJG
 * @since 2014-11-12
 */
@Entity
@Table(name = "DYNAMIC_GROUP_CONTENT")
public class DynamicGroupContent implements Serializable {

  private static final long serialVersionUID = 3317274135205505755L;

  @Id
  @Column(name = "GROUP_DYN_ID")
  private Long groupDynId;
  @Column(name = "DYN_JSON")
  private String dynJson;

  public DynamicGroupContent() {
    super();
  }

  public DynamicGroupContent(Long groupDynId, String dynJson) {
    super();
    this.groupDynId = groupDynId;
    this.dynJson = dynJson;
  }

  public Long getGroupDynId() {
    return groupDynId;
  }

  public void setGroupDynId(Long groupDynId) {
    this.groupDynId = groupDynId;
  }

  public String getDynJson() {
    return dynJson;
  }

  public void setDynJson(String dynJson) {
    this.dynJson = dynJson;
  }

}
