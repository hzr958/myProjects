package com.smate.center.task.service.pdwh.quartz;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.pub.TemTaskSnsBriefDao;
import com.smate.center.task.dao.sns.quartz.PubDataStoreDao;
import com.smate.center.task.dao.sns.quartz.PubSimpleDao;
import com.smate.center.task.dao.sns.quartz.PublicationDao;
import com.smate.center.task.dao.sns.quartz.ScmPubXmlDao;
import com.smate.center.task.model.sns.quartz.PubDataStore;
import com.smate.center.task.model.sns.quartz.PubXmlProcessContext;
import com.smate.center.task.model.sns.quartz.ScmPubXml;
import com.smate.center.task.model.sns.quartz.TemTaskSnsBrief;
import com.smate.center.task.single.factory.pub.BriefDriverFactory;
import com.smate.center.task.single.oldXml.pub.PubXmlDocument;
import com.smate.center.task.single.service.pub.IBriefDriver;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.pub.BriefFormatter;

@Service("updateSnsPubBriefService")
@Transactional(rollbackFor = Exception.class)
public class UpdateSnsPubBriefServiceImpl implements UpdateSnsPubBriefService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PublicationDao publicationDao;
  @Autowired
  private ScmPubXmlDao scmPubXmlDao;
  @Autowired
  private PubSimpleDao pubSimpleDao;
  @Autowired
  private TemTaskSnsBriefDao temTaskSnsBriefDao;
  @Autowired
  PubDataStoreDao pubDataStoreDao;
  /**
   * Xml Brief字段(来源)生成驱动工厂.
   */
  private BriefDriverFactory briefDriverFactory;

  @Override
  public List<TemTaskSnsBrief> getUpdatePubIdList(Integer size) throws Exception {
    return temTaskSnsBriefDao.getUpdatePublist(size);
  }

  @Override
  public void updateSnsPubBrie(List<TemTaskSnsBrief> pubList) throws Exception {
    if (CollectionUtils.isEmpty(pubList)) {
      return;
    }
    for (TemTaskSnsBrief tempPub : pubList) {
      try {
        ScmPubXml pubxml = scmPubXmlDao.get(tempPub.getPubId());
        PubDataStore pubStore = pubDataStoreDao.get(tempPub.getPubId());
        if (pubxml != null) {
          PubXmlDocument doc = new PubXmlDocument(pubxml.getPubXml());
          String formTmpl = doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "tmpl_form");
          Integer typeId = Integer.valueOf(doc.getXmlNodeAttribute(PubXmlConstants.PUB_TYPE_XPATH, "id"));
          IBriefDriver briefDriver = briefDriverFactory.getDriver(formTmpl, typeId);
          if (briefDriver != null) {
            String briefZh = getLanguagesBrief(LocaleUtils.toLocale("zh_CN"), doc, briefDriver);
            doc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc", briefZh);
            String briefEn = getLanguagesBrief(LocaleUtils.toLocale("en_US"), doc, briefDriver);
            doc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc_en", briefEn);
            pubxml.setPubXml(doc.getXmlString());

            publicationDao.updateBrief(briefZh, briefEn, tempPub.getPubId());
            pubSimpleDao.updateBrief(briefZh, briefEn, tempPub.getPubId());
            scmPubXmlDao.save(pubxml);
            if (pubStore != null) {// 更新另一张xml表
              PubXmlDocument docStore = new PubXmlDocument(pubStore.getData());
              docStore.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc", briefZh);
              docStore.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc_en", briefEn);
              pubStore.setData(docStore.getXmlString());
              pubDataStoreDao.save(pubStore);
            }
            saveSuccessMsg(tempPub);// 更新状态
          } else {
            saveErrorMsg(tempPub, "briefDriver为空pub_id=" + tempPub.getPubId());
          }
        } else {
          saveErrorMsg(tempPub, "SCM_PUB_XML获取为空pub_id=" + tempPub.getPubId());
        }
      } catch (Exception e) {
        String errorStr = e.toString();
        if (e.toString().length() > 1000) {
          errorStr = e.toString().substring(0, 1000);
        }
        saveErrorMsg(tempPub, errorStr);
        logger.error("更新个人库成果的brief_desc字段出错，pub_id=" + tempPub.getPubId(), e);
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

  /**
   * 保存错误信息
   * 
   * @param tempPub
   * @param errMsg
   */
  private void saveErrorMsg(TemTaskSnsBrief tempPub, String errMsg) {
    tempPub.setStatus(2);
    errMsg = ObjectUtils.toString(errMsg);
    if (errMsg.length() > 700) {
      errMsg = errMsg.substring(0, 700);
    }
    tempPub.setErrMsg(errMsg);
    temTaskSnsBriefDao.save(tempPub);
  }

  /**
   * 保存成功信息
   * 
   * @param tempPub
   */
  private void saveSuccessMsg(TemTaskSnsBrief tempPub) {
    tempPub.setStatus(1);
    temTaskSnsBriefDao.save(tempPub);
  }

  public BriefDriverFactory getBriefDriverFactory() {
    return briefDriverFactory;
  }

  public void setBriefDriverFactory(BriefDriverFactory briefDriverFactory) {
    this.briefDriverFactory = briefDriverFactory;
  }

}
