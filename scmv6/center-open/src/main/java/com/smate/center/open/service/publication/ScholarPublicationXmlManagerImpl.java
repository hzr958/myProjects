/**
 * 
 */
package com.smate.center.open.service.publication;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.open.form.PublicationForm;
import com.smate.center.open.model.publication.PubXmlDocument;
import com.smate.center.open.model.publication.PublicationXml;
import com.smate.center.open.utils.xml.PubConstFieldRefresh;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.utils.constant.PubXmlConstants;

/**
 * 成果XML处理服务(导入、修改、新增).
 * 
 * @author yamingd
 */
@Service("scholarPublicationXmlManager")
/* @Transactional(rollbackFor = Exception.class) */// 千万不能加事务。tsz
public class ScholarPublicationXmlManagerImpl implements ScholarPublicationXmlManager {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PublicationXmlService publicationXmlService;
  @Autowired
  private IPubXmlServiceFactory scholarPublicationXmlServiceFactory;
  /* @Autowired */
  private PsnCnfService psnCnfService;

  @Override
  public PublicationForm loadXml(PublicationForm form) throws Exception {
    try {
      // 获取缓存数据
      PublicationXml xml = this.publicationXmlService.getById(form.getPubId());
      if (xml == null || StringUtils.isBlank(xml.getXmlData())) {
        throw new Exception(form.getPubId().toString());
      }
      String xmlData = xml.getXmlData();

      PubXmlDocument xmlDocument = new PubXmlDocument(xmlData);

      // 刷新常数字段
      PubConstFieldRefresh.refresh(xmlDocument, this.scholarPublicationXmlServiceFactory);

      this.checkContainsHtml(xmlDocument, form);
      form.setPubXml(xmlDocument.getXmlString());
      return form;
    } catch (Exception e) {
      logger.error("loadXml加载XML,转换为XmlDocument错误, pubId=" + form.getPubId(), e);
      throw new Exception(e);
    }
  }

  private void checkContainsHtml(PubXmlDocument xmlDocument, PublicationForm form) {
    String zhAbstract = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_abstract");
    String enAbstract = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_abstract");
    String regEx_html = "<[^>]+>";
    java.util.regex.Pattern pattern = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
    java.util.regex.Matcher zhAbstractMatcher = pattern.matcher(zhAbstract);
    java.util.regex.Matcher enAbstractMatcher = pattern.matcher(enAbstract);
    form.setIsHtmlZhAbstract(zhAbstractMatcher.find());
    form.setIsHtmlEnAbstract(enAbstractMatcher.find());
  }

  @Override
  public PubXmlDocument getPubXml(Long pubId) throws Exception {
    try {
      // 获取缓存数据
      PublicationXml xml = this.publicationXmlService.getById(pubId);
      if (xml == null || StringUtils.isBlank(xml.getXmlData())) {
        throw new Exception(pubId.toString());
      }
      String xmlData = xml.getXmlData();
      PubXmlDocument xmlDocument = new PubXmlDocument(xmlData);
      return xmlDocument;
    } catch (Exception e) {
      logger.error("getPubXml获取XML,转换为XmlDocument错误, pubId=" + pubId, e);
      throw new Exception(e);
    }
  }

}
