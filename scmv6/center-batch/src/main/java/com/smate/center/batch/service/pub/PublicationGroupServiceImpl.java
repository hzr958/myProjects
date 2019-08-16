package com.smate.center.batch.service.pub;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.GroupPsnNodeDao;
import com.smate.center.batch.dao.sns.pub.GroupPubNodeDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.GroupPsnNode;
import com.smate.center.batch.model.sns.pub.GroupPubNode;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.model.sns.pub.Publication;
import com.smate.center.batch.service.pub.mq.GroupPubRemoveSyncMessage;
import com.smate.center.batch.service.pub.mq.GroupPublicationSyncProducer;
import com.smate.center.batch.util.pub.PublicationArticleType;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 在成果端（不在群组功能中的成果操作）成果与群组的关系模块.
 * 
 * <pre>
 * 	<li>成果被删除时对群组的影响.</li>
 * 	<li>成果被编辑时对群组的影响.</li>
 * </pre>
 * 
 * @author LY
 * 
 */
@Service("publicationGroupService")
@Transactional(rollbackFor = Exception.class)
public class PublicationGroupServiceImpl implements PublicationGroupService {
  private static Logger logger = LoggerFactory.getLogger(PublicationGroupServiceImpl.class);
  @Autowired
  private GroupPublicationService groupPublicationService;
  @Autowired
  private GroupPubNodeDao groupPubNodeDao;
  @Autowired
  private GroupPsnNodeDao groupPsnNodeDao;
  @Autowired
  private PublicationStatisticsService publicationStatisticsService;
  @Autowired
  private GroupPublicationSyncProducer groupPublicationSyncProducer;
  @Autowired
  private GrpPubsService grpPubsService;

  /**
   * 添加成果记录到群组(同时生成动态记录)_MJG_SCM-6197. 用于后台任务，添加psnId
   */
  @Override
  public void addPublicationToGroup(Long psnId, String pubId, String groupId, Integer articleType, Long groupFolderId,
      int dynFlag) throws ServiceException {
    // this.groupPublicationService.addPublicationToGroup(psnId, pubId,
    // groupId,articleType, groupFolderId, dynFlag);
    // 新群组成果
    this.grpPubsService.addPublicationToGroup(psnId, pubId, groupId, articleType, groupFolderId, dynFlag);
  }

  /**
   * 添加成果记录到群组(同时生成动态记录)_MJG_SCM-6197.
   */
  @Override
  public void addPublicationToGroup(String pubId, String groupId, Integer articleType, Long groupFolderId, int dynFlag)
      throws ServiceException {
    this.groupPublicationService.addPublicationToGroup(pubId, groupId, articleType, groupFolderId, dynFlag);
  }

  @Override
  public void delPublication(Long pubId, Long ownerPsnId) throws ServiceException {
    List<GroupPubNode> groupPubNodes = null;
    try {
      groupPubNodes = this.groupPubNodeDao.getGroupPubsByPubId(pubId, ownerPsnId);

      if (CollectionUtils.isNotEmpty(groupPubNodes)) {
        for (GroupPubNode groupPubNode : groupPubNodes) {
          // FIXME 2015-10-29 取消MQ -done
          GroupPubRemoveSyncMessage message = new GroupPubRemoveSyncMessage();
          GroupPsnNode groupPsnNode = this.groupPsnNodeDao.findGroupPnsNode(groupPubNode.getGroupId());
          if (groupPsnNode == null) {
            logger.error("请跟踪该错误,是否同步未到位." + "groupId:" + groupPubNode.getGroupId() + "  pubId:"
                + groupPubNode.getPubId() + " psnId:" + groupPubNode.getPsnId());
            continue;
          }
          message.setArticleType(groupPubNode.getArticleType());
          message.setGroupId(groupPubNode.getGroupId());
          message.setNodeId(groupPsnNode.getNodeId());
          message.setOpAction("mypub");
          message.setPsnId(groupPubNode.getPsnId());
          message.setPubId(pubId);
          this.groupPublicationSyncProducer.publicationRemoveGroupSync(message);
        }
      }
      this.groupPubNodeDao.deleteGroupPubNode(ownerPsnId, pubId);
    } catch (DaoException e) {
      logger.error(e.getMessage() + e);
    }
    // 清除统计缓存
    // FIXME 2015-10-29 Cache
    // publicationStatisticsService.clearAllPubStatistic(ownerPsnId,
    // PublicationArticleType.OUTPUT);
    // publicationStatisticsService.clearAllPubStatistic(ownerPsnId,
    // PublicationArticleType.REFERENCE);

  }

  @Override
  public void savePublicationEdit(Publication pub, PubXmlProcessContext context) throws ServiceException {
    List<GroupPubNode> groupPubNodes = null;
    try {
      groupPubNodes = this.groupPubNodeDao.getGroupPubsByPubId(pub.getId(), pub.getPsnId());
    } catch (DaoException e) {
      logger.error(e.getMessage() + e);
    }
    if (CollectionUtils.isNotEmpty(groupPubNodes)) {
      for (GroupPubNode groupPubNode : groupPubNodes) {
        if (groupPubNode.getArticleType() == PublicationArticleType.OUTPUT) {
          this.groupPublicationService.groupPublicationUpdate(pub, SecurityUtils.getCurrentAllNodeId().get(0),
              groupPubNode.getGroupId());
        } else if (groupPubNode.getArticleType() == PublicationArticleType.REFERENCE) {
          this.groupPublicationService.groupReferenceUpdate(pub, SecurityUtils.getCurrentAllNodeId().get(0),
              groupPubNode.getGroupId());
        }

      }
    }

  }

}
