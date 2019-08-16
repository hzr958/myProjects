package com.smate.center.batch.service.pub;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.GroupBaseinfoDao;
import com.smate.center.batch.dao.sns.pub.GroupInvitePsnNodeDao;
import com.smate.center.batch.dao.sns.pub.GroupKeywordsDao;
import com.smate.center.batch.dao.sns.pub.GroupPsnNodeDao;
import com.smate.center.batch.dao.sns.pub.GroupPubDao;
import com.smate.center.batch.dao.sns.pub.GroupPubNodeDao;
import com.smate.center.batch.dao.sns.pub.GroupRefDao;
import com.smate.center.batch.dao.sns.pub.PubDataStoreDao;
import com.smate.center.batch.dao.sns.pub.PublicationDao;
import com.smate.center.batch.dao.sns.pub.ScmPubXmlDao;
import com.smate.center.batch.dao.sns.pub.TaskGroupResNotifyDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.prj.GroupInvitePsn;
import com.smate.center.batch.model.sns.pub.GroupInvitePsnNode;
import com.smate.center.batch.model.sns.pub.GroupInvitePsnNodePk;
import com.smate.center.batch.model.sns.pub.GroupPsn;
import com.smate.center.batch.model.sns.pub.GroupPsnNode;
import com.smate.center.batch.model.sns.pub.GroupPsnNodeForm;
import com.smate.center.batch.model.sns.pub.GroupPubNode;
import com.smate.center.batch.model.sns.pub.GroupPubs;
import com.smate.center.batch.model.sns.pub.GroupRefs;
import com.smate.center.batch.model.sns.pub.PubDataStore;
import com.smate.center.batch.model.sns.pub.Publication;
import com.smate.center.batch.model.sns.pub.PublicationList;
import com.smate.center.batch.model.sns.pub.ScmPubXml;
import com.smate.center.batch.model.sns.pub.TaskGroupResNotify;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.TaskMarkerService;
import com.smate.center.batch.service.pub.mq.GroupPubRemoveSyncMessage;
import com.smate.center.batch.service.pubfulltexttoimage.ScmPubXmlService;
import com.smate.center.batch.util.pub.BasicRmtSrvModuleConstants;
import com.smate.center.batch.util.pub.PublicationArticleType;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 我的群组与成果的关系实现类.
 * 
 * @author LY
 * 
 */
@Service("groupPublicationService")
@Transactional(rollbackFor = Exception.class)
public class GroupPublicationServiceImpl implements GroupPublicationService {
  private static Logger logger = LoggerFactory.getLogger(GroupPublicationServiceImpl.class);

  public static String ZH_CN = "zh";
  public static String EN = "en";
  @Autowired
  private GroupPsnNodeDao groupPsnNodeDao;
  @Autowired
  private GroupPubNodeDao groupPubNodeDao;
  @Autowired
  private GroupService groupService;
  @Autowired
  private PublicationDao publicationDao;
  @Autowired
  private GroupPubDao groupPubDao;
  @Autowired
  private GroupRefDao groupRefDao;
  @Autowired
  private GroupResourceService groupResourceService;
  @Autowired
  private PublicationStatisticsService publicationStatisticsService;
  @Autowired
  private TaskGroupResNotifyDao taskGroupResNotifyDao;
  @Autowired
  private PublicationListService publicationListService;
  @Autowired
  private GroupRefsService groupRefsService;
  @Autowired
  private GroupPubsService groupPubsService;
  @Autowired
  private GroupInvitePsnNodeDao groupInvitePsnNodeDao;
  @Autowired
  private DynamicGroupProduceService dynGroupProduceAddPubService;
  @Autowired
  private GroupKeywordsDao groupKeywordsDao;
  @Autowired
  private GroupBaseinfoDao groupBaseInfoDao;
  @Autowired
  private ScmPubXmlService scmPubXmlService;
  @Autowired
  private ScmPubXmlDao scmPubXmlDao;
  @Autowired
  private PubDataStoreDao pubDataStoreDao;
  @Autowired
  private CacheService cacheService;
  @Autowired
  private TaskMarkerService taskMarkerService;

