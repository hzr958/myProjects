package com.smate.sie.web.logger.auditlog.utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

/**
 * 
 * json工具类.
 */
public class JsonUtils {

  /**
   * 从一个JSON 对象字符格式中得到一个java对象.
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
   */
  @SuppressWarnings({"rawtypes"})
  public static Map<String, Object> getMap4Json(String jsonString) {
    JSONObject jsonObject = JSONObject.fromObject(jsonString);
    Iterator keyIter = jsonObject.keys();
    String key;
    Object value;
    Map<String, Object> valueMap = new HashMap<String, Object>();
    while (keyIter.hasNext()) {
      key = (String) keyIter.next();
      value = jsonObject.get(key);
      valueMap.put(key, value);
    }
    return valueMap;

  }

  /**
   * 从json数组中得到相应java数组 .
   */
  public static Object[] getObjectArray4Json(String jsonString) {
    JSONArray jsonArray = JSONArray.fromObject(jsonString);
    return jsonArray.toArray();
  }

  /**
   * 从json对象集合表达式中得到一个java对象列表 .
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
   */
  public static String getJsonString4JavaPOJO(Object javaObj) {
    JSONObject json;
    json = JSONObject.fromObject(javaObj);
    return json.toString();
  }

  /**
   * 配置json-lib需要的excludes不需要转换的属性数组.excludes 根据excludes生成的jsonConfig，用于write
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
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public static List covertToList(String arrayStr, Class objectClass, Map<String, Class> map) {
    List list = new ArrayList();
    if (StringUtils.isEmpty(arrayStr)) {
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

  private static final String dateformater = "yyyy-MM-dd hh:mm:ss";

  /**
   * 
   * <pre>
   *       Description:
   *       	统一方法调用	
   *       &#64;param obj
   *       &#64;return
   *       &#64;throws Exception
   *       Arlon.Yang created this method at 2011-2-4 22:52:29
   * </pre>
   */
  @SuppressWarnings("rawtypes")
  public static String FormatToJsonstr(Object obj) {
    StringBuffer sbf = null;
    try {
      if (obj == null) {
        return "\"\"";
      } else if (obj instanceof String || obj instanceof Integer || obj instanceof Double || obj instanceof Float
          || obj instanceof Long || obj instanceof Boolean || obj instanceof Character || obj instanceof Byte) {
        return "\"" + string2Json(obj.toString()) + "\"";
      } else if (obj instanceof BigDecimal) {
        return "\"" + string2Json(obj.toString()) + "\"";
      }

      else if (obj instanceof Date) {
        Date d = (Date) obj;
        SimpleDateFormat sdf = new SimpleDateFormat(dateformater);
        return "\"" + sdf.format(d) + "\"";
      } else if (obj instanceof java.sql.Date) {
        java.sql.Date d = (java.sql.Date) obj;
        SimpleDateFormat sdf = new SimpleDateFormat(dateformater);
        return "\"" + sdf.format(d) + "\"";
      } else if (obj instanceof Timestamp) {
        Timestamp ts = (Timestamp) obj;
        SimpleDateFormat sdf = new SimpleDateFormat(dateformater);
        return "\"" + sdf.format(new Date(ts.getTime())) + "\"";
      } else if (obj instanceof Collection) {
        sbf = FormatCollection((Collection) obj);
      }

      else if (obj instanceof Map) {
        sbf = FormatMap((Map) obj);
      }

      else if (obj instanceof Object[]) {
        sbf = FormatObjectAttr((Object[]) obj);
      } else {
        sbf = FormatPojo(obj);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return sbf.toString();
  }

  /**
   * JSON字符串特殊字符处理，比如：“\A1;1300”
   */
  public static String string2Json(String s) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      switch (c) {
        case '\"':
          sb.append("\\\"");
          break;
        case '\\':
          sb.append("\\\\");
          break;
        case '/':
          sb.append("\\/");
          break;
        case '\b':
          sb.append("\\b");
          break;
        case '\f':
          sb.append("\\f");
          break;
        case '\n':
          sb.append("\\n");
          break;
        case '\r':
          sb.append("\\r");
          break;
        case '\t':
          sb.append("\\t");
          break;
        default:
          sb.append(c);
      }
    }
    return sb.toString();
  }

