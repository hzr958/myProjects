package com.smate.center.batch.model.sns.pub;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.IPubHtmlCellContentBuilder;
import com.smate.center.batch.service.pub.PublicationXmlService;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.exception.InvalidXpathException;
import com.smate.core.base.utils.exception.PubHtmlCellContentBuildException;
import com.smate.core.base.utils.string.IrisNumberUtils;

/**
 * citewrite的实用方法.
 * 
 * @author LY
 * 
 */
@Service("citeWriteDataBuilder")
@Transactional(rollbackFor = Exception.class)
public class CiteWriteDataBuilder {
  private static Logger logger = LoggerFactory.getLogger(CiteWriteDataBuilder.class);
  private static List<String> fields = new ArrayList<String>();

  @Autowired
  private IPubHtmlCellContentBuilder scholarHtmlCellContentBuilder;
  @Autowired
  private PublicationXmlService publicationXmlService;
  static {
    fields.add("/publication/@author_names");
    fields.add("/publication/@brief_desc");
    fields.add("/publication/@brief_desc_en");
    fields.add("/publication/@zh_title");
    fields.add("/publication/@en_title");
    fields.add("/pub_meta/@source_url");
    fields.add("/pub_meta/@zh_source_db_name");
    fields.add("/pub_meta/@en_source_db_name");
    fields.add("/pub_fulltext/@fulltext_url");
    fields.add("/pub_fulltext/@file_id");
    fields.add("/publication/@cited_url");
    fields.add("/publication/@issue");
    fields.add("/publication/@volume");
    fields.add("/publication/@start_page");
    fields.add("/publication/@end_page");
    fields.add("/pub_journal/@jname");
    fields.add("/pub_journal/@jissn");
    fields.add("/publication/@en_abstract");
    fields.add("/publication/@zh_abstract");
    fields.add("/publication/@zh_keywords");
    fields.add("/publication/@en_keywords");
  }

  public static List<String> getFields() {
    return fields;
  }

  public static PublicationCw buildPublicationCw(Publication item, PubXmlDocument xmlDocument, Locale locale)
      throws NumberFormatException, PubHtmlCellContentBuildException {
    if (xmlDocument == null) {
      throw new NullPointerException("xmlDocument参数不能为空。");
    }
    if (locale == null) {
      throw new NullPointerException("locale参数不能为空。");
    }
    Map<String, String> data;
    String pubId = String.valueOf(xmlDocument.getPubId());

    try {
      data = xmlDocument.getFieldsData(getFields());
      String authorNames = data.get("/publication/@author_names");
      String zhTitle = data.get("/publication/@zh_title");
      String enTitle = data.get("/publication/@en_title");
      String briefDesc = data.get("/publication/@brief_desc");
      String briefDescEn = data.get("/publication/@brief_desc_en");
      String sourceUrl = data.get("/pub_meta/@source_url");
      String fulltextUrl = data.get("/pub_fulltext/@fulltext_url");
      String fileId = data.get("/pub_fulltext/@file_id");
      String citedUrl = data.get("/publication/@cited_url");
      String zhSourceDbName = data.get("/pub_meta/@zh_source_db_name");
      String enSourceDbName = data.get("/pub_meta/@en_source_db_name");
      String issue = data.get("/publication/@issue");
      String volume = data.get("/publication/@volume");
      String startPage = data.get("/publication/@start_page");
      String endPage = data.get("/publication/@end_page");
      String jname = data.get("/pub_journal/@jname");
      String issn = data.get("/pub_journal/@jissn");
      String enAbstract = data.get("/publication/@en_abstract");
      String zhAbstract = data.get("/publication/@zh_abstract");
      String zhKeywords = data.get("/publication/@zh_keywords");
      String enKeywords = data.get("/publication/@en_keywords");
      String sourceDbName = XmlUtil.getLanguageSpecificText(locale.getLanguage(), zhSourceDbName, enSourceDbName);
      String title = XmlUtil.getLanguageSpecificText(locale.getLanguage(), zhTitle, enTitle);
      String keywords = XmlUtil.getLanguageSpecificText(locale.getLanguage(), zhKeywords, enKeywords);
      String abstractText = XmlUtil.getLanguageSpecificText(locale.getLanguage(), zhAbstract, enAbstract);

      PublicationCw pubCw = new PublicationCw();
      pubCw.setId(item.getId());
      pubCw.setHtmlAbstract(item.getHtmlAbstract());
      pubCw.setTypeId(item.getTypeId());
      pubCw.setTypeName(item.getTypeName());
      pubCw.setPublishYear(item.getPublishYear());
      pubCw.setPublishMonth(item.getPublishMonth());
      pubCw.setPublishDay(item.getPublishDay());
      pubCw.setTitle(title);
      pubCw.setAuthorNames(authorNames);
      pubCw.setBriefDesc(briefDesc);
      pubCw.setBriefDescEn(briefDescEn);
      pubCw.setSourceDbName(sourceDbName);
      pubCw.setIssue(issue);
      pubCw.setVolume(volume);
      pubCw.setStartPage(IrisNumberUtils.createInteger(startPage));
      pubCw.setEndPage(IrisNumberUtils.createInteger(endPage));
      pubCw.setJname(jname);
      pubCw.setIssn(issn);
      pubCw.setAbstractText(abstractText);
      pubCw.setKeywords(keywords);

      // <input type="hidden" name="PUB_DATE_DESC" id="PUB_DATE_DESC"
      // value=""/>
      // <input type="hidden" name="PUBLISH_DATE" id="PUBLISH_DATE"
      // value="2009"/>
      return pubCw;

    } catch (InvalidXpathException e1) {
      throw new PubHtmlCellContentBuildException(Long.parseLong(pubId), e1);
    }
  }

