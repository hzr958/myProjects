package com.smate.center.batch.service.rol.pub;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.rol.pub.PubDupRolMapDao;
import com.smate.center.batch.dao.rol.pub.PublicationRolDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.ClearTaskNoticeEvent;
import com.smate.center.batch.model.rol.pub.ClearTaskNoticeUserInfo;
import com.smate.center.batch.model.rol.pub.PublicationRol;
import com.smate.center.batch.model.sns.pub.PubDupFields;
import com.smate.center.batch.service.pub.PubDupService;
import com.smate.core.base.utils.constant.ScmRolRoleConstants;

/**
 * 查重service.
 * 
 * 
 */
@Service("pubRolDupHandlerService")
@Transactional(rollbackFor = Exception.class)
public class PubRolDupHandlerServiceImpl implements PubRolDupHandlerService {

  // logger.
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PublicationRolDao publicationRolDao;
  @Autowired
  private PubDupRolMapDao pubDupRolMapDao;
  @Autowired
  private PublicationRolService publicationRolService;
  @Autowired
  private TaskNoticeService taskNoticeService;
  // @Resource(name = "publicationDupRolService")
  // private PublicationDupRolService publicationDupRolService;
  @Autowired
  private PubDupService pubDupService;
  @Autowired
  private PublicationRolStatusTransService publicationRolStatusTransService;

  @Override
  public void check(long insId, long pubId) throws ServiceException {
    try {

      PublicationRol mainPub = publicationRolService.getPublicationById(pubId);
      if (mainPub != null) {
        Long oldGroupId = mainPub.getDupGroupId();
        Long newGroupId = null;
        List<Long> dupPubIds = pubDupService.getDupPubIds(pubId);
        // 未查询到重复,清空
        if (CollectionUtils.isEmpty(dupPubIds)) {
          // 之前就不存在分组，退出
          if (oldGroupId == null) {
            return;
          }
          this.pubDupRolMapDao.saveDupResult(mainPub.getId(), null);
          // 检查oldGroupId里的成果总数是否还>=2,不满足则删除该组
          this.publicationRolDao.updateDupGroup(oldGroupId);
          // 之前不存在分组
        } else {
          List<PublicationRol> dupPubs = publicationRolService.getPublicationById(dupPubIds, insId);
          logger.debug("找到{}条成果和{}重复", dupPubs.size(), pubId);
          // 重复成果ID
          List<Long> dupPubsIds = new ArrayList<Long>();
          // 检查结果列表是否存在组别，如果存在，需要合并到一个分组.
          List<Long> dupPubGroups = new ArrayList<Long>();
          for (PublicationRol pub : dupPubs) {
            if (pub.getDupGroupId() != null) {
              dupPubGroups.add(pub.getDupGroupId());
            }
            dupPubsIds.add(pub.getId());
          }
          newGroupId = dupPubGroups.size() > 0 ? dupPubGroups.get(0) : pubDupRolMapDao.getNextGroupId();
          // 保存结果.
          logger.debug("成果{}进入组{}", pubId, newGroupId);
          dupPubsIds.add(pubId);
          this.pubDupRolMapDao.addPubToGroup(dupPubsIds, newGroupId);
          // 检查oldGroupId里的成果总数是否还>=2,不满足则删除该组
          if (oldGroupId != null && !dupPubGroups.contains(dupPubGroups)) {
            this.publicationRolDao.updateDupGroup(oldGroupId);
          }
        }
        // 清理缓存中的任务统计数
        taskNoticeService.clearTaskNotice(ClearTaskNoticeEvent.getInstance(insId, 0, 1, 0),
            ClearTaskNoticeUserInfo.getInstance(insId, ScmRolRoleConstants.INS_RO, null));
      } else {
        logger.error("成果{0}查重错误:该成果不存在.", pubId);
      }

    } catch (DaoException e) {
      logger.error("成果{0}查重错误:不在查重队列中.", pubId);
    }
  }

  @Override
  public void loadQueuePubsIntoMQ() throws ServiceException {

  }

  @Override
  public void deleteDupTempPub(long insId, long pubId) throws ServiceException {
    try {
      Map<Integer, List<PubDupFields>> dupMap =
          pubDupService.getDupPubField(pubId, PubDupFields.INS_NOT_CONFIRM_STATUS);
      if (dupMap.containsKey(1)) {
        List<PubDupFields> dupList = dupMap.get(1);
        if (CollectionUtils.isNotEmpty(dupList)) {
          for (PubDupFields pubDup : dupList) {
            publicationRolStatusTransService.delete(pubDup.getPubId(), "deleteDupTempPub");
          }
        }
      }
    } catch (Exception e) {
      logger.error("成果查重，并删除重复的临时库成果出现错误，insId=" + insId + ",pubId=" + pubId, e);
      throw new ServiceException("成果查重，并删除重复的临时库成果出现错误，insId=" + insId + ",pubId=" + pubId, e);
    }
  }

}
