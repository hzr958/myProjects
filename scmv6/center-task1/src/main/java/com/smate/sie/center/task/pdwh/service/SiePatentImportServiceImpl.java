package com.smate.sie.center.task.pdwh.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.model.rol.SieInsUnit;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.service.insunit.SieInsUnitService;
import com.smate.core.base.utils.string.IrisNumberUtils;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.sie.center.task.model.PatDupParam;
import com.smate.sie.center.task.model.SiePatDupFields;
import com.smate.sie.center.task.pdwh.task.PubXmlDocument;
import com.smate.sie.center.task.pdwh.task.PubXmlProcessContext;
import com.smate.sie.center.task.pdwh.utils.XmlFragmentCleanerHelper;
import com.smate.sie.center.task.pub.enums.PublicationOperationEnum;
import com.smate.sie.core.base.utils.dao.psn.SieInsPersonDao;
import com.smate.sie.core.base.utils.dao.pub.SiePatMemberDao;
import com.smate.sie.core.base.utils.dao.pub.SiePatXmlDao;
import com.smate.sie.core.base.utils.dao.pub.SiePatentDao;
import com.smate.sie.core.base.utils.model.psn.SieInsPerson;
import com.smate.sie.core.base.utils.model.pub.SiePatErrorFields;
import com.smate.sie.core.base.utils.model.pub.SiePatMember;
import com.smate.sie.core.base.utils.model.pub.SiePatXml;
import com.smate.sie.core.base.utils.model.pub.SiePatent;

/**
 * 单位成果管理service.
 * 
 * @author jszhou
 */
@Service("siePatentImportService")
@Transactional(rollbackFor = Exception.class)
public class SiePatentImportServiceImpl implements SiePatentImportService {

  /**
   * 
   */
  private static final long serialVersionUID = -8220619691038261257L;

  /**
   * 
   */
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private SiePatentDao patentDao;
  @Autowired
  private SiePatentLogService patentLogService;
  @Autowired
  private SiePatMemberDao patMemberDao;
  @Autowired
  private SiePatXmlDao patXmlDao;
  @Autowired
  private PatKeyWordsService patKeyWordsService;
  @Autowired
  private SiePatDupFieldsService patDupFieldsService;
  @Autowired
  private SieInsPersonDao sieInsPersonDao;
  @Autowired
  private SieInsUnitService sieInsUnitService;

  @Override
  public SiePatent getPatentById(Long id) throws SysServiceException {
    try {
      return this.patentDao.get(id);
    } catch (Exception e) {
      logger.error("getPublicationById获取成果实体id=" + id, e);
      throw new SysServiceException(e);
    }
  }

