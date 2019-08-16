package com.smate.sie.center.task.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * SIE库标准单位地址信息常量表中的附属表，用于往主表中新增数据。
 * 
 * @author YEXINGYUAN
 * @date 2018年8月2日
 */
@Embeddable
public class SiePdwhInsAddrConstRefreshPK implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1495815329621874147L;
  private Long insId;// 单位id
  private String insName;// 单位名

  public SiePdwhInsAddrConstRefreshPK() {
    super();
  }

  public SiePdwhInsAddrConstRefreshPK(Long insId, String insName) {
    super();
    this.insId = insId;
    this.insName = insName;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Column(name = "INS_NAME")
  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((insId == null) ? 0 : insId.hashCode());
    result = prime * result + ((insName == null) ? 0 : insName.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    SiePdwhInsAddrConstRefreshPK other = (SiePdwhInsAddrConstRefreshPK) obj;
    if (insId == null) {
      if (other.insId != null)
        return false;
    } else if (!insId.equals(other.insId))
      return false;
    if (insName == null) {
      if (other.insName != null)
        return false;
    } else if (!insName.equals(other.insName))
      return false;
    return true;
  }

}
