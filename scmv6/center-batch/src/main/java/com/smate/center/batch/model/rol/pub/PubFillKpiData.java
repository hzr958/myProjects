package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;
import java.util.List;

/**
 * 成果KPI完整性数据JSON转换类.
 * 
 * @author liqinghua
 * 
 */
public class PubFillKpiData implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7396174924671540599L;

  private Long pubId;
  private Integer pubType;
  private String publishDate;
  private Integer listSci;
  private Integer listEi;
  private Integer listIstp;
  private Integer listSsci;
  private Integer pmType = 1;
  private Long notPsnPub;
  private List<PubFillKpiMember> pubMembers;

  public Long getPubId() {
    return pubId;
  }

  public Integer getPubType() {
    return pubType;
  }

  public String getPublishDate() {
    return publishDate;
  }

  public Integer getListSci() {
    return listSci;
  }

  public Integer getListEi() {
    return listEi;
  }

  public Integer getListIstp() {
    return listIstp;
  }

  public Integer getListSsci() {
    return listSsci;
  }

  public List<PubFillKpiMember> getPubMembers() {
    return pubMembers;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setPubType(Integer pubType) {
    this.pubType = pubType;
  }

  public void setPublishDate(String publishDate) {
    this.publishDate = publishDate;
  }

  public void setListSci(Integer listSci) {
    this.listSci = listSci;
  }

  public void setListEi(Integer listEi) {
    this.listEi = listEi;
  }

  public void setListIstp(Integer listIstp) {
    this.listIstp = listIstp;
  }

  public void setListSsci(Integer listSsci) {
    this.listSsci = listSsci;
  }

  public void setPubMembers(List<PubFillKpiMember> pubMembers) {
    this.pubMembers = pubMembers;
  }

  public Integer getPmType() {
    return pmType;
  }

  public void setPmType(Integer pmType) {
    this.pmType = pmType;
  }

  public Long getNotPsnPub() {
    return notPsnPub;
  }

  public void setNotPsnPub(Long notPsnPub) {
    this.notPsnPub = notPsnPub;
  }

}
