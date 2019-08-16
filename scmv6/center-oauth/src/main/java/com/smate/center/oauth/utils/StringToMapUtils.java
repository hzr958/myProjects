package com.smate.center.oauth.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * @description 将xxx=xxx&yyy=yyy等形式字符串转换成map
 * @author xiexing
 * @date 2019年2月18日
 */
public class StringToMapUtils {
  public static Map<String, String> toMap(String target) {
    if (StringUtils.isEmpty(target)) {
      return null;
    }
    Map<String, String> map = new HashMap<String, String>();
    String[] strings = target.split("&");
    for (String param : strings) {
      if (StringUtils.isNotEmpty(param) && param.indexOf("=") > 0) {
        map.put(param.substring(0, param.indexOf("=")), param.substring(param.indexOf("=") + 1, param.length()));
      }
    }
    return map;
  }

  /**
   * 判断map是否为空
   * 
   * @param map
   * @return
   */
  public static boolean isNotEmpty(Map map) {
    if (map != null && map.size() > 0) {
      return true;
    }
    return false;
  }

  public static void main(String[] args) {
    String target = "accessToken=60504FB2D251AE7AA2751CDE6283D377&expireIn=7776000";
    Map<String, String> map = toMap(target);
    Set<Entry<String, String>> entrySet = map.entrySet();
    for (Entry<String, String> entry : entrySet) {
      System.out.println("key:" + entry.getKey() + " value:" + entry.getValue());
    }
  }
}
