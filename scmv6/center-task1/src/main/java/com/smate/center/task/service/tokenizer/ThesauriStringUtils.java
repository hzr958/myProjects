package com.smate.center.task.service.tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 词库字符串工具类
 * 
 * @author zk
 *
 */
public class ThesauriStringUtils {

  // 需要排除的词
  public static String[] regionExcludeArray = new String[] {"市", "盟", "群岛", "岛", "领土", "共和国", "自治县", "县", "群岛的岛礁及其海域",
      "自治州", "州", "特区", "新区", "自治区", "地区", "区", "斯坦"};

  public static String toString(List<String> strList) {
    StringBuffer sb = new StringBuffer();
    if (CollectionUtils.isNotEmpty(strList)) {
      for (String str : strList) {
        if (StringUtils.isNotBlank(str)) {
          sb.append(cleanSpecialChar(str));
        }
      }
    }
    if (sb.toString().length() == 0) {
      return sb.toString();
    }
    String resultStr = sb.toString();
    resultStr = resultStr.substring(0, resultStr.lastIndexOf("\r\n"));
    return resultStr;
  }

  private static String cleanSpecialChar(String strObj) {
    StringBuffer sb = new StringBuffer();
    Pattern p = Pattern.compile("[^\u4e00-\u9fa5]");
    Matcher m = p.matcher(strObj);
    String matcherStr = m.replaceAll(" ");
    if (StringUtils.isNotBlank(matcherStr)) {
      String[] mArray = matcherStr.split(" ");
      for (String mStr : mArray) {
        if (StringUtils.isNotBlank(mStr) && mStr.length() > 1) {
          sb.append(mStr);
          sb.append("\r\n");
        }
      }
    }

    return sb.toString().toString();
  }


  /**
   * 排除地区指定字符
   * 
   * @param strList
   * @return
   */
  public static List<String> excludeRegionStr(List<String> strList) {
    List<String> resultList = new ArrayList<String>();
    if (CollectionUtils.isNotEmpty(strList)) {
      for (String str : strList) {
        if (StringUtils.isBlank(str)) {
          continue;
        }
        for (String excludeStr : regionExcludeArray) {
          int lastIndex = str.lastIndexOf(excludeStr);
          if (lastIndex > 0) {
            str = str.substring(0, lastIndex);
            break;
          }
        }
        resultList.add(str);
      }
    }
    return resultList;
  }

  public static void main(String args[]) {
    StringBuffer sb = new StringBuffer();
    Pattern p = Pattern.compile("[^\u4e00-\u9fa5]");
    Matcher m = p.matcher("#$在人df2人4");
    String matcherStr = m.replaceAll(" ");
    if (StringUtils.isNotBlank(matcherStr)) {
      String[] mArray = matcherStr.split(" ");
      for (String mStr : mArray) {
        if (StringUtils.isNotBlank(mStr) && mStr.length() > 1) {
          sb.append(mStr);
          sb.append("\r\n");
        }
      }
    }
    System.out.println(sb.toString());
  }
}
