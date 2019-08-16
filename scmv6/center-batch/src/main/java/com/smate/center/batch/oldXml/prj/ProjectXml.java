package com.smate.center.batch.oldXml.prj;


/**
 * 项目XML.
 * 
 * @author liqinghua
 * 
 */
public class ProjectXml implements java.io.Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3579575140154507842L;
  private Long id;
  private String xmlData;

  public ProjectXml() {
    super();
  }

  public ProjectXml(Long id, String xmlData) {
    super();
    this.id = id;
    this.xmlData = xmlData;
  }

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

  public String getXmlData() {
    return xmlData;
  }

}
