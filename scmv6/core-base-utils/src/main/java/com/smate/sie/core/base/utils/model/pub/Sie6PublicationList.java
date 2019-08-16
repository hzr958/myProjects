package com.smate.sie.core.base.utils.model.pub;

import com.smate.core.base.utils.string.ServiceUtil;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 成果收录情况.
 * 
 * @author zll
 * 
 */
@Entity
@Table(name = "PUB_LIST")
public class Sie6PublicationList implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8948487308797484357L;
  // 成果编号
  private Long id;
  private Integer listEi = 0;
  private Integer listSci = 0;
  private Integer listIstp = 0;
  private Integer listSsci = 0;
  private Integer listEiSource = 0;
  private Integer listSciSource = 0;
  private Integer listIstpSource = 0;
  private Integer listSsciSource = 0;

  private Integer listCssci = 0;
  private Integer listPku = 0;
  private Integer listCscd = 0;
  private Integer listOther = 0;
  private Integer listCssciSource = 0;
  private Integer listPkuSource = 0;
  private Integer listCscdSource = 0;
  /**
   * 加密ID.
   */
  private String des3Id;

  public Sie6PublicationList() {
    super();
  }

  public Sie6PublicationList(Long id, Integer listEi, Integer listSci, Integer listIstp, Integer listSsci) {
    this.id = id;
    this.listEi = listEi;
    this.listSci = listSci;
    this.listIstp = listIstp;
    this.listSsci = listSsci;
  }

  @Id
  @Column(name = "PUB_ID")
  public Long getId() {
    return id;
  }

  @Column(name = "LIST_SSCI")
  public Integer getListSsci() {
    return listSsci;
  }

  public void setListSsci(Integer listSsci) {
    this.listSsci = listSsci;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "LIST_EI")
  public Integer getListEi() {
    return listEi;
  }

  @Column(name = "LIST_SCI")
  public Integer getListSci() {
    return listSci;
  }

  @Column(name = "LIST_ISTP")
  public Integer getListIstp() {
    return listIstp;
  }

  @Column(name = "LIST_EI_SOURCE")
  public Integer getListEiSource() {
    return listEiSource;
  }

  @Column(name = "LIST_SCI_SOURCE")
  public Integer getListSciSource() {
    return listSciSource;
  }

  @Column(name = "LIST_ISTP_SOURCE")
  public Integer getListIstpSource() {
    return listIstpSource;
  }

  @Column(name = "LIST_SSCI_SOURCE")
  public Integer getListSsciSource() {
    return listSsciSource;
  }

  public void setListEiSource(Integer listEiSource) {
    this.listEiSource = listEiSource;
  }

  public void setListSciSource(Integer listSciSource) {
    this.listSciSource = listSciSource;
  }

  public void setListIstpSource(Integer listIstpSource) {
    this.listIstpSource = listIstpSource;
  }

  public void setListSsciSource(Integer listSsciSource) {
    this.listSsciSource = listSsciSource;
  }

  public void setListEi(Integer listEi) {
    this.listEi = listEi;
  }

  public void setListSci(Integer listSci) {
    this.listSci = listSci;
  }

  public void setListIstp(Integer listIstp) {
    this.listIstp = listIstp;
  }

  @Column(name = "LIST_CSSCI")
  public Integer getListCssci() {
    return listCssci;
  }

  @Column(name = "LIST_PKU")
  public Integer getListPku() {
    return listPku;
  }

  @Column(name = "LIST_CSCD")
  public Integer getListCscd() {
    return listCscd;
  }

  @Column(name = "LIST_OTHER")
  public Integer getListOther() {
    return listOther;
  }

  @Column(name = "LIST_CSSCI_SOURCE")
  public Integer getListCssciSource() {
    return listCssciSource;
  }

  @Column(name = "LIST_PKU_SOURCE")
  public Integer getListPkuSource() {
    return listPkuSource;
  }

  @Column(name = "LIST_CSCD_SOURCE")
  public Integer getListCscdSource() {
    return listCscdSource;
  }

  public void setListCssci(Integer listCssci) {
    this.listCssci = listCssci;
  }

  public void setListPku(Integer listPku) {
    this.listPku = listPku;
  }

  public void setListCscd(Integer listCscd) {
    this.listCscd = listCscd;
  }

  public void setListOther(Integer listOther) {
    this.listOther = listOther;
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

  @Transient
  public String getDes3Id() {

    if (this.id != null && des3Id == null) {
      des3Id = ServiceUtil.encodeToDes3(this.id.toString());
    }
    return des3Id;
  }
}
