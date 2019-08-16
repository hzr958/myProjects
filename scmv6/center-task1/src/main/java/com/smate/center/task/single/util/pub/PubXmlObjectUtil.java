package com.smate.center.task.single.util.pub;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import com.smate.center.task.model.sns.quartz.PubDataStore;
import com.smate.center.task.model.sns.quartz.PubSimple;
import com.smate.center.task.model.sns.quartz.PubXmlProcessContext;
import com.smate.center.task.model.sns.quartz.Publication;
import com.smate.center.task.single.oldXml.pub.PubXmlDocument;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.string.IrisNumberUtils;


/**
 * 成果从Xml构建对象工具类.
 * 
 * @author lxz
 * 
 */
public class PubXmlObjectUtil {

  /**
   * 从XML中给PubSimple赋值
   *
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param doc
   * @param pubSimple
   * @return
   */
  public static PubSimple getPubSimpleFromXml(PubXmlDocument doc, PubSimple pubSimple) {
    Assert.notNull(doc);
    Assert.notNull(pubSimple);
    Long pubId = doc.getNewPubId();
    Integer articleType = doc.getArticleTypeId();
    Integer sourceDbId = null;
    if (doc.getSourceDbId() != null && doc.getSourceDbId() > 0) {
      sourceDbId = doc.getSourceDbId().intValue();
    }
    Integer pubType = doc.getPubTypeId();
    Integer publishYear = IrisNumberUtils.toInteger(doc.getPublishYear());
    Integer publishMonth = IrisNumberUtils.toInteger(doc.getPublishMonth());
    Integer publishDay = IrisNumberUtils.toInteger(doc.getPublishDay());
    Integer citedTimes = doc.getCiteTimes();
    String zhTitle = XmlUtil.trimAllHtml(doc.getZhTitle());
    String enTitle = XmlUtil.trimAllHtml(doc.getEnTitle());
    String fullTextField = doc.getFullTextId();
    Integer fullTextNodeId = doc.getFullTextNodeId();
    String fullTextFileExt = doc.getFullTextFileExt();
    String impactFactors = doc.getImpactFactors();
    Long simpleVersion = new Date().getTime();
    String authorNames = XmlUtil.trimAllHtml(doc.getAuthorNames());
    String briefDesc = XmlUtil.trimAllHtml(doc.getBriefDesc());
    String briefDescEN = XmlUtil.trimAllHtml(doc.getBriefDescEN());

    // 设置对象值
    pubSimple.setPubId(pubId);
    pubSimple.setArticleType(articleType);
    pubSimple.setSourceDbId(sourceDbId);
    pubSimple.setPubType(pubType);
    pubSimple.setPublishYear(publishYear);
    pubSimple.setPublishMonth(publishMonth);
    pubSimple.setPublishDay(publishDay);
    pubSimple.setCitedTimes(citedTimes);
    pubSimple.setZhTitle(zhTitle);
    pubSimple.setEnTitle(enTitle);
    pubSimple.setFullTextField(fullTextField);
    pubSimple.setFullTextNodeId(fullTextNodeId);
    pubSimple.setFullTextFileExt(fullTextFileExt);
    pubSimple.setSimpleVersion(simpleVersion);
    pubSimple.setImpactFactors(impactFactors);
    pubSimple.setAuthorNames(authorNames);
    pubSimple.setBriefDesc(briefDesc);
    pubSimple.setBriefDescEn(briefDescEN);
    return pubSimple;
  }

  /**
   * 从XML中给PubDataStore赋值
   *
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param doc
   * @param pubDataStore
   * @return
   */
  public static PubDataStore getPubDataStoreFromXml(String xml, PubDataStore pubDataStore) {
    Assert.notNull(xml);
    Assert.notNull(pubDataStore);
    pubDataStore.setData(xml);
    return pubDataStore;
  }

  /**
   * 这里放的是成果录入和编辑时都需要保存更新的字段.
   * 
   * @param doc
   * @param context
   * @param now
   * @param pub
   */
  public static void wrapPublicationSaveField(PubXmlDocument doc, PubXmlProcessContext context, Date now,
      Publication pub) {

    pub.setBriefDesc(
        StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc"), 0, 500));

    pub.setBriefDescEn(
        StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc_en"), 0, 500));

    pub.setFulltextFileid(doc.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "file_id"));
    Integer fulltextNodeId =
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "node_id"));
    pub.setFulltextNodeId(fulltextNodeId);
    pub.setFulltextExt(null);
    if (pub.getFulltextFileid() != null) {
      String fileExt = doc.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "file_ext");
      if (StringUtils.isNotBlank(fileExt)) {
        pub.setFulltextExt(StringUtils.substring(fileExt, 0, 30));
      }
    }
    pub.setFulltextUrl(
        StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "fulltext_url"), 0, 3000));
    pub.setIsiId(doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "isi_id"));
    pub.setSourceDbId(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "source_db_id")));
    pub.setDataValidate(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "is_valid")));
    pub.setRecordFrom(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "record_from")));
    pub.setEnTitle(
        StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_title"), 0, 250));
    pub.setEnTitleHash(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_title_hash")));
    pub.setFingerPrint(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "finger_print")));
    pub.setImpactFactors(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "impact_factors"));
    pub.setJid(IrisNumberUtils.createLong(doc.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jid")));
    pub.setJnlId(IrisNumberUtils.createLong(doc.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jnl_id")));
    pub.setPublishDay(
        IrisNumberUtils.monthDayToInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_day")));
    pub.setPublishMonth(
        IrisNumberUtils.monthDayToInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_month")));
    pub.setPublishYear(
        IrisNumberUtils.monthDayToInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_year")));
    pub.setRegionId(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "country_id")));

    // 引用次数
    Integer citeTimes =
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cite_times"));
    // if (citeTimes != null) {
    pub.setCitedTimes(citeTimes);
    pub.setCitedDate(now);
    // String citationIndex =
    // doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH,
    // "cited_list");
    // if (StringUtils.isNotBlank(citationIndex)) {
    // citationIndex = StringUtils.substring(citationIndex, 0,
    // 1).toUpperCase()
    // + StringUtils.substring(citationIndex, 1);
    // }
    // pub.setCitedList(citationIndex);
    // }
    pub.setCitedUrl(
        StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cited_url"), 0, 3000));
    pub.setZhTitle(
        StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title"), 0, 250));
    pub.setZhTitleHash(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title_hash")));
    pub.setUpdateDate(context.getPubSimple() == null ? now : context.getPubSimple().getUpdateDate());
    pub.setUpdatePsnId(context.getCurrentUserId());

    pub.setGroupId(context.getGroupId());

    // liqinghua添加补充信息
    pub.setStartPage(
        StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "start_page"), 0, 50));
    pub.setEndPage(
        StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "end_page"), 0, 50));
    pub.setIssue(StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "issue"), 0, 20));
    pub.setOldPubId(IrisNumberUtils.createLong(doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "from_pub_id")));
    String isbn = StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "isbn"), 0, 40);
    if (StringUtils.isBlank(isbn)) {
      isbn = StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "isbn"), 0, 40);
    }
    pub.setIsbn(isbn);
    pub.setVolume(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "volume"));
    pub.setIssue(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "issue"));
    pub.setDoi(StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "doi"), 0, 100));
    pub.setArticleNo(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "article_number"));
    pub.setAuthorNames(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "author_names"));
    pub.setTypeId(IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUB_TYPE_XPATH, "id")));
    pub.setArticleType(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUB_TYPE_XPATH, "article_type")));
  }
}
