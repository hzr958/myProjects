package com.smate.web.v8pub.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;

import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.dom.AwardsInfoBean;

/**
 * 属性值的复制工具类
 * 
 * @author YJ
 *
 *         2018年9月5日
 */
public class CopyPropertiesUtils {

  /**
   * 将origin的属性值复制给current，其中属性值为null的话，不进行复制
   * 
   * @param orgin
   * @param current
   */
  public static void MergePropertiesValue(Object orgin, Object current) {
    // 获取所要复制的对象的字段数组
    Field[] fields = orgin.getClass().getDeclaredFields();
    for (int i = 0; i < fields.length; i++) {
      String fieldName = fields[i].getName();
      Object value = getFieldValueByName(fieldName, orgin);
      if (null == value) {
        // 属性值为null的话，不进行属性复制
        continue;
      }
      setFieldValueByName(fieldName, current, value);
    }
  }

  /**
   * 根据字段名进行获取属性值
   * 
   * @param fieldName
   * @param o
   * @return
   */
  private static Object getFieldValueByName(String fieldName, Object o) {
    try {
      String firstLetter = fieldName.substring(0, 1).toUpperCase();
      String getter = "get" + firstLetter + fieldName.substring(1);
      Method method = o.getClass().getMethod(getter, new Class[] {});
      Object value = method.invoke(o, new Object[] {});
      if (value instanceof String) {
        if (StringUtils.isNotEmpty((String) value)) {
          return value;
        } else {
          return null;
        }
      }
      if (value instanceof Long) {
        if (!NumberUtils.isNullOrZero((Long) value)) {
          return value;
        } else {
          return null;
        }
      }
      return value;
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * 根据字段名进行设置属性值
   * 
   * @param fieldName
   * @param o
   * @param value
   */
  private static void setFieldValueByName(String fieldName, Object current, Object value) {
    try {
      Field f = current.getClass().getDeclaredField(fieldName);
      f.setAccessible(true);
      f.set(current, value);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public static void main(String[] args) {
    boolean falg = true;
    AwardsInfoBean newA = new AwardsInfoBean();
    newA.setCategory("newA-Category");
    newA.setCertificateNo("newA-CertificateNo");
    newA.setGrade("");
    // newA.setIssueInsId(43534L);
    // newA.setIssuingAuthority("newA-fdsfdsf");
    AwardsInfoBean oldA = new AwardsInfoBean();
    oldA.setCategory("oldA-Category");
    oldA.setCertificateNo("oldA-35435435");
    oldA.setGrade("oldA-Grade");
    oldA.setIssueInsId(11111L);
    oldA.setIssuingAuthority("oldA-hgnhgnh3");
    if (falg) {
      MergePropertiesValue(newA, oldA);
      System.out.println(oldA);
    } else {
      MergePropertiesValue(oldA, newA);
      System.out.println(newA);
    }

    // System.out.println(Des3Utils.encodeToDes3("21000000691"));
  }

}