  /**
   * FIXME 这没有冗余群组的权限控制，导致只能远程访问其他节点. (non-Javadoc)
   * 
   * @see com.iris.scm.scmweb.service.group.GroupPublicationService#getGroupInvitePsnNodes(Integer,
   *      boolean)
   */
  @Override
  public List<GroupPsnNodeForm> getGroupInvitePsnNodes(Integer articleType, boolean isCurrResNum) {
    try {
      List<GroupPsnNode> groupPsnNodes =
          this.groupPsnNodeDao.getGroupInvitePsnNodesByPsnId(SecurityUtils.getCurrentUserId());
      List<GroupPsnNodeForm> forms = new ArrayList<GroupPsnNodeForm>();
      for (GroupPsnNode groupPsnNode : groupPsnNodes) {
        GroupPsnNodeForm form = new GroupPsnNodeForm();
        form.setDes3GroupId(groupPsnNode.getDes3GroupId());
        form.setDisciplines(groupPsnNode.getDisciplines());
        form.setGroupCategory(groupPsnNode.getGroupCategory());
        form.setGroupId(groupPsnNode.getGroupId());
        form.setGroupName(groupPsnNode.getGroupName());
        form.setNodeId(groupPsnNode.getNodeId());
        form.setOpenType(groupPsnNode.getOpenType());
        GroupInvitePsn groupInvitePsn = null;
        GroupPsn groupPsn = null;
        groupInvitePsn = this.groupService.findGroupInvitePsn(form.getGroupId());
        groupPsn = this.groupService.getGroupPsnInfo(form.getGroupId());
        if (groupInvitePsn != null && groupPsn != null) {
          if (articleType.intValue() == PublicationArticleType.OUTPUT && !"1".equals(groupPsn.getIsPubView())) {
            continue;
          }
          if (articleType.intValue() == PublicationArticleType.REFERENCE && !"1".equals(groupPsn.getIsRefView())) {
            continue;
          }

          if ("1".equals(groupInvitePsn.getGroupRole()) || "2".equals(groupInvitePsn.getGroupRole())) {
            form.setIsCanAdd(1);
          } else if ("1".equals(groupPsn.getPubViewType()) && articleType.intValue() == PublicationArticleType.OUTPUT) {
            form.setIsCanAdd(1);

          } else if ("1".equals(groupPsn.getRefViewType())
              && articleType.intValue() == PublicationArticleType.REFERENCE) {
            form.setIsCanAdd(1);

          } else {
            form.setIsCanAdd(0);
          }
          form.setIsPubView(groupPsn.getIsPubView());
          form.setIsRefView(groupPsn.getIsRefView());
        }
        int count = 0;
        if (isCurrResNum) {
          count = this.publicationDao.getCurrGroupPubNum(groupPsnNode.getGroupId(), articleType,
              SecurityUtils.getCurrentUserId());
        } else {
          count = this.publicationDao.getGroupPubNum(groupPsnNode.getGroupId(), articleType,
              SecurityUtils.getCurrentUserId());
        }
        if (count == 0) {
          continue;
        }
        form.setCount(count);
        forms.add(form);
      }
      return forms;
    } catch (Exception e) {
      logger.error(e.getMessage() + e);
    }
    return null;
  }



  /**
   * 增加群组成果文献数据(重新整理方法中代码_MJG_SCM-3543).
   */
  @Override
  public void addPublicationToGroup(String des3PubIds, String groupIds, Integer articleType, Long folderId, int dynFlag)
      throws ServiceException {
    String[] pubIds = StringUtils.split(des3PubIds, ",");
    String[] groups = StringUtils.split(groupIds, ",");
    Long currentUserId = SecurityUtils.getCurrentUserId();
    int currentNodeId = SecurityUtils.getCurrentUserNodeId();

    JSONArray resDetails = new JSONArray();
    JSONObject resJsonObject = null;
    // 遍历成果数组.
    for (String iPubId : pubIds) {
      String pubIdStr = iPubId;
      if (!StringUtils.isNumeric(pubIdStr)) {
        pubIdStr = ServiceUtil.decodeFromDes3(pubIdStr);
      }
      Long pubId = Long.valueOf(pubIdStr);
      resJsonObject = new JSONObject();
      resJsonObject.accumulate("resId", pubId);
      resJsonObject.accumulate("resNode", currentNodeId);
      resDetails.add(resJsonObject);

      for (String iGroupId : groups) {
        if (!StringUtils.isNumeric(iGroupId)) {
          // groupId = ServiceUtil.decodeFromDes3(groupId);
        }
        Long groupId = Long.valueOf(iGroupId);
        if (this.isAddGroupByPubId(pubId, groupId, articleType)) {
          continue;
        }
        GroupPsnNode groupPsnNode = this.groupPsnNodeDao.get(groupId);
        // 保存群组成果节点数据.
        this.saveGroupPubNode(articleType, pubId, groupId, currentUserId);
        // 保存群组资源.
        this.saveGroupRes(pubId, groupId, folderId, articleType, groupPsnNode);
        // 收集群组资源更新数据
        this.collectionGroupResData(pubId, groupId, currentUserId, groupPsnNode);
      }
    }
    // 生成群组增加资源文件的动态.
    if (dynFlag == 1) {
      this.createGroupAddResDyn(groups, resDetails, articleType, currentNodeId, currentUserId);
    }
    // 清除统计缓存, 万一出错不影响整体导入
    publicationStatisticsService.clearPubGroupStatistic(currentUserId, articleType);

  }

