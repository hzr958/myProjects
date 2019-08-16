package com.smate.center.task.base;

import java.io.Serializable;

import com.smate.core.base.utils.exception.BatchTaskException;


/**
 * 系统配置信息.
 * 
 * @author liqinghua
 * 
 */
public interface AppSettingService extends Serializable {

  /**
   * 获取配置值.
   * 
   * @param key
   * @return
   */
  public String getValue(String key);

  /**
   * 获取int配置值.
   * 
   * @param key
   * @return
   */
  public Integer getIntValue(String key);

  /**
   * 获取long配置值.
   * 
   * @param key
   * @return
   */
  public Long getLongValue(String key);

  /**
   * 清理所有配置信息.
   */
  public void clearAll();

  /**
   * @param key
   */
  public void updateSetting(String key, String value) throws BatchTaskException;
}
