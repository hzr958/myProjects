package com.smate.center.batch.service.pub;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
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

import com.smate.center.batch.dao.sns.pub.NsfcProposalDao;
import com.smate.center.batch.dao.sns.pub.NsfcPrpPubDao;
import com.smate.center.batch.enums.pub.XmlOperationEnum;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.ConstDictionary;
import com.smate.center.batch.model.sns.pub.ConstPubType;
import com.smate.center.batch.model.sns.pub.GrantRule;
import com.smate.center.batch.model.sns.pub.NsfcProposal;
import com.smate.center.batch.model.sns.pub.NsfcPrpPub;
import com.smate.center.batch.model.sns.pub.NsfcPrpPubModel;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.model.sns.pub.Publication;
import com.smate.center.batch.model.sns.pub.PublicationForm;
import com.smate.center.batch.model.sns.pub.PublicationList;
import com.smate.center.batch.model.sns.pub.SyncProposalModel;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.SecurityUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 杰青申报成果模块
 * 
 * @author oyh
 * 
 */
@Service("nsfcPrpPubService")
@Transactional(rollbackFor = Exception.class)
public class NsfcPrpPubServiceImpl implements NsfcPrpPubService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private NsfcProposalDao nsfcProposalDao;
  @Autowired
  private NsfcPrpPubDao nsfcPrpPubDao;
  @Autowired
  private MyPublicationQueryService myPublicationQueryService;
  @Autowired
  private ScholarPublicationXmlManager scholarPublicationXmlManager;
  @Autowired
  private PublicationListService publicationListService;
  @Autowired
  private PublicationService publicationService;
  @Autowired
  private GrantRuleService proposalRuleService;
  @Autowired
  private ConstPubTypeService constPubTypeService;
  @Autowired
  private GrantRuleService grantRuleService;
  @Autowired
  private ConstDictionaryManageCb constDictionaryManage;

  @Override
  public Page getProposalsByPsnId() throws ServiceException {
    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      List<NsfcProposal> prpLists = new ArrayList<NsfcProposal>();
      prpLists = nsfcProposalDao.getPrpListByPsnId(psnId);
      if ("en_US".equals(LocaleContextHolder.getLocale().toString())) {
        for (NsfcProposal nsfcProposal : prpLists) {
          nsfcProposal.setTitle(nsfcProposal.getEtitle());
        }
      } else {
        for (NsfcProposal nsfcProposal : prpLists) {
          nsfcProposal.setTitle(nsfcProposal.getCtitle());
        }
      }
      Page<NsfcProposal> page = new Page<NsfcProposal>();
      page.setResult(prpLists);
      page.setTotalCount(prpLists.size());
      return page;

    } catch (DaoException e) {
      logger.error("获取某个人的申请书，psnId=" + psnId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public NsfcProposal getPrpByIsisGuid(String isisGuid, Long userId) throws ServiceException {
    try {
      if (userId == null) {
        userId = SecurityUtils.getCurrentUserId();
      }
      NsfcProposal nsfcProposal = this.nsfcProposalDao.getPrpByIsisGuid(isisGuid, userId);
      if (nsfcProposal == null) {
        return null;
      }
      if ("en_US".equals(LocaleContextHolder.getLocale().toString())) {
        nsfcProposal.setTitle(nsfcProposal.getEtitle());
      } else {
        nsfcProposal.setTitle(nsfcProposal.getCtitle());
      }
      return nsfcProposal;
    } catch (Exception e) {

      logger.error("获取杰青isisGuid={失败！", isisGuid, e);
      throw new ServiceException(e);

    }
  }

  @Override
  public List<NsfcPrpPub> getPrpPubsById(String isisGuid) throws ServiceException {
    try {
      Assert.notNull(isisGuid, "isisGuid不允许为空！");
      List<ConstPubType> pubTypeList = constPubTypeService.getAll();
      List<NsfcPrpPub> pubs = this.nsfcPrpPubDao.loadPrpPubsByGuid(isisGuid);

      for (NsfcPrpPub nsfcPrpPub : pubs) {
        for (ConstPubType pubType : pubTypeList) {
          if (pubType.getId().intValue() == nsfcPrpPub.getPubType().intValue()) {
            Locale locale = LocaleContextHolder.getLocale();
            if (Locale.US.equals(locale)) {
              nsfcPrpPub.setPubTypeName(pubType.getEnName());
            } else {
              nsfcPrpPub.setPubTypeName(pubType.getZhName());
            }
            break;
          }
        }
      }
      return pubs;
    } catch (Exception e) {

      logger.error("获取isisGuid{}对应的论著失败！", isisGuid, e);
      return null;
    }
  }

  @Override
  public List<NsfcPrpPub> getPubsOrderByType(String isisGuid) throws ServiceException {
    try {
      Assert.notNull(isisGuid, "isisGuid不允许为空！");
      return this.nsfcPrpPubDao.loadPrpPubsOrderByType(isisGuid);
    } catch (Exception e) {

      logger.error("获取isisGuid{}对应的论著失败！", isisGuid, e);
      return null;
    }
  }

  @Override
  public Set<Long> getPubIdsByGuid(String isisGuid) throws ServiceException {

    try {
      return this.nsfcPrpPubDao.getPubIdsByGuid(isisGuid);
    } catch (Exception e) {
      logger.error("查询isisGuid={}的论著id失败！", isisGuid, e);
      throw new ServiceException(e);
    }

  }

  @Override
  public String updateDelieveType(Long prpPubKey, Integer dType) throws ServiceException {
    try {
      NsfcPrpPub nsfcPrpPub = this.nsfcPrpPubDao.get(prpPubKey);
      NsfcProposal prp =
          this.nsfcProposalDao.getPrpByIsisGuid(nsfcPrpPub.getIsisGuid(), SecurityUtils.getCurrentUserId());
      GrantRule grantRule = null;
      if (prp.getCode() != null && !"".equals(prp.getCode())) {
        grantRule = proposalRuleService.getGrantRule(prp.getCode());
        JSONObject json = JSONObject.fromObject(grantRule.getRules());
        if (json != null) {
          // 论著限制
          if (json.containsKey("pType_rule")) {
            JSONArray arr = JSONArray.fromObject(json.get("pType_rule"));
            int typeTotal = 0;
            if (arr != null && arr.size() > 0) { // 存在类型限制
              for (Object obj : arr) {
                JSONObject rule = JSONObject.fromObject(obj);
                typeTotal =
                    this.nsfcPrpPubDao.getPtypeTotalBy(nsfcPrpPub.getIsisGuid(), rule.getString("value").trim());
                if (rule.getString("value").trim().equals(dType.toString())) {
                  typeTotal++;
                }
                if (rule.getInt("limit") == 0) { // 该类型成果填报数不做限制

                  continue;
                }

                if (rule.getInt("limit") < typeTotal) {
                  logger.warn("成果规则检查异常：" + rule.getString("name") + "超过了允许添加的最大值！");
                  return grantRule.getHelpTip();
                }

              }
            }
          }
        }
      }
      nsfcPrpPub.setTreatiseType(dType);
      prp.setPrpDate(new Date());
      prp.setVersion(prp.getVersion() + 1);
      this.nsfcPrpPubDao.save(nsfcPrpPub);
      // 不是国际合作类型的需要重新排序
      if (grantRule != null && grantRule.getIsUserDefined()) {
        List<NsfcPrpPub> pubs =
            this.nsfcPrpPubDao.getPubsOrderByType(nsfcPrpPub.getIsisGuid(), SecurityUtils.getCurrentUserId());
        int order = 1;
        for (NsfcPrpPub pub : pubs) {
          pub.setSeqNo(order);
          order++;
        }
        this.nsfcPrpPubDao.savePrpPubs(pubs);
      }
      return "";
    } catch (Exception e) {

      logger.error("修改论著类型失败！", e);
      throw new ServiceException(e);
    }
  }

  private void wrapRptPublication(Publication pub, NsfcPrpPub prpPub) throws ServiceException {
    if (pub != null) {
      prpPub.setPubOwnerPsnId(pub.getPsnId());
      prpPub.setAuthors(pub.getAuthorNames());
      PublicationList pubListInfo = this.publicationListService.getPublicationList(pub.getId());
      String listInfo = "";
      if (pubListInfo != null) {
        if (pubListInfo.getListEi() != null && pubListInfo.getListEi().intValue() == 1) {
          listInfo += "," + "EI";
        }
        if (pubListInfo.getListSci() != null && pubListInfo.getListSci().intValue() == 1) {
          listInfo += "," + "SCI";
        }
        if (pubListInfo.getListIstp() != null && pubListInfo.getListIstp().intValue() == 1) {
          listInfo += "," + "ISTP";
        }
        if (pubListInfo.getListSsci() != null && pubListInfo.getListSsci().intValue() == 1) {
          listInfo += "," + "SSCI";
        }
        if (StringUtils.startsWith(listInfo, ",")) {
          listInfo = listInfo.substring(1);
        }
      }
      StringBuilder listInfoSource = new StringBuilder();
      if (pubListInfo != null) {
        if (pubListInfo.getListEiSource() != null && pubListInfo.getListEiSource().intValue() == 1) {
          listInfoSource.append(",EI");
        }
        if (pubListInfo.getListSciSource() != null && pubListInfo.getListSciSource().intValue() == 1) {
          listInfoSource.append(",SCI");
        }
        if (pubListInfo.getListIstpSource() != null && pubListInfo.getListIstpSource().intValue() == 1) {
          listInfoSource.append(",ISTP");
        }
        if (pubListInfo.getListSsciSource() != null && pubListInfo.getListSsciSource().intValue() == 1) {
          listInfoSource.append(",SSCI");
        }
        if (StringUtils.startsWith(listInfoSource.toString(), ",")) {
          listInfoSource = listInfoSource.deleteCharAt(0);
        }
      }
      prpPub.setListInfoSource(listInfoSource.toString());
      prpPub.setListInfo(listInfo);

      prpPub.setCitedTimes(pub.getCitedTimes());

      prpPub.setPubYear(pub.getPublishYear() == null ? 0 : pub.getPublishYear());
      prpPub.setPubMonth(pub.getPublishMonth() == null ? 0 : pub.getPublishMonth());
      prpPub.setPubDay(pub.getPublishDay() == null ? 0 : pub.getPublishDay());
      String title = pub.getZhTitle();
      if (StringUtils.isBlank(title)) {
        title = pub.getEnTitle();
      }
      prpPub.setTitle(title);
      Locale locale = LocaleContextHolder.getLocale();
      String brief = pub.getBriefDesc();// doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH,
      // "brief_desc");
      if (locale.equals(Locale.US)) {
        String briefEn = pub.getBriefDescEn();// doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH,
        // "brief_desc_en");
        prpPub.setSource(StringUtils.isBlank(briefEn) ? brief : briefEn);
      } else {
        prpPub.setSource(brief);
      }

    }
  }

  @Override
  public String addPublicationFromMyMate(NsfcPrpPubModel form) throws ServiceException {
    try {
      String result = validatePubRule(form);
      if (result != null) {
        return result;
      }

      NsfcProposal prp = this.nsfcProposalDao.getPrpByIsisGuid(form.getIsisGuid(), SecurityUtils.getCurrentUserId());
      GrantRule pubRules = null;

      Integer pType = null;
      if (prp.getCode() != null || !"".equals(prp.getCode())) { // 没有业务规则限制
        pubRules = proposalRuleService.getGrantRule(prp.getCode());
        pType = pubRules.getDefaultValue();
      }
      if (StringUtils.isNotBlank(form.getPubIds())) {
        List<Long> list = new ArrayList<Long>();
        String[] aPubIds = form.getPubIds().split(",");
        for (String pubId : aPubIds) {
          list.add(Long.valueOf(pubId));
        }
        // 查询成果总条数
        int total = this.nsfcPrpPubDao.getPubCountByIsisGuid(form.getIsisGuid());
        List<Publication> pubList = this.myPublicationQueryService.findPubsForNsfc(list);
        for (int i = 0; i < pubList.size(); i++) {
          Integer seqNo = total + i + 1;
          Publication pub = pubList.get(i);
          NsfcPrpPub prpPub = new NsfcPrpPub();
          if (pubRules != null && pubRules.getIsUserDefined()) {
            seqNo = this.nsfcPrpPubDao.updatePubSeq(form.getIsisGuid(), pubRules.getDefaultValue());
          }
          prpPub.setSeqNo(seqNo); // 最大数目加+1
          prpPub.setPubId(pub.getId());
          prpPub.setTreatiseType(pType);
          prpPub.setIsisGuid(form.getIsisGuid());
          prpPub.setNodeId(SecurityUtils.getCurrentAllNodeId().get(0));
          prpPub.setPubType(pub.getTypeId());
          this.wrapRptPublication(pub, prpPub);
          this.nsfcPrpPubDao.save(prpPub);
        }

      }

      prp.setPrpDate(new Date());
      // 版本号+1
      prp.setVersion(prp.getVersion() + 1);
      this.nsfcProposalDao.save(prp);

    } catch (Exception e) {
      logger.error("保存杰青成果数据失败！", e);
      throw new ServiceException(e);
    }
    return null;
  }

  private String validatePubRule(NsfcPrpPubModel form) {
    String msg = "";
    try {
      NsfcProposal prp = this.nsfcProposalDao.getPrpByIsisGuid(form.getIsisGuid(), SecurityUtils.getCurrentUserId());
      if (prp.getCode() == null || "".equals(prp.getCode())) { // 没有业务规则限制
        return null;
      }
      GrantRule pubRules = proposalRuleService.getGrantRule(prp.getCode());
      msg = pubRules.getHelpTip();
      JSONObject json = JSONObject.fromObject(pubRules.getRules());
      if (json == null) { // 没有规则限制
        return null;
      }
      String[] aPubIds = form.getPubIds().split(",");
      if (json.containsKey("max_pub_total")) {
        int maxTotal = json.getInt("max_pub_total");
        if (maxTotal != 0) {
          int total = this.nsfcPrpPubDao.getPubCountByIsisGuid(form.getIsisGuid());
          // 成果最大数限制
          if (maxTotal > 0 && maxTotal < (total + aPubIds.length)) {
            return msg;
          }
        }
      }

      // 成果类型限制
      if (json.containsKey("pub_type_rule")) {
        JSONArray arr = JSONArray.fromObject(json.get("pub_type_rule"));
        int typeTotal = 0;
        if (arr != null && arr.size() > 0) { // 存在类型限制
          for (Object obj : arr) {
            JSONObject rule = JSONObject.fromObject(obj);
            int pubTypeTotal = this.nsfcPrpPubDao.getTotalBy(form.getIsisGuid(), rule.getString("value").trim());
            int prePubTypeTotal =
                this.publicationService.queryPubTotalByPubIdAndType(form.getPubIds(), rule.getString("value").trim());
            typeTotal += prePubTypeTotal;
            if (rule.getInt("limit") == 0) { // 该类型成果填报数不做限制

              continue;
            }

            if (prePubTypeTotal == 0) {

              continue;
            }
            if (rule.getInt("limit") < (pubTypeTotal + prePubTypeTotal)) {
              logger.warn("成果规则检查异常：" + rule.getString("name") + "超过了允许添加的最大值！");
              return msg;
            }

          }

          // if (typeTotal != aPubIds.length) { // 验证添加的成果类型
          // logger.error("GX杰青成果规则检查异常：存在不允许添加的成果类型！");
          // return false;
          // }
        }
      }
      if (json.containsKey("anthor_apply_type") || json.containsKey("anthor_join_type")) {
        int applyCount = 0;
        int joinCount = 0;
        Set<Long> toTalPubIds = this.nsfcPrpPubDao.getPubIdsByGuid(form.getIsisGuid());
        List<Long> allPubIds = new ArrayList<Long>();
        allPubIds.addAll(toTalPubIds);
        for (String pubId : aPubIds) {
          allPubIds.add(Long.valueOf(pubId));
        }
        for (Long bpubId : allPubIds) {
          PublicationForm pubForm = new PublicationForm();
          pubForm.setPubId(bpubId);
          pubForm = this.scholarPublicationXmlManager.loadXml(pubForm);
          Document doc = null;
          doc = DocumentHelper.parseText(pubForm.getPubXml());
          PubXmlDocument pubXmlDocument = new PubXmlDocument(doc);
          List authors = pubXmlDocument.getNodes(PubXmlConstants.PUB_MEMBERS_MEMBER_XPATH);
          boolean isOwner = false;
          for (int i = 0; i < authors.size(); i++) {
            Element ele = (Element) authors.get(i);
            String owner = StringUtils.trimToEmpty(ele.attributeValue("owner"));// 是否是本人
            if ("1".equals(owner)) {
              applyCount++;
              isOwner = true;
              break;
            }
          }
          if (!isOwner) {
            joinCount++;
          }
        }
        if (json.containsKey("anthor_apply_type")) {
          int applyTypeTotal = json.getInt("anthor_apply_type");
          if (applyCount > applyTypeTotal) {
            return msg;
          }
        }
        if (json.containsKey("anthor_join_type")) {
          int joinTypeTotal = json.getInt("anthor_join_type");
          if (joinCount > joinTypeTotal) {
            return msg;
          }
        }
      }
    } catch (Exception e) {

      logger.error("GX杰青成果规则检查异常", e);

      return msg;
    }

    return null;
  }

  @Override
  public void syncPublicationToProposal(PublicationForm loadXml) throws ServiceException {
    try {

      Publication pub = this.publicationService.getPublicationById(loadXml.getPubId());
      NsfcPrpPub prpPub = this.nsfcPrpPubDao.getPrpPub(loadXml.getPubId(), loadXml.getIsisGuid());
      if (prpPub != null) {
        NsfcProposal prp =
            this.nsfcProposalDao.getPrpByIsisGuid(loadXml.getIsisGuid(), SecurityUtils.getCurrentUserId());
        if (prp.getStatus().equals(1)) {

          return;
        }

        prp.setPrpDate(new Date());
        this.nsfcProposalDao.save(prp);
        prpPub.setNodeId(SecurityUtils.getCurrentAllNodeId().get(0));
        prpPub.setPubType(loadXml.getTypeId());
        this.wrapRptPublication(pub, prpPub);

        this.nsfcPrpPubDao.save(prpPub);
      }

    } catch (Exception e) {
      logger.error("成果编辑--同步到杰青出错!!pubId=" + loadXml.getPubId() + "guid=" + loadXml.getIsisGuid(), e);
      throw new ServiceException("成果编辑--同步到杰青出错!!");
    }
  }

  public static void main(String[] args) {

  }

  @Override
  public void saveProposalPublication(NsfcPrpPubModel form) throws ServiceException {

    try {

      if (StringUtils.isNotBlank(form.getJsonParams())) {
        JSONArray jsonArray = JSONArray.fromObject(form.getJsonParams());
        if (CollectionUtils.isNotEmpty(jsonArray)) {
          for (int i = 0; i < jsonArray.size(); i++) {
            try {
              JSONObject obj = jsonArray.getJSONObject(i);
              Integer pType = obj.getInt("pType");
              Long prpPubId = obj.getLong("prpPubId");
              NsfcPrpPub prpPub = this.nsfcPrpPubDao.get(prpPubId);
              prpPub.setTreatiseType(pType);
              this.nsfcPrpPubDao.save(prpPub);

            } catch (Exception e) {
              logger.error(e.getMessage() + e);
              throw new ServiceException("保存杰青成果失败....");
            }
          }
        }
        if (StringUtils.isNotBlank(form.getIsisGuid())) {

          NsfcProposal prp =
              this.nsfcProposalDao.getPrpByIsisGuid(form.getIsisGuid(), SecurityUtils.getCurrentUserId());
          if (prp != null) {

            prp.setPrpDate(new Date());
            this.nsfcProposalDao.save(prp);
          }

        }

      }
    } catch (Exception e) {

      logger.error("保存杰青成果数据失败！", e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void removeProjectProposalPub(NsfcPrpPubModel form) throws ServiceException {

    try {
      JSONArray jsonArray = JSONArray.fromObject(form.getJsonParams());
      String isisGuid = form.getIsisGuid();
      if (CollectionUtils.isNotEmpty(jsonArray)) {
        for (int i = 0; i < jsonArray.size(); i++) {
          JSONObject obj = jsonArray.getJSONObject(i);
          Long pubId = obj.getLong("pubId");
          this.nsfcPrpPubDao.deletePrpPub(pubId, isisGuid);

        }

      }
      if (StringUtils.isNotBlank(form.getIsisGuid())) {

        NsfcProposal prp = this.nsfcProposalDao.getPrpByIsisGuid(form.getIsisGuid(), SecurityUtils.getCurrentUserId());
        if (prp != null) {

          prp.setPrpDate(new Date());
          prp.setVersion(prp.getVersion() + 1);
          this.nsfcProposalDao.save(prp);
        }

      }

      List<Long> lList = this.nsfcPrpPubDao.getPrpPubOrder(form.getIsisGuid());
      if (CollectionUtils.isNotEmpty(lList)) {
        for (int i = 1; i <= lList.size(); i++) {
          this.nsfcPrpPubDao.updatePrpPubOrder(i, lList.get(i - 1), form.getIsisGuid());
        }
      }

    } catch (Exception e) {

      logger.error("移除isisGuid={}杰青申报成果失败", form.getIsisGuid(), e);

    }

  }

  @Override
  public List<NsfcPrpPub> loadPrpPubsByGuid(SyncProposalModel model) throws ServiceException {
    GrantRule rule = this.grantRuleService.getGrantRule(Long.valueOf(model.getCode()));
    try {
      List<NsfcPrpPub> prpPubs = null;
      if (!rule.getIsUserDefined()) {// 好像国际合作类的页面
        prpPubs = this.nsfcPrpPubDao.getPubsByGuid(model.getGuid(), model.getPsnId());
      } else {
        prpPubs = this.nsfcPrpPubDao.getPubsOrderByType(model.getGuid(), model.getPsnId());
      }
      this.wrapPubTypeName(prpPubs, rule);
      if (StringUtils.isNotEmpty(model.getStatus())) { // 修改申报书状态
        NsfcProposal prp = this.nsfcProposalDao.getPrpByIsisGuid(model.getGuid(), model.getPsnId());
        prp.setStatus(new Integer(model.getStatus()));
        prp.setCtitle(model.getCtitle());
        this.nsfcProposalDao.save(prp);

      }

      return prpPubs;
    } catch (Exception e) {

      logger.error("读取杰青成果列表失败！", e);
      throw new ServiceException(e);
    }

  }

  private List<NsfcPrpPub> wrapPubTypeName(List<NsfcPrpPub> prpPubs, GrantRule rule) throws ServiceException {
    Map<Integer, ConstPubType> cptsMap = new HashMap<Integer, ConstPubType>();
    List<ConstPubType> cpts = constPubTypeService.getAll();
    for (ConstPubType cpt : cpts) {
      cptsMap.put(cpt.getId(), cpt);
    }

    if (rule.getIsUserDefined()) {
      Map<String, ConstDictionary> pubTypeMap =
          this.constDictionaryManage.findConstByCategoryAndCodes("nsfc_pub_type", rule.getTypeValues());
      for (NsfcPrpPub nsfcPrpPub : prpPubs) {
        ConstDictionary constDictionary = pubTypeMap.get(nsfcPrpPub.getTreatiseType().toString());
        nsfcPrpPub.setTreatiseTypeZhName(constDictionary.getZhCnName());
        nsfcPrpPub.setTreatiseTypeEnName(constDictionary.getEnUsName());

        if (nsfcPrpPub.getPubType() != null && !"".equals(nsfcPrpPub.getPubType())) {
          nsfcPrpPub.setPubTypeZhName(cptsMap.get(nsfcPrpPub.getPubType()).getZhName());
          nsfcPrpPub.setPubTypeEnName(cptsMap.get(nsfcPrpPub.getPubType()).getEnName());
        }
      }
    } else {
      for (NsfcPrpPub prpPub : prpPubs) {
        if (prpPub.getPubType() != null && !"".equals(prpPub.getPubType())) {
          prpPub.setPubTypeZhName(cptsMap.get(prpPub.getPubType()).getZhName());
          prpPub.setPubTypeEnName(cptsMap.get(prpPub.getPubType()).getEnName());
        }
      }
    }
    return prpPubs;
  }

  @Override
  public void syncSaveIsisGuid(SyncProposalModel model) throws ServiceException {

    try {

      NsfcProposal prp = new NsfcProposal();
      prp.setIsisGuid(model.getGuid());
      prp.setPrpPsnId(model.getPsnId());
      prp.setStatus(2);
      // prp.setPrpDate(new Date());
      this.nsfcProposalDao.save(prp);

    } catch (Exception e) {
      logger.error("生成isisGuid={}的记录失败！", model.getGuid());
      throw new ServiceException(e);
    }

  }

  @Override
  public void syncSaveProposal(SyncProposalModel model) throws ServiceException {
    try {

      if (model.getStatus().equals("2")) { // 暂存状态
        try {
          syncSaveIsisGuid(model);

        } catch (Exception e) {

          logger.error("初始化sns杰青申请书信息失败！", e);
        }
        return;
      } else if (model.getStatus().equals("3")) { // 删除项目
        try {
          this.nsfcProposalDao.deleteByGuids(model.getGuid(), model.getPsnId());
          this.nsfcPrpPubDao.deletePrpPub(model.getGuid());
        } catch (Exception e) {

          logger.error("删除isisGuid={}项目失败", model.getGuid(), e);
        }

      } else {

        NsfcProposal prp = this.nsfcProposalDao.getPrpByIsisGuid(model.getGuid(), model.getPsnId());

        if (model.getIsRollBack() != null && model.getIsRollBack().booleanValue()) {
          prp.setIsisGuid(model.getGuid());
          prp.setPrpPsnId(model.getPsnId());
          prp.setStatus(NumberUtils.toInt(model.getStatus()));
        } else {
          if (prp == null) {
            prp = new NsfcProposal();

          }
          prp.setEtitle(model.getEtitle());
          if (model.getCode() != null && !"".equals(model.getCode())) {
            prp.setCode(Long.valueOf(model.getCode()));
          }
          prp.setCtitle(model.getCtitle());
          prp.setIsisGuid(model.getGuid());
          prp.setPrpPsnId(model.getPsnId());
          prp.setPrpYear(NumberUtils.toInt(model.getYear()));
          prp.setStatus(NumberUtils.toInt(model.getStatus()));
          prp.setVersion(0);
        }

        // prp.setPrpDate(new Date());
        this.nsfcProposalDao.save(prp);

      }
    } catch (Exception e) {

      logger.error("同步isisGuid={}的杰青申报书信息失败！", model.getGuid());
      throw new ServiceException(e);

    }

  }

  @Override
  public void saveProposal(String guid) throws ServiceException {
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      NsfcProposal prp = this.nsfcProposalDao.getPrpByIsisGuid(guid, psnId);
      if (prp == null) {
        prp = new NsfcProposal();
        prp.setIsisGuid(guid);
        prp.setPrpPsnId(psnId);
        prp.setStatus(2);
        prp.setPrpDate(new Date());
        this.nsfcProposalDao.save(prp);

      }

    } catch (Exception e) {

      logger.error("查询guid={},psnId={}申报信息失败！", new Object[] {guid, SecurityUtils.getCurrentUserId(), e});
    }

  }

  @Override
  public boolean isCanEditable(String isisGuid) throws ServiceException {
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      NsfcProposal prp = this.nsfcProposalDao.getPrpByIsisGuid(isisGuid, psnId);
      if (prp == null || !prp.getStatus().equals(1)) {

        return true;
      }
    } catch (Exception e) {

      logger.error("查询guid={}的申报书信息失败！", e);
      return false;
    }

    return false;
  }

  @Override
  public Long getRepPubsTotal(String isisGuid) throws ServiceException {
    try {
      return this.nsfcPrpPubDao.loadRepPubs(isisGuid, SecurityUtils.getCurrentUserId());
    } catch (DaoException e) {
      // TODO Auto-generated catch block
      logger.error("获取psnId={}的近五年代表性论著统计信息失败", new Object[] {SecurityUtils.getCurrentUserId(), e});
      return 0L;
    }

  }

  @Override
  public void updatePrpPubCitation(String isisGuid, Long pubId, String citation) throws ServiceException {
    try {
      NsfcPrpPub nsfcPrpPub = nsfcPrpPubDao.getPrpPub(pubId, isisGuid);
      nsfcPrpPub.setListInfo(citation);
      nsfcPrpPubDao.save(nsfcPrpPub);

      PublicationForm pubForm = new PublicationForm();
      pubForm.setPubId(pubId);
      pubForm = this.scholarPublicationXmlManager.loadXml(pubForm);
      if (pubForm != null) {
        String xml = pubForm.getPubXml();
        String[] citations = StringUtils.split(citation, ",");
        Integer listIstp = 0;
        Integer listSci = 0;
        Integer listEi = 0;
        Integer listSsci = 0;
        if (citations.length > 0) {
          for (String cited : citations) {
            if ("EI".equals(cited)) {
              listEi = 1;
            }
            if ("SCI".equals(cited)) {
              listSci = 1;
            }
            if ("ISTP".equals(cited)) {
              listIstp = 1;
            }
            if ("SSCI".equals(cited)) {
              listSsci = 1;
            }
          }
        }
        xml = this.reBuildCitedXml(xml, listEi, listSci, listIstp, listSsci);
        PubXmlProcessContext context = new PubXmlProcessContext();
        context.setCurrentPubId(pubId);
        context.setCurrentUserId(SecurityUtils.getCurrentUserId().longValue());
        context.setCurrentAction(XmlOperationEnum.Edit);
        publicationService.updatePublication(xml, context);
      }
    } catch (Exception e) {
      logger.error("更新收录情况出现异常！");
      e.printStackTrace();
    }
  }

  @Override
  public void savePublicationSort(NsfcPrpPubModel form) throws ServiceException {
    Assert.notNull(form.getIsisGuid(), "不允许为空！");
    try {
      NsfcProposal prp = this.nsfcProposalDao.getPrpByIsisGuid(form.getIsisGuid(), SecurityUtils.getCurrentUserId());
      if (prp.getVersion().equals(form.getVersion())) {
        JSONArray jArray = JSONArray.fromObject(form.getJsonParams());
        if (CollectionUtils.isNotEmpty(jArray)) {
          List saveList = new ArrayList();
          for (int i = 0; i < jArray.size(); i++) {
            JSONObject obj = jArray.getJSONObject(i);
            Integer seqNo = obj.getInt("seqNo");
            Long pubId = obj.getLong("pubId");

            NsfcPrpPub prpPub = this.nsfcPrpPubDao.getPrpPub(pubId, form.getIsisGuid());

            prpPub.setSeqNo(seqNo);
            saveList.add(prpPub);
          }

          this.nsfcPrpPubDao.savePrpPubs(saveList);
        }
        prp.setVersion(prp.getVersion() + 1);
      }
    } catch (Exception e) {
      logger.error("调整排序功能失败！", e);
    }

  }

  private String reBuildCitedXml(String xml, Integer listEi, Integer listSci, Integer listIstp, Integer listSsci) {
    try {
      PubXmlDocument doc = new PubXmlDocument(xml);
      if (!doc.existsNode(PubXmlConstants.PUB_LIST_XPATH)) {
        doc.createElement(PubXmlConstants.PUB_LIST_XPATH);
      }
      doc.setNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ei", listEi == null || listEi == 0 ? "0" : "1");
      doc.setNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_sci", listSci == null || listSci == 0 ? "0" : "1");
      doc.setNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_istp", listIstp == null || listIstp == 0 ? "0" : "1");
      doc.setNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ssci", listSsci == null || listSsci == 0 ? "0" : "1");
      String listSSCI = doc.getXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ssci");
      String listSCI = doc.getXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_sci");
      String listISTP = doc.getXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_istp");
      if ((listSSCI == null || "0".equals(listSSCI) || "".equals(listSSCI)) && (listSCI == null || "0".equals(listSCI))
          && (listISTP == null || "0".equals(listISTP))) {
        doc.removeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cite_times");
      }
      return doc.getXmlString();
    } catch (DocumentException e) {
      logger.error("xml格式有错" + xml);
    }
    return null;
  }

  @Override
  public boolean checkSort(NsfcPrpPubModel form) throws ServiceException {
    Assert.notNull(form.getIsisGuid(), "不允许为空！");
    JSONArray jArray = JSONArray.fromObject(form.getJsonParams());
    if (CollectionUtils.isNotEmpty(jArray)) {
      try {
        for (int i = 0; i < jArray.size(); i++) {
          JSONObject obj = jArray.getJSONObject(i);
          Integer oldSeqNo = obj.getInt("oldSeqNo");
          Long pubId = obj.getLong("pubId");
          NsfcPrpPub prpPub = this.nsfcPrpPubDao.getPrpPub(pubId, form.getIsisGuid());
          if (!oldSeqNo.equals(prpPub.getSeqNo())) {
            return false;
          }
        }

      } catch (Exception e) {
        logger.error("检查排序功能时失败！", e);
        throw new ServiceException(e);
      }
    }
    return true;
  }

  @Override
  public List<NsfcPrpPub> findNsfcPrpPubByPubId(Long pubId) throws ServiceException {
    try {
      return this.nsfcPrpPubDao.findNsfcPrpPubByPubId(pubId);
    } catch (DaoException e) {
      logger.error("根据PubId查找申请书成果失败！PubId=" + pubId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveNsfcPrpPub(NsfcPrpPub nsfcPrpPub) throws ServiceException {
    this.nsfcPrpPubDao.save(nsfcPrpPub);
  }

  @Override
  public List<NsfcProposal> getNsfcProposalList(Long psnId) {
    return nsfcProposalDao.getNsfcProposalList(psnId);
  }

  @Override
  public void delNsfcProposal(NsfcProposal proposal) {
    nsfcProposalDao.delete(proposal);
  }

  @Override
  public void saveNsfcProposal(NsfcProposal proposal) {
    nsfcProposalDao.save(proposal);
  }
}
