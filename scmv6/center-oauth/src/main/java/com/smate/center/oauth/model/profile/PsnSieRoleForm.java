package com.smate.center.oauth.model.profile;

import java.io.Serializable;

/**
 * 人员SIE角色处理用Form
 * 
 * @author wsn
 * @date 2018年6月7日
 */
public class PsnSieRoleForm implements Serializable {

  private static final long serialVersionUID = -3265935663708722817L;

  private Long psnId; // 人员ID
  private String des3PsnId; // 加密的人员角色ID
  private Long insId; // 机构ID
  private String des3InsId; // 加密的机构ID
  private boolean rolMultiRole; // 是否有多个角色

  public PsnSieRoleForm() {
    super();
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public String getDes3InsId() {
    return des3InsId;
  }

  public void setDes3InsId(String des3InsId) {
    this.des3InsId = des3InsId;
  }

  public boolean getRolMultiRole() {
    return rolMultiRole;
  }

  public void setRolMultiRole(boolean rolMultiRole) {
    this.rolMultiRole = rolMultiRole;
  }

}
