package com.smate.center.task.service.rol.quartz;



import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.rol.quartz.PubRolMemberDao;
import com.smate.center.task.dao.rol.quartz.PublicationRolDao;
import com.smate.center.task.exception.DaoException;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.rcmd.quartz.PubConfirmSyncMessage;
import com.smate.center.task.model.rol.quartz.InstitutionRol;
import com.smate.center.task.model.rol.quartz.PubMemberRol;
import com.smate.center.task.model.rol.quartz.PublicationRol;
import com.smate.center.task.model.sns.quartz.PubXmlProcessContext;
import com.smate.center.task.model.sns.quartz.PublicationXml;
import com.smate.center.task.single.constants.PublicationArticleType;
import com.smate.center.task.single.dao.rol.psn.RolPsnInsDao;
import com.smate.center.task.single.dao.rol.pub.InsUnitDao;
import com.smate.center.task.single.enums.pub.XmlOperationEnum;
import com.smate.center.task.single.factory.pub.BriefDriverFactory;
import com.smate.center.task.single.factory.pub.IPubXmlServiceFactory;
import com.smate.center.task.single.factory.pub.XmlValidatorFactory;
import com.smate.center.task.single.model.rol.psn.RolPsnIns;
import com.smate.center.task.single.oldXml.pub.PubXmlDocument;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.IrisNumberUtils;

@Service("rolPublicationXmlManager")
@Transactional(rollbackFor = Exception.class)
public class RolPublicationXmlManagerImpl implements RolPublicationXmlManager {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private RolPublicationXmlService rolPublicationXmlService;
  @Autowired
  private RolPsnInsDao rolPsnInsDao;
  @Autowired
  private InsUnitDao insUnitDao;
  @Autowired
  private PublicationRolDao publicationRolDao;
  @Autowired
  private PubRolMemberDao pubRolMemberDao;
  @Autowired
  private InstitutionRolService institutionRolService;
  @Autowired
  private IPubXmlServiceFactory rolPublicationXmlServiceFactory;
  @Autowired
  private PublicationBriefGenerateSerivce publicationBriefGenerateSerivce;
  private String xmlEmailPostfix;
  /**
   * Xml校验工厂.
   */
  private XmlValidatorFactory xmlValidatorFactory;
  /**
   * 成果Brief生成驱动工厂.
   */
  private BriefDriverFactory briefDriverFactory;



  @Override
  public PubMemberRol updatePubMemeberXml(PubMemberRol mb) throws ServiceException {
    try { // 获取XML，XML已经使用自定义缓存
      PublicationXml xmlData = rolPublicationXmlService.getById(mb.getPubId());
      // 转换XML
      if (xmlData.getXmlData() == null) {
        throw new ServiceException("在rol2.ROL_PUB_XML表中根据pubId" + mb.getPubId() + "获取的xml为空");
      }
      PubXmlDocument doc = new PubXmlDocument(xmlData.getXmlData());
      updatePubMemberXml(mb, doc);
      PublicationRol pub = publicationRolDao.get(mb.getPubId());
      // 重构第一作者等信息
      this.praseAuthorNames(doc, pub);
      // 更新xml
      rolPublicationXmlService.save(mb.getPubId(), doc.getXmlString());
      publicationRolDao.save(pub);
      return mb;
    } catch (Exception e) {
      logger.error("更新成果成员xml失败", e);
      throw new ServiceException(e);
    }
  }



  private void updatePubMemberXml(PubMemberRol mb, PubXmlDocument doc) throws DaoException, ServiceException {
    Element e = (Element) doc.getNode(PubXmlConstants.PUB_MEMBERS_MEMBER_XPATH + "[@pm_id=" + mb.getId() + "]");
    updatePubMemberXml(mb, e);
    // 更新xml
    rolPublicationXmlService.save(mb.getPubId(), doc.getXmlString());
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
      logger.error("更新pubMember的xml数据是出错", e);
      throw new DaoException(e);
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
      PublicationXml pubxml = rolPublicationXmlService.getById(insPubId);
      String xmlData = pubxml.getXmlData();
      logger.error(xmlData);
      if (StringUtils.isBlank(xmlData)) {
        throw new ServiceException("在rol中获取到的xml为空");
      }
      Document doc = DocumentHelper.parseText(xmlData);
      PubXmlDocument pubXmlDocument = new PubXmlDocument(doc);
      // 清空原有指派关系
      if (!cofirmPmId.equals(assignPmId)) {
        if (assignPmId != null) {
          Element e = (Element) pubXmlDocument
              .getNode(PubXmlConstants.PUB_MEMBERS_MEMBER_XPATH + "[@pm_id=" + assignPmId + "]");
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
      // 重构作者
      this.rebuildPubMember(cofirmPmId, insId, psnId, pubXmlDocument);
      // 重构brief
      this.rebuildPublicationBrief(pubXmlDocument, pub, psnId);
      return pubXmlDocument;
    } catch (Exception e) {
      logger.error("成果确认后，重构XML错误", e);
      throw new ServiceException(e);
    }
  }

  private void rebuildPublicationBrief(PubXmlDocument xmlDocument, PublicationRol pub, Long psnId) {
    Long currentInsId = pub.getInsId();
    PubXmlProcessContext context = buildXmlProcessContext(XmlOperationEnum.Edit, pub.getTypeId(), psnId, currentInsId);
    try {
      publicationBriefGenerateSerivce.generateBrief(xmlDocument, context);
      String briefDesc = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc");
      pub.setBriefDesc(briefDesc);
      String briefDescEn = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc_en");
      pub.setBriefDescEn(briefDescEn);
    } catch (Exception e) {
      logger.error("重构生成简要描述（页面表格的来源列）错误", e);
      throw new ServiceException(e);
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


  @Override
  public void praseAuthorNames(PubXmlDocument pubXmlDoc, PublicationRol pub) {
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

    } catch (Exception e) {
      logger.error("重构用户名XML", e);
      throw new ServiceException(e);
    }
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

}