  /**
   * <pre>
   *      Description:
   *      		格式化 List,Set 以及其实现子类的数据(支持深度赋值)
   *      &#64;param col
   *      &#64;return
   *      &#64;throws Exception
   *      Arlon.Yang created this method at 2011-2-4 22:52:52
   * </pre>
   */
  @SuppressWarnings("rawtypes")
  private static StringBuffer FormatCollection(Collection col) throws Exception {
    StringBuffer sbf = new StringBuffer();
    if (col == null || col.size() == 0) {
      sbf.append("[]");
      return sbf;
    }
    sbf.append("[");
    for (Object obj : col) {
      sbf.append(FormatToJsonstr(obj));
      sbf.append(",");
    }
    if (sbf.length() != -1) {
      sbf.deleteCharAt(sbf.length() - 1).append("]");
    } else {
      sbf.append("]");
    }
    return sbf;
  }

  /**
   * <pre>
   *      Description:
   *      		格式化Map数据
   *      &#64;param map
   *      &#64;return
   *      &#64;throws Exception
   *      Arlon.Yang created this method at 2011-2-4 22:53:04
   * </pre>
   */
  @SuppressWarnings("rawtypes")
  private static StringBuffer FormatMap(Map map) throws Exception {
    StringBuffer sbf = new StringBuffer();
    sbf.append("{");
    for (Object obj : map.keySet()) {
      sbf.append("\"" + obj + "\":");
      sbf.append(FormatToJsonstr(map.get(obj)));
      sbf.append(",");
    }

    if (sbf.length() != 1) {
      sbf.deleteCharAt(sbf.length() - 1).append("}");
    } else {
      sbf.append("}");
    }
    return sbf;
  }

