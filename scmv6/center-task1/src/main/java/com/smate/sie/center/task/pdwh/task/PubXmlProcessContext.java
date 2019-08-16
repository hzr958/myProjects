package com.smate.sie.center.task.pdwh.task;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

import com.smate.center.task.model.sns.quartz.PubSimple;
import com.smate.center.task.single.enums.pub.XmlOperationEnum;
import com.smate.center.task.single.factory.pub.IPubXmlServiceFactory;
import com.smate.center.task.single.util.pub.PubXmlDbUtils;
import com.smate.sie.center.task.pdwh.brief.BriefDriverFactory;
import com.smate.sie.center.task.pdwh.validator.PubXmlValidatorFactory;

/**
 * XML组件上下文对象. 这个只能适用于成果和文献
 * 
 * @author yamingd 李毅修改
 */
public class PubXmlProcessContext implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -4156793254001997271L;
  /**
   * 当前成果ID.
   */
  private Long currentPubId;
  /**
   * 当前单位ID.
   */
  private Long currentInsId;
  /**
   * 当前用户ID.
   */
  private Long currentUserId;
  /***
   * 当前节点ID.
   */
  private Integer currentNodeId;
  /**
   * 成果的当前状态，暂时只在保存新成果时使用,参考Publication.Status.
   */
  private Integer currentPubStatus;
  /**
   * 当前站点.
   */
  private String currentSite;
  /**
   * 当前语言.
   */
  private String currentLanguage;
  /**
   * 当前操作:onlineImport/fileImport / create / edit / enter .
   */
  private XmlOperationEnum currentAction;
  /**
   * Xml处理需要用到的外部服务.
   */
  private IPubXmlServiceFactory xmlServiceFactory;
  /**
   * 当前语言.
   */
  private Locale locale;

  /**
   * 成果大类别：1(成果)/2(文献) 不要再在这里处理这两种类型/4(项目)/3(工作文档).
   */
  private int articleType;

  /**
   * 成果类别.
   */
  private int pubTypeId;
  /**
   * 成果的隐私级别.
   */
  private Integer privacyLevel;

  /**
   * Xml校验工厂.
   */
  private PubXmlValidatorFactory pubXmlValidatorFactory;
  // 群组中来
  private Long groupId;
  // 群组中的文件夹
  private Long groupFolderId;
  // 基准库成果主键
  private Long pdwhId;

  /**
   * Xml Brief字段(来源)生成驱动工厂.
   */
  private BriefDriverFactory brifDriverFactory;

  private String xmlEmailPostfix;

  // 认领状态
  private Integer authorState = 0;
  // 确认状态
  private Integer confirmResult = 0;
  // 是否发布
  private Integer isOpen = 0;
  // V2.6同步成果ID
  private Long fromPubId;
  // V2.6同步成果更新时间
  private Date updateDate;
  // 导入数据库来源.
  private Long sourceDbId;
  // 当前时间
  private Date now;
  // 是否需要确认才能导入
  private Integer needConfirmStatus;
  // 原全文id
  private String oldFullTextId;
  // 是否批量导入
  private String isBatch;

  // pubsimple
  private PubSimple pubSimple;

  public Integer getCurrentPubStatus() {
    return currentPubStatus;
  }

  public void setCurrentPubStatus(Integer currentPubStatus) {
    this.currentPubStatus = currentPubStatus;
  }

  public Long getGroupId() {
    return groupId;
  }

  public Long getGroupFolderId() {
    return groupFolderId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public void setGroupFolderId(Long groupFolderId) {
    this.groupFolderId = groupFolderId;
  }

  public Integer getPrivacyLevel() {
    return privacyLevel;
  }

  public void setPrivacyLevel(Integer privacyLevel) {
    this.privacyLevel = privacyLevel;
  }

  /**
   * @return the brifDriverFactory
   */
  public BriefDriverFactory getBrifDriverFactory() {
    return brifDriverFactory;
  }

  /**
   * @param brifDriverFactory the brifDriverFactory to set
   */
  public void setBrifDriverFactory(BriefDriverFactory brifDriverFactory) {
    this.brifDriverFactory = brifDriverFactory;
  }

  public Date getNow() {
    if (now == null) {
      now = new Date();
    }
    return now;
  }

  public void setNow(Date now) {
    this.now = now;
  }

  /**
   * 当前单位ID.
   * 
   * @return the currentInsId
   */
  public Long getCurrentInsId() {
    return currentInsId;
  }

  /**
   * @param currentInsId the currentInsId to set
   */
  public void setCurrentInsId(Long currentInsId) {
    this.currentInsId = currentInsId;
  }

  /**
   * 当前用户ID.
   * 
   * @return the currentUserId
   */
  public Long getCurrentUserId() {
    return currentUserId;
  }

  /**
   * 
   * @param currentUserId the currentUserId to set
   */
  public void setCurrentUserId(Long currentUserId) {
    this.currentUserId = currentUserId;
  }

  /**
   * 当前站点：ROL/Scholar.
   * 
   * @return the currentSite
   */
  public String getCurrentSite() {
    return currentSite;
  }

  /**
   * @param currentSite the currentSite to set
   */
  public void setCurrentSite(String currentSite) {
    this.currentSite = currentSite;
  }

  /**
   * 返回当前语言.
   * 
   * @return the currentLanguage
   */
  public String getCurrentLanguage() {
    return currentLanguage;
  }

  /**
   * 设置当前语言.
   * 
   * @param currentLanguage the currentLanguage to set
   */
  public void setCurrentLanguage(String currentLanguage) {
    this.currentLanguage = currentLanguage;
  }

  /**
   * 当前操作：edit/create/view.
   * 
   * @return the currentAction
   */
  public XmlOperationEnum getCurrentAction() {
    return currentAction;
  }

  /**
   * @param currentAction the currentAction to set
   */
  public void setCurrentAction(XmlOperationEnum currentAction) {
    this.currentAction = currentAction;
  }

  /**
   * Xml组件需要使用的外部Service的工厂.
   * 
   * @return the xmlServiceFactory
   */
  public IPubXmlServiceFactory getXmlServiceFactory() {
    return xmlServiceFactory;
  }

  /**
   * @param xmlServiceFactory the xmlServiceFactory to set
   */
  public void setXmlServiceFactory(IPubXmlServiceFactory xmlServiceFactory) {
    this.xmlServiceFactory = xmlServiceFactory;
  }

  /**
   * @param locale the locale to set
   */
  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  /**
   * @return the locale.
   */
  public Locale getLocale() {
    return locale;
  }

  /**
   * @return the articleType
   */
  public int getArticleType() {
    return articleType;
  }

  /**
   * @param articleType the articleType to set
   */
  public void setArticleType(int articleType) {
    this.articleType = articleType;
  }

  /**
   * @return the pubTypeId
   */
  public int getPubTypeId() {
    return pubTypeId;
  }

  /**
   * @param pubTypeId the pubTypeId to set
   */
  public void setPubTypeId(int pubTypeId) {
    this.pubTypeId = pubTypeId;
  }

  /**
   * @return the currentPubId
   */
  public Long getCurrentPubId() {
    return currentPubId;
  }

  /**
   * @param currentPubId the currentPubId to set
   */
  public void setCurrentPubId(Long currentPubId) {
    this.currentPubId = currentPubId;
  }

  /**
   * @param xmlValidatorFactory PubXmlValidatorFactory
   */
  public void setPubXmlValidatorFactory(PubXmlValidatorFactory pubXmlValidatorFactory) {
    this.pubXmlValidatorFactory = pubXmlValidatorFactory;
  }

  /**
   * @return XmlValidatorFactory
   */
  public PubXmlValidatorFactory getPubXmlValidatorFactory() {
    return pubXmlValidatorFactory;
  }

  /**
   * 是否导入.
   * 
   * @return boolean
   */
  public boolean isImport() {
    return XmlOperationEnum.Import.equals(this.getCurrentAction())
        || XmlOperationEnum.ImportPdwh.equals(this.getCurrentAction())
        || XmlOperationEnum.OfflineImport.equals(this.getCurrentAction());
  }

  /**
   * 是否是同步成果.
   * 
   * @return
   */
  public boolean isSyncPub() {
    return XmlOperationEnum.SyncFromSNS.equals(this.getCurrentAction())
        || XmlOperationEnum.SyncToSNS.equals(this.getCurrentAction())
        || XmlOperationEnum.PushFromIns.equals(this.getCurrentAction());
  }

  /**
   * 是否是导入ISI成果.
   * 
   * @return
   */
  public boolean isIsiImport() {

    return PubXmlDbUtils.isIsiDb(this.sourceDbId);
  }

  /**
   * 是否是导入PubMed成果.
   * 
   * @return
   */
  public boolean isPubMedImport() {

    return PubXmlDbUtils.isPubMedDb(this.sourceDbId);
  }

  /**
   * 是否是导入CNKI成果.
   * 
   * @return
   */
  public boolean isCnkiImport() {

    return PubXmlDbUtils.isCnkiDb(this.sourceDbId);
  }

  /**
   * 是否是导入Scopus成果.
   * 
   * @return
   */
  public boolean isScopusImport() {

    return PubXmlDbUtils.isScopusDb(this.sourceDbId);
  }

  /**
   * 是否是导入Cnipr成果.
   * 
   * @return
   */
  public boolean isCniprImport() {

    return PubXmlDbUtils.isCNIPRDb(this.sourceDbId);
  }

  /**
   * 是否是导入CnkiPat成果.
   * 
   * @return
   */
  public boolean isCnkiPatImport() {

    return PubXmlDbUtils.isCnkipatDb(this.sourceDbId);
  }

  /**
   * 是否是导入Ei成果.
   * 
   * @return
   */
  public boolean isEiImport() {

    return PubXmlDbUtils.isEiDb(this.sourceDbId);
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
   * @return the currentNodeId
   */
  public Integer getCurrentNodeId() {
    return currentNodeId;
  }

  /**
   * @param currentNodeId the currentNodeId to set
   */
  public void setCurrentNodeId(Integer currentNodeId) {
    this.currentNodeId = currentNodeId;
  }

  public Integer getAuthorState() {
    return authorState;
  }

  public void setAuthorState(Integer authorState) {
    this.authorState = authorState;
  }

  public Integer getConfirmResult() {
    return confirmResult;
  }

  public void setConfirmResult(Integer confirmResult) {
    this.confirmResult = confirmResult;
  }

  public Long getFromPubId() {
    return fromPubId;
  }

  public void setFromPubId(Long fromPubId) {
    this.fromPubId = fromPubId;
  }

  public Integer getIsOpen() {
    return isOpen;
  }

  public void setIsOpen(Integer isOpen) {
    this.isOpen = isOpen;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public Long getSourceDbId() {
    return sourceDbId;
  }

  public void setSourceDbId(Long sourceDbId) {
    this.sourceDbId = sourceDbId;
  }

  public Integer getNeedConfirmStatus() {
    return needConfirmStatus;
  }

  public void setNeedConfirmStatus(Integer needConfirmStatus) {
    this.needConfirmStatus = needConfirmStatus;
  }

  public String getOldFullTextId() {
    return oldFullTextId;
  }

  public void setOldFullTextId(String oldFullTextId) {
    this.oldFullTextId = oldFullTextId;
  }

  public String getIsBatch() {
    return isBatch;
  }

  public void setIsBatch(String isBatch) {
    this.isBatch = isBatch;
  }

  public PubSimple getPubSimple() {
    return pubSimple;
  }

  public void setPubSimple(PubSimple pubSimple) {
    this.pubSimple = pubSimple;
  }

  public Long getPdwhId() {
    return pdwhId;
  }

  public void setPdwhId(Long pdwhId) {
    this.pdwhId = pdwhId;
  }

}