  /**
   * 增加群组成果文献数据(重新整理方法中代码_MJG_SCM-3543). 在群组中无法用SecurityUtils获取当前操作人id，重新方法
   */
  @Override
  public void addPublicationToGroup(Long psnId, String des3PubIds, String groupIds, Integer articleType, Long folderId,
      int dynFlag) throws ServiceException {
    String[] pubIds = StringUtils.split(des3PubIds, ",");
    String[] groups = StringUtils.split(groupIds, ",");
    Long currentUserId = psnId;
    int currentNodeId = SecurityUtils.getCurrentUserNodeId();

    JSONArray resDetails = new JSONArray();
    JSONObject resJsonObject = null;
    // 遍历成果数组.
    for (String iPubId : pubIds) {
      String pubIdStr = iPubId;
      if (!StringUtils.isNumeric(pubIdStr)) {
        pubIdStr = ServiceUtil.decodeFromDes3(pubIdStr);
      }
      Long pubId = Long.valueOf(pubIdStr);
      resJsonObject = new JSONObject();
      resJsonObject.accumulate("resId", pubId);
      resJsonObject.accumulate("resNode", currentNodeId);
      resDetails.add(resJsonObject);

      for (String iGroupId : groups) {
        if (!StringUtils.isNumeric(iGroupId)) {
          // groupId = ServiceUtil.decodeFromDes3(groupId);
        }
        Long groupId = Long.valueOf(iGroupId);
        if (this.isAddGroupByPubId(pubId, groupId, articleType)) {
          continue;
        }
        GroupPsnNode groupPsnNode = this.groupPsnNodeDao.get(groupId);
        // 保存群组成果节点数据.
        this.saveGroupPubNode(articleType, pubId, groupId, currentUserId);
        // 保存群组资源.
        this.saveGroupRes(pubId, groupId, folderId, articleType, groupPsnNode);
        // 收集群组资源更新数据
        this.collectionGroupResData(pubId, groupId, currentUserId, groupPsnNode);
      }
    }
    // 生成群组增加资源文件的动态.
    if (dynFlag == 1) {
      this.createGroupAddResDyn(groups, resDetails, articleType, currentNodeId, currentUserId);
    }
    // 清除统计缓存, 万一出错不影响整体导入
    publicationStatisticsService.clearPubGroupStatistic(currentUserId, articleType);

  }

  @Override
  public boolean isAddGroupByPubId(Long pubId, Long groupId, Integer articleType) throws ServiceException {
    Long count = 0L;
    try {
      count = this.groupPubNodeDao.getCountGroupPubNode(pubId, groupId, articleType);
    } catch (DaoException e) {
      logger.error("判断成果是否在群组中失败" + e);
    }
    if (count > 0) {
      return true;
    }
    return false;
  }

  /**
   * 保存群组成果节点数据.
   * 
   * @param articleType
   * @param pubId
   * @param groupId
   * @param currentUserId
   */
  private void saveGroupPubNode(Integer articleType, Long pubId, Long groupId, Long currentUserId) {
    GroupPubNode groupPubNode = new GroupPubNode();
    groupPubNode.setArticleType(articleType);
    groupPubNode.setGroupId(groupId);

    groupPubNode.setPsnId(currentUserId);
    groupPubNode.setPubId(pubId);
    this.groupPubNodeDao.save(groupPubNode);
  }

