package com.smate.center.open.service.pubinfo;

import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.string.PinYinUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.string.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

/**
 * 成果验证工具.
 *
 * @author tsz
 *
 */
public class PubInfoVerifyUtil {

  public static Integer title_max_len = 2000;
  public static Integer keycode_max_len = 20;
  public static Integer authors_max_len = 1000;
  public static Integer email_max_len = 50;
  public static Integer tel_max_len = 20;
  public static Integer name_max_len = 60;
  public static Integer paper_type = 1;
  public static Integer home_pub_type = 2;




  public static boolean compareNames(String name, String compareName) {
    name = XmlUtil.getCleanAuthorName5(name);
    compareName = XmlUtil.getCleanAuthorName5(compareName);
    if (StringUtils.isBlank(name) || StringUtils.isBlank(compareName)) {
      return false;
    }
    Boolean isNameCh = ServiceUtil.isChineseStr(name);
    Boolean isCompareNameCh = ServiceUtil.isChineseStr(compareName);
    if (isNameCh && isCompareNameCh) {
      // 全是中文,
      name = name.replaceAll("[\\s]","");
      compareName = compareName.replaceAll("[\\s]","");
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
  private static boolean compareNameNotAllCh(String name, String compareName) {
    if (name.equalsIgnoreCase(compareName)) {
      return true;
    }
    Set<String> nameList = PubInfoVerifyUtil.buildName(name);
    Set<String> compareNameList = PubInfoVerifyUtil.buildName(compareName);

    if (CollectionUtils.containsAny(nameList, compareNameList)) {
      return true;
    }
    return false;
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
    Map<String, Set<String>> souMap = PinYinUtils.generatePsnName(name.replaceAll("\\d+", ""));
    if (souMap != null) {
      nameList.addAll(souMap.get("fullname"));
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

  /**
   * 作者列表中中含有  blank 数据 要去除
   * @param authorsNames
   * @return
   */
  public static String[] dealBlankAuthorsNames(String[] authorsNames){
    String[] names = {};
    List<String> list = new ArrayList<>();
    if(authorsNames != null && authorsNames.length > 0){
      for(String name : authorsNames){
        name = XmlUtil.getCleanAuthorName5(name);
        if(StringUtils.isNotBlank(name)){
          list.add(name);
        }
      }
      if(list != null && list.size()>0){
        names = ArrayUtils.toStringArray(list.toArray());
      }
    }
    return names ;
  }

  /**
   * 允许分号分割作者名
   *  1,存在分号
   *  2， 不存在分号和 逗号的情况下
   * @return
   */
  public static boolean  canSemSplit(String authorNames){
    if(StringUtils.isBlank(authorNames)){
      return false ;
    }
    if(authorNames.contains(";")){
      return  true ;
    }
    if(authorNames.indexOf(",")<0){
      return  true ;
    }
    return false  ;
  }

  /**
   * 允许逗号分割作者名
   *  1,存在分号
   *  2， 不存在分号和 逗号的情况下
   * @return
   */
  public static boolean  canDotSplit(String authorNames){
    if(StringUtils.isBlank(authorNames)){
      return false ;
    }
    if(authorNames.contains(",")){
      return  true ;
    }
    return false  ;
  }



  public static void main(String[] args) {

  }
}
