package com.smate.center.task.single.service.pub;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.pub.PatentHisDataDao;
import com.smate.center.task.dao.sns.quartz.PubDataStoreDao;
import com.smate.center.task.dao.sns.quartz.PubSimpleDao;
import com.smate.center.task.dao.sns.quartz.PublicationDao;
import com.smate.center.task.dao.sns.quartz.ScmPubXmlDao;
import com.smate.center.task.model.sns.quartz.PubDataStore;
import com.smate.center.task.model.sns.quartz.PubSimple;
import com.smate.center.task.model.sns.quartz.PubXmlProcessContext;
import com.smate.center.task.model.sns.quartz.Publication;
import com.smate.center.task.model.sns.quartz.ScmPubXml;
import com.smate.center.task.model.snsbak.PatentHisData;
import com.smate.center.task.single.factory.pub.BriefDriverFactory;
import com.smate.center.task.single.oldXml.pub.PubXmlDocument;
import com.smate.center.task.utils.XmlFragmentCleanerHelper;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.pub.BriefFormatter;
import com.smate.core.base.utils.string.IrisNumberUtils;
import com.smate.core.base.utils.string.StringUtils;

@Service("patentHisDataService")
@Transactional(rollbackFor = Exception.class)
public class PatentHisDataServiceImpl implements PatentHisDataService {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 成果Brief生成驱动工厂.
   */
  private BriefDriverFactory briefDriverFactory;
  @Autowired
  private PatentHisDataDao patentHisDataDao;
  @Autowired
  private ScmPubXmlDao scmPubXmlDao;
  @Autowired
  private PublicationDao publicationDao;
  @Autowired
  private PubDataStoreDao pubDataStoreDao;
  @Autowired
  private PubSimpleDao pubSimpleDao;

  @Override
  public List<PatentHisData> getPatList(Integer size) {
    return patentHisDataDao.getPatList(size);
  }

  @Override
  public void HandlePatentHisData(PatentHisData patData) {
    try {
      ScmPubXml scmPatXml = scmPubXmlDao.get(patData.getSnsPubId());
      PubDataStore pubStore = pubDataStoreDao.get(patData.getSnsPubId());
      Publication publication = publicationDao.get(patData.getSnsPubId());
      PubSimple pubSimple = pubSimpleDao.get(patData.getSnsPubId());
      if (scmPatXml != null) {
        PubXmlDocument doc = new PubXmlDocument(scmPatXml.getPubXml());
        this.handleScmPatXmlData(doc);
        String formTmpl = doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "tmpl_form");
        Integer typeId = Integer.valueOf(doc.getXmlNodeAttribute(PubXmlConstants.PUB_TYPE_XPATH, "id"));
        IBriefDriver briefDriver = briefDriverFactory.getDriver(formTmpl, typeId);
        if (briefDriver != null) {
          String briefZh = getLanguagesBrief(LocaleUtils.toLocale("zh_CN"), doc, briefDriver);
          doc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc", briefZh);
          String briefEn = getLanguagesBrief(LocaleUtils.toLocale("en_US"), doc, briefDriver);
          doc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc_en", briefEn);
          scmPatXml.setPubXml(doc.getXmlString());
          scmPubXmlDao.save(scmPatXml);
          if (pubStore != null) {// 更新另一张xml表
            PubXmlDocument docStore = new PubXmlDocument(pubStore.getData());
            this.handleScmPatXmlData(docStore);
            docStore.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc", briefZh);
            docStore.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc_en", briefEn);
            pubStore.setData(docStore.getXmlString());
            pubDataStoreDao.save(pubStore);
          }
          Integer pubYear = IrisNumberUtils
              .monthDayToInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_year"));
          Integer pubMonth = IrisNumberUtils
              .monthDayToInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_month"));
          Integer pubDay = IrisNumberUtils
              .monthDayToInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_day"));
          if (publication != null) {
            publication.setBriefDesc(briefZh);
            publication.setBriefDescEn(briefEn);
            publication.setPublishYear(pubYear);
            publication.setPublishMonth(pubMonth);
            publication.setPublishDay(pubDay);
            publicationDao.save(publication);
          }
          if (pubSimple != null) {
            pubSimple.setBriefDesc(briefZh);
            pubSimple.setBriefDescEn(briefEn);
            pubSimple.setPublishYear(pubYear);
            pubSimple.setPublishMonth(pubMonth);
            pubSimple.setPublishDay(pubDay);
            pubSimpleDao.save(pubSimple);
          }
        }
      }
      patentHisDataDao.updateHandleStatus(patData.getId(), 1);
    } catch (Exception e) {
      logger.error("处理专利状态出错-----", e);
      patentHisDataDao.updateHandleStatus(patData.getId(), 9);
    }

  }

  private void handleScmPatXmlData(PubXmlDocument xmlDocument) throws Exception {
    // 处理方案与web-pub项目的ImportFieldMappingTask一致
    String[] srcAttrs = new String[] {"effective_start_date", "effective_end_date", "issue_org"};
    String[] desAttrs = new String[] {"start_date", "end_date", "patent_org"};
    for (int i = 0; i < srcAttrs.length; i++) {
      xmlDocument.copyAttributeValue(PubXmlConstants.PUBLICATION_XPATH, srcAttrs[i], PubXmlConstants.PUB_PATENT_XPATH,
          desAttrs[i]);
    }
    if ("授权".equals(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "start_page"))) {
      xmlDocument.setNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_status", "1");
    } else if ("申请".equals(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "start_page"))) {
      xmlDocument.setNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_status", "0");
    }
    if (StringUtils.isBlank(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_status"))) {
      String patentStatus = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "patent_status");
      if (StringUtils.isBlank(patentStatus)) {
        if (StringUtils
            .isNotBlank(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "effective_start_date"))) {
          xmlDocument.setNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_status", "1");
        } else {
          xmlDocument.setNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_status", "0");
        }
      } else {
        xmlDocument.setNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_status", patentStatus);
      }
    }
    String startDate = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "start_date");
    if (StringUtils.isNotBlank(startDate)) {
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_date", startDate);
    }
    // 申请日的取值
    String[] apply_dates = XmlFragmentCleanerHelper.splitDateYearMonth(xmlDocument, PubXmlConstants.PUBLICATION_XPATH,
        "start_date", PubXmlConstants.CHS_DATE_PATTERN);
    if (!"".equals(apply_dates[0])) {
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_year", apply_dates[0]);
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_month", apply_dates[1]);
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_day", apply_dates[2]);
      XmlFragmentCleanerHelper.setDateValueFromYMD(xmlDocument, PubXmlConstants.PUBLICATION_XPATH + "/@publish_",
          PubXmlConstants.CHS_DATE_PATTERN);
    }

  }

  private String getLanguagesBrief(Locale locale, PubXmlDocument xmlDocument, IBriefDriver briefDriver)
      throws Exception {
    PubXmlProcessContext context = new PubXmlProcessContext();
    context.setCurrentLanguage("zh");
    Map result = briefDriver.getData(locale, xmlDocument, context);
    String pattern = briefDriver.getPattern();
    BriefFormatter formatter = new BriefFormatter(locale, result);
    String brief = formatter.format(pattern);
    formatter = null;
    return brief;
  }

  public BriefDriverFactory getBriefDriverFactory() {
    return briefDriverFactory;
  }

  public void setBriefDriverFactory(BriefDriverFactory briefDriverFactory) {
    this.briefDriverFactory = briefDriverFactory;
  }

}