  /**
   * 保存群组资源.
   * 
   * @param pubId 成果/文献ID.
   * @param groupId 群组ID.
   * @param folderId 文件夹ID.
   * @param articleType 资源类型.
   * @param groupPsnNode 群组节点数据.
   * @throws ServiceException
   */
  private void saveGroupRes(Long pubId, Long groupId, Long folderId, Integer articleType, GroupPsnNode groupPsnNode)
      throws ServiceException {
    GroupPubs groupPubs = new GroupPubs();
    GroupRefs groupRefs = new GroupRefs();
    if (articleType.intValue() == PublicationArticleType.OUTPUT) {
      Publication publication = this.publicationDao.get(pubId);
      groupPubs = this.wrapGroupPublication(publication, groupPubs);

    } else if (articleType.intValue() == PublicationArticleType.REFERENCE) {
      Publication publication = this.publicationDao.get(pubId);
      groupRefs = this.wrapGroupReference(publication, groupRefs);
    }
    if (articleType.intValue() == PublicationArticleType.OUTPUT) {
      groupPubs.setNodeId(ServiceConstants.SCHOLAR_NODE_ID_1);
      groupPubs.setGroupId(groupId);

      // 重新计算相关度与标注
      try {
        Integer sharePubCount = this.getPubGroupShareKwCount(groupPubs);
        Integer isLabeled = this.groupPubIsLabeled(groupPubs);
        if (sharePubCount != null) {
          groupPubs.setRelevance(sharePubCount);
        }

        if (isLabeled != null) {
          groupPubs.setLabeled(isLabeled);
        }
      } catch (Exception e) {
        logger.error("群组成果相关性更新失败， 群组Id :" + groupId + "== pubId : " + pubId, e);
      }
      this.groupPubDao.save(groupPubs);
      this.groupResourceService.reCountGroupPubs(groupId);
    } else if (articleType.intValue() == PublicationArticleType.REFERENCE) {
      groupRefs.setNodeId(ServiceConstants.SCHOLAR_NODE_ID_1);
      groupRefs.setGroupId(groupId);
      this.groupRefDao.save(groupRefs);
      this.groupResourceService.reCountGroupRefs(groupId);
    }

  }


  /**
   * 收集群组资源更新数据.
   * 
   * @param pubId
   * @param groupId
   * @param currentUserId
   * @param groupPsnNode
   */
  private void collectionGroupResData(Long pubId, Long groupId, Long currentUserId, GroupPsnNode groupPsnNode) {
    Integer nodeId = SecurityUtils.getCurrentUserNodeId();// groupPsnNode.getNodeId();
    // 群组增加成果、文献动态
    Publication pub = this.publicationDao.get(pubId);
    TaskGroupResNotify groupRes =
        new TaskGroupResNotify(pubId, groupId, currentUserId, pub.getArticleType(), 0, new Date(), nodeId);
    if ("zh".equalsIgnoreCase(LocaleContextHolder.getLocale().getLanguage())) {
      groupRes.setLocale("zh");
    } else {
      groupRes.setLocale("en");
    }

    try {
      taskGroupResNotifyDao.save(groupRes);
    } catch (Exception e) {
      logger.error("本节点nodeId={}保存群组groupId={}的成果类别资源resKey={}失败！", new Object[] {nodeId, groupId, pubId, e});
    }
  }

  /**
   * 生成群组增加资源文件的动态. 需要添加当前操作人，否则后台任务无法用
   * 
   * @param groups
   * @param resDetails
   * @param articleType
   * @param currentNodeId
   * @throws ServiceException
   */
  private void createGroupAddResDyn(String[] groups, JSONArray resDetails, Integer articleType, int currentNodeId,
      Long currenctUserId) {
    JSONObject jsonObject = null;
    // 产生动态.
    for (String groupId : groups) {
      jsonObject = new JSONObject();
      jsonObject.accumulate("resType", articleType);
      jsonObject.accumulate("sameFlag",
          articleType + "_" + currentNodeId + "_" + UUID.randomUUID().toString().replace("-", ""));
      jsonObject.accumulate("groupId", Long.valueOf(groupId));
      jsonObject.accumulate("producer", currenctUserId);
      jsonObject.accumulate("permission", 0);
      jsonObject.accumulate("resDetails", resDetails.toString());
      // 捕获生成群组增加资源文件的动态异常_MJG_SCM-6170.
      // TODO 2015-10-24 迁移动态 -done
      try {
        this.dynGroupProduceAddPubService.produceGroupDynamic(jsonObject.toString());
      } catch (Exception e) {
        logger.error("生成群组增加资源文件的动态出错：resType=" + articleType + ";groupId=" + groupId, e);
        continue;
      }
    }
  }

