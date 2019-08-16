package com.smate.center.oauth.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author lichangwen
 * 
 */
public class EditValidateUtils {

  public static final String URL_COAD = "^[a-zA-z]+://(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*(\\?\\S*)?/{0,1}$";

  public static final String MAIL_COAD = "(?i)^[a-z0-9]+[a-z0-9_\\-.]*@([a-z0-9][0-9a-z\\-]*\\.)+[a-z]{2,10}$";

  public static final String PHONE_COAD = "^(\\d{3}\\+)?(\\d{3,4}-?)?\\d{7,9}$";

  public static final String MOBILE_COAD = "^(\\+\\d{2,3}\\-)?\\d{11}$";

  /**
   * 用于验证页面必填参数.
   */
  public static boolean hasParam(Object param, int maxLength, String regex) {
    if (param == null) {
      return true;
    }
    String str = param.toString();
    if ("".equals(str.trim()) || maxLength <= 0) {
      return true;
    }
    if (str.length() <= 0 || str.length() > maxLength) {
      return true;
    }
    if (regex != null) {
      Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
      if (!pattern.matcher(str).matches()) {
        return true;
      }
    }
    return false;
  }

  /**
   * 用于验证页面可以为空的参数.
   */
  public static boolean isNull(Object param, int maxLength, String regex) {
    if (param == null || "".equals(param.toString().trim())) {
      return false;
    }
    String str = param.toString();
    if (str.length() > maxLength) {
      return true;
    }
    if (regex != null) {
      Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
      if (!pattern.matcher(str).matches()) {
        return true;
      }
    }
    return false;
  }

  /**
   * 去除数组中重复数据.
   */
  public static String[] array_unique(String[] strs) {
    List<String> list = new LinkedList<String>();
    for (int i = 0; i < strs.length; i++) {
      if (!list.contains(strs[i])) {
        list.add(strs[i]);
      } else {
        list.remove(strs[i]);
      }
    }
    if (list.size() == 0)
      return null;
    return (String[]) list.toArray(new String[list.size()]);
  }

  /**
   * 去除字符串中重复数据.
   */
  public static String strDelTostr(String str) {
    if (str == null || "".equals(str.trim()))
      return null;
    StringBuffer newstr = new StringBuffer();
    String[] delstr = array_unique(str.split(","));
    if (delstr == null)
      return null;
    for (int i = 0; i < delstr.length; i++) {
      newstr.append(delstr[i]).append(",");
    }
    return strPas(newstr.toString());

  }

  /**
   * 去除两个字符串头尾的逗号.
   */
  public static String strPas(String str) {
    StringBuffer a = new StringBuffer(str);
    if (a.charAt(0) == ',') {
      a.deleteCharAt(0);
    }
    if (a.charAt(a.length() - 1) == ',') {
      a.deleteCharAt(a.length() - 1);
    }
    return a.toString();
  }
}
