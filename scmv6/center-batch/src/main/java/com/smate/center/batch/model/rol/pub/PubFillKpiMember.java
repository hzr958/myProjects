package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

/**
 * 成果KPI完整性数据JSON转换类.
 * 
 * @author liqinghua
 * 
 */
public class PubFillKpiMember implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7396174924671540599L;

  private Long pmId;
  private String pmName;
  private Long insId;
  private Long pmPsnId;
  private String acPsnName;

  public PubFillKpiMember() {
    super();
  }

  public PubFillKpiMember(Long pmId, Long insId, Long pmPsnId) {
    super();
    this.pmId = pmId;
    this.insId = insId;
    this.pmPsnId = pmPsnId;
  }

  public PubFillKpiMember(Long pmId, Long insId, Long pmPsnId, String acPsnName) {
    super();
    this.pmId = pmId;
    this.insId = insId;
    this.pmPsnId = pmPsnId;
    this.acPsnName = acPsnName;
  }

  public PubFillKpiMember(String pmName, Long insId, Long pmPsnId, String acPsnName) {
    super();
    this.pmName = pmName;
    this.insId = insId;
    this.pmPsnId = pmPsnId;
    this.acPsnName = acPsnName;
  }

  public Long getPmId() {
    return pmId;
  }

  public Long getInsId() {
    return insId;
  }

  public Long getPmPsnId() {
    return pmPsnId;
  }

  public String getAcPsnName() {
    return acPsnName;
  }

  public void setPmId(Long pmId) {
    this.pmId = pmId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setPmPsnId(Long pmPsnId) {
    this.pmPsnId = pmPsnId;
  }

  public void setAcPsnName(String acPsnName) {
    this.acPsnName = acPsnName;
  }

  public String getPmName() {
    return pmName;
  }

  public void setPmName(String pmName) {
    this.pmName = pmName;
  }

}
