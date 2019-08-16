package com.smate.center.open.model.nsfc.project;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 基金委webservice集成smate成果接口获取GoogleScholar库成果xml.
 * 
 * @author ajb
 * 
 * 
 */
@Entity
@Table(name = "NSFC_WS_PUB_XML")
public class NsfcwsPubXml implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3489030855516345947L;
  @Id
  @Column(name = "PUB_ID")
  private Long id;
  @Column(name = "PUB_XML")
  private String xml;

  public NsfcwsPubXml() {
    super();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getXml() {
    return xml;
  }

  public void setXml(String xml) {
    this.xml = xml;
  }

}
