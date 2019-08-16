package com.smate.center.oauth.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description 读取配置文件工具类
 * @author xiexing
 * @date 2019年2月18日
 */
public class PropertiesUtils {
  private static final Logger logger = LoggerFactory.getLogger(PropertiesUtils.class);
  private static final String fileUrl = "/qqlogin/qqconnectconfig." + System.getenv("RUN_ENV") + ".properties";

  public static Map<Object, Object> map = new HashMap<Object, Object>();

  static {
    try {
      map = init(fileUrl);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }


  public static Map<Object, Object> init(String fileUrl) throws IOException {
    if (StringUtils.isEmpty(fileUrl)) {
      return null;
    }
    Properties properties = new Properties();
    Map<Object, Object> paramMap = new HashMap<Object, Object>();
    InputStream inputStream = null;
    try {
      inputStream = PropertiesUtils.class.getResourceAsStream(fileUrl);
      properties.load(inputStream);
      Set<Entry<Object, Object>> entrySet = properties.entrySet();
      if (isNotEmpty(entrySet)) {
        for (Entry<Object, Object> entry : entrySet) {
          paramMap.put(entry.getKey(), entry.getValue());
        }
      }
    } catch (Exception e) {
      logger.error("读取配置文件出错..", e);
    } finally {
      if (inputStream != null) {
        inputStream.close();
      }
    }
    return paramMap;
  }

  /**
   * 判断单列集合是否为空
   * 
   * @param col
   * @return
   */
  public static boolean isNotEmpty(Collection col) {
    if (col != null && col.size() > 0) {
      return true;
    }
    return false;
  }

}
