package com.smate.core.base.utils.json;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.smate.core.base.utils.model.ObjectMappingCustomer;

/**
 * json工具，jackson转换json-lib效率更高，更完善和准确.
 * 
 * @author zhuangyanming
 */
public class JacksonUtils {

  /**
   * 共用ObjectMapper，提升JSON转化效率
   */
  private static ObjectMapper mapper = new ObjectMapper();
  // 自定义mapper
  private static ObjectMappingCustomer customermapper = new ObjectMappingCustomer();
  private static JsonFactory jsonFactory = new JsonFactory();
  static {
    mapper.setSerializationInclusion(Include.NON_NULL);
    mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
  }

  /**
   * m断 字符串 是不是json 格式的字符串. 只判断json对象格式 {}.
   * 
   * @param content
   * @return
   */
  public static Boolean isJsonString(String content) {
    // 判断 { : 看是否满足格式
    int temp1 = content.indexOf("{");
    int temp2 = content.indexOf("}");
    int temp3 = content.indexOf(":");
    if (temp1 < 0 || temp2 < 0 || temp3 < 0) {
      return false;
    }

    // JsonToken jsonToken =
    // mapper._initForReading(jsonFactory.createJsonParser(content));
    // 模拟 mapper._initForReading 方法
    try {
      JsonParser jp = jsonFactory.createParser(content);
      JsonToken t = jp.getCurrentToken();
      if (t == null) {
        // and then we must get something...
        t = jp.nextToken();
        if (t == null) {
          return false;
        }
      }
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  /**
   * .判断json对象 或json数组.
   * 
   * @param content
   * @return
   */
  public static Boolean isJsonObjectOrJsonArray(String content) {
    // 判断 { : 看是否满足格式
    int temp1 = content.indexOf("{");
    int temp2 = content.indexOf("}");
    int temp3 = content.indexOf(":");

    boolean temp4 = content.endsWith("}");
    boolean temp5 = content.startsWith("{");
    boolean temp6 = content.endsWith("]");
    boolean temp7 = content.startsWith("[");

    boolean isJsonObject = temp4 && temp5;
    boolean isJsonArray = temp6 && temp7;

    if (!isJsonObject && !isJsonArray) {
      return false;
    }
    if ((temp1 < 0 || temp2 < 0 || temp3 < 0)) {
      return false;
    }

    // JsonToken jsonToken =
    // mapper._initForReading(jsonFactory.createJsonParser(content));
    // 模拟 mapper._initForReading 方法
    try {
      JsonParser jp = jsonFactory.createParser(content);
      JsonToken t = jp.getCurrentToken();
      if (t == null) {
        // and then we must get something...
        t = jp.nextToken();
        if (t == null) {
          return false;
        }
      }
    } catch (Exception e) {
      return false;
    }
    return true;
  }



  /**
   * 判断 字符串 是不是json 格式的字符串 不验证{：} 并且支持 {} [] [{}]
   * 
   * @param content
   * @return
   */
  public static Boolean isJsonString10(String content) {
    try {
      JsonParser jp = jsonFactory.createParser(content);
      JsonToken t = jp.getCurrentToken();
      if (t == null) {
        // and then we must get something...
        t = jp.nextToken();
        if (t == null) {
          return false;
        }
      }
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  /**
   * 
   * 
   * @param content
   * @return
   */
  public static Boolean isJsonList(String content) {
    try {
      if (!content.startsWith("[") || !content.endsWith("]")) {
        return false;
      }
      List list = JacksonUtils.jsonToList(content);
      if (list == null) {
        return false;
      }
    } catch (Exception e) {
      return false;
    }
    return true;
  }


  /**
   * List利用json，进行深拷贝.
   * 
   * @param src
   * @return
   */
  @SuppressWarnings("unchecked")
  public static List<Map<String, String>> deepListClone(List<Map<String, String>> src) {
    List<Map<String, String>> dest = null;

    try {
      String jsonStr = mapper.writeValueAsString(src);
      dest = mapper.readValue(jsonStr, List.class);
    } catch (Exception e) {
      // json转换失败
      e.printStackTrace();
    }
    return dest;
  }

  /**
   * List利用json，序列化.
   * 
   * @param src
   * @return
   */
  public static String jsonListSerializer(List<Map<String, String>> src) {
    String jsonStr = null;
    try {
      jsonStr = mapper.writeValueAsString(src);

    } catch (Exception e) {
      // json转换失败
      e.printStackTrace();
    }
    return jsonStr;
  }

  /**
   * List列表转为json字符串.
   * 
   * @param src
   * @return
   */
  @SuppressWarnings("rawtypes")
  public static String listToJsonStr(List src) {
    String jsonStr = null;
    try {
      jsonStr = mapper.writeValueAsString(src);

    } catch (Exception e) {
      e.printStackTrace();
      return "listToJsonStr失败";
      // json转换失败

    }
    return jsonStr;
  }

  /**
   * Map转为json字符串.
   * 
   * @param src
   * @return
   */
  @SuppressWarnings("rawtypes")
  public static String mapToJsonStr(Map src) {
    String jsonStr = null;
    try {
      jsonStr = mapper.writeValueAsString(src);

    } catch (Exception e) {
      e.printStackTrace();
      return "mapToJsonStr失败";
      // json转换失败

    }
    return jsonStr;
  }

  /**
   * List利用json，反序列化.
   * 
   * @param src
   * @return
   */
  @SuppressWarnings("unchecked")
  public static List<Map<String, String>> jsonListUnSerializer(String jsonStr) {

    List<Map<String, String>> dest = null;
    try {

      dest = mapper.readValue(jsonStr, List.class);
    } catch (Exception e) {
      // json转换失败
      e.printStackTrace();
    }
    return dest;
  }

  /**
   * List利用json，反序列化.
   *
   * @param src
   * @return
   */
  @SuppressWarnings("unchecked")
  public static List<Map<String, Object>> jsonListObjUnSerializer(String jsonStr) {

    List<Map<String, Object>> dest = null;
    try {

      dest = mapper.readValue(jsonStr, List.class);
    } catch (Exception e) {
      // json转换失败
      e.printStackTrace();
    }
    return dest;
  }
  /**
   * List利用json，反序列化.
   * 
   * @param src
   * @return
   */
  @SuppressWarnings("rawtypes")
  public static List jsonToList(String jsonStr) {
    List dest = null;
    try {
      dest = mapper.readValue(jsonStr, List.class);
    } catch (Exception e) {
      // json转换失败
      e.printStackTrace();
    }
    return dest;
  }

  /**
   * Map利用json，序列化.
   * 
   * @param src
   * @return
   */
  public static String jsonMapSerializer(Map<String, String> src) {
    String jsonStr = null;
    try {
      jsonStr = mapper.writeValueAsString(src);

    } catch (Exception e) {
      // json转换失败
      e.printStackTrace();
    }
    return jsonStr;
  }

  /**
   * Map利用json，反序列化.
   * 
   * @param jsonStr
   * @return
   */
  @SuppressWarnings("unchecked")
  public static Map<String, String> jsonMapUnSerializer(String jsonStr) {

    Map<String, String> dest = null;
    try {

      dest = mapper.readValue(jsonStr, Map.class);
    } catch (Exception e) {
      // json转换失败
      e.printStackTrace();
    }
    return dest;
  }

  /**
   * Map利用json，反序列化.
   * 
   * @param jsonStr json字符串
   * @return 反序列化后的Map
   */
  @SuppressWarnings("unchecked")
  public static <K, V> Map<K, V> jsonToMap(String jsonStr) {

    Map<K, V> dest = null;
    try {
      dest = (Map<K, V>) mapper.readValue(jsonStr, Map.class);
    } catch (Exception e) {
      // json转换失败
      e.printStackTrace();
    }
    return dest;
  }

  /**
   * Map利用json，反序列化.
   * 
   * @param jsonStr json字符串
   * @return 反序列化后的Map
   * @throws Exception 转换失败抛出此异常
   */
  @SuppressWarnings("unchecked")
  public static <K, V> Map<K, V> jsonToMap2(String jsonStr) throws Exception {

    Map<K, V> dest = null;
    try {
      dest = (Map<K, V>) mapper.readValue(jsonStr, Map.class);
    } catch (Exception e) {
      // json转换失败
      e.printStackTrace();
      throw e;
    }
    return dest;
  }


  /**
   * 将Json字符串转换为对应类型的Map，反序列化操作
   * 
   * @param jsonStr 源json字符串
   * @param <K> 转换后Map的Key类型
   * @param <V> 转换后Map的Value类型
   * @return 转换成功后返回Map对象
   * @throws IOException 转换失败抛出此异常
   */
  @SuppressWarnings("unchecked")
  public static <K, V> Map<K, V> json2Map(String jsonStr) throws IOException {
    Map<K, V> dest = (Map<K, V>) mapper.readValue(jsonStr, Map.class);
    return dest;
  }

  @SuppressWarnings("unchecked")
  public static Map<String, Object> json2HashMap(String jsonStr) {
    HashMap<String, Object> dest = null;
    try {
      dest = (HashMap<String, Object>) mapper.readValue(jsonStr, HashMap.class);
    } catch (Exception e) {
      // json转换失败
      e.printStackTrace();
    }
    return dest;
  }


  /**
   * 将对象转换为json字符串
   * 
   * @param obj
   * @return
   */
  public static String jsonObjectSerializer(Object obj) {
    String jsonStr = null;
    try {
      jsonStr = mapper.writeValueAsString(obj);

    } catch (Exception e) {
      // json转换失败
      e.printStackTrace();
    }
    return jsonStr;
  }

  /**
   * 将对象转换为json字符串(去除null值为"")
   * 
   * @param obj
   * @return
   */
  public static String jsonObjectSerializerNoNull(Object obj) {
    String jsonStr = null;
    try {
      jsonStr = customermapper.writeValueAsString(obj);

    } catch (Exception e) {
      // json转换失败
      e.printStackTrace();
    }
    return jsonStr;
  }

  public static String configJsonObjectSerializer(Object obj, String[] excludeItem, String dateFormate) {
    String jsonStr = null;
    try {
      // 设置排除字段
      if (ArrayUtils.isNotEmpty(excludeItem)) {
        SimpleFilterProvider fileter = new SimpleFilterProvider().addFilter("executeFilter",
            SimpleBeanPropertyFilter.serializeAllExcept(excludeItem));
        mapper.setFilters(fileter);
      }
      // 设置日期格式化
      if (StringUtils.isNotBlank(dateFormate)) {
        mapper.setDateFormat(new SimpleDateFormat(dateFormate));
      }

      jsonStr = mapper.writeValueAsString(obj);

    } catch (Exception e) {
      // json转换失败
      e.printStackTrace();
    }
    return jsonStr;
  }

  /**
   * 将json字符串转换为对象
   * 
   * @param jsonStr
   * @return
   */
  public static Object jsonObject(String jsonStr) {
    Object dest = null;
    try {

      dest = mapper.readValue(jsonStr, Object.class);
    } catch (Exception e) {
      // json转换失败
      e.printStackTrace();
    }
    return dest;
  }

  /**
   * 将json字符串转换为对象
   * 
   * @param jsonStr
   * @return
   */
  public static <T> T jsonObject(String jsonStr, Class<T> class1) {
    try {

      return (T) mapper.readValue(jsonStr, class1);
    } catch (Exception e) {
      // json转换失败
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 将json字符串转换为对象
   * 
   * @param jsonStr
   * @return
   */
  @SuppressWarnings("rawtypes")
  public static Object jsonObject(String jsonStr, TypeReference valueTypeRef) {
    Object dest = null;
    try {

      dest = mapper.readValue(jsonStr, valueTypeRef);
    } catch (Exception e) {
      // json转换失败
      e.printStackTrace();
    }
    return dest;
  }

  public static Object configJsonObject(Object obj, String[] excludeItem, String dateFormate) {
    Object dest = null;
    try {
      String jsonStr = configJsonObjectSerializer(obj, excludeItem, dateFormate);
      if (jsonStr != null) {
        dest = jsonObject(jsonStr);
      }
    } catch (Exception e) {
      // json转换失败
      e.printStackTrace();
    }
    return dest;
  }

  @SuppressWarnings("rawtypes")
  public static List jsonToCollection(String jsonString, Class<?> collectionClass, Class<?>... elementClasses) {
    List list = null;
    try {
      JavaType javaType =
          mapper.getTypeFactory().constructParametrizedType(collectionClass, collectionClass, elementClasses);
      list = mapper.readValue(jsonString, javaType);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return list;

  }

}
