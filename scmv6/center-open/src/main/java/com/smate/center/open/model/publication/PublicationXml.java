package com.smate.center.open.model.publication;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果XML.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PUB_XML")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PublicationXml implements java.io.Serializable {

  private static final long serialVersionUID = -3307754488737786344L;

  private Long id;
  private String xmlData;

  public PublicationXml() {
    super();
    // TODO Auto-generated constructor stub
  }

  public PublicationXml(Long id, String xmlData) {
    super();
    this.id = id;
    this.xmlData = xmlData;
  }

  /**
   * @return the id
   */
  @Id
  @Column(name = "PUB_ID")
  public Long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @param xmlData the xmlData to set
   */
  public void setXmlData(String xmlData) {
    this.xmlData = xmlData;
  }

  /**
   * @return the xmlData
   */
  @Column(name = "XMLDATA")
  public String getXmlData() {
    return xmlData;
  }

}
