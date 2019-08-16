package com.smate.center.task.base;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 系统配置信息.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "APPLICATION_SETTING")
public class AppSetting implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -1015431138734447022L;
  private Long id;
  private String key;
  private String value;
  private String remark;

  public AppSetting() {
    super();
  }

  public AppSetting(Long id, String key, String value, String remark) {
    super();
    this.id = id;
    this.key = key;
    this.value = value;
    this.remark = remark;
  }

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  @Column(name = "KEY")
  public String getKey() {
    return key;
  }

  @Column(name = "VALUE")
  public String getValue() {
    return value;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Column(name = "REMARK")
  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

}
