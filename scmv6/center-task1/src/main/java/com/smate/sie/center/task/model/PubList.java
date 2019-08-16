package com.smate.sie.center.task.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果收录实体
 * 
 * @author sjzhou
 *
 */
@Entity
@Table(name = "PUB_LIST")
public class PubList implements Serializable {

  private static final long serialVersionUID = 6345521384426939406L;

  @Id
  @Column(name = "PUB_ID")
  private Long pubId;

  @Column(name = "LIST_EI")
  private Integer listEi;

  @Column(name = "LIST_SCI")
  private Integer listSci;

  @Column(name = "LIST_ISTP")
  private Integer listIstp;

  @Column(name = "LIST_SSCI")
  private Integer listSsci;

  @Column(name = "LIST_EI_SOURCE")
  private Integer listEiSource;

  @Column(name = "LIST_SCI_SOURCE")
  private Integer listSciSource;

  @Column(name = "LIST_ISTP_SOURCE")
  private Integer listIstpSource;

  @Column(name = "LIST_SSCI_SOURCE")
  private Integer listSsciSource;

  @Column(name = "LIST_CSSCI")
  private Integer listCssci;

  @Column(name = "LIST_PKU")
  private Integer listPku;

  @Column(name = "LIST_CSCD")
  private Integer listCscd;

  @Column(name = "LIST_OTHER")
  private Integer listOther;

  @Column(name = "LIST_CSSCI_SOURCE")
  private Integer listCssciSource;

  @Column(name = "LIST_PKU_SOURCE")
  private Integer listPkuSource;

  @Column(name = "LIST_CSCD_SOURCE")
  private Integer listCscdSource;

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Integer getListEi() {
    return listEi;
  }

  public void setListEi(Integer listEi) {
    this.listEi = listEi;
  }

  public Integer getListSci() {
    return listSci;
  }

  public void setListSci(Integer listSci) {
    this.listSci = listSci;
  }

  public Integer getListIstp() {
    return listIstp;
  }

  public void setListIstp(Integer listIstp) {
    this.listIstp = listIstp;
  }

  public Integer getListSsci() {
    return listSsci;
  }

  public void setListSsci(Integer listSsci) {
    this.listSsci = listSsci;
  }

  public Integer getListEiSource() {
    return listEiSource;
  }

  public void setListEiSource(Integer listEiSource) {
    this.listEiSource = listEiSource;
  }

  public Integer getListSciSource() {
    return listSciSource;
  }

  public void setListSciSource(Integer listSciSource) {
    this.listSciSource = listSciSource;
  }

  public Integer getListIstpSource() {
    return listIstpSource;
  }

  public void setListIstpSource(Integer listIstpSource) {
    this.listIstpSource = listIstpSource;
  }

  public Integer getListSsciSource() {
    return listSsciSource;
  }

  public void setListSsciSource(Integer listSsciSource) {
    this.listSsciSource = listSsciSource;
  }

  public Integer getListCssci() {
    return listCssci;
  }

  public void setListCssci(Integer listCssci) {
    this.listCssci = listCssci;
  }

  public Integer getListPku() {
    return listPku;
  }

  public void setListPku(Integer listPku) {
    this.listPku = listPku;
  }

  public Integer getListCscd() {
    return listCscd;
  }

  public void setListCscd(Integer listCscd) {
    this.listCscd = listCscd;
  }

  public Integer getListOther() {
    return listOther;
  }

  public void setListOther(Integer listOther) {
    this.listOther = listOther;
  }

  public Integer getListCssciSource() {
    return listCssciSource;
  }

  public Integer getListPkuSource() {
    return listPkuSource;
  }

  public Integer getListCscdSource() {
    return listCscdSource;
  }

  public void setListCssciSource(Integer listCssciSource) {
    this.listCssciSource = listCssciSource;
  }

  public void setListPkuSource(Integer listPkuSource) {
    this.listPkuSource = listPkuSource;
  }

  public void setListCscdSource(Integer listCscdSource) {
    this.listCscdSource = listCscdSource;
  }
}
