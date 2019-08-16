package com.smate.core.base.utils.dynamicds;

/**
 * 自定义数据源上下文
 * 
 * @author zk
 *
 */
public class CustomerContextHolder {

  private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

  public static void setDataSource(String dataSource) {
    contextHolder.set(dataSource);
  }

  /**
   * 统一使用注解切换数据源
   */
  @Deprecated
  public static void setCasDataSource() {
    contextHolder.set(DataSourceEnum.DB_CAS.toString());
  }

  @Deprecated
  public static void setSnsDataSource() {
    contextHolder.set(DataSourceEnum.DB_SNS.toString());
  }

  @Deprecated
  public static void setRcmdDataSource() {
    contextHolder.set(DataSourceEnum.DB_RCMD.toString());
  }

  @Deprecated
  public static void setSieDataSource() {
    contextHolder.set(DataSourceEnum.DB_SIE.toString());
  }

  @Deprecated
  public static void setEmailsrvDataSource() {
    contextHolder.set(DataSourceEnum.DB_EMAILSRV.toString());
  }

  public static String getDataSource() {
    return contextHolder.get();
  }

  public static void clearDataSource() {
    contextHolder.remove();
  }
}