  private GroupPubs wrapGroupPublication(Publication publication, GroupPubs groupPubs) throws ServiceException {
    groupPubs.setPubId(publication.getId());
    groupPubs.setAuthorNames(publication.getAuthorNames());
    groupPubs.setBriefDesc(publication.getBriefDesc());
    groupPubs.setCitedDate(publication.getCitedDate());
    groupPubs.setCitedList(publication.getCitedList());
    groupPubs.setCitedInfo(publication.getCitedList());
    groupPubs.setCitedTimes(publication.getCitedTimes());
    groupPubs.setCitedUrl(publication.getCitedUrl());
    groupPubs.setEnTitle(publication.getEnTitle());
    groupPubs.setFulltextFileid(publication.getFulltextFileid());
    groupPubs.setFulltextUrl(publication.getFulltextUrl());
    groupPubs.setImpactFactors(publication.getImpactFactors());
    groupPubs.setOwnerPsnId(publication.getPsnId());
    groupPubs.setPublishDay(publication.getPublishDay());
    groupPubs.setPublishMonth(publication.getPublishMonth());
    groupPubs.setPublishYear(publication.getPublishYear());
    groupPubs.setTypeId(publication.getTypeId());
    groupPubs.setZhTitle(publication.getZhTitle());
    groupPubs.setFullTextNodeId(publication.getFulltextNodeId());
    groupPubs.setSourceDbId(publication.getSourceDbId());
    groupPubs.setFulltextExt(publication.getFulltextExt());
    groupPubs.setNodeId(BasicRmtSrvModuleConstants.SNS_MODULE_ID.intValue());
    // 同步收录情况
    this.warpSyncPubListToGroupPub(publication, groupPubs);
    return groupPubs;
  }

  private GroupRefs wrapGroupReference(Publication publication, GroupRefs groupRefs) throws ServiceException {
    groupRefs.setPubId(publication.getId());
    groupRefs.setAuthorNames(publication.getAuthorNames());
    groupRefs.setBriefDesc(publication.getBriefDesc());
    groupRefs.setCitedDate(publication.getCitedDate());
    groupRefs.setCitedInfo(publication.getCitedList());
    groupRefs.setCitedTimes(publication.getCitedTimes());
    groupRefs.setCitedUrl(publication.getCitedUrl());
    groupRefs.setEnTitle(publication.getEnTitle());
    groupRefs.setFulltextFileid(publication.getFulltextFileid());
    groupRefs.setFulltextUrl(publication.getFulltextUrl());
    groupRefs.setImpactFactors(publication.getImpactFactors());
    groupRefs.setOwnerPsnId(publication.getPsnId());
    groupRefs.setPublishDay(publication.getPublishDay());
    groupRefs.setPublishMonth(publication.getPublishMonth());
    groupRefs.setPublishYear(publication.getPublishYear());
    groupRefs.setTypeId(publication.getTypeId());
    groupRefs.setZhTitle(publication.getZhTitle());
    groupRefs.setFullTextNodeId(publication.getFulltextNodeId());
    groupRefs.setSourceDbId(publication.getSourceDbId());
    groupRefs.setFulltextExt(publication.getFulltextExt());
    groupRefs.setNodeId(BasicRmtSrvModuleConstants.SNS_MODULE_ID.intValue());
    // 同步收录情况
    this.warpSyncRefListToGroupRef(publication, groupRefs);
    return groupRefs;
  }

  private void warpSyncPubListToGroupPub(Publication publication, GroupPubs groupPubs) {
    try {
      PublicationList pubList = publicationListService.getPublicationList(publication.getId());
      if (pubList != null) {
        groupPubs.setListEi(pubList.getListEi());
        groupPubs.setListIstp(pubList.getListIstp());
        groupPubs.setListSci(pubList.getListSci());
        groupPubs.setListSsci(pubList.getListSsci());
      }
    } catch (Exception e) {
      logger.error("pubId:{},同步成果收录情况到群组出错", publication.getId(), e);
    }
  }

  private void warpSyncRefListToGroupRef(Publication publication, GroupRefs groupRefs) {
    try {
      PublicationList pubList = publicationListService.getPublicationList(publication.getId());
      if (pubList != null) {
        groupRefs.setListEi(pubList.getListEi());
        groupRefs.setListIstp(pubList.getListIstp());
        groupRefs.setListSci(pubList.getListSci());
        groupRefs.setListSsci(pubList.getListSsci());
      }
    } catch (Exception e) {
      logger.error("pubId:{},同步文献收录情况到群组出错", publication.getId(), e);
    }
  }

