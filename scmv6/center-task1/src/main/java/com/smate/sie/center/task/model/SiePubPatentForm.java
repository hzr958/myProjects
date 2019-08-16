package com.smate.sie.center.task.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SiePubPatentForm implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3426351421940835177L;

  private Long insId;
  private List<String> disCodes;// 获取学科code
  private String dcCode;
  private String searchKey;
  private String unitId;
  private String applyDate;
  private String typeId;
  private int containyear = 0;
  private int containunit = 0;
  private List<Map<String, Object>> unitList;
  private List<Map<String, Object>> yearList;
  private List<Map<String, Object>> disList;
  private List<Map<String, Object>> typeList;
  @JsonIgnore
  private Map<String, Object> resultMap;

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }


  public List<Map<String, Object>> getUnitList() {
    return unitList;
  }

  public void setUnitList(List<Map<String, Object>> unitList) {
    this.unitList = unitList;
  }

  public List<Map<String, Object>> getYearList() {
    return yearList;
  }

  public void setYearList(List<Map<String, Object>> yearList) {
    this.yearList = yearList;
  }

  public List<Map<String, Object>> getDisList() {
    return disList;
  }

  public void setDisList(List<Map<String, Object>> disList) {
    this.disList = disList;
  }

  public List<Map<String, Object>> getTypeList() {
    return typeList;
  }

  public void setTypeList(List<Map<String, Object>> typeList) {
    this.typeList = typeList;
  }

  public List<String> getDisCodes() {
    return disCodes;
  }

  public void setDisCodes(List<String> disCodes) {
    this.disCodes = disCodes;
  }

  public String getDcCode() {
    return dcCode;
  }

  public void setDcCode(String dcCode) {
    this.dcCode = dcCode;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public Map<String, Object> getResultMap() {
    return resultMap;
  }

  public void setResultMap(Map<String, Object> resultMap) {
    this.resultMap = resultMap;
  }

  public String getUnitId() {
    return unitId;
  }

  public void setUnitId(String unitId) {
    this.unitId = unitId;
  }

  public String getApplyDate() {
    return applyDate;
  }

  public void setApplyDate(String applyDate) {
    this.applyDate = applyDate;
  }

  public String getTypeId() {
    return typeId;
  }

  public void setTypeId(String typeId) {
    this.typeId = typeId;
  }

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



}
