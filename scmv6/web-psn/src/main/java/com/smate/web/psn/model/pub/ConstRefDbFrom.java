package com.smate.web.psn.model.pub;

import java.io.Serializable;

/**
 * 第三方数据库表.
 * 
 * @author cwli
 * 
 */
public class ConstRefDbFrom implements Serializable {



  /**
   * 
   */
  private static final long serialVersionUID = -6423438679638947433L;

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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getZhAbbrName() {
    return zhAbbrName;
  }

  public String getEnAbbrName() {
    return enAbbrName;
  }

  public void setZhAbbrName(String zhAbbrName) {
    this.zhAbbrName = zhAbbrName;
  }

  public void setEnAbbrName(String enAbbrName) {
    this.enAbbrName = enAbbrName;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getZhCnName() {
    return zhCnName;
  }

  public void setZhCnName(String zhcnName) {
    this.zhCnName = zhcnName;
  }

  public String getEnUsName() {
    return enUsName;
  }

  public void setEnUsName(String ename) {
    this.enUsName = ename;
  }

  public String getSuportLang() {
    return suportLang;
  }

  public void setSuportLang(String suportLang) {
    this.suportLang = suportLang;
  }

  public int getIsPublic() {
    return isPublic;
  }

  public void setIsPublic(int isPublic) {
    this.isPublic = isPublic;
  }

  public int getEnSortKey() {
    return enSortKey;
  }

  public void setEnSortKey(int esortKey) {
    this.enSortKey = esortKey;
  }

  public int getZhSortKey() {
    return zhSortKey;
  }

  public void setZhSortKey(int csortKey) {
    this.zhSortKey = csortKey;
  }

  public String getDbType() {
    return dbType;
  }

  public void setDbType(String dbType) {
    this.dbType = dbType;
  }

  public int getDbBitCode() {
    return dbBitCode;
  }

  public void setDbBitCode(int dbBitCode) {
    this.dbBitCode = dbBitCode;
  }

  public String getActionUrlInside() {
    return actionUrlInside;
  }

  public void setActionUrlInside(String actionUrlInside) {
    this.actionUrlInside = actionUrlInside;
  }

  public String getLoginUrlInside() {
    return loginUrlInside;
  }

  public void setLoginUrlInside(String loginUrlInside) {
    this.loginUrlInside = loginUrlInside;
  }

  public String getFulltextUrlInside() {
    return fulltextUrlInside;
  }

  public void setFulltextUrlInside(String fulltextUrlInside) {
    this.fulltextUrlInside = fulltextUrlInside;
  }

  public String getFulltextUrl() {
    return fulltextUrl;
  }

  public void setFulltextUrl(String fulltextUrl) {
    this.fulltextUrl = fulltextUrl;
  }

  public String getActionUrl() {
    return actionUrl;
  }

  public void setActionUrl(String actionUrl) {
    this.actionUrl = actionUrl;
  }

  public String getLoginUrl() {
    return loginUrl;
  }

  public void setLoginUrl(String loginUrl) {
    this.loginUrl = loginUrl;
  }

  public String getDbName() {
    return dbName;
  }

  public void setDbName(String dbName) {
    this.dbName = dbName;
  }

  public String getBatchQuery() {
    return batchQuery;
  }

  public void setBatchQuery(String batchQuery) {
    this.batchQuery = batchQuery;
  }
}
