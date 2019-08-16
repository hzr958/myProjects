package com.smate.sie.center.task.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 批量创建单位，单位数据来源常量表
 * 
 * @author hd
 *
 */
@Entity
@Table(name = "IMPORT_INS_DATA_SOURCE")
public class ImportInsDataSource implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4232186658123079487L;

  private String token;

  private String sourName;

  private Date creDate;

  private String remark;

  @Id
  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  @Column(name = "SOURCE_NAME")
  public String getSourName() {
    return sourName;
  }

  public void setSourName(String sourName) {
    this.sourName = sourName;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreDate() {
    return creDate;
  }

  public void setCreDate(Date creDate) {
    this.creDate = creDate;
  }

  @Column(name = "REMARK")
  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

}
