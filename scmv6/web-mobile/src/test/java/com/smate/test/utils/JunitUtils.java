package com.smate.test.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.smate.core.base.utils.string.StringUtils;

/**
 * 测试用例使用到的工具类
 * 
 * @author Administrator
 *
 */
public class JunitUtils {

  // 构建空参校验参数
  public static List<String[]> buildCheckPar(String[] strings) {
    if (strings == null || strings.length == 0) {
      return null;
    }
    List<String[]> groups = new ArrayList<>();
    int i = (int) Math.pow(2, strings.length);
    for (int j = 0; j < i - 1; j++) {
      String binaryString = Integer.toBinaryString(j);
      binaryString = String.format("%" + strings.length + "d", Integer.parseInt(binaryString));
      char[] charArray = binaryString.toCharArray();
      String[] arr = new String[strings.length];
      for (int k = 0; k < arr.length; k++) {
        if (charArray[k] == '1') {
          arr[k] = String.valueOf(strings[k]);
        } else {
          arr[k] = "";
        }
      }
      groups.add(arr);
    }
    return groups;
  }

  // {key1=value1,key2=value2，key3=value3...}形式字符串转Map
  public static Map<String, String> strToMap(String arrayStr) {
    if (arrayStr.contains(",")) {
      HashMap<String, String> map = new HashMap<>();
      String[] array = StringUtils.strip(arrayStr, "{}").split(",");
      for (String str : array) {
        int indexOf = str.indexOf("=");
        if (indexOf != -1) {
          map.put(str.substring(0, indexOf).trim(), str.substring(indexOf + 1).trim());
        }
      }
      return map;
    }
    return null;
  }

  public static void main(String[] args) {
    String[] strings = {"JvUzHyT7%252BGLSWGMm5IJ5Pw%253D%253D"};
    List<String[]> buildCheckPar = buildCheckPar(strings);
    System.out.println("size:" + buildCheckPar.size());
    for (String[] param : buildCheckPar) {
      System.out.println("result:" + Arrays.toString(param));
    }
  }
}
