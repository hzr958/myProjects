package com.smate.center.task.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class TmpTaskUtils {

  /**
   * 截取日志
   * 
   * @param e
   * @return
   */
  public static String getErrorMsg(Exception e) {
    Writer result = new StringWriter();
    PrintWriter printWriter = new PrintWriter(result);
    e.printStackTrace(printWriter);
    String ErrorMsg = result.toString().length() > 600 ? result.toString().substring(600) : result.toString();
    return ErrorMsg;
  }

  /**
   * 是否全中文
   * 
   * @param str
   * @return
   */

  public static boolean isFullChinese(String str) {
    if (str == null) {
      return false;
    }
    Pattern pattern = Pattern.compile("[\\u4E00-\\u9FBF]+");
    return pattern.matcher(str.trim().replace(" ", "")).matches();
  }

  /**
   * 是否全英文
   * 
   * @param str
   * @return
   */

  public static boolean isFullEnglish(String str) {
    if (str == null) {
      return false;
    }
    Pattern pattern = Pattern.compile("[a-zA-Z]+");
    return pattern.matcher(str.trim().replace(" ", "")).matches();
  }

  /**
   * 判断字符串是否有中文
   * 
   * @param name
   * @return
   */
  public static boolean containZhChar(String name) {
    if (StringUtils.isNotBlank(name)) {
      Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
      Matcher matcher = p.matcher(name);
      if (matcher.find()) {
        return true;
      }
    }
    return false;
  }

  /**
   * 判断字符串是否有英文
   * 
   * @param name
   * @return
   */
  public static boolean containEnChar(String name) {
    if (StringUtils.isNotBlank(name)) {
      Pattern p = Pattern.compile("[A-Za-z]");
      Matcher matcher = p.matcher(name);
      if (matcher.find()) {
        return true;
      }
    }
    return false;
  }
}
