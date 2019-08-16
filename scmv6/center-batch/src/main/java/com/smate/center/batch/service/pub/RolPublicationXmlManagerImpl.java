package com.smate.center.batch.service.pub;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.center.batch.chain.pub.DateAttributeCleanTask;
import com.smate.center.batch.chain.pub.PublicationBriefGenerateTask;
import com.smate.center.batch.constant.PublicationRolStatusEnum;
import com.smate.center.batch.dao.pdwh.pubimport.PdwhPubSourceDbDao;
import com.smate.center.batch.dao.rol.psn.RolPsnInsDao;
import com.smate.center.batch.dao.rol.pub.InsUnitDao;
import com.smate.center.batch.dao.rol.pub.PublicationRolDao;
import com.smate.center.batch.enums.pub.PublicationEnterFormEnum;
import com.smate.center.batch.enums.pub.XmlOperationEnum;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.PublicationNotFoundException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.factory.pub.BriefDriverFactory;
import com.smate.center.batch.factory.pub.XmlValidatorFactory;
import com.smate.center.batch.model.pdwh.pub.Journal;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPubSourceDb;
import com.smate.center.batch.model.rcmd.pub.PubConfirmSyncMessage;
import com.smate.center.batch.model.rol.InsConfirm;
import com.smate.center.batch.model.rol.psn.RolPsnIns;
import com.smate.center.batch.model.rol.pub.InsUnit;
import com.smate.center.batch.model.rol.pub.InstitutionRol;
import com.smate.center.batch.model.rol.pub.PubMemberRol;
import com.smate.center.batch.model.rol.pub.PublicationRol;
import com.smate.center.batch.model.rol.pub.PublicationRolForm;
import com.smate.center.batch.model.rol.pub.RolPubXml;
import com.smate.center.batch.model.sns.pub.IPubXmlServiceFactory;
import com.smate.center.batch.model.sns.pub.PubDupFields;
import com.smate.center.batch.model.sns.pub.PubDupParam;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.model.sns.pub.PublicationXml;
import com.smate.center.batch.model.sns.pub.SettingPubForm;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.process.pub.IPubXmlProcess;
import com.smate.center.batch.service.institution.InsConfirmService;
import com.smate.center.batch.service.pdwh.pub.JournalService;
import com.smate.center.batch.service.psn.InsPersonService;
import com.smate.center.batch.service.pub.mq.CniprPubCacheAssignMessage;
import com.smate.center.batch.service.pub.mq.CnkiPatPubCacheAssignMessage;
import com.smate.center.batch.service.pub.mq.CnkiPubCacheAssignMessage;
import com.smate.center.batch.service.pub.mq.EiPubCacheAssignMessage;
import com.smate.center.batch.service.pub.mq.IsiPubCacheAssignMessage;
import com.smate.center.batch.service.pub.mq.PdwhPubCacheAssignMessage;
import com.smate.center.batch.service.pub.mq.PubMedPubCacheAssignMessage;
import com.smate.center.batch.service.pub.mq.SpsPubCacheAssignMessage;
import com.smate.center.batch.service.rol.pub.AutoCompleteRolService;
import com.smate.center.batch.service.rol.pub.InsUnitRolService;
import com.smate.center.batch.service.rol.pub.InstitutionRolService;
import com.smate.center.batch.service.rol.pub.JnlOATypeRefreshService;
import com.smate.center.batch.service.rol.pub.PubFundInfoRolService;
import com.smate.center.batch.service.rol.pub.PubRolMemberDao;
import com.smate.center.batch.service.rol.pub.PublicationListRolService;
import com.smate.center.batch.service.rol.pub.PublicationRolService;
import com.smate.center.batch.service.rol.pub.RolPubDupService;
import com.smate.center.batch.service.rol.pub.RolPubXmlService;
import com.smate.center.batch.util.pub.ConstPublicationType;
import com.smate.center.batch.util.pub.ImportPubXmlUtils;
import com.smate.center.batch.util.pub.PubConstFieldRefresh;
import com.smate.center.batch.util.pub.PubXmlDbUtils;
import com.smate.center.batch.util.pub.PubXmlDocumentBuilder;
import com.smate.center.batch.util.pub.PublicationArticleType;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.exception.BriefDriverNotFoundException;
import com.smate.core.base.utils.model.security.UserRole;
import com.smate.core.base.utils.pub.BriefFormatter;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.IrisNumberUtils;

/**
 * 成果XML处理服务(导入、修改、新增).
 * 
 * @author yamingd
 */
@Transactional(rollbackFor = Exception.class)
@Service("rolPublicationXmlManager")
public class RolPublicationXmlManagerImpl implements RolPublicationXmlManager {

  /**
   * 
   */
  private Logger logger = LoggerFactory.getLogger(getClass());
  /**
   * 在线导入XML.
   */
  private IPubXmlProcess rolXmlOnlineImportProcess;

  /**
   * 保存XMl.
   */
  // private IPubXmlProcess rolXmlSaveProcess;
  @Autowired
  private IPubXmlProcess scholarXmlSaveProcess;
  /**
   * 成果xml同步.
   */
  private IPubXmlProcess pubXmlSyncOldProcess;

  /**
   * 后台导入XMl.
   */
  private IPubXmlProcess rolXmlBackgroundImportProcess;
  /**
   * 同步SNS成果XMl到单位ROL.
   */
  private IPubXmlProcess rolXmlSyncProcess;
  // 转换导入XML为标准XML.
  private IPubXmlProcess importXmlTranslateProcess;
  // 重新构造成果XML，生产Brief,校验
  private IPubXmlProcess rolPubRebuildProcess;
  /**
   * Xml校验工厂.
   */
  private XmlValidatorFactory xmlValidatorFactory;
  /**
   * 成果Brief生成驱动工厂.
   */
  private BriefDriverFactory briefDriverFactory;
  /**
   * Xml处理需要用到的外部服务.
   */
  @Autowired
  private IPubXmlServiceFactory rolPublicationXmlServiceFactory;

  private String xmlEmailPostfix;

  @Autowired
  private PublicationXmlService publicationXmlService;
  @Autowired
  private RolPsnInsDao rolPsnInsDao;
  @Autowired
  private InsUnitDao insUnitDao;
  @Autowired
  private RolPubDupService rolPubDupService;
  @Autowired
  private PublicationRolDao publicationRolDao;
  @Autowired
  private InstitutionRolService institutionRolService;
  @Autowired
  private RolPubXmlService rolPubXmlService;
  @Autowired
  private PubRolMemberDao pubRolMemberDao;
  @Autowired
  private InsPersonService insPersonService;
  @Autowired
  private JournalService journalService;
  @Autowired
  private InsConfirmService insConfirmService;
  @Autowired
  private PublicationRolService publicationRolService;
  @Autowired
  private SettingPubFormService settingPubFormService;
  @Autowired
  private AutoCompleteRolService autoCompleteRolService;
  @Autowired
  private InsUnitRolService insUnitRolService;
  @Autowired
  private PublicationListRolService publicationListRolService;
  @Autowired
  private JnlOATypeRefreshService jnlOATypeRefreshService;
  @Autowired
  private PubFundInfoRolService pubFundInfoService;
  @Autowired
  private PdwhPubSourceDbDao pdwhPubSourceDbDao;

  @SuppressWarnings("rawtypes")
  private void backImportMergePubAuhtors(PubXmlDocument xmlDocument, PubXmlDocument preDoc) throws Exception {

    List authorList = xmlDocument.getPubAuthorList();
    if (authorList != null && authorList.size() > 0) {
      preDoc.removeNode("/pub_authors");
      Element authorEles = preDoc.createElement("/pub_authors");
      for (int i = 0; i < authorList.size(); i++) {
        Element newAuthor = authorEles.addElement("author");
        Element author = (Element) authorList.get(i);
        preDoc.copyPubElement(newAuthor, author);
      }
    }

  }

  /**
   * 配置xml处理上下文.
   * 
   * @param action
   * @param pubTypeId
   * @param currentUserId
   * @param currentInsId
   * @return
   */
  @Override
  public PubXmlProcessContext buildXmlProcessContext(XmlOperationEnum action, int pubTypeId, long currentUserId,
      long currentInsId) {
    PubXmlProcessContext context = new PubXmlProcessContext();
    // 语言
    Locale locale = LocaleContextHolder.getLocale();
    if (locale == null) {
      locale = Locale.CHINESE;
    }
    context.setCurrentLanguage(locale.getLanguage());
    context.setLocale(locale);
    // 类型为成果
    context.setArticleType(PublicationArticleType.OUTPUT);
    // 操作
    context.setCurrentAction(action);
    // 当前人员
    context.setCurrentUserId(currentUserId);
    // 当前单位
    context.setCurrentInsId(currentInsId);
    // 类别
    context.setPubTypeId(pubTypeId);
    // Xml处理需要用到的外部服务
    context.setXmlServiceFactory(this.rolPublicationXmlServiceFactory);
    // Brief生成驱动工厂
    context.setBrifDriverFactory(this.briefDriverFactory);
    // Xml校验者工厂
    context.setXmlValidatorFactory(this.xmlValidatorFactory);
    // XmlEmail拆分后缀
    context.setXmlEmailPostfix(this.xmlEmailPostfix);
    context.setCurrentNodeId(SecurityUtils.getCurrentUserNodeId());
    return context;
  }

