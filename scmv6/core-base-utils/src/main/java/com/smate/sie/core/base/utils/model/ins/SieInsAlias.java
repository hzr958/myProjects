package com.smate.sie.core.base.utils.model.ins;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 检索式表
 * 
 * @author fanzhiqiang
 * 
 */
@Entity
@Table(name = "INS_ALIAS")
public class SieInsAlias implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8732787080116800310L;
  // 主键
  private SieInsAliasId sieInsAliasId;
  // 单位对应文献库的别名
  private String name;
  // 数据库名称
  private String dbName;

  public SieInsAlias() {
    super();
  }

  public SieInsAlias(Long insId, Long dbId, String name) {
    super();
    this.sieInsAliasId = new SieInsAliasId(insId, dbId);
    this.name = name;
  }

  @EmbeddedId
  public SieInsAliasId getInsAliasId() {
    return sieInsAliasId;
  }

  public void setInsAliasId(SieInsAliasId sieInsAliasId) {
    this.sieInsAliasId = sieInsAliasId;
  }

  @Column(name = "NAME")
  public String getName() {
    return name;
  }

  @Transient
  public String getDbName() {
    return dbName;
  }

  public void setDbName(String dbName) {
    this.dbName = dbName;
  }

  public void setName(String name) {
    this.name = name;
  }

}
