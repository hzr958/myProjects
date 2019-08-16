package com.smate.web.v8pub.utils;

import net.sf.json.*;
import net.sf.json.util.CycleDetectionStrategy;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * json工具类.
 * 
 * @author liqinghua
 * 
 */
public class JsonUtils {

  /**
   * 配置json-lib需要的excludes和datePattern.
   * 
   * @param excludes 不需要转换的属性数组
   * @param datePattern 日期转换模式
   * @return JsonConfig 根据excludes和dataPattern生成的jsonConfig，用于write
   */
  /*
   * public static JsonConfig configJson(String[] excludes, String datePattern) { JsonConfig
   * jsonConfig = new JsonConfig(); if (excludes != null) { jsonConfig.setExcludes(excludes); }
   * jsonConfig.setIgnoreDefaultExcludes(false);
   * jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
   * jsonConfig.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor(datePattern));
   * 
   * return jsonConfig; }
   */

  /**
   * 配置json-lib需要的excludes不需要转换的属性数组.
   * 
   * @param excludes 根据excludes生成的jsonConfig，用于write
   * @return
   */
  public static JsonConfig configJson(String[] excludes) {
    JsonConfig jsonConfig = new JsonConfig();
    jsonConfig.setExcludes(excludes);
    jsonConfig.setIgnoreDefaultExcludes(false);
    jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);

