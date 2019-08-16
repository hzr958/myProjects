package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;
import java.util.List;

import com.smate.center.batch.model.sns.pub.ConstRefDb;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 
 * @author liqinghua
 * 
 */
public class DeptAliasForm implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5403809930230705184L;

  private Long fetchId;
  private Long aliasId;
  private Long unitId;
  private String unitName;
  private List<InsUnit> insUnitList;
  private List<ConstRefDb> refDbList;
  private String des3FetchId;
  private String des3AliasId;
  private String forwardUrl;
  private Long insId;
  private Long refDbId;
  private String aliasName;
  private Integer onlyNoDept;
  private String insUnitJson;

  public Long getFetchId() {
    return fetchId;
  }

  public Long getAliasId() {
    return aliasId;
  }

  public Long getUnitId() {
    return unitId;
  }

  public List<InsUnit> getInsUnitList() {
    return insUnitList;
  }

  public void setFetchId(Long fetchId) {
    this.fetchId = fetchId;
  }

  public void setAliasId(Long aliasId) {
    this.aliasId = aliasId;
  }

  public void setUnitId(Long unitId) {
    this.unitId = unitId;
  }

  public void setInsUnitList(List<InsUnit> insUnitList) {
    this.insUnitList = insUnitList;
  }

  public String getDes3FetchId() {
    if (this.fetchId != null && des3FetchId == null) {
      des3FetchId = ServiceUtil.encodeToDes3(this.fetchId.toString());
    }
    return des3FetchId;
  }

  public String getDes3AliasId() {
    if (this.aliasId != null && des3AliasId == null) {
      des3AliasId = ServiceUtil.encodeToDes3(this.aliasId.toString());
    }
    return des3AliasId;
  }

  public void setDes3FetchId(String des3FetchId) {
    this.des3FetchId = des3FetchId;
  }

  public void setDes3AliasId(String des3AliasId) {
    this.des3AliasId = des3AliasId;
  }

  public String getForwardUrl() {
    return forwardUrl;
  }

  public void setForwardUrl(String forwardUrl) {
    this.forwardUrl = forwardUrl;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public Long getRefDbId() {
    return refDbId;
  }

  public void setRefDbId(Long refDbId) {
    this.refDbId = refDbId;
  }

  public String getAliasName() {
    return aliasName;
  }

  public void setAliasName(String aliasName) {
    this.aliasName = aliasName;
  }

  public Integer getOnlyNoDept() {
    return onlyNoDept;
  }

  public void setOnlyNoDept(Integer onlyNoDept) {
    this.onlyNoDept = onlyNoDept;
  }

  public String getInsUnitJson() {
    return insUnitJson;
  }

  public void setInsUnitJson(String insUnitJson) {
    this.insUnitJson = insUnitJson;
  }

  public String getUnitName() {
    return unitName;
  }

  public void setUnitName(String unitName) {
    this.unitName = unitName;
  }

  public List<ConstRefDb> getRefDbList() {
    return refDbList;
  }

  public void setRefDbList(List<ConstRefDb> refDbList) {
    this.refDbList = refDbList;
  }

}
