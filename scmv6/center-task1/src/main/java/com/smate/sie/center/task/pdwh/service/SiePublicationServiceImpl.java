package com.smate.sie.center.task.pdwh.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.exception.DaoException;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.date.DateUtils;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.model.rol.SieInsUnit;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.service.insunit.SieInsUnitService;
import com.smate.core.base.utils.string.IrisNumberUtils;
import com.smate.sie.center.task.model.PubDupParam;
import com.smate.sie.center.task.model.PubList;
import com.smate.sie.center.task.model.SiePubDupFields;
import com.smate.sie.center.task.pdwh.task.PubXmlDocument;
import com.smate.sie.center.task.pdwh.task.PubXmlProcessContext;
import com.smate.sie.center.task.pub.enums.PublicationOperationEnum;
import com.smate.sie.core.base.utils.dao.psn.SieInsPersonDao;
import com.smate.sie.core.base.utils.dao.pub.PubXmlDao;
import com.smate.sie.core.base.utils.dao.pub.SiePubMemberDao;
import com.smate.sie.core.base.utils.dao.pub.SiePublicationDao;
import com.smate.sie.core.base.utils.model.psn.SieInsPerson;
import com.smate.sie.core.base.utils.model.pub.SiePubErrorField;
import com.smate.sie.core.base.utils.model.pub.SiePubMember;
import com.smate.sie.core.base.utils.model.pub.SiePubXml;
import com.smate.sie.core.base.utils.model.pub.SiePublication;

import net.sf.json.JSONArray;

/**
 * 单位成果管理service.
 * 
 * @author jszhou
 */
@Service("siePublicationService")
@Transactional(rollbackFor = Exception.class)
public class SiePublicationServiceImpl implements SiePublicationService {

  /**
   * 
   */
  private static final long serialVersionUID = -8220619691038261257L;

  /**
   * 
   */
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private SiePublicationDao publicationDao;
  @Autowired
  private SiePublicationLogService siePublicationLogService;
  @Autowired
  private SiePubMemberDao pubMemberDao;
  @Autowired
  private PubXmlDao siePubXmlDao;
  @Autowired
  private PubKeyWordsService pubKeyWordsService;
  @Autowired
  private PubListService pubListService;
  @Autowired
  private SiePubDupFieldsService pubDupFieldsService;
  @Autowired
  private SieInsPersonDao sieInsPersonDao;
  @Autowired
  private SieInsUnitService sieInsUnitService;
  @Autowired
  private PubMeetingNameService pubMeetingNameService;
  @Autowired
  private PubMeetingOrganizerService pubMeetingOrganizerService;
  @Autowired
  private PubBookPublisherService pubBookPublisherService;
  @Autowired
  private PubAwardIssueInsService pubAwardIssueInsService;
  @Autowired
  private PubThesisOrgService pubThesisOrgService;

  /**
   * 获取成果实体，先从自定义缓存读取，如果不存在直接查询数据库.
   * 
   * @param id
   * @return
   * @throws SysServiceException
   */
  public SiePublication getPublicationById(Long id) throws SysServiceException {
    try {
      return this.publicationDao.get(id);
    } catch (Exception e) {
      logger.error("getPublicationById获取成果实体id=" + id, e);
      throw new SysServiceException(e);
    }
  }

  @Override
  public Integer getPubStatusById(Long id) throws SysServiceException {
    try {
      return null;
    } catch (Exception e) {
      logger.error("获取成果状态id=" + id, e);
      throw new SysServiceException("获取成果状态id=" + id, e);
    }
  }

  @Override
  public List<SiePublication> getPublicationById(List<Long> ids, Long insId) throws SysServiceException {
    try {
      return null;
    } catch (Exception e) {
      logger.error("批量获取单位成果", e);
      throw new SysServiceException(e);
    }
  }

