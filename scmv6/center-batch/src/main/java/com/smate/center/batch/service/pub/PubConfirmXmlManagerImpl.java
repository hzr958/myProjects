package com.smate.center.batch.service.pub;

import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.constant.BatchConfConstant;
import com.smate.center.batch.enums.pub.XmlOperationEnum;
import com.smate.center.batch.factory.pub.BriefDriverFactory;
import com.smate.center.batch.factory.pub.XmlValidatorFactory;
import com.smate.center.batch.model.sns.pub.IPubXmlServiceFactory;
import com.smate.center.batch.model.sns.pub.PubSimple;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.process.pub.IPubXmlProcess;
import com.smate.core.base.utils.security.SecurityUtils;

@Service("pubConfirmXmlManager")
@Transactional(rollbackFor = Exception.class)
public class PubConfirmXmlManagerImpl implements PubConfirmXmlManager {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubSimpleService pubSimpleService;
  /**
   * Xml处理需要用到的外部服务.
   */
  @Autowired
  private IPubXmlServiceFactory scholarPublicationXmlServiceFactory;
  /**
   * 成果Brief生成驱动工厂.
   */
  private BriefDriverFactory briefDriverFactory;
  /**
   * Xml校验工厂.
   */
  private XmlValidatorFactory xmlValidatorFactory;

  private IPubXmlProcess pubConfirmXmlProcess;

  @Override
  public Integer createXmlNew(String newXmlData, int pubTypeId, int articleType, PubSimple pubSimple) {
    try {
      PubXmlDocument xmlDocument = new PubXmlDocument(newXmlData);
      // 构造全局变量对象
      Long psnId = pubSimple.getOwnerPsnId();
      String localStr = xmlDocument.getExpandLocal();
      PubXmlProcessContext context = this.buildXmlProcessContext(psnId, pubTypeId, localStr, pubSimple.getUpdateDate());
      context.setArticleType(articleType);
      context.setCurrentAction(XmlOperationEnum.PushFromIns);
      context.setPubSimple(pubSimple);
      this.pubConfirmXmlProcess.start(xmlDocument, context);
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

  public IPubXmlProcess getPubConfirmXmlProcess() {
    return pubConfirmXmlProcess;
  }

  public void setPubConfirmXmlProcess(IPubXmlProcess pubConfirmXmlProcess) {
    this.pubConfirmXmlProcess = pubConfirmXmlProcess;
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

}
