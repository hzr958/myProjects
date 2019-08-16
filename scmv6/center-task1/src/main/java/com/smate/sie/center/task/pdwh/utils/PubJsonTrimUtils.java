package com.smate.sie.center.task.pdwh.utils;

import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;

import com.smate.sie.core.base.utils.pub.service.PubJsonDTO;

/**
 * 
 * @author lijianming
 * @date 2019年4月30日
 *
 */
public class PubJsonTrimUtils {

  public static void trimAttributeValue(Object obj) {
    // 得到class
    Class cls = obj.getClass();
    // 得到所有属性
    Field[] fields = cls.getDeclaredFields();
    for (int i = 0; i < fields.length; i++) {// 遍历
      try {
        // 得到属性
        Field field = fields[i];
        // 打开私有访问
        field.setAccessible(true);
        // 获取属性的数据类型
        String type = field.getGenericType().toString();
        // 获取属性
        String name = field.getName();
        // 获取属性值
        Object value = field.get(obj);
        // 判定是否为空并去掉左右两边空格
        if (value != null) {
          if (StringUtils.isNotBlank(value.toString()) && ("class java.lang.String").equals(type)) {
            field.set(obj, value.toString().trim());
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public static void main(String[] args) {
    PubJsonDTO json = new PubJsonDTO();
    json.citationsUpdateTime = "   citationsUpdateTime    ";
    json.authorNames = "         authorNames     ";
    json.briefDesc = "    briefDesc    ";
    json.citedUrl = "     citedUrl    ";
    json.des3PsnId = "     des3PsnId   ";
    json.disciplineCode = "   disciplineCode   ";
    json.isPublicName = "    isPublicName   ";
    PubJsonTrimUtils.trimAttributeValue(json);
    System.out.println(json.citationsUpdateTime + " " + json.authorNames);
  }
}