  public static PublicationEndNote buildPublicationEndNote(Publication item, PubXmlDocument xmlDocument, Locale locale)
      throws NumberFormatException, PubHtmlCellContentBuildException {
    if (xmlDocument == null) {
      throw new NullPointerException("xmlDocument参数不能为空。");
    }
    if (locale == null) {
      throw new NullPointerException("locale参数不能为空。");
    }
    Map<String, String> data;
    String pubId = String.valueOf(xmlDocument.getPubId());

    try {
      data = xmlDocument.getFieldsData(getFields());
      String authorNames = data.get("/publication/@author_names");
      String zhTitle = data.get("/publication/@zh_title");
      String enTitle = data.get("/publication/@en_title");
      String fulltextUrl = data.get("/pub_fulltext/@fulltext_url");
      String issue = data.get("/publication/@issue");
      String volume = data.get("/publication/@volume");
      String startPage = data.get("/publication/@start_page");
      String endPage = data.get("/publication/@end_page");
      String jname = data.get("/pub_journal/@jname");
      String issn = data.get("/pub_journal/@jissn");
      String enAbstract = data.get("/publication/@en_abstract");
      String zhAbstract = data.get("/publication/@zh_abstract");
      String zhKeywords = data.get("/publication/@zh_keywords");
      String enKeywords = data.get("/publication/@en_keywords");
      String sourceUrl = data.get("/pub_type/@source_url");
      String proceedingTitle = data.get("/publication/@proceeding_title");
      String confVenue = data.get("/publication/@conf_venue");
      String original = data.get("/publication/@original");
      String articleNumber = data.get("/publication/@article_number");
      String title = XmlUtil.getLanguageSpecificText(locale.getLanguage(), zhTitle, enTitle);
      String keywords = XmlUtil.getLanguageSpecificText(locale.getLanguage(), zhKeywords, enKeywords);
      String abstractText = XmlUtil.getLanguageSpecificText(locale.getLanguage(), zhAbstract, enAbstract);
      // 修改了语言设置值和作者人员名称_MJG_2013-07-23_SCM-3101.
      String language = LocaleContextHolder.getLocale().toString();
      if ("zh_CN".equals(language)) {
        if (!"".equalsIgnoreCase(zhTitle))
          language = "Chinese";
        else
          language = "English";
      } else {
        if (!"".equalsIgnoreCase(enTitle))
          language = "English";
        else
          language = "Chinese";
      }
      if (StringUtils.isNotBlank(authorNames)) {
        authorNames = authorNames.replace("<strong>", "");
        authorNames = authorNames.replace("</strong>", "");
      }

      PublicationEndNote endNote = new PublicationEndNote();
      endNote.setId(item.getId());
      endNote.setTypeId(item.getTypeId());
      endNote.setTypeName(item.getTypeName());
      endNote.setAuthorNames(authorNames);
      endNote.setPublishYear(item.getPublishYear());
      endNote.setPublishMonth(item.getPublishMonth());
      endNote.setPublishDay(item.getPublishDay());
      endNote.setTitle(title);
      endNote.setJname(jname);
      endNote.setVolume(volume);
      endNote.setIssue(issue);
      endNote.setStartPage(IrisNumberUtils.createInteger(startPage));
      endNote.setEndPage(IrisNumberUtils.createInteger(endPage));
      endNote.setIssn(issn);
      endNote.setDoi(item.getDoi());
      endNote.setAccessionNumber("");
      endNote.setKeywords(keywords);
      endNote.setAbstractText(abstractText);
      endNote.setMaterialsandMethods("");
      endNote.setResults("");
      endNote.setNotes("");
      endNote.setCitedTimes(item.getCitedTimes());
      endNote.setCitedReferencesCount("");
      endNote.setUrl(fulltextUrl);
      endNote.setAuthorAddress("");
      endNote.setLanguage(language);
      endNote.setSourceUrl(sourceUrl);
      endNote.setProceedingTitle(proceedingTitle);
      endNote.setConfVenue(confVenue);
      endNote.setOriginal(original);
      endNote.setArticleNumber(articleNumber);

      return endNote;

    } catch (InvalidXpathException e1) {
      throw new PubHtmlCellContentBuildException(Long.parseLong(pubId), e1);
    }
  }

