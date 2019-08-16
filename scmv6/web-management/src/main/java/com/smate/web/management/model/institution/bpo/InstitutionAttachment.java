package com.smate.web.management.model.institution.bpo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 单位附件.
 * 
 * @author zjh
 *
 */
@Entity
@Table(name = "INSTITUTION_ATTACHMENT")
public class InstitutionAttachment implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 767087643891665931L;
  private Long insId;
  private String faxPath;

  public InstitutionAttachment() {}

  public InstitutionAttachment(Long insId, String faxPath) {
    this.insId = insId;
    this.faxPath = faxPath;
  }

  @Id
  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Column(name = "FAX_PATH")
  public String getFaxPath() {
    return faxPath;
  }

  public void setFaxPath(String faxPath) {
    this.faxPath = faxPath;
  }

}
