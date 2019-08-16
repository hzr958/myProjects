package com.smate.test.utils;

import org.apache.commons.lang.StringUtils;

public class JsonStringUtils {
  public static String getTargetValue(String source, String target) {
    if (StringUtils.isBlank(source) || StringUtils.isBlank(target)) {
      return "";
    }
    String[] first = source.split(",");
    for (String param : first) {
      if (StringUtils.isNotBlank(param)) {
        if (param.indexOf(target) > 0) {
          String[] second = param.split("=");
          if (checkArray(second)) {
            return second[1] == null ? "" : second[1];
          }
        }
      }
    }
    return "";
  }

  public static boolean checkArray(Object[] target) {
    if (target != null && target.length > 0) {
      return true;
    }
    return false;
  }
}
