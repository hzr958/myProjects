package com.smate.center.task.base;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.exception.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.quartz.AppSettingDao;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 系统配置信息.
 * 
 * @author liqinghua
 * 
 */
@Service("appSettingService")
@Transactional(rollbackFor = Exception.class)
public class AppSettingServiceImpl implements AppSettingService {

  /**
   * 
   */
  private static final long serialVersionUID = 2742777357968589065L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private AppSettingDao appSettingDao;
  // 缓存
  private Map<String, String> cache = new HashMap<String, String>();
  // 用户判断是否过期
  private Date holdDate = null;
  private boolean cacheAble = true;

  @Override
  public String getValue(String key) {

    try {
      clearAll();
      String value = null;
      if (cacheAble) {
        this.cache.get(key);
      }
      if (value == null) {
        value = appSettingDao.getAppSettingValue(key);
        if (value != null && cacheAble) {
          this.cache.put(key, value);
        }
      }
      return value;
    } catch (Exception e) {
      logger.error("获取配置值异常", e);
    }
    return null;
  }

  @Override
  public Integer getIntValue(String key) {
    String value = this.getValue(key);
    if (NumberUtils.isNumber(value)) {
      return Integer.valueOf(value);
    }
    return null;
  }

  @Override
  public Long getLongValue(String key) {

    String value = this.getValue(key);
    if (NumberUtils.isNumber(value)) {
      return Long.valueOf(value);
    }
    return null;
  }

  @Override
  public void clearAll() {
    try {
      if (holdDate == null) {
        holdDate = new Date();
        return;
      }
      Date now = new Date();
      // 超过3分钟，过期
      if (now.getTime() - holdDate.getTime() > 180000) {
        this.cache.clear();
        holdDate = new Date();
      }
    } catch (Exception e) {
      logger.error("清理所有配置信息错误", e);
    }
  }

  @Override
  public void updateSetting(String key, String value) throws BatchTaskException {
    try {
      this.cache.put(key, value);
      appSettingDao.updateAppSetting(key, value);
    } catch (DataException e) {
      logger.error("更新异常，key=" + key + "  value=" + value, e);
      throw new BatchTaskException(e);
    }
  }

  public void setCacheAble(boolean cacheAble) {
    this.cacheAble = cacheAble;
  }

}
