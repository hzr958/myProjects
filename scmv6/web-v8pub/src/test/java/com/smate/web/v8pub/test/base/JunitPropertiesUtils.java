package com.smate.web.v8pub.test.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class JunitPropertiesUtils {

  public static Map<String, String> JUNIT_PROPERTIES;

  /**
   * 根据环境变量加载properties文件
   */
  public static void loadProperties() throws IOException {
    Properties testPro = new Properties();
    InputStream in = null;
    try {
      // 获取设置的环境变量
      String runEnv = System.getenv("RUN_ENV");
      in = new FileInputStream("src/main/resources/junit_config/" + runEnv + "_junit.properties");
      testPro.load(in);
      if (testPro != null) {
        Map<String, String> propertiesMap = new HashMap<String, String>();
        for (Iterator<Object> iterator = testPro.keySet().iterator(); iterator.hasNext();) {
          String name = (String) iterator.next();
          propertiesMap.put(name, testPro.getProperty(name) == null ? "" : testPro.getProperty(name));
        }
        JUNIT_PROPERTIES = Collections.unmodifiableMap(propertiesMap);
      }
    } catch (IOException ex) {
      throw ex;
    } finally {
      if (in != null) {
        in.close();
      }
    }
  }


}
