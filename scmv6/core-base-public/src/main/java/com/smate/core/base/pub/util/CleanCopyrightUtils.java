package com.smate.core.base.pub.util;

import org.apache.commons.lang.StringUtils;

public class CleanCopyrightUtils {

  /**
   * 版权信息所含有的字段
   */
  private static String[] copyrightList =
      new String[] {"All rights reserved", "Copyright", "copyright", "(C)", "(c)", "©", "&copy;", "&Copy;"};

  /**
   * 清除摘要信息的版权信息 <br/>
   * 数据模板： <br/>
   * "(C) All rights reserved © 2003 American Chemical Society."; <br/>
   * 
   * @param summary 摘要信息
   * @return
   */
  public static String cleanSummary(String summary) {
    if (StringUtils.isNotBlank(summary)) {
      int len = summary.length();
      String firstHalf = "";
      String lastHalf = "";
      if (len > 200) {
        // 摘要拆分成两部分，以后200个字符为划分点
        firstHalf = summary.substring(0, len - 200);
        lastHalf = summary.substring(len - 200);
      } else {
        // 不够200个字符
        lastHalf = summary;
      }
      // lastHalf是否包含数组中的任意版权字符
      int copyPos = StringUtils.indexOfAny(lastHalf, copyrightList);
      if (copyPos > -1) {
        // 截取包含的版权字符
        lastHalf = lastHalf.substring(0, copyPos);
        // 以最后出现的英文句号为划分点
        int dotPos = StringUtils.lastIndexOf(lastHalf, ".");
        if (dotPos > -1) {
          // 包含句号
          lastHalf = lastHalf.substring(0, dotPos + 1);
          // 拼接去除版权信息的摘要内容，并返回
          return firstHalf + lastHalf;
        } else {
          return firstHalf;
        }
      }
    }
    return summary;
  }
}
