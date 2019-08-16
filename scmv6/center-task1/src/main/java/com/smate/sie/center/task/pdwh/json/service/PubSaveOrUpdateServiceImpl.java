package com.smate.sie.center.task.pdwh.json.service;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.v8pub.pdwh.po.PubPdwhPO;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.consts.SieConstRegion;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.service.consts.SieConstRegionService;
import com.smate.core.base.utils.string.IrisNumberUtils;
import com.smate.sie.center.task.model.PubDupParam;
import com.smate.sie.center.task.model.SiePubDupFields;
import com.smate.sie.center.task.pdwh.service.PubAwardIssueInsService;
import com.smate.sie.center.task.pdwh.service.PubBookPublisherService;
import com.smate.sie.center.task.pdwh.service.PubKeyWordsService;
import com.smate.sie.center.task.pdwh.service.PubListService;
import com.smate.sie.center.task.pdwh.service.PubMeetingNameService;
import com.smate.sie.center.task.pdwh.service.PubMeetingOrganizerService;
import com.smate.sie.center.task.pdwh.service.PubThesisOrgService;
import com.smate.sie.center.task.pdwh.service.SiePubDupFieldsService;
import com.smate.sie.core.base.utils.dao.pub.SiePublicationDao;
import com.smate.sie.core.base.utils.date.DateUtils;
import com.smate.sie.core.base.utils.model.pub.SiePublication;
import com.smate.sie.core.base.utils.pub.dom.AwardsInfoBean;
import com.smate.sie.core.base.utils.pub.dom.BookInfoBean;
import com.smate.sie.core.base.utils.pub.dom.ConferencePaperBean;
import com.smate.sie.core.base.utils.pub.dom.JournalInfoBean;
import com.smate.sie.core.base.utils.pub.dom.ThesisInfoBean;
import com.smate.sie.core.base.utils.pub.service.PubJsonDTO;
import com.smate.sie.core.base.utils.pub.service.PubJsonPOService;

/**
 * 成果保存服务类
 * 
 * @author ZSJ
 *
 * @date 2019年2月19日
 */
@Service("pubSaveOrUpdateService")
@Transactional(rollbackFor = Exception.class)
public class PubSaveOrUpdateServiceImpl implements PubSaveOrUpdateService {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  @Autowired
  private PubMeetingNameService pubMeetingNameService;
  @Autowired
  private PubMeetingOrganizerService pubMeetingOrganizerService;
  @Autowired
  private PubAwardIssueInsService pubAwardIssueInsService;
  @Autowired
  private PubBookPublisherService pubBookPublisherService;
  @Autowired
  private PubThesisOrgService pubThesisOrgService;
  @Autowired
  private SieConstRegionService sieConstRegionService;
  @Autowired
  private SiePublicationDao publicationDao;
  @Autowired
  private PublicationMemberService publicationMemberService;
  @Autowired
  private PubListService pubListService;
  @Autowired
  private SiePubDupFieldsService pubDupFieldsService;
  @Autowired
  private PubKeyWordsService pubKeyWordsService;
  @Autowired
  private PubJsonPOService pubJsonPOService;

