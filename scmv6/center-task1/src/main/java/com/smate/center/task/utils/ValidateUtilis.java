package com.smate.center.task.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.smate.center.task.model.sns.pub.ErrorField;
import com.smate.center.task.single.constants.ErrorNoEnum;
import com.smate.center.task.single.oldXml.pub.PubXmlDocument;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.exception.InvalidXpathException;

/**
 * @author yamingd 校验工具类
 */
public class ValidateUtilis {
  /**
   * 读取字段数据
   * 
   * @param fields 字段集合
   * @param xmlDoc XmlDocument
   * @return Map<String, String>
   * @throws InvalidXpathException InvalidXpathException
   * @throws com.smate.web.pub.exception.InvalidXpathException
   */
  public static Map<String, String> getFieldsData(List<String> fields, PubXmlDocument xmlDoc)
      throws InvalidXpathException {
    Map<String, String> data = new HashMap<String, String>();
    for (int index = 0; index < fields.size(); index++) {
      String xpath = fields.get(index);
      String value = xmlDoc.getXmlNodeAttribute(xpath);
      data.put(xpath, value);
    }
    return data;
  }

  /**
   * 检查字段是否为空.
   * 
   * @param name 字段名
   * @param xpath 字段路径
   * @param datas 数据
   * @param errors 错误字段集合
   */
  public static void fieldIsEmpty(String name, String xpath, Map<String, String> datas, List<ErrorField> errors) {
    String value = datas.get(xpath);
    if (XmlUtil.isEmpty(value)) {
      if (name == null) {
        name = xpath.split("/@")[1];
      }
      ErrorField ef = new ErrorField(name, ErrorNoEnum.Empty);
      errors.add(ef);
    }
  }

  /**
   * 检查字段是否为空.
   * 
   * @param xpath 字段路径
   * @param datas 数据
   * @param errors 错误字段集合
   */
  public static void fieldIsEmpty(String xpath, Map<String, String> datas, List<ErrorField> errors) {
    String value = datas.get(xpath);
    if (XmlUtil.isEmpty(value)) {
      String name = xpath.split("/@")[1];
      ErrorField ef = new ErrorField(name, ErrorNoEnum.Empty);
      errors.add(ef);
    }
  }

  /**
   * 检查字段是否为空. 只要xpath中有一个不为空就通过检查
   * 
   * @param name 字段名
   * @param xpaths 字段路径
   * @param datas 数据
   * @param errors 错误字段集合
   */
  public static void fieldIsEmpty(String name, String[] xpaths, Map<String, String> datas, List<ErrorField> errors) {
    boolean found = false;
    for (String xpath : xpaths) {
      String value = datas.get(xpath);
      if (!XmlUtil.isEmpty(value)) {
        found = true;
      }
    }
    if (!found) {
      ErrorField ef = new ErrorField(name, ErrorNoEnum.Empty);
      errors.add(ef);
    }
  }
}