  @Override
  public void groupPublicationUpdate(Publication pub, Integer nodeId, Long groupId) throws ServiceException {
    try {
      GroupPsnNode groupPsnNode = groupPsnNodeDao.findGroupPnsNode(groupId);
      if (groupPsnNode != null) {
        GroupPubs groupPub = this.groupPubsService.findGroupPub(groupId, pub.getId(), groupPsnNode.getNodeId());
        groupPub = wrapGroupPublication(pub, groupPub);
        groupPub.setGroupId(groupId);
        this.groupPubsService.saveGroupPub(groupPub);
      }

    } catch (Exception e) {
      logger.error(e.getMessage() + e);
    }
  }


  @Override
  public void groupReferenceUpdate(Publication pub, Integer nodeId, Long groupId) throws ServiceException {
    try {
      GroupInvitePsnNodePk id = new GroupInvitePsnNodePk(groupId, SecurityUtils.getCurrentUserId());
      GroupInvitePsnNode groupInvitePsnNode = groupInvitePsnNodeDao.findGroupInvitePsnNode(id);
      if (groupInvitePsnNode == null) {
        return;
      }
      GroupRefs groupRef = this.groupRefsService.findGroupRef(groupId, pub.getId(), nodeId);
      groupRef = wrapGroupReference(pub, groupRef);
      groupRef.setGroupId(groupId);
      this.groupRefsService.saveGroupRef(groupRef);
      this.groupResourceService.reCountGroupPubs(groupId);
    } catch (ServiceException e) {
      logger.error(e.getMessage() + e);
    }
  }

  @Deprecated
  @Override
  public void receivePublicationRemoveGroupSync(GroupPubRemoveSyncMessage receiveMessage) throws ServiceException {
    if (receiveMessage.getArticleType() == null || receiveMessage.getGroupId() == null
        || receiveMessage.getPsnId() == null || receiveMessage.getPubId() == null
        || receiveMessage.getOpAction() == null) {
      throw new ServiceException("成果从群组移除信息不全..");
    }
    if ("mypub".equals(receiveMessage.getOpAction())) {
      if (receiveMessage.getArticleType() == PublicationArticleType.OUTPUT) {
        this.deletePublicationFromGroup(receiveMessage.getGroupId(), receiveMessage.getPsnId(),
            receiveMessage.getPubId());
      } else if (receiveMessage.getArticleType() == PublicationArticleType.REFERENCE) {
        this.deleteReferenceFromGroup(receiveMessage.getGroupId(), receiveMessage.getPsnId(),
            receiveMessage.getPubId());
      }
      // this.groupService.reCountGroupPsnSumByGroupId(receiveMessage.getGroupId());
    } else if ("mygroup".equals(receiveMessage.getOpAction())) {
      try {
        this.groupPubNodeDao.deleteGroupPubNode(receiveMessage.getGroupId(), receiveMessage.getPsnId(),
            receiveMessage.getPubId(), receiveMessage.getArticleType());
      } catch (DaoException e) {
        logger.error(e.getMessage() + e);
      }
    }
  }

  @Override
  public void deletePublicationFromGroup(Long groupId, Long psnId, Long pubId) throws ServiceException {
    this.groupPubDao.deletePub(groupId, psnId, pubId);
    this.groupResourceService.reCountGroupPubs(groupId);
  }

  @Override
  public void deleteReferenceFromGroup(Long groupId, Long psnId, Long pubId) throws ServiceException {
    this.groupRefDao.deletePub(groupId, psnId, pubId);
    this.groupResourceService.reCountGroupRefs(groupId);
  }

  @Override
  public List<GroupPubs> getGroupPubsList(Long groupId) {
    return this.groupPubDao.getGroupPubList(groupId);
  }

  @Override
  public List<String> getGroupKw(Long groupId) {
    return this.groupKeywordsDao.getGroupKwStrList(groupId);
  }

  @Override
  public Integer getShareKwCount(String pubKw, List<String> groupKws) {
    Integer count = 0;
    if (StringUtils.isEmpty(pubKw) || CollectionUtils.isEmpty(groupKws)) {
      return count;
    }

    for (String groupKw : groupKws) {
      if (StringUtils.isNotEmpty(groupKw)) {
        if (pubKw.indexOf(groupKw) != -1) {
          count++;
        }
      }
    }
    return count;
  }