  /**
   * 新增
   * 
   * @throws SysServiceException
   */
  @Override
  public SiePublication createPublication(PubJsonDTO pubJson, PubPdwhPO pdwhPublications) throws ServiceException {
    try {
      Date now = new Date();
      SiePublication pub = new SiePublication();
      pub.setInsId(pubJson.insId);
      pub.setCreatePsnId(-1L);
      pub.setPubType(pubJson.pubTypeCode);
      pub.setCreateDate(now);
      pub.setStatus("0");
      wrapPubField(pubJson, pub);
      if (pubJson.pubTypeCode == 3) {
        confPaperField(pubJson, pub, pdwhPublications.getPubId());
      } else if (pubJson.pubTypeCode == 2) {
        bookField(pubJson, pub);
      } else if (pubJson.pubTypeCode == 1) {
        awardField(pubJson, pub, pdwhPublications.getPubId());
      } else if (pubJson.pubTypeCode == 10) {
        bookChapterField(pubJson, pub, pdwhPublications.getPubId());
      } else if (pubJson.pubTypeCode == 8) {
        thesisField(pubJson, pub, pdwhPublications.getPubId());
      } else if (pubJson.pubTypeCode == 4) {
        journalField(pubJson, pub);
      }
      publicationDao.save(pub);
      pubJson.pubId = pub.getPubId();
      // 建立成果、成员关系数据
      publicationMemberService.savePubMember(pubJson);
      pub.setAuthorNames(pubJson.authorNames);
      publicationDao.save(pub);

      // 解析收录情况
      pubListService.prasePubList(pubJson);

      // }
      /**
       * 清除数据库的pub_error_fields,遍历xml中的pub_errors/error,把错误的字段名保存进pub_error_fields 此逻辑目前去掉，业务上没有用到。
       */
      // savePubErrorFields(doc, context);

      // 保存成果查重字段
      pubDupFieldsService.savePubDupFields(buildPubDupFields(pubJson, pub), pubJson.pubTypeCode, pubJson.pubId,
          pubJson.insId, SiePubDupFields.NORMAL_STATUS);

      // 成果关键词拆分保存
      pubKeyWordsService.savePubKeywords(pub.getPubId(), pubJson.insId, pubJson.keywords);
      pubJsonPOService.savePubJson(pubJson);
      return pub;
    } catch (Exception e) {
      logger.error("savePubCreate保存新添加成果出错, pubId=" + pubJson.pubId, e);
      StackTraceElement stackTraceElement = e.getStackTrace()[0];
      int lineNum = stackTraceElement.getLineNumber();
      String errMsg = "异常类文件名： " + stackTraceElement.getFileName() + "异常方法名： " + stackTraceElement.getMethodName()
          + "，错误行号： " + lineNum;
      throw new ServiceException(
          "createPublication保存新添加成果出错, pubId=" + pubJson.pubId + "," + e.toString() + " --> " + errMsg, e);
    }
  }

  @Override
  public SiePublication updatePublication(PubJsonDTO pubJson, PubPdwhPO pdwhPublications) throws ServiceException {
    try {
      SiePublication pub = publicationDao.get(pubJson.pubId);
      if (pub == null) {
        throw new ServiceException("数据库中没有该条记录，无法完成更新操作，pubId = " + pubJson.pubId);
      }
      // 表示数据已被用户在页面编辑过，该任务不再做更新。
      if (pub.getIsValid() == 1) {
        return pub;
      }
      pub.setInsId(pubJson.insId);
      pub.setPubType(pubJson.pubTypeCode);
      pub.setStatus("0");
      wrapPubField(pubJson, pub);
      if (pubJson.pubTypeCode == 3) {
        confPaperField(pubJson, pub, pdwhPublications.getPubId());
      } else if (pubJson.pubTypeCode == 2) {
        bookField(pubJson, pub);
      } else if (pubJson.pubTypeCode == 1) {
        awardField(pubJson, pub, pdwhPublications.getPubId());
      } else if (pubJson.pubTypeCode == 10) {
        bookChapterField(pubJson, pub, pdwhPublications.getPubId());
      } else if (pubJson.pubTypeCode == 8) {
        thesisField(pubJson, pub, pdwhPublications.getPubId());
      } else if (pubJson.pubTypeCode == 4) {
        journalField(pubJson, pub);
      }
      if (pub.getUpdateDate().before(pdwhPublications.getGmtModified())) {
        pub.setUpdateDate(pdwhPublications.getGmtModified());
      }
      pub.setAuthorNames(pubJson.authorNames);
      pub.setPdwhImportStatus(true);
      publicationDao.save(pub);
      // 建立成果和成员关系
      publicationMemberService.savePubMember(pubJson);

      // 解析收录情况
      pubListService.prasePubList(pubJson);

      // }
      /**
       * 清除数据库的pub_error_fields,遍历xml中的pub_errors/error,把错误的字段名保存进pub_error_fields 此逻辑目前去掉，业务上没有用到。
       */
      // savePubErrorFields(doc, context);

      // 保存成果查重字段
      pubDupFieldsService.savePubDupFields(buildPubDupFields(pubJson, pub), pubJson.pubTypeCode, pubJson.pubId,
          pubJson.insId, SiePubDupFields.NORMAL_STATUS);

      // 成果关键词拆分保存
      pubKeyWordsService.savePubKeywords(pub.getPubId(), pubJson.insId, pubJson.keywords);
      pubJsonPOService.savePubJson(pubJson);
      return pub;
    } catch (Exception e) {
      logger.error("updatePublication更新成果出错 ， pubId = " + pubJson.pubId, e);
      StackTraceElement stackTraceElement = e.getStackTrace()[0];
      int lineNum = stackTraceElement.getLineNumber();
      String errMsg = "异常类文件名： " + stackTraceElement.getFileName() + "， 异常方法名： " + stackTraceElement.getMethodName()
          + "，错误行号： " + lineNum;
      throw new ServiceException(
          "updatePublication更新成果出错， pubId = " + pubJson.pubId + ", " + e.toString() + " --> " + errMsg, e);
    }
  }