  /**
   * <pre>
   *      Description:
   *      		格式化单个的pojo(利用snitf工具生成格式的Pojo其余的适应)
   *      &#64;param obj
   *      &#64;return
   *      Arlon.Yang created this method at 2011-2-4 22:53:15
   * </pre>
   */
  @SuppressWarnings("rawtypes")
  private static StringBuffer FormatPojo(Object obj) {
    StringBuffer sbf = new StringBuffer();
    Class c = null;
    Object resultobject = null;
    sbf.append("{");
    try {
      c = obj.getClass();
      /** Snitf 类中的Pojo */
      /** 获取所有的字段 */
      for (Field field : c.getDeclaredFields()) {
        field.setAccessible(true);
        resultobject = field.get(obj);
        sbf.append("\"" + field.getName() + "\":" + FormatToJsonstr(resultobject) + ",");
      }

      if (sbf.length() != 1) {
        sbf.deleteCharAt(sbf.length() - 1).append("}");
      } else {
        sbf.deleteCharAt(sbf.length() - 1);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      resultobject = null;
      c = null;
    }
    return sbf;
  }

  /**
   * <pre>
   *      Description:
   *      		格式化数组对象
   *      &#64;param objattr
   *      &#64;return
   *      &#64;throws Exception
   *      Arlon.Yang created this method at 2011-2-4 22:53:42
   * </pre>
   */
  private static StringBuffer FormatObjectAttr(Object[] objattr) throws Exception {
    StringBuffer sbf = new StringBuffer();
    sbf.append("[");
    for (Object obj : objattr) {
      sbf.append(FormatToJsonstr(obj));
      sbf.append(",");
    }
    if (sbf.length() != 1) {
      sbf.deleteCharAt(sbf.length() - 1).append("]");
    } else {
      sbf.append("]");
    }
    return sbf;
  }

  /** 判断是否为json字符串 */
  public static boolean isJson(String jsonString) {
    if (jsonString == null) {
      return false;
    }
    try {
      JSONObject.fromObject(jsonString);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 解析JSON格式数据，解析为Map或List，可无限嵌套解析.
   * 
   * @param resource json字符串
   * @return
   * @throws Exception
   */
  public static Object parseJSON(String resource) throws Exception {
    if (StringUtils.isBlank(resource)) {
      return null;
    }
    resource = resource.trim();
    if (resource.startsWith("{")) {
      return parseJSONObject(JSONObject.fromObject(resource));
    } else if (resource.startsWith("[")) {
      return parseJSONArray(JSONArray.fromObject(resource));
    } else {
      return null;
    }
  }

  /**
   * 解析JSON对象.
   * 
   * @param json
   * @return
   */
  @SuppressWarnings("rawtypes")
  public static Map<String, Object> parseJSONObject(JSONObject json) {
    Map<String, Object> data = new HashMap<String, Object>();

    Iterator it = json.keys();
    while (it.hasNext()) {
      String key = it.next().toString();
      Object obj = json.get(key);
      key = key.toLowerCase();

      if (obj instanceof JSONObject) {
        data.put(key, parseJSONObject((JSONObject) obj));
      } else if (obj instanceof JSONArray) {
        data.put(key, parseJSONArray((JSONArray) obj));
      } else {
        data.put(key, org.apache.commons.lang3.ObjectUtils.toString(obj));
      }
    }

    return data;
  }

  /**
   * 解析JSON数组.
   * 
   * @param json
   * @return
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public static List parseJSONArray(JSONArray json) {
    List data = new ArrayList();

    for (int i = 0; i < json.size(); i++) {
      Object obj = json.get(i);

      if (obj instanceof JSONObject) {
        data.add(parseJSONObject((JSONObject) obj));
      } else if (obj instanceof JSONArray) {
        data.add(parseJSONArray((JSONArray) obj));
      } else {
        data.add(org.apache.commons.lang3.ObjectUtils.toString(obj));
      }
    }

    return data;
  }

  /**
   * 从List或Map中取值，支持嵌套取值. <br/>
   * <br/>
   * JSON字符串： {"name":"zhangsan", "age":"20", "cars":[{"name":"car name 1", "money":200}, {"name":"car
   * name 2", "money":300}]} <br/>
   * <br/>
   * 经过解析后取值示例：<br/>
   * 示例一：key=name <br/>
   * 结果一：value=zhangsan<br/>
   * <br/>
   * 示例二：key=cars <br/>
   * 结果二：value=[{name=123, money=200}, {name=456, money=300}]<br/>
   * <br/>
   * 示例三：key=cars[0] <br/>
   * 结果三： value={name=123, money=200} <br/>
   * <br/>
   * 示例四：key=cars[0].name<br/>
   * 结果四： value=car name 1 <br/>
   * <br/>
   * <br/>
   * <br/>
   * <br/>
   * JSON字符串： [{"name":"car name 3", "money":200}, {"name":"2222222", "money":300}]<br/>
   * <br/>
   * 经过解析后取值示例：<br/>
   * 示例一：key=name <br/>
   * 结果一：value=car name 3,2222222<br/>
   * <br/>
   * 示例二：key=[1] <br/>
   * 结果二：value={name=2222222, money=300}<br/>
   * <br/>
   * 示例三：key=[0].name <br/>
   * 结果三： value=car name 3 <br/>
   * <br/>
   * 示例四：key=[0].money<br/>
   * 结果四： value=200 <br/>
   * 
   * @param data List或Map
   * @param key
   * 
   * @return
   */
  public static Object getValue(Object data, String key) {
    String[] keys = key.split("\\.");

    return getValueSub(data, keys, 0, -1);
  }

  @SuppressWarnings("rawtypes")
  private static Object getValueSub(Object data, String[] keys, int index, int arrayIndex) {
    if (data == null) {
      return null;
    }
    if (index == keys.length) {
      return data;
    }

    Object obj = null;
    String key = keys[index];
    int arrSep = key.indexOf("[");
    if (arrSep != -1) {
      arrayIndex = Integer.valueOf(key.substring(arrSep + 1, key.length() - 1));
      key = key.substring(0, arrSep);
      if ("".equals(key)) {
        index++;
      }
    } else {
      arrayIndex = -1;
    }

    if (data instanceof Map) {
      obj = ((Map) data).get(key);
      index++;
    } else if (data instanceof List) {
      StringBuffer buffer = new StringBuffer();
      int i = 0;
      for (Object tmp : (List) data) {
        if (arrayIndex == -1 || ("".equals(key) && arrayIndex == i)) {
          buffer.append(",");
          buffer.append(getValueSub(tmp, keys, index, arrayIndex));
        }
        i++;
      }
      if (buffer.length() > 0) {
        buffer.deleteCharAt(0);
      }
      obj = buffer.toString();
      index = keys.length;
    }

    if (arrayIndex != -1) {
      if (obj instanceof List)
        obj = ((List) obj).get(arrayIndex);
    }

    return getValueSub(obj, keys, index, arrayIndex);

  }

  /**
   * 下一个字符.
   * 
   * @param resource
   * @param index
   * @return
   */
  public static Character nextChar(String resource, int index) {
    if (index + 1 < resource.length()) {
      return resource.charAt(index + 1);
    }
    return null;
  }

  /**
   * 上一个字符.
   * 
   * @param resource
   * @param index
   * @return
   */
  public static Character prevChar(String resource, int index) {
    if (index - 1 >= 0) {
      return resource.charAt(index - 1);
    }
    return null;
  }

  /**
   * 下一个key-value中的value.
   * 
   * @param resource
   * @param index
   * @return
   */
  private static String nextValue(String resource, int index) {
    int idx = resource.indexOf(",", index);
    if (idx == -1) {
      idx = resource.length();
    }
    if (index + 1 < resource.length()) {
      return resource.substring(index + 1, idx);
    }
    return null;
  }

  /**
   * 将Map形式的字符串转换成JSON格式.
   * 
   * @param resource
   * @return
   */
  public static String formatMapToJson(String resource) {
    if (resource.indexOf(":") != -1) {
      resource = resource.replace(":", "=");
    }
    if (resource.indexOf("\"") != -1) {
      resource = resource.replace("\"", "#@#");
    }
    int valueBeginFlag = 0;
    int valueEndIndex = 0;

    StringBuffer buffer = new StringBuffer();
    int equalCount = 0;
    for (int index = 0; index < resource.length(); index++) {
      char ch = resource.charAt(index);
      switch (ch) {
        case ',':
          buffer.append(ch);
          break;
        case ' ':
          buffer.append(ch);
          break;
        case '=':
          valueBeginFlag = 1;
          if (equalCount == 0) {
            buffer.append("\"");
            buffer.append(":");
            Character nextChar = nextChar(resource, index);
            if (nextChar != null && nextChar != '{' && nextChar != '[') {
              buffer.append("\"");
            }
            if ("null".equals(nextValue(resource, index))) {
              index += 4;
            }
          } else {
            buffer.append(ch);
          }
          equalCount++;
          break;
        case '{':
        case '[':
          buffer.append(ch);
          Character nextChar = nextChar(resource, index);
          if (nextChar != null && nextChar != '{' && nextChar != '[') {
            buffer.append("\"");
          }
          equalCount = 0;
          break;
        case '}':
        case ']':
          Character prevChar = prevChar(resource, index);
          if (prevChar != '}' && prevChar != ']') {
            buffer.append("\"");
          }
          buffer.append(ch);
          break;
        case '\"':
          buffer.append("\\\"");
          break;

        default:
          buffer.append(ch);
          break;
      }

      if (valueBeginFlag == 1) {
        if (index == resource.length()) {
          valueEndIndex = resource.length() - 1;
        } else {
          int eqIndex = index + 1;
          for (; eqIndex < resource.length(); eqIndex++) {
            if (resource.charAt(eqIndex) == '=') {
              break;
            }
          }
          valueEndIndex = resource.substring(0, eqIndex).lastIndexOf(',');
          if (eqIndex == resource.length()) {
            valueEndIndex = resource.length() - 1;
          }
        }
        valueBeginFlag = 0;
      }
      if (index == valueEndIndex && index != 0) {
        if (index != resource.length() - 1) {
          if (buffer.charAt(buffer.length() - 1) == ',') {
            buffer.deleteCharAt(buffer.length() - 1);
          }
          Character prevChar = prevChar(resource, index);
          if (prevChar != '}' && prevChar != ']') {
            buffer.append("\"");
          }
          buffer.append(",");

          Character nextChar = null;
          // 跳过空格
          for (; index + 1 < resource.length(); index++) {
            nextChar = resource.charAt(index + 1);
            if (nextChar == ' ') {
              continue;
            } else {
              break;
            }
          }
          if (nextChar != null && nextChar != '{' && nextChar != '[') {
            buffer.append("\"");
          }
          equalCount = 0;
        }
      }
    }
    // 出现["{'key':value}"]时不能把‘“’保留，应去掉，否则不满足json字符串格式，无法解析
    if (buffer.indexOf("#@#") != -1) {
      // return buffer.toString().replace("#@#", "\\\"");
      return buffer.toString().replace("\"#@#", "").replace("#@#\"", "");
    }
    return buffer.toString();
  }

  /**
   * 通过String获得List<Map<String,Object>> <br/>
   * json格式 [{a:vv,b:dd},{a:vv,b:dd}]
   */
  public static List<Map<String, Object>> getListMap(String json) {
    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    JSONArray jsonArray = JSONArray.fromObject(json);
    JSONObject jsonObject;
    for (int i = 0; i < jsonArray.size(); i++) {
      jsonObject = jsonArray.getJSONObject(i);
      list.add(getMap(jsonObject.toString()));
    }
    return list;
  }

  private static Map<String, Object> getMap(String jsonString) {
    Map<String, Object> valueMap = new HashMap<String, Object>();
    JSONObject jsonObject = JSONObject.fromObject(jsonString);
    @SuppressWarnings("unchecked")
    Iterator<String> keyIter = jsonObject.keys();
    while (keyIter.hasNext()) {
      String key = keyIter.next();
      Object value = jsonObject.get(key);
      valueMap.put(key, value);
    }
    return valueMap;
  }
}
