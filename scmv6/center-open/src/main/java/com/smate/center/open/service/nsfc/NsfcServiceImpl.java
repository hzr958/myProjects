package com.smate.center.open.service.nsfc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.CDATA;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gdata.util.common.base.CharMatcher;
import com.smate.center.open.model.consts.ConstRegion;
import com.smate.center.open.model.nsfc.NsfcExpertPub;
import com.smate.center.open.model.nsfc.NsfcSyncProject;
import com.smate.center.open.model.nsfc.SyncProposalModel;
import com.smate.center.open.model.nsfc.project.NsfcPrjRptPub;
import com.smate.center.open.model.nsfc.project.NsfcProject;
import com.smate.center.open.model.nsfc.project.NsfcReschProject;
import com.smate.center.open.model.proposal.NsfcProposal;
import com.smate.center.open.model.proposal.NsfcPrpPub;
import com.smate.center.open.model.publication.NsfcPubMember;
import com.smate.center.open.model.publication.PubXmlDocument;
import com.smate.center.open.service.consts.ConstRegionService;
import com.smate.center.open.service.pdwh.jnl.JnlLevelService;
import com.smate.center.open.service.profile.PersonManager;
import com.smate.center.open.service.proposal.NsfcPrpPubService;
import com.smate.center.open.service.publication.MyPublicationQueryService;
import com.smate.center.open.service.publication.NsfcPubMemberService;
import com.smate.center.open.service.publication.NsfcPubSourceService;
import com.smate.center.open.service.publication.PubMemberService;
import com.smate.center.open.service.publication.PublicationService;
import com.smate.center.open.service.publication.ScholarPublicationXmlManager;
import com.smate.center.open.service.reschproject.SnsSyncRolService;
import com.smate.center.open.service.user.UserService;
import com.smate.center.open.utils.publication.PubMemberNameUtils;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.pub.model.Publication;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XMLEntityConvertUtils;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.model.cas.security.SysRolUser;
import com.smate.core.base.utils.string.IrisStringUtils;

/**
 * 成果在线服务服务
 * 
 * @author tsz
 * 
 */