  @Override
  public SiePatent createPatent(PubXmlDocument doc, PubXmlProcessContext context) throws Exception {
    try {
      Date now = new Date();
      SiePatent patent = new SiePatent();
      patent.setPatId(context.getCurrentPubId());
      patent.setInsId(context.getCurrentInsId());
      patent.setCreatePsnId(context.getCurrentUserId());
      patent.setCreateDt(now);
      patent.setStatus("0");
      wrapPubField(doc, context, now, patent);
      patentDao.save(patent);
      doc.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "pub_id", patent.getPatId().toString());
      context.setCurrentPubId(patent.getPatId());
      // 建立成果、成员关系数据
      this.savePatMember(doc, context);
      patent.setAuthors(StringUtils
          .substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "original_author_names"), 0, 200));
      patentDao.save(patent);
      // 清除数据库的pub_error_fields,遍历xml中的pub_errors/error,把错误的字段名保存进pub_error_fields
      savePatErrorFields(doc, context);
      // 保存成果查重字段
      this.parsePatDupFields(doc, patent);
      // 保存专利xml
      this.savePatXml(doc, context);
      String zhKeywords = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_keywords");
      String enKeywords = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_keywords");
      // 成果关键词拆分保存
      this.patKeyWordsService.savePatKeywords(patent.getPatId(), context.getCurrentInsId(), zhKeywords, enKeywords);
      return patent;
    } catch (SysServiceException e) {
      logger.error("savePatCreate保存新添加专利出错 ", e);
      throw new SysServiceException(e);
    }
  }

  /**
   * 专利录入，更新公共属性处理.
   * 
   * @param doc
   * @param context
   * @param now
   * @param pub
   */
  private void wrapPubField(PubXmlDocument doc, PubXmlProcessContext context, Date now, SiePatent patent)
      throws SysServiceException {
    // 申请
    doc.getXmlString();
    patent.setApplyDay(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_day")));
    patent.setApplyMonth(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_month")));
    patent.setApplyYear(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_year")));
    patent.setApplyNo(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "patent_no"));
    // 授权

    String[] dates = XmlFragmentCleanerHelper.splitDateYearMonth(doc, PubXmlConstants.PUBLICATION_XPATH,
        "effective_start_date", PubXmlConstants.CHS_DATE_PATTERN);
    if (!"".equals(dates[0])) {
      patent.setAuthDay(IrisNumberUtils.createInteger(dates[2]));
      patent.setAuthMonth(IrisNumberUtils.createInteger(dates[1]));
      patent.setAuthYear(IrisNumberUtils.createInteger(dates[0]));
    }
    patent.setAuthNo(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "auth_no"));
    String zhTitle = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title");
    // Long zhTitleHash = PubHashUtils.cleanTitleHash(zhTitle);
    patent.setZhTitle(StringUtils.substring(zhTitle, 0, 250));
    patent.setEnTitle(StringUtils.substring(zhTitle, 0, 250));
    // patent.setZhTitlehash(zhTitleHash);
    // patent.setEnTitleHash(zhTitleHash);
    patent.setBriefDesc(
        StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc"), 0, 500));
    patent.setBriefDescEn(
        StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc_en"), 0, 500));
    patent.setDataFrom(4);
    patent.setCeritfNo(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "ceritficate_no"));// 证书编号
    patent.setFulltextUrl(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "fulltext_url"));
    // 专利类型
    String patentType = doc.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_type");
    if ("51".equals(patentType)) {
      patent.setPatType(51);
    } else if ("52".equals(patentType)) {
      patent.setPatType(52);
    } else if ("53".equals(patentType)) {
      patent.setPatType(53);
    } else if ("54".equals(patentType)) {
      patent.setPatType(54);
    } else {
      patent.setPatType(null);
    }
    patent.setDbId(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "source_db_id")));
    // 法律状态
    String legalStatus = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "legal_status");
    if ("授权".equals(legalStatus)) {
      patent.setLevelStatus("1");
    } else if ("申请".equals(legalStatus)) {
      patent.setLevelStatus("0");
    } else if ("视撤".equals(legalStatus)) {
      patent.setLevelStatus("2");
    } else if ("有效".equals(legalStatus)) {
      patent.setLevelStatus("3");
    } else if ("失效".equals(legalStatus)) {
      patent.setLevelStatus("4");
    } else {
      patent.setLevelStatus(null);
    }
    patent.setIsVaild(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "is_valid")));
    patent.setUpdateDt(now);
    patent.setUpPsnId(context.getCurrentUserId());
    patent.setIsPublic(1); // 默认公开
  }

  /**
   * 修改/添加专利发明人
   * 
   * @param doc
   * @param context
   * @throws SysServiceException
   */
  @SuppressWarnings({"unchecked", "unused"})
  public Long savePatMember(PubXmlDocument doc, PubXmlProcessContext context) throws SysServiceException {
    try {
      Long patId = context.getCurrentPubId();
      List<Node> ndList = doc.getPubMembers();
      if (ndList == null || ndList.size() == 0)
        return null;
      Long firstPsnId = null;
      SiePatent pat = this.getPatentById(patId);
      for (int i = 0; i < ndList.size(); i++) {
        Element em = (Element) ndList.get(i);
        SiePatMember pm = null;
        // 获取成员ID，如果存在则查找修改
        Long pmId = IrisNumberUtils.createLong(em.attributeValue("pm_id"));
        if (pmId != null) {
          pm = patMemberDao.getPatMemberById(pmId);
          // 创建成员
          if (pm == null) {
            pm = new SiePatMember();
          }
        } else {
          // 创建成员
          pm = new SiePatMember();
        }
        pm.setAuthorPos(IrisNumberUtils.createLong(em.attributeValue("author_pos")));
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
          pm.setInsId(IrisNumberUtils.createLong(em.attributeValue("ins_id")));
          pm.setInsName(StringUtils.substring(em.attributeValue("ins_name"), 0, 100));
        } else {
          pm.setMemberPsnid(null);
          em.addAttribute("member_psn_id", "");
          em.addAttribute("member_psn_acname", "");
          em.addAttribute("unit_id", "");
          em.addAttribute("unit_name", "");
        }
        String memberName = StringUtils.substring(em.attributeValue("member_psn_name"), 0, 50);
        // 前台已做控制
        if ("".equals(memberName)) {
          continue;
        }
        pm.setMemberName(memberName);
        pm.setPatId(patId);
        pm.setSeqNo(IrisNumberUtils.createLong(em.attributeValue("seq_no")));
        // 保存数据
        patMemberDao.savePatMember(pm);
        em.addAttribute("pm_id", pm.getPmId().toString());
      }
      return firstPsnId;
    } catch (Exception e) {
      logger.error("savePatMember保存成员出错 ", e);
      throw new SysServiceException(e);
    }
  }

  /**
   * 删除成果成员.
   * 
   * @param doc
   * @param context
   * @throws SysServiceException
   */
  public void deletePatMember(PubXmlDocument doc, PubXmlProcessContext context) throws SysServiceException {
    Long patId = context.getCurrentPubId();
    Long currentInsId = context.getCurrentInsId();
    try {
      long opPsnId = SecurityUtils.getCurrentUserId();
      List<Long> pmIds = patMemberDao.getPmIdByPatId(patId);
      for (Long pmId : pmIds) {
        SiePatMember pm = patMemberDao.getPatMemberById(pmId);
        patMemberDao.deletePatMember(pm);
        Map<String, String> opDetail = new HashMap<String, String>();
        opDetail.put("pmId", pm.getPmId().toString());
        opDetail.put("author", pm.getMemberName());
        opDetail.put("seqNo", pm.getSeqNo().toString());
        patentLogService.logOp(patId, opPsnId, currentInsId, PublicationOperationEnum.RemovePubMember, opDetail);
      }
    } catch (NumberFormatException e) {
      logger.error("deletePatMember专利发明人出错 ", e);
      throw new SysServiceException(e);
    } catch (Exception e) {
      logger.error("deletePatMember专利发明人出错 ", e);
      throw new SysServiceException(e);
    }
  }

  /**
   * 把专利发明人的名字用逗号分隔连接起来.
   * 
   * @param pubId
   * @return
   * @throws SysServiceException
   */
  public Map<String, String> buildPatAuthorNames(long patId) throws SysServiceException {
    try {
      return this.patMemberDao.buildPatAuthorNames(patId);
    } catch (Exception e) {
      logger.error("读取专利发明人列表错误;patId: " + patId, e);
      throw new SysServiceException(e);
    }
  }

  @Override
  public SiePatDupFields parsePatDupFields(PubXmlDocument doc, SiePatent pat) throws SysServiceException {
    String zhTitle = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title");
    String patentNo = doc.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_no");
    Long ownerId = pat.getInsId();
    Long patId = pat.getPatId();
    Integer pubYear = pat.getApplyYear();
    Integer sourceDbId = pat.getDbId();
    PatDupParam param = new PatDupParam();
    param.setPatentNo(patentNo);
    param.setEnTitle(zhTitle);
    param.setPubYear(pubYear);
    param.setSourceDbId(sourceDbId);
    param.setZhTitle(zhTitle);
    Integer patDupStatus = SiePatDupFields.NORMAL_STATUS;
    return this.patDupFieldsService.savePatDupFields(param, patId, ownerId, patDupStatus);
  }

  /**
   * 添加、删除、保存专利错误字段.
   * 
   * @param doc
   * @return
   * @throws SysServiceException
   */
  @SuppressWarnings("unchecked")
  public List<SiePatErrorFields> savePatErrorFields(PubXmlDocument doc, PubXmlProcessContext context)
      throws SysServiceException {
    Long patId = context.getCurrentPubId();
    try {
      // if (XmlOperationEnum.Edit.equals(context.getCurrentAction()))
      // 删除原有错误检查信息
      patentDao.deleteErrorFields(patId);
      List<Node> ndList = doc.getPubErrorFields();
      if (ndList == null || ndList.size() == 0)
        return null;
      List<SiePatErrorFields> errorList = new ArrayList<SiePatErrorFields>();
      for (Node node : ndList) {
        Element em = (Element) node;
        // 创建错误实体
        SiePatErrorFields error = new SiePatErrorFields();
        error.setCreateAt(new Date());
        error.setErrorNo(IrisNumberUtils.createInteger(em.attributeValue("error_no")));
        error.setName(em.attributeValue("field"));
        error.setPatId(patId);
        // 保存
        patentDao.saveErrorField(error);
        errorList.add(error);
      }
      return errorList;
    } catch (Exception e) {
      logger.error("savePatErrorFields保存专利错误信息出错 ", e);
      throw new SysServiceException(e);
    }
  }

  public SiePatXml savePatXml(PubXmlDocument doc, PubXmlProcessContext context) {
    // 存储XML数据
    String xml = doc.getXmlString();
    Long patId = context.getCurrentPubId();
    SiePatXml xmlData = patXmlDao.get(patId);
    if (xmlData == null) {
      xmlData = new SiePatXml();
      xmlData.setPatId(patId);
    }
    xmlData.setPatXml(xml);
    this.patXmlDao.save(xmlData);
    return xmlData;
  }

  @Override
  public SiePatent updatePatent(PubXmlDocument doc, PubXmlProcessContext context)
      throws SysServiceException, BatchTaskException {
    try {
      Long patId = context.getCurrentPubId();
      SiePatent patent = patentDao.get(patId);
      if (patent != null) {
        // 值为1时不用再更新
        if (patent.getIsVaild() == 1) {
          return patent;
        }
        Date now = new Date();
        patent.setPatId(context.getCurrentPubId());
        patent.setInsId(context.getCurrentInsId());
        patent.setCreatePsnId(context.getCurrentUserId());
        patent.setStatus("0");
        wrapPubField(doc, context, now, patent);
        patentDao.save(patent);
        doc.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "pub_id", patent.getPatId().toString());
        context.setCurrentPubId(patent.getPatId());
        // 先删除再更新
        this.deletePatMember(doc, context);
        // 建立成果、成员关系数据
        this.savePatMember(doc, context);
        patent.setAuthors(StringUtils
            .substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "original_author_names"), 0, 200));
        patentDao.save(patent);
        // 清除数据库的pub_error_fields,遍历xml中的pub_errors/error,把错误的字段名保存进pub_error_fields
        savePatErrorFields(doc, context);
        // 保存成果查重字段
        this.parsePatDupFields(doc, patent);
        // 保存专利xml
        this.savePatXml(doc, context);
        String zhKeywords = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_keywords");
        String enKeywords = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_keywords");
        // 成果关键词拆分保存
        this.patKeyWordsService.savePatKeywords(patent.getPatId(), context.getCurrentInsId(), zhKeywords, enKeywords);
      } else {
        throw new SysServiceException("updatePatent该专利不存在patId:" + patId);
      }
      return patent;
    } catch (SysServiceException e) {
      logger.error("savePubCreate保存新添加成果出错 ", e);
      throw new SysServiceException(e);
    }
  }

}
