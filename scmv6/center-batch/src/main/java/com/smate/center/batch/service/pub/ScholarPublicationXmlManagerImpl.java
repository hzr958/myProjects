/**
 * 
 */
package com.smate.center.batch.service.pub;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

import com.smate.center.batch.constant.BatchConfConstant;
import com.smate.center.batch.dao.sns.pub.PubDataStoreDao;
import com.smate.center.batch.enums.pub.PublicationEnterFormEnum;
import com.smate.center.batch.enums.pub.XmlOperationEnum;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.PublicationNotFoundException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.factory.pub.BriefDriverFactory;
import com.smate.center.batch.factory.pub.XmlValidatorFactory;
import com.smate.center.batch.model.sns.pub.IPubXmlServiceFactory;
import com.smate.center.batch.model.sns.pub.PubDataStore;
import com.smate.center.batch.model.sns.pub.PubSimple;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.model.sns.pub.Publication;
import com.smate.center.batch.model.sns.pub.PublicationForm;
import com.smate.center.batch.model.sns.pub.PublicationXml;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.process.pub.IPubXmlProcess;
import com.smate.center.batch.process.pub.PubImportXmlTranslateProcess;
import com.smate.center.batch.process.pub.PubXmlImportProcess;
import com.smate.center.batch.service.pdwh.pub.PublicationAllService;
import com.smate.center.batch.service.pdwh.pub.PublicationXmlPdwhService;
import com.smate.center.batch.util.pub.PubConstFieldRefresh;
import com.smate.center.batch.util.pub.PubXmlDocumentBuilder;
import com.smate.center.batch.util.pub.PubXmlObjectUtil;
import com.smate.center.batch.util.pub.PublicationArticleType;
import com.smate.core.base.psn.consts.PsnCnfConst;
import com.smate.core.base.psn.model.psncnf.PsnConfigPub;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.exception.BriefDriverNotFoundException;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.pub.BriefFormatter;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 成果XML处理服务(导入、修改、新增).
 * 
 * @author yamingd
 */
public class ScholarPublicationXmlManagerImpl implements ScholarPublicationXmlManager {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private IPubXmlProcess scholarXmlSaveProcess;

  private IPubXmlProcess scholarXmlSaveComplementaryProcess;

  private PubImportXmlTranslateProcess importXmlTranslateProcess;

  private PubXmlImportProcess pubXmlImportProcess;

  /**
   * 成果Brief生成驱动工厂.
   */
  private BriefDriverFactory briefDriverFactory;
  /**
   * Xml校验工厂.
   */
  private XmlValidatorFactory xmlValidatorFactory;
  /**
   * Xml处理需要用到的外部服务.
   */
  @Autowired
  private IPubXmlServiceFactory scholarPublicationXmlServiceFactory;
  @Autowired
  private PsnCnfService psnCnfService;
  @Autowired
  private PublicationXmlService publicationXmlService;
  @Autowired
  private PublicationXmlPdwhService publicationXmlPdwhService;
  @Autowired
  PublicationAllService publicationAllService;
  @Autowired
  private PubSimpleService pubSimpleService;
  @Autowired
  private PubDataStoreDao pubDataStoreDao;
  @Autowired
  private PublicationService publicationService;