  /**
   * 保存新添加成果.
   * 
   * @param pub
   * @throws Exception
   */
  public SiePublication createPublication(PubXmlDocument doc, PubXmlProcessContext context) throws Exception {
    try {
      Date now = new Date();
      SiePublication pub = new SiePublication();
      pub.setPubId(context.getCurrentPubId());
      pub.setInsId(context.getCurrentInsId());
      pub.setCreatePsnId(context.getCurrentUserId());
      pub.setPubType(context.getPubTypeId());
      pub.setCreateDate(now);
      pub.setStatus("0");
      wrapPubField(doc, context, now, pub);
      if (doc.isConfPaper()) {
        confPaperField(doc, context, pub);
      } else if (doc.isBook()) {
        bookField(doc, context, pub);
      } else if (doc.isAward()) {
        awardField(doc, context, pub);
      } else if (doc.isBookChapter()) {
        bookChapterField(doc, context, pub);
      } else if (doc.isThesis()) {
        thesisField(doc, context, pub);
      }
      publicationDao.save(pub);
      doc.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "pub_id", pub.getPubId().toString());
      context.setCurrentPubId(pub.getPubId());
      // 建立成果、成员关系数据
      this.savePubMember(doc, context);
      pub.setAuthorNames(StringUtils
          .substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "original_author_names"), 0, 200));
      publicationDao.save(pub);
      PubList publist = null;
      if (context.isImport() || context.isSyncPub()) {
        // 成果导入时pubList拆分（包括原始引用情况）
        publist = this.pubListService.praseSourcePubList(doc);
      } else {
        // 解析收录情况
        // publist = this.pubListService.prasePubList(doc);
      }
      // 清除数据库的pub_error_fields,遍历xml中的pub_errors/error,把错误的字段名保存进pub_error_fields
      savePubErrorFields(doc, context);
      // 保存成果查重字段
      this.parsePubDupFields(doc, pub);
      // 存储XML数据
      this.savePubXml(doc, context);
      // 保存成果项目信息
      String zhKeywords = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_keywords");
      String enKeywords = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_keywords");
      // 成果关键词拆分保存
      // this.pubKeyWordsService.savePubKeywords(pub.getPubId(), context.getCurrentInsId(), zhKeywords,
      // enKeywords);
      return pub;
    } catch (Exception e) {
      logger.error("savePubCreate保存新添加成果出错 ", e);
      throw new SysServiceException("createPublication保存成果出错");
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
  private void wrapPubField(PubXmlDocument doc, PubXmlProcessContext context, Date now, SiePublication pub)
      throws SysServiceException {
    pub.setJid(IrisNumberUtils.createLong(doc.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jid")));

    pub.setPublishDay(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_day")));
    pub.setPublishMonth(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_month")));
    pub.setPublishYear(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_year")));
    // 不区分中英文，两个字段都存值.
    String zhTitle = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title");
    pub.setZhTitle(StringUtils.substring(zhTitle, 0, 250));
    pub.setEnTitle(StringUtils.substring(zhTitle, 0, 250));
    Long zhTitleHash = PubHashUtils.cleanTitleHash(zhTitle);
    /*
     * if (zhTitleHash != null) { pub.setZhTitleHash(zhTitleHash.intValue());
     * pub.setEnTitleHash(zhTitleHash.intValue()); }
     */

    pub.setBriefDesc(
        StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc"), 0, 500));
    pub.setRegionId(
        IrisNumberUtils.createLong(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "country_id")));
    pub.setRegionName(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "country_name"));
    pub.setBriefDescEn(
        StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc_en"), 0, 500));
    pub.setDateFrom(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "record_from")));
    pub.setDbId(IrisNumberUtils.createLong(doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "source_db_id")));

    // 补充信息
    pub.setStartPage(
        StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "start_page"), 0, 50));
    pub.setEndPage(
        StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "end_page"), 0, 50));
    pub.setVolume(StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "volume"), 0, 20));
    pub.setPubStatus(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_state"));
    pub.setPubType2(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "pub_type2_code")));
    pub.setIssue(StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "issue"), 0, 20));
    pub.setDoi(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "doi"));
    pub.setUpdateDate(now);
    pub.setUpdatePsnId(context.getCurrentUserId());
    pub.setIsValid(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "is_valid")));
    String articleNo =
        StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "article_number"), 0, 100);
    pub.setArticleNo(articleNo);
    pub.setIsPublic(1);// 默认公开
    pub.setFulltextUrl(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "fulltext_url"));
    pub.setIsiCited(
        IrisNumberUtils.createLong(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "isi_cited")));
    pub.setIsiCitedUpdate(
        DateUtils.parseStringToDate(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "isi_cited_update")));
  }

  @Override
  public SiePubDupFields parsePubDupFields(PubXmlDocument doc, SiePublication pub) throws SysServiceException {
    String articleNo =
        StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "article_number"), 0, 100);
    Long jid = IrisNumberUtils.createLong(doc.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jid"));
    String volume = StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "volume"), 0, 20);
    String issue = StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "issue"), 0, 20);
    String startPage =
        StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "start_page"), 0, 50);
    String isbn = StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "isbn"), 0, 40);
    if (StringUtils.isBlank(isbn)) {
      isbn = StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "isbn"), 0, 40);
    }
    String zhTitle = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title");
    String doi = StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "doi"), 0, 100);
    String isiId = doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "isi_id");
    String eiId = doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "ei_id");
    String spsId = doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "sps_id");
    String confName = doc.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "conf_name");
    String issn = doc.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jissn");
    String jname = doc.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jname");
    if (StringUtils.isBlank(issn)) {
      issn = StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "issn"), 0, 40);
    }
    // 作者名
    String auNames = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "original_author_names");
    Long ownerId = pub.getInsId();
    Long pubId = pub.getPubId();
    Integer pubYear = pub.getPublishYear();
    Long sourceDbId = pub.getDbId();
    Integer pubType = pub.getPubType();
    PubDupParam param = new PubDupParam();
    param.setArticleNo(articleNo);
    param.setDoi(doi);
    param.setEnTitle(zhTitle);
    param.setIsbn(isbn);
    param.setIsiId(isiId);
    param.setEiId(eiId);
    param.setSpsId(spsId);
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
    Integer pubDupStatus = SiePubDupFields.NORMAL_STATUS;
    return this.pubDupFieldsService.savePubDupFields(param, pubType, pubId, ownerId, pubDupStatus);
  }

  public SiePubXml savePubXml(PubXmlDocument doc, PubXmlProcessContext context) {
    // 存储XML数据
    String xml = doc.getXmlString();
    Long pubId = context.getCurrentPubId();
    SiePubXml xmlData = siePubXmlDao.get(pubId);
    if (xmlData == null) {
      xmlData = new SiePubXml();
      xmlData.setPubId(pubId);
    }
    xmlData.setPubXml(xml);
    this.siePubXmlDao.save(xmlData);
    return xmlData;
  }

  /**
   * 会议论文字段保存
   * 
   * @param doc
   * @param context
   * @param pub
   * @throws SysServiceException
   * @throws DaoException
   */
  @SuppressWarnings("deprecation")
  private void confPaperField(PubXmlDocument doc, PubXmlProcessContext context, SiePublication pub)
      throws SysServiceException, DaoException {
    String meetingName = StringUtils.substring(
        StringEscapeUtils.escapeHtml4(doc.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "conf_name")), 0,
        100);
    String confOrganizer = StringUtils.substring(
        StringEscapeUtils.escapeHtml4(doc.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "conf_organizer")),
        0, 100);
    try {
      // 保存会议名称自动提示，若不存在则新增
      pubMeetingNameService.savePubMeetingName(meetingName, context.getPdwhId());
      // 保存会议组织者自动提示，若不存在则新增
      pubMeetingOrganizerService.savePubMeetingOrganizer(confOrganizer, context.getPdwhId());
    } catch (Exception e) {
      throw new SysServiceException("基准库成果ID:" + context.getPdwhId() + "，保存会议名称或者会议组织者出错");
    }
  }

  @SuppressWarnings("deprecation")
  private void bookField(PubXmlDocument doc, PubXmlProcessContext context, SiePublication pub)
      throws SysServiceException {
    String isbn = StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "isbn"), 0, 40);
    if (StringUtils.isBlank(isbn)) {
      isbn = StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "isbn"), 0, 40);
    }
    /*
     * pub.setIsbn(isbn);
     * pub.setWriteNum(IrisNumberUtils.createLong(doc.getXmlNodeAttribute(PubXmlConstants.
     * PUB_BOOK_XPATH, "total_words"))); pub.setBookPublisher(StringUtils.substring(
     * StringEscapeUtils.escapeHtml4(doc.getXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH,
     * "publisher")), 0, 50));
     * pub.setBookLanguage(doc.getXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "language"));
     */
    pub.setPubType2(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "book_type")));
  }

  @SuppressWarnings("deprecation")
  private void awardField(PubXmlDocument doc, PubXmlProcessContext context, SiePublication pub)
      throws SysServiceException {
    String issueInsName = StringUtils.substring(
        StringEscapeUtils.escapeHtml4(doc.getXmlNodeAttribute(PubXmlConstants.PUB_AWARD_XPATH, "issue_ins_name")), 0,
        100);
    try {
      pubAwardIssueInsService.savePubAwardIssueIns(issueInsName, context.getPdwhId());
    } catch (Exception e) {
      throw new SysServiceException("基准库成果ID:" + context.getPdwhId() + "，savePubBookPublisher保存出版社出错");
    }
  }

  @SuppressWarnings("deprecation")
  private void bookChapterField(PubXmlDocument doc, PubXmlProcessContext context, SiePublication pub)
      throws SysServiceException {
    String isbn = StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "isbn"), 0, 40);
    if (StringUtils.isBlank(isbn)) {
      isbn = StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "isbn"), 0, 40);
    }
    /*
     * pub.setIsbn(isbn); pub.setBookPublisher(StringUtils.substring(
     * StringEscapeUtils.escapeHtml4(doc.getXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH,
     * "publisher")), 0, 50));
     */// ROL-4480转义
    String publisher = StringUtils.substring(
        StringEscapeUtils.escapeHtml4(doc.getXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "publisher")), 0, 100);
    try {
      pubBookPublisherService.savePubBookPublisher(publisher, context.getPdwhId());
    } catch (Exception e) {
      throw new SysServiceException("基准库成果ID:" + context.getPdwhId() + "，savePubBookPublisher保存出版社出错");
    }
  }

  @SuppressWarnings("deprecation")
  private void thesisField(PubXmlDocument doc, PubXmlProcessContext context, SiePublication pub)
      throws SysServiceException {
    String issueOrg = StringUtils.substring(
        StringEscapeUtils.escapeHtml4(doc.getXmlNodeAttribute(PubXmlConstants.PUB_THESIS_XPATH, "issue_org")), 0, 100);
    try {
      pubThesisOrgService.savePubThesisOrg(issueOrg, context.getPdwhId());
    } catch (Exception e) {
      throw new SysServiceException("基准库成果ID:" + context.getPdwhId() + "，savePubThesisOrg保存颁发单位出错");
    }
  }

  @Override
  public SiePublication saveSyncOldPublication(PubXmlDocument doc, PubXmlProcessContext context)
      throws SysServiceException {
    // try {
    //
    // Date updateDate = context.getUpdateDate();
    // if (updateDate == null) {
    // updateDate = new Date();
    // }
    // SiePublication pub = new SiePublication();
    // this.wrapPubField(doc, context, updateDate, pub);
    //
    // pub.setArticleType(context.getArticleType());
    // Integer versionNo = IrisNumberUtils
    // .createInteger(doc.getXmlNodeAttribute(PrjXmlConstants.PRJ_META_XPATH,
    // "version_no"));
    // if (versionNo == null) {
    // versionNo = 1;
    // }
    // pub.setVersionNo(versionNo);
    // pub.setTypeId(context.getPubTypeId());
    // // 单位添加默认为已批准
    // pub.setStatus(PublicationRolStatusEnum.APPROVED);
    //
    // // 认领状态，默认为0
    // pub.setAuthorState(context.getAuthorState());
    // // 源成果ID,V2.6同步使用
    // pub.setOldPubId(context.getFromPubId());
    // pub.setCreatePsnId(context.getCurrentUserId());
    // publicationDao.save(pub);
    //
    // doc.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "pub_id",
    // pub.getId().toString());
    // doc.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "version_no",
    // pub.getVersionNo().toString());
    //
    // context.setCurrentPubId(pub.getId());
    //
    // // 建立成果、成员关系数据
    // Long firstAuthor = savePubMember(doc, context);
    //
    // pub.setAuthorNames(StringUtils.substring(
    // doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH,
    // "brief_author_names"), 0, 200));
    //
    // // 保存第一作者
    // pub.setFirstAuthorPsnId(firstAuthor);
    // publicationDao.save(pub);
    //
    // // 解析收录情况
    // this.publicationListService.prasePubList(doc);
    //
    // //
    // 清除数据库的pub_error_fields,遍历xml中的pub_errors/error,把错误的字段名保存进pub_error_fields
    // savePubErrorFields(doc, context);
    //
    // // 存储XML数据
    // String xml = doc.getXmlString();
    // this.publicationXmlService.save(pub.getId(), xml);
    // // 刷新期刊-开放存储类型
    // if (pub.getJid() != null && pub.getJid() > 0) {
    // this.jnlOATypeRefreshService.saveJnlOATypeRefresh(pub.getJid());
    // }
    //
    // return pub;
    // } catch (SysServiceException e) {
    // logger.error("saveSyncOldPublication保存同步成果出错 ", e);
    // throw new SysServiceException(e);
    // }
    return null;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void syncOldInsPub(Map<String, Object> map) throws SysServiceException {
    // String pubXml = map.get("PUB_XML") == null ? null :
    // map.get("PUB_XML").toString();
    // Integer articleType = map.get("ARTICLE_TYPE") == null ? null
    // : Integer.valueOf(map.get("ARTICLE_TYPE").toString());
    // Integer typeId = map.get("PUB_TYPE") == null ? null :
    // Integer.valueOf(map.get("PUB_TYPE").toString());
    //
    // if (StringUtils.isBlank(pubXml) || articleType == null || typeId ==
    // null)
    // return;
    //
    // Map<String, Object> params = new HashMap<String, Object>();
    // params.put("PUB_XML", pubXml);
    // params.put("ARTICLE_TYPE", articleType);
    // params.put("PUB_TYPE", typeId);
    //
    // // 单位ID列表
    // List<Map<String, Object>> pubInsList = (List<Map<String, Object>>)
    // map.get("pubInsList");
    // // 认领状态authorState
    // Integer authorState = map.get("AUTHOR_ASSIGN_STATE") == null ? 0
    // : Integer.valueOf(map.get("AUTHOR_ASSIGN_STATE").toString());
    // Long pubId = map.get("PUB_ID") == null ? 0 :
    // Long.valueOf(map.get("PUB_ID").toString());
    // Long psnId = map.get("CREATE_PSN_ID") == null ? 0 :
    // Long.valueOf(map.get("CREATE_PSN_ID").toString());
    // Date updateDate = map.get("UPDATE_DATE") == null ? null : (Date)
    // map.get("UPDATE_DATE");
    // params.put("AUTHOR_ASSIGN_STATE", authorState);
    // params.put("PUB_ID", pubId);
    // params.put("CREATE_PSN_ID", psnId);
    // params.put("UPDATE_DATE", updateDate);
    //
    // for (Map<String, Object> mapPubIns : pubInsList) {
    //
    // Integer isOpen = mapPubIns.get("IS_OPEN") == null ? 0
    // : Integer.valueOf(mapPubIns.get("IS_OPEN").toString());
    // Integer confirmResult = mapPubIns.get("CONFIRM_STATUS") == null ? 0
    // : Integer.valueOf(mapPubIns.get("CONFIRM_STATUS").toString());
    // Long insId = mapPubIns.get("INS_ID") == null ? 0 :
    // Long.valueOf(mapPubIns.get("INS_ID").toString());
    // params.put("IS_OPEN", isOpen);
    // params.put("CONFIRM_STATUS", confirmResult);
    // params.put("INS_ID", insId);
    // publicationXmlManager.syncOldPubXml(params);
    // }
  }

  public void sendPubDupCheckQueue(PubXmlProcessContext context) throws SysServiceException {
    // 成果排入查重队列
    // Long pubId = context.getCurrentPubId();
    // Long insId = context.getCurrentInsId();
    // 把pubId放进MQ的查重队列,立刻处理
    // try {
    // logger.debug("把pubId={},insId={}放进MQ的查重队列", pubId, insId);
    // PubDupCheckMessage message = new PubDupCheckMessage(insId, pubId,
    // context.getCurrentUserId());
    // // 后台批量导入，等级设置成最低
    // if
    // (XmlOperationEnum.OfflineImport.equals(context.getCurrentAction())) {
    // message.setPriority(1);
    // }
    // this.pubDupCheckMessageProducer.sendDupCheckMessage(message);
    // } catch (Exception e) {
    // logger.error("sendPubDupCheckQueue发送查重消息错误。 ", e);
    // throw new SysServiceException(e);
    // }
  }

  /**
   * 保存成果编辑内容.
   * 
   * @param pub
   * @throws BatchTaskException
   * @throws DaoException
   * @throws ServcieException
   */
  @Override
  public SiePublication updatePublication(PubXmlDocument doc, PubXmlProcessContext context) throws Exception {
    try {
      Long pubId = context.getCurrentPubId();
      SiePublication pub = publicationDao.get(pubId);
      if (pub != null) {
        // 值为1时不用再更新
        if (pub.getIsValid() == 1) {
          return pub;
        }
        Date now = new Date();
        this.wrapPubField(doc, context, now, pub);
        if (doc.isConfPaper()) {
          confPaperField(doc, context, pub);
        } else if (doc.isBook()) {
          bookField(doc, context, pub);
        } else if (doc.isAward()) {
          awardField(doc, context, pub);
        } else if (doc.isBookChapter()) {
          bookChapterField(doc, context, pub);
        } else if (doc.isThesis()) {
          thesisField(doc, context, pub);
        }
        pub.setPubType(doc.getPubTypeId());
        // 建立成果、成员关系数据
        this.deletePubMember(doc, context);
        Long firstAuthor = savePubMember(doc, context);
        pub.setAuthorNames(StringUtils
            .substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "original_author_names"), 0, 200));
        publicationDao.save(pub);
        PubList publist = null;
        // 解析收录情况
        if (context.isImport() || context.isSyncPub()) {
          // 成果导入时pubList拆分（包括原始引用情况）
          publist = this.pubListService.praseSourcePubList(doc);
        } else {
          // 解析收录情况
          // publist = this.pubListService.prasePubList(doc);
        }
        // 保存成果查重字段
        this.parsePubDupFields(doc, pub);
        // 清除数据库的pub_error_fields,遍历xml中的pub_errors/error,把错误的字段名保存进pub_error_fields
        savePubErrorFields(doc, context);
        String zhKeywords = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_keywords");
        String enKeywords = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_keywords");
        // 成果关键词拆分保存
        // this.pubKeyWordsService.savePubKeywords(pub.getPubId(), context.getCurrentInsId(), zhKeywords,
        // enKeywords);
        // 存储XML数据
        this.savePubXml(doc, context);
      } else {
        throw new SysServiceException("updatePublication该成果不存在pubId:" + pubId);
      }
      return pub;
    } catch (Exception e) {
      logger.error("savePubEdit保存成果编辑内容出错 ", e);
      throw new SysServiceException("savePubEdit保存成果编辑内容出错 ");
    }
  }

  /**
   * 删除成果成员.
   * 
   * @param doc
   * @param context
   * @throws SysServiceException
   */
  public void deletePubMember(PubXmlDocument doc, PubXmlProcessContext context) throws SysServiceException {
    Long pubId = context.getCurrentPubId();
    Long currentInsId = context.getCurrentInsId();
    try {
      long opPsnId = SecurityUtils.getCurrentUserId();
      List<Long> pmIds = pubMemberDao.getPmIdByPubId(pubId);
      for (Long pmId : pmIds) {
        SiePubMember pm = pubMemberDao.getPubMemberById(pmId);
        pubMemberDao.deletePubMember(pm);
        Map<String, String> opDetail = new HashMap<String, String>();
        opDetail.put("pmId", pm.getPmId().toString());
        opDetail.put("author", pm.getMemberName());
        opDetail.put("seqNo", pm.getSeqNo().toString());
        siePublicationLogService.logOp(pubId, opPsnId, currentInsId, PublicationOperationEnum.RemovePubMember,
            opDetail);
      }
    } catch (NumberFormatException e) {
      logger.error("deletePubMember成果成员出错 ", e);
      throw new SysServiceException(e);
    } catch (Exception e) {
      logger.error("deletePubMember成果成员出错 ", e);
      throw new SysServiceException(e);
    }
  }

  /**
   * 修改/添加成果成员，返回第一作者PsnId.
   * 
   * @param doc
   * @param context
   * @throws SysServiceException
   */
  @SuppressWarnings({"unchecked", "unused"})
  public Long savePubMember(PubXmlDocument doc, PubXmlProcessContext context) throws SysServiceException {
    doc.getXmlString();
    try {
      Long pubId = context.getCurrentPubId();
      List<Node> ndList = doc.getPubMembers();
      if (ndList == null || ndList.size() == 0)
        return null;
      Long firstPsnId = null;
      SiePublication pub = this.getPublicationById(pubId);
      for (int i = 0; i < ndList.size(); i++) {
        Element em = (Element) ndList.get(i);
        SiePubMember pm = null;
        // 获取成员ID，如果存在则查找修改
        Long pmId = IrisNumberUtils.createLong(em.attributeValue("pm_id"));
        if (pmId != null) {
          pm = pubMemberDao.getPubMemberById(pmId);
          // 创建成员
          if (pm == null) {
            pm = new SiePubMember();
          }
        } else {
          // 创建成员
          pm = new SiePubMember();
        }
        pm.setAuthorPos("1".equals(em.attributeValue("author_pos")) ? 1 : 0);
        Long cpmPsn = IrisNumberUtils.createLong(em.attributeValue("member_psn_id"));
        // 作者ID不为空
        if (cpmPsn != null) {
          SieInsPerson person = sieInsPersonDao.findPsnIns(cpmPsn, context.getCurrentInsId());
          if (person.getUnitId() != null) {
            SieInsUnit insUnit = sieInsUnitService.getInsUnitById(person.getUnitId());
            pm.setUnitId(person.getUnitId());
            em.addAttribute("unit_id", person.getUnitId().toString());
            String unitName = insUnit.getZhName() == null ? insUnit.getEnName() : insUnit.getZhName();
            em.addAttribute("unit_name", unitName);
            pm.setUnitName(unitName);
          }
          pm.setMemberPsnid(cpmPsn);
        } else {
          pm.setInsId(IrisNumberUtils.createLong(em.attributeValue("ins_id")));
          pm.setInsName(StringUtils.substring(em.attributeValue("ins_name"), 0, 100));
          pm.setEmail(StringUtils.substring(em.attributeValue("email"), 0, 50));
          pm.setMemberPsnid(null);
          em.addAttribute("member_psn_id", "");
          em.addAttribute("member_psn_acname", "");
          em.setAttributeValue("unit_name", "");
          em.setAttributeValue("unit_id", "");
        }
        String memberName = StringUtils.substring(em.attributeValue("member_psn_name"), 0, 50);
        pm.setMemberName(memberName);
        pm.setPubId(pubId);
        pm.setSeqNo(IrisNumberUtils.createLong(em.attributeValue("seq_no")));
        // 保存数据
        pubMemberDao.savePubMember(pm);
        em.addAttribute("pm_id", pm.getPmId().toString());
      }
      return firstPsnId;
    } catch (Exception e) {
      logger.error("savePubMember保存成员出错 ", e);
      throw new SysServiceException("savePubMember保存成员出错");
    }
  }

  /**
   * 添加、删除、保存成果错误字段.
   * 
   * @param doc
   * @return
   * @throws SysServiceException
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<SiePubErrorField> savePubErrorFields(PubXmlDocument doc, PubXmlProcessContext context)
      throws SysServiceException {
    Long pubId = doc.getPubId();
    try {
      // if (XmlOperationEnum.Edit.equals(context.getCurrentAction()))
      // 删除原有错误检查信息
      publicationDao.deleteErrorFields(pubId);
      List<Node> ndList = doc.getPubErrorFields();
      if (ndList == null || ndList.size() == 0)
        return null;
      List<SiePubErrorField> errorList = new ArrayList<SiePubErrorField>();
      for (Node node : ndList) {
        Element em = (Element) node;
        // 创建错误实体
        SiePubErrorField error = new SiePubErrorField();
        error.setCreateAt(new Date());
        error.setErrorNo(IrisNumberUtils.createInteger(em.attributeValue("error_no")));
        error.setName(em.attributeValue("field"));
        error.setPubId(pubId);
        // 保存
        publicationDao.saveErrorField(error);
        errorList.add(error);
      }
      return errorList;
    } catch (Exception e) {
      logger.error("savePubErrorFields保存成果错误信息出错 ", e);
      throw new SysServiceException("savePubErrorFields保存成果错误信息出错");
    }
  }

  /**
   * 把成果成员的名字用逗号分隔连接起来.
   * 
   * @param pubId
   * @return
   * @throws SysServiceException
   */
  public Map<String, String> buildPubAuthorNames(long pubId) throws SysServiceException {
    try {
      return this.pubMemberDao.buildPubAuthorNames(pubId);
    } catch (Exception e) {
      logger.error("读取成果成员列表错误;pubId: " + pubId, e);
      throw new SysServiceException(e);
    }
  }

  @Override
  public boolean isSameType(long pubIdA, long pubIdB) throws SysServiceException {
    SiePublication pubA = this.publicationDao.get(pubIdA);
    SiePublication pubB = this.publicationDao.get(pubIdB);
    return pubA.getPubType() == pubB.getPubType();
  }

  @Override
  public void rebuildOldPubData(SiePublication pub) throws SysServiceException {
    try {
      if (pub == null)
        return;
      Long pubId = pub.getPubId();
      SiePubXml pubXml = this.siePubXmlDao.get(pubId);
      String xml = pubXml.getPubXml();
      if (StringUtils.isBlank(xml))
        return;
      PubXmlDocument xmlDocument = new PubXmlDocument(xml);
      pub.setEnTitle(StringUtils
          .substring(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_title_text"), 0, 250));
      pub.setZhTitle(StringUtils
          .substring(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title_text"), 0, 250));
      this.publicationDao.save(pub);
    } catch (Exception e) {
      logger.error("重构导入的成果数据. ", e);
      throw new SysServiceException(e);
    }
  }

  @Override
  public Boolean validatePubEnter(Map postData) throws SysServiceException {
    // PubXmlDocument newDoc = null;
    // try {
    // newDoc = PubXmlDocumentBuilder.build(postData);
    // } catch (Exception e1) {
    // logger.error(e1.getMessage(), e1);
    // return false;
    // }
    // String xmlStr = newDoc.getXmlString();
    // try {
    // DocumentHelper.parseText(xmlStr);
    // } catch (DocumentException e) {
    // logger.error(e.getMessage(), e);
    // return false;
    // }
    return true;
  }

  @Override
  public List<SiePubMember> getPubMembersByPubId(Long pubId) throws SysServiceException {
    try {
      return pubMemberDao.getPubMembersByPubId(pubId);
    } catch (Exception e) {
      logger.error("根据pubId查询PubMemberRol出错 ", e);
      throw new SysServiceException(e);
    }
  }

  @Override
  public String getPubXmlById(Long pubId) throws SysServiceException {
    String xmlstr = "";
    try {
      SiePubXml pubxml = this.siePubXmlDao.get(pubId);
      if (pubxml != null)
        xmlstr = pubxml.getPubXml();
    } catch (Exception e) {
      logger.error(" 根据成果ID：{} 获取xml出错", pubId, e);
      throw new SysServiceException(e);
    }
    return xmlstr;
  }

  @Override
  public String getUpdatePubCitedParams(List<Long> pubIds) throws SysServiceException {
    try {
      List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
      for (Long pubId : pubIds) {
        String citeSourceUrl = "";
        Map<String, String> map = new HashMap<String, String>();
        PubXmlDocument doc = new PubXmlDocument(this.getPubXmlById(pubId));
        String zhTitle = XmlUtil
            .trimNBSP(StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title")));
        String enTitle = XmlUtil
            .trimNBSP(StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_title")));
        String isiId = StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "isi_id"));
        String url = "";
        if (StringUtils.isNotBlank(isiId)) {
          url += "UT=" + isiId;
        } else if (StringUtils.isNotBlank(enTitle)) {
          url += "TI=(" + enTitle + ")";
        } else if (StringUtils.isNotBlank(zhTitle)) {
          url += "TI=(" + zhTitle + ")";
        }
        citeSourceUrl = "$" + url;
        map.put("pubIds", ObjectUtils.toString(pubId));
        map.put("sid", "");
        map.put("psnId", ObjectUtils.toString(SecurityUtils.getCurrentUserId()));
        map.put("citeUrl", citeSourceUrl);
        mapList.add(map);
      }
      return JSONArray.fromObject(mapList).toString();
    } catch (Exception e) {
      logger.error("获取成果更新参数出错", e);
    }
    return null;
  }

  @Override
  public List<SiePublication> getPublicationByIds(List<Long> pubIds) throws SysServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void deletePubPsn(PubXmlDocument doc, PubXmlProcessContext context) throws SysServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void syncInsPubIdToSubmission(PubXmlDocument xmlDocument, long insPubId, long snsPubId,
      PubXmlProcessContext context) throws SysServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void setPubConfirmResult(List<Long> pubIds, Integer confirmResult) throws SysServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void setPubPublish(List<Long> pubIds, Integer openStatus) throws SysServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public List<SiePublication> getPubByBatchForOld(Long lastId, int batchSize) throws SysServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public SiePublication saveRebuildPublication(PubXmlDocument doc, PubXmlProcessContext context)
      throws SysServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void saveRebuildPublication(Long pubId) {
    // TODO Auto-generated method stub

  }

  @Override
  public List<SiePublication> loadRebuildPubId(Long lastPubId, int size) throws SysServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<SiePublication> loadRebuildIsiId(Long lastId, int batchSize) throws SysServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<SiePublication> loadRebuildCiteRecordUrlPubId(Long lastPubId, int size) throws SysServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void rebuildPubXMLIsiId(SiePublication pub) {
    // TODO Auto-generated method stub

  }

  @Override
  public Map<String, List<Long>> confirmPsnPubRelation(List<Long> pubIds, Long psnId) throws SysServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Object[]> getFilterNotDelPubSource(Collection<Long> pubIds) throws SysServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List findPubIdsBatch(Long lastId, int batchSize) throws SysServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Long briefDescTask(Long pubId) throws SysServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Long> findAllPubIdsBatch(Long lastId, int size) throws SysServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Map<String, Long>> getPubCountByUnitId(List<Long> unitIdList, Long superUnitId, Long insId)
      throws SysServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<String, Long> queryPubOtherCount(Long insId, Long parentUnitId) throws SysServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<String, Long> queryPubCountAllByUnitId(Long unitId, Long insId) throws SysServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Map<String, Long>> getPubCountByPsnId(List<Long> psnIdList, Long insId) throws SysServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isPubMemberLinkPsn(Long pubId, Long insPsnId) throws SysServiceException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void updatePubCitedTimes(List<Long> pubIds, String citedXml) throws SysServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public Map<String, Object> getShowPubData(Long pubId, String domain) throws SysServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void uploadPubXmlFulltext(Long pubId, Map<String, Object> parameterMap, Long insId) {
    // TODO Auto-generated method stub

  }
}
