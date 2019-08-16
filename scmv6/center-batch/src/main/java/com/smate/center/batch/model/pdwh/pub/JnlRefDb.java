package com.smate.center.batch.model.pdwh.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 第三方数据库表.
 * 
 * @author cwli
 * 
 */
@Entity
@Table(name = "JNL_REF_DB")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class JnlRefDb implements Serializable {

  private static final long serialVersionUID = -3903295592304981435L;

  private Long dbId;

  private String dbCode;

  private String zhName;

  private String enName;

  // 是否公用 0:不公用 1: 公用
  private int isPublic;

  @Id
  @Column(name = "DBID")
  public Long getDbId() {
    return dbId;
  }

  public void setDbId(Long dbId) {
    this.dbId = dbId;
  }

  @Column(name = "DB_CODE")
  public String getDbCode() {
    return dbCode;
  }

  public void setDbCode(String dbCode) {
    this.dbCode = dbCode;
  }

  @Column(name = "ZH_CN_NAME")
  public String getZhName() {
    return zhName;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  @Column(name = "EN_US_NAME")
  public String getEnName() {
    return enName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  @Column(name = "IS_PUBLIC")
  public int getIsPublic() {
    return isPublic;
  }

  public void setIsPublic(int isPublic) {
    this.isPublic = isPublic;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