  /**
   * 加载成果XML.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public String loadXmlById(Long pubId) throws ServiceException, PublicationNotFoundException {

    try {

      PublicationXml xmlData = publicationXmlService.rolGetById(pubId);
      if (xmlData == null) {
        throw new PublicationNotFoundException(pubId.toString());
      }
      return xmlData.getXmlData();
    } catch (Exception e) {
      logger.error("loadXmlById加载XML错误, pubId=" + pubId, e);
      throw new ServiceException(e);
    }
  }

  /**
   * 保持成果XML.
   * 
   * @param pubId
   * @param xml
   * @throws ServiceException
   */
  public void updatePubXml(Long pubId, String xml) throws ServiceException {

    try {
      this.publicationXmlService.rolSave(pubId, xml);
    } catch (Exception e) {
      logger.error("updatePubXml保持成果XML错误, pubId=" + pubId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void reBuildXml(long pubId, String xmlData) throws ServiceException {

    try {
      if (StringUtils.isBlank(xmlData)) {
        return;
      }
      PublicationRol pub = this.publicationRolDao.get(pubId);
      PubXmlDocument doc = new PubXmlDocument(xmlData);
      String articleNo =
          StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "article_number"), 0, 100);
      Long jid = IrisNumberUtils.createLong(doc.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jid"));
      String volume =
          StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "volume"), 0, 20);
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
      String sourceId = doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "source_id");
      String patentNo = doc.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_no");
      String confName = doc.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "conf_name");
      String issn = doc.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jissn");
      String jname = doc.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jname");
      if (StringUtils.isBlank(isbn)) {
        isbn = StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "issn"), 0, 40);
      }
      // 作者名
      String auNames = StringUtils.join(doc.getPubMemberNames(), ",");
      if (StringUtils.isBlank(sourceId)) {
        sourceId = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "source_id");
      }
      boolean flagXml = false;
      boolean flagPub = false;
      Integer sourceDbId = pub.getSourceDbId();
      if (PubXmlDbUtils.isScopusDb(sourceDbId) && StringUtils.isBlank(spsId) && StringUtils.isNotBlank(sourceId)) {
        spsId = sourceId;
        doc.setNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "sps_id", spsId);
        doc.setNodeAttribute(PubXmlConstants.PUB_META_XPATH, "sps_id", spsId);
        // 之前错把所有source_id当isi_id看了
        if (StringUtils.isNotBlank(isiId) && sourceId.trim().equalsIgnoreCase(isiId.trim())) {
          doc.setNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "isi_id", "");
          doc.setNodeAttribute(PubXmlConstants.PUB_META_XPATH, "isi_id", "");
          pub.setIsiId(null);
          flagPub = true;
        }
        flagXml = true;
      }
      if (PubXmlDbUtils.isEiDb(sourceDbId) && StringUtils.isBlank(eiId) && StringUtils.isNotBlank(sourceId)) {
        eiId = sourceId;
        doc.setNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "ei_id", eiId);
        doc.setNodeAttribute(PubXmlConstants.PUB_META_XPATH, "ei_id", eiId);
        // 之前错把所有source_id当isi_id看了
        if (StringUtils.isNotBlank(isiId) && sourceId.trim().equalsIgnoreCase(isiId.trim())) {
          doc.setNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "isi_id", "");
          doc.setNodeAttribute(PubXmlConstants.PUB_META_XPATH, "isi_id", "");
          pub.setIsiId(null);
          flagPub = true;
        }
        flagXml = true;
      }
      if (PubXmlDbUtils.isIsiDb(sourceDbId) && StringUtils.isBlank(isiId) && StringUtils.isNotBlank(sourceId)) {
        isiId = sourceId;
        doc.setNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "isi_id", isiId);
        doc.setNodeAttribute(PubXmlConstants.PUB_META_XPATH, "isi_id", isiId);
        flagXml = true;
      }
      if (PubXmlDbUtils.isIsiDb(pub.getSourceDbId())
          && (!PubXmlDbUtils.isIsiDb(sourceDbId) || StringUtils.isBlank(isiId) || pub == null)) {
        String citedUrl =
            "http://apps.webofknowledge.com/CitingArticles.do?product=WOS&SID=@SID@&search_mode=CitingArticles&parentProduct=UA&UT=WOS:"
                + isiId;
        doc.removeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cite_record_url");
        doc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cited_url", citedUrl);
        pub.setCitedUrl(StringUtils.substring(citedUrl, 0, 3000));
        flagPub = true;
        flagXml = true;
      }

      Long ownerId = pub.getInsId();
      Integer pubYear = pub.getPublishYear();
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
      param.setAuthorNames(auNames);
      param.setConfName(confName);
      param.setIssn(issn);
      param.setJname(jname);
      boolean canDup = false;
      if (pub.getStatus() != null && PublicationRolStatusEnum.APPROVED == pub.getStatus()) {
        canDup = true;
      }
      this.rolPubDupService.savePubDupFields(param, pubType, pubId, ownerId, pub.getArticleType(), canDup);

      if (flagPub) {
        // 更新成果
        this.publicationRolDao.save(pub);
      }
      if (flagXml) {
        // 更新xml
        publicationXmlService.rolSave(pubId, doc.getXmlString());
      }
    } catch (Exception e) {
      logger.error("重新构造成果XML", e);
      throw new ServiceException("重新构造成果XML", e);
    }

  }

  /**
   *
   */
  @SuppressWarnings("unchecked")
  @Override
  public Long createXml(String newXmlData, int pubTypeId, int articleType) throws ServiceException {

    long currentUserId = SecurityUtils.getCurrentUserId();
    long currentInsId = SecurityUtils.getCurrentInsId();
    PubXmlProcessContext context =
        buildXmlProcessContext(XmlOperationEnum.Enter, pubTypeId, currentUserId, currentInsId);
    try {
      // 创建XML
      PubXmlDocument xmlDocument = new PubXmlDocument(newXmlData);
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "record_ins_id", String.valueOf(currentUserId));
      // xml处理程序链
      this.scholarXmlSaveProcess.start(xmlDocument, context);
      Long pubId = xmlDocument.getPubId();
      return pubId;
    } catch (Exception e) {
      logger.error("保存XML错误", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 保存成果时，如果是新建成果，则将request().getParameterMap()组织成XML数据.
   */
  @SuppressWarnings("unchecked")
  @Override
  @Deprecated
  public Long createXml(Map postData, int pubTypeId, int articleType) throws ServiceException {

    long currentUserId = SecurityUtils.getCurrentUserId();
    long currentInsId = SecurityUtils.getCurrentInsId();
    PubXmlProcessContext context =
        buildXmlProcessContext(XmlOperationEnum.Enter, pubTypeId, currentUserId, currentInsId);
    try {
      // 创建XML
      PubXmlDocument xmlDocument = PubXmlDocumentBuilder.build(postData);
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "record_ins_id", String.valueOf(currentUserId));
      // xml处理程序链
      this.scholarXmlSaveProcess.start(xmlDocument, context);
      Long pubId = xmlDocument.getPubId();
      return pubId;
    } catch (Exception e) {
      logger.error("保存XML错误", e);
      throw new ServiceException(e);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public Long updateXml(long pubId, String newXmlData, int pubTypeId) throws ServiceException {

    long currentUserId = SecurityUtils.getCurrentUserId();
    long currentInsId = SecurityUtils.getCurrentInsId();
    // 配置xml处理上下文
    PubXmlProcessContext context =
        buildXmlProcessContext(XmlOperationEnum.Edit, pubTypeId, currentUserId, currentInsId);

    try {

      PubXmlDocument newDoc = new PubXmlDocument(newXmlData);

      Integer confirmResult =
          IrisNumberUtils.createInteger(newDoc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "confirm_result"));
      confirmResult = confirmResult == null ? 0 : confirmResult;
      context.setConfirmResult(confirmResult);
      context.setCurrentPubId(pubId);
      // read old-pub-meta and copy to xmlDocument
      PublicationXml pubXml = publicationXmlService.rolGetById(pubId);
      PubXmlDocument oldDoc = new PubXmlDocument(pubXml.getXmlData());
      Element oldMeta = (Element) oldDoc.getNode(PubXmlConstants.PUB_META_XPATH);
      newDoc.copyPubElement(oldMeta);
      String[] attrs = PubXmlConstants.PUB_EDIT_REMAIN_PUBLICATION_ATTR;
      newDoc.copyAttributeValue(oldDoc, PubXmlConstants.PUBLICATION_XPATH, PubXmlConstants.PUBLICATION_XPATH, attrs,
          attrs);
      // 对调数据，原因：历史数据与新数据交叉
      String[] attrs2 = PubXmlConstants.PUB_EDIT_REMAIN_MATA_ATTR;
      newDoc.fillAttributeValue(newDoc, PubXmlConstants.PUBLICATION_XPATH, PubXmlConstants.PUB_META_XPATH, attrs2,
          attrs2);
      newDoc.fillAttributeValue(newDoc, PubXmlConstants.PUB_META_XPATH, PubXmlConstants.PUBLICATION_XPATH, attrs2,
          attrs2);
      // 原始引用情况
      String[] pubListAttrs = PubXmlConstants.PUB_EDIT_REMAIN_PUBLIST_ATTR;
      newDoc.copyAttributeValue(oldDoc, PubXmlConstants.PUB_LIST_XPATH, PubXmlConstants.PUB_LIST_XPATH, pubListAttrs,
          pubListAttrs);
      // 拷贝成果基准库情况
      newDoc.copyPubElement((Element) oldDoc.getNode(PubXmlConstants.PUB_PDWH_XPATH));
      // 指派需要的数据
      // 如存在则保存好导入的【作者】名字
      List lists = oldDoc.getNodes(PubXmlConstants.PUB_AUTHOR_XPATH);
      if (lists != null && lists.size() > 0) {
        Element ele = newDoc.createElement("/pub_authors");
        for (int index = 0; index < lists.size(); index++) {
          Element au = ele.addElement("author");
          newDoc.copyPubElement(au, (Element) lists.get(index));
        }
      }
      // 如存在keyword_plus,sc,author_names_abbr则保存
      String keywordPlus = oldDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "keyword_plus");
      if (!"".equals(keywordPlus)) {
        newDoc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "keyword_plus", keywordPlus);
      }
      String categories = oldDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "sc");
      if (!"".equals(categories)) {
        newDoc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "sc", categories);
      }
      String pubAuthorsAbbr = oldDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "author_names_abbr");
      if (!"".equals(pubAuthorsAbbr)) {
        newDoc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "author_names_abbr", pubAuthorsAbbr);
      }

      this.scholarXmlSaveProcess.start(newDoc, context);

      return pubId;

    } catch (Exception e) {

      logger.error("保存XML错误", e);
      throw new ServiceException(e);

    }
  }

  @SuppressWarnings("unchecked")
  @Override
  @Deprecated
  public Long updateXml(long pubId, Map postData, int pubTypeId) throws ServiceException {

    long currentUserId = SecurityUtils.getCurrentUserId();
    long currentInsId = SecurityUtils.getCurrentInsId();
    // 配置xml处理上下文
    PubXmlProcessContext context =
        buildXmlProcessContext(XmlOperationEnum.Edit, pubTypeId, currentUserId, currentInsId);

    try {

      PubXmlDocument newDoc = PubXmlDocumentBuilder.build(postData);

      Integer confirmResult =
          IrisNumberUtils.createInteger(newDoc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "confirm_result"));
      confirmResult = confirmResult == null ? 0 : confirmResult;
      context.setConfirmResult(confirmResult);
      context.setCurrentPubId(pubId);
      // read old-pub-meta and copy to xmlDocument
      PublicationXml pubXml = publicationXmlService.rolGetById(pubId);
      PubXmlDocument oldDoc = new PubXmlDocument(pubXml.getXmlData());
      Element oldMeta = (Element) oldDoc.getNode(PubXmlConstants.PUB_META_XPATH);
      newDoc.copyPubElement(oldMeta);
      String[] attrs = PubXmlConstants.PUB_EDIT_REMAIN_PUBLICATION_ATTR;
      newDoc.copyAttributeValue(oldDoc, PubXmlConstants.PUBLICATION_XPATH, PubXmlConstants.PUBLICATION_XPATH, attrs,
          attrs);
      // 对调数据，原因：历史数据与新数据交叉
      String[] attrs2 = PubXmlConstants.PUB_EDIT_REMAIN_MATA_ATTR;
      newDoc.fillAttributeValue(newDoc, PubXmlConstants.PUBLICATION_XPATH, PubXmlConstants.PUB_META_XPATH, attrs2,
          attrs2);
      newDoc.fillAttributeValue(newDoc, PubXmlConstants.PUB_META_XPATH, PubXmlConstants.PUBLICATION_XPATH, attrs2,
          attrs2);
      // 原始引用情况
      String[] pubListAttrs = PubXmlConstants.PUB_EDIT_REMAIN_PUBLIST_ATTR;
      newDoc.copyAttributeValue(oldDoc, PubXmlConstants.PUB_LIST_XPATH, PubXmlConstants.PUB_LIST_XPATH, pubListAttrs,
          pubListAttrs);
      // 拷贝成果基准库情况
      newDoc.copyPubElement((Element) oldDoc.getNode(PubXmlConstants.PUB_PDWH_XPATH));
      // 指派需要的数据
      // 如存在则保存好导入的【作者】名字
      List lists = oldDoc.getNodes(PubXmlConstants.PUB_AUTHOR_XPATH);
      if (lists != null && lists.size() > 0) {
        Element ele = newDoc.createElement("/pub_authors");
        for (int index = 0; index < lists.size(); index++) {
          Element au = ele.addElement("author");
          newDoc.copyPubElement(au, (Element) lists.get(index));
        }
      }
      // 如存在keyword_plus,sc,author_names_abbr则保存
      String keywordPlus = oldDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "keyword_plus");
      if (!"".equals(keywordPlus)) {
        newDoc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "keyword_plus", keywordPlus);
      }
      String categories = oldDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "sc");
      if (!"".equals(categories)) {
        newDoc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "sc", categories);
      }
      String pubAuthorsAbbr = oldDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "author_names_abbr");
      if (!"".equals(pubAuthorsAbbr)) {
        newDoc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "author_names_abbr", pubAuthorsAbbr);
      }

      this.scholarXmlSaveProcess.start(newDoc, context);

      return pubId;

    } catch (Exception e) {

      logger.error("保存XML错误", e);
      throw new ServiceException(e);

    }
  }

  @Override
  public void setRolXmlOnlineImportProcess(IPubXmlProcess process) {
    Assert.notNull(process, "请注入RolXmlImportProcess Bean");
    this.rolXmlOnlineImportProcess = process;
  }

  /*
   * @Override public void setRolXmlSaveProcess(IPubXmlProcess process) { Assert.notNull(process,
   * "请注入RolXmlSaveProcess Bean"); this.scholarXmlSaveProcess = process; }
   */

  /**
   * @param xmlValidatorFactory the xmlValidatorFactory to set
   */
  public void setXmlValidatorFactory(XmlValidatorFactory xmlValidatorFactory) {
    this.xmlValidatorFactory = xmlValidatorFactory;
  }

  /**
   * @param briefDriverFactory the briefDriverFactory to set
   */
  public void setBriefDriverFactory(BriefDriverFactory briefDriverFactory) {
    this.briefDriverFactory = briefDriverFactory;
  }

  public BriefDriverFactory getBriefDriverFactory() {
    return briefDriverFactory;
  }

  @Override
  public void setRolXmlBackgroundImportProcess(IPubXmlProcess process) {
    Assert.notNull(process, "请注入RolXmlBackgroundImportProcess Bean");
    this.rolXmlBackgroundImportProcess = process;
  }

  /**
   * @param importXmlTranslateProcess the importXmlTranslateProcess to set
   */
  public void setImportXmlTranslateProcess(IPubXmlProcess importXmlTranslateProcess) {
    this.importXmlTranslateProcess = importXmlTranslateProcess;
  }

  /**
   * @return the rolXmlSyncProcess
   */
  public IPubXmlProcess getRolXmlSyncProcess() {
    return rolXmlSyncProcess;
  }

  public void setPubXmlSyncOldProcess(IPubXmlProcess pubXmlSyncOldProcess) {
    this.pubXmlSyncOldProcess = pubXmlSyncOldProcess;
  }

  /**
   * @param rolXmlSyncProcess the rolXmlSyncProcess to set
   */
  public void setRolXmlSyncProcess(IPubXmlProcess rolXmlSyncProcess) {
    Assert.notNull(rolXmlSyncProcess, "请注入rolXmlSyncProcess Bean");
    this.rolXmlSyncProcess = rolXmlSyncProcess;
  }

  @SuppressWarnings("unchecked")
  @Override
  public String generateBriefFromImportXml(PubXmlProcessContext context, PubXmlDocument xmlDocument, String formTmpl,
      Integer typeId) throws ServiceException {
    try {
      IBriefDriver briefDriver = briefDriverFactory.getDriver(formTmpl, typeId);
      if (briefDriver != null) {
        String brief = getLanguagesBrief(context.getLocale(), context, xmlDocument, briefDriver);
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc", brief);
        return brief;
      }
    } catch (BriefDriverNotFoundException e) {
      logger.error("generateBriefFromImportXml找不到类型typeId={}对应的BriefDriver", typeId, e);
      throw new ServiceException(e);
    } catch (Exception e) {
      logger.error("generateBriefFromImportXml错误", e);
      throw new ServiceException(e);
    }
    return "";
  }

  @Override
  public String getLanguagesBrief(Locale locale, PubXmlProcessContext context, PubXmlDocument xmlDocument,
      Integer pubType) throws Exception {
    IBriefDriver briefDriver = briefDriverFactory.getDriver(PublicationEnterFormEnum.SCHOLAR, pubType);
    return this.getLanguagesBrief(locale, context, xmlDocument, briefDriver);
  }

  private String getLanguagesBrief(Locale locale, PubXmlProcessContext context, PubXmlDocument xmlDocument,
      IBriefDriver briefDriver) throws Exception {
    Map result = briefDriver.getData(locale, xmlDocument, context);
    String pattern = briefDriver.getPattern();
    BriefFormatter formatter = new BriefFormatter(locale, result);
    String brief = formatter.format(pattern);
    formatter = null;
    return brief;
  }

  @Override
  public Map<String, String> generateBriefFromImportXmlMap(PubXmlProcessContext context, PubXmlDocument xmlDocument,
      String formTmpl, Integer typeId) throws ServiceException {
    try {
      Map<String, String> briefMap = new HashMap<String, String>();
      IBriefDriver briefDriver = briefDriverFactory.getDriver(formTmpl, typeId);
      if (briefDriver != null) {
        String briefZh = getLanguagesBrief(LocaleUtils.toLocale("zh_CN"), context, xmlDocument, briefDriver);
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc", briefZh);
        briefMap.put("brief_desc_zh", briefZh);
        String briefEn = getLanguagesBrief(LocaleUtils.toLocale("en_US"), context, xmlDocument, briefDriver);
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc_en", briefEn);
        briefMap.put("brief_desc_en", briefEn);
        return briefMap;
      }
    } catch (BriefDriverNotFoundException e) {
      logger.error("generateBriefFromImportXml找不到类型typeId={}对应的BriefDriver", typeId, e);
      throw new ServiceException(e);
    } catch (Exception e) {
      logger.error("generateBriefFromImportXml错误", e);
      throw new ServiceException(e);
    }
    return null;
  }

  @Override
  public PubXmlDocument translateImportXml(PubXmlProcessContext context, String importXml) throws ServiceException {

    context.setArticleType(PublicationArticleType.OUTPUT);
    context.setCurrentAction(XmlOperationEnum.Import);

    try {

      PubXmlDocument xmlDocument = new PubXmlDocument(importXml);
      this.importXmlTranslateProcess.start(xmlDocument, context);
      return xmlDocument;

    } catch (DocumentException e) {
      logger.error("导入XML,转换错误", e);
      throw new ServiceException(e);

    } catch (Exception e) {
      logger.error("导入XML错误", e);
      throw new ServiceException(e);
    }
  }

  /**
   * @return the xmlEmailPostfix
   */
  public String getXmlEmailPostfix() {
    return xmlEmailPostfix;
  }

  /**
   * @param xmlEmailPostfix the xmlEmailPostfix to set
   */
  public void setXmlEmailPostfix(String xmlEmailPostfix) {
    this.xmlEmailPostfix = xmlEmailPostfix;
  }

  /**
   * 更新引用次数.
   * 
   * @param pubId
   * @param citedTimes
   * @throws ServiceException
   */
  public PubXmlDocument resetCitedTimes(Long pubId, Integer citedTimes) throws ServiceException {

    try {
      if (citedTimes == null || citedTimes == 0) {
        return null;
      }
      // 修改XML
      PublicationXml pubxml = publicationXmlService.rolGetById(pubId);
      String xmlData = pubxml.getXmlData();
      Document doc = DocumentHelper.parseText(xmlData);
      PubXmlDocument pubXmlDocument = new PubXmlDocument(doc);
      return this.resetCitedTimes(pubId, pubXmlDocument, citedTimes);
    } catch (Exception e) {
      logger.error("更新引用次数错误", e);
      throw new ServiceException(e);
    }
  }

  public PubXmlDocument resetCitedTimes(Long pubId, PubXmlDocument doc, Integer citedTimes) throws ServiceException {
    try {
      if (citedTimes == null || citedTimes == 0) {
        return null;
      }
      // 修改成果
      PublicationRol pub = publicationRolDao.get(pubId);
      if (pub == null) {
        return null;
      }
      pub.setCitedTimes(citedTimes);
      publicationRolDao.save(pub);
      doc.setNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cite_times", citedTimes.toString());
      return doc;
    } catch (Exception e) {
      logger.error("更新引用次数错误", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public PubXmlDocument reBuildInsPubxml(PubConfirmSyncMessage msg, Long assignPmId, Long cofirmPmId,
      PublicationRol pub) throws ServiceException {

    try {

      Long insId = msg.getInsId();
      Long insPubId = msg.getInsPubId();
      Long psnId = msg.getPsnId();
      String publishDate = StringUtils.trimToEmpty(msg.getPublishDate());

      // PublicationXml pubxml = publicationXmlService.getById(insPubId);
      RolPubXml pubxml = rolPubXmlService.getPubXml(insPubId);
      if (pubxml == null) {
        throw new Exception("成果xml文件为空，pubId= " + insPubId);
      }
      String xmlData = pubxml.getPubXml();

      if (StringUtils.isBlank(xmlData)) {
        throw new Exception("成果xml文件为空，pubId= " + insPubId);
      }

      Document doc = DocumentHelper.parseText(xmlData);
      PubXmlDocument pubXmlDocument = new PubXmlDocument(doc);

      if (!cofirmPmId.equals(assignPmId)) {
        // 清空原有指派关系
        if (assignPmId != null) {
          Element e = (Element) pubXmlDocument
              .getNode(PubXmlConstants.PUB_MEMBERS_MEMBER_XPATH + "[@pm_id=" + assignPmId + "]");
          if (e != null) {
            Long prePsnId = IrisNumberUtils.createLong(e.attributeValue("member_psn_id"));
            // 原来pubmember中的人员为确认成果的人员，清空
            if (psnId.equals(prePsnId)) {
              e.addAttribute("member_psn_acname", "");
              e.addAttribute("member_psn_id", "");
              e.addAttribute("unit_id", "");
              e.addAttribute("parent_unit_id", "");
            }
          }
        }
      }
      // 重构作者
      this.rebuildPubMember(cofirmPmId, insId, psnId, pubXmlDocument);
      // 重构引用情况
      // if (MapUtils.isNotEmpty(msg.getPubList())) {
      // this.reBuildInsPubList(pubXmlDocument, msg.getPubList());
      // }

      // 重构日期
      // if (StringUtils.isNotBlank(publishDate)) {
      // String preYear = ObjectUtils.toString(pub.getPublishYear());
      // String preMonth = ObjectUtils.toString(pub.getPublishMonth());
      // String prePublishDate = preYear + "-" + preMonth;
      // // // 如果已经修改
      // if (!prePublishDate.equals(publishDate)) {
      // this.reBuildPublishDate(pubXmlDocument, publishDate, psnId,
      // insId);
      // this.prasePublishDate(pubXmlDocument, pub);
      // }
      // }

      // 重构brief
      this.rebuildPublicationBrief(pubXmlDocument, pub, psnId);

      return pubXmlDocument;

    } catch (Exception e) {
      logger.error("成果确认后，重构XML错误", e);
      throw new ServiceException(e);
    }

  }

  @Override
  public PubXmlDocument reBuildPublishDate(PubXmlDocument pubXmlDocument, String publishDate, Long psnId, Long insId)
      throws ServiceException {

    try {
      int pubTypeId = pubXmlDocument.getPubTypeId();
      if (pubTypeId == ConstPublicationType.PUB_PATENT_TYPE) {
        pubXmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_date", publishDate);
        pubXmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "apply_date", publishDate);
      } else if (pubTypeId == ConstPublicationType.PUB_OTHERS_TYPE) {
        if (pubXmlDocument.existsNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "di_mode")) {
          String dim = pubXmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "di_mode");// $("pub_other@di_mode").value;
          if ("period".equalsIgnoreCase(dim)) {
            pubXmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "start_date", publishDate);
          } else if ("date".equalsIgnoreCase(dim)) {
            pubXmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "pub_date", publishDate);
          } else {
            String publishYear = publishDate.split("-")[0];
            pubXmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "pub_year", publishYear);
          }
        }
        pubXmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_date", publishDate);
      } else {
        pubXmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_date", publishDate);
      }
      // 处理日期
      DateAttributeCleanTask task = new DateAttributeCleanTask();
      PubXmlProcessContext context =
          this.buildXmlProcessContext(XmlOperationEnum.Edit, pubXmlDocument.getPubTypeId(), psnId, insId);
      context.setLocale(Locale.CHINESE);
      task.run(pubXmlDocument, context);
      return pubXmlDocument;
    } catch (Exception e) {
      logger.error("更新成果日期，重构XML错误", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public PubXmlDocument reBuildInsPubList(PubXmlDocument pubXmlDocument, Map<String, String> pubList)
      throws ServiceException {

    try {
      int pubTypeId = pubXmlDocument.getPubTypeId();
      if ((pubTypeId == ConstPublicationType.PUB_CONFERECE_TYPE || pubTypeId == ConstPublicationType.PUB_JOURNAL_TYPE
          || pubTypeId == ConstPublicationType.PUB_OTHERS_TYPE) && pubList != null) {
        Element ele = (Element) pubXmlDocument.getNode(PubXmlConstants.PUB_LIST_XPATH);
        if (ele == null) {
          ele = pubXmlDocument.createElement(PubXmlConstants.PUB_LIST_XPATH);
        }
        if (ele != null) {
          pubXmlDocument.fillPubListAttribute(ele, pubList);
        }
      }
      return pubXmlDocument;
    } catch (Exception e) {
      logger.error("更新成果引用情况，重构XML错误", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public PubXmlDocument rebuildPubMember(Long pmId, Long insId, Long psnId, Long pubId) throws ServiceException {
    try {
      PublicationXml pubxml = publicationXmlService.rolGetById(pubId);
      String xmlData = pubxml.getXmlData();
      Document doc = DocumentHelper.parseText(xmlData);
      PubXmlDocument pubXmlDocument = new PubXmlDocument(doc);
      this.rebuildPubMember(pmId, insId, psnId, pubXmlDocument);
      return pubXmlDocument;
    } catch (Exception e) {
      logger.error("设置Pubmember xml为单位具体人员错误", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void rebuildPubMember(Long pmId, Long insId, Long psnId, PubXmlDocument pubXmlDocument)
      throws ServiceException {
    try {
      // 创建新的指派关系
      Element e = (Element) pubXmlDocument.getNode(PubXmlConstants.PUB_MEMBERS_MEMBER_XPATH + "[@pm_id=" + pmId + "]");
      if (e != null) {
        // 确认人员pubmember中的人员不为确认成果的人员，替换
        RolPsnIns psnIns = rolPsnInsDao.findPsnIns(psnId, insId);
        if (psnIns != null) {
          String psnName = psnIns.getZhName();
          if (StringUtils.isBlank(psnName)) {
            psnName = psnIns.getFirstName() + " " + psnIns.getLastName();
          }
          // 获取部门
          if (psnIns.getUnitId() != null) {
            String unitName = insUnitDao.getUnitName(psnIns.getUnitId(), insId, Locale.CHINESE);
            if (unitName != null)
              psnName += "(" + unitName + ")";
          }
          e.addAttribute("member_psn_acname", psnName);
          e.addAttribute("member_psn_id", psnId.toString());
          e.addAttribute("unit_id", psnIns.getUnitId() == null ? "" : psnIns.getUnitId().toString());
          e.addAttribute("parent_unit_id", psnIns.getSuperUnitId() == null ? "" : psnIns.getSuperUnitId().toString());
          e.addAttribute("ins_id", insId.toString());
          // 添加单位
          this.reBuildPubMemberInsxml(e, insId);
        }
      }
    } catch (Exception e) {
      logger.error("设置Pubmember xml为单位具体人员错误", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public PubMemberRol updatePubMemeberXml(PubMemberRol mb) throws ServiceException {

    try {
      // 获取XML，XML已经使用自定义缓存
      PublicationXml xmlData = publicationXmlService.rolGetById(mb.getPubId());
      // 转换XML
      PubXmlDocument doc = new PubXmlDocument(xmlData.getXmlData());
      updatePubMemberXml(mb, doc);
      PublicationRol pub = publicationRolDao.get(mb.getPubId());
      // 重构第一作者等信息
      this.praseAuthorNames(doc, pub);
      // 更新xml
      publicationXmlService.rolSave(mb.getPubId(), doc.getXmlString());
      publicationRolDao.save(pub);
      return mb;
    } catch (Exception e) {
      logger.error("更新成果成员指派XML", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void updatePubMemeberXml(Long pubId, List<PubMemberRol> mbs) throws ServiceException {
    try {
      if (CollectionUtils.isEmpty(mbs)) {
        return;
      }
      // 获取XML，XML已经使用自定义缓存
      PublicationXml xmlData = publicationXmlService.rolGetById(pubId);
      // 转换XML
      PubXmlDocument doc = new PubXmlDocument(xmlData.getXmlData());
      for (PubMemberRol mb : mbs) {
        updatePubMemberXml(mb, doc);
      }

      PublicationRol pub = publicationRolDao.get(pubId);
      // 重构第一作者等信息
      this.praseAuthorNames(doc, pub);
      // 更新xml
      publicationXmlService.rolSave(pubId, doc.getXmlString());
      publicationRolDao.save(pub);
    } catch (Exception e) {
      logger.error("更新成果成员指派XML", e);
      throw new ServiceException(e);
    }
  }

  private void updatePubMemberXml(PubMemberRol mb, PubXmlDocument doc) throws DaoException, Exception {
    Element e = (Element) doc.getNode(PubXmlConstants.PUB_MEMBERS_MEMBER_XPATH + "[@pm_id=" + mb.getId() + "]");
    updatePubMemberXml(mb, e);
    // 更新xml
    publicationXmlService.rolSave(mb.getPubId(), doc.getXmlString());
  }

  @Override
  public void noSaveUpdatePubMemberXml(PubMemberRol mb, PubXmlDocument doc) throws ServiceException {
    try {
      Element e = (Element) doc.getNode(PubXmlConstants.PUB_MEMBERS_MEMBER_XPATH + "[@pm_id=" + mb.getId() + "]");

      updatePubMemberXml(mb, e);
    } catch (Exception e) {
      logger.error("更新成果成员指派XML", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 更新pubmember的xml数据.
   * 
   * @param mb
   * @param e
   * @throws DaoException
   */
  private void updatePubMemberXml(PubMemberRol mb, Element pubMember) throws DaoException {
    try {
      if (mb.getPsnId() == null) {
        pubMember.addAttribute("member_psn_acname", "");
        pubMember.addAttribute("member_psn_id", "");
        pubMember.addAttribute("unit_id", "");
        pubMember.addAttribute("parent_unit_id", "");
      } else {
        RolPsnIns psnIns = rolPsnInsDao.findPsnIns(mb.getPsnId(), mb.getInsId());
        if (psnIns != null) {
          Locale locale = LocaleContextHolder.getLocale();
          String psnName = psnIns.getPsnName();
          if (Locale.US.equals(locale)) {
            psnName = psnIns.getEnName();
          } else {
            psnName = psnIns.getZhName();
          }
          // 获取部门
          if (psnIns.getUnitId() != null) {
            String unitName = insUnitDao.getUnitName(psnIns.getUnitId(), mb.getInsId(), locale);
            if (unitName != null)
              psnName += "(" + unitName + ")";
          }
          pubMember.addAttribute("ins_id", psnIns.getPk().getInsId().toString());
          pubMember.addAttribute("unit_id", psnIns.getUnitId() == null ? "" : psnIns.getUnitId().toString());
          pubMember.addAttribute("parent_unit_id",
              psnIns.getSuperUnitId() == null ? "" : psnIns.getSuperUnitId().toString());
          pubMember.addAttribute("member_psn_acname", psnName);
          pubMember.addAttribute("member_psn_id", mb.getPsnId().toString());
        } else {
          pubMember.addAttribute("member_psn_acname", "");
          pubMember.addAttribute("member_psn_id", "");
          pubMember.addAttribute("unit_id", "");
          pubMember.addAttribute("parent_unit_id", "");
          mb.setPsnId(null);
          mb.setIsConfirm(0);
        }
      }
      pubMember.addAttribute("seq_no", mb.getSeqNo() == null ? "" : mb.getSeqNo().toString());
      pubMember.addAttribute("member_psn_name", mb.getName());
      pubMember.addAttribute("pm_id", mb.getId() == null ? "" : mb.getId().toString());
    } catch (Exception e) {
      logger.error("", e);
    }
  }

  @Override
  public PublicationRol prasePublishDate(PubXmlDocument pubXmlDoc, PublicationRol pub) {
    // 更新日期
    pub.setPublishDay(
        IrisNumberUtils.createInteger(pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_day")));
    pub.setPublishMonth(IrisNumberUtils
        .createInteger(pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_month")));
    pub.setPublishYear(IrisNumberUtils
        .createInteger(pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_year")));
    return pub;
  }

  @Override
  public PublicationRol praseAuthorNames(PubXmlDocument pubXmlDoc, PublicationRol pub) throws ServiceException {
    try {
      // 重构用户名，第一作者
      Map<String, Object> authorMap = pubRolMemberDao.buildPubAuthorNames2(pub.getId());
      String authorNames = (String) authorMap.get("AuthorNames");
      String briefAuthorNames = (String) authorMap.get("BriefAuthorNames");
      Long firstAuthor = (Long) authorMap.get("FirstAuthor");
      pubXmlDoc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "author_names", authorNames);
      pubXmlDoc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_author_names", briefAuthorNames);
      pub.setAuthorNames(StringUtils.substring(briefAuthorNames, 0, 200));
      pub.setFirstAuthorPsnId(firstAuthor);
    } catch (DaoException e) {
      logger.error("重构用户名XML", e);
      throw new ServiceException(e);
    }
    return pub;
  }

  /**
   * 映射成果作者与单位人员的关系时仅清除pub_member中的人员ID.
   * 
   * @param psnId
   * @param pubId
   * @throws DaoException
   * @throws ServiceException
   */
  private void mappingDupPubAuthorClearPrePsn(Long psnId, Long pubId, PubXmlDocument doc)
      throws DaoException, ServiceException {
    // 判断人员之前是否已经指派给了该成果
    PubMemberRol prePmRol = this.pubRolMemberDao.getPsnPubMember(pubId, psnId);
    if (prePmRol != null) {
      prePmRol.setPsnId(null);
      prePmRol.setIsConfirm(0);
      // 保存
      this.pubRolMemberDao.savePubMember(prePmRol);
      // 更新XML
      this.noSaveUpdatePubMemberXml(prePmRol, doc);
    }
  }

  @Override
  public void rebuildPublicationBrief(PubXmlDocument xmlDocument, PublicationRol pub) throws ServiceException {

    long currentUserId = SecurityUtils.getCurrentUserId();
    this.rebuildPublicationBrief(xmlDocument, pub, currentUserId);
  }

  /**
   * 重构生成简要描述（页面表格的来源列）.
   * 
   * @param xmlDocument
   * @param pub
   * @param psnId
   * @throws ServiceException
   */
  public void rebuildPublicationBrief(PubXmlDocument xmlDocument, PublicationRol pub, Long psnId)
      throws ServiceException {

    long currentInsId = pub.getInsId();
    PubXmlProcessContext context = buildXmlProcessContext(XmlOperationEnum.Edit, pub.getTypeId(), psnId, currentInsId);
    try {
      // xml处理程序
      IPubXmlTask task = this.scholarXmlSaveProcess.getPubXmlTask(PublicationBriefGenerateTask.Brief_GENERATE);
      if (task != null && task.can(xmlDocument, context)) {
        task.run(xmlDocument, context);
        String briefDesc = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc");
        pub.setBriefDesc(briefDesc);
        String briefDescEn = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc_en");
        pub.setBriefDescEn(briefDescEn);
      }
    } catch (Exception e) {
      logger.error("重构生成简要描述（页面表格的来源列）错误", e);
      throw new ServiceException(e);

    }
  }

  /**
   * 如果单位的成果人员中不存在单位，添加单位到成果的Xml中.
   * 
   * @param member
   * @param insId
   * @throws ServiceException
   */
  private void reBuildPubMemberInsxml(Element member, Long insId) throws ServiceException {
    if (member == null || insId == null)
      return;
    InstitutionRol ins = institutionRolService.getInstitution(insId);
    if (ins == null)
      return;
    String insName = ins.getZhName() == null ? ins.getEnName() : ins.getZhName();
    member.addAttribute("ins_id", insId.toString());
    member.addAttribute("ins_name", insName);

  }

  public IPubXmlProcess getRolPubRebuildProcess() {
    return rolPubRebuildProcess;
  }

  public void setRolPubRebuildProcess(IPubXmlProcess rolPubRebuildProcess) {
    this.rolPubRebuildProcess = rolPubRebuildProcess;
  }

  @Override
  public PubXmlDocument getPubXmlByPubId(Long pubId) throws ServiceException {
    try {
      PublicationXml xmlData = publicationXmlService.rolGetById(pubId);
      if (xmlData == null || StringUtils.isBlank(xmlData.getXmlData()))
        return null;
      PubXmlDocument xmlDocument = new PubXmlDocument(xmlData.getXmlData());
      return xmlDocument;
    } catch (ServiceException e) {
      logger.error("getPubXmlByPubId加载XML错误, pubId=" + pubId, e);
      throw new ServiceException(e);
    } catch (Exception e) {
      logger.error("getPubXmlByPubId加载XML,转换为XmlDocument错误, pubId=" + pubId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Long onlineImportPubXml(String importXml, int pubTypeId) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Long syncOldPubXml(Map<String, Object> param) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Long saveXmlFromSNS(Long insId, Long userId, Long snsPubId, String xmlData) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void mappingDupPubAuthor(Map<String, Object> mapInfo, String importXml, int pubTypeId)
      throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void fillInfoByDupXml(Element dupPubNode, Long pubId) throws ServiceException {
    try {
      PublicationRol pub = this.publicationRolDao.get(pubId);
      PublicationXml pubXml = this.publicationXmlService.rolGetById(pubId);
      PubXmlDocument remainDoc = new PubXmlDocument(pubXml.getXmlData());
      boolean refreshJnlOAType = false;

      // 为成果添加成果所属基金信息
      String fundInfo = StringUtils.trimToEmpty(dupPubNode.attributeValue("fundinfo"));
      remainDoc.setNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "fundinfo", fundInfo);
      String source = StringUtils.trimToEmpty(dupPubNode.attributeValue("source"));

      // 如果来源为isi且sc为空，添加sc
      String preScienceCategory = remainDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "sc");
      if (StringUtils.isBlank(preScienceCategory) && ("ISI".equalsIgnoreCase(source))) {
        String scienceCategory = StringUtils.trimToEmpty(dupPubNode.attributeValue("sc"));
        if (StringUtils.isNotBlank(scienceCategory)) {
          remainDoc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "sc", scienceCategory);
        }
      }

      if ("EI".equalsIgnoreCase(source)) {
        String journalCategoryNo = StringUtils.trimToEmpty(dupPubNode.attributeValue("journal_category_no"));
        if (StringUtils.isNotEmpty(journalCategoryNo)) {
          remainDoc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "journal_category_no", journalCategoryNo);
        }
      }

      String dbCode = StringUtils.trimToEmpty(dupPubNode.attributeValue("source_db_code"));
      // 修改引用情况
      // if (PubXmlDbUtils.isIsiDb(dbCode)) {
      Integer citedTimes =
          IrisNumberUtils.createInteger(StringUtils.trimToEmpty(dupPubNode.attributeValue("cite_times")));
      if (citedTimes != null && citedTimes != 0) {
        pub.setCitedTimes(citedTimes);
        remainDoc.setNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cite_times", citedTimes.toString());
        String citedRecordUrl = StringUtils.trimToEmpty(dupPubNode.attributeValue("cite_record_url"));
        if (StringUtils.isNotBlank(citedRecordUrl)) {
          pub.setCitedUrl(StringUtils.substring(citedRecordUrl, 0, 3000));
          remainDoc.setNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cited_url", citedRecordUrl);
        }
      }
      // }
      // 收录情况
      String citationIndex = StringUtils.trimToEmpty(dupPubNode.attributeValue("citation_index"));
      if (StringUtils.isNotBlank(citationIndex)) {
        remainDoc.fillPubListByCitationIndex(citationIndex);
      }

      if (StringUtils.isNotBlank(source) && "EI".equalsIgnoreCase(source)) {
        remainDoc.setXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ei", "1");
        remainDoc.setXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ei_source", "1");
      }
      // 补充sps_id、ei_id、isi_id
      String sourceId = StringUtils.trimToNull(dupPubNode.attributeValue("source_id"));
      if (sourceId != null && "Scopus".equalsIgnoreCase(dbCode)) {
        String spsId = remainDoc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "sps_id");
        if (StringUtils.isBlank(spsId)) {
          remainDoc.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "sps_id", sourceId);
        }
      } else if (sourceId != null && "ChinaJournal".equalsIgnoreCase(dbCode)) {
        String eiId = remainDoc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "ei_id");
        if (StringUtils.isBlank(eiId)) {
          remainDoc.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "ei_id", sourceId);
        }
      } else if (sourceId != null && (PubXmlDbUtils.isIsiDb(dbCode))) {
        String isiId = remainDoc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "isi_id");
        if (StringUtils.isBlank(isiId)) {
          remainDoc.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "isi_id", sourceId);
          pub.setIsiId(isiId);
        }
      }
      // 补充isbn
      String isbn = StringUtils.substring(StringUtils.trimToNull(dupPubNode.attributeValue("isbn")), 0, 40);
      if (isbn != null) {
        String preIsbn = remainDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "isbn");
        if (StringUtils.isBlank(preIsbn)) {
          remainDoc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "isbn", isbn);
          pub.setIsbn(isbn);
        }
      }
      // 补充issue
      String issue = StringUtils.substring(StringUtils.trimToNull(dupPubNode.attributeValue("issue")), 0, 20);
      if (issue != null) {
        String preIssue = remainDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "issue");
        if (StringUtils.isBlank(preIssue)) {
          remainDoc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "issue", issue);
          pub.setIssue(issue);
        }
      }
      // 补充volume
      String volume = StringUtils.substring(StringUtils.trimToNull(dupPubNode.attributeValue("volume")), 0, 20);
      if (volume != null) {
        String prevolume = remainDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "volume");
        if (StringUtils.isBlank(prevolume)) {
          remainDoc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "volume", volume);
          pub.setVolume(volume);
        }
      }

      // 如果重复成果为期刊，完善期刊信息
      Integer pubtype = IrisNumberUtils.createInteger(StringUtils.trimToEmpty(dupPubNode.attributeValue("pub_type")));
      if (remainDoc.isJournalArticle() && (pubtype != null && pubtype == PublicationTypeEnum.JOURNAL_ARTICLE)) {
        Long prejid =
            IrisNumberUtils.createLong(remainDoc.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jid"));
        if (prejid == null) {
          Long jid = IrisNumberUtils.createLong(dupPubNode.attributeValue("jid"));
          if (jid != null) {
            Journal journal = journalService.getById(jid);
            if (journal != null) {
              // 期刊统一放在1节点
              remainDoc.fillPubJournalByJournal(journal, IPubXmlServiceFactory.FIRST_NODE_ID);
              pub.setJid(jid);
              refreshJnlOAType = true;
            }
          } else {
            String journalName = XmlUtil.changeSBCChar(StringUtils.trimToEmpty(dupPubNode.attributeValue("original")));
            String issn = XmlUtil.changeSBCChar(StringUtils.trimToEmpty(dupPubNode.attributeValue("issn")));
            if (StringUtils.isNotBlank(journalName)) {
              Journal journal =
                  journalService.findJournalByNameIssn(journalName, issn, SecurityUtils.getCurrentUserId());
              if (journal == null) {
                String journamFrom = dupPubNode.attributeValue("source_db_code");
                journal = journalService.addJournal(journalName, issn, SecurityUtils.getCurrentUserId(), journamFrom);
              }
              if (journal != null) {
                // 期刊统一放在1节点
                remainDoc.fillPubJournalByJournal(journal, IPubXmlServiceFactory.FIRST_NODE_ID);
                pub.setJid(jid);
              }
            }
          }
        }
      }
      // 为成果添加成果所属基金信息
      this.pubFundInfoService.savePubFundInfo(pub.getId(), pub.getInsId(), fundInfo);
      // 收录情况
      this.publicationListRolService.praseSourcePubList(remainDoc);
      // 查重数据
      this.publicationRolService.parsePubDupFields(remainDoc, pub);
      // 重构brief
      this.rebuildPublicationBrief(remainDoc, pub);
      pub.setUpdateDate(new Date());
      this.publicationRolDao.save(pub);
      // 保存成果XML
      publicationXmlService.rolSave(pubId, remainDoc.getXmlString());
      // 刷新期刊-开放存储类型
      if (refreshJnlOAType) {
        this.jnlOATypeRefreshService.saveJnlOATypeRefresh(pub.getJid());
      }
    } catch (Exception e) {
      logger.error("成果导入时，严格查重后，忽略重复成果，将忽略成果的收录情况等补充到重复成果错误", e);
      throw new ServiceException(e);
    }
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public Long backgroundImportIsiPubXml(IsiPubCacheAssignMessage message) throws Exception {
    Long insId = message.getInsId();
    Long xmlId = message.getXmlId();
    String importXml = message.getXmlData();
    try {
      // get admin user id
      List<UserRole> users = this.insPersonService.getSysAdministrator();
      if (users.size() == 0) {
        throw new Exception("SysAdministrator Not Found !");
      }
      long currentUserId = users.get(0).getId().getUserId();
      // 进行isi_id查重，如果重复，则不导入，只更新引用次数
      PubXmlDocument xmlDocument = new PubXmlDocument(importXml);
      // 进行查重，如果重复，则不导入，只更新引用次数
      Element pubEle = (Element) xmlDocument.getPublication();
      Journal journal = getJournal(pubEle);
      if (journal != null) {
        pubEle.addAttribute("jid", journal.getId().toString());
      }

      // 正常成果严格查重
      List<Long> dupPubIds = this.rolPubDupService.getStrictDupPubByImportPub(pubEle, insId);
      if (dupPubIds != null && dupPubIds.size() > 0) {
        this.fillInfoByDupXml(pubEle, dupPubIds.get(0));
        // 记录
        publicationRolService.saveIsiPubcacheInsAssign(xmlId, dupPubIds.get(0), insId, 0);
        return dupPubIds.get(0);
      }

      Integer needConfirmStatus = this.insConfirmService.findInsConfirmStatus(insId);
      // 需要单位确认，查临时库成果，查到忽略
      if (InsConfirm.NEED_CONFIRM.equals(needConfirmStatus)) {
        List<Long> dupTempPubIds =
            this.rolPubDupService.getStrictDupPubByImportPub(pubEle, insId, PubDupFields.INS_NOT_CONFIRM_STATUS);
        if (CollectionUtils.isNotEmpty(dupTempPubIds)) {
          return dupTempPubIds.get(0);
        }
      }

      Integer pubTypeId = ImportPubXmlUtils.parsePubType(importXml);
      // 继续导入新成果
      PubXmlProcessContext context =
          buildXmlProcessContext(XmlOperationEnum.OfflineImport, pubTypeId, currentUserId, insId);
      context.setNeedConfirmStatus(needConfirmStatus);
      Element meta = (Element) xmlDocument.getPubMeta();
      if (meta == null) {
        meta = xmlDocument.createElement(PubXmlConstants.PUB_META_XPATH);
      }
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "record_ins_id", String.valueOf(insId));
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "dbcache_xml_id", String.valueOf(xmlId));
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "batch_no", String.valueOf(0));
      SettingPubForm tmplForm = settingPubFormService.getSettingPubFormByInsId(insId);
      xmlDocument.setFormTemplate(tmplForm.getTmpFolder());
      this.rolXmlBackgroundImportProcess.start(xmlDocument, context);
      Long pubId = xmlDocument.getPubId();
      // 记录
      publicationRolService.saveIsiPubcacheInsAssign(xmlId, pubId, insId, 1);
      return pubId;

    } catch (DocumentException e) {
      logger.error("backgroundImportIsiPubXml导入XML,转换错误", e);
      throw new ServiceException(e);
    } catch (Exception e) {
      logger.error("backgroundImportIsiPubXml导入XML错误", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 导入成果，获取期刊.
   * 
   * @param pubEle
   * @return
   */
  private Journal getJournal(Element pubEle) {
    // pubtype
    String pubTypeId = StringUtils.trimToEmpty(pubEle.attributeValue("pub_type"));
    Integer pubType = null;
    if (NumberUtils.isDigits(pubTypeId)) {
      pubType = Integer.valueOf(pubTypeId);
    }
    // 期刊文章
    if (pubType == null || pubType != PublicationTypeEnum.JOURNAL_ARTICLE) {
      return null;
    }
    String journalName = XmlUtil.changeSBCChar(StringUtils.trimToEmpty(pubEle.attributeValue("original")));
    String issn = XmlUtil.changeSBCChar(StringUtils.trimToEmpty(pubEle.attributeValue("issn")));
    if (StringUtils.isEmpty(journalName)) {
      return null;
    }
    try {
      Journal journal = null;
      if (StringUtils.isNotBlank(journalName)) {
        journal = journalService.findJournalByNameIssn(journalName, issn, 1L);
      }
      if (journal == null) {
        String journamFrom = pubEle.attributeValue("source_db_code");
        journal = journalService.addJournal(journalName, issn, 1, journamFrom);
      }
      return journal;
    } catch (Exception e) {
      logger.error("获取期刊错误", e);
      return null;
    }
  }

  /**
   * 加载刷新成果XML数据.
   */
  @Override
  public PublicationRolForm loadXml(PublicationRolForm form) throws ServiceException, PublicationNotFoundException {

    try {

      String xmlData = loadXmlById(form.getPubId());
      // 刷新常数字段
      PubXmlDocument xmlDocument = new PubXmlDocument(xmlData);
      PubConstFieldRefresh.refresh(xmlDocument, this.rolPublicationXmlServiceFactory);
      // 保证 编辑的时候作者列表的部门显示一致 start
      // 获取作者列表(有关联的)
      List<Element> pubMembers = xmlDocument.getPubMembers();
      if (pubMembers != null && pubMembers.size() > 0 && SecurityUtils.getCurrentInsId() != null) {// 报表系统中
                                                                                                   // SecurityUtils.getCurrentInsId()==null，可以不走这段代码
        Element e = null;
        Locale locale = LocaleContextHolder.getLocale();
        for (int i = 0; i < pubMembers.size(); i++) {
          e = pubMembers.get(i);
          Long member_psn_id = IrisNumberUtils.createLong(e.attributeValue("member_psn_id"));
          if (member_psn_id != null && member_psn_id > 1) {
            // 获取psn的实体对象 findPsnInsAllStatus
            RolPsnIns person = rolPsnInsDao.findPsnInsAllStatus(member_psn_id, SecurityUtils.getCurrentInsId());
            if (person != null) {
              person.setPsnName(autoCompleteRolService.getShowName(person.getZhName(), person.getEnName(), locale));
              // 获取部门名称
              if (person.getUnitId() != null) {
                InsUnit insUnit = insUnitRolService.getInsUnitRolById(person.getUnitId());
                if (insUnit != null) {
                  String unitName =
                      autoCompleteRolService.getShowName(insUnit.getZhName(), insUnit.getEnName(), locale);
                  person.setPsnName(person.getPsnName() + "(" + unitName + ")");
                }
              }
              e.addAttribute("member_psn_acname", person.getPsnName());
            }
          }
        }
      }
      // end
      // 设置类型信息
      form.setTypeId(xmlDocument.getPubTypeId());
      form.setArticleType(xmlDocument.getArticleTypeId());

      form.setPubXml(xmlDocument.getXmlString());
      return form;

    } catch (DaoException e) {
      logger.error("loadXml加载XML错误, DaoException, pubId=" + form.getPubId(), e);
      throw new ServiceException(e);
    } catch (PublicationNotFoundException e) {
      logger.error("loadXml加载XML错误,PublicationNotFoundException, pubId=" + form.getPubId(), e);
      throw e;
    } catch (Exception e) {
      logger.error("loadXml加载XML,转换为XmlDocument错误, pubId=" + form.getPubId(), e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Long backgroundImportSpsPubXml(SpsPubCacheAssignMessage message) throws Exception {
    Long insId = message.getInsId();
    Long xmlId = message.getXmlId();
    String importXml = message.getXmlData();
    try {
      // get admin user id
      List<UserRole> users = this.insPersonService.getSysAdministrator();
      if (users.size() == 0) {
        throw new Exception("===SysAdministratorNotFound!==");
      }
      long currentUserId = users.get(0).getId().getUserId();
      // 如果重复，则不导入
      PubXmlDocument xmlDocument = new PubXmlDocument(importXml);
      // 进行查重，如果重复，则不导入
      Element pubEle = (Element) xmlDocument.getPublication();
      Journal journal = getJournal(pubEle);
      if (journal != null) {
        pubEle.addAttribute("jid", journal.getId().toString());
      }
      // 严格查重
      List<Long> dupPubIds = this.rolPubDupService.getStrictDupPubByImportPub(pubEle, insId);
      if (dupPubIds != null && dupPubIds.size() > 0) {
        // 记录
        publicationRolService.saveSpsPubcacheInsAssign(xmlId, dupPubIds.get(0), insId, 0);
        return dupPubIds.get(0);
      }

      Integer needConfirmStatus = this.insConfirmService.findInsConfirmStatus(insId);
      // 需要单位确认，查临时库成果，查到忽略
      if (InsConfirm.NEED_CONFIRM.equals(needConfirmStatus)) {
        List<Long> dupTempPubIds =
            this.rolPubDupService.getStrictDupPubByImportPub(pubEle, insId, PubDupFields.INS_NOT_CONFIRM_STATUS);
        if (CollectionUtils.isNotEmpty(dupTempPubIds)) {
          return dupTempPubIds.get(0);
        }
      }
      Integer pubTypeId = ImportPubXmlUtils.parsePubType(importXml);
      // 继续导入新成果
      PubXmlProcessContext context =
          buildXmlProcessContext(XmlOperationEnum.OfflineImport, pubTypeId, currentUserId, insId);
      context.setNeedConfirmStatus(needConfirmStatus);
      Element meta = (Element) xmlDocument.getPubMeta();
      if (meta == null) {
        meta = xmlDocument.createElement(PubXmlConstants.PUB_META_XPATH);
      }
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "record_ins_id", String.valueOf(insId));
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "dbcache_xml_id", String.valueOf(xmlId));
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "batch_no", String.valueOf(0));
      SettingPubForm tmplForm = settingPubFormService.getSettingPubFormByInsId(insId);
      xmlDocument.setFormTemplate(tmplForm.getTmpFolder());
      this.rolXmlBackgroundImportProcess.start(xmlDocument, context);
      Long pubId = xmlDocument.getPubId();
      // 记录
      publicationRolService.saveSpsPubcacheInsAssign(xmlId, pubId, insId, 1);
      return pubId;

    } catch (DocumentException e) {
      logger.error("backgroundImportspsPubXml导入XML,转换错误", e);
      throw new ServiceException(e);
    } catch (Exception e) {
      logger.error("backgroundImportspsPubXml导入XML错误", e);
      throw new ServiceException(e);
    }
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public Long backgroundImportPubMedPubXml(PubMedPubCacheAssignMessage message) throws Exception {
    Long insId = message.getInsId();
    Long xmlId = message.getXmlId();
    String importXml = message.getXmlData();
    try {
      // get admin user id
      List<UserRole> users = this.insPersonService.getSysAdministrator();
      if (users.size() == 0) {
        throw new Exception("===SysAdministratorNotFound!===");
      }
      long currentUserId = users.get(0).getId().getUserId();
      // 进行isi_id查重，如果重复，则不导入，只更新引用次数
      PubXmlDocument xmlDocument = new PubXmlDocument(importXml);
      // 进行查重，如果重复，则不导入，只更新引用次数
      Element pubEle = (Element) xmlDocument.getPublication();
      Journal journal = getJournal(pubEle);
      if (journal != null) {
        pubEle.addAttribute("jid", journal.getId().toString());
      }
      // 严格查重
      List<Long> dupPubIds = this.rolPubDupService.getStrictDupPubByImportPub(pubEle, insId);
      if (dupPubIds != null && dupPubIds.size() > 0) {
        this.fillInfoByDupXml(pubEle, dupPubIds.get(0));
        // 记录
        publicationRolService.savePubMedPubcacheInsAssign(xmlId, dupPubIds.get(0), insId, 0);
        return dupPubIds.get(0);
      }

      Integer needConfirmStatus = this.insConfirmService.findInsConfirmStatus(insId);
      // 需要单位确认，查临时库成果，查到忽略
      if (InsConfirm.NEED_CONFIRM.equals(needConfirmStatus)) {
        List<Long> dupTempPubIds =
            this.rolPubDupService.getStrictDupPubByImportPub(pubEle, insId, PubDupFields.INS_NOT_CONFIRM_STATUS);
        if (CollectionUtils.isNotEmpty(dupTempPubIds)) {
          return dupTempPubIds.get(0);
        }
      }

      Integer pubTypeId = ImportPubXmlUtils.parsePubType(importXml);
      // 继续导入新成果
      PubXmlProcessContext context =
          buildXmlProcessContext(XmlOperationEnum.OfflineImport, pubTypeId, currentUserId, insId);
      context.setNeedConfirmStatus(needConfirmStatus);
      Element meta = (Element) xmlDocument.getPubMeta();
      if (meta == null) {
        meta = xmlDocument.createElement(PubXmlConstants.PUB_META_XPATH);
      }
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "record_ins_id", String.valueOf(insId));
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "dbcache_xml_id", String.valueOf(xmlId));
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "batch_no", String.valueOf(0));
      SettingPubForm tmplForm = settingPubFormService.getSettingPubFormByInsId(insId);
      xmlDocument.setFormTemplate(tmplForm.getTmpFolder());
      this.rolXmlBackgroundImportProcess.start(xmlDocument, context);
      Long pubId = xmlDocument.getPubId();
      // 记录
      publicationRolService.savePubMedPubcacheInsAssign(xmlId, pubId, insId, 1);
      return pubId;

    } catch (DocumentException e) {
      logger.error("backgroundImportPubMedPubXml导入XML,转换错误", e);
      throw new ServiceException(e);
    } catch (Exception e) {
      logger.error("backgroundImportPubMedPubXml导入XML错误", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Long backgroundImportCnkiPubXml(CnkiPubCacheAssignMessage message) throws Exception {
    Long insId = message.getInsId();
    Long xmlId = message.getXmlId();
    String importXml = message.getXmlData();
    Integer pubTypeId = ImportPubXmlUtils.parsePubType(importXml);
    try {
      // get admin user id
      List<UserRole> users = this.insPersonService.getSysAdministrator();
      if (users.size() == 0) {
        throw new Exception("===SysAdministratorNotFound!===");
      }
      long currentUserId = users.get(0).getId().getUserId();

      // 进行查重，如果重复，则不导入
      PubXmlDocument xmlDocument = new PubXmlDocument(importXml);
      Element pubEle = (Element) xmlDocument.getPublication();
      List<Long> dupPubIds = backImportCnkiPubGetDupPub(insId, xmlDocument);
      if (dupPubIds != null && dupPubIds.size() > 0) {
        // 更新资助基金信息
        fillFundInfoByDupXml(pubEle, dupPubIds.get(0));
        // 记录
        publicationRolService.saveCnkiPubcacheInsAssign(xmlId, dupPubIds.get(0), insId, 0);
        return dupPubIds.get(0);
      }

      Integer needConfirmStatus = this.insConfirmService.findInsConfirmStatus(insId);
      // 需要单位确认，查临时库成果，查到忽略
      if (InsConfirm.NEED_CONFIRM.equals(needConfirmStatus)) {
        List<Long> dupTempPubIds =
            this.rolPubDupService.getStrictDupPubByImportPub(pubEle, insId, PubDupFields.INS_NOT_CONFIRM_STATUS);
        if (CollectionUtils.isNotEmpty(dupTempPubIds)) {
          return dupTempPubIds.get(0);
        }
      }
      // 继续导入新成果
      PubXmlProcessContext context =
          buildXmlProcessContext(XmlOperationEnum.OfflineImport, pubTypeId, currentUserId, insId);
      context.setNeedConfirmStatus(needConfirmStatus);
      Element meta = (Element) xmlDocument.getPubMeta();
      if (meta == null) {
        meta = xmlDocument.createElement(PubXmlConstants.PUB_META_XPATH);
      }
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "record_ins_id", String.valueOf(insId));
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "dbcache_xml_id", String.valueOf(xmlId));
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "batch_no", String.valueOf(0));
      SettingPubForm tmplForm = settingPubFormService.getSettingPubFormByInsId(insId);
      xmlDocument.setFormTemplate(tmplForm.getTmpFolder());
      this.rolXmlBackgroundImportProcess.start(xmlDocument, context);
      Long pubId = xmlDocument.getPubId();
      // 记录
      publicationRolService.saveCnkiPubcacheInsAssign(xmlId, pubId, insId, 1);
      return pubId;

    } catch (DocumentException e) {
      logger.error("backgroundImportCnkiPubXml导入XML,转换错误", e);
      throw new ServiceException(e);
    } catch (Exception e) {
      logger.error("backgroundImportCnkiPubXml导入XML错误", e);
      throw new ServiceException(e);
    }
  }

  private List<Long> backImportCnkiPubGetDupPub(Long insId, PubXmlDocument xmlDocument) throws ServiceException {
    // String doi =
    // xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH,
    // "doi");
    Element pubNode = (Element) xmlDocument.getPublication();
    Journal journal = getJournal(pubNode);
    if (journal != null) {
      pubNode.addAttribute("jid", journal.getId().toString());
    }
    List<Long> dupPubIds = rolPubDupService.getDupPubByImportPubAll(pubNode, insId);
    return dupPubIds;
  }

  @Override
  public Long backgroundImportCnkiPatPubXml(CnkiPatPubCacheAssignMessage message) throws Exception {
    Long insId = message.getInsId();
    Long xmlId = message.getXmlId();
    String importXml = message.getXmlData();
    Integer pubTypeId = ImportPubXmlUtils.parsePubType(importXml);
    try {
      // get admin user id
      List<UserRole> users = this.insPersonService.getSysAdministrator();
      if (users.size() == 0) {
        throw new Exception("===SysAdministratorNotFound!===");
      }
      long currentUserId = users.get(0).getId().getUserId();

      // 进行查重，如果重复，则不导入
      PubXmlDocument xmlDocument = new PubXmlDocument(importXml);
      List<Long> dupPubIds = backImportCniprPubGetDupPub(insId, xmlDocument);
      if (dupPubIds != null && dupPubIds.size() > 0) {
        // 记录
        publicationRolService.saveCnkiPatPubcacheInsAssign(xmlId, dupPubIds.get(0), insId, 0);
        return dupPubIds.get(0);
      }

      Integer needConfirmStatus = this.insConfirmService.findInsConfirmStatus(insId);
      // 需要单位确认，查临时库成果，查到忽略
      Element pubEle = (Element) xmlDocument.getPublication();
      if (InsConfirm.NEED_CONFIRM.equals(needConfirmStatus)) {
        List<Long> dupTempPubIds =
            this.rolPubDupService.getStrictDupPubByImportPub(pubEle, insId, PubDupFields.INS_NOT_CONFIRM_STATUS);
        if (CollectionUtils.isNotEmpty(dupTempPubIds)) {
          return dupTempPubIds.get(0);
        }
      }
      // 继续导入新成果
      PubXmlProcessContext context =
          buildXmlProcessContext(XmlOperationEnum.OfflineImport, pubTypeId, currentUserId, insId);
      context.setNeedConfirmStatus(needConfirmStatus);
      Element meta = (Element) xmlDocument.getPubMeta();
      if (meta == null) {
        meta = xmlDocument.createElement(PubXmlConstants.PUB_META_XPATH);
      }
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "record_ins_id", String.valueOf(insId));
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "dbcache_xml_id", String.valueOf(xmlId));
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "batch_no", String.valueOf(0));
      SettingPubForm tmplForm = settingPubFormService.getSettingPubFormByInsId(insId);
      xmlDocument.setFormTemplate(tmplForm.getTmpFolder());
      this.rolXmlBackgroundImportProcess.start(xmlDocument, context);
      Long pubId = xmlDocument.getPubId();
      // 记录
      publicationRolService.saveCnkiPatPubcacheInsAssign(xmlId, pubId, insId, 1);
      return pubId;

    } catch (DocumentException e) {
      logger.error("backgroundImportCnkiPatPubXml导入XML,转换错误xmlId=" + xmlId, e);
      throw new ServiceException(e);
    } catch (Exception e) {
      logger.error("backgroundImportCnkiPatPubXml导入XML错误xmlId=" + xmlId, e);
      throw new ServiceException(e);
    }
  }

  private List<Long> backImportCniprPubGetDupPub(Long insId, PubXmlDocument xmlDocument) throws ServiceException {

    Element pubNode = (Element) xmlDocument.getPublication();
    List<Long> dupPubIds = rolPubDupService.getStrictDupPubByImportPub(pubNode, insId);
    return dupPubIds;
  }

  @Override
  public Long backgroundImportCniprPubXml(CniprPubCacheAssignMessage message) throws Exception {
    Long insId = message.getInsId();
    Long xmlId = message.getXmlId();
    String importXml = message.getXmlData();
    Integer pubTypeId = ImportPubXmlUtils.parsePubType(importXml);
    try {
      // get admin user id
      List<UserRole> users = this.insPersonService.getSysAdministrator();
      if (users.size() == 0) {
        throw new Exception("===SysAdministratorNotFound!===");
      }
      long currentUserId = users.get(0).getId().getUserId();

      // 进行查重，如果重复，则不导入
      PubXmlDocument xmlDocument = new PubXmlDocument(importXml);
      List<Long> dupPubIds = backImportCniprPubGetDupPub(insId, xmlDocument);
      if (dupPubIds != null && dupPubIds.size() > 0) {
        // 记录
        publicationRolService.saveCniprPubcacheInsAssign(xmlId, dupPubIds.get(0), insId, 0);
        return dupPubIds.get(0);
      }

      Integer needConfirmStatus = this.insConfirmService.findInsConfirmStatus(insId);
      // 需要单位确认，查临时库成果，查到忽略
      Element pubEle = (Element) xmlDocument.getPublication();
      if (InsConfirm.NEED_CONFIRM.equals(needConfirmStatus)) {
        List<Long> dupTempPubIds =
            this.rolPubDupService.getStrictDupPubByImportPub(pubEle, insId, PubDupFields.INS_NOT_CONFIRM_STATUS);
        if (CollectionUtils.isNotEmpty(dupTempPubIds)) {
          return dupTempPubIds.get(0);
        }
      }
      // 继续导入新成果
      PubXmlProcessContext context =
          buildXmlProcessContext(XmlOperationEnum.OfflineImport, pubTypeId, currentUserId, insId);
      context.setNeedConfirmStatus(needConfirmStatus);
      Element meta = (Element) xmlDocument.getPubMeta();
      if (meta == null) {
        meta = xmlDocument.createElement(PubXmlConstants.PUB_META_XPATH);
      }
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "record_ins_id", String.valueOf(insId));
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "dbcache_xml_id", String.valueOf(xmlId));
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "batch_no", String.valueOf(0));
      SettingPubForm tmplForm = settingPubFormService.getSettingPubFormByInsId(insId);
      xmlDocument.setFormTemplate(tmplForm.getTmpFolder());
      this.rolXmlBackgroundImportProcess.start(xmlDocument, context);
      Long pubId = xmlDocument.getPubId();
      // 记录
      publicationRolService.saveCniprPubcacheInsAssign(xmlId, pubId, insId, 1);
      return pubId;

    } catch (DocumentException e) {
      logger.error("backgroundImportCniprPubXml导入XML,转换错误", e);
      throw new ServiceException(e);
    } catch (Exception e) {
      logger.error("backgroundImportCniprPubXml导入XML错误", e);
      throw new ServiceException(e);
    }
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public Long backgroundImportEiPubXml(EiPubCacheAssignMessage message) throws Exception {
    Long insId = message.getInsId();
    Long xmlId = message.getXmlId();
    String importXml = message.getXmlData();
    try {
      // get admin user id
      List<UserRole> users = this.insPersonService.getSysAdministrator();
      if (users.size() == 0) {
        throw new Exception("SysAdministrator Not Found !");
      }
      long currentUserId = users.get(0).getId().getUserId();
      // 进行isi_id查重，如果重复，则不导入，只更新引用次数
      PubXmlDocument xmlDocument = new PubXmlDocument(importXml);
      // 进行查重，如果重复，则不导入，只更新引用次数
      Element pubEle = (Element) xmlDocument.getPublication();
      Journal journal = getJournal(pubEle);
      if (journal != null) {
        pubEle.addAttribute("jid", journal.getId().toString());
      }

      // 正常成果严格查重
      List<Long> dupPubIds = this.rolPubDupService.getStrictDupPubByImportPub(pubEle, insId);
      if (dupPubIds != null && dupPubIds.size() > 0) {
        this.fillInfoByDupXml(pubEle, dupPubIds.get(0));
        // 记录
        publicationRolService.saveEiPubcacheInsAssign(xmlId, dupPubIds.get(0), insId, 0);
        return dupPubIds.get(0);
      }

      Integer needConfirmStatus = this.insConfirmService.findInsConfirmStatus(insId);
      // 需要单位确认，查临时库成果，查到忽略
      if (InsConfirm.NEED_CONFIRM.equals(needConfirmStatus)) {
        List<Long> dupTempPubIds =
            this.rolPubDupService.getStrictDupPubByImportPub(pubEle, insId, PubDupFields.INS_NOT_CONFIRM_STATUS);
        if (CollectionUtils.isNotEmpty(dupTempPubIds)) {
          return dupTempPubIds.get(0);
        }
      }

      Integer pubTypeId = ImportPubXmlUtils.parsePubType(importXml);
      // 继续导入新成果
      PubXmlProcessContext context =
          buildXmlProcessContext(XmlOperationEnum.OfflineImport, pubTypeId, currentUserId, insId);
      context.setNeedConfirmStatus(needConfirmStatus);
      Element meta = (Element) xmlDocument.getPubMeta();
      if (meta == null) {
        meta = xmlDocument.createElement(PubXmlConstants.PUB_META_XPATH);
      }
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "record_ins_id", String.valueOf(insId));
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "dbcache_xml_id", String.valueOf(xmlId));
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "batch_no", String.valueOf(0));
      SettingPubForm tmplForm = settingPubFormService.getSettingPubFormByInsId(insId);
      xmlDocument.setFormTemplate(tmplForm.getTmpFolder());
      this.rolXmlBackgroundImportProcess.start(xmlDocument, context);
      Long pubId = xmlDocument.getPubId();
      // 记录
      publicationRolService.saveEiPubcacheInsAssign(xmlId, pubId, insId, 1);
      return pubId;

    } catch (DocumentException e) {
      logger.error("backgroundImportEiPubXml导入XML,转换错误", e);
      throw new ServiceException(e);
    } catch (Exception e) {
      logger.error("backgroundImportEiPubXml导入XML错误", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 更新重复成果中的资助基金信息
   * 
   */
  private void fillFundInfoByDupXml(Element dupPubNode, Long pubId) throws ServiceException {
    try {
      PublicationRol pub = this.publicationRolDao.get(pubId);
      PublicationXml pubXml = this.publicationXmlService.rolGetById(pubId);
      PubXmlDocument remainDoc = new PubXmlDocument(pubXml.getXmlData());

      // 为成果添加成果所属基金信息
      String fundInfo = StringUtils.trimToEmpty(dupPubNode.attributeValue("fundinfo"));
      remainDoc.setNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "fundinfo", fundInfo);

      // 如果cnki分类号为空，添加
      String preCategoryNo = remainDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "journal_category_no");
      if (StringUtils.isBlank(preCategoryNo)) {
        String categoryNo = StringUtils.trimToEmpty(dupPubNode.attributeValue("journal_category_no"));
        remainDoc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "journal_category_no", categoryNo);
      }

      // 为成果添加成果所属基金信息
      this.pubFundInfoService.savePubFundInfo(pub.getId(), pub.getInsId(), fundInfo);
      pub.setUpdateDate(new Date());
      this.publicationRolDao.save(pub);
      // 保存成果XML
      publicationXmlService.rolSave(pubId, remainDoc.getXmlString());

    } catch (Exception e) {
      logger.error("CNKI成果导入时，严格查重后，忽略重复成果，将成果基金信息补充到重复成果错误", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Long backgroundImportPdwhPubXml(PdwhPubCacheAssignMessage msg) {

    Long insId = msg.getInsId();
    Long xmlId = msg.getXmlId();
    String importXml = msg.getXmlData();
    Integer dbid = msg.getDbid();
    try {
      // get admin user id
      List<UserRole> users = this.insPersonService.getSysAdministrator();
      if (users.size() == 0) {
        throw new Exception("SysAdministrator Not Found !");
      }
      long currentUserId = users.get(0).getId().getUserId();
      // 进行isi_id查重，如果重复，则不导入，只更新引用次数
      PubXmlDocument xmlDocument = new PubXmlDocument(importXml);
      // 进行查重，如果重复，则不导入，只更新引用次数
      Element pubEle = (Element) xmlDocument.getPublication();
      // 只有cnkiPat沒用到getJournal
      if (dbid == 21) {
        Journal journal = getJournal(pubEle);
        if (journal != null) {
          pubEle.addAttribute("jid", journal.getId().toString());
        }
      }
      List<Long> dupPubIds = new ArrayList<Long>();
      // 正常成果严格查重
      // cnki用的是這個方法查重
      if (dbid == 4) {
        dupPubIds = rolPubDupService.getDupPubByImportPubAll(pubEle, insId);
      } else {
        // 其他
        dupPubIds = this.rolPubDupService.getStrictDupPubByImportPub(pubEle, insId);
      }
      if (dupPubIds != null && dupPubIds.size() > 0) {
        this.fillPubInfoByDupXml(pubEle, dupPubIds.get(0), dbid, xmlId);
        // 记录
        // publicationRolService.saveEiPubcacheInsAssign(xmlId,dupPubIds.get(0),
        // insId, 0);
        publicationRolService.savePdwhPubCacheInsAssign(xmlId, dupPubIds.get(0), insId, 0);
        return dupPubIds.get(0);
      }

      Integer needConfirmStatus = this.insConfirmService.findInsConfirmStatus(insId);
      // 需要单位确认，查临时库成果，查到忽略
      if (InsConfirm.NEED_CONFIRM.equals(needConfirmStatus)) {
        List<Long> dupTempPubIds =
            this.rolPubDupService.getStrictDupPubByImportPub(pubEle, insId, PubDupFields.INS_NOT_CONFIRM_STATUS);
        if (CollectionUtils.isNotEmpty(dupTempPubIds)) {
          return dupTempPubIds.get(0);
        }
      }
      Integer pubTypeId = ImportPubXmlUtils.parsePubType(importXml);
      // 继续导入新成果
      PubXmlProcessContext context =
          buildXmlProcessContext(XmlOperationEnum.OfflineImport, pubTypeId, currentUserId, insId);
      context.setNeedConfirmStatus(needConfirmStatus);
      context.setSourceDbId(Long.valueOf(dbid));
      Element meta = (Element) xmlDocument.getPubMeta();
      if (meta == null) {
        meta = xmlDocument.createElement(PubXmlConstants.PUB_META_XPATH);
      }
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "record_ins_id", String.valueOf(insId));
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "dbcache_xml_id", String.valueOf(xmlId));
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "batch_no", String.valueOf(0));
      SettingPubForm tmplForm = settingPubFormService.getSettingPubFormByInsId(insId);
      xmlDocument.setFormTemplate(tmplForm.getTmpFolder());
      this.rolXmlBackgroundImportProcess.start(xmlDocument, context);
      Long pubId = xmlDocument.getPubId();
      // 记录
      publicationRolService.savePdwhPubCacheInsAssign(xmlId, pubId, insId, 1);
      return pubId;

    } catch (DocumentException e) {
      logger.error("backgroundImportPdwhPubXml导入XML,转换错误", e);
      throw new ServiceException(e);
    } catch (Exception e) {
      logger.error("backgroundImportPdwhPubXml导入XML错误", e);
      throw new ServiceException(e);
    }

  }

  private void fillPubInfoByDupXml(Element dupPubNode, Long pubId, Integer dbid, Long xmlId) {
    try {
      PublicationRol pub = this.publicationRolDao.get(pubId);
      PublicationXml pubXml = this.publicationXmlService.rolGetById(pubId);
      PubXmlDocument remainDoc = new PubXmlDocument(pubXml.getXmlData());

      boolean refreshJnlOAType = false;
      // 为成果添加成果所属基金信息
      String fundInfo = StringUtils.trimToEmpty(dupPubNode.attributeValue("fundinfo"));
      remainDoc.setNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "fundinfo", fundInfo);
      // 更新分类号
      String journalCategoryNo = StringUtils.trimToEmpty(dupPubNode.attributeValue("journal_category_no"));
      if (StringUtils.isNotEmpty(journalCategoryNo)) {
        remainDoc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "journal_category_no", journalCategoryNo);
      }

      String source = StringUtils.trimToEmpty(dupPubNode.attributeValue("source"));

      // 更新sc
      String scienceCategory = StringUtils.trimToEmpty(dupPubNode.attributeValue("sc"));
      if (StringUtils.isNotBlank(scienceCategory)) {
        remainDoc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "sc", scienceCategory);
      }

      // 更新引用情况
      Integer citedTimes =
          IrisNumberUtils.createInteger(StringUtils.trimToEmpty(dupPubNode.attributeValue("cite_times")));
      Integer eiCitedTimes =
          IrisNumberUtils.createInteger(StringUtils.trimToEmpty(dupPubNode.attributeValue("ei_cite_times")));
      Integer cnkiCiteTimes =
          IrisNumberUtils.createInteger(StringUtils.trimToEmpty(dupPubNode.attributeValue("cnki_cite_times")));
      Integer cniprCiteTimes =
          IrisNumberUtils.createInteger(StringUtils.trimToEmpty(dupPubNode.attributeValue("cnipr_cite_times")));
      if (eiCitedTimes != null && eiCitedTimes != 0) {
        remainDoc.setNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "ei_cite_times", eiCitedTimes.toString());
        String eiCitedRecordUrl = StringUtils.trimToEmpty(dupPubNode.attributeValue("ei_cite_record_url"));
        if (StringUtils.isNotBlank(eiCitedRecordUrl)) {
          if (dbid == 14) {
            pub.setCitedUrl(StringUtils.substring(eiCitedRecordUrl, 0, 3000));
          }
          remainDoc.setNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "ei_cite_record_url", eiCitedRecordUrl);
        }
      }
      if (cnkiCiteTimes != null && cnkiCiteTimes != 0) {
        remainDoc.setNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cnki_cite_record_url", cnkiCiteTimes.toString());
        String cnkiCitedRecordUrl = StringUtils.trimToEmpty(dupPubNode.attributeValue("cnki_cite_record_url"));
        if (StringUtils.isNotBlank(cnkiCitedRecordUrl)) {
          if (dbid == 4) {
            pub.setCitedUrl(StringUtils.substring(cnkiCitedRecordUrl, 0, 3000));
          }
          remainDoc.setNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cnki_cite_record_url", cnkiCitedRecordUrl);
        }
      }
      if (cniprCiteTimes != null && cniprCiteTimes != 0) {
        remainDoc.setNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cnipr_cite_record_url",
            cniprCiteTimes.toString());
        String cniprCitedRecordUrl = StringUtils.trimToEmpty(dupPubNode.attributeValue("cnipr_cite_record_url"));
        if (StringUtils.isNotBlank(cniprCitedRecordUrl)) {
          if (dbid == 11) {
            pub.setCitedUrl(StringUtils.substring(cniprCitedRecordUrl, 0, 3000));
          }
          remainDoc.setNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cnipr_cite_record_url", cniprCitedRecordUrl);
        }
      }
      if (citedTimes != null && citedTimes != 0) {
        remainDoc.setNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cite_times", citedTimes.toString());
        String citedRecordUrl = StringUtils.trimToEmpty(dupPubNode.attributeValue("cite_record_url"));
        if (StringUtils.isNotBlank(citedRecordUrl)) {
          if (dbid != 14 && dbid != 11 && dbid != 4) {
            pub.setCitedUrl(StringUtils.substring(citedRecordUrl, 0, 3000));
          }
          pub.setCitedUrl(StringUtils.substring(citedRecordUrl, 0, 3000));
          remainDoc.setNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cited_url", citedRecordUrl);
        }
      }

      // 更新收录情况
      /*
       * String citationIndex = StringUtils.trimToEmpty(dupPubNode .attributeValue("citation_index")); if
       * (StringUtils.isNotBlank(citationIndex)) { remainDoc.fillPubListByCitationIndex(citationIndex); }
       * 
       * if (StringUtils.isNotBlank(source) && "EI".equalsIgnoreCase(source)) {
       * remainDoc.setXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ei", "1");
       * remainDoc.setXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ei_source", "1"); }
       */
      PdwhPubSourceDb pubSourceDb = pdwhPubSourceDbDao.get(xmlId);
      if (pubSourceDb != null) {
        remainDoc.fillPubList(pubSourceDb, dbid);
      }
      // 补充sps_id、ei_id、isi_id
      String sourceId = StringUtils.trimToNull(dupPubNode.attributeValue("source_id"));
      if (sourceId != null && dbid == 8) {
        String spsId = remainDoc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "sps_id");
        if (StringUtils.isBlank(spsId)) {
          remainDoc.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "sps_id", sourceId);
        }
      } else if (sourceId != null && dbid == 14) {
        String eiId = remainDoc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "ei_id");
        if (StringUtils.isBlank(eiId)) {
          remainDoc.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "ei_id", sourceId);
        }
      } else if (sourceId != null && (dbid == 15 || dbid == 16 || dbid == 17)) {
        String isiId = remainDoc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "isi_id");
        if (StringUtils.isBlank(isiId)) {
          remainDoc.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "isi_id", sourceId);
          pub.setIsiId(isiId);
        }
      }
      // 补充isbn
      String isbn = StringUtils.substring(StringUtils.trimToNull(dupPubNode.attributeValue("isbn")), 0, 40);
      if (isbn != null) {
        String preIsbn = remainDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "isbn");
        if (StringUtils.isBlank(preIsbn)) {
          remainDoc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "isbn", isbn);
          pub.setIsbn(isbn);
        }
      }
      // 补充issue
      String issue = StringUtils.substring(StringUtils.trimToNull(dupPubNode.attributeValue("issue")), 0, 20);
      if (issue != null) {
        String preIssue = remainDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "issue");
        if (StringUtils.isBlank(preIssue)) {
          remainDoc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "issue", issue);
          pub.setIssue(issue);
        }
      }
      // 补充volume
      String volume = StringUtils.substring(StringUtils.trimToNull(dupPubNode.attributeValue("volume")), 0, 20);
      if (volume != null) {
        String prevolume = remainDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "volume");
        if (StringUtils.isBlank(prevolume)) {
          remainDoc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "volume", volume);
          pub.setVolume(volume);
        }
      }
      // 如果重复成果为期刊，完善期刊信息
      Integer pubtype = IrisNumberUtils.createInteger(StringUtils.trimToEmpty(dupPubNode.attributeValue("pub_type")));
      if (remainDoc.isJournalArticle() && (pubtype != null && pubtype == PublicationTypeEnum.JOURNAL_ARTICLE)) {
        Long prejid =
            IrisNumberUtils.createLong(remainDoc.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jid"));
        if (prejid == null) {
          Long jid = IrisNumberUtils.createLong(dupPubNode.attributeValue("jid"));
          if (jid != null) {
            Journal journal = journalService.getById(jid);
            if (journal != null) {
              // 期刊统一放在1节点
              remainDoc.fillPubJournalByJournal(journal, IPubXmlServiceFactory.FIRST_NODE_ID);
              pub.setJid(jid);
              refreshJnlOAType = true;
            }
          } else {
            String journalName = XmlUtil.changeSBCChar(StringUtils.trimToEmpty(dupPubNode.attributeValue("original")));
            String issn = XmlUtil.changeSBCChar(StringUtils.trimToEmpty(dupPubNode.attributeValue("issn")));
            if (StringUtils.isNotBlank(journalName)) {
              Journal journal =
                  journalService.findJournalByNameIssn(journalName, issn, SecurityUtils.getCurrentUserId());
              if (journal == null) {
                String journamFrom = dupPubNode.attributeValue("source_db_code");
                journal = journalService.addJournal(journalName, issn, SecurityUtils.getCurrentUserId(), journamFrom);
              }
              if (journal != null) {
                // 期刊统一放在1节点
                remainDoc.fillPubJournalByJournal(journal, IPubXmlServiceFactory.FIRST_NODE_ID);
                pub.setJid(jid);
              }
            }
          }
        }
      }
      // 保存成果XML
      publicationXmlService.rolSave(pubId, remainDoc.getXmlString());
      // 为成果添加成果所属基金信息
      this.pubFundInfoService.savePubFundInfo(pub.getId(), pub.getInsId(), fundInfo);
      // 收录情况
      // this.publicationListRolService.praseSourcePubList(remainDoc);
      this.publicationListRolService.praseAndSavePubList(remainDoc);
      // 查重数据
      this.publicationRolService.parsePubDupFields(remainDoc, pub);
      // 重构brief
      this.rebuildPublicationBrief(remainDoc, pub);
      pub.setUpdateDate(new Date());
      this.publicationRolDao.save(pub);
      // 保存成果XML
      publicationXmlService.rolSave(pubId, remainDoc.getXmlString());
      // 刷新期刊-开放存储类型
      if (refreshJnlOAType) {
        this.jnlOATypeRefreshService.saveJnlOATypeRefresh(pub.getJid());
      }
    } catch (Exception e) {
      logger.error("成果导入时，严格查重后，忽略重复成果，将忽略成果的收录情况等补充到重复成果错误", e);
      throw new ServiceException(e);
    }

  }
}
