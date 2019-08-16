package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * @author fanzhiqiang
 * 
 */
@Entity
@Table(name = "INS_ALIAS")
public class InsAlias implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3896568506542539134L;
  // 主键
  private InsAliasId insAliasId;
  // 单位对应文献库的别名
  private String name;
  // 数据库名称
  private String dbName;

  public InsAlias() {
    super();
  }

  public InsAlias(Long insId, Long dbId, String name) {
    super();
    this.insAliasId = new InsAliasId(insId, dbId);
    this.name = name;
  }

  @EmbeddedId
  public InsAliasId getInsAliasId() {
    return insAliasId;
  }

  public void setInsAliasId(InsAliasId insAliasId) {
    this.insAliasId = insAliasId;
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
