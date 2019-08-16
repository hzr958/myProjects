package com.smate.sie.center.task.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PublicationForm implements Serializable {

  private static final long serialVersionUID = -7780123561314043756L;

  private Long insId;
  private String unitId;
  private String pubType;
  private String publishYear;
  private String searchKey;
  private String dcCode;
  private Long totalNum;
  private String includeType; // 收录
  private List<String> disCodes;// 获取学科code
  private List<String> pubLists;// 索引
  private List<Map<String, Object>> unitList;// 部门
  private List<Map<String, Object>> fieldList;// 获取领域
  private List<Map<String, Object>> yearList;// 年份
  private List<Map<String, Object>> indexList;// 索引
  private List<Map<String, Object>> pubTypeList;// 成果类型
  private int containyear = 0;
  private int containunit = 0;

  public int getContainyear() {
    return containyear;
  }

  public void setContainyear(int containyear) {
    this.containyear = containyear;
  }

  public int getContainunit() {
    return containunit;
  }

  public void setContainunit(int containunit) {
    this.containunit = containunit;
  }

  @JsonIgnore
  private Map<String, Object> resultMap;

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public Long getTotalNum() {
    return totalNum;
  }

  public void setTotalNum(Long totalNum) {
    this.totalNum = totalNum;
  }

  public String getDcCode() {
    return dcCode;
  }

  public void setDcCode(String dcCode) {
    this.dcCode = dcCode;
  }

  public List<String> getDisCodes() {
    return disCodes;
  }

  public void setDisCodes(List<String> disCodes) {
    this.disCodes = disCodes;
  }

  public Map<String, Object> getResultMap() {
    return resultMap;
  }

  public void setResultMap(Map<String, Object> resultMap) {
    this.resultMap = resultMap;
  }

  public List<Map<String, Object>> getUnitList() {
    return unitList;
  }

  public void setUnitList(List<Map<String, Object>> unitList) {
    this.unitList = unitList;
  }

  public List<Map<String, Object>> getFieldList() {
    return fieldList;
  }

  public void setFieldList(List<Map<String, Object>> fieldList) {
    this.fieldList = fieldList;
  }

  public List<Map<String, Object>> getYearList() {
    return yearList;
  }

  public void setYearList(List<Map<String, Object>> yearList) {
    this.yearList = yearList;
  }

  public List<Map<String, Object>> getIndexList() {
    return indexList;
  }

  public void setIndexList(List<Map<String, Object>> indexList) {
    this.indexList = indexList;
  }

  public List<Map<String, Object>> getPubTypeList() {
    return pubTypeList;
  }

  public void setPubTypeList(List<Map<String, Object>> pubTypeList) {
    this.pubTypeList = pubTypeList;
  }

  public String getUnitId() {
    return unitId;
  }

  public void setUnitId(String unitId) {
    this.unitId = unitId;
  }

  public String getPubType() {
    return pubType;
  }

  public void setPubType(String pubType) {
    this.pubType = pubType;
  }

  public String getPublishYear() {
    return publishYear;
  }

  public void setPublishYear(String publishYear) {
    this.publishYear = publishYear;
  }

  public List<String> getPubLists() {
    return pubLists;
  }

  public void setPubLists(List<String> pubLists) {
    this.pubLists = pubLists;
  }

  public String getIncludeType() {
    return includeType;
  }

  public void setIncludeType(String includeType) {
    this.includeType = includeType;
  }

}
