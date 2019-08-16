package com.smate.center.batch.service.pub;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.psn.PsnPubStatDao;
import com.smate.center.batch.dao.sns.psn.inforefresh.PsnRefreshPubInfoDao;
import com.smate.center.batch.dao.sns.psn.inforefresh.PsnRefreshUserInfoDao;
import com.smate.center.batch.dao.sns.pub.GroupPsnNodeDao;
import com.smate.center.batch.dao.sns.pub.GroupPubDao;
import com.smate.center.batch.dao.sns.pub.GrpPubsDao;
import com.smate.center.batch.dao.sns.pub.PubDataStoreDao;
import com.smate.center.batch.dao.sns.pub.PubFolderItemsDao;
import com.smate.center.batch.dao.sns.pub.PubFulltextDao;
import com.smate.center.batch.dao.sns.pub.PubKnowDao;
import com.smate.center.batch.dao.sns.pub.PubMemberDao;
import com.smate.center.batch.dao.sns.pub.PubSimpleDao;
import com.smate.center.batch.dao.sns.pub.PublicationDao;
import com.smate.center.batch.dao.sns.pub.PublicationJournalDao;
import com.smate.center.batch.dao.sns.pub.PublicationListDao;
import com.smate.center.batch.dao.sns.pub.RcmdSyncPsnInfoDao;
import com.smate.center.batch.dao.sns.pub.TaskGroupResNotifyDao;
import com.smate.center.batch.enums.pub.PublicationOperationEnum;
import com.smate.center.batch.enums.pub.XmlOperationEnum;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.PublicationNotFoundException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.PdwhPublicationAll;
import com.smate.center.batch.model.sns.psn.PsnRefreshPubInfo;
import com.smate.center.batch.model.sns.psn.PsnRefreshUserInfo;
import com.smate.center.batch.model.sns.pub.GroupPsnNode;
import com.smate.center.batch.model.sns.pub.GrpPubs;
import com.smate.center.batch.model.sns.pub.IPubXmlServiceFactory;
import com.smate.center.batch.model.sns.pub.KeywordSplit;
import com.smate.center.batch.model.sns.pub.PrivacySettings;
import com.smate.center.batch.model.sns.pub.PubAuthor;
import com.smate.center.batch.model.sns.pub.PubDataStore;
import com.smate.center.batch.model.sns.pub.PubDupFields;
import com.smate.center.batch.model.sns.pub.PubDupParam;
import com.smate.center.batch.model.sns.pub.PubErrorFields;
import com.smate.center.batch.model.sns.pub.PubFulltext;
import com.smate.center.batch.model.sns.pub.PubGrouping;
import com.smate.center.batch.model.sns.pub.PubInfo;
import com.smate.center.batch.model.sns.pub.PubKnow;
import com.smate.center.batch.model.sns.pub.PubMember;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.model.sns.pub.Publication;
import com.smate.center.batch.model.sns.pub.PublicationForm;
import com.smate.center.batch.model.sns.pub.PublicationJournal;
import com.smate.center.batch.model.sns.pub.PublicationList;
import com.smate.center.batch.model.sns.pub.PublicationPdwh;
import com.smate.center.batch.model.sns.pub.PublicationXml;
import com.smate.center.batch.model.sns.pub.TaskGroupResNotify;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pdwh.isipub.IsiPublicationService;
import com.smate.center.batch.service.pdwh.pub.JournalService;
import com.smate.center.batch.service.pdwh.pub.PublicationPdwhService;
import com.smate.center.batch.service.pdwh.pub.PublicationXmlPdwhService;
import com.smate.center.batch.service.psn.PersonManager;
import com.smate.center.batch.service.psn.PsnScoreService;
import com.smate.center.batch.service.pub.mq.DelResProducer;
import com.smate.center.batch.service.pub.mq.PsnStatisticsRefreshMessageProducer;
import com.smate.center.batch.service.pub.mq.PubModifySyncProducer;
import com.smate.center.batch.service.pub.mq.PubSyncToPubFtSrvProducer;
import com.smate.center.batch.service.pub.mq.RcmdSyncFlagMessageProducer;
import com.smate.center.batch.service.pub.mq.ResumeAuthorityProducer;
import com.smate.center.batch.service.pub.mq.SnsPubTotalSyncProducer;
import com.smate.center.batch.service.pub.pubgrouping.PubGroupingService;
import com.smate.center.batch.service.pubfulltexttoimage.PubFullTextService;
import com.smate.center.batch.service.user.UserSettingsService;
import com.smate.center.batch.util.pub.BasicRmtSrvModuleConstants;
import com.smate.center.batch.util.pub.DynamicConstant;
import com.smate.center.batch.util.pub.LogUtils;
import com.smate.center.batch.util.pub.PubConstFieldRefresh;
import com.smate.center.batch.util.pub.PubFulltextPermission;
import com.smate.center.batch.util.pub.PublicationArticleType;
import com.smate.center.batch.util.pub.PublicationDetailFrom;
import com.smate.core.base.psn.consts.PsnCnfConst;
import com.smate.core.base.psn.model.psncnf.PsnConfigPub;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.common.HashUtils;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.file.ArchiveFileUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.IrisNumberUtils;
import com.smate.core.base.utils.string.IrisStringUtils;
import com.smate.core.base.utils.string.ServiceUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 成果、参考文献SERVICE. 增删改
 * 
 * @author liqinghua
 * 
 */
@Service("publicationService")
@Transactional(rollbackFor = Exception.class)
public class PublicationServiceImpl implements PublicationService, PublicationSaveService {
  private static final long serialVersionUID = -2781411896625319233L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PublicationDao publicationDao;
  @Autowired
  private PubKnowDao pubKnowDao;
  @Autowired
  private PubDataStoreDao pubDataStoreDao;
  @Autowired
  private PubFolderItemsDao pubFolderItemsDao;
  @Autowired
  private PublicationListDao publicationListDao;
  @Autowired
  private PsnRefreshUserInfoDao psnRefreshUserInfoDao;
  @Autowired
  private PsnRefreshPubInfoDao psnRefreshPubInfoDao;
  @Autowired
  private PublicationLogService publicationLogService;
  @Autowired
  private PubFolderService pubFolderService;
  @Autowired
  private PsnPubStatDao psnPubStatDao;
  @Autowired
  private PubMemberDao pubMemberDao;
  @Autowired
  private ScholarPublicationXmlManager scholarPublicationXmlManager;
  @Autowired
  private PublicationXmlService publicationXmlService;
  @Autowired
  private DynamicProduceService dynamicProduceAddPubService;
  @Resource(name = "snsPubTotalSyncProducer")
  private SnsPubTotalSyncProducer snsPubTotalSyncProducer;
  @Autowired
  private PublicationGroupService publicationGroupService;
  @Autowired
  private GroupPsnNodeDao groupPsnNodeDao;
  @Autowired
  private GroupPubDao groupPubDao;
  @Autowired
  private PubSimpleDao pubSimpleDao;
  @Autowired
  private PubFulltextDao pubFulltextDao;
  @Autowired
  private PublicationStatisticsService publicationStatisticsService;
  @Autowired
  private TaskGroupResNotifyDao taskGroupResNotifyDao;
  @Autowired
  private PublicationWrapService publicationWrapService;
  @Autowired
  private LabPubService labPubService;
  @Autowired
  private NsfcPrpPubService nsfcPrpPubService;
  @Autowired
  private PubFinalReportService pubFinalReportService;
  @Autowired
  private PubResearchRptService pubResearchRptService;
  @Autowired
  private ExpertPubService expertPubService;
  @Resource(name = "pubListServiceProxy")
  private PublicationListService pubListServiceProxy;
  @Autowired
  private PubDupService pubDupService;
  @Autowired
  private PubKeyWordsService pubKeyWordsService;
  @Autowired
  private PersonManager personManager;
  @Autowired
  private PsnScoreService psnScoreService;
  @Autowired
  private ResumeAuthorityProducer resumeAuthorityProducer;
  @Autowired
  private PublicationPdwhService publicationPdwhService;
  @Autowired
  private UserSettingsService userSettingsService;
  @Autowired
  private PubModifySyncProducer pubModifySyncProducer;
  @Autowired
  private DelResProducer delResProducer;
  @Autowired
  private MyPublicationQueryService myPublicationQueryService;
  @Autowired
  private FriendService friendService;
  @Autowired
  private DynamicProduceService dynamicProduceUploadFTPubService;
  @Autowired
  private GroupService groupService;
  @Autowired
  private PubOwnerMatchService pubOwnerMatchService;
  @Autowired
  private PublicationJournalService pubJournalService;
  @Autowired
  private PublicationRefcJournalService pubRefcJournalService;
  @Autowired
  private RcmdSyncFlagMessageProducer rcmdSyncFlagMessageProducer;
  @Autowired
  private PsnStatisticsRefreshMessageProducer psnStatisticsRefreshMessageProducer;
  @Autowired
  private IrisExcludedPubService irisExcludedPubService;
  @Autowired
  private PubFullTextService pubFulltextService;
  @Autowired
  private PublicationListService publicationListService;
  @Autowired
  private KeywordsDicService keywordsDicService;
  @Autowired
  private PublicationJournalDao publicationJournalDao;
  @Autowired
  private PubGroupingService pubGroupingService;
  @Autowired
  private PubSyncToPubFtSrvProducer pubSyncToPubFtSrvProducer;
  @Autowired
  private PubFundinfoService pubFundinfoService;
  @Autowired
  private PsnCnfService psnCnfService;
  @Autowired
  private IPubXmlServiceFactory scholarPublicationXmlServiceFactory;
  @Autowired
  private IsiPublicationService isiPublicationService;
  @Autowired
  private PublicationXmlPdwhService publicationXmlPdwhService;
  @Autowired
  private JournalService journalService;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  PubSimpleService pubSimpleService;
  @Autowired
  private RcmdSyncPsnInfoDao rcmdSyncPsnInfoDao;
  @Autowired
  private DynamicGroupProduceService dynamicGroupProduceService;
  @Autowired
  private GrpPubsDao grpPubsDao;

  @Override
  public void deletePublication(String pubIds) throws ServiceException {
    String[] tmpPubIds = StringUtils.split(pubIds, ",");
    try {
      for (String tmpPubId : tmpPubIds) {
        Long pubId = null;
        if (StringUtils.isNumeric(tmpPubId)) {
          pubId = Long.valueOf(tmpPubId);
        } else {
          pubId = NumberUtils.toLong(ServiceUtil.decodeFromDes3(tmpPubId));
        }
        Publication publication = publicationDao.get(pubId);
        // ========================================================
        // SCM-12264_20170503_需求: 删除个人成果,不同步删除群组成果,且点击群组成果的标题能进入详情
        // 所以该条成果是群组成果的话,v_pub_simple.status改为5,publication.status改为5
        List<GrpPubs> grpPubsList = grpPubsDao.findGrpPubs(pubId);
        if (CollectionUtils.isNotEmpty(grpPubsList)) {
          pubSimpleDao.updateStatus(5, new Date().getTime(), pubId);
          publication.setStatus(5);
        } else {
          publication.setStatus(1);
        }
        // SCM-11347 WSN_2017-8-4
        publication.setUpdateDate(new Date());
        // ========================================================
        // Publication pe = publicationDao.get(pubId);
        // articleType = pe.getArticleType();
        // // 把已加入文件夹的成果记录删除
        // this.pubFolderService.removePublicationFromPubFolder(pe.getId().toString(),
        // articleType);
        // SCM-11347 WSN_2017-8-4
        this.updatePubXmlDealDate(pubId);
        publicationDao.save(publication);
      }
    } catch (Exception e) {
      logger.error("deletePublication删除成果、文献出错pubId: " + pubIds, e);
      throw new ServiceException(e);
    }

  }

