package com.smate.center.batch.util.pub;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * isi用户别名处理.
 * 
 * @author liqinghua
 * 
 */
public class PsnPmIsiNameUtils {

  /**
   * 拆分拼接在一起的first name.
   * 
   * @param firstName
   * @param zhName
   * @return
   */
  public static String splitJoinFirstName(String firstName, String zhName) {

    // 中文为空，文长度本来就不需要拆分，不含有中文字符，直接退出
    if (StringUtils.isBlank(zhName) || zhName.trim().length() <= 2 || !ServiceUtil.isChineseStr(zhName)) {
      return firstName;
    }
    // first name本身就是拆分的，不需要重新拆分
    String[] strNames = firstName.split("[\\s\\-\\._]{1,}");
    if (strNames.length > 1) {
      return firstName;
    }
    Map<String, String> pymap = ServiceUtil.parsePinYin(zhName);
    String pyfname = pymap.get("firstName");
    if (StringUtils.isBlank(pyfname)) {
      return firstName;
    }
    // 去除空格，内容一致，则返回拆分的firstname
    if (pyfname.replace(" ", "").equalsIgnoreCase(firstName)) {
      return pyfname;
    }
    return firstName;
  }

  /**
   * 获取isi用户名匹配前缀.
   * 
   * @param firstName
   * @param lastName
   * @param otherName
   * @return
   */
  public static Set<String> buildPrefixName(String firstName, String lastName, String otherName) {
    firstName = formatName(firstName);
    lastName = formatName(lastName);
    otherName = formatName(otherName);
    if (StringUtils.isBlank(firstName) || StringUtils.isBlank(lastName)) {
      return null;
    }
    List<String> lnList = buildLastNames(lastName);
    Set<String> nameSet = new HashSet<String>();
    for (String ln : lnList) {
      nameSet.addAll(doBuildPrefixName(firstName, ln, otherName));
    }
    return nameSet;
  }

  /**
   * 获取isi用户名匹配前缀.
   * 
   * @param firstName
   * @param lastName
   * @param otherName
   * @return
   */
  private static Set<String> doBuildPrefixName(String firstName, String lastName, String otherName) {
    firstName = formatName(firstName);
    lastName = formatName(lastName);
    otherName = formatName(otherName);
    if (firstName == null || lastName == null) {
      return null;
    }
    Set<String> nameSet = new HashSet<String>();
    // --简称前缀lin z，last_name + (fist_name首字母)
    nameSet.add(lastName + " " + firstName.substring(0, 1));
    // --简称前缀lin a，last_name + (other_name首字母)
    if (otherName != null) {
      nameSet.add(lastName + " " + otherName.substring(0, 1));
    }
    return nameSet;
  }

  /**
   * 构造isi用户简名组合.
   * 
   * @param firstName
   * @param lastName
   * @param otherName
   * @return
   */
  public static Set<String> buildInitName(String firstName, String lastName, String otherName) {
    firstName = formatName(firstName);
    lastName = formatName(lastName);
    otherName = formatName(otherName);
    if (StringUtils.isBlank(firstName) || StringUtils.isBlank(lastName)) {
      return null;
    }
    List<String> lnList = buildLastNames(lastName);
    Set<String> nameSet = new HashSet<String>();
    for (String ln : lnList) {
      nameSet.addAll(doBuildInitName(firstName, ln, otherName));
    }
    return nameSet;
  }

  /**
   * last name可能会存在各种形式，例如ou-yang,ou yang.
   * 
   * @param lastName
   * @return
   */
  private static List<String> buildLastNames(String lastName) {

    List<String> lnList = new ArrayList<String>();
    lnList.add(lastName);
    if (lastName.indexOf(" ") > 0) {
      StringBuilder sbLn = new StringBuilder(lastName);
      int i = 0;
      while (true) {
        i = sbLn.indexOf(" ", i);
        if (i < 0) {
          break;
        }
        sbLn.replace(i, i + 1, "-");
        lnList.add(sbLn.toString());
        // 正好sbLn的长度-1了，所以i没必要++了
        sbLn.replace(i, i + 1, "");
        lnList.add(sbLn.toString());
      }
    }
    if (lastName.indexOf("-") > 0) {
      StringBuilder sbLn = new StringBuilder(lastName);
      int i = 0;
      while (true) {
        i = sbLn.indexOf("-", i);
        if (i < 0) {
          break;
        }
        sbLn.replace(i, i + 1, " ");
        lnList.add(sbLn.toString());
        // 正好sbLn的长度-1了，所以i没必要++了
        sbLn.replace(i, i + 1, "");
        lnList.add(sbLn.toString());
      }
    }
    return lnList;
  }

