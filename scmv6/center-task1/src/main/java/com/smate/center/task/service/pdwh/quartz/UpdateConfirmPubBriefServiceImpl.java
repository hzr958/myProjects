package com.smate.center.task.service.pdwh.quartz;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.LocaleUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.rcmd.quartz.PubConfirmRolPubDao;
import com.smate.center.task.model.rcmd.quartz.PubConfirmRolPub;
import com.smate.center.task.model.sns.quartz.PubXmlProcessContext;
import com.smate.center.task.model.sns.quartz.PublicationXml;
import com.smate.center.task.service.rol.quartz.RolPublicationXmlService;
import com.smate.center.task.single.factory.pub.BriefDriverFactory;
import com.smate.center.task.single.oldXml.pub.PubXmlDocument;
import com.smate.center.task.single.service.pub.IBriefDriver;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.pub.BriefFormatter;

@Service("updateConfirmPubBriefService")
@Transactional(rollbackFor = Exception.class)
public class UpdateConfirmPubBriefServiceImpl implements UpdateConfirmPubBriefService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubConfirmRolPubDao pubConfirmRolPubDao;
  @Autowired
  private RolPublicationXmlService rolPublicationXmlService;
  /**
   * Xml Brief字段(来源)生成驱动工厂.
   */
  private BriefDriverFactory briefDriverFactory;

  @Override
  public List<PubConfirmRolPub> getUpdatePubIdList(Integer size, Long endPubId) throws Exception {
    return pubConfirmRolPubDao.batchGetPublist(endPubId, size);
  }

  @Override
  public void updatePubConfirmBrie(List<PubConfirmRolPub> pubList) throws Exception {
    if (CollectionUtils.isEmpty(pubList)) {
      return;
    }
    for (PubConfirmRolPub pub : pubList) {
      try {
        PublicationXml pubxml = rolPublicationXmlService.getById(pub.getRolPubId());
        String xmlData = pubxml.getXmlData();
        if (xmlData != null) {
          Document document = DocumentHelper.parseText(xmlData);
          PubXmlDocument doc = new PubXmlDocument(document);
          String formTmpl = doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "tmpl_form");
          Integer typeId = Integer.valueOf(doc.getXmlNodeAttribute(PubXmlConstants.PUB_TYPE_XPATH, "id"));
          IBriefDriver briefDriver = briefDriverFactory.getDriver(formTmpl, typeId);
          if (briefDriver != null) {
            String briefZh = getLanguagesBrief(LocaleUtils.toLocale("zh_CN"), doc, briefDriver);
            pub.setBriefDesc(briefZh);

            pubConfirmRolPubDao.updateBrief(pub);
          }
        }
      } catch (Exception e) {
        logger.error("更新个人库成果的brief_desc字段出错，pub_id=" + pub.getRolPubId(), e);
        continue;
      }
    }

  }

  private String getLanguagesBrief(Locale locale, PubXmlDocument xmlDocument, IBriefDriver briefDriver)
      throws Exception {
    PubXmlProcessContext context = new PubXmlProcessContext();
    context.setCurrentLanguage(locale.getLanguage());
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
