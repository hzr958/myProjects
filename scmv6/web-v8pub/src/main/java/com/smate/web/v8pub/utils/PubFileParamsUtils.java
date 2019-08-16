package com.smate.web.v8pub.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;


/**
 * 文件导入的成果参数处理工具
 * 
 * @author YJ
 *
 *         2018年11月22日
 */
public class PubFileParamsUtils {

  /**
   * 构造成果日期
   * 
   * @param YR
   * @param FD
   * @return
   */
  public static String buildPublishDate(String YR, String FD) {
    if (StringUtils.isBlank(YR)) {
      return StringUtils.isNotEmpty(FD) ? FD : "";
    }
    if (StringUtils.isBlank(FD)) {
      return StringUtils.isNotEmpty(YR) ? YR : "";
    }
    return YR + FD;
  }

  /**
   * 替换多个空格变成一个空格
   * 
   * @param data
   * @return
   */
  public static String replaceMultipleSpace(String data) {
    if (StringUtils.isEmpty(data)) {
      return "";
    }
    // 把一个或多个空格替换成一个空格
    data = data.replaceAll("\\s+", " ");
    return data;
  }

  public static List<String> parsePubTypes(String DT) {
    String[] typeList = null;
    if (StringUtils.isBlank(DT)) {
      return new ArrayList<>();
    }
    typeList = DT.split("[;；]");
    List<String> list = new ArrayList<String>();
    for (String pubType : typeList) {
      pubType = pubType.trim();
      if (StringUtils.isNotBlank(pubType)) {
        list.add(pubType);
      }
    }
    return list;
  }

}
