package com.smate.center.batch.service.pub.mq;

import java.io.Serializable;
import java.util.List;

import com.smate.center.batch.model.pdwh.pub.IeeexploreInsName;
import com.smate.center.batch.model.pdwh.pub.SciencedirectInsName;
import com.smate.center.batch.model.pdwh.pub.cnipr.CniprInsName;
import com.smate.center.batch.model.pdwh.pub.cnki.CnkiInsName;
import com.smate.center.batch.model.pdwh.pub.ei.EiInsName;
import com.smate.center.batch.model.pdwh.pub.isi.IsiInsName;
import com.smate.center.batch.model.pdwh.pub.pubmed.PubmedInsName;
import com.smate.center.batch.model.pdwh.pub.sps.SpsInsName;
import com.smate.center.batch.model.pdwh.pub.wanfang.WanfangInsName;
import com.smate.center.batch.model.sns.pub.InsAlias;

public class InsAliasSyncMessage {

  private Long insId;
  private List<InsAlias> insAliasList;
  private List<IsiInsName> isiInsNameList;
  private List<SpsInsName> spsInsNameList;
  private List<CnkiInsName> cnkiInsNameList;
  private List<WanfangInsName> wanfangInsNameList;
  private List<PubmedInsName> pubmedInsNameList;
  private List<EiInsName> eiInsNameList;
  private List<CniprInsName> cniprInsNameList;
  private List<SciencedirectInsName> sciencedirectInsNameList;
  private List<IeeexploreInsName> ieeexploreInsNameList;

  public InsAliasSyncMessage() {
    super();
  }

  public InsAliasSyncMessage(Long insId, List<InsAlias> insAliasList, List<IsiInsName> isiInsNameList,
      List<SpsInsName> spsInsNameList, List<CnkiInsName> cnkiInsNameList, List<WanfangInsName> wanfangInsNameList,
      List<PubmedInsName> pubmedInsNameList, List<EiInsName> eiInsNameList, List<CniprInsName> cniprInsNameList,
      List<SciencedirectInsName> sciencedirectInsNameList, List<IeeexploreInsName> ieeexploreInsNameList) {
    super();
    this.insId = insId;
    this.insAliasList = insAliasList;
    this.isiInsNameList = isiInsNameList;
    this.spsInsNameList = spsInsNameList;
    this.cnkiInsNameList = cnkiInsNameList;
    this.wanfangInsNameList = wanfangInsNameList;
    this.pubmedInsNameList = pubmedInsNameList;
    this.eiInsNameList = eiInsNameList;
    this.cniprInsNameList = cniprInsNameList;
    this.sciencedirectInsNameList = sciencedirectInsNameList;
    this.ieeexploreInsNameList = ieeexploreInsNameList;
  }

  public Long getInsId() {
    return insId;
  }

  public List<InsAlias> getInsAliasList() {
    return insAliasList;
  }

  public List<IsiInsName> getIsiInsNameList() {
    return isiInsNameList;
  }

  public List<SpsInsName> getSpsInsNameList() {
    return spsInsNameList;
  }

  public List<CnkiInsName> getCnkiInsNameList() {
    return cnkiInsNameList;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setInsAliasList(List<InsAlias> insAliasList) {
    this.insAliasList = insAliasList;
  }

  public void setIsiInsNameList(List<IsiInsName> isiInsNameList) {
    this.isiInsNameList = isiInsNameList;
  }

  public void setSpsInsNameList(List<SpsInsName> spsInsNameList) {
    this.spsInsNameList = spsInsNameList;
  }

  public void setCnkiInsNameList(List<CnkiInsName> cnkiInsNameList) {
    this.cnkiInsNameList = cnkiInsNameList;
  }

  public List<WanfangInsName> getWanfangInsNameList() {
    return wanfangInsNameList;
  }

  public void setWanfangInsNameList(List<WanfangInsName> wanfangInsNameList) {
    this.wanfangInsNameList = wanfangInsNameList;
  }

  public List<PubmedInsName> getPubmedInsNameList() {
    return pubmedInsNameList;
  }

  public void setPubmedInsNameList(List<PubmedInsName> pubmedInsNameList) {
    this.pubmedInsNameList = pubmedInsNameList;
  }

  public List<EiInsName> getEiInsNameList() {
    return eiInsNameList;
  }

  public void setEiInsNameList(List<EiInsName> eiInsNameList) {
    this.eiInsNameList = eiInsNameList;
  }

  public List<CniprInsName> getCniprInsNameList() {
    return cniprInsNameList;
  }

  public void setCniprInsNameList(List<CniprInsName> cniprInsNameList) {
    this.cniprInsNameList = cniprInsNameList;
  }

  public List<SciencedirectInsName> getSciencedirectInsNameList() {
    return sciencedirectInsNameList;
  }

  public void setSciencedirectInsNameList(List<SciencedirectInsName> sciencedirectInsNameList) {
    this.sciencedirectInsNameList = sciencedirectInsNameList;
  }

  public List<IeeexploreInsName> getIeeexploreInsNameList() {
    return ieeexploreInsNameList;
  }

  public void setIeeexploreInsNameList(List<IeeexploreInsName> ieeexploreInsNameList) {
    this.ieeexploreInsNameList = ieeexploreInsNameList;
  }

}