  @Override
  public void saveGroupPub(GroupPubs groupPubs) {
    this.groupPubDao.save(groupPubs);
  }

  /*
   * 成果关键词与群组关键词对应的数量，标识此成果与群组的相关度
   */
  @Override
  public Integer getPubGroupShareKwCount(GroupPubs groupPub) throws Exception {
    Integer count = 0;
    Long pubId = groupPub.getPubId();
    Long groupId = groupPub.getGroupId();
    try {
      String zhKwStr = this.getKwStr(pubId, ZH_CN);
      String enKwStr = this.getKwStr(pubId, EN);
      if (StringUtils.isEmpty(zhKwStr) && StringUtils.isEmpty(enKwStr)) {
        return count;
      }
      List<String> kwStrList = this.getGroupKw(groupId);
      if (CollectionUtils.isEmpty(kwStrList)) {
        logger.debug("群组关键词为空, 群组Id :" + groupId);
        return count;
      }
      // 过滤
      zhKwStr = this.filterKwString(zhKwStr);
      enKwStr = this.filterKwString(enKwStr);
      kwStrList = this.filterKwStringList(kwStrList);
      // 获取与群组关键词相同的词数
      Integer zhCount = this.getShareKwCount(zhKwStr, kwStrList);
      Integer enCount = this.getShareKwCount(enKwStr, kwStrList);

      return zhCount + enCount;
    } catch (Exception e) {
      throw new Exception("getPubGroupShareKwCount计算群组与群组成果相同关键字出错， pubId ： " + pubId + " === " + e);
    }
  }

  /**
   * 1成果基金信息与群组对应上，已标注；0未对应上，未标注
   * 
   * 
   */
  @Override
  public Integer groupPubIsLabeled(GroupPubs groupPub) throws Exception {
    Integer isLabeled = 0;
    Long pubId = groupPub.getPubId();
    Long groupId = groupPub.getGroupId();

    try {

      String pubFundInfo = this.getPubFundInfo(pubId);
      if (StringUtils.isEmpty(pubFundInfo)) {
        return isLabeled;
      }

      String groupFundInfo = this.groupBaseInfoDao.getGroupFundInfo(groupId);
      if (StringUtils.isEmpty(groupFundInfo)) {
        return isLabeled;
      }

      // pubFundInfo不过滤标点符号
      pubFundInfo = XmlUtil.filterAuForCompare(pubFundInfo);

      // 需要过滤所有不必要的信息
      groupFundInfo = XmlUtil.filterAuForCompare(groupFundInfo);

      // 一个项目只对应一个资助基金号，而一个成果对应多个不同的资助基金。所以用项目基金号去匹配成果基金号string。
      isLabeled = this.pubIsLabeled(pubFundInfo, groupFundInfo);

      return isLabeled;
    } catch (Exception e) {
      throw new Exception("groupPubIsLabeled群组成果标记出错, pubId : " + pubId + " === " + e);
    }
  }

  @Override
  public void updateGroupPubsInfo() {
    if (taskMarkerService
        .getApplicationQuartzSettingValue("updateRelevanceAndLabeledForAllGroupPubs_remove_cache") == 1) {
      this.cacheService.remove("groupId_key_cache", "groupId");
    }

    Long groupId = (Long) this.cacheService.get("groupId_key_cache", "groupId");

    if (groupId == null) {
      groupId = 0L;
    }

    List<Long> groupIdList = this.groupBaseInfoDao.getGroupIdList(groupId, 500);
    if (CollectionUtils.isEmpty(groupIdList)) {
      logger.debug("UpdateRelevanceAndLabeledForAllGroupPubs, 获取gourpIdList为空");
      return;
    }

    Long lastGroupId = groupIdList.get(groupIdList.size() - 1);
    this.cacheService.put("groupId_key_cache", "groupId", lastGroupId);

    for (Long gId : groupIdList) {
      List<GroupPubs> groupPubList = this.getGroupPubsList(gId);
      if (CollectionUtils.isEmpty(groupPubList)) {
        continue;
      }

      for (GroupPubs groupPub : groupPubList) {
        try {
          Integer pubShareCount = this.getPubGroupShareKwCount(groupPub);
          Integer labeled = this.groupPubIsLabeled(groupPub);// 0未标注；1已标注

          if (pubShareCount != null) {
            groupPub.setRelevance(pubShareCount);
          }

          if (labeled != null) {
            groupPub.setLabeled(labeled);
          }

          this.saveGroupPub(groupPub);

        } catch (Exception e) {
          logger.error("UpdateRelevanceAndLabeledForAllGroupPubs, 群组成果相关性更新失败, 群组Id :" + groupId, e);
        }
      }
    }
  }

