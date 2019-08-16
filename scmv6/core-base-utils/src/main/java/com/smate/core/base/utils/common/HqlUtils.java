package com.smate.core.base.utils.common;

import java.io.BufferedReader;
import java.io.Reader;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.smate.core.base.utils.json.JacksonUtils;

/**
 * cwli.
 */
@SuppressWarnings("unchecked")
public class HqlUtils {
  protected static final Logger LOG = LoggerFactory.getLogger(HqlUtils.class);

  public static String getHqlparams(List<Long> idList) {
    StringBuffer sb = new StringBuffer();
    if (CollectionUtils.isEmpty(idList))
      return null;
    for (Long id : idList) {
      sb.append("?").append(",");
    }
    sb.deleteCharAt(sb.length() - 1);
    return sb.toString();
  }

  public static String getHqlparamsById(List<Long> idList) {
    StringBuffer sb = new StringBuffer();
    if (CollectionUtils.isEmpty(idList))
      return null;
    for (Long id : idList) {
      sb.append(id).append(",");
    }
    sb.deleteCharAt(sb.length() - 1);
    return sb.toString();
  }

  /**
   * Integer,Long类字符串去重复并排序.
   * 
   * @param str
   * @return
   */
  public static String insIdsFormat(String str) {
    if (StringUtils.isBlank(str))
      return null;
    String[] ids = str.split(",");
    List<Long> list = new ArrayList<Long>();
    for (String string : ids) {
      if (StringUtils.isNotBlank(string))
        list.add(Long.valueOf(string));
    }
    list = removeDuplicateWithOrder(list);
    Collections.sort(list);
    str = JacksonUtils.jsonObjectSerializer(list);
    return str.substring(1, str.length() - 1);
  }

  /**
   * 去重复
   * 
   * @param list
   * @return
   */
  @SuppressWarnings("rawtypes")
  public static List removeDuplicateWithOrder(List list) {
    Set set = new HashSet();
    List newList = new ArrayList();
    for (Iterator iter = list.iterator(); iter.hasNext();) {
      Object element = iter.next();
      if (set.add(element))
        newList.add(element);
    }
    return newList;
  }

  /**
   * 获取100之内的随机数
   * 
   * @return
   */
  public static int getRandomMax100() {
    Random random = new Random();
    return Math.abs(random.nextInt()) % 100;
  }

  /**
   * 获取指定数之间的随机数
   * 
   * @param min
   * @param max
   * @return
   */
  public static long getRandom(int min, int max) {
    // 产生1000到9999的随机数
    // Math.round(Math.random()*8999+1000);
    return Math.round(Math.random() * (max - min) + min);
  }

  public static String ClobToString(Clob clob) {
    String reString = "";
    try {
      Reader is = clob.getCharacterStream();
      BufferedReader br = new BufferedReader(is);
      String s = br.readLine();
      StringBuffer sb = new StringBuffer();
      while (s != null) {
        sb.append(s);
        s = br.readLine();
      }
      reString = sb.toString();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return reString;
  }
}
