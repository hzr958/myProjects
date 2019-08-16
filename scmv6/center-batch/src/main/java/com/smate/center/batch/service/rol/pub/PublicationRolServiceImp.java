package com.smate.center.batch.service.rol.pub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.constant.PubAssignModeEnum;
import com.smate.center.batch.constant.PubHtmlContants;
import com.smate.center.batch.constant.PublicationRolStatusEnum;
import com.smate.center.batch.dao.rol.pub.CniprPubcacheInsAssignDao;
import com.smate.center.batch.dao.rol.pub.CnkiPatPubcacheInsAssignDao;
import com.smate.center.batch.dao.rol.pub.CnkiPubcacheInsAssignDao;
import com.smate.center.batch.dao.rol.pub.EiPubcacheInsAssignDao;
import com.smate.center.batch.dao.rol.pub.InsRegionDao;
import com.smate.center.batch.dao.rol.pub.IsiPubcacheInsAssignDao;
import com.smate.center.batch.dao.rol.pub.PdwhPubcacheInsAssignDao;
import com.smate.center.batch.dao.rol.pub.PubFulltextImageRefreshRolDao;
import com.smate.center.batch.dao.rol.pub.PubMedPubcacheInsAssignDao;
import com.smate.center.batch.dao.rol.pub.PubPsnRolDao;
import com.smate.center.batch.dao.rol.pub.PubRolSubmissionDao;
import com.smate.center.batch.dao.rol.pub.PubUnitOwnerDao;
import com.smate.center.batch.dao.rol.pub.PublicationRolDao;
import com.smate.center.batch.dao.rol.pub.RolPubXmlDao;
import com.smate.center.batch.dao.rol.pub.SpsPubcacheInsAssignDao;
import com.smate.center.batch.enums.pub.PublicationOperationEnum;
import com.smate.center.batch.enums.pub.XmlOperationEnum;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.BaseJournalSearch;
import com.smate.center.batch.model.pdwh.pub.cnipr.CniprPubcacheInsAssign;
import com.smate.center.batch.model.rol.InsConfirm;
import com.smate.center.batch.model.rol.pub.ClearTaskNoticeEvent;
import com.smate.center.batch.model.rol.pub.CnkiPatPubcacheInsAssign;
import com.smate.center.batch.model.rol.pub.CnkiPubcacheInsAssign;
import com.smate.center.batch.model.rol.pub.EiPubcacheInsAssign;
import com.smate.center.batch.model.rol.pub.InsRegion;
import com.smate.center.batch.model.rol.pub.InsUnit;
import com.smate.center.batch.model.rol.pub.IsiPubcacheInsAssign;
import com.smate.center.batch.model.rol.pub.PdwhPubcacheInsAssign;
import com.smate.center.batch.model.rol.pub.PubErrorFieldRol;
import com.smate.center.batch.model.rol.pub.PubFillKpiData;
import com.smate.center.batch.model.rol.pub.PubFillKpiMember;
import com.smate.center.batch.model.rol.pub.PubHtml;
import com.smate.center.batch.model.rol.pub.PubMedPubcacheInsAssign;
import com.smate.center.batch.model.rol.pub.PubMemberRol;
import com.smate.center.batch.model.rol.pub.PubPsnCreateRelation;
import com.smate.center.batch.model.rol.pub.PubPsnRol;
import com.smate.center.batch.model.rol.pub.PubRolSubmission;
import com.smate.center.batch.model.rol.pub.PubRolSubmissionStatusEnum;
import com.smate.center.batch.model.rol.pub.PublicationListRol;
import com.smate.center.batch.model.rol.pub.PublicationRol;
import com.smate.center.batch.model.rol.pub.RolPubDupFields;
import com.smate.center.batch.model.rol.pub.SpsPubcacheInsAssign;
import com.smate.center.batch.model.sns.pub.PubDupFields;
import com.smate.center.batch.model.sns.pub.PubDupParam;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.model.sns.pub.PublicationXml;
import com.smate.center.batch.oldXml.prj.PrjXmlConstants;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pdwh.pub.JournalService;
import com.smate.center.batch.service.pdwh.pub.PublicationPdwhService;
import com.smate.center.batch.service.psn.InsPersonService;
import com.smate.center.batch.service.pub.PublicationLogService;
import com.smate.center.batch.service.pub.PublicationXmlService;
import com.smate.center.batch.service.pub.RolPublicationXmlManager;
import com.smate.center.batch.service.pub.mq.PubDupCheckMessage;
import com.smate.center.batch.service.pub.mq.PubDupCheckMessageProducer;
import com.smate.center.batch.util.pub.BasicRmtSrvModuleConstants;
import com.smate.center.batch.util.pub.ConstPublicationType;
import com.smate.center.batch.util.pub.PubXmlDocumentBuilder;
import com.smate.center.batch.util.pub.PublicationArticleType;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.constant.ScmRolRoleConstants;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.IrisNumberUtils;
import com.smate.core.base.utils.string.ServiceUtil;

import net.sf.json.JSONArray;

/**
 * 单位成果管理service.
 * 
 * @author liqinghua
 * 
 */
@Service("publicationRolService")
@Transactional(rollbackFor = Exception.class)
public class PublicationRolServiceImp implements PublicationRolService {

  /**
   * 
   */
  private static final long serialVersionUID = -8220619691038261257L;

  /**
   * 
   */
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PublicationRolDao publicationRolDao;
  @Autowired
  private RolPubXmlDao rolPubXmlDao;
  @Autowired
  private PublicationLogService publicationLogService;
  @Autowired
  private PubRolMemberDao pubRolMemberDao;
  @Autowired
  private PubPsnRolDao pubPsnRolDao;
  @Autowired
  private PublicationXmlService publicationXmlService;
  @Autowired
  private RolPublicationXmlManager rolPublicationXmlManager;
  @Autowired
  private IsiPubcacheInsAssignDao isiPubcacheInsAssignDao;
  @Autowired
  private PubMedPubcacheInsAssignDao pubMedPubcacheInsAssignDao;
  @Autowired
  private CnkiPubcacheInsAssignDao cnkiPubcacheInsAssignDao;
  @Autowired
  private SpsPubcacheInsAssignDao spsPubcacheInsAssignDao;
  @Autowired
  private InsRegionDao insRegionDao;
  @Autowired
  private KpiRefreshPubService kpiRefreshPubService;
  @Autowired
  private TaskNoticeService taskNoticeService;
  @Autowired
  private InstitutionRolService institutionRolService;
  @Autowired
  private InsUnitRolService insUnitRolService;
  @Autowired
  private PublicationListRolService publicationListRolService;
  @Autowired
  private PubRolPersonService pubRolPersonService;
  @Autowired
  private RolPubDupService rolPubDupService;
  @Autowired
  private PubHtmlService pubHtmlService;
  @Autowired
  private PublicationPdwhService publicationPdwhService;
  @Autowired
  private InsPersonService insPersonService;
  @Autowired
  private JournalService journalService;
  @Autowired
  private PubFulltextRolService pubFulltextRolService;
  @Autowired
  private PubFundInfoRolService pubFundInfoService;
  @Autowired
  private PubRolKeyWordsService pubRolKeyWordsService;
  @Autowired
  private JnlOATypeRefreshService jnlOATypeRefreshService;
  @Autowired
  private PubInfoHtmlBuilder pubInfoHtmlBuilder;
  @Autowired
  private PubDupCheckMessageProducer pubDupCheckMessageProducer;
  @Autowired
  private PsnPubStatSyncService psnPubStatSyncService;

  @Autowired
  private CniprPubcacheInsAssignDao cniprPubcacheInsAssignDao;
  @Autowired
  private CnkiPatPubcacheInsAssignDao cnkiPatPubcacheInsAssignDao;
  @Autowired
  private PubFulltextImageRefreshRolDao pubFulltextImageRefreshDao;
  @Autowired
  private PubUnitOwnerDao pubUnitOwnerDao;
  @Autowired
  private PubRolSubmissionDao pubRolSubmissionDao;
  @Autowired
  private EiPubcacheInsAssignDao eiPubcacheInsAssignDao;
  @Autowired
  private PdwhPubcacheInsAssignDao pdwhPubcacheInsAssignDao;

  /**
   * 获取成果实体，先从自定义缓存读取，如果不存在直接查询数据库.
   * 
   * @param id
   * @return
   * @throws ServiceException
   */
  public PublicationRol getPublicationById(Long id) throws ServiceException {

    try {
      return this.publicationRolDao.get(id);
    } catch (Exception e) {
      logger.error("getPublicationById获取成果实体id=" + id, e);
      throw new ServiceException(e);
    }

  }

  @Override
  public Integer getPubStatusById(Long id) throws ServiceException {

    try {
      return this.publicationRolDao.getPubStatusById(id);
    } catch (Exception e) {
      logger.error("获取成果状态id=" + id, e);
      throw new ServiceException("获取成果状态id=" + id, e);
    }
  }