  /**
   * 成果录入，更新公共属性处理.
   * 
   * @param doc
   * @param context
   * @param now
   * @param pub
   */
  private void wrapPubField(PubJsonDTO pubJson, SiePublication pub) throws ServiceException {
    pub.setPublishDay(StringUtils.isNotBlank(pubJson.publishDay) ? NumberUtils.toInt(pubJson.publishDay) : null);
    pub.setPublishMonth(StringUtils.isNotBlank(pubJson.publishMonth) ? NumberUtils.toInt(pubJson.publishMonth) : null);
    pub.setPublishYear(StringUtils.isNotBlank(pubJson.publishYear) ? NumberUtils.toInt(pubJson.publishYear) : null);
    pub.setZhTitle(StringUtils.substring(pubJson.title, 0, 250));
    // pub.setZhTitleHash(PublicationHash.cleanTitleHash(pub.getTitle()));
    pub.setEnTitle(StringUtils.substring(pubJson.title, 0, 250));
    // pub.setEnTitleHash(PublicationHash.cleanTitleHash(pub.getTitle()));
    pub.setBriefDesc(StringUtils.substring(pubJson.briefDesc, 0, 500));
    pub.setBriefDescEn(StringUtils.substring(pubJson.briefDesc, 0, 500));
    pub.setDateFrom(pubJson.dataFrom);
    Integer dbId = pubJson.srcDbId;
    if (dbId != null) {
      pub.setDbId(Long.valueOf(dbId));
    }
    // 表单没该字段
    pub.setAddress("");
    pub.setDoi(pubJson.doi);// 转义
    pub.setFulltextUrl(pubJson.srcFulltextUrl);
    pub.setIsPublic(pubJson.isPublicCode);
    pub.setUpdateDate(new Date());
    pub.setUpdatePsnId(-1L);
    // pub.setFulltextFileid(ObjectUtils.toString(pubJson.fulltextId));
    // 页面编辑过的数据 IsValid字段的值赋予1，表示基准库同步时不做更新
    if (pubJson.isEdit) {
      pub.setIsValid(1);
    } else {
      pub.setIsValid(0);
    }
    Integer citations = pubJson.citations;
    if (citations != null && citations > 0) {
      pub.setIsiCited(Long.valueOf(citations));
      pub.setIsiCitedUpdate(DateUtils.parseStringToDate(pubJson.citationsUpdateTime));
    } else {
      pub.setIsiCited(null);
      pub.setIsiCitedUpdate(null);
    }
    pub.setAuthorNames(pubJson.authorNames);
  }

