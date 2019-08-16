package com.smate.core.base.pub.recommend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author tsz
 * 
 */
@Entity
@Table(name = "CONST_PUB_TYPE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ConstPubType implements java.io.Serializable {

  private static final long serialVersionUID = 2604384174807880623L;

  private Integer id;
  private String zhName;
  private String enName;
  private boolean enabled;
  private int seqNo;
  private String defaultPublishState;
  private Integer count;

  /**
   * 真实需要使用的名称.
   */
  private String name;

  public ConstPubType() {
    super();
  }

  public ConstPubType(Integer id, String zhName) {
    super();
    this.id = id;
    this.zhName = zhName;
  }

  /**
   * @return the id
   */
  @Id
  @Column(name = "TYPE_ID")
  public Integer getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * @return the zhName
   */
  @Column(name = "ZH_NAME")
  public String getZhName() {
    return zhName;
  }

  /**
   * @param zhName the zhName to set
   */
  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  /**
   * @return the enName
   */
  @Column(name = "EN_NAME")
  public String getEnName() {
    return enName;
  }

  /**
   * @param enName the enName to set
   */
  public void setEnName(String enName) {
    this.enName = enName;
  }

  /**
   * @return the enabled
   */
  @Column(name = "ENABLED")
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * @param enabled the enabled to set
   */
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  /**
   * @return the seqNo
   */
  @Column(name = "SEQ_NO")
  public int getSeqNo() {
    return seqNo;
  }

  /**
   * @param seqNo the seqNo to set
   */
  public void setSeqNo(int seqNo) {
    this.seqNo = seqNo;
  }

  /**
   * @return the defaultPublishState
   */
  @Column(name = "DEFAULT_PUBLISH_STATE")
  public String getDefaultPublishState() {
    return defaultPublishState;
  }

  /**
   * @param defaultPublishState the defaultPublishState to set
   */
  public void setDefaultPublishState(String defaultPublishState) {
    this.defaultPublishState = defaultPublishState;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Transient
  public String getName() {
    return name;
  }

  @Transient
  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

}
