package com.smate.center.batch.util.pub;

import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.Assert;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.ConstDictionary;
import com.smate.center.batch.model.sns.pub.IPubXmlServiceFactory;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.ConstDictionaryManageCb;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.string.IrisNumberUtils;

/**
 * 刷新xml常数字段.
 * 
 * @author yamingd
 */
public class PubConstFieldRefresh {

  /**
   * 
   */
  protected static final Logger LOGGER = LoggerFactory.getLogger(PubConstFieldRefresh.class);

  /**
   * 刷新XML.
   * 
   * @param xmlDocument
   * @param xmlDaoService
   * @param journalSerivce
   * @throws DaoException
   */
  public static void refresh(PubXmlDocument xmlDocument, IPubXmlServiceFactory xmlServiceFactory) throws Exception {

    Assert.notNull(xmlDocument);
    Assert.notNull(xmlServiceFactory);

    Locale locale = LocaleContextHolder.getLocale();

    ConstDictionaryManageCb constDictionaryManage = xmlServiceFactory.getConstDictionaryManage();

    String bookType = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "book_type");
    if (!StringUtils.isBlank(bookType)) {
      try {
        ConstDictionary cd = constDictionaryManage.findConstByCategoryAndCode("pub_book_type", bookType);
        if (cd == null) {
          throw new DaoException("读取pub_book_type常数错误，code=" + bookType);
        } else {
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "zh_book_type_name", cd.getZhCnName());
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "en_book_type_name", cd.getEnUsName());
        }
      } catch (Exception e) {
        throw e;
      }
    }

    String confType = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "conf_paper");
    if (!StringUtils.isBlank(confType)) {
      ConstDictionary cd = constDictionaryManage.findConstByCategoryAndCode("pub_conf_type", confType);
      if (cd == null) {
        throw new DaoException("读取pub_conf_type常数错误，code=" + confType);
      } else {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "zh_conf_type_name", cd.getZhCnName());
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "en_conf_type_name", cd.getEnUsName());
      }
    }

    String confCategory = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "conf_category");
    if (!StringUtils.isBlank(confCategory)) {
      ConstDictionary cd = constDictionaryManage.findConstByCategoryAndCode("pub_conf_category", confCategory);
      if (cd == null) {
        throw new DaoException("读取pub_conf_category常数错误，code=" + confCategory);
      } else {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "zh_conf_category_name",
            cd.getZhCnName());
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "en_conf_category_name",
            cd.getEnUsName());
      }
    }
    String confPaperType = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "paper_type");
    if (!StringUtils.isBlank(confPaperType)) {
      ConstDictionary cd = constDictionaryManage.findConstByCategoryAndCode("pub_paper_type", confPaperType);
      if (cd == null) {
        throw new DaoException("读取pub_paper_type常数错误，code=" + confPaperType);
      } else {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "zh_paper_type_name", cd.getZhCnName());
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "en_paper_type_name", cd.getEnUsName());
      }
    }

    String publishState = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_state");
    if (!StringUtils.isBlank(publishState)) {
      ConstDictionary cd = constDictionaryManage.findConstByCategoryAndCode("pub_publish_state", publishState);
      if (cd == null) {
        throw new DaoException("读取pub_publish_state常数错误，code=" + publishState);
      } else {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_publish_state_name", cd.getZhCnName());
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_publish_state_name", cd.getEnUsName());
      }
    }

    String patentType = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_type");
    if (!StringUtils.isBlank(patentType)) {
      ConstDictionary cd = constDictionaryManage.findConstByCategoryAndCode("pub_patent_type", patentType);
      if (cd == null) {
        throw new DaoException("读取pub_patent_type常数错误，code=" + patentType);
      } else {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "zh_patent_type_name", cd.getZhCnName());
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "en_patent_type_name", cd.getEnUsName());
      }
    }

    String programme = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_THESIS_XPATH, "programme");
    if (!StringUtils.isBlank(programme)) {
      ConstDictionary cd = constDictionaryManage.findConstByCategoryAndCode("pub_thesis_degree", programme);
      if (cd == null) {
        LOGGER.error("读取pub_thesis_degree常数错误，code=" + programme);
      } else {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_THESIS_XPATH, "zh_programme_name", cd.getZhCnName());
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_THESIS_XPATH, "en_programme_name", cd.getEnUsName());
      }
    }

    String jid = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jid");
    if (!StringUtils.isBlank(jid)) {
      try {

        Long userId = IrisNumberUtils
            .createLong(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "last_update_psn_id"));
        if (userId == null) {
          userId = IrisNumberUtils
              .createLong(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "create_psn_id"));
        }
      } catch (NumberFormatException e) {
        throw new DaoException("NumberFormatException读取journal常数错误，jid=" + jid);
      }
    }

    String citeTimes = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cite_times");
    String citeDate = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cited_date");
    if (StringUtils.isNotBlank(citeTimes)) {
      try {
        Integer ct = Integer.valueOf(citeTimes);
        if (ct >= 0) {
          String datePattern = PubXmlConstants.CITE_CHS_DATE_PATTERN;
          if ("en".equals(locale.getLanguage())) {
            datePattern = PubXmlConstants.CITE_ENG_DATE_PATTERN;
          }
          if (StringUtils.isBlank(citeDate)) {
            citeDate = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "create_date");
          }
          if (StringUtils.isNotBlank(citeDate)) {
            citeDate = citeDate.replace("/", "-");
            Date date = DateUtils.parseDate(citeDate, new String[] {"yyyy-MM-dd"});
            citeDate = DateFormatUtils.format(date, datePattern);
            xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cited_date", citeDate);
          }
        }
      } catch (Exception e) {
        xmlDocument.setNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cite_times",
            StringUtils.isBlank(citeTimes) ? "" : citeTimes);
      }
    }
  }
}