  public Integer pubIsLabeled(String pubFundInfo, String groupFundInfo) {
    Integer isLabeled = 0;
    // 成果可能会对应多个基金资助，而项目群组只对应一个
    if (StringUtils.isEmpty(groupFundInfo) || StringUtils.isEmpty(pubFundInfo)) {
      return isLabeled;
    }

    if (pubFundInfo.indexOf(groupFundInfo) != -1) {
      isLabeled = 1;
    }

    return isLabeled;
  }

  public String getKwStr(Long pubId, String lang) {
    String kwStr = null;
    String pubXmlStr = null;
    // 被修改的内容优先存储在pubDataStore中
    PubDataStore pubDataStore = pubDataStoreDao.get(pubId);

    if (pubDataStore == null || StringUtils.isEmpty(pubDataStore.getData())) {
      ScmPubXml pubXml = scmPubXmlDao.get(pubId);
      if (pubDataStore == null || StringUtils.isEmpty(pubXml.getPubXml())) {
        return kwStr;
      }
      pubXmlStr = pubXml.getPubXml();
    } else {
      pubXmlStr = pubDataStore.getData();
    }
    try {
      PubXmlDocument newDoc = new PubXmlDocument(pubXmlStr);
      if (ZH_CN.equals(lang)) {
        kwStr = newDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_keywords");
      }
      if (EN.equals(lang)) {
        kwStr = newDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_keywords");
      }

    } catch (Exception e) {
      logger.error("GroupPubRelevanceReCalTasklet 从xml获取关键词出错, pubId :" + pubId, e);
    }

    return kwStr;
  }

  public String getPubFundInfo(Long pubId) {
    // 优先从pubDataStore中间表中取值：成果修改的数据都是直接写入pubDataStore中
    String pubXmlStr = null;
    String fundInfoStr = null;
    PubDataStore pubDataStore = pubDataStoreDao.get(pubId);
    ScmPubXml pubXml = scmPubXmlDao.get(pubId);
    // 不影响整体成果处理流程
    try {
      if (pubDataStore == null || StringUtils.isEmpty(pubDataStore.getData())) {
        if (pubXml == null || StringUtils.isEmpty(pubXml.getPubXml())) {
          return fundInfoStr;
        }
        pubXmlStr = pubXml.getPubXml();
        PubXmlDocument newDoc = new PubXmlDocument(pubXmlStr);
        fundInfoStr = newDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "fundinfo");
      } else {
        pubXmlStr = pubDataStore.getData();
        PubXmlDocument newDoc = new PubXmlDocument(pubXmlStr);
        fundInfoStr = newDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "fundinfo");
        // 如果pubdatastore中取得的fundInfoStr还是为空，则从scmpubxml中取值
        if (StringUtils.isEmpty(fundInfoStr)) {
          if (pubXml == null || StringUtils.isEmpty(pubXml.getPubXml())) {
            return fundInfoStr;
          }
          pubXmlStr = pubXml.getPubXml();
          PubXmlDocument pubDoc = new PubXmlDocument(pubXmlStr);
          fundInfoStr = pubDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "fundinfo");
        }
      }

    } catch (Exception e) {
      logger.error("GroupPubRelevanceReCalTasklet， 从xml获取基金资助信息fundinfo出错, pubId :" + pubId, e);
    }

    return fundInfoStr;
  }

  public String filterKwString(String str) {
    if (StringUtils.isEmpty(str)) {
      return "";
    }

    str = XmlUtil.filterAuForCompare(str);
    // 过滤数字
    str = str.replaceAll("\\d+", "");
    return str;
  }

  public List<String> filterKwStringList(List<String> kwStrList) {
    List<String> strList = new ArrayList<String>();
    for (String str : kwStrList) {
      str = this.filterKwString(str);
      if (StringUtils.isEmpty(str)) {
        continue;
      }
      strList.add(str);
    }
    return strList;
  }
}