  /**
   * 为CiteWrite准备的数据.
   * 
   * @param result
   * @param b
   * @return
   * @throws ServiceException
   */
  public List<PublicationCw> wrapPopulateDataFromCw(List<Publication> outputs, boolean isFillErrorField)
      throws ServiceException {
    if (outputs.size() == 0) {
      return null;
    }
    List<PublicationCw> list = new ArrayList<PublicationCw>();
    for (int i = 0; i < outputs.size(); i++) {
      Publication item = outputs.get(i);
      PublicationCw pubCW = this.populateDataForCw(item, isFillErrorField);
      pubCW.setTitle(StringEscapeUtils.unescapeHtml(pubCW.getTitle()));
      pubCW.setBriefDesc(StringEscapeUtils.unescapeHtml(pubCW.getBriefDesc()));
      list.add(pubCW);
    }
    return list;
  }

  /**
   * 为EndNote准备的数据.
   * 
   * @param outputs
   * @param isFillErrorField
   * @return
   * @throws ServiceException
   */
  public List<PublicationEndNote> wrapPopulateDataFromEndNote(List<Publication> outputs, boolean isFillErrorField)
      throws ServiceException {
    if (outputs.size() == 0) {
      return null;
    }
    List<PublicationEndNote> list = new ArrayList<PublicationEndNote>();
    for (int i = 0; i < outputs.size(); i++) {
      Publication item = outputs.get(i);
      list.add(this.populateDataForEndNote(item, isFillErrorField));
    }
    return list;
  }