  /**
   * 会议论文字段保存
   * 
   * @param pubJson
   * @param pub
   * @throws ServiceException
   * @throws SysServiceException
   * @throws DaoException
   */
  private void confPaperField(PubJsonDTO pubJson, SiePublication pub, Long pdwhPubId)
      throws ServiceException, SysServiceException, DaoException {
    String json = JacksonUtils.jsonObjectSerializer(pubJson.pubTypeInfoBean);
    ConferencePaperBean conference = JacksonUtils.jsonObject(json, ConferencePaperBean.class);
    String country = conference.getCountry();
    if (StringUtils.isNotBlank(country)) {
      SieConstRegion region = null;
      try {
        region = sieConstRegionService.getRegionByName(country);
      } catch (Exception e) {
        pub.setRegionId(null);
        pub.setRegionName(country);
        logger.error("读取国家/地区错误", e);
      }
      if (region != null) {
        pub.setRegionId(region.getId());
        pub.setRegionName(country);
      } else {
        pub.setRegionId(null);
        pub.setRegionName(country);
        logger.warn("找不到国家/地区, 参数=" + country);
      }
    } else {
      pub.setRegionId(null);
      pub.setRegionName(country);
    }
    String meetingName = StringUtils.substring(conference.getName(), 0, 100);
    String confOrganizer = StringUtils.substring(conference.getOrganizer(), 0, 100);
    // 保存会议名称自动提示，若不存在则新增
    pubMeetingNameService.savePubMeetingName(meetingName, pdwhPubId);
    // 保存会议组织者自动提示，若不存在则新增
    pubMeetingOrganizerService.savePubMeetingOrganizer(confOrganizer, pdwhPubId);
  }

  private void bookField(PubJsonDTO pubJson, SiePublication pub) throws ServiceException {
    String json = JacksonUtils.jsonObjectSerializer(pubJson.pubTypeInfoBean);
    BookInfoBean book = JacksonUtils.jsonObject(json, BookInfoBean.class);
    pub.setArticleNo(null);
    /*
     * pub.setWriteNum(book.getTotalWords().longValue());
     * pub.setBookPublisher(StringUtils.substring(book.getPublisher(), 0, 50));// ROL-4480转义
     * pub.setBookLanguage(book.getLanguageCode());
     */
    pub.setPubType2(IrisNumberUtils.createInteger(book.getTypeCode()));
    /* pub.setIsbn(StringUtils.substring(book.getISBN(), 0, 40)); */
    String status = book.getPublishStatusCode();
    pub.setPubStatus(status);
    // 待出版，表中相关日期字段不存值（excel导入可能会出现这情况）
    if ("0".equals(status)) {
      pub.setPublishDay(null);
      pub.setPublishMonth(null);
      pub.setPublishYear(null);
    }
  }

  private void awardField(PubJsonDTO pubJson, SiePublication pub, Long pdwhPubId)
      throws ServiceException, SysServiceException {
    String json = JacksonUtils.jsonObjectSerializer(pubJson.pubTypeInfoBean);
    AwardsInfoBean award = JacksonUtils.jsonObject(json, AwardsInfoBean.class);
    String issueInsName = StringUtils.substring(award.getIssuingAuthority(), 0, 100);
    pubAwardIssueInsService.savePubAwardIssueIns(issueInsName, pdwhPubId);
  }

  private void bookChapterField(PubJsonDTO pubJson, SiePublication pub, Long pdwhPubId)
      throws ServiceException, SysServiceException {
    String json = JacksonUtils.jsonObjectSerializer(pubJson.pubTypeInfoBean);
    BookInfoBean book = JacksonUtils.jsonObject(json, BookInfoBean.class);
    /*
     * pub.setBookPublisher(StringUtils.substring(book.getPublisher(), 0, 50));
     * pub.setIsbn(StringUtils.substring(book.getISBN(), 0, 40));
     */
    String publisher = StringUtils.substring(book.getPublisher(), 0, 100);
    pubBookPublisherService.savePubBookPublisher(publisher, pdwhPubId);
  }

