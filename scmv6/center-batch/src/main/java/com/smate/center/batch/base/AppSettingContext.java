package com.smate.center.batch.base;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 服务配置信息，必须在服务器已经启动完成才可使用.
 * 
 * @author liqinghua
 * 
 */
public class AppSettingContext implements InitializingBean {

  private static AppSettingService appSettingServiceStatic;
  @Autowired
  private AppSettingService appSettingService;

  /**
   * 获取配置值.
   * 
   * @param key
   * @return
   */
  public static String getValue(String key) {
    return appSettingServiceStatic.getValue(key);
  }

  /**
   * 获取int配置值.
   * 
   * @param key
   * @return
   */
  public static Integer getIntValue(String key) {
    return appSettingServiceStatic.getIntValue(key);
  }

  /**
   * 获取long配置值.
   * 
   * @param key
   * @return
   */
  public static Long getLongValue(String key) {
    return appSettingServiceStatic.getLongValue(key);
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    AppSettingContext.appSettingServiceStatic = appSettingService;
  }
}
