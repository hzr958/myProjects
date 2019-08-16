package com.smate.core.base.utils.dynamicds;

/**
 * 数据源枚举类
 * 
 * @author tsz
 * @since 6.0.1-snapshot
 * @version 6.0.1-snapshot
 */
public enum DataSourceEnum {
  DB_SNS("dssns"), DB_RCMD("dsrcmd"), DB_SIE("dssie"), DB_EMAILSRV("dsesrv"), DB_CAS("dscas"), DB_PDWH("dspdwh");
  // 定义私有变量
  private String value;

  // 构造函数，枚举类型只能为私有
  private DataSourceEnum(String value) {
    this.value = value;

  }

  @Override
  public String toString() {
    return this.value;
  }
}
