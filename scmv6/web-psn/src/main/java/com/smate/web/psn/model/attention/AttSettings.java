package com.smate.web.psn.model.attention;



import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author oyh
 * 
 */
@Entity
@Table(name = "ATT_SETTINGS")
public class AttSettings implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -685086639487675819L;

  private AttSettingsId attSettingsId;

  /**
   * @return the attSettingsId
   */
  @Id
  @AttributeOverrides({@AttributeOverride(name = "psnId", column = @Column(name = "PSN_ID")),
      @AttributeOverride(name = "attId", column = @Column(name = "ATT_ID"))})
  public AttSettingsId getAttSettingsId() {
    return attSettingsId;
  }

  /**
   * @param attSettingsId the attSettingsId to set
   */
  public void setAttSettingsId(AttSettingsId attSettingsId) {
    this.attSettingsId = attSettingsId;
  }

}