  // 更新成果xml的最后更新时间
  private void updatePubXmlDealDate(Long pubId) {
    try {
      PublicationXml pubXml = this.publicationXmlService.getById(pubId);
      String xml = pubXml.getXmlData();
      if (StringUtils.isBlank(xml)) {
        return;
      }
      PubXmlDocument doc = new PubXmlDocument(xml);
      Date date = new Date();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      doc.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "last_update_date", sdf.format(date));
      xml = doc.getXmlString();
      this.publicationXmlService.save(pubId, xml);
    } catch (Exception e) {
      logger.error("更新成果xml中最后更新时间出错， pubId=" + pubId, e);
    }
  }

  @Override
  public void deletePublicationComplementaryProcesses(String getPubIds) throws BatchTaskException {
    Long opPsnId = 9999999999999L;
    String[] tmpPubIds = StringUtils.split(getPubIds, ",");
    try {
      List<Long> authIdList = new ArrayList<Long>();
      for (String tmpPubId : tmpPubIds) {
        Long pubId = null;
        if (StringUtils.isNumeric(tmpPubId)) {
          pubId = Long.valueOf(tmpPubId);
        } else {
          pubId = NumberUtils.toLong(ServiceUtil.decodeFromDes3(tmpPubId));
        }
        Publication pe = publicationDao.get(pubId);
        opPsnId = pe.getPsnId();
        // 删除 改成果的分组记录 tsz
        pubGroupingService.delGroupingByPubId(pubId);
        // 将重复数据删除.
        pubDupService.setPubCanDup(pe.getId(), false);
        Map<String, String> opDetail = new HashMap<String, String>();
        opDetail.put("function", "my outputs");
        this.publicationLogService.logOp(pubId, opPsnId, null, PublicationOperationEnum.Delete, opDetail);
        if (pe.getArticleType() == PublicationArticleType.OUTPUT) {
          // 个人成果关键词删除
          pubKeyWordsService.delPubKeywords(pubId);
          pubOwnerMatchService.delPubOwnerMatch(pubId);
          pubJournalService.delPubJournal(pubId);
          psnPubStatDao.decrTotalOutputs(opPsnId, 1);
          // 发送同步消息到单位
          snsPubTotalSyncProducer.sendSnsPubTotal(opPsnId);
          // 刷新成果记录
          rcmdSyncFlagMessageProducer.syncPsnPub(pe.getPsnId(), pe.getId(), 1);
          // 冗余信息更新至sns
          PsnRefreshUserInfo psnRefInfo = new PsnRefreshUserInfo(pe.getPsnId());
          psnRefInfo.setPub(1);
          // 保存刷新的成果
          PsnRefreshPubInfo psnRefPub = psnRefreshPubInfoDao.get(pubId);
          if (psnRefPub == null) {
            psnRefPub = new PsnRefreshPubInfo(pubId, pe.getPsnId());
          }
          psnRefPub.setIsDel(1);
          psnRefPub.setStatus(0);
          psnRefreshPubInfoDao.save(psnRefPub);
          psnRefreshUserInfoDao.save(psnRefInfo);
          // 刷新基金标注记录
          pubFundinfoService.syncPubFundinfo(pe.getId(), pe.getPsnId(), null, null);
        } else if (pe.getArticleType() == PublicationArticleType.REFERENCE) {
          // 删除文献冗余数据
          pubRefcJournalService.delPubRefcJournal(pubId);
          // 刷新文献信息
          rcmdSyncFlagMessageProducer.syncPsnRefc(pe.getPsnId(), pe.getId(), 1);
          // 冗余数据更新至sns
          PsnRefreshUserInfo psnRefInfo = new PsnRefreshUserInfo(pe.getPsnId());
          psnRefInfo.setRefc(1);
          psnRefreshUserInfoDao.save(psnRefInfo);
        }
        // // 同步到群组节点
        // this.publicationGroupSyncMessage(null, pe, null, "del");
        authIdList.add(pubId);
        // 置为空值以便删除全文信息.
        String fulltextFileId = pe.getFulltextFileid();
        pe.setFulltextFileid("");
        this.savePubFulltext(pe);
        pe.setFulltextFileid(fulltextFileId);
        pubModifySyncProducer.sendSyncMessage(pe, 1);
        // 同步删除其他记录 ,在老系统中已经被注释掉。在此处先注释，
        // delResProducer.syncDelResMessage(pe.getId(),
        // pe.getArticleType(), pe.getPsnId());
        // 将pub_know表中对应pub_id置1
        PubKnow pubKnow = this.pubKnowDao.get(pubId);
        if (pubKnow != null) {
          pubKnow.setStatus(1);
          this.pubKnowDao.save(pubKnow);
        }
      }
      // 同步删除隐私设置
      resumeAuthorityProducer.syncResumeAuthority(SecurityUtils.getCurrentUserId(), authIdList, "pub", 1);
      // 重构个人成果成果相关统计
      psnStatisticsRefreshMessageProducer.refreshPsnPubStatistics(opPsnId);
      System.gc();
    } catch (Exception e) {
      logger.error("deletePublicationComplementaryProcesses删除成果、文献出错pubId: " + getPubIds, e);
      throw new BatchTaskException(e);
    }

  }

  @Override
  public void pubCreateDynamic(PubXmlDocument xmlDocument, PubXmlProcessContext context) {
    // 产生动态 抽取到 过程的子任务中去
    if (context.getCurrentPubId() != null) {
      try {
        Long producer = SecurityUtils.getCurrentUserId();

        Long groupId = context.getGroupId();

        if (producer == null || producer == 0L) {
          producer = context.getCurrentUserId();
        }

        // int nodeId =
        // SecurityUtils.getCurrentUserNodeId();,统一使用sns节点，默认
        int nodeId = ServiceConstants.SCHOLAR_NODE_ID_1;
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("resType", context.getArticleType());
        jsonObject.accumulate("sameFlag",
            context.getArticleType() + "_" + nodeId + "_" + UUID.randomUUID().toString().replace("-", ""));
        jsonObject.accumulate("producer", producer);
        if (context.getArticleType() == 2) {
          jsonObject.remove("permission");
          PrivacySettings ps = userSettingsService.loadPsByPsnId(producer, DynamicConstant.DYN_SETTING_ADDREF);
          jsonObject.accumulate("permission", ps == null ? 0 : ps.getPermission());
        } else {
          String authority = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "authority");
          int permission = 0;
          if (StringUtils.isNotBlank(authority)) {
            permission = Integer.parseInt(authority);
            switch (permission) {
              case 7:
                permission = 0;// 所有人可见
                break;
              case 6:
                permission = 1;// 好友可见
                break;
              case 4:
                permission = 2;// 本人可见
                break;
            }
          }
          jsonObject.accumulate("permission", permission);
        }
        JSONArray resDetails = new JSONArray();
        JSONObject resJsonObject = new JSONObject();
        resJsonObject.accumulate("resId", context.getCurrentPubId());
        resJsonObject.accumulate("resNode", nodeId);
        resDetails.add(resJsonObject);
        jsonObject.accumulate("resDetails", resDetails.toString());
        // 产生个人动态,groupId默认1L,非群组动态
        if (groupId == 1L) {

          this.dynamicProduceAddPubService.produceDynamic(jsonObject.toString());

        } else {// 产生群组动态

          jsonObject.accumulate("groupId", groupId);
          this.dynamicGroupProduceService.produceGroupDynamic(jsonObject.toString());
          logger.info("产生动态成功");
        }
      } catch (Exception e) {
        logger.error("产生动态时出错========", e);
      }
    }
  }

  @Override
  public void updateFullTextDynamic(PubXmlDocument newDoc, PubXmlProcessContext context) {
    // 全文附件动态
    try {
      String fullTextId = newDoc.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "file_id");
      String oldFullTextId = context.getOldFullTextId();
      String hasCreateDyn = newDoc.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "fulltext_dyn");
      if (StringUtils.isNotBlank(fullTextId)) {

        // 修改上传全文需要与直接上传全文区分开发送动态
        if ((StringUtils.isBlank(hasCreateDyn) || "0".equals(hasCreateDyn))
            && (StringUtils.isBlank(oldFullTextId) || !fullTextId.equals(oldFullTextId))) {
          JSONArray resDetails = new JSONArray();

          Long producer = SecurityUtils.getCurrentUserId();
          if (producer == null || producer == 0L) {
            producer = context.getCurrentUserId();
          }
          // int nodeId =
          // SecurityUtils.getCurrentUserNodeId();,统一使用sns节点，默认
          int nodeId = ServiceConstants.SCHOLAR_NODE_ID_1;
          JSONObject resJsonObject = new JSONObject();
          resJsonObject.accumulate("resNode", nodeId);
          resJsonObject.accumulate("resId", context.getCurrentPubId());
          resDetails.add(resJsonObject);
          JSONObject jsonObject = new JSONObject();
          jsonObject.accumulate("resType", context.getArticleType());
          jsonObject.accumulate("sameFlag", DynamicConstant.RES_TYPE_PUB + "_" + SecurityUtils.getCurrentUserNodeId()
              + "_" + UUID.randomUUID().toString().replace("-", ""));
          jsonObject.accumulate("producer", producer);

          String pubPermission = newDoc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "authority");
          int permission = 0;
          if (StringUtils.isNotBlank(pubPermission)) {
            permission = Integer.parseInt(pubPermission);
            switch (permission) {
              case 7:
                permission = 0;// 所有人可见
                break;
              case 6:
                permission = 1;// 好友可见
                break;
              case 4:
                permission = 2;// 本人可见
                break;
            }
          }
          jsonObject.accumulate("permission", permission);// 成果的权限
          jsonObject.accumulate("resDetails", resDetails.toString());
          dynamicProduceUploadFTPubService.produceDynamic(jsonObject.toString());
        }
      }
    } catch (Exception e) {
      logger.error("产生动态时出错啦！");
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.scmweb.service.publication.PublicationService#getPublication
   * (com.iris.scm.scmweb.model.publication .PublicationForm)
   */
  @Override
  public PublicationForm getPublication(PublicationForm form) throws ServiceException {
    try {
      return this.scholarPublicationXmlManager.loadXml(form);
    } catch (PublicationNotFoundException e) {
      logger.error("没有找到成果" + form.getDes3Id());
    }
    return null;
  }

  @Override
  public PublicationForm getPublication(Long pubId) throws ServiceException {
    PublicationForm form = new PublicationForm();
    form.setPubId(pubId);
    return this.getPublication(form);
  }

  /*
   * ~~~~~~~~~~~~~~~~~~~~~~~~~~对PublicationSaveService的实现~~~~~~~~~~~~~~~~~~~~~~
   * ~~~~~~~~~~~~~~~~~~~~~~~~
   */

  /**
   * 重构作者
   * <p>
   * <li>通讯作者前加*号</li>
   * <li>已经关联作者的加粗Strong，即member_psn_id中有值.</li>
   * </p>
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  private Map<String, String> buildPubAuthorNames(long pubId) throws ServiceException {
    try {

      Map<String, String> map = new HashMap<String, String>();
      List<PubMember> authors = publicationDao.getPubMembersByPubId(pubId);
      StringBuffer authorNames = new StringBuffer();
      String allNames = "";
      String briefNames = "";
      String splitStr = ", ";
      String endStr = "……";
      if (CollectionUtils.isNotEmpty(authors)) {
        for (int i = 0; i < authors.size(); i++) {
          PubMember item = authors.get(i);
          String name = StringUtils.trimToEmpty(item.getName());
          if ("".equals(name)) {
            continue;
          }
          Long psnId = item.getPsnId();
          if (1 == item.getAuthorPos()) {
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
      }

      if (CollectionUtils.isNotEmpty(authors)) {
        allNames = authorNames.toString();
        if (StringUtils.isBlank(briefNames)) {
          briefNames = allNames;
        }
      }
      map.put("all_names", allNames);
      map.put("brief_names", briefNames);
      return map;

    } catch (DaoException e) {
      logger.error("读取成果成员列表错误;pubId: " + pubId, e);
      throw new ServiceException(e);
    }

  }

  /**
   * 保存新添加成果.
   * 
   * @param pub
   * @throws ServiceException
   */
  @Override
  public Publication savePubCreate(PubXmlDocument doc, PubXmlProcessContext context) throws ServiceException {

    try {

      if (context.getPubSimple() == null || context.getPubSimple().getPubId() == null) {
        return null;
      }
      Date now = null;
      if (context.getPubSimple().getCreateDate() != null) {
        now = context.getPubSimple().getCreateDate();
      } else {
        now = new Date();
      }
      Publication pub = new Publication();

      pub.setId(context.getPubSimple().getPubId());
      pub.setArticleType(doc.getArticleTypeId());

      pub.setCreatePsnId(context.getCurrentUserId());// 当前成果id
      // 源来代码注释
      /*
       * if (context.getCurrentPubStatus() != null && context.getCurrentPubStatus() > 1) {// 成果当前状态
       * pub.setStatus(context.getCurrentPubStatus()); } else { pub.setStatus(0); }
       */
      // tj获取成果状态
      if (context.getPubSimple().getUpdateMark() != null) {
        pub.setUpdateMark(context.getPubSimple().getUpdateMark());
      }
      if (context.getPubSimple().getStatus() != null && context.getPubSimple().getStatus() > 1) {
        pub.setStatus(context.getPubSimple().getStatus());

      } else {
        pub.setStatus(0);
      }

      pub.setPsnId(context.getCurrentUserId());
      wrapPublicationSaveField(doc, context, now, pub);

      pub.setTypeId(doc.getPubTypeId());
      pub.setVersionNo(1);

      pub.setPubStart(0);
      pub.setPubStartPsns(0);
      pub.setPubReviews(0);
      pub.setCreateDate(now);
      publicationDao.save(pub);
      doc.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "pub_id", pub.getId().toString());
      doc.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "version_no", pub.getVersionNo().toString());
      PublicationList publist = null;
      // 2015-11-3 在第一阶段已完成
      // 第一阶段没有保存pubList的地方，这里还需要；SCM-17623
      if (context.isImport() || context.isSyncPub()) {// 是否导入 //是否是同步成果
        // 成果导入时pubList拆分（包括原始引用情况）
        publist = this.pubListServiceProxy.praseSourcePubList(doc);
      } else {
        // 解析收录情况
        publist = this.pubListServiceProxy.prasePubList(doc);
      }
      publist = publicationListDao.get(pub.getId());
      // 设置CitedList.
      this.setPubCitedList(pub, publist, doc);

      context.setCurrentPubId(pub.getId());
      // 保存成果查重字段
      this.parsePubDupFields(doc, pub, pub.getStatus());
      if (pub.getArticleType().equals(PublicationArticleType.OUTPUT)) {
        context.setPrivacyLevel(context.getPrivacyLevel() == null ? 0 : context.getPrivacyLevel());// 成果默认所有人可见。
      } else if (pub.getArticleType().equals(PublicationArticleType.REFERENCE)) {
        context.setPrivacyLevel(context.getPrivacyLevel() == null ? 1 : context.getPrivacyLevel());// 文献默认好友可见。
      }

      // 建立成果、成员关系数据
      savePubMember(doc, context);
      pub.setAuthorNames(context.getPubSimple().getAuthorNames());
      if (pub.getStatus() == 0 && pub.getArticleType().equals(PublicationArticleType.OUTPUT)) {

        String startPage = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "start_page");
        String endPage = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "end_page");
        String articleNo = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "article_number");
        if ("".equals(startPage) && "".equals(endPage) && StringUtils.isNotBlank(articleNo)) {
          doc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "not_number", Integer.toString(1));
        }

        String zhKeywords = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_keywords");
        String enKeywords = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_keywords");
        // 该方法已废弃 zk 个人特征关键词
        // 同步成果关键词到基准库
        // this.psnKeywordService.syncPsnPubKeywords(context.getCurrentUserId(),
        // pub, publist, zhKeywords,
        // enKeywords);
        // 成果关键词拆分保存
        this.pubKeyWordsService.savePubKeywords(pub.getId(), context.getCurrentUserId(), zhKeywords, enKeywords);

        if (pub.getTypeId().equals(PublicationTypeEnum.JOURNAL_ARTICLE)) {
          // 保存成果期刊信息
          String jname = doc.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jname");
          String issn = doc.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jissn");
          if (StringUtils.isBlank(issn)) {
            issn = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "issn");
          }
          this.pubJournalService.savePubJournal(pub.getId(), context.getCurrentUserId(), issn, jname, pub.getJnlId(),
              pub.getPublishYear());
          pub.setIssn(issn);
        }

        if (pub.getTypeId().equals(PublicationTypeEnum.CONFERENCE_PAPER)) {
          String confName = doc.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "conf_name");
          if (StringUtils.isBlank(confName)) {
            confName = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "conf_name");
          }
          pub.setConfName(confName);
        }

        // 刷新成果记录
        rcmdSyncFlagMessageProducer.syncPsnPub(context.getCurrentUserId(), pub.getId(), 0);
        PsnRefreshUserInfo psnRefInfo = new PsnRefreshUserInfo(context.getCurrentUserId());
        psnRefInfo.setPub(1);
        // 保存刷新的成果
        PsnRefreshPubInfo psnRefPub = psnRefreshPubInfoDao.get(pub.getId());
        if (psnRefPub == null) {
          psnRefPub = new PsnRefreshPubInfo(pub.getId(), context.getCurrentUserId());
        }
        psnRefPub.setIsDel(0);
        psnRefPub.setStatus(0);
        psnRefreshPubInfoDao.save(psnRefPub);
        psnRefreshUserInfoDao.save(psnRefInfo);
        // 刷新基金标注记录
        String fundinfo = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "fundinfo");
        pubFundinfoService.syncPubFundinfo(pub.getId(), context.getCurrentUserId(), pub.getTypeId(), fundinfo);
      } else if (pub.getStatus() == 0 && pub.getArticleType().equals(PublicationArticleType.REFERENCE)) {
        // 保存成果期刊信息
        String jname = doc.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jname");
        String issn = doc.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jissn");
        if (StringUtils.isBlank(issn)) {
          issn = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "issn");
        }
        this.pubRefcJournalService.savePubRefcJournal(pub.getId(), context.getCurrentUserId(), issn, jname,
            pub.getJnlId(), pub.getPublishYear());

        if (pub.getTypeId().equals(PublicationTypeEnum.CONFERENCE_PAPER)) {
          String confName = doc.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "conf_name");
          if (StringUtils.isBlank(confName)) {
            confName = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "conf_name");
          }
          pub.setConfName(confName);
        }

        // 刷新文献信息
        rcmdSyncFlagMessageProducer.syncPsnRefc(context.getCurrentUserId(), pub.getId(), 0);
        // 冗余数据更新至sns
        PsnRefreshUserInfo psnRefInfo = new PsnRefreshUserInfo(context.getCurrentUserId());
        psnRefInfo.setRefc(1);
        psnRefreshUserInfoDao.save(psnRefInfo);
      }

      // 清除数据库的pub_error_fields,遍历xml中的pub_errors/error,把错误的字段名保存进pub_error_fields
      // savePubErrorFields(doc, context);

      // 更新成果统计数字
      if (pub.getArticleType() == PublicationArticleType.OUTPUT) {
        psnPubStatDao.incrTotalOutputs(pub.getPsnId(), 1);
        // 重构个人成果成果相关统计
        psnStatisticsRefreshMessageProducer.refreshPsnPubStatistics(context.getCurrentUserId());
      }
      // 存储XML数据
      String xml = doc.getXmlString();
      this.publicationXmlService.save(pub.getId(), xml);

      // 为小组导入录入成果.
      publicationGroup(context, pub);

      publicationDynMessage(context, pub);
      // if (pub.getStatus() == 0) {
      // TODO 2015-11-4 EH清除缓存
      // this.publicationStatisticsService
      // .clearAllPubStatistic(context.getCurrentUserId(),
      // pub.getArticleType());
      // }

      // 刷新XML中的基准库信息到表中，如果是推荐给别人的，或者认领的成果需要刷新到pub_pdwh表中
      if (XmlOperationEnum.SyncToSNS.equals(context.getCurrentAction())
          || XmlOperationEnum.PushFromIns.equals(context.getCurrentAction())
          || XmlOperationEnum.ImportPdwh.equals(context.getCurrentAction())) {
        this.publicationPdwhService.refreshPubPdwhToDB(pub.getId(), doc);
      }

      // 保存全文信息.
      // String permissionStr =
      // doc.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH,
      // "permission");
      // pub.setPermission(NumberUtils.toInt(permissionStr,
      // PubFulltextPermission.all));
      // this.savePubFulltext(pub);

      // 发送成果修改消息.
      pubModifySyncProducer.sendSyncMessage(pub, 0);
      // 最后再次更新PUB
      publicationDao.save(pub);
      return pub;
    } catch (BatchTaskException e) {
      logger.error("savePubCreate保存新添加成果出错 ", e);
      throw new ServiceException(e);
    } catch (ServiceException e) {
      logger.error("savePubCreate保存新添加成果出错 ", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void savePubKnow(Long pubId, Long psnId, int isOwner) throws ServiceException {
    try {
      Publication pub = this.publicationDao.get(pubId);
      int seq = this.getMatchPubAuthor(pubId, psnId);
      syncSavePubKnow(pub, isOwner, seq);
    } catch (Exception e) {
      logger.error("刷新成果合作者计算需要的数据出错 pubid=" + pubId, e);
      throw new ServiceException("刷新成果合作者计算需要的数据出错 pubid=" + pubId, e);
    }

  }

  // 得到authors以及他们的序列号
  @Override
  public Integer getMatchPubAuthor(Long pubId, Long psnId) throws ServiceException {
    Person currPsn = personManager.getPerson(psnId);
    List<PubMember> pubList = pubMemberDao.getPubMemberList(pubId);
    int seq = -1;
    if (pubList != null && currPsn != null) {
      // 获取选择的成员序号,在此通过id来匹配具有第一优先级
      for (PubMember member : pubList) {
        if (member.getPsnId() != null & currPsn.getPersonId() != null) {
          if (currPsn.getPersonId().intValue() == member.getPsnId().intValue()) {
            seq = member.getSeqNo().intValue();
            return seq;
          }
        }
      }
      // 获取第一个匹配的成员序号，通过名称来匹配，返回第一个匹配上的名字
      for (PubMember member : pubList) {
        if (!StringUtils.isBlank(member.getName()) && !StringUtils.isBlank(currPsn.getName())
            && currPsn.getName().equalsIgnoreCase(member.getName())) {
          seq = member.getSeqNo().intValue();
          return seq;
        } else {
          String firstName = currPsn.getFirstName();
          String lastName = currPsn.getLastName();
          if (StringUtils.isBlank(firstName)
              || StringUtils.isBlank(lastName) && StringUtils.isBlank(member.getName())) {
            continue;
          }
          String preF = firstName.substring(0, 1).toLowerCase();
          lastName = lastName.toLowerCase();
          // 尝试z lin 是否匹配上alen z lin或者 z alen lin
          int index = member.getName().toLowerCase().indexOf(preF);
          if (index > -1 && member.getName().substring(index).toLowerCase().endsWith(lastName)) {
            seq = member.getSeqNo().intValue();
            return seq;
          }
          index = member.getName().toLowerCase().lastIndexOf(preF);
          if (index > 0 && member.getName().substring(0, index).toLowerCase().startsWith(lastName)) {
            seq = member.getSeqNo().intValue();
            return seq;
          }
        }
      }
    }
    return seq;
  }

  private void syncSavePubKnow(Publication pub, int isOwner, int seq) throws ServiceException {
    if (pub == null) {
      return;
    }
    if (pub.getJid() != null) {
      PublicationJournal pj = this.pubJournalService.getPubJournal(pub.getId());
      if (pj != null && pj.getJnlId() != null) {
        pub.setJnlId(pj.getJnlId());
      }
    }
    this.publicationDao.savePubKnow(pub, isOwner, seq);
  }

  /**
   * 解析并保存成果查重数据.
   * 
   * @param doc
   * @param context
   * @param now
   * @param pub
   * @return
   */
  private PubDupFields parsePubDupFields(PubXmlDocument doc, Publication pub, Integer status) throws ServiceException {

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
    String doi = StringUtils
        .substring(StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "doi")), 0, 100);
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
    Long ownerId = pub.getPsnId();
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
    boolean canDup = false;
    if (status != null && 0 == status) {
      canDup = true;
    }
    return this.pubDupService.savePubDupFields(param, pubType, pubId, ownerId, pub.getArticleType(), canDup);
  }

  /**
   * 这里放的是成果录入和编辑时都需要保存更新的字段.
   * 
   * @param doc
   * @param context
   * @param now
   * @param pub
   */
  private void wrapPublicationSaveField(PubXmlDocument doc, PubXmlProcessContext context, Date now, Publication pub) {

    pub.setBriefDesc(
        StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc"), 0, 500));

    pub.setBriefDescEn(
        StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc_en"), 0, 500));

    pub.setFulltextFileid(doc.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "file_id"));
    Integer fulltextNodeId =
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "node_id"));
    pub.setFulltextNodeId(fulltextNodeId);
    pub.setFulltextExt(null);
    if (pub.getFulltextFileid() != null) {
      String fileExt = doc.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "file_ext");
      if (StringUtils.isNotBlank(fileExt)) {
        pub.setFulltextExt(StringUtils.substring(fileExt, 0, 30));
      }
    }
    pub.setFulltextUrl(
        StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "fulltext_url"), 0, 3000));
    pub.setIsiId(doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "isi_id"));
    pub.setSourceDbId(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "source_db_id")));
    pub.setDataValidate(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "is_valid")));
    pub.setRecordFrom(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "record_from")));
    pub.setEnTitle(
        StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_title_text"), 0, 250));
    pub.setEnTitleHash(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_title_hash")));
    pub.setFingerPrint(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "finger_print")));
    pub.setImpactFactors(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "impact_factors"));
    pub.setJid(IrisNumberUtils.createLong(doc.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jid")));
    pub.setJnlId(IrisNumberUtils.createLong(doc.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jnl_id")));
    pub.setPublishDay(
        IrisNumberUtils.monthDayToInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_day")));
    pub.setPublishMonth(
        IrisNumberUtils.monthDayToInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_month")));
    pub.setPublishYear(
        IrisNumberUtils.monthDayToInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_year")));
    pub.setRegionId(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "country_id")));

    // 引用次数
    Integer citeTimes =
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cite_times"));
    // if (citeTimes != null) {
    pub.setCitedTimes(citeTimes);
    pub.setCitedDate(now);
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
    pub.setCitedUrl(
        StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cited_url"), 0, 3000));
    pub.setZhTitle(
        StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title_text"), 0, 250));
    pub.setZhTitleHash(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title_hash")));
    pub.setUpdateDate(context.getPubSimple() == null ? now : context.getPubSimple().getUpdateDate());
    pub.setUpdatePsnId(context.getCurrentUserId());

    // liqinghua添加补充信息
    pub.setStartPage(
        StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "start_page"), 0, 50));
    pub.setEndPage(
        StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "end_page"), 0, 50));
    pub.setIssue(StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "issue"), 0, 20));
    pub.setOldPubId(IrisNumberUtils.createLong(doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "from_pub_id")));
    String isbn = StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "isbn"), 0, 40);
    if (StringUtils.isBlank(isbn)) {
      isbn = StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "isbn"), 0, 40);
    }
    pub.setIsbn(isbn);
    pub.setVolume(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "volume"));
    pub.setIssue(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "issue"));
    pub.setDoi(StringUtils.substring(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "doi"), 0, 100));
    pub.setArticleNo(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "article_number"));

  }

  /**
   * 成果的动态消息.
   * <p/>
   * 群组导入录入时成果以下不计入操作情况.
   * 
   * @param context
   * @param pub
   * @throws ServiceException
   */
  private void publicationDynMessage(PubXmlProcessContext context, Publication pub) throws ServiceException {
    if (context.getGroupId() == null) {
      if (pub.getStatus() != null && pub.getStatus() == 0 && pub.getArticleType() == PublicationArticleType.OUTPUT) {
        // 发送同步消息到单位
        snsPubTotalSyncProducer.sendSnsPubTotal(pub.getPsnId());
      }
    }
    if (context.getGroupId() != null) {
      GroupPsnNode groupPsnNode = groupPsnNodeDao.get(context.getGroupId());
      int nodeId = SecurityUtils.getCurrentUserNodeId();
      if (groupPsnNode != null) {
        nodeId = groupPsnNode.getNodeId();
      }
      // 收集群组资源更新数据
      TaskGroupResNotify groupRes = new TaskGroupResNotify(pub.getId(), context.getGroupId(),
          context.getCurrentUserId(), pub.getArticleType(), 0, new Date(), nodeId);
      if ("zh".equalsIgnoreCase(LocaleContextHolder.getLocale().getLanguage())) {
        groupRes.setLocale("zh");
      } else {
        groupRes.setLocale("en");
      }

      try {
        this.taskGroupResNotifyDao.save(groupRes);
      } catch (Exception e) {

        logger.error("资源群组关系保存异常！", e);
      }

    }
  }

  /**
   * 为小组导入录入成果.
   * 
   * @param context
   * @param pub
   * @throws ServiceException
   */
  private void publicationGroup(PubXmlProcessContext context, Publication pub) throws ServiceException {
    if (context.getGroupId() != null) {
      int dynFlag = 1;
      if ((context.getIsBatch() != null && "yes".equals(context.getIsBatch())) || context.isImport()) {
        dynFlag = 0;
      }
      publicationGroupService.addPublicationToGroup(context.getCurrentUserId(), pub.getId() + "",
          context.getGroupId() + "", context.getArticleType(), context.getGroupFolderId(), dynFlag);
    }
  }

  /**
   * 录入成果时保存成果、人员关系数据.
   * 
   * @param doc
   * @return
   * @throws ServiceException
   */
  @SuppressWarnings("unchecked")
  private List<Long> savePubMember(PubXmlDocument doc, PubXmlProcessContext context) throws ServiceException {

    try {
      Long pubId = context.getCurrentPubId();
      List<Node> ndList = doc.getPubMembers();
      // 保留的人员
      List<Long> remainPmIds = new ArrayList<Long>();
      if (CollectionUtils.isEmpty(ndList)) {
        doc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "author_names", "");
        doc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_author_names", "");
        // return remainPmIds;
      }

      // Set<Long> insSet = new HashSet<Long>();
      for (Node node : ndList) {

        Element em = (Element) node;

        PubMember pm = null;
        // 获取成员ID，如果存在则查找修改
        Long pmId = IrisNumberUtils.createLong(em.attributeValue("pm_id"));
        if (pmId != null) {
          pm = publicationDao.getPubMemberById(pmId);
          // 创建成员
          if (pm == null) {
            pm = new PubMember();
          }
        } else {
          // 创建成员
          pm = new PubMember();
        }

        pm.setAuthorPos(IrisNumberUtils.createInteger(em.attributeValue("author_pos")));
        pm.setEmail(StringUtils.substring(em.attributeValue("email"), 0, 50));
        pm.setInsCount(IrisNumberUtils.createInteger(em.attributeValue("ins_count")));
        String memberName = StringUtils.substring(em.attributeValue("member_psn_name"), 0, 61);
        pm.setName(memberName);
        Integer owner = IrisNumberUtils.createInteger(em.attributeValue("owner"));
        if (owner == null) {
          owner = 0;
        }
        pm.setOwner(owner);
        pm.setPubId(pubId);
        pm.setPsnId(IrisNumberUtils.createLong(em.attributeValue("member_psn_id")));
        pm.setSeqNo(IrisNumberUtils.createInteger(em.attributeValue("seq_no")));
        pm.setFirstAuthor(IrisNumberUtils.createInteger(em.attributeValue("first_author")));
        // // 单位、成果数据
        // for (int i = 1; i < 6; i++) {
        // Long insId =
        // IrisNumberUtils.createLong(em.attributeValue("ins_id" + i));
        // if (insId != null && insId > 0)
        // insSet.add(insId);
        // }
        // 保存数据
        publicationDao.savePubMember(pm);
        // 成员ID写入XML
        em.addAttribute("pm_id", pm.getId().toString());
        remainPmIds.add(pm.getId());
      }
      // scholarSNS端不需要成果单位关系
      // 保存单位成果关系数据
      // savePubIns(insSet, pubId, context.getCurrentUserId());
      this.publicationDao.removeOtherPubmember(remainPmIds, pubId);
      return remainPmIds;

    } catch (Exception e) {
      logger.error("savePubMember保存成员出错 ", e);
      throw new ServiceException(e);
    }

  }

  /**
   * 保存成果错误信息.
   * 
   * @param doc
   * @return
   * @throws ServiceException
   */
  @SuppressWarnings("unchecked")
  public List<PubErrorFields> savePubErrorFields(PubXmlDocument doc, PubXmlProcessContext context)
      throws ServiceException {

    Long pubId = doc.getPubId();
    try {
      if (XmlOperationEnum.Edit.equals(context.getCurrentAction()))
      // 删除原有错误检查信息
      {
        publicationDao.removePubErrorFieldsByPubId(pubId);
      }

      List<Node> ndList = doc.getPubErrorFields();
      if (ndList == null || ndList.size() == 0) {
        return null;
      }
      List<PubErrorFields> errorList = new ArrayList<PubErrorFields>();
      for (Node node : ndList) {
        Element em = (Element) node;
        // 创建错误实体
        PubErrorFields error = new PubErrorFields();
        error.setCreateAt(new Date());
        error.setErrorNo(IrisNumberUtils.createInteger(em.attributeValue("error_no")));
        error.setName(em.attributeValue("field"));
        error.setPubId(pubId);
        // 保存
        publicationDao.savePubErrorFields(error);
        errorList.add(error);
      }
      return errorList;

    } catch (DaoException e) {
      logger.error("savePubErrorFields保存成果错误信息出错 ", e);
      throw new ServiceException(e);
    }

  }

  /**
   * 保存成果编辑内容.
   * 
   * @param pub
   * @throws ServcieException
   */
  @Override
  public Publication savePubEdit(PubXmlDocument doc, PubXmlProcessContext context) throws ServiceException {

    try {
      Long pubId = doc.getPubId();
      Publication pub = publicationDao.get(pubId);
      if (pub != null) {
        // SCM-15482 修改成果时，不要更新各表的cite_date字段
        Date now = null;
        if (pub.getCitedDate() != null) {
          now = pub.getCitedDate();
        } else {
          now = new Date();
        }
        // 之前的成果类型
        int preTypeId = pub.getTypeId();
        pub.setTypeId(doc.getPubTypeId());
        pub.setVersionNo(pub.getVersionNo() + 1);
        if (context.getCurrentPubStatus() != null) {
          pub.setStatus(context.getCurrentPubStatus());
        }

        // 在保存成员与成果关系时保证取到pubId
        context.setCurrentPubId(pubId);

        doc.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "version_no", pub.getVersionNo().toString());

        this.checkedUpdateSource(doc, pub);

        this.wrapPublicationSaveField(doc, context, now, pub);
        // 保存成果查重字段
        this.parsePubDupFields(doc, pub, pub.getStatus());
        // 建立成果、成员关系数据
        // this.deletePubMember(doc, context);
        savePubMember(doc, context);
        if (context.getPubSimple().getUpdateMark() != null) {
          pub.setUpdateMark(context.getPubSimple().getUpdateMark());
        }
        pub.setAuthorNames(context.getPubSimple().getAuthorNames());
        PublicationList publist = null;
        // 解析收录情况
        // 2015-11-3 在第一阶段已完成
        // if (context.isImport() || context.isSyncPub()) {
        // // 成果导入时pubList拆分（包括原始引用情况）
        // publist = this.pubListServiceProxy.praseSourcePubList(doc);
        // } else {
        // // 解析收录情况
        // publist = this.pubListServiceProxy.prasePubList(doc);
        // }
        publist = publicationListDao.get(pub.getId());
        // 设置CitedList
        setPubCitedList(pub, publist, doc);
        publicationDao.save(pub);

        if (pub.getStatus() == 0 && pub.getArticleType().equals(PublicationArticleType.OUTPUT)) {
          doc.getXmlString();
          String startPage = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "start_page");
          String endPage = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "end_page");
          String articleNo = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "article_number");
          if ("".equals(startPage) && "".equals(endPage) && StringUtils.isNotBlank(articleNo)) {
            doc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "not_number", Integer.toString(1));
          }

          String zhKeywords = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_keywords");
          String enKeywords = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_keywords");
          // 该方法已废弃 zk 个人特征关键词
          // 同步成果关键词到基准库
          // this.psnKeywordService.syncPsnPubKeywords(context.getCurrentUserId(),
          // pub, publist, zhKeywords,
          // enKeywords);
          // 保存拆分关键词
          this.pubKeyWordsService.savePubKeywords(pub.getId(), context.getCurrentUserId(), zhKeywords, enKeywords);

          if (pub.getTypeId().equals(PublicationTypeEnum.JOURNAL_ARTICLE)) {
            // 保存成果期刊信息
            String jname = doc.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jname");
            String issn = doc.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jissn");
            if (StringUtils.isBlank(issn)) {
              issn = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "issn");
            }
            this.pubJournalService.savePubJournal(pub.getId(), context.getCurrentUserId(), issn, jname, pub.getJnlId(),
                pub.getPublishYear());
            pub.setIssn(issn);
          }

          if (pub.getTypeId().equals(PublicationTypeEnum.CONFERENCE_PAPER)) {
            String confName = doc.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "conf_name");
            if (StringUtils.isBlank(confName)) {
              confName = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "conf_name");
            }
            pub.setConfName(confName);
          }

          // 刷新成果记录
          rcmdSyncFlagMessageProducer.syncPsnPub(context.getCurrentUserId(), pub.getId(), 0);
          PsnRefreshUserInfo psnRefInfo = new PsnRefreshUserInfo(context.getCurrentUserId());
          psnRefInfo.setPub(1);
          // 保存刷新的成果
          PsnRefreshPubInfo psnRefPub = psnRefreshPubInfoDao.get(pub.getId());
          if (psnRefPub == null) {
            psnRefPub = new PsnRefreshPubInfo(pub.getId(), context.getCurrentUserId());
          }
          psnRefPub.setIsDel(0);
          psnRefPub.setStatus(0);
          psnRefreshPubInfoDao.save(psnRefPub);
          psnRefreshUserInfoDao.save(psnRefInfo);

          // 刷新基金标注记录
          String fundinfo = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "fundinfo");
          pubFundinfoService.syncPubFundinfo(pub.getId(), context.getCurrentUserId(), pub.getTypeId(), fundinfo);
        } else if (pub.getStatus() == 0 && pub.getArticleType().equals(PublicationArticleType.REFERENCE)) {
          // 保存成果期刊信息
          String jname = doc.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jname");
          String issn = doc.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jissn");
          if (StringUtils.isBlank(issn)) {
            issn = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "issn");
          }
          this.pubRefcJournalService.savePubRefcJournal(pub.getId(), context.getCurrentUserId(), issn, jname,
              pub.getJnlId(), pub.getPublishYear());

          if (pub.getTypeId().equals(PublicationTypeEnum.CONFERENCE_PAPER)) {
            String confName = doc.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "conf_name");
            if (StringUtils.isBlank(confName)) {
              confName = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "conf_name");
            }
            pub.setConfName(confName);
          }

          // 刷新文献信息
          rcmdSyncFlagMessageProducer.syncPsnRefc(context.getCurrentUserId(), pub.getId(), 0);
          // 冗余数据更新至sns
          PsnRefreshUserInfo psnRefInfo = new PsnRefreshUserInfo(context.getCurrentUserId());
          psnRefInfo.setRefc(1);
          psnRefreshUserInfoDao.save(psnRefInfo);
        }
        // 清除数据库的pub_error_fields,遍历xml中的pub_errors/error,把错误的字段名保存进pub_error_fields
        // savePubErrorFields(doc, context);
        // 同步到群组节点
        this.publicationGroupSyncMessage(doc, pub, context, "edit");
        // if (pub.getStatus() == 0) {
        // TODO 2015-11-4 EH清除缓存
        // this.publicationStatisticsService.clearAllPubStatistic(context.getCurrentUserId(),
        // pub.getArticleType());
        // }

        // 保存全文信息.
        // 保存全文信息.
        // String permissionStr =
        // doc.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH,
        // "permission");
        // pub.setPermission(NumberUtils.toInt(permissionStr,
        // PubFulltextPermission.all));
        // this.savePubFulltext(pub);

        Integer isDel = 0;
        // 文献期刊类型，标记需要同步到单位统计期刊
        if ((PublicationTypeEnum.JOURNAL_ARTICLE != pub.getTypeId()
            || PublicationTypeEnum.JOURNAL_ARTICLE == preTypeId)) {
          // 之前是期刊类型，后来换了其他类型，需要标记删除
          isDel = 1;
        }
        pubModifySyncProducer.sendSyncMessage(pub, isDel);
        if (pub.getArticleType() == PublicationArticleType.OUTPUT) {
          // 重构个人成果成果相关统计
          psnStatisticsRefreshMessageProducer.refreshPsnPubStatistics(context.getCurrentUserId());
        }
      } else {

        throw new ServiceException("updatePublication该成果不存在pubId:" + pubId);
      }
      // 存储XML数据
      String xml = doc.getXmlString();
      this.publicationXmlService.save(pubId, xml);
      return pub;
    } catch (Exception e) {
      logger.error("savePubEdit保存成果编辑内容出错 ", e);
      throw new ServiceException(e);
    }
  }

  /**
   * // 设置CitedList.
   * 
   * @param pub
   * @param publist
   */
  private void setPubCitedList(Publication pub, PublicationList publist, PubXmlDocument doc) {
    String citedList = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cited_list");
    // 收录情况冗余字段
    if (publist != null) {
      if ((publist.getListIstp() != null && publist.getListIstp() > 0)
          || (publist.getListSci() != null && publist.getListSci() > 0)
          || (publist.getListSsci() != null && publist.getListSsci() > 0)) {
        pub.setCitedList("ISI");
      }
    }
    if (StringUtils.isNotBlank(citedList) && "ISI".equalsIgnoreCase(citedList.trim())) {
      pub.setCitedList("ISI");
    }
    if (StringUtils.isNotBlank(pub.getCitedList()) && !"ISI".equalsIgnoreCase(pub.getCitedList())) {
      pub.setCitedList("");
    }
  }

  private void checkedUpdateSource(PubXmlDocument doc, Publication pub) {
    try {
      if (pub.getSourceDbId() != null) {
        PublicationList pubList = publicationListDao.get(pub.getId());
        if (pubList == null) {
          return;
        }
        boolean flag = matchPubList(pubList, doc);
        String citeTimes = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cite_times");
        if (flag) {
          String jId = doc.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jid");
          String zhTitleHash = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title_hash");
          String enTitleHash = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_title_hash");
          int citeTimesInt = StringUtils.isBlank(citeTimes) ? 0 : Integer.parseInt(citeTimes);
          Long jIdInt = StringUtils.isBlank(jId) ? 0 : Long.parseLong(jId);
          Long zhTitleHashInt = StringUtils.isBlank(zhTitleHash) ? 0 : Long.parseLong(zhTitleHash);
          Long enTitleHashInt = StringUtils.isBlank(enTitleHash) ? 0 : Long.parseLong(enTitleHash);
          int sourceCiteTimes = pub.getCitedTimes() == null ? 0 : pub.getCitedTimes().intValue();
          Long sourceJid = pub.getJid() == null ? 0 : pub.getJid();
          int sourceZhTitleHash = pub.getZhTitleHash() == null ? 0 : pub.getZhTitleHash().intValue();
          int sourceEnTitleHash = pub.getEnTitleHash() == null ? 0 : pub.getEnTitleHash().intValue();
          if (citeTimesInt != sourceCiteTimes) {
            flag = false;
          }
          if (!jIdInt.equals(sourceJid)) {
            flag = false;
          }
          if (zhTitleHashInt.intValue() != sourceZhTitleHash) {
            flag = false;
          }
          if (enTitleHashInt.intValue() != sourceEnTitleHash) {
            flag = false;
          }
        }
        if (!flag) {
          pub.setIsUpdateSource(1);
          Node pubNode = doc.getNode(PubXmlConstants.PUBLICATION_XPATH);
          Element el = (Element) pubNode;
          el.addAttribute("update_source", "1");
          if (StringUtils.isBlank(citeTimes)) {
            pub.setCitedTimes(0);
          }
        }
      }
    } catch (Exception e) {
      logger.error("检查成果编辑是否修改了标题，期刊，收录，引用字段出错,xml:{} ", doc.getXmlString(), e);
    }
  }

  private boolean matchPubList(PublicationList pubList, PubXmlDocument doc) {
    String sci = doc.getXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_sci");
    String istp = doc.getXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_istp");
    String ssci = doc.getXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ssci");
    String ei = doc.getXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ei");
    int sciInt = StringUtils.isBlank(sci) ? 0 : Integer.parseInt(sci);
    int istpInt = StringUtils.isBlank(istp) ? 0 : Integer.parseInt(istp);
    int ssciInt = StringUtils.isBlank(ssci) ? 0 : Integer.parseInt(ssci);
    int eiInt = StringUtils.isBlank(ei) ? 0 : Integer.parseInt(ei);
    int sourceSci = pubList.getListSci() == null ? 0 : pubList.getListSci().intValue();
    int sourceIstp = pubList.getListIstp() == null ? 0 : pubList.getListIstp().intValue();
    int sourceSsci = pubList.getListSsci() == null ? 0 : pubList.getListSsci().intValue();
    int sourceEi = pubList.getListEi() == null ? 0 : pubList.getListEi().intValue();
    boolean flag = true;
    if (sciInt != sourceSci) {
      flag = false;
    }
    if (istpInt != sourceIstp) {
      flag = false;
    }
    if (ssciInt != sourceSsci) {
      flag = false;
    }
    if (eiInt != sourceEi) {
      flag = false;
    }
    return flag;
  }

  /**
   * 新增成果、编辑成果同步到小组.
   * <p/>
   * action='del' 删除.
   * <p/>
   * action="add" 新增.
   * <p/>
   * action="edit" 编辑.
   * 
   * @param doc
   * @param pub
   * @param context
   * @param action
   * @throws ServiceException
   */
  private void publicationGroupSyncMessage(PubXmlDocument doc, Publication pub, PubXmlProcessContext context,
      String action) throws ServiceException {
    if ("del".equals(action)) {
      this.publicationGroupService.delPublication(pub.getId(), pub.getPsnId());
    }
    if ("edit".equals(action)) {
      this.publicationGroupService.savePublicationEdit(pub, context);
    }

  }

  @Override
  public String getPubXmlById(Long pubId) throws ServiceException {
    PublicationForm form = new PublicationForm();
    form.setPubId(pubId);
    try {
      form = this.scholarPublicationXmlManager.loadXml(form);
    } catch (PublicationNotFoundException e) {
      logger.error("没有找到pubId为" + form.getPubId() + "的xml数据");
    }
    return form.getPubXml();
  }

  @Override
  public void updatePublication(String xml, PubXmlProcessContext context) throws ServiceException {
    PubXmlDocument doc;
    try {
      doc = new PubXmlDocument(xml);
      this.publicationXmlService.save(context.getCurrentPubId(), xml);
      this.savePubEdit(doc, context);
    } catch (Exception e) {
      logger.error("xml格式有错" + xml);
    }
  }

  @Override
  public Long getTotalPubsByPsnId(Long psnId) throws ServiceException {
    try {
      return this.publicationDao.getTotalPubsByPsnId(psnId);

    } catch (Exception e) {
      logger.error("获取成果总数", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Publication getPublicationById(Long pubId) throws ServiceException {
    try {
      return this.publicationDao.findPublicationByPubId(pubId);
    } catch (DaoException e) {
      logger.error("查找成果ID为：" + pubId + "的成果失败" + e);
    }
    return null;
  }

  @Override
  public Publication getPubByOldPub(Long oldPubId, Long psnId) throws ServiceException {
    try {
      return this.publicationDao.getPubByOldPub(oldPubId, psnId);
    } catch (Exception e) {
      logger.error("获取同步数据的新成果.", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void isSyncPublicationToFinalReport(PublicationForm loadXml) throws ServiceException {
    if ("finalReport".equals(loadXml.getFrom())) {
      this.pubFinalReportService.syncPublicationToFinalReport(loadXml);
    } else if ("reschReport".equalsIgnoreCase(loadXml.getFrom())) {
      this.pubResearchRptService.syncPublicationToFinalReport(loadXml);
    }
  }

  @Override
  public void isSyncPublicationToExpertPub(PublicationForm loadXml) throws ServiceException {
    if ("expertpub".equalsIgnoreCase(loadXml.getFrom())) {

      expertPubService.syncPublicationToExpertuPub(loadXml);

    }

  }

  @Override
  public void isSyncPublicationToLabPub(PublicationForm loadXml) throws ServiceException {
    if ("labpub".equalsIgnoreCase(loadXml.getFrom())) {

      labPubService.syncPublicationToLabPub(loadXml);

    }

  }

  /**
   * 获取成果.
   */
  @Override
  public Publication getPub(Long pubId) throws ServiceException {
    return this.publicationDao.get(pubId);
  }

  @Override
  public Integer queryPubTotalByPubIdAndType(String pubIds, String type) throws ServiceException {
    try {
      return this.publicationDao.queryPubTotalByPubIdAndType(pubIds, type);
    } catch (DaoException e) {
      logger.error("计算在指定的成果中某个成果类型的个数出现异常", e);
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public List<Publication> findPubByPnsId(Long psnId) throws ServiceException {
    return this.publicationDao.findPubByPnsId(psnId);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Map<String, Object>> matchPubPsnIds(Publication pub) throws ServiceException {
    List<Map<String, Object>> list1 = this.publicationDao.matchPubPsnIds(pub);
    List<Map<String, Object>> list2 = this.publicationDao.matchPubConfimPsnIds(pub);
    if (CollectionUtils.isEmpty(list1)) {
      return list2;
    }
    if (CollectionUtils.isEmpty(list2)) {
      return list1;
    }
    return (List<Map<String, Object>>) CollectionUtils.union(list1, list2);
  }

  @Override
  public List<Map<String, Object>> matchRefPsnIds(Publication pub) throws ServiceException {
    return this.publicationDao.matchRefPsnIds(pub);
  }

  @Override
  public List<Publication> findPubsByIds(List<Long> pubIds) throws ServiceException {
    List<Publication> pubs = new ArrayList<Publication>();
    for (Long pubId : pubIds) {
      pubs.add(this.publicationDao.get(pubId));
    }
    return pubs;
  }

  @Override
  public List<PublicationList> getPubListByBatchForSourceDbId2(Long lastId, int batchSize) throws ServiceException {
    return this.publicationDao.getPubListByBatchForSourceDbId2(lastId, batchSize);
  }

  @Override
  public void rebuildPubXmlByPubSourceDbId2(PublicationList pubList) throws ServiceException {
    try {
      PublicationXml pubXml = this.publicationXmlService.getById(pubList.getId());
      String xml = pubXml.getXmlData();
      if (StringUtils.isBlank(xml)) {
        return;
      }
      PubXmlDocument doc = new PubXmlDocument(xml);
      if (!doc.existsNode(PubXmlConstants.PUB_LIST_XPATH)) {
        doc.createElement(PubXmlConstants.PUB_LIST_XPATH);
      }
      String listSCI = pubList.getListSci() == null ? "0" : String.valueOf(pubList.getListSci());
      String listISTP = pubList.getListIstp() == null ? "0" : String.valueOf(pubList.getListIstp());
      String listSSCI = pubList.getListSsci() == null ? "0" : String.valueOf(pubList.getListSsci());
      doc.setNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_sci", listSCI);
      doc.setNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_istp", listISTP);
      doc.setNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ssci", listSSCI);
      this.publicationXmlService.save(pubList.getId(), doc.getXmlString());
    } catch (Exception e) {
      logger.error("pubId={},重构publication表sourceDbId=2的isi数据出错", pubList.getId(), e);
    }
  }

  @Override
  public void rebuildPubFulltext(Publication pub) throws ServiceException {
    try {
      PublicationXml pubXml = this.publicationXmlService.getById(pub.getId());
      String xml = pubXml.getXmlData();
      if (StringUtils.isBlank(xml)) {
        return;
      }
      PubXmlDocument doc = new PubXmlDocument(xml);
      if (doc.existsNode(PubXmlConstants.PUB_FULLTEXT_XPATH)) {
        doc.setNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "permission", "1");
        this.publicationXmlService.save(pub.getId(), doc.getXmlString());
      }
    } catch (Exception e) {
      logger.error("pubId={},重构成果xmlfulltext权限出错", pub.getId(), e);
    }
  }

  @Override
  public int pubMatchName(Long tmPsnId, String zhName, String likeName) throws ServiceException {
    return publicationDao.pubMatchName(tmPsnId, zhName, likeName);
  }

  @Override
  public List findPubIdsBatch(Long lastId, int batchSize) throws ServiceException {
    try {
      return publicationDao.findPubIdsBatch(lastId, batchSize);
    } catch (DaoException e) {
      logger.error("批量查询成果信息出错，lastId=" + lastId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Publication getPubOwnerPsnIdOrStatus(Long pubId) throws ServiceException {
    return publicationDao.getPubOwnerPsnIdOrStatus(pubId);
  }

  /**
   * 
   * @author liangguokeng
   */
  @Override
  public List<Publication> findPubIdsByPsnId(Long psnId) throws ServiceException {
    try {
      return publicationDao.findPubIdsByPsnId(psnId);
    } catch (DaoException e) {
      logger.error("后台任务，查询成果Id出错", e);
      e.printStackTrace();
    }
    return null;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public List<Long> findRebuildPubId(int size) throws ServiceException {
    List<Long> idsList = new ArrayList<Long>();
    try {
      List list = publicationDao.findRebuildPubId(size);
      if (CollectionUtils.isNotEmpty(list)) {
        for (Object obj : list) {
          Map map = (Map) obj;
          idsList.add(NumberUtils.toLong(ObjectUtils.toString(map.get("PUB_ID"))));
        }
      }
    } catch (Exception e) {
      logger.error("获取重构成果author_names字段出错", e);
    }
    return idsList;
  }

  @Override
  public void rebuildSnsPubAuthorNames(Long pubId) throws ServiceException {
    try {
      PublicationXml pubXml = this.publicationXmlService.getById(pubId);
      String xml = pubXml.getXmlData();
      if (StringUtils.isBlank(xml)) {
        return;
      }
      Map<String, String> authorNames = this.buildPubAuthorNames(pubId);
      String allNames = authorNames.get("all_names");
      String briefNames = authorNames.get("brief_names");
      if (StringUtils.isBlank(allNames)) {
        return;
      }
      PubXmlDocument doc = new PubXmlDocument(xml);
      doc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "author_names", allNames);
      doc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_author_names", briefNames);
      this.publicationXmlService.save(pubId, doc.getXmlString());
      // 同步更新冗余
      briefNames = IrisStringUtils.filterSupplementaryChars(briefNames);
      briefNames = StringUtils.substring(briefNames, 0, 200);
      publicationDao.updatePubAuthorNames(pubId, briefNames);
      groupPubDao.updatePubAuthorNames(pubId, briefNames);
    } catch (Exception e) {
      logger.error("pubId={},重构成果xml的author_names出错", pubId, e);
    }

  }

  @Override
  public void updateTaskPubAuthor(Long pubId) throws ServiceException {
    publicationDao.updateTaskPubAuthor(pubId);
  }

  @Override
  public Map<String, String> updatePubXmlFulltext(Long pubId, Map<String, Object> params, boolean dynamic)
      throws ServiceException {
    Map<String, String> map = new HashMap<String, String>();
    try {
      PublicationXml pubXml = this.publicationXmlService.getById(pubId);
      String xml = pubXml.getXmlData();
      if (StringUtils.isBlank(xml)) {
        map.put("fulltextIcon", "");
        map.put("downUrl", "");
        return map;
      }
      String nodeId = ObjectUtils.toString(((String[]) params.get("node_id"))[0]);
      String fileId = ObjectUtils.toString(((String[]) params.get("file_id"))[0]);
      String fileName = ObjectUtils.toString(((String[]) params.get("file_name"))[0]);
      String fileExt = ObjectUtils.toString(((String[]) params.get("file_ext"))[0]);
      String fileDesc = ObjectUtils.toString(((String[]) params.get("file_desc"))[0]);
      String fileDate = ObjectUtils.toString(((String[]) params.get("upload_date"))[0]);
      String permission = ObjectUtils.toString(((String[]) params.get("permission"))[0]);
      String insId = ObjectUtils.toString(SecurityUtils.getCurrentInsId());

      PubXmlDocument xmlDocument = new PubXmlDocument(xml);
      Element fullText = (Element) xmlDocument.getNode(PubXmlConstants.PUB_FULLTEXT_XPATH);
      if (fullText == null) {
        xmlDocument.createElement(PubXmlConstants.PUB_FULLTEXT_XPATH);
      }
      // 默认好友可见
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "permission", permission);
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "node_id", nodeId);
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "file_id", fileId);
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "file_name", fileName);
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "file_ext", fileExt);
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "file_desc", fileDesc);
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "upload_date", fileDate);
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "ins_id", insId);

      this.publicationXmlService.save(pubId, xmlDocument.getXmlString());
      Publication pub = publicationDao.get(pubId);
      pub.setFulltextNodeId(NumberUtils.toInt(nodeId));
      pub.setFulltextFileid(fileId);
      pub.setFulltextExt(StringUtils.substring(fileExt, 0, 30));
      pub.setPermission(NumberUtils.toInt(permission, PubFulltextPermission.all));

      String fulltextIcon = ArchiveFileUtil.getFileTypeIco("/resmod", fileExt, LocaleContextHolder.getLocale());

      String url = domainscm + "/scmwebsns";
      String link =
          ArchiveFileUtil.generateDownloadLink(url, NumberUtils.toLong(fileId), NumberUtils.toInt(nodeId), null);

      map.put("fulltextIcon", StringUtils.trimToEmpty(fulltextIcon));
      map.put("downUrl", StringUtils.trimToEmpty(link));

      // 保存全文信息.
      this.savePubFulltext(pub);
      if (pub.getArticleType() == PublicationArticleType.OUTPUT) {
        // 刷新成果记录
        rcmdSyncFlagMessageProducer.syncPsnPub(pub.getPsnId(), pub.getId(), 0);
        PsnRefreshUserInfo psnRefInfo = new PsnRefreshUserInfo(pub.getPsnId());
        psnRefInfo.setPub(1);
        // 保存刷新的成果
        PsnRefreshPubInfo psnRefPub = psnRefreshPubInfoDao.get(pub.getId());
        if (psnRefPub == null) {
          psnRefPub = new PsnRefreshPubInfo(pub.getId(), pub.getPsnId());
        }
        psnRefPub.setIsDel(0);
        psnRefPub.setStatus(0);
        psnRefreshPubInfoDao.save(psnRefPub);
        psnRefreshUserInfoDao.save(psnRefInfo);
      } else if (pub.getArticleType() == PublicationArticleType.REFERENCE) {
        // 刷新文献信息
        rcmdSyncFlagMessageProducer.syncPsnRefc(pub.getPsnId(), pub.getId(), 0);
        // 冗余数据更新至sns
        PsnRefreshUserInfo psnRefInfo = new PsnRefreshUserInfo(pub.getPsnId());
        psnRefInfo.setRefc(1);
        psnRefreshUserInfoDao.save(psnRefInfo);
      }

      if (dynamic) {
        // 上传全文动态.
        JSONArray resDetails = new JSONArray();
        JSONObject resJsonObject = new JSONObject();
        resJsonObject.accumulate("resNode", nodeId);
        resJsonObject.accumulate("resId", pubId);
        resDetails.add(resJsonObject);
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("resType", pub.getArticleType());
        jsonObject.accumulate("sameFlag",
            DynamicConstant.RES_TYPE_PUB + "_" + nodeId + "_" + UUID.randomUUID().toString().replace("-", ""));

        String pubPermission = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "authority");
        int pubPermissionInt = 0;
        if (StringUtils.isNotBlank(pubPermission)) {
          pubPermissionInt = Integer.parseInt(pubPermission);
          switch (pubPermissionInt) {
            case 7:
              pubPermissionInt = 0;// 所有人可见
              break;
            case 6:
              pubPermissionInt = 1;// 好友可见
              break;
            case 4:
              pubPermissionInt = 2;// 本人可见
              break;
          }
        }
        jsonObject.accumulate("permission", pubPermissionInt);// 成果的权限
        jsonObject.accumulate("resDetails", resDetails.toString());
        dynamicProduceUploadFTPubService.produceDynamic(jsonObject.toString());
      }
    } catch (Exception e) {
      map.put("fulltextIcon", "");
      map.put("downUrl", "");
      logger.error("pubId={},列表上传fulltext更新成果及xml的出错", pubId, e);
    }
    return map;
  }

  @Override
  public List<PubMember> getPubMembersByPubId(Long pubId) throws ServiceException {
    try {
      return publicationDao.getPubMembersByPubId(pubId);
    } catch (DaoException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public Map<String, String> getPubPartDetail(Long pubId) throws ServiceException {
    Map<String, String> map = new HashMap<String, String>();
    String pubXml = this.getPubXmlById(pubId);
    try {
      PubXmlDocument doc = new PubXmlDocument(pubXml);
      String zh_Title = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title");
      String en_Title = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_title");
      String zh_abstract = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_abstract");
      String en_abstract = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_abstract");
      String zh_keywords = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_keywords");
      String en_keywords = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_keywords");
      map.put("zhTitle", XmlUtil.trimAllHtml(zh_Title));
      map.put("enTitle", XmlUtil.trimAllHtml(en_Title));
      map.put("zhAbstract", XmlUtil.trimAllHtml(zh_abstract));
      map.put("enAbstract", XmlUtil.trimAllHtml(en_abstract));
      map.put("zhKeywords", XmlUtil.trimAllHtml(zh_keywords));
      map.put("enKeywords", XmlUtil.trimAllHtml(en_keywords));
      return map;
    } catch (DocumentException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public String getPublicationTitle(Long pubId) throws ServiceException {
    try {
      String title = null;
      Publication publication = publicationDao.get(pubId);
      if (publication != null) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        if ("zh".equals(lang)) {
          title = StringUtils.isBlank(publication.getZhTitle()) ? publication.getEnTitle() : publication.getZhTitle();
        } else {
          title = StringUtils.isBlank(publication.getEnTitle()) ? publication.getZhTitle() : publication.getEnTitle();
        }
      }

      return title;
    } catch (Exception e) {
      logger.error("获取成果标题出现异常：", e);
      throw new ServiceException();
    }
  }

  @Override
  public boolean isCurrPsnPub(Long pubId) throws ServiceException {
    return publicationDao.isCurrPsnPub(pubId, SecurityUtils.getCurrentUserId());
  }

  @Override
  public List<Publication> getPubListByPubIds(List<Long> pubIds) throws ServiceException {
    if (pubIds.isEmpty()) {
      return null;
    }
    String sPubIds = "";
    if (CollectionUtils.isNotEmpty(pubIds)) {
      for (Long pubId : pubIds) {
        if ("".equals(sPubIds)) {
          sPubIds = String.valueOf(pubId);
        } else {
          sPubIds += "," + String.valueOf(pubId);
        }
      }
    }
    try {
      return this.publicationDao.getPublicationByPubIds(sPubIds);
    } catch (DaoException e) {
      logger.error("成果取出出错", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Map<String, Object>> findPdwhPubLinkeScmPubs(Long pubId, int dbid) throws ServiceException {
    try {
      List<Publication> pubList = this.publicationDao.findPdwhPubLinkeScmPubs(pubId, dbid);
      if (CollectionUtils.isEmpty(pubList)) {
        return null;
      }
      List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
      for (Publication pub : pubList) {
        myPublicationQueryService.wrapQueryResultTypeName(pub);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pubId", pub.getId());
        map.put("des3PsnId", ServiceUtil.encodeToDes3(ObjectUtils.toString(pub.getPsnId())));
        Person psn = personManager.getAvatarsForEdit(pub.getPsnId());
        map.put("avatars", psn == null ? "" : psn.getAvatars());
        map.put("htmlAbstract",
            publicationWrapService.buildHtmlAbstract(pub, LocaleContextHolder.getLocale(), false, null, null));
        mapList.add(map);
      }
      return mapList;
    } catch (Exception e) {
      logger.error("", e);
    }
    return null;
  }

  @Override
  public List<Publication> getPdwhPubs(List<Map<String, Object>> pdwhPuballKey) throws ServiceException {
    List<Publication> pubList = new ArrayList<Publication>();
    try {
      for (Map<String, Object> map : pdwhPuballKey) {
        Long pubId = NumberUtils.toLong(ObjectUtils.toString(map.get("pubId")));
        Integer dbid = NumberUtils.toInt(ObjectUtils.toString(map.get("dbid")));
        PdwhPublicationAll puball = publicationXmlPdwhService.getPdwhPublicationAll(pubId, dbid);
        Publication pub = new Publication();
        pub.setId(pubId);
        pub.setTypeId(puball.getTypeId());
        pub.setZhTitle(puball.getZhTitle());
        pub.setEnTitle(puball.getEnTitle());
        pub.setAuthorNames(puball.getAuthorNames());
        pub.setBriefDesc(puball.getBriefDescZh());
        pub.setBriefDescEn(puball.getBriefDescZh());
        pub.setPublishYear(puball.getPublishYear());
        pub.setPublishMonth(puball.getPublishMonth());
        pub.setCitedTimes(puball.getCitedTimes());
        // pub.setTypeName(publicationTypeService.queryResultTypeName(puball.getTypeId()));
        pub.setFulltextUrl(puball.getFulltextUrl());
        pub.setDbid(dbid);
        pub.setDes3ResRecId(ObjectUtils.toString(map.get("resRecId"))); // 导入待导入好友分享成果到我的成果库时需要用到
        pub.setHtmlAbstract(publicationWrapService.buildHtmlAbstract(puball));
        // myPublicationQueryService.wrapQueryResultTypeName(pub);
        pubList.add(pub);
      }
    } catch (Exception e) {
      logger.error("", e);
    }
    return pubList;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Map> getLastMonthPsnCitedTimes(Integer size) throws ServiceException {
    try {
      return publicationDao.getLastMonthCitedTimes(size);
    } catch (DaoException e) {
      logger.error("批量获取成果数时出错", e.getMessage());
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Map> getLastMonthPsnPubs(Integer size) throws ServiceException {
    try {
      return publicationDao.getLastMonthPsnPubs(size);
    } catch (DaoException e) {
      logger.error("批量获取成果数时出错", e.getMessage());
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> getAllPsnIdBySize(Integer size) throws ServiceException {
    try {
      return publicationDao.getPsnId(size);
    } catch (DaoException e) {
      logger.error("批量获取成果人员id时出错", e.getMessage());
      throw new ServiceException(e);
    }
  }

  @Override
  public boolean checkPubIsDeleteByPubId(Long pubId) throws ServiceException {
    try {
      Publication pub = publicationDao.get(pubId);
      if (pub == null || pub.getStatus() == 1) {
        return true;
      }
      return false;
    } catch (Exception e) {
      logger.error("判断成果是否删除出现异常：", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public String getSharePubContent(Long pubId) {
    Map<String, String> map = new HashMap<String, String>();
    try {
      String pubXml = this.getPubXmlById(pubId);
      if (StringUtils.isNotBlank(pubXml)) {
        String locale = LocaleContextHolder.getLocale().toString();
        PubXmlDocument doc = new PubXmlDocument(pubXml);
        String authorNames = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "author_names");
        String zhTitle = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title");
        String enTitle = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_title");
        String briefDesc = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc");
        String briefDescEn = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc_en");
        String zhAbstract = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_abstract");
        String enAbstract = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_abstract");
        // Integer pubTypeId =
        // NumberUtils.toInt(doc.getXmlNodeAttribute(PubXmlConstants.PUB_TYPE_XPATH,
        // "id"), 4);
        // String pubType =
        // publicationTypeService.queryResultTypeName(pubTypeId);
        if ("zh_CN".equals(locale)) {
          String title = StringUtils.isBlank(zhTitle) ? enTitle : zhTitle;
          map.put("authorNames", XmlUtil.formateSymbolAuthors(title, authorNames));
          map.put("title", title);
          // map.put("source", (StringUtils.isBlank(briefDesc) ?
          // briefDescEn : briefDesc) + pubType);
          map.put("source", (StringUtils.isBlank(briefDesc) ? briefDescEn : briefDesc));
          map.put("pubAbstract", StringUtils.isBlank(zhAbstract) ? enAbstract : zhAbstract);
        } else {
          String title = StringUtils.isBlank(enTitle) ? zhTitle : enTitle;
          map.put("authorNames", XmlUtil.formateSymbolAuthors(title, authorNames));
          map.put("title", title);
          // map.put("source", (StringUtils.isBlank(briefDescEn) ?
          // briefDesc : briefDescEn) + pubType);
          map.put("source", (StringUtils.isBlank(briefDescEn) ? briefDesc : briefDescEn));
          map.put("pubAbstract", StringUtils.isBlank(enAbstract) ? zhAbstract : enAbstract);
        }
        map.put("des3Id", ServiceUtil.encodeToDes3(pubId.toString()));
      }
    } catch (Exception e) {
      logger.error(String.format("获取分享成果pubId={1}到站外所需的成果数据出现异常：", pubId), e);
    }
    return JacksonUtils.jsonMapSerializer(map);
  }

  @Override
  public Long getPsnPublicPubCount(Long psnId, String keywords, String excludedPubIds, List<Integer> permissions,
      String pubTypes) throws ServiceException {
    try {
      String uuid = null;
      if (StringUtils.isNotBlank(excludedPubIds)) {
        uuid = UUID.randomUUID().toString();
        this.irisExcludedPubService.saveIrisExcludedPub(ServiceUtil.splitStrToLong(excludedPubIds), uuid);
      }
      List<Integer> pubTypeList = ServiceUtil.splitStrToInteger(pubTypes);
      if (CollectionUtils.isEmpty(permissions)) {
        permissions = new ArrayList<Integer>();
        permissions.add(PsnCnfConst.ALLOWS);// 默认公开
      }
      // 查询关联权限表的公开成果数
      Long pubCount1 = this.publicationDao.queryPsnPublicPubCount(psnId, keywords, uuid, permissions, pubTypeList);

      if (StringUtils.isNotBlank(excludedPubIds)) {
        this.irisExcludedPubService.deleteIrisExcludedPub(uuid);
      }
      // 获取因其它导入方式在权限表没有记录的成果数，这些成果默认为公开
      Long pubCount2 = this.publicationDao.getPsnNotExistsResumePubCount(psnId);

      return (pubCount1 + pubCount2);
    } catch (Exception e) {
      logger.error(String.format("查询用户psnId=${1}的公开成果数出现异常：", psnId), e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Page<Publication> getPsnPublicPubByPage(Long psnId, String keywords, String excludedPubIds,
      List<Integer> permissions, String sortType, Page<Publication> page, String pubTypes) throws ServiceException {
    try {
      String uuid = null;
      if (StringUtils.isNotBlank(excludedPubIds)) {
        uuid = UUID.randomUUID().toString();
        this.irisExcludedPubService.saveIrisExcludedPub(ServiceUtil.splitStrToLong(excludedPubIds), uuid);
      }
      List<Integer> pubTypeList = ServiceUtil.splitStrToInteger(pubTypes);
      if (CollectionUtils.isEmpty(permissions)) {
        permissions = new ArrayList<Integer>();
        permissions.add(PsnCnfConst.ALLOWS);// 默认公开
      }
      page =
          this.publicationDao.queryPsnPublicPubByPage(psnId, keywords, uuid, permissions, sortType, page, pubTypeList);

      if (StringUtils.isNotBlank(excludedPubIds)) {
        this.irisExcludedPubService.deleteIrisExcludedPub(uuid);
      }

      return page;
    } catch (Exception e) {
      logger.error(String.format("查询用户psnId=${1}的公开成果记录出现异常：", psnId), e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Long getPsnPubCount(Long psnId, String keywords, String authors, String excludedPubIds, String pubTypes)
      throws ServiceException {
    try {
      String uuid = null;
      if (StringUtils.isNotBlank(excludedPubIds)) {
        uuid = UUID.randomUUID().toString();
        this.irisExcludedPubService.saveIrisExcludedPub(ServiceUtil.splitStrToLong(excludedPubIds), uuid);
      }
      List<Integer> pubTypeList = ServiceUtil.splitStrToInteger(pubTypes);
      Long pubCount = this.publicationDao.queryPsnPubCount(psnId, keywords, authors, uuid, pubTypeList);

      if (StringUtils.isNotBlank(excludedPubIds)) {
        this.irisExcludedPubService.deleteIrisExcludedPub(uuid);
      }

      return pubCount;
    } catch (Exception e) {
      logger.error(String.format("查询用户psnId=${1}的成果数出现异常：", psnId), e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Page<Publication> getPsnPubByPage(Long psnId, String keywords, String authors, String excludedPubIds,
      String sortType, Page<Publication> page, String pubTypes) throws ServiceException {
    try {
      String uuid = null;
      if (StringUtils.isNotBlank(excludedPubIds)) {
        uuid = UUID.randomUUID().toString();
        this.irisExcludedPubService.saveIrisExcludedPub(ServiceUtil.splitStrToLong(excludedPubIds), uuid);
      }
      List<Integer> pubTypeList = ServiceUtil.splitStrToInteger(pubTypes);
      page = this.publicationDao.queryPsnPubByPage(psnId, keywords, authors, uuid, sortType, page, pubTypeList);

      if (StringUtils.isNotBlank(excludedPubIds)) {
        this.irisExcludedPubService.deleteIrisExcludedPub(uuid);
      }

      return page;
    } catch (Exception e) {
      logger.error(String.format("查询用户psnId=${1}的成果记录出现异常：", psnId), e);
      throw new ServiceException(e);
    }
  }

  @Override
  public boolean checkPubBelongToPsn(Long pubId, Long psnId) throws ServiceException {
    try {
      boolean belong = false;
      Long count = this.publicationDao.queryPubCountByPubIdAnPsnId(pubId, psnId);
      if (count != null && count.intValue() > 0) {
        belong = true;
      }
      return belong;
    } catch (Exception e) {
      logger.error(String.format("检验成果pubId={1}是否属于某用户psnId={2}出现异常", pubId, psnId), e);
      throw new ServiceException(e);
    }
  }

  /**
   * 批量查询上周发表过成果的人.
   */
  @Override
  public List<Long> getPsnIdByLastWeekly(Integer size, Long lastPsnId) throws ServiceException {
    try {
      return publicationDao.getPsnIdByLastWeekly(size, lastPsnId);
    } catch (Exception e) {
      logger.error("查询上周发表过成果时出错", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 获取某人上周发表过的公开成果.
   * 
   */
  @Deprecated
  @Override
  public Map<String, Object> findPubLastWeekByPsnId(Long psnId, Long configId) throws ServiceException {

    try {
      return publicationDao.getPubInfoByConfig(psnId, configId);
    } catch (DaoException e) {
      logger.error("获取{1}上周发表过的公开成果时出错", psnId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Map<String, Object> findPubLastWeekByPsnId(Long psnId) throws ServiceException {

    try {
      return publicationDao.getPubInfoByConfig(psnId);
    } catch (DaoException e) {
      logger.error("获取{1}上周发表过的公开成果时出错", psnId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Map<String, Object> getShowPubData(Long pubId) throws ServiceException {
    Map<String, Object> map = null;
    Publication publication = publicationDao.get(pubId);
    if (publication != null) {
      map = new HashMap<String, Object>();
      map.put("id", pubId);
      map.put("des3Id", ServiceUtil.encodeToDes3(pubId.toString()));
      String title = null;
      String source = "";
      String briefDesc = StringUtils.replace(publication.getBriefDesc(), ">", "&gt;");
      briefDesc = StringUtils.replace(publication.getBriefDesc(), "<", "&lt;");
      String briefDescEn = StringUtils.replace(publication.getBriefDescEn(), ">", "&gt;");
      briefDescEn = StringUtils.replace(publication.getBriefDescEn(), "<", "&lt;");
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

      try {
        map.put("resOther", source);
        map.put("language", XmlUtil.isChinese(title) ? "zh" : "en");
        int resNode = BasicRmtSrvModuleConstants.SNS_MODULE_ID.intValue();
        map.put("resLink", domainscm + "/scmwebsns/publication/view?des3Id="
            + ServiceUtil.encodeToDes3(pubId.toString()) + "," + resNode);
        map.put("pic", this.pubFulltextService.getFulltextImageUrl(pubId));
      } catch (Exception e) {
        logger.error("getShowPubData", e);
        throw new ServiceException("getShowPubData", e);
      }
    }

    return map;
  }

  @Override
  public Map<String, String> getPubTitleById(Long pubId) {
    Publication pub = publicationDao.getPubTitleById(pubId);

    Map<String, String> returnMap = null;

    // 保证至少有标题出现
    if (pub != null && (StringUtils.isNotBlank(pub.getEnTitle()) || StringUtils.isNotBlank(pub.getZhTitle()))) {
      returnMap = new HashMap<String, String>();

      // 作者
      if (StringUtils.isNotBlank(pub.getAuthorNames())) {
        returnMap.put("authorNames", pub.getAuthorNames());
      }

      returnMap.put("zhTitle",
          pub.getZhTitle() == "" || pub.getZhTitle() == null ? pub.getEnTitle() : pub.getZhTitle());
      returnMap.put("enTitle",
          pub.getEnTitle() == "" || pub.getEnTitle() == null ? pub.getZhTitle() : pub.getEnTitle());

      // 来源
      if (StringUtils.isNotBlank(pub.getBriefDesc()) || StringUtils.isNotBlank(pub.getBriefDescEn())) {

        returnMap.put("briefDesc",
            pub.getBriefDesc() == "" || pub.getBriefDesc() == null ? pub.getBriefDescEn() : pub.getBriefDesc());
        returnMap.put("briefDescEn",
            pub.getBriefDescEn() == "" || pub.getBriefDescEn() == null ? pub.getBriefDesc() : pub.getBriefDescEn());
      }

    }
    return returnMap;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public void updateCiteTimeAndListInfo(Long pubId, Map map) throws ServiceException {
    try {
      Publication publication = this.publicationDao.get(pubId);
      String xml = this.getPubXmlById(pubId);
      if (publication != null || StringUtils.isNotBlank(xml)) {
        boolean isUpdate = false;
        PubXmlDocument doc = new PubXmlDocument(xml);
        Integer citedTimes = (Integer) map.get("citedTimes");
        // 引用次数大于原来的引用次数才更新
        if ((publication.getCitedTimes() == null && citedTimes != null)
            || (citedTimes != null && citedTimes > publication.getCitedTimes())) {
          isUpdate = true;
          publication.setCitedTimes(citedTimes);
          if (doc.existsNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cite_times")) {
            doc.setNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cite_times", citedTimes.toString());
          }
        }
        String pubListStr = (String) map.get("pubList");
        if (StringUtils.isNotBlank(pubListStr)) {
          isUpdate = true;
          PublicationList pubList = this.publicationListService.wrapPublicationList(pubListStr.split(","), null);
          PublicationList oldPubList = this.publicationListDao.getPublicationList(pubId);
          if (oldPubList != null) {
            pubList.setListSci(oldPubList.getListSci() == 1 ? 1 : pubList.getListSci());
            pubList.setListIstp(oldPubList.getListIstp() == 1 ? 1 : pubList.getListIstp());
            pubList.setListSsci(oldPubList.getListSsci() == 1 ? 1 : pubList.getListSsci());
            pubList.setListEi(oldPubList.getListEi() == 1 ? 1 : pubList.getListEi());
          }
          publication.setListInfo(this.publicationListService.convertPubListToString(pubList));

          if (!doc.existsNode(PubXmlConstants.PUB_LIST_XPATH)) {
            doc.createElement(PubXmlConstants.PUB_LIST_XPATH);
          }
          doc.setNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_sci", pubList.getListSci().toString());
          doc.setNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_istp", pubList.getListIstp().toString());
          doc.setNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ssci", pubList.getListSsci().toString());
          doc.setNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ei", pubList.getListEi().toString());
        }
        if (isUpdate) {
          this.publicationDao.save(publication);
          this.publicationXmlService.save(pubId, doc.getXmlString());
        }

      }
    } catch (Exception e) {
      logger.error("更新成果pubId={}引用次数和收录情况出现异常：{}", pubId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Map<String, Object> getShowPdwhPubData(Long pubId, Integer dbid) throws ServiceException {
    try {
      Map<String, Object> map = null;
      PdwhPublicationAll puball = publicationXmlPdwhService.getPdwhPublicationAll(pubId, dbid);
      if (puball != null) {
        map = new HashMap<String, Object>();
        map.put("id", pubId);
        map.put("des3Id", ServiceUtil.encodeToDes3(pubId.toString()));
        String title = null;
        String source = "";
        String briefDesc = StringUtils.replace(puball.getBriefDescZh(), ">", "&gt;");
        briefDesc = StringUtils.replace(puball.getBriefDescZh(), "<", "&lt;");
        String briefDescEn = StringUtils.replace(puball.getBriefDescEn(), ">", "&gt;");
        briefDescEn = StringUtils.replace(puball.getBriefDescEn(), "<", "&lt;");
        String locale = LocaleContextHolder.getLocale().toString();
        if ("zh_CN".equals(locale)) {
          title = StringUtils.isBlank(puball.getZhTitle()) ? puball.getEnTitle() : puball.getZhTitle();
          source = StringUtils.isBlank(briefDesc) ? briefDescEn : briefDesc;
        } else {
          title = StringUtils.isBlank(puball.getEnTitle()) ? puball.getZhTitle() : puball.getEnTitle();
          source = (StringUtils.isBlank(briefDescEn) ? briefDesc : briefDescEn) + " ";
        }
        map.put("authorNames", XmlUtil.formateSymbolAuthors(title, puball.getAuthorNames()));
        map.put("title", title);
        source = XmlUtil.formateSymbol(title, source);
        map.put("resOther", source);
        map.put("language", XmlUtil.isChinese(title) ? "zh" : "en");
        map.put("resLink", domainscm + "/scmwebsns/publication/viewpdwh?des3Id="
            + ServiceUtil.encodeToDes3(pubId.toString()) + "&dbid=" + dbid);
        map.put("pic", "");
      }
      return map;
    } catch (Exception e) {
      logger.error("getShowPdwhPubData", e);
      throw new ServiceException("getShowPdwhPubData", e);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public Page findPdwhRelatedPubs(Page page, Long pubId, int dbid) throws ServiceException {
    try {
      List<Publication> pubList = this.publicationDao.findPdwhRelatedPubs(page, pubId, dbid);
      if (CollectionUtils.isEmpty(pubList)) {
        return null;
      }
      List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
      for (Publication pub : pubList) {
        myPublicationQueryService.wrapQueryResultTypeName(pub);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pubId", pub.getId());
        map.put("des3PsnId", ServiceUtil.encodeToDes3(ObjectUtils.toString(pub.getPsnId())));
        Person psn = personManager.getAvatarsForEdit(pub.getPsnId());
        map.put("avatars", psn == null ? "" : psn.getAvatars());
        map.put("htmlAbstract",
            publicationWrapService.buildHtmlAbstract(pub, LocaleContextHolder.getLocale(), false, null, null));
        mapList.add(map);
      }
      page.setResult(mapList);
      return page;
    } catch (Exception e) {
      logger.error("查找成果详情页面(基准库)之相关成果出现异常：pubId=" + pubId + ", dbid=" + dbid, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Map<String, Object> findPdwhRelatedPub(Long pubId, int dbid) throws ServiceException {
    try {
      Map<String, Object> map = null;
      List<Publication> pubList = this.publicationDao.findPdwhRelatedPub(true, pubId, dbid);
      if (CollectionUtils.isEmpty(pubList)) {
        pubList = this.publicationDao.findPdwhRelatedPub(false, pubId, dbid);
      }
      if (CollectionUtils.isNotEmpty(pubList)) {
        map = new HashMap<String, Object>();
        map.put("pubId", pubList.get(0).getId());
        map.put("psnId", pubList.get(0).getPsnId());
      }
      return map;
    } catch (Exception e) {
      logger.error("查找成果详情页面(基准库)之某条相关成果信息出现异常：pubId=" + pubId + ", dbid=" + dbid, e);
      throw new ServiceException(e);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public Page<PubAuthor> findSnsPubAuthorList(Page page, Long id, Integer dbId) throws ServiceException {
    try {
      List<Long> psnIds = this.publicationPdwhService.findSnsPubOwnerPsnIds(page, id, dbId);
      List<PubAuthor> pubAuthorList = new ArrayList<PubAuthor>();
      long currPsnId = SecurityUtils.getCurrentUserId();
      String locale = LocaleContextHolder.getLocale().toString();
      if (CollectionUtils.isNotEmpty(psnIds)) {
        for (Long psnId : psnIds) {
          Person person = this.personManager.getPerson(psnId);
          PubAuthor pubAuthor = new PubAuthor();
          pubAuthor.setPsnId(person.getPersonId());
          boolean isPsnFriend = this.friendService.isPsnFirend(currPsnId, person.getPersonId());
          if (pubAuthor.getPsnId().equals(currPsnId) || isPsnFriend) {
            pubAuthor.setIsPsnFriend(1);
          }
          if ("en_US".equals(locale)) {
            if (StringUtils.isBlank(person.getFirstName()) && StringUtils.isBlank(person.getLastName())) {
              pubAuthor.setName(person.getName());
            } else {
              pubAuthor.setName(person.getFirstName() + " " + person.getLastName());
            }
          } else {
            if (StringUtils.isBlank(person.getName())) {
              pubAuthor.setName(person.getFirstName() + " " + person.getLastName());
            } else {
              pubAuthor.setName(person.getName());
            }
          }
          pubAuthor.setAvatars(person.getAvatars());
          pubAuthor.setViewTitolo(person.getViewTitolo());

          pubAuthorList.add(pubAuthor);
        }
      }
      page.setResult(pubAuthorList);
      return page;
    } catch (Exception e) {
      logger.error("获取基准库ID对应的PubAuthorList出错", e);
      throw new ServiceException("获取基准库ID对应的PubAuthorList出错", e);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public Page<PubAuthor> findSnsPubAuthorListByPubId(Page page, Long pubId) throws ServiceException {
    try {
      List<PubAuthor> pubAuthorList = new ArrayList<PubAuthor>();
      long currPsnId = SecurityUtils.getCurrentUserId();
      List<Map<String, String>> listMap = this.publicationPdwhService.findSnsPubOwnersByPubId(page, pubId);
      if (CollectionUtils.isNotEmpty(listMap)) {
        for (int i = 0; i < listMap.size(); i++) {
          Map<String, String> result = listMap.get(i);
          String psnId = "";
          if (result.get("MEMBER_PSN_ID") != null) {
            psnId = ((Serializable) result.get("MEMBER_PSN_ID")).toString();
          }
          if (StringUtils.isBlank(psnId)) {
            PubAuthor pubAuthor = new PubAuthor();
            pubAuthor.setName(((Serializable) result.get("PSN_NAME")).toString());
            pubAuthor.setIsPsnFriend(1);
            pubAuthor.setAvatars("");
            pubAuthor.setViewTitolo("");
            pubAuthorList.add(pubAuthor);
          } else {
            Person person = this.personManager.getPerson(NumberUtils.toLong(psnId));
            PubAuthor pubAuthor = new PubAuthor();
            pubAuthor.setPsnId(person.getPersonId());
            boolean isPsnFriend = this.friendService.isPsnFirend(currPsnId, person.getPersonId());
            if (pubAuthor.getPsnId().equals(currPsnId) || isPsnFriend) {
              pubAuthor.setIsPsnFriend(1);
            }
            pubAuthor.setName(person.getViewName());
            pubAuthor.setAvatars(person.getAvatars());
            pubAuthor.setViewTitolo(person.getViewTitolo());
            pubAuthorList.add(pubAuthor);
          }
        }
      }
      page.setResult(pubAuthorList);
      return page;
    } catch (Exception e) {
      logger.error("根据pubId查找出sns的其它相关成果的Authors出错, pubId=" + pubId, e);
      throw new ServiceException("根据pubId查找出sns的其它相关成果的Authors出错, pubId=" + pubId, e);
    }
  }

  @Override
  public Long findOtherRelatedPubId(Long pubId) throws ServiceException {
    try {
      List<Long> pubIdList = this.publicationPdwhService.findOtherRelatedPubIdByPubId(pubId);
      if (CollectionUtils.isEmpty(pubIdList)) {
        return null;
      }
      Long otherRelPubId = pubIdList.get(0);
      return otherRelPubId;
    } catch (Exception e) {
      logger.error("查找其它相关成果的pubId出现异常：pubId=" + pubId, e);
    }
    return null;
  }

  @Override
  public Map<String, Object> getPubRecommendRequir(Long pubId) throws ServiceException {
    try {
      String pubXml = this.getPubXmlById(pubId);
      if (StringUtils.isBlank(pubXml)) {
        return null;
      }
      PubXmlDocument doc = new PubXmlDocument(pubXml);
      String zhTitle = StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title"));
      String enTitle = StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_title"));
      String zhabstract =
          StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_abstract"));
      String enabstract =
          StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_abstract"));
      String createPsnId =
          StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "record_psn_id"));
      if (StringUtils.isBlank(zhTitle) && StringUtils.isBlank(enTitle)) {
        return null;
      }
      PubInfo pubInfo = new PubInfo();
      if (StringUtils.isNotBlank(zhTitle)) {
        pubInfo.setZhTitle(zhTitle);
        pubInfo.setZhAbs(zhabstract);
      } else {
        pubInfo.setEnTitle(enTitle);
        pubInfo.setEnAbs(enabstract);
      }
      List<KeywordSplit> list = this.keywordsDicService.findPubKeywords(pubInfo);
      list = new ArrayList<KeywordSplit>(list.subList(0, list.size() < 10 ? list.size() : 10));
      if (CollectionUtils.isEmpty(list)) {
        return null;
      }

      List<String> keywords = new ArrayList<String>();
      for (KeywordSplit keywordSplit : list) {
        keywords.add(keywordSplit.getKeyword());
      }
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("pubId", pubId);
      map.put("psnId", NumberUtils.toLong(createPsnId));
      map.put("keywords", keywords);
      return map;
    } catch (Exception e) {
      logger.error(" 获取成果相关文献必要条件出现异常：pubId=" + pubId, e);
    }
    return null;
  }

  @Override
  public List<Map<String, Object>> getPubFullTexts(String des3PubId, boolean isExcludeCurPub) throws ServiceException {
    String[] params = ServiceUtil.decodeFromDes3(des3PubId).split(",");
    Long pubId = NumberUtils.createLong(params[0]);
    // List<Long> pubIds =
    // this.publicationPdwhService.findSnsPubIdsByPubId(pubId);
    // 通过成果分组信息来获取相关成果 tsz_2014.11.28_SCM-5983
    PubGrouping pubGrouping = pubGroupingService.findPubGroup(pubId);
    List<Long> pubIds = null;
    if (pubGrouping != null) {
      pubIds = pubGroupingService.findSnsPubIdsByGroupId(pubGrouping.getGroupId());
    }
    if (CollectionUtils.isEmpty(pubIds)) {
      return null;
    }
    Long currPsnId = SecurityUtils.getCurrentUserId();
    if (isExcludeCurPub) {
      pubIds.remove(pubId);
    }
    List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
    try {
      List<Map<String, Object>> downloadList = new ArrayList<Map<String, Object>>();
      List<Map<String, Object>> requestList = new ArrayList<Map<String, Object>>();

      List<Publication> pubList = this.publicationDao.getPubFullTexts(pubIds);

      if (CollectionUtils.isNotEmpty(pubList)) {
        for (Publication pub : pubList) {
          String des3Id = "";
          Map<String, Object> map = new HashMap<String, Object>();
          if (pub.getId().longValue() == pubId.longValue()) {
            des3Id = des3PubId;
          } else {
            des3Id = pub.getDes3Id();
          }
          map.put("des3Id", des3Id);
          map.put("nodeId", SecurityUtils.getCurrentAllNodeId().get(0));
          map.put("des3PsnId", ServiceUtil.encodeToDes3(ObjectUtils.toString(pub.getPsnId())));
          Person psn = this.personManager.getPersonByRecommend(pub.getPsnId());
          map.put("name", psn.getViewName());
          map.put("avatars", psn.getAvatars());
          map.put("viewTitolo", psn.getViewTitolo());
          if (StringUtils.isNotBlank(pub.getFulltextFileid())) {
            Map<String, Object> rtnMap = this.getPubFullTextInfo(des3Id, currPsnId);
            if (ObjectUtils.toString(rtnMap.get("permission")).equals("yes")) {// 全文下载
              map.put("isFullTextDownload", 1);
              map.put("isFullTextReq", 0);
              downloadList.add(map);
            } else {// 全文请求（有全文，但无权限）
              map.put("isFullTextDownload", 0);
              map.put("isFullTextReq", 1);
              requestList.add(map);
            }
          } else {// 全文请求（无全文）
            if (!pub.getPsnId().equals(currPsnId) && pub.getArticleType() != PublicationArticleType.REFERENCE) {
              map.put("isFullTextDownload", 0);
              map.put("isFullTextReq", 1);
              requestList.add(map);
            }
          }
        }
      }
      mapList.addAll(downloadList);
      mapList.addAll(requestList);
      return mapList;
    } catch (Exception e) {
      logger.error("getPubFullTexts获取相关全文出错，pubId=" + pubId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Map<String, Object> getPubFullTextInfo(String pubDes3Id, Long currPsnId) throws ServiceException {
    Map<String, Object> rtnMap = null;
    String[] params = ServiceUtil.decodeFromDes3(pubDes3Id).split(",");
    Long pubId = NumberUtils.toLong(params[0]);
    try {
      Publication publication = this.publicationDao.getPubOwnerPsnIdOrStatus(pubId);
      if (publication == null || publication.getStatus() == 1) {
        return null;
      }
      PubFulltext fulltext = this.pubFulltextService.getPubFulltextByPubId(pubId);
      if (fulltext == null) {
        return null;
      }
      rtnMap = new HashMap<String, Object>();
      boolean isEnableDowload = false;
      Long pubOwnerId = publication.getPsnId();
      if (pubOwnerId.longValue() == currPsnId.longValue()) { // 下载自己的成果或文献全文
        isEnableDowload = true;
      } else {
        int permissionInt = fulltext.getPermission();
        if (permissionInt != 1 && permissionInt != 2) { // 所有人可见
          isEnableDowload = true;
        } else {
          // 判断是否下载本人分享给请求下载人的成果
          if (params.length > 2 && (params[2].matches("\\d{4}-\\d{2}-\\d{2}"))) {
            if (params.length > 3 && params[1].equals(ObjectUtils.toString(PublicationDetailFrom.PUBLICATION_SHARE))
                && params[3].equals("1")) {
              isEnableDowload = true;
            } else if (params[1].equals(ObjectUtils.toString(PublicationDetailFrom.FULLTEXT_REQUEST))) {
              isEnableDowload = true;
            } else {
              isEnableDowload = false;
            }
          }
          if (!isEnableDowload) {
            // 群组中的成果或文献的全文下载权限对群组成员公开SCM-3207
            isEnableDowload = this.groupService.checkPsnIsInPubGroup(currPsnId, pubId);

            // 不是项目所在群组的成员且项目全文为好友可下载
            if (!isEnableDowload && permissionInt == 1 && currPsnId != 0) {
              boolean isPsnFriend = friendService.isPsnFirend(currPsnId, pubOwnerId);
              if (isPsnFriend) {
                isEnableDowload = true;
              }
            }
          }
        }
      }
      rtnMap.put("permission", isEnableDowload ? "yes" : "no");
      return rtnMap;
    } catch (Exception e) {
      logger.error(String.format("人员psnId={0}获取成果pubId={1}的全文信息出现异常：", currPsnId, pubId), e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Publication> getNeedRefreshPub(int maxSize) throws ServiceException {
    try {
      return this.publicationDao.queryNeedRefreshPub(maxSize);
    } catch (Exception e) {
      logger.error("查询需要刷新到pub_pdwh表记录的成果", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void refreshPubToPubPdwh(Publication pub) throws ServiceException {
    try {
      PublicationPdwh pubPdwh = this.publicationPdwhService.getPubPdwh(pub.getId());
      if (pubPdwh == null) {
        Long pubPdwhId = isiPublicationService.getPubPdwhIdBySourceId(pub.getIsiId());
        if (pubPdwhId != null) {
          pubPdwh = new PublicationPdwh();
          pubPdwh.setPubId(pub.getId());
          pubPdwh.setIsiId(pubPdwhId);
          this.publicationPdwhService.savePublicationPdwh(pubPdwh);
        }
      }
    } catch (Exception e) {
      LogUtils.error(logger, e, "刷新pub_id={}到pub_pdwh表记录的成果", pub.getId());
      throw new ServiceException(e);
    }
  }

  @Override
  public Map<String, Object> getPubSyncPubFtSrvData(Long pubId) throws ServiceException {

    Publication pub = this.publicationDao.get(pubId);
    return this.getPubSyncPubFtSrvData(pub, true);
  }

  @Override
  public Map<String, Object> getPubSyncPubFtSrvData(Publication pub, boolean syncOldData) throws ServiceException {

    try {
      Map<String, Object> map = new HashMap<String, Object>();
      if (pub != null) {
        map.put("pubId", pub.getId());
        map.put("psnId", pub.getPsnId());
        map.put("pubType", pub.getTypeId());
        map.put("zhTitle", pub.getZhTitle());
        map.put("enTitle", pub.getEnTitle());
        if (StringUtils.isNotBlank(pub.getZhTitle())) {
          map.put("zhTitleHash", HashUtils.getStrHashCode(pub.getZhTitle()));
        }
        if (StringUtils.isNotBlank(pub.getEnTitle())) {
          map.put("enTitleHash", HashUtils.getStrHashCode(pub.getEnTitle()));
        }
        map.put("isiId", pub.getIsiId());
        if (pub.getTypeId() == PublicationTypeEnum.JOURNAL_ARTICLE) {
          String issn = pub.getIssn();
          if (syncOldData) {
            issn = this.publicationJournalDao.getPubJIssn(pub.getId());
          }
          map.put("issn", issn);
        }

        PubXmlDocument doc = null;
        if (pub.getTypeId() == PublicationTypeEnum.CONFERENCE_PAPER) {
          String confName = pub.getConfName();
          if (syncOldData) {
            String xmlData = this.getPubXmlById(pub.getId());
            if (StringUtils.isNotBlank(xmlData)) {
              doc = new PubXmlDocument(xmlData);
              confName = doc.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "conf_name");
              if (StringUtils.isBlank(confName)) {
                confName = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "conf_name");
              }
            }
          }
          map.put("confName", confName);
        }

        map.put("fulltextFileId", pub.getFulltextFileid());
        int permission = pub.getPermission();
        if (syncOldData) {
          if (doc == null) {
            String xmlData = this.getPubXmlById(pub.getId());
            if (StringUtils.isNotBlank(xmlData)) {
              doc = new PubXmlDocument(xmlData);
            }
          }
          if (doc != null) {
            String permissionStr = doc.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "permission");
            permission = NumberUtils.toInt(permissionStr, PubFulltextPermission.all);
          }
        }
        map.put("permission", permission);
        boolean isMatch = this.pubOwnerMatchService.isPubOwnerMatch(pub.getId(), pub.getPsnId());
        map.put("isOwnerMatch", isMatch ? 1 : 0);
        map.put("status", pub.getStatus());
      }
      return map;
    } catch (Exception e) {
      LogUtils.error(logger, e, "获取发送到pubftsrv的成果={}冗余信息出现异常", pub.getId());
      throw new ServiceException(e);
    }
  }

  /**
   * 保存全文.
   * 
   * @param pub
   */
  private void savePubFulltext(Publication pub) {
    try {
      // 如果对应的成果存在分组信息 需要加上分组id tsz_2014.12.1
      Integer fulltextNode =
          pub.getFulltextNodeId() == null ? SecurityUtils.getCurrentAllNodeId().get(0) : pub.getFulltextNodeId();
      Long groupId = pubGroupingService.findPubGroupId(pub.getId());

      this.pubFulltextService.dealPubFulltext(pub.getId(), pub.getPsnId(),
          StringUtils.isNotBlank(pub.getFulltextFileid()) ? NumberUtils.createLong(pub.getFulltextFileid()) : null,
          fulltextNode, pub.getFulltextExt(), pub.getPermission(), groupId);
    } catch (Exception e) {
      logger.error("保存成果pubId={}全文fulltextFileId={}数据出错：{}", pub.getId(), pub.getFulltextFileid(), e);
    }
  }

  @Override
  public void syncPubToPubFtSrv(Publication pub) throws ServiceException {

    try {
      Map<String, Object> map = this.getPubSyncPubFtSrvData(pub, false);
      if (MapUtils.isNotEmpty(map)) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(1);
        list.add(map);
        // FIXME 2015-10-29 取消MQ -done
        this.pubSyncToPubFtSrvProducer.sendUpdatePubMessage(list);
      }
    } catch (Exception e) {
      LogUtils.error(logger, e, "同步成果pubId={}冗余信息到pubftsrv出现异常", pub.getId());
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> findAllPubIds(Long lastPubId, Integer size) throws ServiceException {
    try {
      return this.publicationDao.findAllPubIds(lastPubId, size);
    } catch (Exception e) {
      LogUtils.error(logger, e, "查找所有成果ID出现问题，lastPubId=" + lastPubId);
      throw new ServiceException("查找所有成果ID出现问题，lastPubId=" + lastPubId, e);
    }
  }

  @Override
  public Publication getPubBasicRcmdInfo(Long pubId) throws ServiceException {
    try {
      return publicationDao.getPubBasicRcmdInfo(pubId);
    } catch (Exception e) {
      logger.error("获取成果推荐基本信息pubId=" + pubId, e);
      throw new ServiceException("获取成果推荐基本信息pubId=" + pubId, e);
    }
  }

  @Override
  public void save(Publication pe) throws ServiceException {
    publicationDao.save(pe);
  }

  @SuppressWarnings("rawtypes")
  @Override
  public List findPubMatchOwnerIds(int size) throws ServiceException {
    return publicationDao.findPubMatchOwnerIds(size);
  }

  @Override
  public void executedPubMatchOwnerIds(Long pubId) throws ServiceException {
    publicationDao.executedPubMatchOwnerIds(pubId);
  }

  @SuppressWarnings("rawtypes")
  @Override
  public void updatePubAuthority(Long pubId) throws ServiceException {
    try {
      PubXmlDocument xmlDocument = new PubXmlDocument(this.getPubXmlById(pubId));
      Long psnId = NumberUtils.toLong(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "record_psn_id"));
      List authors = xmlDocument.getNodes(PubXmlConstants.PUB_MEMBERS_MEMBER_XPATH);
      if (authors == null || authors.size() == 0) {
        return;
      }
      Boolean flag = false;
      Person psn = personManager.getPerson(psnId);
      for (int i = 0; i < authors.size(); i++) {
        Element ele = (Element) authors.get(i);
        String pmName = ele.attributeValue("member_psn_name");
        flag = matchPubAthor(pmName, psn);
        if (flag) {
          break;
        }
      }
      // 如果未匹配上则设置为隐私为本人
      if (!flag) {
        Integer anyUser = Integer.parseInt(PsnCnfConst.ALLOWS_SELF.toString());
        // 构造权限对象
        PsnConfigPub cnfPub = new PsnConfigPub();
        cnfPub.getId().setPubId(pubId);
        cnfPub.setAnyUser(anyUser);
        cnfPub.setAnyView(cnfPub.getAnyUser());
        psnCnfService.save(psnId, cnfPub);// 保存权限
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "authority", ObjectUtils.toString(anyUser));
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "match_owner", ObjectUtils.toString(flag));
        this.publicationXmlService.save(pubId, xmlDocument.getXmlString());
      }
    } catch (Exception e) {
      logger.error("更新成果隐私权限出错", e);
    }
  }

  public boolean matchPubAthor(String pmName, Person psn) {
    if (StringUtils.isBlank(pmName) || null == psn) {
      return false;
    }
    pmName = pmName.toLowerCase();
    if (StringUtils.isNotBlank(psn.getName())) {
      if (pmName.indexOf(psn.getName().toLowerCase()) >= 0) {
        return true;
      }
    }
    String firstName = XmlUtil.getCleanAuthorName(psn.getFirstName());
    String lastName = XmlUtil.getCleanAuthorName(psn.getLastName());
    if (StringUtils.isBlank(firstName) || StringUtils.isBlank(lastName)) {
      return false;
    }
    String preF = firstName.substring(0, 1).toLowerCase();
    lastName = lastName.toLowerCase();
    // 尝试z lin 是否匹配上alen z lin或者 z alen lin
    int index = pmName.indexOf(preF);
    if (index > -1 && pmName.substring(index).endsWith(lastName)) {
      return true;
    }
    // 尝试lin z是否匹配上lin z alen或者lin alen z
    index = pmName.lastIndexOf(preF);
    if (index > 0 && pmName.substring(0, index).startsWith(lastName)) {
      return true;
    }
    return false;
  }

  // 更新成果brief_desc_en字段用
  @Override
  public List findPubIdsBatchByPubType(Long lastId, int batchSize) throws ServiceException {
    try {
      return publicationDao.findPubIdsBatchByPubType(lastId, batchSize);
    } catch (DaoException e) {
      logger.error("批量查询成果信息出错，lastId=" + lastId, e);
      throw new ServiceException(e);
    }
  }

  /**
   * 根据成果ID更新成果brief_desc_en字段
   */
  @Override
  public Long updateBriefDescEnTask(Long pubId) throws ServiceException {
    try {
      Publication publication = publicationDao.get(pubId);

      if (publication != null) {
        PubXmlProcessContext context = new PubXmlProcessContext();
        context.setCurrentPubId(publication.getId());
        context.setCurrentUserId(SecurityUtils.getCurrentUserId());
        context.setCurrentLanguage("zh");
        PublicationForm form = new PublicationForm();
        form.setPubId(pubId);

        PublicationXml pubXml = this.publicationXmlService.getById(form.getPubId());
        if (pubXml == null || StringUtils.isBlank(pubXml.getXmlData())) {
          return pubId;
        }
        String xmlData = pubXml.getXmlData();

        PubXmlDocument xmlDocument = new PubXmlDocument(xmlData);

        // 刷新常数字段
        PubConstFieldRefresh.refresh(xmlDocument, this.scholarPublicationXmlServiceFactory);

        this.checkContainsHtml(xmlDocument, form);
        form.setTypeId(xmlDocument.getPubTypeId());
        form.setArticleType(xmlDocument.getArticleTypeId());
        form.setPubXml(xmlDocument.getXmlString());
        form.setOwnerPsnId(xmlDocument.getOwerPsnId());
        checkPubAuthority(form, xmlDocument);

        String xml = form.getPubXml();
        PubXmlDocument xmlDoc = new PubXmlDocument(xml);
        String briefEn = scholarPublicationXmlManager.getLanguagesBriefDesc(LocaleUtils.toLocale("en_US"), context,
            xmlDoc, publication.getTypeId());
        if (StringUtils.isNotBlank(briefEn)) {
          publicationDao.updateBriefDesc("en_US", briefEn, pubId);
        }
      }
    } catch (Exception e) {
      logger.error("重构brief_desc_en出错,pubId:{}", pubId, e);
    }
    return pubId;
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

    } catch (IllegalArgumentException e) {
      logger.error("检测权限时，个人配置：主表PSN_CONFIG中找不到该人员信息，改人员信息可能已丢失", e);
    } catch (Exception e) {
      logger.error("获取成果隐私设置出错", e);
    }
  }

  @Override
  public String getFulltextPubIds(String pubIdsStr) throws ServiceException {
    String result = "";
    List<Long> pubIds = new ArrayList<Long>();
    String[] pubIdStrs = StringUtils.split(pubIdsStr, ",");
    for (int i = 0; i < pubIdStrs.length; i++) {
      pubIds.add(Long.valueOf(pubIdStrs[i]));
    }
    if (!CollectionUtils.isEmpty(pubIds)) {
      List<Long> ftPubIds = this.pubFulltextDao.getFulltextPubIds(pubIds);
      if (!CollectionUtils.isEmpty(ftPubIds)) {
        result = JacksonUtils.jsonObjectSerializer(ftPubIds);
      }
    }
    return result;
  }

  @Override
  public void isSyncPublicationToProposal(PublicationForm loadXml) throws ServiceException {

    try {

      if ("proposal".equals(loadXml.getFrom())) {

        this.nsfcPrpPubService.syncPublicationToProposal(loadXml);

      }
      // 2015-11-4 老系统已经没有了对应的远程实现类
      // else if ("gxprp".equals(loadXml.getFrom())) {
      //
      // this.getPrpPubService().syncPublicationToProposal(loadXml);
      //
      // }
    } catch (Exception e) {

      e.printStackTrace();
    }

  }

  @Override
  public Map<String, String> getPubFullTextInfo(Long pubId) throws ServiceException {
    Map<String, String> rtnMap = new HashMap<String, String>();
    try {
      PubXmlDocument doc = new PubXmlDocument(getPubXmlById(pubId));
      rtnMap.put("fullTextId", doc.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "file_id"));
      rtnMap.put("fullTextName", doc.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "file_name"));
      return rtnMap;
    } catch (DocumentException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public Map<String, String> getPubFullTextInfoFromPubDataStore(Long pubId) throws ServiceException {
    Map<String, String> rtnMap = new HashMap<String, String>();
    try {
      // PubXmlDocument doc = new PubXmlDocument(getPubXmlById(pubId));
      // 任务从v_pub_data_store获取fulltextid，成果处理中，会
      // 优先向此表写入准备处理全文图片的id。在scm_pub_xml中会滞后，或者为空

      PubDataStore pubXml = pubDataStoreDao.get(pubId);
      if (pubXml == null || pubXml.getData() == null) {
        logger.error("没有找到pubId为" + pubId + "的xml数据");
      }
      PubXmlDocument doc = new PubXmlDocument(pubXml.getData());
      rtnMap.put("fullTextId", doc.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "file_id"));
      rtnMap.put("fullTextName", doc.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "file_name"));
      return rtnMap;
    } catch (DocumentException e) {
      throw new ServiceException(e);
    }
  }
}
