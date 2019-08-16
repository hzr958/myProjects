package com.smate.center.batch.service.pub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.GroupPubDao;
import com.smate.center.batch.dao.sns.pub.GroupRefDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.GroupStatistics;

/**
 * 群组资源接口实现类.
 * 
 * @author zhuagnyanming
 * 
 */
@Service("groupResourceService")
@Transactional(rollbackFor = Exception.class)
public class GroupResourceServiceImpl implements GroupResourceService {

  /**
   * 
   */
  private static final long serialVersionUID = -4374150989206223837L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private GroupPubDao groupPubDao;
  @Autowired
  private GroupRefDao groupRefDao;
  @Autowired
  private GroupService groupService;
  @Autowired
  private GroupPsnSearchService groupPsnSearchService;
  @Autowired
  private GroupPsnEditService groupPsnEditService;

  @Override
  public void reCountGroupPubs(Long groupId) throws ServiceException {

    try {
      // 修改了保存群组统计信息的逻辑_MJG_SCM-6000.
      // GroupPsn groupPsn = groupPsnDao.findMyGroup(groupId);// 统计群组成果
      GroupStatistics statistics = groupPsnSearchService.getStatistics(groupId);
      if (statistics != null) {
        Integer sumPubs = groupPubDao.sumGroupPubs(groupId, null);
        Integer sumPubsNfolder = groupPubDao.sumGroupPubs(groupId, -1L);
        statistics.setSumPubs(sumPubs);
        statistics.setSumPubsNfolder(sumPubsNfolder);
        // groupPsnDao.save(groupPsn);

        groupPsnEditService.saveStatistics(statistics);
        groupService.toSendSyncGroupInvitePsn(groupId);// 同步群组信息到群组成员
      }

    } catch (Exception e) {
      logger.error("统计群组成果出错", e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void reCountGroupRefs(Long groupId) throws ServiceException {

    try {
      // 修改了保存群组统计信息的逻辑_MJG_SCM-6000.
      // GroupPsn groupPsn = groupPsnDao.findMyGroup(groupId);// 统计群组文献
      GroupStatistics statistics = groupPsnSearchService.getStatistics(groupId);
      if (statistics != null) {
        Integer sumRefs = groupRefDao.sumGroupRefs(groupId, null);
        Integer sumRefsNfolder = groupRefDao.sumGroupRefs(groupId, -1L);
        statistics.setSumRefs(sumRefs);
        statistics.setSumRefsNfolder(sumRefsNfolder);
        // groupPsnDao.save(groupPsn);
        groupPsnEditService.saveStatistics(statistics);

        groupService.toSendSyncGroupInvitePsn(groupId);// 同步群组信息到群组成员
      }

    } catch (Exception e) {
      logger.error("统计群组文献出错", e);
      throw new ServiceException(e);
    }

  }

}