  /**
   * 构造isi用户简名组合.
   * 
   * @param firstName
   * @param lastName
   * @param otherName
   * @return
   */
  private static Set<String> doBuildInitName(String firstName, String lastName, String otherName) {

    firstName = formatName(firstName);
    lastName = formatName(lastName);
    otherName = formatName(otherName);
    if (firstName == null || lastName == null) {
      return null;
    }
    Set<String> nameSet = new HashSet<String>();

    // --1、lin zj
    nameSet.add(lastName + " " + buildInitJoin(firstName, ""));
    // --2、lin z j
    nameSet.add(lastName + " " + buildInitJoin(firstName, " "));
    if (otherName != null) {
      // --3、lin zja
      nameSet.add(lastName + " " + buildInitJoin(firstName + " " + otherName, ""));
      // --4、lin zj a
      nameSet.add(lastName + " " + buildInitJoin(firstName, "") + " " + otherName.substring(0, 1));
      // --5、lin z j a
      nameSet.add(lastName + " " + buildInitJoin(firstName + " " + otherName, " "));
      // --6、lin azj
      nameSet.add(lastName + " " + buildInitJoin(otherName + " " + firstName, ""));
      // --7、lin a zj
      nameSet.add(lastName + " " + otherName.substring(0, 1) + " " + buildInitJoin(firstName, ""));
      // --8、lin a z j
      nameSet.add(lastName + " " + buildInitJoin(otherName + " " + firstName, " "));
      // --9、lin z a
      nameSet.add(lastName + " " + firstName.substring(0, 1) + " " + otherName.substring(0, 1));
      // --10、lin za
      nameSet.add(lastName + " " + firstName.substring(0, 1) + otherName.substring(0, 1));
    }
    return nameSet;
  }

  /**
   * 构造isi用户简名组合拼接方式.
   * 
   * @param firstName
   * @param joined
   * @return
   */
  private static String buildInitJoin(String name, String joined) {

    String[] strNames = name.split("[\\s\\-\\._]{1,}");
    StringBuilder sb = new StringBuilder();
    for (String strName : strNames) {
      if (StringUtils.isBlank(strName)) {
        continue;
      }
      if (sb.length() > 0) {
        sb.append(joined);
      }
      sb.append(strName.substring(0, 1).trim());
    }
    return sb.length() > 0 ? sb.toString() : null;
  }

  /**
   * 构造isi用户全称组合.
   * 
   * @param firstName
   * @param lastName
   * @param otherName
   * @return
   */
  public static Set<String> buildFullName(String firstName, String lastName, String otherName) {

    firstName = formatName(firstName);
    lastName = formatName(lastName);
    otherName = formatName(otherName);
    if (StringUtils.isBlank(firstName) || StringUtils.isBlank(lastName)) {
      return null;
    }
    List<String> lnList = buildLastNames(lastName);
    Set<String> nameSet = new HashSet<String>();
    for (String ln : lnList) {
      nameSet.addAll(doBuildFullName(firstName, ln, otherName));
    }
    return nameSet;
  }

