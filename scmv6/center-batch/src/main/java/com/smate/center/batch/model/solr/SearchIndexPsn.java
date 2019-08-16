package com.smate.center.batch.model.solr;

import java.io.Serializable;

public class SearchIndexPsn implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -4110809639098065020L;

  private String businessType;
  private String runEnv;
  private Long id;
  private Long psnId;
  private String psnName;
  private String enPsnName;
  private String title;
  private String zhInsName;
  private String enInsName;
  private String zhUnit;
  private String enUnit;

  public String getBusinessType() {
    return businessType;
  }

  public void setBusinessType(String businessType) {
    this.businessType = businessType;
  }

  public String getRunEnv() {
    return runEnv;
  }

  public void setRunEnv(String runEnv) {
    this.runEnv = runEnv;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getPsnName() {
    return psnName;
  }

  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  public String getEnPsnName() {
    return enPsnName;
  }

  public void setEnPsnName(String enPsnName) {
    this.enPsnName = enPsnName;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getZhInsName() {
    return zhInsName;
  }

  public void setZhInsName(String zhInsName) {
    this.zhInsName = zhInsName;
  }

  public String getEnInsName() {
    return enInsName;
  }

  public void setEnInsName(String enInsName) {
    this.enInsName = enInsName;
  }

  public String getZhUnit() {
    return zhUnit;
  }

  public void setZhUnit(String zhUnit) {
    this.zhUnit = zhUnit;
  }

  public String getEnUnit() {
    return enUnit;
  }

  public void setEnUnit(String enUnit) {
    this.enUnit = enUnit;
  }

}
