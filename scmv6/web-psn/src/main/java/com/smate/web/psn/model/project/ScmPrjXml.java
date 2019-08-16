package com.smate.web.psn.model.project;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 项目XML.
 * 
 * @author tj
 * 
 */
@Entity
@Table(name = "SCM_PRJ_XML")
public class ScmPrjXml implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7031734208857465413L;
  private Long prjId;
  private String prjXml;

  public ScmPrjXml() {
    super();
  }

  public ScmPrjXml(Long prjId) {
    super();
    this.prjId = prjId;
  }

  @Id
  @Column(name = "PRJ_ID")
  public Long getPrjId() {
    return prjId;
  }

  @Column(name = "PRJ_XML")
  public String getPrjXml() {
    return prjXml;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  public void setPrjXml(String prjXml) {
    this.prjXml = prjXml;
  }

}