  /**
   * 构造isi用户全称组合.
   * 
   * @param firstName
   * @param lastName
   * @param otherName
   * @return
   */
  private static Set<String> doBuildFullName(String firstName, String lastName, String otherName) {

    firstName = formatName(firstName);
    lastName = formatName(lastName);
    otherName = formatName(otherName);
    if (firstName == null || lastName == null) {
      return null;
    }
    Set<String> nameSet = new HashSet<String>();
    // --1、lin zhenjiang
    nameSet.add(lastName + " " + buildFullJoin(firstName, ""));
    // --2、lin zhen jiang
    nameSet.add(lastName + " " + buildFullJoin(firstName, " "));
    if (otherName != null) {
      // --3、lin zhen jiang allen
      nameSet.add(lastName + " " + buildFullJoin(firstName + " " + otherName, " "));
      // --4、lin zhenjiang allen
      nameSet.add(lastName + " " + buildFullJoin(firstName, "") + " " + otherName);
      // --5、lin zhen jiang a
      nameSet.add(lastName + " " + buildFullJoin(firstName, " ") + " " + otherName.substring(0, 1));
      // --6、lin zhenjian a
      nameSet.add(lastName + " " + buildFullJoin(firstName, "") + " " + otherName.substring(0, 1));
      // --7、lin z j allen
      nameSet.add(lastName + " " + buildInitJoin(firstName, " ") + " " + otherName);
      // --8、lin zj allen
      nameSet.add(lastName + " " + buildInitJoin(firstName, "") + " " + otherName);
      // --9、lin z allen
      nameSet.add(lastName + " " + firstName.substring(0, 1) + " " + otherName);
      // --10、lin allen
      nameSet.add(lastName + " " + otherName);
      // --11、lin allen zhen jiang
      nameSet.add(lastName + " " + buildFullJoin(otherName + " " + firstName, " "));
      // --12、lin a zhen jiang
      nameSet.add(lastName + " " + otherName.substring(0, 1) + " " + buildFullJoin(firstName, " "));
      // --13、lin a zhenjiang
      nameSet.add(lastName + " " + otherName.substring(0, 1) + " " + buildFullJoin(firstName, ""));
      // --14、lin allen z j
      nameSet.add(lastName + " " + otherName + " " + buildInitJoin(firstName, " "));
      // --15、lin allen zj
      nameSet.add(lastName + " " + otherName + " " + buildInitJoin(firstName, ""));
      // --16、lin allen z
      nameSet.add(lastName + " " + otherName + " " + firstName.substring(0, 1));
    }
    return nameSet;
  }

  /**
   * 构建所有名称，包括简称，全称.
   * 
   * @param firstName
   * @param lastName
   * @param otherName
   * @return
   */
  public static Set<String> buildAllName(String firstName, String lastName, String otherName) {
    if (firstName == null || lastName == null) {
      return null;
    }
    // 简称
    Set<String> nameList = buildInitName(firstName, lastName, otherName);
    // 全称
    nameList.addAll(buildFullName(firstName, lastName, otherName));
    return nameList;
  }

  /**
   * 匹配用户名与作者名.
   * 
   * @param authorName
   * @param firstName
   * @param lastName
   * @param otherName
   * @return
   */
  public static boolean matchUserAuthorName(String authorName, String firstName, String lastName, String otherName) {
    if (authorName == null || firstName == null || lastName == null) {
      return false;
    }
    authorName = XmlUtil.getCleanAuthorName(authorName);
    // 构造人员各种名字组合
    Set<String> allName = buildAllName(firstName, lastName, otherName);
    Iterator<String> iter = allName.iterator();
    while (iter.hasNext()) {
      // 匹配上了
      if (authorName.equalsIgnoreCase(iter.next())) {
        return true;
      }
    }
    return false;
  }

  /**
   * 构造isi用户全称组合拼接方式.
   * 
   * @param firstName
   * @param joined
   * @return
   */
  private static String buildFullJoin(String firstName, String joined) {

    String[] strNames = firstName.split("[\\s\\-\\._]{1,}");
    StringBuilder sb = new StringBuilder();
    for (String strName : strNames) {
      if (StringUtils.isBlank(strName)) {
        continue;
      }
      if (sb.length() > 0) {
        sb.append(joined);
      }
      sb.append(strName.trim());
    }
    return sb.length() > 0 ? sb.toString() : null;
  }

  /**
   * 处理用户姓名.
   * 
   * @param name
   * @return
   */
  private static String formatName(String name) {
    if (StringUtils.isBlank(name)) {
      return null;
    }
    return StringUtils.lowerCase(StringUtils.trim(name));

  }

  public static void main(String[] args) {

    System.out.println(buildFullName("qing hua", "ou yang", null));
    System.out.println(buildFullName("qing hua", "si-tu", null));
    System.out.println(buildInitName("qing hua", "ou yang", null));
    System.out.println(buildPrefixName("qing hua", "ou yang", null));
  }
}
