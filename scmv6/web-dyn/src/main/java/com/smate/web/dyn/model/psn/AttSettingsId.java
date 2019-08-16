package com.smate.web.dyn.model.psn;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author zk
 * 
 */
@Embeddable
public class AttSettingsId implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3865830618697476828L;
  private Long psnId;
  private String attId;

  public AttSettingsId() {
    super();
    // TODO Auto-generated constructor stub
  }

  public AttSettingsId(Long psnId, String attId) {
    super();
    this.psnId = psnId;
    this.attId = attId;
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
   * @return the attId
   */
  @Column(name = "ATT_ID")
  public String getAttId() {
    return attId;
  }

  /**
   * @param attId the attId to set
   */
  public void setAttId(String attId) {
    this.attId = attId;
  }

}
