package com.smate.center.task.model.bdsp;

import java.util.List;
import java.util.Map;

public class BdspInterfacePushDataForm {
  // 每次获取更新数据的数量
  private Integer pushSize = 2000;
  // 需要更新的数据类型
  private boolean updateDataTypePsn = false;
  private boolean updateDataTypePrj = false;
  private boolean updateDataTypePaper = false;
  private boolean updateDataTypePatent = false;
  // 是否有更新的数据
  private boolean hasDataPsnToUpdate = false;
  private boolean hasDataPrjToUpdate = false;
  private boolean hasDataPaperToUpdate = false;
  private boolean hasDataPatentToUpdate = false;
  // 推送结果集
  private List<BdspResearchPsnBase> psnlist;
  private List<BdspPrjBase> prjlist;
  private List<BdspPaperBase> paperlist;
  private List<BdspPatentBase> patentlist;
  // 待推送的数据
  private List<Map<String, Object>> psnBaseList;
  private List<Map<String, Object>> prjBaseList;
  private List<Map<String, Object>> paperBaseList;
  private List<Map<String, Object>> patentBaseList;
  // 错误数据收集
  private List<Long> errorPsnlist;
  private List<Long> errorPrjlist;
  private List<Long> errorPaperlist;
  private List<Long> errorPatentlist;
  // 错误原因收集
  private Map<String, String> errorMsgPsnMap;
  private Map<String, String> errorMsgPrjMap;
  private Map<String, String> errorMsgPaperMap;
  private Map<String, String> errorMsgPatentMap;



  public Map<String, String> getErrorMsgPsnMap() {
    return errorMsgPsnMap;
  }

  public void setErrorMsgPsnMap(Map<String, String> errorMsgPsnMap) {
    this.errorMsgPsnMap = errorMsgPsnMap;
  }

  public Map<String, String> getErrorMsgPrjMap() {
    return errorMsgPrjMap;
  }

  public void setErrorMsgPrjMap(Map<String, String> errorMsgPrjMap) {
    this.errorMsgPrjMap = errorMsgPrjMap;
  }

  public Map<String, String> getErrorMsgPaperMap() {
    return errorMsgPaperMap;
  }

  public void setErrorMsgPaperMap(Map<String, String> errorMsgPaperMap) {
    this.errorMsgPaperMap = errorMsgPaperMap;
  }

  public Map<String, String> getErrorMsgPatentMap() {
    return errorMsgPatentMap;
  }

  public void setErrorMsgPatentMap(Map<String, String> errorMsgPatentMap) {
    this.errorMsgPatentMap = errorMsgPatentMap;
  }

  public List<BdspResearchPsnBase> getPsnlist() {
    return psnlist;
  }

  public void setPsnlist(List<BdspResearchPsnBase> psnlist) {
    this.psnlist = psnlist;
  }

  public List<BdspPrjBase> getPrjlist() {
    return prjlist;
  }

  public void setPrjlist(List<BdspPrjBase> prjlist) {
    this.prjlist = prjlist;
  }

  public List<BdspPaperBase> getPaperlist() {
    return paperlist;
  }

  public void setPaperlist(List<BdspPaperBase> paperlist) {
    this.paperlist = paperlist;
  }

  public List<BdspPatentBase> getPatentlist() {
    return patentlist;
  }

  public void setPatentlist(List<BdspPatentBase> patentlist) {
    this.patentlist = patentlist;
  }

  public List<Long> getErrorPsnlist() {
    return errorPsnlist;
  }

  public void setErrorPsnlist(List<Long> errorPsnlist) {
    this.errorPsnlist = errorPsnlist;
  }

  public List<Long> getErrorPrjlist() {
    return errorPrjlist;
  }

  public void setErrorPrjlist(List<Long> errorPrjlist) {
    this.errorPrjlist = errorPrjlist;
  }

  public List<Long> getErrorPaperlist() {
    return errorPaperlist;
  }

  public void setErrorPaperlist(List<Long> errorPaperlist) {
    this.errorPaperlist = errorPaperlist;
  }

  public List<Long> getErrorPatentlist() {
    return errorPatentlist;
  }

  public void setErrorPatentlist(List<Long> errorPatentlist) {
    this.errorPatentlist = errorPatentlist;
  }

  public List<Map<String, Object>> getPsnBaseList() {
    return psnBaseList;
  }

  public void setPsnBaseList(List<Map<String, Object>> psnBaseList) {
    this.psnBaseList = psnBaseList;
  }

  public List<Map<String, Object>> getPrjBaseList() {
    return prjBaseList;
  }

  public void setPrjBaseList(List<Map<String, Object>> prjBaseList) {
    this.prjBaseList = prjBaseList;
  }

  public List<Map<String, Object>> getPaperBaseList() {
    return paperBaseList;
  }

  public void setPaperBaseList(List<Map<String, Object>> paperBaseList) {
    this.paperBaseList = paperBaseList;
  }

  public List<Map<String, Object>> getPatentBaseList() {
    return patentBaseList;
  }

  public void setPatentBaseList(List<Map<String, Object>> patentBaseList) {
    this.patentBaseList = patentBaseList;
  }

  public boolean isUpdateDataTypePsn() {
    return updateDataTypePsn;
  }

  public void setUpdateDataTypePsn(boolean updateDataTypePsn) {
    this.updateDataTypePsn = updateDataTypePsn;
  }

  public boolean isUpdateDataTypePrj() {
    return updateDataTypePrj;
  }

  public void setUpdateDataTypePrj(boolean updateDataTypePrj) {
    this.updateDataTypePrj = updateDataTypePrj;
  }

  public boolean isUpdateDataTypePaper() {
    return updateDataTypePaper;
  }

  public void setUpdateDataTypePaper(boolean updateDataTypePaper) {
    this.updateDataTypePaper = updateDataTypePaper;
  }

  public boolean isUpdateDataTypePatent() {
    return updateDataTypePatent;
  }

  public void setUpdateDataTypePatent(boolean updateDataTypePatent) {
    this.updateDataTypePatent = updateDataTypePatent;
  }

  public boolean isHasDataPsnToUpdate() {
    return hasDataPsnToUpdate;
  }

  public void setHasDataPsnToUpdate(boolean hasDataPsnToUpdate) {
    this.hasDataPsnToUpdate = hasDataPsnToUpdate;
  }

  public boolean isHasDataPrjToUpdate() {
    return hasDataPrjToUpdate;
  }

  public void setHasDataPrjToUpdate(boolean hasDataPrjToUpdate) {
    this.hasDataPrjToUpdate = hasDataPrjToUpdate;
  }

  public boolean isHasDataPaperToUpdate() {
    return hasDataPaperToUpdate;
  }

  public void setHasDataPaperToUpdate(boolean hasDataPaperToUpdate) {
    this.hasDataPaperToUpdate = hasDataPaperToUpdate;
  }

  public boolean isHasDataPatentToUpdate() {
    return hasDataPatentToUpdate;
  }

  public void setHasDataPatentToUpdate(boolean hasDataPatentToUpdate) {
    this.hasDataPatentToUpdate = hasDataPatentToUpdate;
  }

  public Integer getPushSize() {
    return pushSize;
  }

  public void setPushSize(Integer pushSize) {
    this.pushSize = pushSize;
  }



}