@Service("nsfcService")
@Transactional(rollbackFor = Exception.class)
public class NsfcServiceImpl implements NsfcService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  //
  @Autowired
  private UserService userService;
  //
  @Autowired
  private NsfcPrpPubService nsfcPrpPubService;
  //
  @Autowired
  private PersonManager personManager;
  //
  @Autowired
  private NsfcPubMemberService nsfcPubMemberService;

  @Autowired
  private PubMemberService pubMemberService;

  @Autowired
  private NsfcPubSourceService nsfcPubSourceService;

  @Autowired
  private SnsSyncRolService snsSyncRolService;

  @Autowired
  private ExpertPubService expertPubService;

  @Autowired
  private PublicationService pubService;

  @Autowired
  private ScholarPublicationXmlManager scholarPublicationXmlManager;

  @Autowired
  private MyPublicationQueryService myPublicationQueryService;

  @Autowired
  private JnlLevelService jnlLevelService;

  @Autowired
  private ConstRegionService constRegionService;

  /**
   * 同步申请书
   * 
   * @param model
   * @throws ServiceException
   */
  @Override
  public void updateSnsProposal(SyncProposalModel model) throws Exception {

    this.nsfcPrpPubService.syncSaveProposal(model);

  }

  /**
   * 获取申请书成果，以XML的格式的字符串返回
   * 
   * @param prpModel
   * @throws ServiceException
   */
  @Override
  public String buildPrpPubsXml(SyncProposalModel model) throws Exception {

    try {
      String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
      xml += "<root><results></results></root>";
      Document doc = DocumentHelper.parseText(xml);

      Element pe = (Element) doc.selectObject("/root/results");
      NsfcProposal proposal = this.nsfcPrpPubService.getPrpByIsisGuid(model.getGuid(), model.getPsnId());
      String currentPsnName = personManager.getPsnName(model.getPsnId(), null);
      if (proposal != null && proposal.getPrpDate() != null) {
        SimpleDateFormat formate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        pe = pe.addAttribute("prpdate", formate.format(proposal.getPrpDate()));
      }

      List<NsfcPrpPub> prpPubs = loadSnsPrpPubs(model);// 这里出错好多

      if (prpPubs == null || prpPubs.size() == 0) {
        return doc.asXML();

      }
      addPrpPubsToXML(pe, currentPsnName, prpPubs);

      return doc.asXML();

    } catch (Exception e) {
      logger.error("xml文档构建异常", e);

      throw new Exception("xml文档构建异常", e);
    }

  }

  // 把申请书成果添加到 XML中
  private void addPrpPubsToXML(Element pe, String currentPsnName, List<NsfcPrpPub> prpPubs) throws Exception {
    Integer i = 1;
    for (NsfcPrpPub prpPub : prpPubs) {
      Element subElement = pe.addElement("result");
      subElement.addElement("PUB_ID").add(this.getCDATA(prpPub.getPubId().toString()));
      subElement.addElement("PUB_TYPE").add(this.getCDATA(prpPub.getPubType().toString()));
      subElement.addElement("NODE_ID").add(this.getCDATA(prpPub.getNodeId().toString()));
      subElement.addElement("ISIS_GUID").add(this.getCDATA(prpPub.getIsisGuid()));
      subElement.addElement("TITLE").add(this.getCDATA(prpPub.getTitle()));
      /*
       * List<PubMember> pubMemberList = this.pubMemberService.getPubMemberList(prpPub.getPubId()); String
       * authors = PubMemberNameUtils .getPubMemberName(pubMemberList, prpPub.getPubType(),
       * currentPsnName);
       */
      List<NsfcPubMember> nsfcPubMemberList = this.nsfcPubMemberService.getNsfcPubMemberList(prpPub.getPubId());
      String authors = PubMemberNameUtils.getPubMemberName(nsfcPubMemberList, prpPub.getPubType(), currentPsnName);
      subElement.addElement("AUTHORS").add(this.getCDATA(XMLEntityConvertUtils.convertToXmlString(authors)));
      String source = this.nsfcPubSourceService.getNsfcPubSource(prpPub.getPubId());
      subElement.addElement("SOURCE").add(this.getCDATA(XMLEntityConvertUtils.convertToXmlString(source)));
      subElement.addElement("PTYPE")
          .add(this.getCDATA(prpPub.getTreatiseType() == null ? "" : prpPub.getTreatiseType().toString()));
      subElement.addElement("TYPE_NAME").add(this.getCDATA(prpPub.getPubTypeZhName()));
      subElement.addElement("TYPE_ENNAME").add(this.getCDATA(prpPub.getPubTypeEnName()));
      subElement.addElement("PZHNAME").add(this.getCDATA(prpPub.getTreatiseTypeZhName()));
      subElement.addElement("PENNAME").add(this.getCDATA(prpPub.getTreatiseTypeEnName()));
      subElement.addElement("PUB_YEAR").add(this.getCDATA((prpPub.getPubYear().toString())));
      subElement.addElement("PUB_MONTH").add(this.getCDATA((prpPub.getPubMonth().toString())));
      subElement.addElement("PUB_DAY").add(this.getCDATA((prpPub.getPubDay().toString())));
      subElement.addElement("GTYPE").add(this.getCDATA(prpPub.getListInfo()));
      subElement.addElement("SEQNO").add(this.getCDATA(i.toString()));
      subElement.addElement("LISTINFO")
          .add(this.getCDATA(prpPub.getCitedTimes() == null ? "" : prpPub.getCitedTimes().toString()));
      subElement.addElement("AUTHOR_FLAG").add(this.getCDATA(prpPub.getAuthorFlag()));
      if (prpPub.getPubType() == 3) {
        PubXmlDocument pubXmlDoc = this.getPubXML(prpPub.getPubId());
        if (pubXmlDoc != null) {
          String paperType = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "paper_type");
          String confType = "";
          if ("A".equals(paperType)) {
            confType = "A";
          }
          subElement.addElement("CONF_TYPE").add(this.getCDATA(confType));
        }
      }
      if (prpPub.getPubType() == 5) {
        PubXmlDocument pubXmlDoc = this.getPubXML(prpPub.getPubId());
        if (pubXmlDoc != null) {
          String effectiveStartYear = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "start_year");
          String patentStatus = StringUtils.isBlank(effectiveStartYear) ? "0" : "1";
          subElement.addElement("PATENT_STATUS").add(this.getCDATA(patentStatus));
        }
      }
      i++;
    }
  }

  private CDATA getCDATA(String str) {
    CDATA cData = null;
    if (str != null) {
      str = IrisStringUtils.filterSupplementaryChars(str);
      str = CharMatcher.WHITESPACE.trimFrom(str);
      str = CharMatcher.JAVA_ISO_CONTROL.removeFrom(str);
      cData = DocumentFactory.getInstance().createCDATA(str);
    } else
      cData = DocumentFactory.getInstance().createCDATA("");
    return cData;
  }

  @Override
  public List<NsfcPrpPub> loadSnsPrpPubs(SyncProposalModel model) throws Exception {
    return this.nsfcPrpPubService.loadPrpPubsByGuid(model);

  }

  @Override
  public Long getSyncRolPerson(SysRolUser sysRolUser) throws Exception {
    return userService.getRolUserByGuid(sysRolUser);
  }

  /**
   * 同步研究成果
   * 
   * @param nsfcSyncProject
   * @return
   * @throws Exception
   */
  @Override
  public NsfcReschProject saveNsfcReschSyncProject(NsfcSyncProject nsfcSyncProject) throws Exception {
    return this.snsSyncRolService.saveNsfcSyncReschProject(nsfcSyncProject);
  }

  @Override
  public NsfcProject syncNsfcProject(NsfcSyncProject nsfcSyncProject) throws Exception {

    return snsSyncRolService.syncNsfcProject(nsfcSyncProject);
  }

  /**
   * 构建评议专家成果
   */
  @Override
  public String buildExpertPubsXml(SyncProposalModel model) throws Exception {

    try {
      String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
      xml += "<root><results ></results></root>";
      Document doc = DocumentHelper.parseText(xml);

      Element pe = (Element) doc.selectObject("/root/results");
      pe.addAttribute("result", "1").addAttribute("des", "Loading Success");

      List<NsfcExpertPub> expertPubs = loadSnsExpertPubs(model);

      if (expertPubs == null || expertPubs.size() == 0) {
        return doc.asXML();

      }
      for (NsfcExpertPub expertPub : expertPubs) {

        Element subElement = pe.addElement("result");

        subElement.addElement("PUB_ID").addText(expertPub.getPubId().toString());
        subElement.addElement("TITLE").addText(expertPub.getTitle() == null ? "" : expertPub.getTitle());
        subElement.addElement("AUTHORS").addText(expertPub.getAuthors() == null ? "" : expertPub.getAuthors());
        subElement.addElement("PUB_YEAR")
            .addText(expertPub.getPubYear() == null ? "" : expertPub.getPubYear().toString());
        subElement.addElement("SOURCE").addText(expertPub.getSource() == null ? "" : expertPub.getSource());
        subElement.addElement("PUB_TYPE")
            .addText(StringUtils.isBlank(expertPub.getPubTypeDes()) ? "" : expertPub.getPubTypeDes());

        subElement.addElement("SEQNO").addText(expertPub.getSeqNo() == null ? "" : expertPub.getSeqNo().toString());
        subElement.addElement("IS_TAG").addText(expertPub.getIsTag() == null ? "" : expertPub.getIsTag().toString());
        subElement.addElement("IS_OPEN").addText(expertPub.getIsOpen() == null ? "" : expertPub.getIsOpen().toString());
        subElement.addElement("LIST_INFO").addText(expertPub.getListInfo() == null ? "" : expertPub.getListInfo());
        Publication pub = pubService.getPub(expertPub.getPubId());
        // 引用次数
        subElement.addElement("CITE_TIMES").addText(pub.getCitedTimes() == null ? "" : pub.getCitedTimes().toString());
        subElement.addElement("IMPACT_FACTORS").addText(pub.getImpactFactors() == null ? "" : pub.getImpactFactors());

      }

      return doc.asXML();

    } catch (Exception e) {
      logger.error("评议专家成果xml文档构建异常", e);
      throw new Exception(e);
    }

  }

  @Override
  public List<NsfcExpertPub> loadSnsExpertPubs(SyncProposalModel model) throws Exception {
    return this.expertPubService.loadExpertPubsByGuid(model);

  }

  @Override
  public List<NsfcPrjRptPub> fillPFPubsAddtlProps(List<NsfcPrjRptPub> pubs) throws Exception {
    try {
      List<Long> jnlPubIds = null;
      jnlPubIds = new ArrayList<Long>();
      for (NsfcPrjRptPub pub : pubs) {
        switch (pub.getPubType()) {
          case PublicationTypeEnum.AWARD:
            pub.setAdditionalProperties(this.getPFAwardAddtlProps(pub.getId().getPubId()));
            break;
          case PublicationTypeEnum.BOOK:
            pub.setAdditionalProperties(this.getPFBookAddtlProps(pub.getId().getPubId()));
            break;
          case PublicationTypeEnum.CONFERENCE_PAPER:
            pub.setAdditionalProperties(this.getPFConfAddtlProps(pub.getId().getPubId()));
            break;
          case PublicationTypeEnum.JOURNAL_ARTICLE:
            jnlPubIds.add(pub.getId().getPubId());
            break;
          case PublicationTypeEnum.PATENT:
            pub.setAdditionalProperties(this.getPFPatentAddtlProps(pub.getId().getPubId()));
            break;

          default:
            break;
        }
      }
      if (!CollectionUtils.isEmpty(jnlPubIds)) {
        this.fillPFJnlsAddtlProps(pubs, jnlPubIds);
      }
    } catch (Exception e) {
      logger.error("填充进展/结题报告成果额外属性出现异常了喔", e);
      throw new Exception("填充进展/结题报告成果额外属性出现异常了喔", e);
    }
    return pubs;
  }

  private PubXmlDocument getPubXML(Long pubId) throws Exception {
    try {
      return this.scholarPublicationXmlManager.getPubXml(pubId);
    } catch (Exception e) {
      logger.error("获取成果XML出现异常了喔,pubId={}", pubId, e);
      throw new Exception(e);
    }
  }

  /**
   * 获取进展/结题报告奖励额外属性.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  private Map<String, Object> getPFAwardAddtlProps(Long pubId) throws Exception {
    Map<String, Object> addtlProps = new HashMap<String, Object>();
    PubXmlDocument pubXmlDoc = this.getPubXML(pubId);
    if (pubXmlDoc != null) {
      String awardGrade = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_AWARD_XPATH, "award_grade");
      String awardCategory = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_AWARD_XPATH, "award_category");
      String awardGradeCode = "9999";
      String awardTypeCode = "99";
      if (awardGrade.indexOf("国际学术奖") > -1 || awardGrade.indexOf("International Academic Award") > -1) {
        awardGradeCode = "0001";
      } else if (awardGrade.indexOf("国家一等奖") > -1 || awardGrade.indexOf("National First Award") > -1) {
        awardGradeCode = "0101";
      } else if (awardGrade.indexOf("国家二等奖") > -1 || awardGrade.indexOf("National Second Award") > -1) {
        awardGradeCode = "0102";
      } else if (awardGrade.indexOf("省部一等奖") > -1 || awardGrade.indexOf("Provincial First Award") > -1) {
        awardGradeCode = "0301";
      } else if (awardGrade.indexOf("省部二等奖") > -1 || awardGrade.indexOf("Provincial Second Award") > -1) {
        awardGradeCode = "0302";
      } else {
        awardGradeCode = "9999";
      }

      if (awardCategory.indexOf("自然科学") > -1 || awardCategory.indexOf("Science") > -1) {
        awardTypeCode = "01";
      } else if (awardCategory.indexOf("科技进步") > -1 || awardCategory.indexOf("Technology") > -1) {
        awardTypeCode = "02";
      } else if (awardCategory.indexOf("发明") > -1 || awardCategory.indexOf("Innovation") > -1) {
        awardTypeCode = "03";
      } else {
        awardTypeCode = "99";
      }
      addtlProps.put("awardGrade", awardGradeCode);
      addtlProps.put("awardType", awardTypeCode);
    }
    return addtlProps;
  }

  /**
   * 获取进展/结题报告专著额外属性.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  private Map<String, Object> getPFBookAddtlProps(Long pubId) throws Exception {
    Map<String, Object> addtlProps = new HashMap<String, Object>();
    PubXmlDocument pubXmlDoc = this.getPubXML(pubId);
    if (pubXmlDoc != null) {
      String language = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "language");
      String language2 = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "language");
      String publicationStatus = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "publication_status");
      String publishYear = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_year");
      String publishMonth = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_month");
      String publishDay = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_day");
      String languageCode = "1";
      String publicationStatusCode = "0";
      if ((StringUtils.isBlank(language2) && (StringUtils.isBlank(language) || XmlUtil.isChinese(language)))
          || "1".equals(language2)) {
        languageCode = "1";
      } else {
        languageCode = "2";
      }
      publicationStatusCode = StringUtils.isBlank(publishYear) ? "0" : "1";
      addtlProps.put("language", languageCode);
      addtlProps.put("publicationStatus", publicationStatusCode);
      addtlProps.put("publishYear", publishYear);
      addtlProps.put("publishMonth", publishMonth);
      addtlProps.put("publishDay", publishDay);
    }
    return addtlProps;
  }

  /**
   * 获取进展/结题报告会议额外属性.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  private Map<String, Object> getPFConfAddtlProps(Long pubId) throws Exception {
    Map<String, Object> addtlProps = new HashMap<String, Object>();
    PubXmlDocument pubXmlDoc = this.getPubXML(pubId);
    if (pubXmlDoc != null) {
      String confName = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "conf_name");
      String paperType = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "paper_type");
      String confLevel = "1";
      String confType = "";
      if (confName.toUpperCase().indexOf("INTERNATIONAL") > -1 || confName.indexOf("国际") > -1) {
        confLevel = "0";
      } else {
        confLevel = "1";
      }
      if ("A".equals(paperType)) {
        confType = "A";
      } else if ("E".equals(paperType)) {
        confType = "E";
      }
      addtlProps.put("confLevel", confLevel);
      addtlProps.put("confType", confType);
    }
    return addtlProps;
  }

  /**
   * 填充进展/结题报告期刊额外属性.
   * 
   * @param nsfcPubs
   * @param pubIds
   * @return
   * @throws ServiceException
   */
  @SuppressWarnings("unchecked")
  private List<NsfcPrjRptPub> fillPFJnlsAddtlProps(List<NsfcPrjRptPub> nsfcPubs, List<Long> pubIds) throws Exception {
    List<Publication> pubs = this.myPublicationQueryService.getPubsByPubIds(pubIds);
    if (CollectionUtils.isEmpty(pubs)) {
      return nsfcPubs;
    }
    List<Long> jids = new ArrayList<Long>();
    List<Long> otherJnlPubIds = new ArrayList<Long>();
    if (!CollectionUtils.isEmpty(pubs)) {
      for (Publication pub : pubs) {
        if (pub.getJID() != null) {
          if (!jids.contains(pub.getJID())) {
            jids.add(pub.getJID());
          }
        } else {
          otherJnlPubIds.add(pub.getPubId());
        }
      }
    }
    List<Map<String, Object>> pubJnlLevelMaps = new ArrayList<Map<String, Object>>();
    if (!CollectionUtils.isEmpty(jids)) {
      Map<String, Object> jnlLevelJidsMap = this.jnlLevelService.getJnlLevelJids(jids);
      List<Long> intJnlJids = (List<Long>) jnlLevelJidsMap.get("intJnlJids");
      List<Long> homeCoreJnlJids = (List<Long>) jnlLevelJidsMap.get("homeCoreJnlJids");
      List<Long> otherJnlJids = (List<Long>) jnlLevelJidsMap.get("otherJnlJids");
      Map<String, Object> pubJnlLevelMap = null;
      if (!CollectionUtils.isEmpty(intJnlJids)) {
        for (Long jid : intJnlJids) {
          for (Publication pub : pubs) {
            if (jid.equals(pub.getJID())) {
              pubJnlLevelMap = new HashMap<String, Object>();
              pubJnlLevelMap.put("pubId", pub.getPubId());
              pubJnlLevelMap.put("jnlLevel", "0000");
              pubJnlLevelMaps.add(pubJnlLevelMap);
            }
          }
        }
      }
      if (!CollectionUtils.isEmpty(homeCoreJnlJids)) {
        for (Long jid : homeCoreJnlJids) {
          for (Publication pub : pubs) {
            if (jid.equals(pub.getJID())) {
              pubJnlLevelMap = new HashMap<String, Object>();
              pubJnlLevelMap.put("pubId", pub.getPubId());
              pubJnlLevelMap.put("jnlLevel", "0101");
              pubJnlLevelMaps.add(pubJnlLevelMap);
            }
          }
        }
      }
      if (!CollectionUtils.isEmpty(otherJnlJids)) {
        for (Long jid : otherJnlJids) {
          for (Publication pub : pubs) {
            if (jid.equals(pub.getJID())) {
              pubJnlLevelMap = new HashMap<String, Object>();
              pubJnlLevelMap.put("pubId", pub.getPubId());
              pubJnlLevelMap.put("jnlLevel", "0102");
              pubJnlLevelMaps.add(pubJnlLevelMap);
            }
          }
        }
      }
      if (!CollectionUtils.isEmpty(otherJnlPubIds)) {
        for (Long pubId : otherJnlPubIds) {
          pubJnlLevelMap = new HashMap<String, Object>();
          pubJnlLevelMap.put("pubId", pubId);
          pubJnlLevelMap.put("jnlLevel", "0102");
          pubJnlLevelMaps.add(pubJnlLevelMap);
        }
      }
    }
    Map<String, Object> additionalProperties = null;
    for (Map<String, Object> pubJnlLevel : pubJnlLevelMaps) {
      Long pubId = Long.valueOf(pubJnlLevel.get("pubId").toString());
      for (NsfcPrjRptPub nsfcPrjRptPub : nsfcPubs) {
        if (nsfcPrjRptPub.getId().getPubId().equals(pubId)) {
          String jnlLevel = null;
          additionalProperties = nsfcPrjRptPub.getAdditionalProperties();
          if (additionalProperties == null) {
            additionalProperties = new HashMap<String, Object>();
          } else {
            jnlLevel = additionalProperties.get("jnlLevel").toString();
          }
          if (StringUtils.isBlank(jnlLevel)) {
            jnlLevel = pubJnlLevel.get("jnlLevel").toString();
          } else {
            jnlLevel += "," + pubJnlLevel.get("jnlLevel").toString();
          }
          additionalProperties.put("jnlLevel", jnlLevel);
          nsfcPrjRptPub.setAdditionalProperties(additionalProperties);
          break;
        }
      }
    }
    return nsfcPubs;
  }

  /**
   * 获取进展/结题报告专利额外属性.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  private Map<String, Object> getPFPatentAddtlProps(Long pubId) throws Exception {
    Map<String, Object> addtlProps = new HashMap<String, Object>();
    PubXmlDocument pubXmlDoc = this.getPubXML(pubId);
    if (pubXmlDoc != null) {
      String patentStatus = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_status");
      String countryName = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "country_name");
      String effectiveStartYear = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "start_year");
      String effectiveStartMonth = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "start_month");
      String effectiveStartDay = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "start_day");
      String effectiveEndYear = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "end_year");
      String effectiveEndMonth = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "end_month");
      String effectiveEndDay = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "end_day");
      String applyYear = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_year");
      String applyMonth = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_month");
      String applyDay = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_day");
      String homeOrAbroad = "1";
      boolean isHome = false;
      if (StringUtils.isBlank(countryName)) {
        isHome = true;
      }
      if (!isHome && (countryName.contains("中国") || countryName.contains("香港") || countryName.contains("澳门")
          || countryName.contains("台湾"))) {
        isHome = true;
      } else {
        if (!isHome) {
          List<ConstRegion> allCNRegions = this.constRegionService.getAllCNRegion();
          for (ConstRegion region : allCNRegions) {
            String regionZhName = region.getZhName().substring(0, region.getZhName().length() - 1);
            if (regionZhName.contains(countryName) || countryName.contains(regionZhName)) {
              isHome = true;
              break;
            }
          }
        }
      }
      homeOrAbroad = isHome ? "1" : "2";
      patentStatus = StringUtils.isBlank(effectiveStartYear) ? "0" : "1";
      addtlProps.put("homeOrAbroad", homeOrAbroad);
      addtlProps.put("patentStatus", patentStatus);
      addtlProps.put("effectiveStartYear", effectiveStartYear);
      addtlProps.put("effectiveStartMonth", effectiveStartMonth);
      addtlProps.put("effectiveStartDay", effectiveStartDay);
      addtlProps.put("effectiveEndYear", effectiveEndYear);
      addtlProps.put("effectiveEndMonth", effectiveEndMonth);
      addtlProps.put("effectiveEndDay", effectiveEndDay);
      addtlProps.put("applyYear", applyYear);
      addtlProps.put("applyMonth", applyMonth);
      addtlProps.put("applyDay", applyDay);
    }
    return addtlProps;
  }

}
