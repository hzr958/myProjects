package com.smate.web.psn.utils;

import org.springframework.context.i18n.LocaleContextHolder;

/**
 * 国际化工具类
 * 
 * @author WSN
 *
 */
public class LocaleStringUtils {

  // 根据语言环境返回对应的字符串
  public static String getStringByLocale(String enString, String zhString) {
    if ("en_US".equals(LocaleContextHolder.getLocale().toString())) {
      return enString;
    } else {
      return zhString;
    }
  }
}
