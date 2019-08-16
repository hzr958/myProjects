package com.smate.center.batch.service.pub.mq;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.batch.enums.pub.SnsSyncGroupStatisActionEnum;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.GroupPsn;

/**
 * 群组统计信息同步
 * 
 * @author zyx
 * 
 */
@Component("syncGroupStatisticsProducer")
public class SnsSyncGroupStatisticsProducer {

  @Autowired
  private SyncGroupStatisticsConsumer syncGroupStatisticsConsumer;
  protected final Logger logger = LoggerFactory.getLogger(getClass());


  /**
   * 群组信息和成员有变动，同步到ROL(合作分析).
   * 
   * @param g
   * @param memberPsnIdList
   * @throws ServiceException
   */
  public void syncForAllGroupUpdateToRol(GroupPsn g, List<Long> memberPsnIdList) throws ServiceException {
    try {
      if (g != null) {
        Integer sumBiz = g.getSumPubs() + g.getSumRefs() + g.getSumFiles();
        // 群组活动暂时没有这个功能（设为0）
        SnsSyncGroupStatisticsMessage message = new SnsSyncGroupStatisticsMessage(g.getGroupId(), g.getGroupNodeId(),
            g.getGroupName(), g.getGroupCategory(), g.getCreateDate(), g.getSumMembers(), 0, sumBiz, g.getVisitCount(),
            memberPsnIdList, SnsSyncGroupStatisActionEnum.SYNC_ALL_UPDATE);

        syncGroupStatisticsConsumer.receive(message);
      }
    } catch (Exception e) {
      logger.error("群组修改，同步信息到ROL(合作分析)失败！", e);
      throw new ServiceException();
    }
  }

  /**
   * 删除群组，同步到ROL(合作分析).
   * 
   * @param g
   * @param memberList
   * @throws ServiceException
   */
  public void syncForGroupDelToRol(Long groupId, Integer nodeId) throws ServiceException {
    try {
      if (groupId != null && groupId > 0) {
        SnsSyncGroupStatisticsMessage message =
            new SnsSyncGroupStatisticsMessage(groupId, nodeId, SnsSyncGroupStatisActionEnum.SYNC_GROUP_DEL);
        syncGroupStatisticsConsumer.receive(message);
      }

    } catch (Exception e) {
      logger.error("删除群组，同步信息到ROL(合作分析)失败！", e);
      throw new ServiceException();
    }
  }
}
