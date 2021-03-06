package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果XML.
 * 
 * @author tj
 * 
 */
@Entity
@Table(name = "SCM_PUB_XML")
public class ScmPubXml implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7762079308186842090L;

  private Long pubId;
  private String pubXml;

  public ScmPubXml() {
    super();
  }

  public ScmPubXml(Long pubId) {
    super();
    this.pubId = pubId;
  }

  public ScmPubXml(Long pubId, String pubXml) {
    super();
    this.pubId = pubId;
    this.pubXml = pubXml;
  }

  @Id
  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "PUB_XML")
  public String getPubXml() {
    return pubXml;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setPubXml(String pubXml) {
    this.pubXml = pubXml;
  }

}