  /**
   * 为citeWrite准备数据.
   * 
   * @param item
   * @param isFillErrorField
   * @return
   * @throws ServiceException
   */
  @SuppressWarnings("unchecked")
  public PublicationCw populateDataForCw(Publication item, boolean isFillErrorField) throws ServiceException {
    try {
      Locale locale = LocaleContextHolder.getLocale();
      PublicationXml xmlData;
      xmlData = this.publicationXmlService.getById(item.getId());
      PubXmlDocument xmlDocument;
      try {
        xmlDocument = new PubXmlDocument(xmlData.getXmlData());
      } catch (DocumentException e1) {
        logger.error(String.format("populateDataFromXml转换成果xml错误。pubId=%s", item.getId()), e1);
        throw new ServiceException(e1);
      }
      try {

        String html = this.scholarHtmlCellContentBuilder.build(locale, xmlDocument, item.getCitedTimes(),
            item.getCitedDate(), item.getImpactFactors());

        item.setHtmlAbstract(html);
        // 是否需要获取错误信息.
        if (isFillErrorField) {
          List<Node> errorFields = xmlDocument.getPubErrorFields();
          if (errorFields != null && errorFields.size() > 0) {
            Set<ErrorField> erFields = new HashSet<ErrorField>();
            for (Node errorField : errorFields) {
              Element element = (Element) errorField;
              String fieldName = element.attributeValue("field");
              String code = element.attributeValue("error_no");
              ErrorField errorField2 = new ErrorField(fieldName, NumberUtils.toInt(code));
              erFields.add(errorField2);
            }
            item.setErrorFields(erFields);
          }
        }
        return CiteWriteDataBuilder.buildPublicationCw(item, xmlDocument, locale);
      } catch (PubHtmlCellContentBuildException e) {
        logger.error(String.format("populateDataFromXml通过成果xml构造Html内容错误。pubId=%s", item.getId()), e);
        throw new ServiceException(e);
      }
    } catch (BatchTaskException e2) {
      // TODO Auto-generated catch block
      throw new ServiceException(e2);
    }
  }

  /**
   * 为EndNote准备数据.
   * 
   * @param item
   * @param isFillErrorField
   * @return
   * @throws ServiceException
   */
  @SuppressWarnings("unchecked")
  public PublicationEndNote populateDataForEndNote(Publication item, boolean isFillErrorField) throws ServiceException {
    try {
      Locale locale = LocaleContextHolder.getLocale();
      PublicationXml xmlData;

      xmlData = this.publicationXmlService.getById(item.getId());

      PubXmlDocument xmlDocument;
      try {
        xmlDocument = new PubXmlDocument(xmlData.getXmlData());
      } catch (DocumentException e1) {
        logger.error(String.format("populateDataFromXml转换成果xml错误。pubId=%s", item.getId()), e1);
        throw new ServiceException(e1);
      }
      try {

        String html = this.scholarHtmlCellContentBuilder.build(locale, xmlDocument, item.getCitedTimes(),
            item.getCitedDate(), item.getImpactFactors());

        item.setHtmlAbstract(html);
        // 是否需要获取错误信息.
        if (isFillErrorField) {
          List<Node> errorFields = xmlDocument.getPubErrorFields();
          if (errorFields != null && errorFields.size() > 0) {
            Set<ErrorField> erFields = new HashSet<ErrorField>();
            for (Node errorField : errorFields) {
              Element element = (Element) errorField;
              String fieldName = element.attributeValue("field");
              String code = element.attributeValue("error_no");
              ErrorField errorField2 = new ErrorField(fieldName, NumberUtils.toInt(code));
              erFields.add(errorField2);
            }
            item.setErrorFields(erFields);
          }
        }
        return CiteWriteDataBuilder.buildPublicationEndNote(item, xmlDocument, locale);
      } catch (PubHtmlCellContentBuildException e) {
        logger.error(String.format("populateDataFromXml通过成果xml构造Html内容错误。pubId=%s", item.getId()), e);
        throw new ServiceException(e);
      }
    } catch (BatchTaskException e2) {
      // TODO Auto-generated catch block
      throw new ServiceException(e2);
    }
  }