  @Override
  public List<PublicationRol> getPublicationById(List<Long> ids, Long insId) throws ServiceException {
    try {
      return this.publicationRolDao.getByIds(ids, insId);
    } catch (Exception e) {
      logger.error("批量获取单位成果", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 保存新添加成果.
   * 
   * @param pub
   * @throws Exception
   */
  public PublicationRol createPublication(PubXmlDocument doc, PubXmlProcessContext context) throws Exception {
    try {

      Date now = new Date();

      PublicationRol pub = new PublicationRol();
      pub.setArticleType(doc.getArticleTypeId());
      pub.setInsId(context.getCurrentInsId());
      pub.setCreatePsnId(context.getCurrentUserId());
      pub.setTypeId(context.getPubTypeId());
      pub.setCreateDate(now);

      // 该单位是否需要确认才能导入成果
      if (InsConfirm.NEED_CONFIRM.equals(context.getNeedConfirmStatus())) {
        pub.setStatus(PublicationRolStatusEnum.NEED_CONFIRM);
      } else {
        // 个人提交，状态为等待审核
        if (XmlOperationEnum.SyncFromSNS.equals(context.getCurrentAction())) {
          pub.setStatus(PublicationRolStatusEnum.PENDING_FOR_VERIFICATION);
        } else {// 单位添加默认为已批准
          pub.setStatus(PublicationRolStatusEnum.APPROVED);
        }
      }

      wrapPubField(doc, context, now, pub);

      // 认领状态，默认为0
      pub.setAuthorState(0);
      pub.setVersionNo(1);
      publicationRolDao.save(pub);

      doc.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "pub_id", pub.getId().toString());
      doc.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "version_no", pub.getVersionNo().toString());
      context.setCurrentPubId(pub.getId());

      if (XmlOperationEnum.SyncFromSNS.equals(context.getCurrentAction())) {
        String snsPubId = doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "rol_sync_from_pub");
        if (!"".equals(snsPubId)) {
          pub.setSnsPubId(Long.parseLong(snsPubId));
          this.syncInsPubIdToSubmission(doc, context.getCurrentPubId(), Long.parseLong(snsPubId), context);
        }
      }

      // 建立成果、成员关系数据
      Long firstAuthor = savePubMember(doc, context);
      // 第一作者
      doc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "first_author",
          firstAuthor == null ? "" : firstAuthor.toString());

      pub.setAuthorNames(StringUtils
          .substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_author_names"), 0, 200));

      // 保存第一作者
      pub.setFirstAuthorPsnId(firstAuthor);
      // ROL-2497 创建后默认公开
      pub.setIsOpen(1);
      publicationRolDao.save(pub);
      // ROL-2497 创建后默认公开,与SaveOrUpdatePubHtmlTask重复，注释
      // this.pubHtmlService.updatePubHtmlRefresh(pub.getId(),
      // PubHtmlContants.PUB_OUTSIDE_TEM_CODE);

      // 刷新XML中的基准库信息到表中
      if (XmlOperationEnum.SyncFromSNS.equals(context.getCurrentAction())) {
        publicationPdwhService.refreshRolPubPdwhToDB(pub.getId(), doc);
      }
      PublicationListRol publist = null;
      if (context.isImport() || context.isSyncPub()) {
        // 成果导入时pubList拆分（包括原始引用情况）
        if (XmlOperationEnum.OfflineImport.equals(context.getCurrentAction())) {
          publist = this.publicationListRolService.praseAndSavePubList(doc);
        } else {
          publist = this.publicationListRolService.praseSourcePubList(doc);
        }
      } else {
        // 解析收录情况
        publist = this.publicationListRolService.prasePubList(doc);
      }
      // 设置CitedList
      this.setPubCitedList(pub, publist);

      // 清除数据库的pub_error_fields,遍历xml中的pub_errors/error,把错误的字段名保存进pub_error_fields
      savePubErrorFields(doc, context);

      // 保存创建部门ID，只有部门管理员才需要
      this.savePubUnitOwner(pub.getId(), context.getCurrentInsId());
      // 保存成果查重字段
      this.parsePubDupFields(doc, pub);
      // 存储XML数据
      String xml = doc.getXmlString();

      this.publicationXmlService.rolSave(pub.getId(), xml);
      // 保存成果项目信息
      String fundInfo = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "fundinfo");
      this.pubFundInfoService.savePubFundInfo(pub.getId(), pub.getInsId(), fundInfo);
      String zhKeywords = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_keywords");
      String enKeywords = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_keywords");
      // 成果关键词拆分保存
      this.pubRolKeyWordsService.savePubKeywords(pub.getId(), context.getCurrentInsId(), zhKeywords, enKeywords);

      this.savePubFulltext(pub);

      if (pub.getStatus() == PubRolSubmissionStatusEnum.APPROVED) {
        // 添加成果统计冗余
        this.kpiRefreshPubService.addPubRefresh(pub, false);
      }
      // 清理缓存中的任务统计数
      taskNoticeService.clearTaskNotice(ClearTaskNoticeEvent.getInstance(context.getCurrentInsId(), 1, 1, 1));
      // 刷新期刊-开放存储类型
      if (pub.getJid() != null && pub.getJid() > 0) {
        this.jnlOATypeRefreshService.saveJnlOATypeRefresh(pub.getJid());
      }
      return pub;
    } catch (ServiceException e) {
      logger.error("savePubCreate保存新添加成果出错 ", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 保存成果部门关联关系，部门管理员才有这个功能.
   * 
   * @param pubId
   * @param insId
   * @throws ServiceException
   */
  private void savePubUnitOwner(Long pubId, Long insId) throws ServiceException {

    Integer roleId = SecurityUtils.getCurrentUserRoleId();
    Long unitId = SecurityUtils.getCurrentUnitId();
    // 部门管理员
    if (ScmRolRoleConstants.UNIT_RO.equals(roleId) && (unitId != null && unitId != 0)) {
      InsUnit insUnit = this.insUnitRolService.getInsUnitRolById(unitId);
      if (insUnit != null) {
        this.pubUnitOwnerDao.savePubUnitOwner(pubId, insId, unitId, insUnit.getSuperInsUnitId());
      }
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
  private void wrapPubField(PubXmlDocument doc, PubXmlProcessContext context, Date now, PublicationRol pub)
      throws ServiceException {

    // 确认状态
    Integer confirmResult = pub.getConfirm();

    if (confirmResult == null || confirmResult == 0) {
      confirmResult =
          IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "confirm_result"));
    }
    if (confirmResult == null || confirmResult == 0) {
      confirmResult = context.getConfirmResult();
    }
    if (confirmResult != null) {
      doc.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "confirm_result", confirmResult.toString());
    }
    pub.setConfirm(confirmResult);
    // 引用次数
    Integer citeTimes =
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cite_times"));
    // if (citeTimes != null) {
    pub.setCitedTimes(citeTimes);
    pub.setCitedDate(now);
    // pub.setCreateDate(now);//rol-919 修改成果后的createDate不应改变
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
    pub.setIsiId(doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "isi_id"));
    pub.setSourceDbId(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "source_db_id")));
    pub.setDataValidate(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "is_valid")));
    pub.setEnTitle(
        StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_title_text"), 0, 250));
    pub.setEnTitleText(StringUtils
        .substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_title_text").toLowerCase(), 0, 250));
    pub.setEnTitleHash(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_title_hash")));
    pub.setFingerPrint(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "finger_print")));
    pub.setImpactFactors(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "impact_factors"));
    pub.setJid(IrisNumberUtils.createLong(doc.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jid")));

    pub.setPublishDay(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_day")));
    pub.setPublishMonth(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_month")));
    pub.setPublishYear(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_year")));
    pub.setRecordFrom(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "record_from")));
    pub.setRegionId(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "country_id")));

    pub.setZhTitle(
        StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title_text"), 0, 250));
    pub.setZhTitleText(StringUtils
        .substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title_text").toLowerCase(), 0, 250));
    pub.setZhTitleHash(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title_hash")));

    pub.setBriefDesc(
        StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc"), 0, 500));

    pub.setBriefDescEn(
        StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc_en"), 0, 500));

    pub.setFulltextFileid(doc.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "file_id"));
    Integer fulltextNodeId =
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "node_id"));
    pub.setFulltextNodeId(fulltextNodeId);
    pub.setFulltextUrl(
        StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "fulltext_url"), 0, 3000));
    pub.setFulltextExt(null);
    if (pub.getFulltextFileid() != null) {
      String fileExt = doc.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "file_ext");
      if (StringUtils.isNotBlank(fileExt)) {
        pub.setFulltextExt(StringUtils.substring(fileExt, 0, 30));
      }
    }

    // 补充信息
    pub.setStartPage(
        StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "start_page"), 0, 50));
    pub.setEndPage(
        StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "end_page"), 0, 50));
    String isbn = StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "isbn"), 0, 40);
    if (StringUtils.isBlank(isbn)) {
      isbn = StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "isbn"), 0, 40);
    }
    pub.setIsbn(isbn);
    pub.setVolume(StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "volume"), 0, 20));
    pub.setIssue(StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "issue"), 0, 20));
    pub.setDoi(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "doi"));
    pub.setUpdateDate(now);
    pub.setUpdatePsnId(context.getCurrentUserId());
  }

  @Override
  public RolPubDupFields parsePubDupFields(PubXmlDocument doc, PublicationRol pub) throws ServiceException {

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
    String enTitle = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_title");
    String doi = StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "doi"), 0, 100);
    String isiId = doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "isi_id");
    String eiId = doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "ei_id");
    String spsId = doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "sps_id");
    String patentNo = doc.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_no");
    String patentOpenNo = doc.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_open_no");
    String confName = doc.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "conf_name");
    String issn = doc.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jissn");
    String jname = doc.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jname");
    if (StringUtils.isBlank(issn)) {
      issn = StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "issn"), 0, 40);
    }
    // 作者名
    String auNames = StringUtils.join(doc.getPubMemberNames(), ",");
    Long ownerId = pub.getInsId();
    Long pubId = pub.getId();
    Integer pubYear = pub.getPublishYear();
    Integer sourceDbId = pub.getSourceDbId();
    Integer pubType = pub.getTypeId();
    PubDupParam param = new PubDupParam();
    param.setArticleNo(articleNo);
    param.setDoi(doi);
    param.setEnTitle(enTitle);
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
    param.setPatentNo(patentNo);
    param.setPatentOpenNo(patentOpenNo);
    param.setAuthorNames(auNames);
    param.setConfName(confName);
    param.setIssn(issn);
    param.setJname(jname);
    Integer pubDupStatus = PubDupFields.DELETE_STATUS;
    if (pub.getStatus() != null) {
      if (PublicationRolStatusEnum.APPROVED == pub.getStatus()) {
        pubDupStatus = PubDupFields.NORMAL_STATUS;
      } else if (PublicationRolStatusEnum.NEED_CONFIRM == pub.getStatus()) { // 临时成果不参与查重
        pubDupStatus = PubDupFields.INS_NOT_CONFIRM_STATUS;
      }
    }
    return this.rolPubDupService.savePubDupFields(param, pubType, pubId, ownerId, pub.getArticleType(), pubDupStatus);
  }

  @Override
  public PublicationRol saveSyncOldPublication(PubXmlDocument doc, PubXmlProcessContext context)
      throws ServiceException {
    try {

      Date updateDate = context.getUpdateDate();
      if (updateDate == null) {
        updateDate = new Date();
      }
      PublicationRol pub = new PublicationRol();
      this.wrapPubField(doc, context, updateDate, pub);

      pub.setArticleType(context.getArticleType());
      Integer versionNo =
          IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PrjXmlConstants.PRJ_META_XPATH, "version_no"));
      if (versionNo == null) {
        versionNo = 1;
      }
      pub.setVersionNo(versionNo);
      pub.setTypeId(context.getPubTypeId());
      // 单位添加默认为已批准
      pub.setStatus(PublicationRolStatusEnum.APPROVED);

      // 认领状态，默认为0
      pub.setAuthorState(context.getAuthorState());
      // 源成果ID,V2.6同步使用
      pub.setOldPubId(context.getFromPubId());
      pub.setCreatePsnId(context.getCurrentUserId());
      publicationRolDao.save(pub);

      doc.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "pub_id", pub.getId().toString());
      doc.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "version_no", pub.getVersionNo().toString());

      context.setCurrentPubId(pub.getId());

      // 建立成果、成员关系数据
      Long firstAuthor = savePubMember(doc, context);

      pub.setAuthorNames(StringUtils
          .substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_author_names"), 0, 200));

      // 保存第一作者
      pub.setFirstAuthorPsnId(firstAuthor);
      publicationRolDao.save(pub);

      // 解析收录情况
      this.publicationListRolService.prasePubList(doc);

      // 清除数据库的pub_error_fields,遍历xml中的pub_errors/error,把错误的字段名保存进pub_error_fields
      savePubErrorFields(doc, context);

      // 存储XML数据
      String xml = doc.getXmlString();
      this.publicationXmlService.save(pub.getId(), xml);
      // 刷新期刊-开放存储类型
      if (pub.getJid() != null && pub.getJid() > 0) {
        this.jnlOATypeRefreshService.saveJnlOATypeRefresh(pub.getJid());
      }

      return pub;
    } catch (Exception e) {
      logger.error("saveSyncOldPublication保存同步成果出错 ", e);
      throw new ServiceException(e);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public void syncOldInsPub(Map<String, Object> map) throws ServiceException {

    String pubXml = map.get("PUB_XML") == null ? null : map.get("PUB_XML").toString();
    Integer articleType = map.get("ARTICLE_TYPE") == null ? null : Integer.valueOf(map.get("ARTICLE_TYPE").toString());
    Integer typeId = map.get("PUB_TYPE") == null ? null : Integer.valueOf(map.get("PUB_TYPE").toString());

    if (StringUtils.isBlank(pubXml) || articleType == null || typeId == null)
      return;

    Map<String, Object> params = new HashMap<String, Object>();
    params.put("PUB_XML", pubXml);
    params.put("ARTICLE_TYPE", articleType);
    params.put("PUB_TYPE", typeId);

    // 单位ID列表
    List<Map<String, Object>> pubInsList = (List<Map<String, Object>>) map.get("pubInsList");
    // 认领状态authorState
    Integer authorState =
        map.get("AUTHOR_ASSIGN_STATE") == null ? 0 : Integer.valueOf(map.get("AUTHOR_ASSIGN_STATE").toString());
    Long pubId = map.get("PUB_ID") == null ? 0 : Long.valueOf(map.get("PUB_ID").toString());
    Long psnId = map.get("CREATE_PSN_ID") == null ? 0 : Long.valueOf(map.get("CREATE_PSN_ID").toString());
    Date updateDate = map.get("UPDATE_DATE") == null ? null : (Date) map.get("UPDATE_DATE");
    params.put("AUTHOR_ASSIGN_STATE", authorState);
    params.put("PUB_ID", pubId);
    params.put("CREATE_PSN_ID", psnId);
    params.put("UPDATE_DATE", updateDate);

    for (Map<String, Object> mapPubIns : pubInsList) {

      Integer isOpen = mapPubIns.get("IS_OPEN") == null ? 0 : Integer.valueOf(mapPubIns.get("IS_OPEN").toString());
      Integer confirmResult =
          mapPubIns.get("CONFIRM_STATUS") == null ? 0 : Integer.valueOf(mapPubIns.get("CONFIRM_STATUS").toString());
      Long insId = mapPubIns.get("INS_ID") == null ? 0 : Long.valueOf(mapPubIns.get("INS_ID").toString());
      params.put("IS_OPEN", isOpen);
      params.put("CONFIRM_STATUS", confirmResult);
      params.put("INS_ID", insId);
      rolPublicationXmlManager.syncOldPubXml(params);
    }
  }

  public void sendPubDupCheckQueue(PubXmlProcessContext context) throws ServiceException {
    // 成果排入查重队列
    Long pubId = context.getCurrentPubId();
    Long insId = context.getCurrentInsId();
    // 把pubId放进MQ的查重队列,立刻处理
    try {
      logger.debug("把pubId={},insId={}放进MQ的查重队列", pubId, insId);
      PubDupCheckMessage message = new PubDupCheckMessage(insId, pubId, context.getCurrentUserId());
      // 后台批量导入，等级设置成最低
      if (XmlOperationEnum.OfflineImport.equals(context.getCurrentAction())) {
        message.setPriority(1);
      }
      this.pubDupCheckMessageProducer.sendDupCheckMessage(message);
    } catch (Exception e) {
      logger.error("sendPubDupCheckQueue发送查重消息错误。 ", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 保存成果编辑内容.
   * 
   * @param pub
   * @throws ServcieException
   */
  public PublicationRol updatePublication(PubXmlDocument doc, PubXmlProcessContext context) throws ServiceException {

    try {
      Long pubId = context.getCurrentPubId();
      PublicationRol pub = publicationRolDao.get(pubId);
      if (pub != null) {
        Date now = new Date();

        this.wrapPubField(doc, context, now, pub);

        pub.setTypeId(doc.getPubTypeId());
        // 建立成果、成员关系数据
        this.deletePubMember(doc, context);
        this.deletePubPsn(doc, context);
        Long firstAuthor = savePubMember(doc, context);

        pub.setFirstAuthorPsnId(firstAuthor);
        pub.setAuthorNames(StringUtils
            .substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_author_names"), 0, 200));
        // 保存第一作者
        pub.setFirstAuthorPsnId(firstAuthor);

        pub.setVersionNo(pub.getVersionNo() + 1);
        publicationRolDao.save(pub);

        PublicationListRol publist = null;
        // 解析收录情况
        if (context.isImport() || context.isSyncPub()) {
          // 成果导入时pubList拆分（包括原始引用情况）
          publist = this.publicationListRolService.praseSourcePubList(doc);
        } else {
          // 解析收录情况
          publist = this.publicationListRolService.prasePubList(doc);
        }
        // 设置CitedList
        this.setPubCitedList(pub, publist);
        // 保存成果查重字段
        this.parsePubDupFields(doc, pub);
        // 清除数据库的pub_error_fields,遍历xml中的pub_errors/error,把错误的字段名保存进pub_error_fields
        savePubErrorFields(doc, context);
        String zhKeywords = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_keywords");
        String enKeywords = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_keywords");
        // 成果关键词拆分保存
        this.pubRolKeyWordsService.savePubKeywords(pub.getId(), context.getCurrentInsId(), zhKeywords, enKeywords);

        // 存储XML数据
        String xml = doc.getXmlString();
        this.publicationXmlService.rolSave(pub.getId(), xml);

        // 保存成果项目信息
        String fundInfo = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "fundinfo");
        this.pubFundInfoService.savePubFundInfo(pub.getId(), pub.getInsId(), fundInfo);
        // 刷新期刊-开放存储类型
        if (pub.getJid() != null && pub.getJid() > 0) {
          this.jnlOATypeRefreshService.saveJnlOATypeRefresh(pub.getJid());
        }
      } else {

        throw new ServiceException("updatePublication该成果不存在pubId:" + pubId);
      }

      this.savePubFulltext(pub);

      // 添加成果统计冗余
      this.kpiRefreshPubService.addPubRefresh(pub.getId(), false);
      // 清理缓存中的任务统计数
      taskNoticeService.clearTaskNotice(ClearTaskNoticeEvent.getInstance(context.getCurrentInsId(), 1, 1, 1));
      return pub;
    } catch (Exception e) {
      logger.error("savePubEdit保存成果编辑内容出错 ", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 删除成果成员.
   * 
   * @param doc
   * @param context
   * @throws ServiceException
   */
  public void deletePubMember(PubXmlDocument doc, PubXmlProcessContext context) throws ServiceException {

    Long pubId = context.getCurrentPubId();
    Long currentInsId = context.getCurrentInsId();
    try {

      long opPsnId = SecurityUtils.getCurrentUserId();
      String[] pmIdStrs = doc.getXmlNodeAttribute(PubXmlConstants.PUB_MEMBERS_XPATH, "remove_pms").split(",");

      for (int i = 0; i < pmIdStrs.length; i++) {
        if (!StringUtils.isBlank(pmIdStrs[i])) {
          long pmId = Long.parseLong(pmIdStrs[i]);

          PubMemberRol pm = pubRolMemberDao.getPubMemberById(pmId);

          List<PubPsnRol> pubPsnRolList = this.pubPsnRolDao.getAllStatusPubPsnRolByPmId(pmId);
          for (PubPsnRol pubPsnRol : pubPsnRolList) {

            this.psnPubStatSyncService.removePubPsn(pubPsnRol.getPubId(), pubPsnRol.getInsId(), pubPsnRol.getPsnId());
            Map<String, String> opDetail = new HashMap<String, String>();
            opDetail.put("pmId", pm.getId().toString());
            opDetail.put("psnId", String.valueOf(pubPsnRol.getPsnId()));
            opDetail.put("seqNo", pm.getSeqNo().toString());
            publicationLogService.logOp(pubId, opPsnId, currentInsId, PublicationOperationEnum.RemovePubPsn, opDetail);
          }
          pubRolMemberDao.deletePubMember(pm);

          Map<String, String> opDetail = new HashMap<String, String>();
          opDetail.put("pmId", pm.getId().toString());
          opDetail.put("author", pm.getName());
          opDetail.put("seqNo", pm.getSeqNo().toString());
          publicationLogService.logOp(pubId, opPsnId, currentInsId, PublicationOperationEnum.RemovePubMember, opDetail);
        }
      }

    } catch (NumberFormatException e) {
      logger.error("deletePubMember成果成员出错 ", e);
      throw new ServiceException(e);
    } catch (DaoException e) {
      logger.error("deletePubMember成果成员出错 ", e);
      throw new ServiceException(e);
    } catch (Exception e) {
      logger.error("deletePubMember成果成员出错 ", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 修改/添加成果成员，返回第一作者PsnId.
   * 
   * @param doc
   * @param context
   * @throws ServiceException
   */
  @SuppressWarnings("unchecked")
  public Long savePubMember(PubXmlDocument doc, PubXmlProcessContext context) throws ServiceException {

    try {
      Long pubId = context.getCurrentPubId();
      Long currentInsId = context.getCurrentInsId();

      List<Node> ndList = doc.getPubMembers();
      if (ndList == null || ndList.size() == 0)
        return null;
      Long firstPsnId = null;

      PublicationRol pub = this.getPublicationById(pubId);
      int status = pub.getStatus();

      ArrayList<PubMemberRol> authorNameAll = new ArrayList<PubMemberRol>();

      for (int i = 0; i < ndList.size(); i++) {

        Element em = (Element) ndList.get(i);

        PubMemberRol pm = null;
        // 获取成员ID，如果存在则查找修改
        Long pmId = IrisNumberUtils.createLong(em.attributeValue("pm_id"));
        if (pmId != null) {
          pm = pubRolMemberDao.getPubMemberById(pmId);
          // 创建成员
          if (pm == null) {
            pm = new PubMemberRol();
          }
        } else {
          // 创建成员
          pm = new PubMemberRol();
        }

        pm.setAuthorPos(IrisNumberUtils.createInteger(em.attributeValue("author_pos")));
        Long cpmPsn = IrisNumberUtils.createLong(em.attributeValue("member_psn_id"));
        // 当前修改匹配到的作者跟之前匹配到的作者不一样；或者之前匹配到的人员不为空，现在匹配到的人员为空
        if (pm.getPsnId() != null && cpmPsn != null && !pm.getPsnId().equals(cpmPsn)) {
          this.psnPubStatSyncService.removePubPsn(context.getCurrentPubId(), context.getCurrentInsId(), pm.getPsnId());
        }
        // 作者ID不为空
        if (cpmPsn != null) {
          // 设置机构ID为当前机构ID
          em.addAttribute("ins_id", SecurityUtils.getCurrentInsId().toString());
          String psnName = this.insPersonService.getPsnUnitNameCompose(SecurityUtils.getCurrentInsId(), cpmPsn);
          if (psnName != null) {
            // 成果为已确认
            pm.setIsConfirm(1);
            pm.setPsnId(cpmPsn);
            em.addAttribute("member_psn_acname", psnName);
          } else {
            em.addAttribute("member_psn_acname", "");
            em.addAttribute("member_psn_id", "");
            pm.setIsConfirm(0);
            pm.setPsnId(null);
            cpmPsn = null;
          }

        } else {// 清空作者
          // 成果为已确认
          pm.setIsConfirm(0);
          pm.setPsnId(null);
          em.addAttribute("member_psn_id", "");
          em.addAttribute("member_psn_acname", "");
        }
        // 作者所在机构
        pm.setPmInsId(IrisNumberUtils.createLong(em.attributeValue("ins_id")));
        String memberName = StringUtils.substring(em.attributeValue("member_psn_name"), 0, 50);
        if (context.getCurrentInsId().equals(pm.getPmInsId())) {
          pm.setPsnId(cpmPsn);
          if (cpmPsn != null) {
            pm.setIsConfirm(1);
          }
          if (i == 0 && cpmPsn != null) {
            firstPsnId = cpmPsn;
          }
          // 选择了其他机构，删除关联到的人员
        } else if (pm.getPmInsId() != null && pm.getId() != null) {
          this.psnPubStatSyncService.removePubPsnByPmId(context.getCurrentPubId(), pm.getId());
        }
        // 机构所在省市
        if (pm.getPmInsId() != null && pm.getPmInsId() != 1) {
          InsRegion region = insRegionDao.get(pm.getPmInsId());
          if (region != null) {
            pm.setPmCity(region.getCyId());
            pm.setPmPrv(region.getPrvId());
            pm.setPmDis(region.getDisId());
          } else {
            pm.setPmCity(null);
            pm.setPmPrv(null);
            pm.setPmDis(null);
          }
        }

        pm.setName(memberName);
        pm.setPubId(pubId);
        pm.setSeqNo(IrisNumberUtils.createInteger(em.attributeValue("seq_no")));
        pm.setInsId(context.getCurrentInsId());
        em.addAttribute("unit_id", "");
        em.addAttribute("parent_unit_id", "");
        pm.setUnitId(null);
        pm.setParentUnitId(null);
        // 保存数据
        pubRolMemberDao.savePubMember(pm);
        // 获取pmId
        pmId = pm.getId();

        // 成员ID写入XML
        em.addAttribute("pm_id", pm.getId().toString());
        // 保存人员、成果、部门之间的关系，只有成果是已批准状态
        if (pm.getPsnId() != null && status == PubRolSubmissionStatusEnum.APPROVED) {
          psnPubStatSyncService.addPubPsn(pubId, currentInsId, pm.getPsnId(), PubAssignModeEnum.ASSIGN_ON_ENTER, false,
              pmId);
        }

        // 添加至数组
        authorNameAll.add(pm);
      }

      // 在后台任务中,由于事务提交的顺序，无法获取最新的pubmember。因此直接使用authorNameAll；
      // Map<String, String> authorNames =
      // this.buildPubAuthorNames(pubId);
      Map<String, String> authorNames = this.buildPubAuthorNames(authorNameAll);
      doc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "author_names", authorNames.get("all_names"));
      doc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_author_names", authorNames.get("brief_names"));

      return firstPsnId;

    } catch (DaoException e) {
      logger.error("savePubMember保存成员出错 ", e);
      throw new ServiceException(e);
    } catch (Exception e) {
      logger.error("savePubMember保存成员出错 ", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 删除成果-单位人员关系.
   * 
   * @param doc
   * @param context
   * @throws ServiceException
   */
  public void deletePubPsn(PubXmlDocument doc, PubXmlProcessContext context) throws ServiceException {

    try {

      long opPsnId = SecurityUtils.getCurrentUserId();
      long pubId = context.getCurrentPubId();
      Long insId = context.getCurrentInsId();
      String[] psnIdStrs = doc.getXmlNodeAttribute(PubXmlConstants.PUB_MEMBERS_XPATH, "remove_psns").split(",");

      for (int i = 0; i < psnIdStrs.length; i++) {
        if (!StringUtils.isBlank(psnIdStrs[i])) {
          long psnId = Long.parseLong(psnIdStrs[i]);

          psnPubStatSyncService.removePubPsn(pubId, insId, psnId);
          Map<String, String> opDetail = new HashMap<String, String>();
          opDetail.put("psnId", String.valueOf(psnId));
          publicationLogService.logOp(pubId, opPsnId, insId, PublicationOperationEnum.RemovePubPsn, opDetail);
        }
      }

    } catch (NumberFormatException e) {
      logger.error("deletePubPsn成果成员出错 ", e);
      throw new ServiceException(e);
    } catch (Exception e) {
      logger.error("deletePubPsn成果成员出错 ", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 添加、删除、保存成果错误字段.
   * 
   * @param doc
   * @return
   * @throws ServiceException
   */
  @SuppressWarnings("unchecked")
  public List<PubErrorFieldRol> savePubErrorFields(PubXmlDocument doc, PubXmlProcessContext context)
      throws ServiceException {

    Long pubId = doc.getPubId();
    try {
      if (XmlOperationEnum.Edit.equals(context.getCurrentAction()))
        // 删除原有错误检查信息
        publicationRolDao.deleteErrorFields(pubId);

      List<Node> ndList = doc.getPubErrorFields();
      if (ndList == null || ndList.size() == 0)
        return null;
      List<PubErrorFieldRol> errorList = new ArrayList<PubErrorFieldRol>();
      for (Node node : ndList) {
        Element em = (Element) node;
        // 创建错误实体
        PubErrorFieldRol error = new PubErrorFieldRol();
        error.setCreateAt(new Date());
        error.setErrorNo(IrisNumberUtils.createInteger(em.attributeValue("error_no")));
        error.setName(em.attributeValue("field"));
        error.setPubId(pubId);
        // 保存
        publicationRolDao.saveErrorField(error);
        errorList.add(error);
      }

      return errorList;

    } catch (DaoException e) {
      logger.error("savePubErrorFields保存成果错误信息出错 ", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 把成果成员的名字用逗号分隔连接起来.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public Map<String, String> buildPubAuthorNames(ArrayList<PubMemberRol> authorNameAll) throws ServiceException {

    return this.HandlePubAuthorNames(authorNameAll);

  }

  /**
   * 把成果成员的名字用逗号分隔连接起来.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public Map<String, String> buildPubAuthorNames(long pubId) throws ServiceException {

    try {
      return this.pubRolMemberDao.buildPubAuthorNames(pubId);
    } catch (DaoException e) {
      logger.error("读取成果成员列表错误;pubId: " + pubId, e);
      throw new ServiceException(e);
    }
  }

  /**
   * 把成果成员的名字用逗号分隔连接起来.
   * 
   * @param authors
   * @return
   */
  private Map<String, String> HandlePubAuthorNames(ArrayList<PubMemberRol> authors) {
    StringBuffer authorNames = new StringBuffer(0);
    String allNames = "";
    String briefNames = "";
    String splitStr = ", ";
    String endStr = "……";
    Map<String, String> map = new HashMap<String, String>();
    for (int i = 0; i < authors.size(); i++) {
      PubMemberRol item = authors.get(i);
      String name = StringUtils.trimToEmpty(item.getName());
      if (StringUtils.isBlank(name)) {
        continue;
      }
      Long psnId = item.getPsnId();
      if (item.getAuthorPos() != null && 1 == item.getAuthorPos()) {
        name = "*" + name; // 是通讯作者，则名称前加*号
      }
      if (null == psnId) {
        // 缩略
        if (authorNames.length() < 200
            && (authorNames.length() + splitStr.length() + name.length() + endStr.length()) > 200) {
          briefNames = authorNames.toString() + endStr;
        }
        if (authorNames.length() > 0) {
          authorNames.append("; ");
        }
        authorNames.append(name);
      } else {
        // 缩略
        name = String.format("<strong>%s</strong>", name);
        if (authorNames.length() < 200
            && (authorNames.length() + splitStr.length() + name.length() + endStr.length()) > 200) {
          briefNames = authorNames.toString() + endStr;
        }
        if (authorNames.length() > 0) {
          authorNames.append("; ");
        }
        authorNames.append(name); // 可以关联到psn_id,则加粗显示
      }
    }
    if (authors.size() > 0) {
      allNames = authorNames.toString();
      if (StringUtils.isBlank(briefNames)) {
        briefNames = allNames;
      }
    }
    map.put("all_names", allNames);
    map.put("brief_names", briefNames);
    return map;
  }

  @Override
  public boolean isSameType(long pubIdA, long pubIdB) throws ServiceException {

    PublicationRol pubA = this.publicationRolDao.get(pubIdA);
    PublicationRol pubB = this.publicationRolDao.get(pubIdB);
    return pubA.getTypeId() == pubB.getTypeId();
  }

  @Override
  public void syncInsPubIdToSubmission(PubXmlDocument xmlDocument, long insPubId, long snsPubId,
      PubXmlProcessContext context) throws ServiceException {

    try {
      PubRolSubmission sub = this.pubRolSubmissionDao.getSubmissionBySNSPubId(snsPubId, context.getCurrentInsId());

      if (sub == null) {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "pub_status",
            String.valueOf(PublicationRolStatusEnum.PENDING_FOR_VERIFICATION));
        return;
      }

      PublicationRol pub = this.publicationRolDao.get(insPubId);
      if (sub.getSubmitStatus() != null && PubRolSubmissionStatusEnum.APPROVED == sub.getSubmitStatus()) {
        pub.setStatus(PublicationRolStatusEnum.APPROVED);
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "pub_status",
            String.valueOf(PublicationRolStatusEnum.APPROVED));
      } else {
        pub.setStatus(PublicationRolStatusEnum.PENDING_FOR_VERIFICATION);
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "pub_status",
            String.valueOf(PublicationRolStatusEnum.PENDING_FOR_VERIFICATION));
      }
      pub.setSnsPubId(snsPubId);
      this.publicationRolDao.save(pub);

      sub.setInsPub(pub);
      sub.setRealInsPub(pub);
      this.pubRolSubmissionDao.save(sub);

    } catch (DaoException e) {
      logger.error("syncInsPubIdToSubmission成果出错 ", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void setPubConfirmResult(List<Long> pubIds, Integer confirmResult) throws ServiceException {
    try {
      this.publicationRolDao.setPubConfirmResult(pubIds, confirmResult);
    } catch (Exception e) {
      logger.error("设置成果确认状态出错 ", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void setPubPublish(List<Long> pubIds, Integer openStatus) throws ServiceException {
    try {
      this.publicationRolDao.setPubPublish(pubIds, openStatus);
      for (Long pubId : pubIds) {
        this.pubHtmlService.updatePubHtmlRefresh(pubId, PubHtmlContants.PUB_OUTSIDE_TEM_CODE);
      }
    } catch (Exception e) {
      logger.error("设置成果发布状态出错 ", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<PublicationRol> getPubByBatchForOld(Long lastId, int batchSize) throws ServiceException {
    try {
      return this.publicationRolDao.getPubByBatchForOld(lastId, batchSize);
    } catch (Exception e) {
      logger.error("批量获取publication表数据，数据同步后重构XML用. ", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void rebuildOldPubData(PublicationRol pub) throws ServiceException {
    try {

      if (pub == null)
        return;
      Long pubId = pub.getId();
      PublicationXml pubXml = this.publicationXmlService.getById(pubId);
      String xml = pubXml.getXmlData();
      if (StringUtils.isBlank(xml))
        return;
      PubXmlDocument xmlDocument = new PubXmlDocument(xml);
      /**
       * <pre>
       * // 全文节点
       * Element fullText = (Element) xmlDocument.getNode(PubXmlConstants.PUB_FULLTEXT_XPATH);
       * if (fullText != null) {
       *   fullText.addAttribute(&quot;node_id&quot;, &quot;1&quot;);
       * }
       * // 附件节点
       * List&lt;Node&gt; attachs = xmlDocument.getNodes(PubXmlConstants.PUB_ATTACHMENTS_ATTACHMENT_XPATH);
       * if (attachs != null &amp;&amp; attachs.size() &gt; 0) {
       *   for (Node node : attachs) {
       *     Element attach = (Element) node;
       *     attach.addAttribute(&quot;node_id&quot;, &quot;1&quot;);
       *   }
       * }
       * // 起止页
       * pub.setStartPage(StringUtils
       *     .substring(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, &quot;start_page&quot;), 0, 50));
       * pub.setEndPage(StringUtils
       *     .substring(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, &quot;end_page&quot;), 0, 50));
       * publicationRolDao.save(pub);
       * xml = xmlDocument.getXmlString();
       * publicationXmlService.save(pubId, xml);
       * </pre>
       */
      pub.setEnTitle(StringUtils
          .substring(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_title_text"), 0, 250));
      pub.setZhTitle(StringUtils
          .substring(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title_text"), 0, 250));
      pub.setEnTitleText(StringUtils.substring(
          xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_title_text").toLowerCase(), 0, 250));
      pub.setZhTitleText(StringUtils.substring(
          xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title_text").toLowerCase(), 0, 250));
      this.publicationRolDao.save(pub);
    } catch (Exception e) {
      logger.error("重构导入的成果数据. ", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<PublicationRol> loadRebuildPubId(Long lastPubId, int size) throws ServiceException {
    try {

      return this.publicationRolDao.loadRebuildPubId(lastPubId, size);
    } catch (Exception e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public List<PublicationRol> loadRebuildCiteRecordUrlPubId(Long lastPubId, int size) throws ServiceException {
    try {

      return this.publicationRolDao.loadRebuildCiteRecordUrlPubId(lastPubId, size);
    } catch (Exception e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveRebuildPublication(Long pubId) {
    try {
      PublicationXml pubxml = this.publicationXmlService.getById(pubId);
      this.rolPublicationXmlManager.reBuildXml(pubId, pubxml.getXmlData());
    } catch (Exception e) {
      logger.error("重新构造成果XML，生产Brief,校验数据出错 ", e);
    }
  }

  @Override
  public PublicationRol saveRebuildPublication(PubXmlDocument doc, PubXmlProcessContext context) throws Exception {
    Long pubId = doc.getPubId();
    PublicationRol pub = publicationRolDao.get(pubId);
    if (pub != null) {
      // 重新生成brief
      pub.setBriefDesc(
          StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc"), 0, 500));
      // 重新生成briefEn
      pub.setBriefDescEn(
          StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc_en"), 0, 500));
      // 清除数据库的pub_error_fields,遍历xml中的pub_errors/error,把错误的字段名保存进pub_error_fields
      savePubErrorFields(doc, context);
      // 保存成果
      publicationRolDao.save(pub);
      // 存储XML数据
      String xml = doc.getXmlString();
      // 保存成果XML
      this.publicationXmlService.save(pubId, xml);
    }
    return pub;
  }

  @Override
  public List<PublicationRol> loadRebuildIsiId(Long lastId, int batchSize) throws ServiceException {

    return this.publicationRolDao.loadRebuildIsiId(lastId, batchSize);
  }

  @Override
  public void rebuildPubXMLIsiId(PublicationRol pub) {

    if (StringUtils.isBlank(pub.getIsiId())) {
      return;
    }

    try {
      PublicationXml pubXml = this.publicationXmlService.getById(pub.getId());
      String xml = pubXml.getXmlData();
      if (StringUtils.isBlank(xml))
        return;
      PubXmlDocument xmlDocument = new PubXmlDocument(xml);

      Element pubEle = (Element) xmlDocument.getNode(PubXmlConstants.PUBLICATION_XPATH);
      if (pubEle != null) {
        pubEle.addAttribute("isi_id", pub.getIsiId());
      }
      Element metaEle = (Element) xmlDocument.getNode(PubXmlConstants.PUB_META_XPATH);
      if (metaEle != null) {
        metaEle.addAttribute("isi_id", pub.getIsiId());
      }
      xml = xmlDocument.getXmlString();
      publicationXmlService.save(pub.getId(), xml);
      logger.info("重构isi_id成功");
    } catch (Exception e) {
      logger.error("重构isi_id失败", e);
    }
  }

  @Override
  public IsiPubcacheInsAssign saveIsiPubcacheInsAssign(Long xmlId, Long pubId, Long insId, Integer imported)
      throws ServiceException {

    try {
      return this.isiPubcacheInsAssignDao.saveIsiPubcacheInsAssign(xmlId, pubId, insId, imported);
    } catch (Exception e) {
      logger.error("单位端成果匹配到单位记录 ", e);
      throw new ServiceException(e);
    }

  }

  @Override
  public PubMedPubcacheInsAssign savePubMedPubcacheInsAssign(Long xmlId, Long pubId, Long insId, Integer imported)
      throws ServiceException {

    try {
      return this.pubMedPubcacheInsAssignDao.savePubMedPubcacheInsAssign(xmlId, pubId, insId, imported);
    } catch (Exception e) {
      logger.error("PUBMED单位端成果匹配到单位记录 ", e);
      throw new ServiceException(e);
    }

  }

  @Override
  public CnkiPubcacheInsAssign saveCnkiPubcacheInsAssign(Long xmlId, Long pubId, Long insId, Integer imported)
      throws ServiceException {
    try {
      return this.cnkiPubcacheInsAssignDao.saveCnkiPubcacheInsAssign(xmlId, pubId, insId, imported);
    } catch (Exception e) {
      logger.error("单位端CNKI成果匹配到单位记录 ", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public CniprPubcacheInsAssign saveCniprPubcacheInsAssign(Long xmlId, Long pubId, Long insId, Integer imported)
      throws ServiceException {
    try {
      return this.cniprPubcacheInsAssignDao.saveCniprPubcacheInsAssign(xmlId, pubId, insId, imported);
    } catch (Exception e) {
      logger.error("单位端Cnipr成果匹配到单位记录 ", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public CnkiPatPubcacheInsAssign saveCnkiPatPubcacheInsAssign(Long xmlId, Long pubId, Long insId, Integer imported)
      throws ServiceException {
    try {
      return this.cnkiPatPubcacheInsAssignDao.saveCnkiPatPubcacheInsAssign(xmlId, pubId, insId, imported);
    } catch (Exception e) {
      logger.error("单位端CnkiPat成果匹配到单位记录 ", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public SpsPubcacheInsAssign saveSpsPubcacheInsAssign(Long xmlId, Long pubId, Long insId, Integer imported)
      throws ServiceException {
    try {
      return spsPubcacheInsAssignDao.saveSpsPubcacheInsAssign(xmlId, pubId, insId, imported);
    } catch (Exception e) {
      logger.error("单位端scopus成果匹配到单位记录 ", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public EiPubcacheInsAssign saveEiPubcacheInsAssign(Long xmlId, Long pubId, Long insId, Integer imported)
      throws ServiceException {
    try {
      return this.eiPubcacheInsAssignDao.saveEiPubcacheInsAssign(xmlId, pubId, insId, imported);
    } catch (Exception e) {
      logger.error("单位端EI成果匹配到单位记录 ", e);
      throw new ServiceException(e);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @seecom.iris.scm.rolweb.service.publication.PublicationRolService#
   * validatePubEnter(java.util.Map)
   */
  @Override
  public Boolean validatePubEnter(Map postData) throws ServiceException {
    PubXmlDocument newDoc = null;
    try {
      newDoc = PubXmlDocumentBuilder.build(postData);
    } catch (Exception e1) {
      logger.error(e1.getMessage(), e1);
      return false;
    }
    String xmlStr = newDoc.getXmlString();
    try {
      DocumentHelper.parseText(xmlStr);
    } catch (DocumentException e) {
      logger.error(e.getMessage(), e);
      return false;
    }
    return true;
  }

  @Override
  public void markPubNoDup(Long groupId, List<Long> pubIds) throws ServiceException {

    try {
      Long insId = SecurityUtils.getCurrentInsId();
      publicationRolDao.markPubNoDup(pubIds, SecurityUtils.getCurrentInsId());
      this.publicationRolDao.updateDupGroup(groupId);
      // 清理缓存中的任务统计数
      taskNoticeService.clearTaskNotice(ClearTaskNoticeEvent.getInstance(insId, 0, 1, 0));
    } catch (Exception e) {
      logger.error("标记成果不重复group_id:" + pubIds, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void fillKpiData(PubFillKpiData fillData) throws ServiceException {

    try {
      Long pubId = fillData.getPubId();
      // 获取成果实体
      PublicationRol pub = this.publicationRolDao.get(pubId);
      // 不是某人成果，先执行指派删除操作，因其要执行XML操作
      if (fillData.getNotPsnPub() != null) {
        this.pubRolPersonService.removeAssignByPubPsn(pubId, fillData.getNotPsnPub());
      }
      // 获取XML
      PublicationXml pubXml = this.publicationXmlService.getById(pubId);
      PubXmlDocument pubXmlDoc = new PubXmlDocument(pubXml.getXmlData());
      // 重构日期
      String publishDate = fillData.getPublishDate();
      String preYear = ObjectUtils.toString(pub.getPublishYear());
      String preMonth = ObjectUtils.toString(pub.getPublishMonth());
      String prePublishDate = preYear + "-" + preMonth;
      // 如果已经修改
      if (!prePublishDate.equals(publishDate)) {
        this.rolPublicationXmlManager.reBuildPublishDate(pubXmlDoc, publishDate, pub.getCreatePsnId(), pub.getInsId());
        this.rolPublicationXmlManager.prasePublishDate(pubXmlDoc, pub);
      }

      // 重构引用情况
      if (pub.getTypeId().equals(ConstPublicationType.PUB_CONFERECE_TYPE)
          || pub.getTypeId().equals(ConstPublicationType.PUB_JOURNAL_TYPE)
          || pub.getTypeId().equals(ConstPublicationType.PUB_OTHERS_TYPE)) {
        Map<String, String> pubList = new HashMap<String, String>();
        pubList.put("list_sci", String.valueOf(fillData.getListSci() != null && fillData.getListSci() == 1 ? 1 : 0));
        pubList.put("list_ei", String.valueOf(fillData.getListEi() != null && fillData.getListEi() == 1 ? 1 : 0));
        pubList.put("list_istp", String.valueOf(fillData.getListIstp() != null && fillData.getListIstp() == 1 ? 1 : 0));
        pubList.put("list_ssci", String.valueOf(fillData.getListSsci() != null && fillData.getListSsci() == 1 ? 1 : 0));
        this.rolPublicationXmlManager.reBuildInsPubList(pubXmlDoc, pubList);
        this.publicationListRolService.prasePubList(pubXmlDoc);
      }
      // 重构作者
      this.rebuildFillKpiPm(pubXmlDoc, pub, fillData.getPmType(), fillData.getPubMembers());
      // 重构作者列表
      this.rolPublicationXmlManager.praseAuthorNames(pubXmlDoc, pub);
      // 重构生成简要描述（页面表格的来源列）
      this.rolPublicationXmlManager.rebuildPublicationBrief(pubXmlDoc, pub);

      this.publicationRolDao.save(pub);
      // 添加成果统计冗余
      kpiRefreshPubService.addPubRefresh(pub, false);
      this.publicationXmlService.save(pubId, pubXmlDoc.getXmlString());
      // 更新成果html
      this.pubInfoHtmlBuilder.buildHtml(pubXmlDoc);
    } catch (Exception e) {
      logger.error("补充成果KPI完整性", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void pendingConfirm(PubFillKpiData fillData) throws ServiceException {
    try {
      Long pubId = fillData.getPubId();
      // 获取成果实体
      PublicationRol pub = this.publicationRolDao.get(pubId);
      pub.setStatus(PublicationRolStatusEnum.APPROVED);
      // 获取XML
      PublicationXml pubXml = this.publicationXmlService.getById(pubId);
      PubXmlDocument pubXmlDoc = new PubXmlDocument(pubXml.getXmlData());
      // 重构作者
      this.rebuildFillKpiPm(pubXmlDoc, pub, fillData.getPmType(), fillData.getPubMembers());
      // 重构作者列表
      this.rolPublicationXmlManager.praseAuthorNames(pubXmlDoc, pub);
      // 重构生成简要描述（页面表格的来源列）
      this.rolPublicationXmlManager.rebuildPublicationBrief(pubXmlDoc, pub);
      // 修改为发布状态
      pub.setIsOpen(1);
      this.publicationRolDao.save(pub);
      this.publicationXmlService.save(pubId, pubXmlDoc.getXmlString());
      // 刷新kpi
      this.kpiRefreshPubService.addPubRefresh(pubId, false);
      // 刷新查重表数据
      this.rolPubDupService.setPubCanDup(pubId, true);
      // 更新成果html
      this.pubInfoHtmlBuilder.buildHtml(pubXmlDoc);
    } catch (Exception e) {
      logger.error("pendingConfirm出现异常！", e);
      throw new ServiceException("pendingConfirm出现异常！", e);
    }
  }

  /**
   * // 设置CitedList.
   * 
   * @param pub
   * @param publist
   */
  private void setPubCitedList(PublicationRol pub, PublicationListRol publist) {
    // 收录情况冗余字段
    if (publist != null) {
      if ((publist.getListIstp() != null && publist.getListIstp() > 0)
          || (publist.getListSci() != null && publist.getListSci() > 0)
          || (publist.getListSsci() != null && publist.getListSsci() > 0)) {
        pub.setCitedList("ISI");
      }
    }
    if (StringUtils.isNotBlank(pub.getCitedList()) && !"ISI".equalsIgnoreCase(pub.getCitedList()))
      pub.setCitedList("");
  }

  /**
   * 重构KPI完整性作者.
   * 
   * @param kpiPmList
   * @throws ServiceException
   */
  private void rebuildFillKpiPm(PubXmlDocument pubXmlDoc, PublicationRol pub, Integer pmType,
      List<PubFillKpiMember> kpiPmList) throws ServiceException {

    try {

      if (kpiPmList == null || kpiPmList.size() == 0) {
        return;
      }

      Long currentInsId = pub.getInsId();
      String currentInsName = this.institutionRolService.getInsName(currentInsId);
      for (PubFillKpiMember kpiMember : kpiPmList) {
        this.rebuildFillKpiPm(pubXmlDoc, pub, pmType, kpiMember, currentInsId, currentInsName);
      }
    } catch (Exception e) {
      logger.error("重构KPI完整性作者出错 ", e);
      throw new ServiceException(e);
    }
  }

  public void rebuildFillKpiPm(PubXmlDocument pubXmlDoc, PublicationRol pub, Integer pmType, PubFillKpiMember kpiMember,
      Long currentInsId, String currentInsName) throws ServiceException {

    try {
      Long pubId = pub.getId();
      Element em = null;
      // 添加作者
      if (pmType != null && pmType == 2) {
        if (StringUtils.isBlank(kpiMember.getPmName()) || kpiMember.getPmPsnId() == null) {
          return;
        }
        Element pmEm = (Element) pubXmlDoc.getNode(PubXmlConstants.PUB_MEMBERS_XPATH);
        if (pmEm == null) {
          pmEm = pubXmlDoc.createElement(PubXmlConstants.PUB_MEMBERS_XPATH);
        }
        em = pmEm.addElement("pub_member");
      } else {
        em = (Element) pubXmlDoc
            .getNode(PubXmlConstants.PUB_MEMBERS_MEMBER_XPATH + "[@pm_id=" + kpiMember.getPmId() + "]");
      }
      if (em == null) {
        return;
      }
      PubMemberRol pm = null;
      // 添加作者
      if (pmType != null && pmType == 2) {
        pm = new PubMemberRol();
        pm.setInsCount(0);
        pm.setInsId(SecurityUtils.getCurrentInsId());
        pm.setIsConfirm(1);
        pm.setName(kpiMember.getPmName());
        pm.setPsnId(kpiMember.getPmPsnId());
        pm.setPubId(pubId);
        pm.setSeqNo(1);
        // 保存数据
        pubRolMemberDao.savePubMember(pm);
        kpiMember.setPmId(pm.getId());
        // 获取ID，名称
        em.addAttribute("pm_id", pm.getId().toString());
        em.addAttribute("member_psn_name", kpiMember.getPmName());

      } else {
        pm = pubRolMemberDao.getPubMemberById(kpiMember.getPmId());

        // 人员没有发生变化
        if (pm.getPsnId() != null && pm.getPsnId().equals(kpiMember.getPmPsnId())) {
          return;
        }
        // // 前后人员都为空，单位也没发生变化
        // if ((pm.getPsnId() == null && kpiMember.getPmPsnId() == null)
        // && ((pm.getPmInsId() == null && kpiMember.getInsId() == null)
        // || (pm.getPmInsId() != null && pm
        // .getPmInsId().equals(kpiMember.getInsId())))) {
        // return;
        // }
        // 指派了新的单位人员、或者删除了之前的指派关系，删除之前用户的指派
        if (pm.getPsnId() != null && !pm.getPsnId().equals(kpiMember.getPmPsnId())) {
          this.psnPubStatSyncService.removePubPsn(pubId, currentInsId, pm.getPsnId());
        }
      }

      Long cpmPsn = kpiMember.getPmPsnId();
      Long selectInsId = kpiMember.getInsId();
      // 作者ID不为空
      if (cpmPsn != null) {
        // 设置机构ID为当前机构ID
        selectInsId = currentInsId;
        // 成果为已确认
        pm.setIsConfirm(1);
        pm.setPsnId(cpmPsn);
        em.addAttribute("member_psn_id", cpmPsn.toString());
        em.addAttribute("member_psn_acname", kpiMember.getAcPsnName());
      } else {// 清空作者
        // 成果为已确认
        pm.setIsConfirm(0);
        pm.setPsnId(null);
        em.addAttribute("member_psn_id", "");
        em.addAttribute("member_psn_acname", "");
      }
      // 选择了其他机构,还原机构ID为智能匹配上的机构ID
      if (selectInsId != null && selectInsId == 1) {
        Long matchInsId = IrisNumberUtils.createLong(em.attributeValue("matched_ins_id"));
        if (matchInsId != null && !matchInsId.equals(currentInsId)) {
          selectInsId = matchInsId;
        }
      }
      em.addAttribute("ins_id", selectInsId == null ? "" : selectInsId.toString());
      // 作者所在机构
      pm.setPmInsId(selectInsId);
      // 如果不是本机构，则清空指派数据
      if (!currentInsId.equals(selectInsId) && selectInsId != null) {
        // 删除关联到的人员
        this.psnPubStatSyncService.removePubPsnByPmId(pubId, pm.getId());
      }
      // 机构单位名,所在省市
      if (pm.getPmInsId() != null && pm.getPmInsId() != 1) {

        // 机构单位名
        String insName = "";
        if (pm.getPmInsId().equals(currentInsId)) {
          insName = currentInsName;
        } else {
          insName = StringUtils.trimToEmpty(this.institutionRolService.getInsName(pm.getPmInsId()));
        }
        em.addAttribute("ins_name", insName);
        // 所在省市
        InsRegion region = insRegionDao.get(pm.getPmInsId());
        if (region != null) {
          pm.setPmCity(region.getCyId());
          pm.setPmPrv(region.getPrvId());
          pm.setPmDis(region.getDisId());
        } else {
          pm.setPmCity(null);
          pm.setPmPrv(null);
          pm.setPmDis(null);
        }
      } else {// 清空单位名称
        em.addAttribute("ins_name", "");
        pm.setPmCity(null);
        pm.setPmPrv(null);
      }

      // 清空部门，让KPI刷新程序去更新
      em.addAttribute("unit_id", "");
      em.addAttribute("parent_unit_id", "");
      pm.setUnitId(null);
      pm.setParentUnitId(null);

      if (pm.getPsnId() != null) {
        // 保存人员、成果、部门之间的关系，只有成果是已批准状态
        psnPubStatSyncService.addPubPsn(pubId, currentInsId, pm.getPsnId(), PubAssignModeEnum.MANUALLY, false,
            kpiMember.getPmId());
      }
      // 保存数据
      pubRolMemberDao.savePubMember(pm);
    } catch (Exception e) {
      logger.error("重构KPI完整性作者出错 ", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<PubMemberRol> getPubMembersByPubId(Long pubId) throws ServiceException {
    try {
      return pubRolMemberDao.getPubMembersByPubId(pubId);
    } catch (DaoException e) {
      logger.error("根据pubId查询PubMemberRol出错 ", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Map<String, List<Long>> confirmPsnPubRelation(List<Long> pubIds, Long psnId) throws ServiceException {

    List<PubPsnRol> list = this.pubPsnRolDao.getPubPsn(pubIds, psnId);
    List<Long> successList = new ArrayList<Long>();
    List<Long> errorList = new ArrayList<Long>();
    Map<String, List<Long>> map = new HashMap<String, List<Long>>();
    map.put("success", successList);
    map.put("error", errorList);
    Long currentInsId = SecurityUtils.getCurrentInsId();
    String currentInsName = this.institutionRolService.getInsName(currentInsId);
    try {
      for (PubPsnRol pubPsn : list) {
        if (pubPsn.getAuthorPMId() == null) {
          errorList.add(pubPsn.getPubId());
          continue;
        }
        Long pubId = pubPsn.getPubId();
        // 获取XML
        PublicationXml pubXml = this.publicationXmlService.getById(pubId);
        PubXmlDocument pubXmlDoc = new PubXmlDocument(pubXml.getXmlData());

        // 获取成果实体
        PublicationRol pub = this.publicationRolDao.get(pubId);
        PubFillKpiMember kpiMember = new PubFillKpiMember(pubPsn.getAuthorPMId(), currentInsId, psnId);
        // 1创建关联，2添加作者
        this.rebuildFillKpiPm(pubXmlDoc, pub, 1, kpiMember, currentInsId, currentInsName);
        // 重构作者列表
        this.rolPublicationXmlManager.praseAuthorNames(pubXmlDoc, pub);
        this.publicationRolDao.save(pub);
        // 添加成果统计冗余
        this.kpiRefreshPubService.addPubRefresh(pub, false);
        this.publicationXmlService.save(pubId, pubXmlDoc.getXmlString());
        // 更新成果html
        this.pubInfoHtmlBuilder.buildHtml(pubXmlDoc);
        successList.add(pubPsn.getPubId());
      }
    } catch (Exception e) {
      logger.error(" 确认人员与成果的关联关系出错 ", e);
      throw new ServiceException(e);
    }
    return map;
  }

  @Override
  public void createPsnPubRelation(PubPsnCreateRelation relation) throws ServiceException {

    Long currentInsId = SecurityUtils.getCurrentInsId();
    String currentInsName = this.institutionRolService.getInsName(currentInsId);

    try {

      Long psnId = relation.getPsnId();
      Long pubId = relation.getPubId();

      // 获取XML
      PublicationXml pubXml = this.publicationXmlService.getById(pubId);
      PubXmlDocument pubXmlDoc = new PubXmlDocument(pubXml.getXmlData());
      // 获取成果实体
      PublicationRol pub = this.publicationRolDao.get(pubId);

      String acPsnName = this.insPersonService.getPsnUnitNameCompose(currentInsId, psnId);
      if (acPsnName == null) {
        return;
      }
      // 1创建关联，2添加作者
      if (relation.getType() == 1) {
        PubFillKpiMember kpiMember =
            new PubFillKpiMember(relation.getPmId(), currentInsId, relation.getPsnId(), acPsnName);
        this.rebuildFillKpiPm(pubXmlDoc, pub, 1, kpiMember, currentInsId, currentInsName);
      } else if (relation.getType() == 2) {
        PubFillKpiMember kpiMember =
            new PubFillKpiMember(relation.getMemberName(), currentInsId, relation.getPsnId(), acPsnName);
        this.rebuildFillKpiPm(pubXmlDoc, pub, 2, kpiMember, currentInsId, currentInsName);
      }
      // 重构作者列表
      this.rolPublicationXmlManager.praseAuthorNames(pubXmlDoc, pub);
      this.publicationRolDao.save(pub);
      // 添加成果统计冗余
      this.kpiRefreshPubService.addPubRefresh(pub, false);
      this.publicationXmlService.save(pubId, pubXmlDoc.getXmlString());

    } catch (Exception e) {
      logger.error(" 建立人员与成果的确认关联关系出错 ", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Object[]> getFilterNotDelPubSource(Collection<Long> pubIds) throws ServiceException {

    try {
      return this.publicationRolDao.getFilterNotDelPubId(pubIds);
    } catch (Exception e) {
      logger.error(" 过滤出不是删除状态的成果ID ", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public String getPubXmlById(Long pubId) throws ServiceException {
    String xmlstr = "";
    try {
      PublicationXml pubxml = this.publicationXmlService.getById(pubId);
      if (pubxml != null)
        xmlstr = pubxml.getXmlData();
    } catch (Exception e) {
      logger.error(" 根据成果ID：{} 获取xml出错", pubId, e);
      throw new ServiceException(e);
    }
    return xmlstr;
  }

  @Override
  public List findPubIdsBatch(Long lastId, int batchSize) throws ServiceException {
    try {
      return this.publicationRolDao.findPubIdsBatch(lastId, batchSize);
    } catch (DaoException e) {
      logger.error("批量获取成果Id出错，lastId=" + lastId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Long briefDescTask(Long pubId) throws ServiceException {
    PublicationRol publication = publicationRolDao.get(pubId);
    if (publication != null) {
      try {
        Long jid = publication.getJid();
        if (jid == null)
          return pubId;
        Long jnlId = journalService.getMatchBaseJnlId(jid);
        if (jnlId == pubId)
          return pubId;
        // 根据匹配的基础期刊，重构sns期刊名称
        BaseJournalSearch bjnl = journalService.getBaseJournalById(jnlId);
        if (bjnl == null)
          return pubId;
        PubXmlProcessContext context = new PubXmlProcessContext();
        context.setCurrentPubId(publication.getId());
        context.setCurrentUserId(SecurityUtils.getCurrentUserId());
        PubXmlDocument xmlDoc = new PubXmlDocument(this.getPubXmlById(publication.getId()));
        xmlDoc.setXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "zh_name",
            StringUtils.trimToEmpty(bjnl.getTitleXx()));
        xmlDoc.setXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "en_name",
            StringUtils.trimToEmpty(bjnl.getTitleEn()));
        String jname = xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jname");
        String zhName = xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "zh_name");
        String enName = xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "en_name");
        // 期刊名称显示英文还是中文，要取决于来源数据库是英文库还是中文库。
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(jname);// 如果zhSourceDbName中包含有中文字符则表明是中文
        if (m.find()) {
          jname = StringUtils.isBlank(zhName) ? enName : zhName;
        } else {
          jname = StringUtils.isBlank(enName) ? zhName : enName;
        }
        xmlDoc.setXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jname", jname);

        String briefZh = rolPublicationXmlManager.getLanguagesBrief(LocaleUtils.toLocale("zh_CN"), context, xmlDoc,
            publication.getTypeId());
        if (StringUtils.isNotBlank(briefZh)) {
          xmlDoc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc", briefZh);
          publication.setBriefDesc(briefZh);
        }

        String briefEn = rolPublicationXmlManager.getLanguagesBrief(LocaleUtils.toLocale("en_US"), context, xmlDoc,
            publication.getTypeId());

        if (StringUtils.isNotBlank(briefEn)) {
          xmlDoc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc_en", briefEn);
          publication.setBriefDescEn(briefEn);
        }
        this.publicationXmlService.save(publication.getId(), xmlDoc.getXmlString());
        publicationRolDao.save(publication);
        publicationRolDao.updatePubBriefDescTaskStatus(pubId, 1);
        pubId = publication.getId();
      } catch (Exception e) {
        logger.error("重构brief_desc出错,pubId:{}", publication.getId(), e);
        throw new ServiceException(e);
      }
    }
    return pubId;

  }

  @Override
  public List<Long> findAllPubIdsBatch(Long lastId, int size) throws ServiceException {

    try {
      return this.publicationRolDao.findAllPubIdsBatch(lastId, size);
    } catch (Exception e) {
      logger.error("查找所有成果ID", e);
      throw new ServiceException("查找所有成果ID", e);
    }
  }

  @Override
  public List<Map<String, Long>> getPubCountByUnitId(List<Long> unitIdList, Long superUnitId, Long insId)
      throws ServiceException {
    try {
      List<Map<String, Long>> unitPubCount = new ArrayList<Map<String, Long>>();
      if (CollectionUtils.isNotEmpty(unitIdList)) {
        Long count = 0L;
        Map<String, Long> map = null;
        for (Long unitId : unitIdList) {
          map = new HashMap<String, Long>();
          count = this.publicationRolDao.queryPubCountByUnitId(unitId, insId);
          map.put("unitId", unitId);
          map.put("pubCount", count);
          unitPubCount.add(map);
        }
      }
      return unitPubCount;
    } catch (Exception e) {
      logger.error("查询一级部门或者二级部门的成果总数出现异常：", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Map<String, Long> queryPubOtherCount(Long insId, Long parentUnitId) throws ServiceException {
    try {
      Map<String, Long> map = new HashMap<String, Long>();
      Long count = this.publicationRolDao.queryPubOtherCount(insId, parentUnitId);
      map.put("unitId", -1l);
      map.put("pubCount", count);

      return map;
    } catch (Exception e) {
      logger.error("其他成果总数出现异常：insId=" + insId + ",parentUnitId=" + parentUnitId, e);
      throw new ServiceException("其他成果总数出现异常insId=" + insId + ",parentUnitId=" + parentUnitId, e);
    }
  }

  @Override
  public Map<String, Long> queryPubCountAllByUnitId(Long unitId, Long insId) throws ServiceException {
    try {
      Map<String, Long> map = new HashMap<String, Long>();
      Long count = this.publicationRolDao.queryPubCountAllByUnitId(unitId, insId);
      map.put("unitId", 0l);
      map.put("pubCount", count);

      return map;
    } catch (Exception e) {
      logger.error("全部成果总数出现异常：insId=" + insId + ",unitId=" + unitId, e);
      throw new ServiceException("全部成果总数出现异常insId=" + insId + ",unitId=" + unitId, e);
    }
  }

  @Override
  public List<Map<String, Long>> getPubCountByPsnId(List<Long> psnIdList, Long insId) throws ServiceException {
    try {
      return this.publicationRolDao.queryPubCountByPsnId(psnIdList, insId);
    } catch (Exception e) {
      logger.error("查询人员成果总数出现异常：", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public boolean isPubMemberLinkPsn(Long pubId, Long insPsnId) throws ServiceException {
    try {
      List<PubMemberRol> list = pubRolMemberDao.getPubMembersByPubId(pubId);
      if (CollectionUtils.isNotEmpty(list)) {
        for (PubMemberRol pubMemberRol : list) {
          if (pubMemberRol.getPsnId() != null && pubMemberRol.getPsnId().equals(insPsnId)) {
            return true;
          }
        }
      }
    } catch (Exception e) {
      logger.error("查询人员是否已经关联了成果作者出现异常：", e);
    }
    return false;
  }

  /**
   * 保存全文.
   * 
   * @param pub
   */
  private void savePubFulltext(PublicationRol pub) {
    try {
      Integer fulltextNode =
          pub.getFulltextNodeId() == null ? SecurityUtils.getCurrentAllNodeId().get(0) : pub.getFulltextNodeId();
      this.pubFulltextRolService.dealPubFulltext(pub.getId(),
          StringUtils.isNotBlank(pub.getFulltextFileid()) ? NumberUtils.createLong(pub.getFulltextFileid()) : null,
          fulltextNode, pub.getFulltextExt());
    } catch (Exception e) {
      logger.error("保存成果pubId={}全文fulltextFileId={}数据出错：{}", pub.getId(), pub.getFulltextFileid(), e);
    }
  }

  @Override
  public String getUpdatePubCitedParams(List<Long> pubIds) throws ServiceException {
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

  @SuppressWarnings("rawtypes")
  @Override
  public void updatePubCitedTimes(List<Long> pubIds, String citedXml) throws ServiceException {
    try {
      Document citedDoc = null;
      try {
        citedDoc = DocumentHelper.parseText(citedXml);
      } catch (DocumentException e) {
        logger.error("xml格式错误,pubIds:{},citedXml:{}", new Object[] {pubIds, citedXml});
        return;
      }
      List nodes = citedDoc.selectNodes("//data/tc");
      if (CollectionUtils.isEmpty(nodes) || pubIds.size() != nodes.size())
        return;
      for (int i = 0; i < pubIds.size(); i++) {
        Long pubId = pubIds.get(i);
        Element em = (Element) nodes.get(i);
        Integer citedTimes = IrisNumberUtils.createInteger(em.attributeValue("count"));
        String citationIndex = em.attributeValue("citation_index");// 更新引用库

        if (citedTimes != null && citedTimes > 0) {
          PubXmlDocument pubDoc = new PubXmlDocument(this.getPubXmlById(pubId));
          if (pubDoc.existsNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cite_times")) {
            pubDoc.setNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cite_times", citedTimes.toString());
          } else {
            pubDoc.setNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cite_times", citedTimes.toString());
          }
          pubDoc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cited_list", "ISI");
          this.publicationXmlService.save(pubId, pubDoc.getXmlString());
          PublicationRol publication = publicationRolDao.get(pubId);
          publication.setCitedTimes(citedTimes);
          publication.setCitedList("ISI");
        }

        // 针对没有收录情况的成果更新引用时，如果更新次数大于0，则自动将该成果收录到成功更新引用的isi分库，如：ssci
        if (StringUtils.isNotBlank(citationIndex) && citedTimes != null && citedTimes > 0) {
          PublicationListRol pubList = publicationListRolService.getPublicationList(pubId);
          if ("SCI".toLowerCase().equalsIgnoreCase(citationIndex)
              || "SCIE".toLowerCase().equalsIgnoreCase(citationIndex)) {
            pubList.setListSci(1);
          } else if ("SSCI".toLowerCase().equalsIgnoreCase(citationIndex)) {
            pubList.setListSsci(1);
          } else if ("ISTP".toLowerCase().equalsIgnoreCase(citationIndex)) {
            pubList.setListIstp(1);
          }
          publicationListRolService.savePublictionList(pubList);
        }
      }
    } catch (Exception e) {
      logger.error("", e);
    }
  }

  @Override
  public Map<String, Object> getShowPubData(Long pubId, String domain) throws ServiceException {
    try {
      Map<String, Object> map = null;
      PublicationRol publication = this.publicationRolDao.get(pubId);
      if (publication != null) {
        map = new HashMap<String, Object>();
        map.put("id", pubId);
        map.put("des3Id", ServiceUtil.encodeToDes3(pubId.toString()));
        String title = null;
        String source = "";
        String briefDesc = StringUtils.replace(publication.getBriefDesc(), ">", "&gt;");
        briefDesc = StringUtils.replace(publication.getBriefDesc(), "<", "&lt;");
        briefDesc = StringUtils.trimToEmpty(briefDesc);
        String briefDescEn = StringUtils.replace(publication.getBriefDescEn(), ">", "&gt;");
        briefDescEn = StringUtils.replace(publication.getBriefDescEn(), "<", "&lt;");
        briefDescEn = StringUtils.trimToEmpty(briefDescEn);
        String locale = LocaleContextHolder.getLocale().toString();
        if ("zh_CN".equals(locale)) {
          title = StringUtils.isBlank(publication.getZhTitle()) ? publication.getEnTitle() : publication.getZhTitle();
          source = StringUtils.isBlank(briefDesc) ? briefDescEn : briefDesc;
        } else {
          title = StringUtils.isBlank(publication.getEnTitle()) ? publication.getZhTitle() : publication.getEnTitle();
          source = (StringUtils.isBlank(briefDescEn) ? briefDesc : briefDescEn) + " ";
        }
        map.put("authorNames", XmlUtil.formateSymbolAuthors(title, publication.getAuthorNames()));
        map.put("title", title);
        source = XmlUtil.formateSymbol(title, source);
        map.put("resOther", source);
        map.put("language", XmlUtil.isChinese(title) ? "zh" : "en");
        map.put("resLink", domain + "/publication/view?des3Id=" + ServiceUtil.encodeToDes3(pubId.toString()) + ","
            + BasicRmtSrvModuleConstants.SIE_MODULE_ID);
        map.put("pic", this.pubFulltextRolService.getFulltextImageUrl(pubId));
      }

      return map;
    } catch (Exception e) {
      logger.error("获取成果分享内容出错，pubId=" + pubId, e);
      throw new ServiceException("获取成果分享内容出错，pubId=" + pubId, e);
    }
  }

  @Override
  public List<PublicationRol> getPublicationByIds(List<Long> pubIds) throws ServiceException {
    List<PublicationRol> pubList = publicationRolDao.queryPubByIds(pubIds, PublicationArticleType.OUTPUT);
    this.wrapPubsHtmlAbstract(pubList);
    return pubList;
  }

  private void wrapPubsHtmlAbstract(List<PublicationRol> pubList) throws ServiceException {
    if (CollectionUtils.isEmpty(pubList)) {
      return;
    }
    Locale locale = LocaleContextHolder.getLocale();
    this.warpPubHtmlAbstract(pubList, PubHtmlContants.PUB_LIST_TEM_CODE, locale);
  }

  /**
   * 获取成果Html
   * 
   * @param page
   * @throws ServiceException
   */
  private void warpPubHtmlAbstract(List<PublicationRol> pubList, Integer tempCode, Locale locale)
      throws ServiceException {
    if (pubList == null || CollectionUtils.isEmpty(pubList)) {
      return;
    }
    for (PublicationRol pubRol : pubList) {
      PubHtml pubHtml = this.pubHtmlService.findPubHtml(pubRol.getId(), tempCode);
      if (pubHtml == null) {
        continue;
      }
      if ("en".equalsIgnoreCase(locale.getLanguage())) {
        pubRol.setHtmlAbstract(pubHtml.getHtmlEn());
      } else {
        pubRol.setHtmlAbstract(pubHtml.getHtmlZh());
      }
    }
  }

  @Override
  public PdwhPubcacheInsAssign savePdwhPubCacheInsAssign(Long xmlId, Long pubId, Long insId, int imported)
      throws ServiceException {
    try {
      return this.pdwhPubcacheInsAssignDao.savaPdwhPubcacheInsAssign(xmlId, pubId, insId, imported);
    } catch (Exception e) {
      logger.error("单位端EI成果匹配到单位记录 ", e);
      throw new ServiceException(e);
    }

  }

  @Override
  public List<PublicationRol> getPublicationList(int batchSize, Long lastPubId) {
    return publicationRolDao.getPublicationList(batchSize, lastPubId);
  }
}
