package com.smate.web.mobile.v8pub.vo.pdwh;

/**
 * ConstRefDb表对应的值操作对象
 * 
 * @author wsn
 * @date 2018年8月9日
 */
public class ConstRefDbVO {

  private Long id;// 文献库ID (system generated key)
  private String code;// 文献库code
  private String zhCnName; // 文献库中文名称
  private String enUsName;// 文献库英文名
  private String dbName;// 根据语言环境显示的数据库名称
  private String actionUrlInside; // 用于校内查询的url
  private String actionUrl;// 用于校外查询的url
  private String loginUrl;// 用于校外登录的url
  private String loginUrlInside;// 用户进行登录验证的URL
  // private String fulltextUrl;// 校外全文url的域名
  private String fulltextUrlInside;// 校内全文url的域名
  private String suportLang;// 表示此数据库唯一支持的语言种类，如果为空则表示支持多种语言
  private int isPublic;// 是否公用 0:不公用 1: 公用
  private int enSortKey;// 英文版本时显示顺序
  private int zhSortKey;// 中文版本时显示顺序
  private String dbType;// 标识该数据库适用于哪个查询方式 1: 成果 2 : 文献 4: 项目
  private int dbBitCode;// 标识该数据库使用哪个插件（插件ID）
  private String zhAbbrName;// 文献库简写中文名称
  private String enAbbrName;// 文献库简写英文名称
  private String batchQuery;// 批量查询？？？？
  private String nameDetails; // 文献库全称，页面根据语言环境显示用

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public void setZhCnName(String zhCnName) {
    this.zhCnName = zhCnName;
  }

  public String getEnUsName() {
    return enUsName;
  }

  public void setEnUsName(String enUsName) {
    this.enUsName = enUsName;
  }

  public String getActionUrlInside() {
    return actionUrlInside;
  }

  public void setActionUrlInside(String actionUrlInside) {
    this.actionUrlInside = actionUrlInside;
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

  public void setEnSortKey(int enSortKey) {
    this.enSortKey = enSortKey;
  }

  public int getZhSortKey() {
    return zhSortKey;
  }

  public void setZhSortKey(int zhSortKey) {
    this.zhSortKey = zhSortKey;
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

  public String getZhAbbrName() {
    return zhAbbrName;
  }

  public void setZhAbbrName(String zhAbbrName) {
    this.zhAbbrName = zhAbbrName;
  }

  public String getEnAbbrName() {
    return enAbbrName;
  }

  public void setEnAbbrName(String enAbbrName) {
    this.enAbbrName = enAbbrName;
  }

  public String getBatchQuery() {
    return batchQuery;
  }

  public void setBatchQuery(String batchQuery) {
    this.batchQuery = batchQuery;
  }

  public String getDbName() {
    return dbName;
  }

  public void setDbName(String dbName) {
    this.dbName = dbName;
  }

  public String getNameDetails() {
    return nameDetails;
  }

  public void setNameDetails(String nameDetails) {
    this.nameDetails = nameDetails;
  }

}
