package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

/**
 * 确定成果作者、人员关联关系.
 * 
 * @author liqinghua
 * 
 */
public class PubPsnCreateRelation implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -558848361851065144L;

  // 成果ID
  private Long pubId;
  // 人员ID
  private Long psnId;
  // 1创建关联，2添加作者
  private Integer type;
  // 关联的作者ID
  private Long pmId;
  // 添加的作者名称
  private String memberName;

  public Long getPubId() {
    return pubId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public Integer getType() {
    return type;
  }

  public Long getPmId() {
    return pmId;
  }

  public String getMemberName() {
    return memberName;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public void setPmId(Long pmId) {
    this.pmId = pmId;
  }

  public void setMemberName(String memberName) {
    this.memberName = memberName;
  }

}
