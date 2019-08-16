package com.smate.web.psn.model.privacy;



import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author oyh
 * 
 */
@Entity
@Table(name = "PRIVACY_ATT_CONFIG")
public class PrivacyAttConfig implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3427063752810239456L;

  private Integer id;
  private String attId;
  private String privacyKey;

  /**
   * @return the id
   */
  @Id
  public Integer getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(Integer id) {
    this.id = id;
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

  /**
   * @return the privacyKey
   */
  @Column(name = "PRIVACY_KEY")
  public String getPrivacyKey() {
    return privacyKey;
  }

  /**
   * @param privacyKey the privacyKey to set
   */
  public void setPrivacyKey(String privacyKey) {
    this.privacyKey = privacyKey;
  }

}