  private void thesisField(PubJsonDTO pubJson, SiePublication pub, Long pdwhPubId)
      throws ServiceException, SysServiceException {
    String json = JacksonUtils.jsonObjectSerializer(pubJson.pubTypeInfoBean);
    ThesisInfoBean thesis = JacksonUtils.jsonObject(json, ThesisInfoBean.class);
    String issueOrg = StringUtils.substring(thesis.getIssuingAuthority(), 0, 100);
    pubThesisOrgService.savePubThesisOrg(issueOrg, pdwhPubId);

  }

  private void journalField(PubJsonDTO pubJson, SiePublication pub) throws ServiceException {
    String json = JacksonUtils.jsonObjectSerializer(pubJson.pubTypeInfoBean);
    JournalInfoBean journal = JacksonUtils.jsonObject(json, JournalInfoBean.class);
    pub.setJid(journal.getJid());
    pub.setStartPage(StringUtils.substring(journal.getStartPage(), 0, 40));
    pub.setEndPage(StringUtils.substring(journal.getEndPage(), 0, 40));
    pub.setVolume(StringUtils.substring(journal.getVolumeNo(), 0, 18));
    pub.setIssue(StringUtils.substring(journal.getIssue(), 0, 18));
    pub.setPubStatus(StringUtils.substring(journal.getPublishStatusCode(), 0, 8));
  }

  public PubDupParam buildPubDupFields(PubJsonDTO pubJson, SiePublication pub) throws ServiceException {
    String articleNo = StringUtils.substring(pub.getArticleNo(), 0, 50);
    Long jid = pub.getJid();
    String volume = StringUtils.substring(pub.getVolume(), 0, 20);
    String issue = StringUtils.substring(pub.getIssue(), 0, 20);
    String startPage = pub.getStartPage();
    String isbn = "";// StringUtils.substring(pub.getIsbn(), 0, 40);
    String zhTitle = pub.getTitle();
    String enTitle = zhTitle;
    String doi = StringUtils.substring(pub.getDoi(), 0, 100);
    // 2019-2-20 注释 ZSJ
    // String isiId =
    // doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "isi_id");
    // String eiId = doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH,
    // "ei_id");
    // String spsId =
    // doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "sps_id");
    Integer pubType = pub.getPubType();
    String confName = "";
    String issn = "";
    String jname = "";
    if (pubType == 3) {
      String json = JacksonUtils.jsonObjectSerializer(pubJson.pubTypeInfoBean);
      ConferencePaperBean conference = JacksonUtils.jsonObject(json, ConferencePaperBean.class);
      confName = conference.getName();
    } else if (pubType == 4) {
      String json = JacksonUtils.jsonObjectSerializer(pubJson.pubTypeInfoBean);
      JournalInfoBean journal = JacksonUtils.jsonObject(json, JournalInfoBean.class);
      issn = journal.getISSN();
      jname = journal.getName();
    } else if (pubType == 2 || pubType == 10) {
      String json = JacksonUtils.jsonObjectSerializer(pubJson.pubTypeInfoBean);
      BookInfoBean book = JacksonUtils.jsonObject(json, BookInfoBean.class);
      isbn = StringUtils.substring(book.getISBN(), 0, 40);
    }
    // 作者名
    String auNames = pub.getAuthorNames();
    Integer pubYear = pub.getPublishYear();
    Long sourceDbId = pub.getDbId();
    PubDupParam param = new PubDupParam();
    param.setArticleNo(articleNo);
    param.setDoi(doi);
    param.setEnTitle(enTitle);
    param.setIsbn(isbn);
    param.setIsiId(null);
    param.setEiId(null);
    param.setSpsId(null);
    param.setIssue(issue);
    param.setJid(jid);
    param.setPubType(pubType);
    param.setPubYear(pubYear);
    param.setSourceDbId(sourceDbId);
    param.setStartPage(startPage);
    param.setVolume(volume);
    param.setZhTitle(zhTitle);
    param.setAuthorNames(auNames);
    param.setConfName(confName);
    param.setIssn(issn);
    param.setJname(jname);
    return param;
  }

}
