package com.smate.center.task.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.xss.XssUtils;

public class XSSUtils {

  public static String xssReplace(String value) {
    String reslut = "";
    if (JacksonUtils.isJsonObjectOrJsonArray(value)) {
      reslut = transferJson(value);
    } else {
      // 对参数值进行过滤.
      reslut = XssUtils.filterByXssStr(value);
    }
    return reslut;
  }


  @SuppressWarnings({"rawtypes", "unchecked"})
  private static void transferJson(Map map) {
    if (map != null) {
      Iterator<Map.Entry> it = map.entrySet().iterator();
      while (it.hasNext()) {
        Map.Entry entry = it.next();
        Object object = entry.getValue();
        if (object instanceof java.util.List) {
          transferJson((List) object);
        } else if (object != null && object.toString().startsWith("{") && (object instanceof java.util.Map)) {
          Map<String, Object> map1 = (Map<String, Object>) object;
          transferJson(map1);
          map.put(entry.getKey(), JacksonUtils.jsonObject(JacksonUtils.jsonObjectSerializer(map1)));
        } else if (object != null) {
          map.put(entry.getKey(), xssReplace(object.toString()));
        }
      }
    }
  }

  @SuppressWarnings({"rawtypes"})
  private static void transferJson(List list) {
    for (Object object : list) {
      if (object instanceof java.util.Map) {
        transferJson((Map) object);
      } else {
        XssUtils.filterByXssStr(object.toString());
      }
    }
  }

  @SuppressWarnings("rawtypes")
  private static String transferJson(String string) {
    if (StringUtils.isEmpty(string)) {
      return string;
    }
    if (string.startsWith("[") && string.endsWith("]") && JacksonUtils.isJsonList(string)) {
      List list = JacksonUtils.jsonToList(string);
      transferJson(list);
      return JacksonUtils.listToJsonStr(list);
    } else {
      Map<String, Object> map = JacksonUtils.json2HashMap(string);
      transferJson(map);
      return JacksonUtils.jsonObjectSerializer(map);
    }
  }


}
