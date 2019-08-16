package com.smate.center.batch.oldXml.prj;

import java.util.Locale;
import java.util.Map;

import com.smate.center.batch.context.OpenProjectContext;

/**
 * 成果Brief生成驱动，提供Pattern和数据.
 * 
 * @author liqinghua
 * 
 */
public interface IPrjBriefDriver {

  /**
   * 成果录入模板名称.
   * 
   * @return String
   */
  String getForTmplForm();

  /**
   * 返回格式化Pattern.
   * 
   * @return String
   */
  String getPattern();

  String getPatternEn();

  /**
   * 返回格式化需要的数据.
   * 
   * @param locale Locale
   * @param xmlDocument XmlDocument
   * @param context Xml处理的上下文对象
   * @return Map<String, String>
   */
  Map<String, String> getData(Locale locale, PrjXmlDocument xmlDocument, OpenProjectContext context) throws Exception;
}
