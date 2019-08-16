package com.smate.sie.center.task.pdwh.brief;

import java.util.Locale;
import java.util.Map;

import com.smate.sie.center.task.pdwh.task.PubXmlDocument;
import com.smate.sie.center.task.pdwh.task.PubXmlProcessContext;

/**
 * @author yamingd 成果Brief生成驱动，提供Pattern和数据
 */
public interface IBriefDriver {

  /**
   * 成果录入模板名称.
   * 
   * @return String
   */
  String getForTmplForm();

  /**
   * 成果类型ID.
   * 
   * @return int
   */
  int getForType();

  /**
   * 返回格式化Pattern.
   * 
   * @return String
   */
  String getPattern();

  /**
   * 返回格式化需要的数据.
   * 
   * @param locale Locale
   * @param xmlDocument XmlDocument
   * @param context Xml处理的上下文对象
   * @return Map<String, String>
   */
  Map<String, String> getData(Locale locale, PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception;
}