    return jsonConfig;
  }

  /**
   * 将JSON串转换成LIST.
   * 
   * <pre>
   * eg:
   * String str = "[{assignId:1000,result:1,pubAddrs:[{orgId:1001,result:2,confmName:'peking univ'},{orgId:1002,result:1,confmName:'beijing univ'}]}]";
   * Map<String, Class> m = new HashMap<String, Class>();
   * m.put("pubAddrs", PubConfirmAddr.class);
   * List<PubConfirm> list = JsonUtils.covertToList(str, PubConfirm.class, m);
   * </pre>
   * 
   * @param arrayStr
   * @param objectClass
   * @param map
   * @return
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public static List covertToList(String arrayStr, Class objectClass, Map<String, Class> map) {

    List list = new ArrayList();
    if (StringUtils.isBlank(arrayStr)) {
      return list;
    }
    if (!net.sf.json.util.JSONUtils.mayBeJSON(arrayStr)) {
      return list;
    }
    JSONArray ay = JSONArray.fromObject(arrayStr);
    if (ay.size() == 0) {
      return list;
    }
    for (int i = 0; i < ay.size(); i++) {
      JSONObject obj = (JSONObject) ay.get(i);
      if (map != null) {
        list.add(JSONObject.toBean(obj, objectClass, map));
      } else {
        list.add(JSONObject.toBean(obj, objectClass));
      }
    }
    return list;
  }

  /**
   * 从一个JSON 对象字符格式中得到一个java对象.
   * 
   * @param jsonString
   * 
   * 
   * @param pojoCalss
   * 
   * 
   * @return
   */

  @SuppressWarnings("rawtypes")
  public static Object getObject4JsonString(String jsonString, Class pojoCalss) {
    Object pojo;
    JSONObject jsonObject = JSONObject.fromObject(jsonString);
    pojo = JSONObject.toBean(jsonObject, pojoCalss);
    return pojo;
  }

  /**
   * 从json HASH表达式中获取一个map，改map支持嵌套功能.
   * 
   * @param jsonString
   * 
   * 
   * @return
   */

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static Map getMap4Json(String jsonString) {
    JSONObject jsonObject = JSONObject.fromObject(jsonString);
    Iterator keyIter = jsonObject.keys();
    String key;
    Object value;
    Map valueMap = new HashMap();
    while (keyIter.hasNext()) {
      key = (String) keyIter.next();
      value = jsonObject.get(key);
      valueMap.put(key, value);
    }
    return valueMap;

  }

  /**
   * 从json数组中得到相应java数组 .
   * 
   * @param jsonString
   * 
   * 
   * @return
   */

  public static Object[] getObjectArray4Json(String jsonString) {
    JSONArray jsonArray = JSONArray.fromObject(jsonString);
    return jsonArray.toArray();
  }

  /**
   * 从json对象集合表达式中得到一个java对象列表 .
   * 
   * @param jsonString
   * 
   * 
   * @param pojoClass
   * 
   * 
   * @return
   */

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static List getList4Json(String jsonString, Class pojoClass) {
    JSONArray jsonArray = JSONArray.fromObject(jsonString);
    JSONObject jsonObject;
    Object pojoValue;
    List list = new ArrayList();
    for (int i = 0; i < jsonArray.size(); i++) {
      jsonObject = jsonArray.getJSONObject(i);
      pojoValue = JSONObject.toBean(jsonObject, pojoClass);
      list.add(pojoValue);
    }
    return list;

  }

  /**
   * 从json数组中解析出java字符串数组.
   * 
   * @param jsonString
   * 
   * @return
   * 
   *
   * 
   */

  public static String[] getStringArray4Json(String jsonString) {
    JSONArray jsonArray = JSONArray.fromObject(jsonString);

    String[] stringArray = new String[jsonArray.size()];

    for (int i = 0; i < jsonArray.size(); i++) {

      stringArray[i] = jsonArray.getString(i);

    }
    return stringArray;

  }

  /**
   * 从json数组中解析出javaLong型对象数组.
   * 
   * @param jsonString
   * 
   * @return
   * 
   *
   * 
   */

  public static Long[] getLongArray4Json(String jsonString) {

    JSONArray jsonArray = JSONArray.fromObject(jsonString);

    Long[] longArray = new Long[jsonArray.size()];

    for (int i = 0; i < jsonArray.size(); i++) {

      longArray[i] = jsonArray.getLong(i);

    }

    return longArray;

  }

  /**
   * 从json数组中解析出java Integer型对象数组.
   * 
   * @param jsonString
   * 
   * @return
   * 
   *
   * 
   */
  public static Integer[] getIntegerArray4Json(String jsonString) {

    JSONArray jsonArray = JSONArray.fromObject(jsonString);

    Integer[] integerArray = new Integer[jsonArray.size()];

    for (int i = 0; i < jsonArray.size(); i++) {

      integerArray[i] = jsonArray.getInt(i);

    }

    return integerArray;

  }

  /**
   * 从json数组中解析出java Date 型对象数组，使用本方法必须保证.
   * 
   * @param jsonString
   * 
   * @return
   * 
   * 
   * 
   * 
   * @throws ParseException
   */

  public static Date[] getDateArray4Json(String jsonString, String dataFormat) throws ParseException {

    JSONArray jsonArray = JSONArray.fromObject(jsonString);

    Date[] dateArray = new Date[jsonArray.size()];

    String dateString;

    Date date;

    for (int i = 0; i < jsonArray.size(); i++) {

      dateString = jsonArray.getString(i);

      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
      date = df.parse(dateString);

      dateArray[i] = date;

    }

    return dateArray;

  }

  /**
   * 从json数组中解析出java Integer型对象数组.
   * 
   * @param jsonString
   * 
   * @return
   * 
   *
   * 
   */

  public static Double[] getDoubleArray4Json(String jsonString) {

    JSONArray jsonArray = JSONArray.fromObject(jsonString);

    Double[] doubleArray = new Double[jsonArray.size()];

    for (int i = 0; i < jsonArray.size(); i++) {

      doubleArray[i] = jsonArray.getDouble(i);

    }

    return doubleArray;

  }

  /**
   * 将java对象转换成json字符串.
   * 
   * @param javaObj
   * 
   * @return
   * 
   *
   * 
   */

  public static String getJsonString4JavaPOJO(Object javaObj) {

    JSONObject json;

    json = JSONObject.fromObject(javaObj);

    return json.toString();

  }

  /**
   * 将java对象转换成json字符串.
   * 
   * @param pojo
   * @param excludes
   * @return
   */
  public static String getJsonString(Object pojo, String[] excludes) {
    JsonConfig jsonConfig = JsonUtils.configJson(excludes);
    JSON json = JSONSerializer.toJSON(pojo, jsonConfig);
    return json.toString();
  }

}
