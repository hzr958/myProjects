package com.smate.test.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;

import org.springframework.util.Assert;

/**
 * 操作properties文件工具类
 * 
 * @author wsn
 * @date Jan 14, 2019
 */
public class JunitPropertiesUtils {


  /**
   * 加载properties文件内容到Map中
   */
  public static Map<String, String> loadProperties(String filePath) throws IOException {
    Assert.notNull(filePath, "properties文件路径不能为空");
    Properties testPro = new Properties();
    Map<String, String> propertiesMap = new HashMap<String, String>();
    InputStream in = null;
    try {
      // 读取properties文件，filePath为properties文件路径，如"src/test/java/com/smate/test/base/properties/development_test.properties"
      in = new FileInputStream(filePath);
      testPro.load(in);
      if (testPro != null) {
        for (Iterator<Object> iterator = testPro.keySet().iterator(); iterator.hasNext();) {
          String name = (String) iterator.next();
          propertiesMap.put(name, testPro.getProperty(name) == null ? "" : testPro.getProperty(name));
        }
      }
    } catch (IOException ex) {
      throw ex;
    } finally {
      if (in != null) {
        in.close();
      }
    }
    return propertiesMap;
  }
  /**
   * 读取csv数据驱动文件
   */
  public static Stream<String[]> getCSVFileStream(String filePath) {
    List<String[]> records = new ArrayList<String[]>();
    String record;
    try {
      // 设置字符集为UTF-8
      BufferedReader file = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
      file.readLine();// 忽略CSV文件的标题行（第一行）
      // 遍历读取文件中除第一行外的其它所有行内容，并存储在名为records的ArrayList中，每一个records中存储的对象为一个string数组；
      while ((record = file.readLine()) != null) {
        String[] fields = record.split(",");
        records.add(fields);
      }
      file.close();
    } catch (Exception e) {
      System.out.println(e);
    }
    // 定义函数返回值Object[][],将list转换为一个Object的二维数组；
    String[][] results = new String[records.size()][];
    // 设置二维数组每行的值，每行是一个Object对象
    for (int i = 0; i < records.size(); i++) {
      results[i] = records.get(i);
    }
    return Stream.of(results);
  }
}
