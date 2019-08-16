package com.smate.core.base.utils.common;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * 一些比较常用的方法
 *
 * @author wsn
 * @createTime 2017年7月25日 下午6:41:42
 *
 */
public class CommonUtils {

  /**
   * 比较两个Long值
   * 
   * @param one
   * @param two
   * @return
   */
  public static boolean compareLongValue(Long one, Long two) {
    boolean equ = false;
    if (one == null && two == null) {
      equ = true;
    } else if (one == null || two == null) {
      equ = false;
    } else if (one.longValue() == two.longValue()) {
      equ = true;
    }
    return equ;
  }

  /**
   * 删除url中的域名部分 如：http://www.scholarmate.com/pubweb/xxx/xxx 变为：/pubweb/xxx/xxx
   * 
   * @param url
   * @return
   */
  public static String deleteHostFromUrl(String url) {
    String newUrl = StringUtils.isNotBlank(url) ? url : "";
    String pattern = "^((http://)|(https://))?([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}";
    Pattern p = Pattern.compile(pattern);
    // 替换
    return newUrl.replaceAll(pattern, "");
  }

  /**
   * 比较两个Integer值
   * 
   * @param one
   * @param two
   * @return
   */
  public static boolean compareIntegerValue(Integer one, Integer two) {
    boolean equ = false;
    if (one == null && two == null) {
      equ = true;
    } else if (one == null || two == null) {
      equ = false;
    } else if (one.intValue() == two.intValue()) {
      equ = true;
    }
    return equ;
  }
}
