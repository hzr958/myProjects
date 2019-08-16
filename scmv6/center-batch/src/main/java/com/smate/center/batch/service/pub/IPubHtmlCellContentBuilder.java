package com.smate.center.batch.service.pub;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.smate.center.batch.model.sns.pub.HtmlCellConfig;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.core.base.utils.exception.PubHtmlCellContentBuildException;

/**
 * @author yamingd 通过Xml构建成果页面表格Cell的内容
 */
public interface IPubHtmlCellContentBuilder {

  /**
   * 返回生成配置.
   * 
   * @return HtmlCellConfig
   */
  HtmlCellConfig getHtmlCellConfig();

  /**
   * 返回静态文件web应用Context.
   * 
   * @return String
   */
  String getResappContext();

  /**
   * 返回Scholar或ROL站点Context.
   * 
   * @return String
   */
  String getWebappContext();

  /**
   * 返回当前节点ID.
   * 
   * @return
   */
  int getCurrentNodeId();

  /**
   * 返回xml字段集合. 数据字段形如：/publication/@zh_title
   * 
   * @return List<String>
   */
  List<String> getXmlFields();

  /**
   * 读取xml字段内容，构造页面表格显示Cell的内容.
   * 
   * @param cellConfig 生成配置
   * @param xmlDocument 成果xml
   * @param citedTimes 成果引用次数,来自数据库
   * @param citedDate 成果引用次数更新日期，来自数据库
   * @param impactFactors 成果影响因子，来自数据库
   * @return String 返回拼接好的htmlCell内容
   */
  String build(Locale locale, PubXmlDocument xmlDocument, Integer citedTimes, Date citedDate, String impactFactors)
      throws PubHtmlCellContentBuildException;


}
