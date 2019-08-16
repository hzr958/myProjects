package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果XML为空的记录.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "ROL_PUB_XML_EMPTY")
public class RolPubXmlEmpty implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6080534021327294954L;
  private Long pubId;

  public RolPubXmlEmpty() {
    super();
  }

  public RolPubXmlEmpty(Long pubId) {
    super();
    this.pubId = pubId;
  }

  @Id
  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

}
