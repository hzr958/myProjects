package com.smate.web.prj.dto;

import java.io.Serializable;

import com.smate.web.prj.model.common.ScmPrjXml;

/**
 * 项目XML实体DTO.
 * 
 * @author liqinghua
 */
public class PrjXmlDTO implements Serializable {
  private static final long serialVersionUID = 3579575140154507842L;
  private Long id;
  private String xmlData;

  public PrjXmlDTO() {
    super();
  }

  public PrjXmlDTO(ScmPrjXml scmPrjXml) {
    super();
    this.id = scmPrjXml.getPrjId();
    this.xmlData = scmPrjXml.getPrjXml();
  }

  public PrjXmlDTO(Long id, String xmlData) {
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
