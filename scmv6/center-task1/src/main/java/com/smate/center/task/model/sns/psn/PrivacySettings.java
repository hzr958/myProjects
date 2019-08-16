/**
 * 
 */
package com.smate.center.task.model.sns.psn;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author oyh
 * 
 */
@Entity
@Table(name = "PRIVACY_SETTINGS")
public class PrivacySettings implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -2271574844818733009L;
  private PrivacySettingsPK pk;
  // 权限设置
  private Integer permission;

  public PrivacySettings() {
    super();
    // TODO Auto-generated constructor stub
  }

  public PrivacySettings(PrivacySettingsPK pk) {
    super();
    this.pk = pk;
  }

  public PrivacySettings(PrivacySettingsPK pk, Integer permission) {
    super();
    this.pk = pk;
    this.permission = permission;
  }

  /**
   * @return the pk
   */
  @EmbeddedId
  public PrivacySettingsPK getPk() {
    return pk;
  }

  /**
   * @param pk the pk to set
   */
  public void setPk(PrivacySettingsPK pk) {
    this.pk = pk;
  }

  /**
   * @return the permission
   */
  @Column(name = "PERMISSION")
  public Integer getPermission() {
    return permission;
  }

  /**
   * @param permission the permission to set
   */
  public void setPermission(Integer permission) {
    this.permission = permission;
  }

}