  /**
   * 为citeWrite准备数据.
   * 
   * @param item
   * @param xml
   * @param isFillErrorField
   * @return
   * @throws ServiceException
   */
  @SuppressWarnings("unchecked")
  public PublicationCw populateDataForCw(Publication item, String xml, boolean isFillErrorField)
      throws ServiceException {
    if (StringUtils.isBlank(xml)) {
      return this.populateDataForCw(item, isFillErrorField);
    }
    Locale locale = LocaleContextHolder.getLocale();
    PubXmlDocument xmlDocument;
    try {
      xmlDocument = new PubXmlDocument(xml);
    } catch (DocumentException e1) {
      logger.error(String.format("populateDataFromXml转换成果xml错误。pubId=%s", item.getId()), e1);
      throw new ServiceException(e1);
    }
    try {

      String html = this.scholarHtmlCellContentBuilder.build(locale, xmlDocument, item.getCitedTimes(),
          item.getCitedDate(), item.getImpactFactors());

      item.setHtmlAbstract(html);
      // 是否需要获取错误信息.
      if (isFillErrorField) {
        List<Node> errorFields = xmlDocument.getPubErrorFields();
        if (errorFields != null && errorFields.size() > 0) {
          Set<ErrorField> erFields = new HashSet<ErrorField>();
          for (Node errorField : errorFields) {
            Element element = (Element) errorField;
            String fieldName = element.attributeValue("field");
            String code = element.attributeValue("error_no");
            ErrorField errorField2 = new ErrorField(fieldName, NumberUtils.toInt(code));
            erFields.add(errorField2);
          }
          item.setErrorFields(erFields);
        }
      }
      return CiteWriteDataBuilder.buildPublicationCw(item, xmlDocument, locale);
    } catch (PubHtmlCellContentBuildException e) {
      logger.error(String.format("populateDataFromXml通过成果xml构造Html内容错误。pubId=%s", item.getId()), e);
      throw new ServiceException(e);
    }
  }

  /**
   * 为citeWrite准备数据.
   * 
   * @param item
   * @param xml
   * @param isFillErrorField
   * @return
   * @throws ServiceException
   */
  @SuppressWarnings("unchecked")
  public PublicationEndNote populateDataForEndNote(Publication item, String xml, boolean isFillErrorField)
      throws ServiceException {
    if (StringUtils.isBlank(xml)) {
      return this.populateDataForEndNote(item, isFillErrorField);
    }
    Locale locale = LocaleContextHolder.getLocale();
    PubXmlDocument xmlDocument;
    try {
      xmlDocument = new PubXmlDocument(xml);
    } catch (DocumentException e1) {
      logger.error(String.format("populateDataFromXml转换成果xml错误。pubId=%s", item.getId()), e1);
      throw new ServiceException(e1);
    }
    try {

      String html = this.scholarHtmlCellContentBuilder.build(locale, xmlDocument, item.getCitedTimes(),
          item.getCitedDate(), item.getImpactFactors());

      item.setHtmlAbstract(html);
      // 是否需要获取错误信息.
      if (isFillErrorField) {
        List<Node> errorFields = xmlDocument.getPubErrorFields();
        if (errorFields != null && errorFields.size() > 0) {
          Set<ErrorField> erFields = new HashSet<ErrorField>();
          for (Node errorField : errorFields) {
            Element element = (Element) errorField;
            String fieldName = element.attributeValue("field");
            String code = element.attributeValue("error_no");
            ErrorField errorField2 = new ErrorField(fieldName, NumberUtils.toInt(code));
            erFields.add(errorField2);
          }
          item.setErrorFields(erFields);
        }
      }
      return CiteWriteDataBuilder.buildPublicationEndNote(item, xmlDocument, locale);
    } catch (PubHtmlCellContentBuildException e) {
      logger.error(String.format("populateDataFromXml通过成果xml构造Html内容错误。pubId=%s", item.getId()), e);
      throw new ServiceException(e);
    }
  }

}
