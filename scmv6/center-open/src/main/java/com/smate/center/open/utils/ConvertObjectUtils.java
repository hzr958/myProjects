package com.smate.center.open.utils;

public class ConvertObjectUtils {

  /**
   * 处理对象生成字符串
   *
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param obj
   * @return
   */
  public static String objectToString(Object obj) {
    if (obj != null) {
      return obj.toString();
    }
    return null;
  }

  /**
   * 处理对象生成数字
   *
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param obj
   * @return
   */
  public static Integer objectToInt(Object obj) {
    if (obj != null) {
      return Integer.parseInt(obj.toString());
    }
    return null;
  }


}
