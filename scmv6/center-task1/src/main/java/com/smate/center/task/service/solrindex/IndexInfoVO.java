package com.smate.center.task.service.solrindex;

import java.util.List;

import com.smate.center.task.v8pub.pdwh.po.PubPdwhPO;
import com.smate.center.task.v8pub.sns.po.PubSnsPO;
import com.smate.core.base.utils.model.security.Person;

public class IndexInfoVO {
  public static String INDEX_TYPE_PDWH_PAPER = "pdwh_paper_index";
  public static String INDEX_TYPE_PDWH_PAT = "pdwh_pat_index";
  public static String INDEX_TYPE_PSN = "person_index";
  public static String INDEX_TYPE_FUND = "fund_index";
  public static String INDEX_TYPE_KW = "keywords_index";
  public static String INDEX_TYPE_SNS_PUB = "simple_sns_pub_index";
  private String serviceType;
  private Long lastPsnId;
  private Long lastSnsPubId;
  private Long lastPatId;
  private Long lastPaperId;
  private String status;
  private String Msg;
  private List<PubPdwhPO> pdwhPubList;
  private List<PubSnsPO> snsPubList;
  private List<Person> psnList;
  private Long maxId;
  private Long maxPubId;


  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getMsg() {
    return Msg;
  }

  public void setMsg(String msg) {
    Msg = msg;
  }

  public Long getLastPatId() {
    return lastPatId;
  }

  public void setLastPatId(Long lastPatId) {
    this.lastPatId = lastPatId;
  }

  public List<PubPdwhPO> getPdwhPubList() {
    return pdwhPubList;
  }

  public void setPdwhPubList(List<PubPdwhPO> list) {
    this.pdwhPubList = list;
  }

  public Long getLastPaperId() {
    return lastPaperId;
  }

  public void setLastPaperId(Long lastPaperId) {
    this.lastPaperId = lastPaperId;
  }

  public List<PubSnsPO> getSnsPubList() {
    return snsPubList;
  }

  public void setSnsPubList(List<PubSnsPO> list) {
    this.snsPubList = list;
  }

  public Long getLastSnsPubId() {
    return lastSnsPubId;
  }

  public void setLastSnsPubId(Long lastSnsPubId) {
    this.lastSnsPubId = lastSnsPubId;
  }

  public Long getLastPsnId() {
    return lastPsnId;
  }

  public void setLastPsnId(Long lastPsnId) {
    this.lastPsnId = lastPsnId;
  }

  public List<Person> getPsnList() {
    return psnList;
  }

  public void setPsnList(List<Person> psnList) {
    this.psnList = psnList;
  }

  public String getServiceType() {
    return serviceType;
  }

  public void setServiceType(String serviceType) {
    this.serviceType = serviceType;
  }

  public Long getMaxId() {
    return maxId;
  }

  public void setMaxId(Long maxId) {
    this.maxId = maxId;
  }

  public Long getMaxPubId() {
    return maxPubId;
  }

  public void setMaxPubId(Long maxPubId) {
    this.maxPubId = maxPubId;
  }

}
