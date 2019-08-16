package com.smate.core.base.utils.common;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

/**
 * 国际化问题的实用工具类. <br/>
 * 默认语言为中文.
 * 
 * @author LY
 * 
 */
public class LocaleTextUtils {
  private static final String DEFAULT_LANGUAGE = "zh";

  /**
   * @param locale
   * @param zhText
   * @param enText
   * @return
   */
  public static String getLocaleText(Locale locale, String zhText, String enText) {
    String language = "";
    if (locale == null) {
      language = DEFAULT_LANGUAGE;
    } else {
      language = locale.getLanguage();
    }
    if ("en".equalsIgnoreCase(language) && StringUtils.isNotBlank(enText)) {
      return enText;
    } else if ("zh".equalsIgnoreCase(language) && StringUtils.isNotBlank(zhText)) {
      return zhText;
    } else {
      if (StringUtils.isNotBlank(zhText)) {
        return zhText;
      }
      if (StringUtils.isNotBlank(enText)) {
        return enText;
      }
    }
    return "";
  }

  /**
   * 根据语言环境获取对应的信息
   * 
   * @param locale 语言环境
   * @param zhStr 中文字符
   * @param enStr 英文字符
   * @return
   */
  public static String getStrByLocale(String locale, String zhStr, String enStr) {
    if ("en_US".equals(locale)) {
      return StringUtils.isNotBlank(enStr) ? enStr : zhStr;
    } else {
      return StringUtils.isNotBlank(zhStr) ? zhStr : enStr;
    }
  }
}
