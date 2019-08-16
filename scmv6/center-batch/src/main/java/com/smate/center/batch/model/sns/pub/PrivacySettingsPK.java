package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PrivacySettingsPK implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8680838322264446781L;
  private Long psnId;
  // 隐私类型
  private String privacyAction;

  public PrivacySettingsPK() {
    super();
    // TODO Auto-generated constructor stub
  }

  public PrivacySettingsPK(Long psnId, String privacyAction) {
    super();
    this.psnId = psnId;
    this.privacyAction = privacyAction;
  }

  /**
   * @return the psnId
   */
  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  /**
   * @param psnId the psnId to set
   */
  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  /**
   * @return the privacyAction
   */
  @Column(name = "PRIVACY_ACTION")
  public String getPrivacyAction() {
    return privacyAction;
  }

  /**
   * @param privacyAction the privacyAction to set
   */
  public void setPrivacyAction(String privacyAction) {
    this.privacyAction = privacyAction;
  }

}
