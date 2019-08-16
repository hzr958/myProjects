package com.smate.sie.center.task.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 第三方数据库表.
 * 
 * @author jszhou
 *
 */
@Entity
@Table(name = "CONST_REF_DB")
public class SieConstRefDb implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3847807049020275021L;

  // RefDB ID (system generated key)
  private Long id;

  // RefDB Code
  private String code;

  // RefDB name
  private String zhCnName;

  // Foreign language RefDB name
  private String enUsName;

  private String dbName;

  // 用于校内查询的url
  private String actionUrlInside;

  // 用于校外查询的url
  private String actionUrl;
  // 用于校外登录的url
  private String loginUrl;

  // 用户进行登录验证的URL
  private String loginUrlInside;
  // 校外全文url的域名
  private String fulltextUrl;
  // 校内全文url的域名
  private String fulltextUrlInside;
  // 表示此数据库唯一支持的语言种类，如果为空则表示支持多种语言
  private String suportLang;

  // 是否公用 0:不公用 1: 公用
  private int isPublic;

  // 英文版本时显示顺序
  private int enSortKey;

  // 中文版本时显示顺序
  private int zhSortKey;

  // 标识该数据库适用于哪个查询方式 1: 成果 2 : 文献 4: 项目
  private String dbType;

  // 标识该数据库使用哪个插件（插件ID）
  private int dbBitCode;
  // 简写
  private String zhAbbrName;
  private String enAbbrName;
  private String batchQuery;

  @Id
  @Column(name = "DBID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "ZH_ABBR_NAME")
  public String getZhAbbrName() {
    return zhAbbrName;
  }

  @Column(name = "EN_ABBR_NAME")
  public String getEnAbbrName() {
    return enAbbrName;
  }

  public void setZhAbbrName(String zhAbbrName) {
    this.zhAbbrName = zhAbbrName;
  }

  public void setEnAbbrName(String enAbbrName) {
    this.enAbbrName = enAbbrName;
  }

  @Column(name = "DB_CODE")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Column(name = "ZH_CN_NAME")
  public String getZhCnName() {
    return zhCnName;
  }

  public void setZhCnName(String zhcnName) {
    this.zhCnName = zhcnName;
  }

  @Column(name = "EN_US_NAME")
  public String getEnUsName() {
    return enUsName;
  }

  public void setEnUsName(String ename) {
    this.enUsName = ename;
  }

  @Column(name = "ONLY_SUPPORT_LANG")
  public String getSuportLang() {
    return suportLang;
  }

  public void setSuportLang(String suportLang) {
    this.suportLang = suportLang;
  }

  @Column(name = "IS_PUBLIC")
  public int getIsPublic() {
    return isPublic;
  }

  public void setIsPublic(int isPublic) {
    this.isPublic = isPublic;
  }

  @Column(name = "EN_SORT_KEY")
  public int getEnSortKey() {
    return enSortKey;
  }

  public void setEnSortKey(int esortKey) {
    this.enSortKey = esortKey;
  }

  @Column(name = "ZH_SORT_KEY")
  public int getZhSortKey() {
    return zhSortKey;
  }

  public void setZhSortKey(int csortKey) {
    this.zhSortKey = csortKey;
  }

  @Column(name = "DB_TYPE")
  public String getDbType() {
    return dbType;
  }

  public void setDbType(String dbType) {
    this.dbType = dbType;
  }

  @Column(name = "DB_BIT_CODE")
  public int getDbBitCode() {
    return dbBitCode;
  }

  public void setDbBitCode(int dbBitCode) {
    this.dbBitCode = dbBitCode;
  }

  @Column(name = "ACTION_URL_INSIDE")
  public String getActionUrlInside() {
    return actionUrlInside;
  }

  public void setActionUrlInside(String actionUrlInside) {
    this.actionUrlInside = actionUrlInside;
  }

  @Column(name = "LOGIN_URL_INSIDE")
  public String getLoginUrlInside() {
    return loginUrlInside;
  }

  public void setLoginUrlInside(String loginUrlInside) {
    this.loginUrlInside = loginUrlInside;
  }

  @Column(name = "FULLTEXT_URL_INSIDE")
  public String getFulltextUrlInside() {
    return fulltextUrlInside;
  }

  public void setFulltextUrlInside(String fulltextUrlInside) {
    this.fulltextUrlInside = fulltextUrlInside;
  }

  @Transient
  public String getFulltextUrl() {
    return fulltextUrl;
  }

  public void setFulltextUrl(String fulltextUrl) {
    this.fulltextUrl = fulltextUrl;
  }

  @Column(name = "ACTION_URL")
  public String getActionUrl() {
    return actionUrl;
  }

  public void setActionUrl(String actionUrl) {
    this.actionUrl = actionUrl;
  }

  @Column(name = "LOGIN_URL")
  public String getLoginUrl() {
    return loginUrl;
  }

  public void setLoginUrl(String loginUrl) {
    this.loginUrl = loginUrl;
  }

  @Transient
  public String getDbName() {
    return dbName;
  }

  public void setDbName(String dbName) {
    this.dbName = dbName;
  }

  @Column(name = "BATCH_QUERY")
  public String getBatchQuery() {
    return batchQuery;
  }

  public void setBatchQuery(String batchQuery) {
    this.batchQuery = batchQuery;
  }
}
