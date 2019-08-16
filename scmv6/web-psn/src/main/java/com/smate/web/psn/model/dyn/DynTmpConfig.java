package com.smate.web.psn.model.dyn;



import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author oyh
 * 
 */
@Entity
@Table(name = "DYN_TEMPLATE_CONFIG")
@Deprecated
public class DynTmpConfig implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -281645918298836745L;
  private Long tmpId;
  private String tmpPath;
  private String remark;

  /**
   * @return the tmpId
   */
  @Id
  public Long getTmpId() {
    return tmpId;
  }

  /**
   * @param tmpId the tmpId to set
   */
  public void setTmpId(Long tmpId) {
    this.tmpId = tmpId;
  }

  /**
   * @return the tmpPath
   */
  @Column(name = "TMP_PATH")
  public String getTmpPath() {
    return tmpPath;
  }

  /**
   * @param tmpPath the tmpPath to set
   */
  public void setTmpPath(String tmpPath) {
    this.tmpPath = tmpPath;
  }

  /**
   * @return the remark
   */
  @Column(name = "REMARK")
  public String getRemark() {
    return remark;
  }

  /**
   * @param remark the remark to set
   */
  public void setRemark(String remark) {
    this.remark = remark;
  }

}
