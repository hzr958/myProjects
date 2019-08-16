package com.smate.sie.core.base.utils.model.prj;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author yxs
 * @descript 项目xml
 */
@Entity
@Table(name = "PRJ_XML")
public class SiePrjXml {
  private Long prjId;
  private String prjXml;

  public SiePrjXml() {
    super();
  }

  public SiePrjXml(Long prjId) {
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
