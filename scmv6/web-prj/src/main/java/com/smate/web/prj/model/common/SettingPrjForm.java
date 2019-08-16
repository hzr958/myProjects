package com.smate.web.prj.model.common;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * setting_pub_form实体.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "SETTING_PRJ_FORM")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class SettingPrjForm implements Serializable {

  private static final long serialVersionUID = -625316156865187210L;

  private Integer id;
  private String tmpFolder;
  private int enabed;
  private Long insId;

  @Id
  @Column(name = "TMPL_ID")
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @Column(name = "TMPL_FOLDER")
  public String getTmpFolder() {
    return tmpFolder;
  }

  public void setTmpFolder(String tmpFolder) {
    this.tmpFolder = tmpFolder;
  }

  @Column(name = "ENABLED")
  public int getEnabed() {
    return enabed;
  }

  public void setEnabed(int enabed) {
    this.enabed = enabed;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

}
