package com.smate.center.batch.model.sns.pub;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author yamingd
 * 
 */
@Entity
@Table(name = "INSTITUTION")
public class PublicationIns implements java.io.Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8884991950182861274L;

  private long insId;
  // 中文名称
  private String zhName;
  // 英文名称
  private String enName;

  private int enabled;

  /**
   * @param insId the insId to set
   */
  public void setInsId(long insId) {
    this.insId = insId;
  }

  /**
   * @return the insId
   */
  @Id
  @Column(name = "INS_ID")
  public long getInsId() {
    return insId;
  }

  /**
   * @param cname the cname to set
   */
  public void setZhName(String cname) {
    this.zhName = cname;
  }

  /**
   * @return the cname
   */
  @Column(name = "ZH_NAME")
  public String getZhName() {
    return zhName;
  }

  /**
   * @param ename the ename to set
   */
  public void setEnName(String ename) {
    this.enName = ename;
  }

  /**
   * @return the ename
   */
  @Column(name = "EN_NAME")
  public String getEnName() {
    return enName;
  }

  @Column(name = "ENABLED")
  public int getEnabled() {
    return enabled;
  }

  public void setEnabled(int enabled) {
    this.enabled = enabled;
  }

}
