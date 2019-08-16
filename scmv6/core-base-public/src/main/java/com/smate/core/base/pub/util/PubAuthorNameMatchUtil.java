package com.smate.core.base.pub.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.string.StringUtils;

/**
 * 成果验证工具.
 * 
 * @author tsz
 *
 */
public class PubAuthorNameMatchUtil {

  public static Integer title_max_len = 2000;
  public static Integer keycode_max_len = 20;
  public static Integer authors_max_len = 1000;
  public static Integer email_max_len = 50;
  public static Integer tel_max_len = 20;
  public static Integer name_max_len = 60;
  public static Integer paper_type = 1;
  public static Integer home_pub_type = 2;

  /**
   * 只进行名字的全称匹配
   * 
   * @param name
   * @param compareName
   * @return
   */
  public static boolean compareByFullName(String name, String compareName) {
    // 清除数据
    name = AuthorNamesUtils.cleanAuthorName(name);
    compareName = AuthorNamesUtils.cleanAuthorName(compareName);
    // 特殊字符数据过滤
    name = XmlUtil.getCleanAuthorName5(name);
    compareName = XmlUtil.getCleanAuthorName5(compareName);
    if (StringUtils.isBlank(name) || StringUtils.isBlank(compareName)) {
      return false;
    }
    Boolean isNameCh = ServiceUtil.isChineseStr(name);
    Boolean isCompareNameCh = ServiceUtil.isChineseStr(compareName);
    if (isNameCh && isCompareNameCh) {
      // 两者都是中文情况下，去除所有的空格
      name = name.replaceAll("[\\s]", "");
      compareName = compareName.replaceAll("[\\s]", "");
      return name.equals(compareName);
    } else {
      name = name.replaceAll("[\\s+]", " ");
      compareName = compareName.replaceAll("[\\s+]", " ");
      // 都不是中文情况下，忽略大小写进行比较
      return name.equalsIgnoreCase(compareName);
    }
  }



  public static boolean compareNames(String name, String compareName) {
    // 清除数据
    name = AuthorNamesUtils.cleanAuthorName(name);
    compareName = AuthorNamesUtils.cleanAuthorName(compareName);
    // 特殊字符数据过滤
    name = XmlUtil.getCleanAuthorName5(name);
    compareName = XmlUtil.getCleanAuthorName5(compareName);
    if (StringUtils.isBlank(name) || StringUtils.isBlank(compareName)) {
      return false;
    }
    Boolean isNameCh = ServiceUtil.isChineseStr(name);
    Boolean isCompareNameCh = ServiceUtil.isChineseStr(compareName);
    if (isNameCh && isCompareNameCh) {
      // 全是中文,
      name = name.replaceAll("[\\s]", "");
      compareName = compareName.replaceAll("[\\s]", "");
      return name.equals(compareName);
    } else {
      // 不全是中文
      return compareNameNotAllCh(name, compareName);
    }
  }

  /**
   * 不全是中文的对比
   * 
   * @param name
   * @param compareName
   * @return
   */
  protected static boolean compareNameNotAllCh(String name, String compareName) {
    if (name.equalsIgnoreCase(compareName)) {
      return true;
    }
    // 增加逻辑，进行两者包含关系的判断
    List<String> names = PubAuthorNameMatchUtil.spitName(name);
    List<String> compareNames = PubAuthorNameMatchUtil.spitName(compareName);
    if (!names.containsAll(compareNames) && !compareNames.containsAll(names)) {
      return false;
    }

    Set<String> nameList = PubAuthorNameMatchUtil.buildName(name);
    Set<String> compareNameList = PubAuthorNameMatchUtil.buildName(compareName);

    if (CollectionUtils.containsAny(nameList, compareNameList)) {
      return true;
    }
    return false;
  }

  protected static List<String> spitName(String name) {
    List<String> nameList = new ArrayList<>();
    if (StringUtils.isBlank(name)) {
      return nameList;
    }
    name = name.replaceAll("[\\s]", "");
    char[] names = name.toCharArray();
    for (char c : names) {
      nameList.add(String.valueOf(c));
    }
    return nameList;
  }



  /**
   * 抽取名字.
   * 
   * @param name
   * @return
   */
  public static Set<String> buildName(String name) {
    Set<String> nameList = new HashSet<>();
    nameList.add(name);
    Map<String, Set<String>> souMap = ServiceUtil.generatePsnName(name.replaceAll("\\d+", ""));
    if (souMap != null) {
      nameList.addAll(souMap.get("fullname"));
      souMap.get("fullname").forEach(n -> {
        nameList.addAll(getEameGroup(n));
      });
      nameList.addAll(souMap.get("initname"));
      nameList.addAll(souMap.get("prefixname"));
    } else {
      // 做英文拆分
      nameList.addAll(getEameGroup(name));
    }
    return nameList;
  }

  public static Set<String> getEameGroup(String ename) {
    Set<String> enameList = new HashSet<>();
    int firstBlank = ename.indexOf(" ");
    if (firstBlank > 0) {
      int lastBlank = ename.lastIndexOf(" ");
      int length = ename.length();
      // fan zhi qiang
      enameList.add(ename.substring(0, firstBlank) + " " + ename.substring(firstBlank + 1).substring(0, 1));// fan z
      enameList.add(ename.substring(lastBlank + 1, length) + " " + ename.substring(0).substring(0, 1)); // qiang f
      enameList.add(ename.substring(0, firstBlank) + ename.substring(firstBlank + 1).replace(" ", "")); // fanzhiqiang
      enameList.add(ename.substring(firstBlank + 1).replace(" ", "") + ename.substring(0, firstBlank));// zhiqiangfan
      /*
       * if (lastBlank > firstBlank) { enameList.add(ename.substring(firstBlank + 1, lastBlank) + " " +
       * ename.substring(0).substring(0, 1)); String middleName = ename.substring(firstBlank + 1,
       * lastBlank); int secondBlank = middleName.indexOf(" "); if (secondBlank > 0) {
       * enameList.add(ename.substring(0, firstBlank) + " " + middleName.substring(secondBlank +
       * 1).substring(0, 1)); } }
       */
      // enameList.add(ename.substring(0, firstBlank) + " " + ename.substring(lastBlank + 1).substring(0,
      // 1));
    }
    return enameList;
  }
}