  @Override
  public Integer createXmlNew(String newXmlData, int pubTypeId, int articleType, PubSimple pubSimple) {
    try {
      PubXmlDocument xmlDocument = new PubXmlDocument(newXmlData);
      // 构造全局变量对象
      Long psnId = xmlDocument.getExpandPsnId();
      String localStr = xmlDocument.getExpandLocal();
      PubXmlProcessContext context = this.buildXmlProcessContext(psnId, pubTypeId, localStr, pubSimple.getUpdateDate());
      context.setArticleType(articleType);
      context.setCurrentAction(XmlOperationEnum.Enter);
      context.setPubSimple(pubSimple);
      this.scholarXmlSaveProcess.start(xmlDocument, context);
      // 更新pubSImple的状态
      pubSimple.setSimpleStatus(1L);
      pubSimple.setSimpleTask(1);
      pubSimpleService.save(pubSimple);
      // 文件夹与群组的相关保存在第一阶段完成
      return BatchConfConstant.JOB_SUCCESS;
    } catch (Exception e) {
      // TODO 自定义异常
      logger.error("保存XML错误", e);
      pubSimple.setSimpleStatus(99L);
      pubSimpleService.save(pubSimple);
      return BatchConfConstant.JOB_ERROR;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.scmweb.service.publication.ScholarPublicationXmlManager#
   * buildXmlProcessContext(java.lang.Long, java.lang.Integer)
   */
  @Override
  public PubXmlProcessContext buildXmlProcessContext(Long psnId, Integer typeId, String local, Date date) {
    PubXmlProcessContext context = new PubXmlProcessContext();
    // 2015-11-2 获取第一阶段保存环境语言 <pub_expand psnId="xxx" local="zh"/>
    Locale locale = Locale.CHINA;
    if (Locale.US.getLanguage().equals(local)) {
      locale = Locale.US;
    }
    context.setCurrentLanguage(locale.getLanguage());
    context.setCurrentUserId(psnId);
    context.setLocale(locale);
    context.setPubTypeId(typeId);
    context.setXmlServiceFactory(this.scholarPublicationXmlServiceFactory);
    context.setBrifDriverFactory(this.briefDriverFactory);
    context.setXmlValidatorFactory(this.xmlValidatorFactory);
    context.setCurrentNodeId(SecurityUtils.getCurrentUserNodeId());
    context.setNow(date);
    return context;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.scmweb.service.publication.ScholarPublicationXmlManager#
   * loadXml(com.iris.scm.scmweb.model.publication .PublicationForm)
   */
  @Override
  public PublicationForm loadXml(PublicationForm form) throws ServiceException, PublicationNotFoundException {
    try {
      // 获取缓存数据
      PublicationXml xml = this.publicationXmlService.getById(form.getPubId());
      if (xml == null || StringUtils.isBlank(xml.getXmlData())) {
        throw new PublicationNotFoundException(form.getPubId().toString());
      }
      String xmlData = xml.getXmlData();

      PubXmlDocument xmlDocument = new PubXmlDocument(xmlData);

      // 刷新常数字段
      PubConstFieldRefresh.refresh(xmlDocument, this.scholarPublicationXmlServiceFactory);

      this.checkContainsHtml(xmlDocument, form);
      form.setTypeId(xmlDocument.getPubTypeId());
      form.setArticleType(xmlDocument.getArticleTypeId());
      form.setPubXml(xmlDocument.getXmlString());
      form.setOwnerPsnId(xmlDocument.getOwerPsnId());
      checkPubAuthority(form, xmlDocument);
      return form;
    } catch (DaoException e) {
      logger.error("loadXml加载XML错误, pubId=" + form.getPubId(), e);
      throw new ServiceException(e);
    } catch (PublicationNotFoundException e) {
      throw e;
    } catch (Exception e) {
      logger.error("loadXml加载XML,转换为XmlDocument错误, pubId=" + form.getPubId(), e);
      throw new ServiceException(e);
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

  /**
   * 检测权限.
   * 
   * @param form
   * @param xmlDocument
   */
  private void checkPubAuthority(PublicationForm form, PubXmlDocument xmlDocument) {
    try {
      // 刷新权限
      if (xmlDocument.getOwerPsnId() > 0) {
        PsnConfigPub cnfPub = new PsnConfigPub();
        cnfPub.getId().setPubId(form.getPubId());
        PsnConfigPub cnfPubExists = psnCnfService.get(xmlDocument.getOwerPsnId(), cnfPub);
        if (cnfPubExists == null) {
          form.setAuthority(PsnCnfConst.ALLOWS.toString());
        } else {
          form.setAuthority(cnfPubExists.getAnyUser().toString());
        }
      }

    } catch (Exception e) {
      logger.error("获取成果隐私设置出错", e);
    }
  }

  @Override
  public String getLanguagesBriefDesc(Locale locale, PubXmlProcessContext context, PubXmlDocument xmlDocument,
      Integer typeId) throws ServiceException {
    try {
      IBriefDriver briefDriver = briefDriverFactory.getDriver(PublicationEnterFormEnum.SCHOLAR, typeId);
      Map result = briefDriver.getData(locale, xmlDocument, context);
      String pattern = briefDriver.getPattern();
      BriefFormatter formatter = new BriefFormatter(locale, result);
      return formatter.format(pattern);
    } catch (BriefDriverNotFoundException e) {
      logger.error("getLanguagesBriefDesc找不到类型typeId={}对应的BriefDriver", typeId, e);
      throw new ServiceException(e);
    } catch (Exception e) {
      logger.error("getLanguagesBriefDesc错误", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public PublicationForm loadPdwhXml(PublicationForm form) throws ServiceException, PublicationNotFoundException {
    try {
      // 获取缓存数据
      PublicationXml xml = publicationXmlPdwhService.getPdwhPubXmlById(form.getPubId(), form.getDbid());
      if (xml == null || StringUtils.isBlank(xml.getXmlData())) {
        throw new PublicationNotFoundException(form.getPubId().toString());
      }
      String xmltop = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
      String xmlData = xmltop + xml.getXmlData();

      PubXmlDocument xmlDocument = new PubXmlDocument(xmlData);
      String zhTitle = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "ctitle");
      String enTitle = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "etitle");
      Locale locale = LocaleContextHolder.getLocale();
      String title = "";
      if ("zh".equalsIgnoreCase(locale.getLanguage())) {
        title = zhTitle;
        if (StringUtils.isBlank(title)) {
          title = enTitle;
        }
      } else {
        title = enTitle;
        if (StringUtils.isBlank(title)) {
          title = zhTitle;
        }
      }
      Map map = publicationAllService.getBriefDesc(form.getPubId(), form.getDbid());
      if (map != null) {
        String briefDescZh = ObjectUtils.toString(map.get("briefDescZh"));
        if (StringUtils.isNotBlank(briefDescZh)) {
          briefDescZh = briefDescZh.replace(">", "&gt;").replace("<", "&lt;");
          briefDescZh = XmlUtil.formateSymbol(title, briefDescZh);
        }
        String briefDescEn = ObjectUtils.toString(map.get("briefDescEn"));
        if (StringUtils.isNotBlank(briefDescEn)) {
          briefDescEn = briefDescEn.replace(">", "&gt;").replace("<", "&lt;");
          briefDescEn = XmlUtil.formateSymbol(title, briefDescEn);
        }
        xmlDocument.setNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc", briefDescZh);
        xmlDocument.setNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc_en", briefDescEn);
      }
      // 刷新常数字段
      Document doc = DocumentHelper.parseText(xmlData);
      List nodes = doc.selectNodes("/data");
      Node pitem = (Node) nodes.get(0);
      Node item = pitem.selectSingleNode("publication");

      form.setTypeId(NumberUtils.toInt(StringUtils.trimToEmpty(item.valueOf("@pub_type"))));
      // form.setArticleType(xmlDocument.getArticleTypeId());
      form.setPubXml(xmlDocument.getXmlString());
      // form.setOwnerPsnId(xmlDocument.getOwerPsnId());
      return form;
    } catch (Exception e) {
      logger.error("loadPdwhXml加载XML,转换为XmlDocument错误, pubId=" + form.getPubId() + ",dbid=" + form.getDbid(), e);
      throw new ServiceException(e);
    }
  }

  @SuppressWarnings({"rawtypes"})
  @Override
  public Integer updateXml(long pubId, String newXmlData, int pubTypeId, int articleType, PubSimple pubSimple)
      throws ServiceException {
    try {
      PubXmlDocument newDoc = new PubXmlDocument(newXmlData);
      // 构造全局变量对象
      Long psnId = newDoc.getExpandPsnId();
      String localStr = newDoc.getExpandLocal();

      // test
      Element meta = (Element) newDoc.getPubMeta();
      String recordFrom = meta.attributeValue("record_from");
      if (StringUtils.isBlank(recordFrom)) {
        String test = "test";
      }

      // 因为有的数据是从老系统publication直接转移过来的，导致xml没有expand这个节点。如果scm_pub_xml中为空，则从v_pub_data_store中取
      if (psnId == null || psnId == 0L) {
        PubDataStore pubDataStore = this.publicationXmlService.getXmlFromPubDataStore(pubId);

        if (pubDataStore == null) {
          throw new Exception("=====成果XML为空，请检查，pubId = " + pubId + " =====");
        }

        PubXmlDocument xmlDocument = new PubXmlDocument(pubDataStore.getData());
        psnId = xmlDocument.getExpandPsnId();
      }

      // TODO 如果仍然为空，则将psnid设置为成果拥有人,临时处理错误数据，处理完成后需要注释掉
      if (psnId == null || psnId == 0L) {
        PubSimple pub = this.pubSimpleService.queryPubSimple(pubId);

        if (pub == null) {
          throw new Exception("=====v_pub_simple成果为空，请检查，pubId = " + pubId + " =====");
        }
        psnId = pub.getOwnerPsnId();
      }

      if (psnId == null || psnId == 0L) {
        // XML没有expand psnid节点时,返回报错
        throw new Exception("=====成果节点/pub_expand中无psn_id，没有记录到操作人员ID，请检查，pubId = " + pubId + " =====");
      }

      PubXmlProcessContext context = this.buildXmlProcessContext(psnId, pubTypeId, localStr, pubSimple.getUpdateDate());
      context.setCurrentAction(XmlOperationEnum.Edit);
      context.setCurrentPubId(pubId);
      context.setArticleType(articleType);
      context.setPubSimple(pubSimple);

      String newAuthority = newDoc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "authority");

      // read old-pub-meta and copy to xmlDocument
      PublicationXml pubXml = this.publicationXmlService.getById(pubId);
      PubXmlDocument oldDoc = new PubXmlDocument(pubXml.getXmlData());
      Element oldMeta = (Element) oldDoc.getNode(PubXmlConstants.PUB_META_XPATH);
      newDoc.copyPubElement(oldMeta);

      newDoc.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "authority", newAuthority);

      String[] attrs = PubXmlConstants.PUB_EDIT_REMAIN_PUBLICATION_ATTR;
      newDoc.fillAttributeValue(oldDoc, PubXmlConstants.PUBLICATION_XPATH, PubXmlConstants.PUBLICATION_XPATH, attrs,
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
      // 记录 原全文id
      String oldFullTextId = oldDoc.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "file_id");
      context.setOldFullTextId(oldFullTextId);
      this.scholarXmlSaveProcess.start(newDoc, context);
      // 更新pubSImple的状态
      pubSimple.setSimpleStatus(1L);
      pubSimpleService.save(pubSimple);
      return BatchConfConstant.JOB_SUCCESS;

    } catch (Exception e) {

      logger.error("保存XML错误, pubid = " + pubId + "; ", e);
      return BatchConfConstant.JOB_ERROR;

    }
  }

  public IPubXmlProcess getScholarXmlSaveProcess() {
    return scholarXmlSaveProcess;
  }

  public void setScholarXmlSaveProcess(IPubXmlProcess scholarXmlSaveProcess) {
    this.scholarXmlSaveProcess = scholarXmlSaveProcess;
  }

  public BriefDriverFactory getBriefDriverFactory() {
    return briefDriverFactory;
  }

  public void setBriefDriverFactory(BriefDriverFactory briefDriverFactory) {
    this.briefDriverFactory = briefDriverFactory;
  }

  public XmlValidatorFactory getXmlValidatorFactory() {
    return xmlValidatorFactory;
  }

  public void setXmlValidatorFactory(XmlValidatorFactory xmlValidatorFactory) {
    this.xmlValidatorFactory = xmlValidatorFactory;
  }

  public PubImportXmlTranslateProcess getImportXmlTranslateProcess() {
    return importXmlTranslateProcess;
  }

  public void setImportXmlTranslateProcess(PubImportXmlTranslateProcess importXmlTranslateProcess) {
    this.importXmlTranslateProcess = importXmlTranslateProcess;
  }

  public PubXmlImportProcess getPubXmlImportProcess() {
    return pubXmlImportProcess;
  }

  public void setPubXmlImportProcess(PubXmlImportProcess pubXmlImportProcess) {
    this.pubXmlImportProcess = pubXmlImportProcess;
  }

  @Override
  public void createOrUpdateXmlComplementaryProcesses(String actionType, int pubTypeId, int articleType,
      PubSimple pubSimple) throws BatchTaskException {
    Long pubId = pubSimple.getPubId();
    Long groupId = pubSimple.getGroupId();
    // read old-pub-meta and copy to xmlDocument
    PublicationXml pubXml = this.publicationXmlService.getById(pubId);
    try {
      PubXmlDocument oldDoc = new PubXmlDocument(pubXml.getXmlData());

      // PubXmlDocument newDoc = new PubXmlDocument(newXmlData);
      // 构造全局变量对象
      Long psnId = oldDoc.getExpandPsnId();
      String localStr = oldDoc.getExpandLocal();

      // TODO
      // 因为有的数据是从老系统publication直接转移过来的，导致xml没有expand这个节点。如果scm_pub_xml中为空，则从v_pub_data_store中取
      if (psnId == null || psnId == 0L) {
        PubDataStore pubDataStore = this.publicationXmlService.getXmlFromPubDataStore(pubId);

        if (pubDataStore == null) {
          throw new Exception("=====成果XML为空，请检查，pubId = " + pubId + " =====");
        }

        PubXmlDocument xmlDocument = new PubXmlDocument(pubDataStore.getData());
        psnId = xmlDocument.getExpandPsnId();
      }

      // 如果仍然为空，则将psnid设置为成果拥有人,临时处理错误数据，处理完成后需要注释掉
      if (psnId == null || psnId == 0L) {
        PubSimple pub = this.pubSimpleService.queryPubSimple(pubId);

        if (pub == null) {
          throw new Exception("=====v_pub_simple成果为空，请检查，pubId = " + pubId + " =====");
        }
        psnId = pub.getOwnerPsnId();
      }

      PubXmlProcessContext context = this.buildXmlProcessContext(psnId, pubTypeId, localStr, pubSimple.getUpdateDate());

      String oldFullTextId = oldDoc.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "old_file_id");
      context.setOldFullTextId(oldFullTextId);
      context.setCurrentPubId(pubId);
      context.setArticleType(articleType);
      context.setPubSimple(pubSimple);
      context.setGroupId(groupId);

      if ("edit".equalsIgnoreCase(actionType)) {
        context.setCurrentAction(XmlOperationEnum.Edit);
      } else if ("enter".equalsIgnoreCase(actionType)) {
        context.setCurrentAction(XmlOperationEnum.Enter);
      } else if ("import".equalsIgnoreCase(actionType)) {
        context.setCurrentAction(XmlOperationEnum.Import);
      } else if ("ImportPdwh".equalsIgnoreCase(actionType)) {
        context.setCurrentAction(XmlOperationEnum.ImportPdwh);
      }

      this.scholarXmlSaveComplementaryProcess.start(oldDoc, context);

      // 存储XML数据
      String xml = oldDoc.getXmlString();
      this.publicationXmlService.save(pubId, xml);

    } catch (Exception e) {
      logger.error("scholarXmlSaveComplementaryProcess处理xml出错, pubid = " + pubId + "; ", e);
      throw new BatchTaskException(e);
    }

  }

  public IPubXmlProcess getScholarXmlSaveComplementaryProcess() {
    return scholarXmlSaveComplementaryProcess;
  }

  public void setScholarXmlSaveComplementaryProcess(IPubXmlProcess scholarXmlSaveComplementaryProcess) {
    this.scholarXmlSaveComplementaryProcess = scholarXmlSaveComplementaryProcess;
  }

  public XmlOperationEnum getExpandCurrentAction(String importXmlData) throws ServiceException {
    XmlOperationEnum currentAction = XmlOperationEnum.Import;
    try {
      PubXmlDocument xmlDocument = new PubXmlDocument(importXmlData);
      String currentActionStr = xmlDocument.getExpandCurrentAction();
      try {
        currentAction = XmlOperationEnum.valueOf(currentActionStr);
      } catch (Exception e) {
        currentAction = XmlOperationEnum.Import;
      }
    } catch (DocumentException e) {
      logger.error("获取成果导入来源出错", e);
      throw new ServiceException(e);
    }
    return currentAction;
  }

  @Override
  public Long importPubXml(String importXmlData, int pubTypeId, int articleType, PubSimple pubSimple)
      throws ServiceException {
    try {
      PubXmlDocument xmlDocument = new PubXmlDocument(importXmlData);
      // 构造全局变量对象
      Long psnId = xmlDocument.getExpandPsnId();
      String localStr = xmlDocument.getExpandLocal();
      PubXmlProcessContext context = this.buildXmlProcessContext(psnId, pubTypeId, localStr, pubSimple.getUpdateDate());
      // 兼容导入个人成果库 lxz begin 2015-12-18
      String articleTypeStr = xmlDocument.getExpandArticleType();
      String currentActionStr = xmlDocument.getExpandCurrentAction();
      String groupFolderIdStr = xmlDocument.getExpandGroupFolderId();
      String privacyLevelStr = xmlDocument.getExpandPrivacyLevel();
      String groupIdStr = xmlDocument.getExpandGroupId();
      XmlOperationEnum currentAction;
      try {
        currentAction = XmlOperationEnum.valueOf(currentActionStr);
      } catch (Exception e) {
        currentAction = XmlOperationEnum.Import;
      }
      if (articleTypeStr != null && !"".equals(articleTypeStr) && NumberUtils.isNumber(articleTypeStr)) {
        articleType = Integer.parseInt(articleTypeStr);
      }
      if (groupFolderIdStr != null && !"".equals(groupFolderIdStr) && NumberUtils.isNumber(groupFolderIdStr)) {
        context.setGroupFolderId(NumberUtils.toLong(groupFolderIdStr));

      }
      if (privacyLevelStr != null && !"".equals(privacyLevelStr) && NumberUtils.isNumber(privacyLevelStr)) {
        context.setPrivacyLevel(NumberUtils.toInt(privacyLevelStr));

      }
      if (groupIdStr != null && !"".equals(groupIdStr) && NumberUtils.isNumber(groupIdStr)) {
        context.setGroupId(NumberUtils.toLong(groupIdStr));

      }
      // 兼容导入个人成果库 end
      context.setPubSimple(pubSimple);
      context.setArticleType(articleType);
      context.setCurrentAction(currentAction);
      this.scholarXmlSaveProcess.start(xmlDocument, context);
      // 更新pubSImple的状态
      pubSimple.setSimpleStatus(1L);
      pubSimple.setSimpleTask(1);
      pubSimpleService.save(pubSimple);
      Long pubId = xmlDocument.getPubId();
      return pubId;
    } catch (DocumentException e) {
      logger.error("导入XML,转换错误", e);
      throw new ServiceException(e);
    } catch (Exception e) {
      logger.error("导入XML错误", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 用于从pdwh导入群组成果，创建项目群组
   * 
   */
  @Override
  public Long importPubXml(String importXml, Long currentUserId, int pubTypeId, Integer isToMyPub, Integer privacyLevel,
      Long groupId, String groupFolderId) throws ServiceException {

    PubXmlProcessContext context = this.buildXmlProcessContext(currentUserId, pubTypeId);
    context.setArticleType(PublicationArticleType.OUTPUT);
    context.setPrivacyLevel(privacyLevel);
    context.setCurrentAction(XmlOperationEnum.Import);
    if (isToMyPub != null && isToMyPub.intValue() == 0) {
      context.setCurrentPubStatus(4);
    }
    if (groupId != null && groupId != 0L) {
      context.setGroupId(groupId);
      context.setCurrentPubStatus(5);
    }
    if (StringUtils.isNotBlank(groupFolderId)) {
      context.setGroupFolderId(Long.valueOf(groupFolderId));
    }
    try {
      Long pubId = this.importXml(importXml, context);
      return pubId;
    } catch (DocumentException e) {
      logger.error("导入XML,转换错误", e);
      throw new ServiceException(e);
    } catch (Exception e) {
      logger.error("导入XML错误", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public PubXmlProcessContext buildXmlProcessContext(Long psnId, Integer typeId) {
    PubXmlProcessContext context = new PubXmlProcessContext();
    Locale locale = LocaleContextHolder.getLocale();
    context.setCurrentLanguage(locale.getLanguage());
    context.setCurrentUserId(psnId);
    context.setLocale(locale);
    context.setPubTypeId(typeId);
    context.setXmlServiceFactory(this.scholarPublicationXmlServiceFactory);
    context.setBrifDriverFactory(this.briefDriverFactory);
    context.setXmlValidatorFactory(this.xmlValidatorFactory);
    context.setCurrentNodeId(ServiceConstants.SCHOLAR_NODE_ID_1);
    return context;
  }

  @Override
  public PubXmlDocument translateImportXml(PubXmlProcessContext context, String importXml) throws SysServiceException {
    context.setArticleType(PublicationArticleType.OUTPUT);
    context.setCurrentAction(XmlOperationEnum.Import);
    try {
      PubXmlDocument xmlDocument = new PubXmlDocument(importXml);
      Element ele = xmlDocument.createElement(PubXmlConstants.PUB_TYPE_XPATH);
      ele.addAttribute("id", String.valueOf(context.getPubTypeId()));
      ele.addAttribute("article_type", String.valueOf(context.getArticleType()));
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

  @Override
  public Map<String, String> generateBriefFromImportXmlMap(PubXmlProcessContext context, PubXmlDocument xmlDocument,
      String formTmpl, Integer typeId) throws ServiceException {
    try {
      Map<String, String> briefMap = new HashMap<String, String>();
      IBriefDriver briefDriver = briefDriverFactory.getDriver(formTmpl, typeId);
      if (briefDriver != null) {
        String briefZh = getLanguagesBrief(LocaleUtils.toLocale("zh_CN"), context, xmlDocument, briefDriver);
        briefMap.put("brief_desc_zh", briefZh);
        String briefEn = getLanguagesBrief(LocaleUtils.toLocale("en_US"), context, xmlDocument, briefDriver);
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

  private String getLanguagesBrief(Locale locale, PubXmlProcessContext context, PubXmlDocument xmlDocument,
      IBriefDriver briefDriver) throws Exception {
    Map result = briefDriver.getData(locale, xmlDocument, context);
    String pattern = briefDriver.getPattern();
    BriefFormatter formatter = new BriefFormatter(locale, result);
    String brief = formatter.format(pattern);
    return brief;
  }

  /**
   * @param importXml
   * @param context
   * @return
   * @throws Exception
   */
  private Long importXml(String importXml, PubXmlProcessContext context) throws Exception {

    context.setCurrentAction(XmlOperationEnum.Import);

    PubXmlDocument xmlDocument = new PubXmlDocument(importXml);
    if (context.getPrivacyLevel() != null && context.getPrivacyLevel() > 0) {
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "authority",
          context.getPrivacyLevel().toString());
    }
    xmlDocument.setFormTemplate(PublicationEnterFormEnum.SCHOLAR);
    buildOtherXml(xmlDocument, context.getCurrentUserId(), context.getArticleType(), context.getCurrentPubStatus(),
        context.getGroupFolderId(), context.getPrivacyLevel(), context.getGroupId(), context.getCurrentAction());
    Publication oldPub = new Publication();
    pubXmlImportProcess.start(xmlDocument, context);
    // 赋值dup_action,dup_pub_id
    String dupId = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "dup_pub_id");
    String dupAction = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "dup_action");
    xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "dup_pub_id", dupId);
    xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "dup_action", dupAction);
    if (StringUtils.isNotBlank(dupId) && "1".equals(dupAction)) {
      PublicationForm dupForm = this.publicationService.getPublication(Long.valueOf(dupId));
      PubXmlDocumentBuilder.mergeWhenImport(xmlDocument, dupForm.getPubXml());
    }

    // XML转换出一个Publication对象,对应属性是PubSimple需要的
    PubXmlObjectUtil.wrapPublicationSaveField(xmlDocument, context, new Date(), oldPub);
    Long pubId = savePubNewData(xmlDocument, oldPub);
    return pubId;

  }

  private Long savePubNewData(PubXmlDocument doc, Publication pub) throws Exception {
    return pubSimpleService.savePubSimpleData(pub, doc);
  }

  /**
   * 工具函数- 构建后台任务使用参数
   *
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param doc
   * @param psnId
   * @param xmlData
   * @param articleType
   * @param pubStatus
   * @param xmlOperationEnum
   * @param oldPub
   */
  private void buildOtherXml(PubXmlDocument doc, Long psnId, Integer articleType, Integer pubStatus, Long groupFolderId,
      Integer privacyLevel, Long groupId, XmlOperationEnum xmlOperationEnum) {
    Element meta = (Element) doc.getPubMeta();
    if (meta == null) {
      meta = doc.createElement(PubXmlConstants.PUB_META_XPATH);
    }

    Element expand = (Element) doc.getPubExpand();
    if (expand == null) {
      expand = doc.createElement(PubXmlConstants.PUB_EXPAND_XPATH);
    }
    Locale locale = LocaleContextHolder.getLocale();
    // 当前操作人ID
    expand.addAttribute(PubXmlConstants.PSNID, psnId + "");
    // 语言
    expand.addAttribute(PubXmlConstants.LOCAL, locale.getLanguage());
    // 成果状态
    expand.addAttribute(PubXmlConstants.CURRENT_PUB_STATUS, pubStatus + "");
    // 成果操作动作
    expand.addAttribute(PubXmlConstants.CURRENT_ACTION, xmlOperationEnum.toString());
    // articleType
    expand.addAttribute(PubXmlConstants.ARTICLE_TYPE, articleType + "");
    if (groupFolderId != null) {
      // groupFolderId
      expand.addAttribute(PubXmlConstants.GROUP_FOLDER_ID, groupFolderId + "");
    }
    if (privacyLevel != null) {
      // privacyLevel
      expand.addAttribute(PubXmlConstants.PRIVACY_LEVEL, privacyLevel + "");
    }
    if (groupId != null) {
      // groupId
      expand.addAttribute(PubXmlConstants.GROUP_ID, groupId + "");
    }
  }
}
