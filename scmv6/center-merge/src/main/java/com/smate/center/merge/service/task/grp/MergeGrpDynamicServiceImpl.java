package com.smate.center.merge.service.task.grp;

import com.smate.center.merge.dao.group.GroupDynamicAwardsDao;
import com.smate.center.merge.dao.group.GroupDynamicCommentsDao;
import com.smate.center.merge.dao.group.GroupDynamicMsgDao;
import com.smate.center.merge.dao.group.GroupDynamicShareDao;
import com.smate.center.merge.model.sns.group.GroupDynamicAwards;
import com.smate.center.merge.model.sns.group.GroupDynamicComments;
import com.smate.center.merge.model.sns.group.GroupDynamicMsg;
import com.smate.center.merge.model.sns.group.GroupDynamicShare;
import com.smate.center.merge.model.sns.task.AccountsMergeData;
import com.smate.center.merge.service.task.MergeBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 群组合并->群组动态相关服务.
 * 
 * @author ajb
 *
 */
@Transactional(rollbackFor = Exception.class)
public class MergeGrpDynamicServiceImpl extends MergeBaseService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private GroupDynamicAwardsDao groupDynamicAwardsDao;
  @Autowired
  private GroupDynamicCommentsDao groupDynamicCommentsDao;
  @Autowired
  private GroupDynamicMsgDao groupDynamicMsgDao;
  @Autowired
  private GroupDynamicShareDao groupDynamicShareDao;

  @Override
  public boolean checkRun(Long savePsnId, Long delPsnId) throws Exception {
    return true;
  }

  /**
   * 处理群组统计表表.
   */
  @Override
  public boolean dealMerge(Long savePsnId, Long delPsnId) throws Exception {
    try {
      // 更新动态赞
      dealDynamicAward(savePsnId, delPsnId);
      // 更新动态评论
      dealDynamicCommnet(savePsnId, delPsnId);
      // 更新动态消息
      dealDynamicMsg(savePsnId, delPsnId);
      // 更新动态分享
      dealDynamicShare(savePsnId, delPsnId);
    } catch (Exception e) {
      logger.error("帐号合并->群组合并->群组动态  出错 savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
      throw new Exception("帐号合并->群组合并->群组动态    出错  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
    }
    return true;
  }

  /**
   * 处理动态赞.
   */
  private void dealDynamicAward(Long savePsnId, Long delPsnId) throws Exception {
    List<GroupDynamicAwards> list = groupDynamicAwardsDao.getListByPsnId(delPsnId);
    if (list == null || list.size() == 0) {
      return;
    }
    for (GroupDynamicAwards awards : list) {
      try {
        // 保存备份记录
        String desc = "合并群组 ,处理动态赞   动态赞id   V_GROUP_DYNAMIC_AWARDS ";
        AccountsMergeData accountsMergeData =
            super.saveAccountsMergeData(savePsnId, delPsnId, desc, super.DEAL_TYPE_MERGE, awards);
        // 修改赞人员
        awards.setAwardPsnId(savePsnId);
        groupDynamicAwardsDao.save(awards);
        // 更新备份数据状态
        super.updateAccountsMergeDataStatus(accountsMergeData);
      } catch (Exception e) {
        logger.error(
            "帐号合并->群组合并->处理动态赞   动态赞id =" + awards.getId() + ", savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
        throw new Exception(
            "帐号合并->群组合并->处理动态赞   动态赞id =" + awards.getId() + ", savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
      }
    }
  }

  /**
   * 处理动态消息.
   * 
   */
  private void dealDynamicMsg(Long savePsnId, Long delPsnId) throws Exception {
    List<GroupDynamicMsg> list = groupDynamicMsgDao.getListByPsnId(delPsnId);
    if (list == null || list.size() == 0) {
      return;
    }
    for (GroupDynamicMsg msg : list) {
      try {
        // 保存备份记录
        String desc = "合并群组 ,处理动态赞   动态消息 id   V_GROUP_DYNAMIC_MSG ";
        AccountsMergeData accountsMergeData =
            super.saveAccountsMergeData(savePsnId, delPsnId, desc, super.DEAL_TYPE_MERGE, msg);
        // 修改赞人员
        msg.setProducer(savePsnId);
        groupDynamicMsgDao.save(msg);
        // 更新备份数据状态
        super.updateAccountsMergeDataStatus(accountsMergeData);
      } catch (Exception e) {
        logger.error(
            "帐号合并->群组合并->处理动态赞    动态消息  动态id =" + msg.getDynId() + ", savePsnId=" + savePsnId + ",delPsnId=" + delPsnId,
            e);
        throw new Exception("帐号合并->群组合并->处理动态赞    动态消息     动态id =" + msg.getDynId() + ", savePsnId=" + savePsnId
            + ",delPsnId=" + delPsnId, e);
      }
    }
  }

  /**
   * 处理动态评论.
   * 
   */
  private void dealDynamicCommnet(Long savePsnId, Long delPsnId) throws Exception {
    List<GroupDynamicComments> list = groupDynamicCommentsDao.getListByPsnId(delPsnId);
    if (list == null || list.size() == 0) {
      return;
    }
    for (GroupDynamicComments comment : list) {
      try {
        // 保存备份记录
        String desc = "合并群组 ,处理动态赞   动态评论 id   V_GROUP_DYNAMIC_COMMENTS ";
        AccountsMergeData accountsMergeData =
            super.saveAccountsMergeData(savePsnId, delPsnId, desc, super.DEAL_TYPE_MERGE, comment);
        // 修改赞人员
        comment.setCommentPsnId(savePsnId);
        groupDynamicCommentsDao.save(comment);
        // 更新备份数据状态
        super.updateAccountsMergeDataStatus(accountsMergeData);
      } catch (Exception e) {
        logger.error(
            "帐号合并->群组合并->处理动态赞    动态评论 id =" + comment.getId() + ", savePsnId=" + savePsnId + ",delPsnId=" + delPsnId,
            e);
        throw new Exception(
            "帐号合并->群组合并->处理动态赞    动态评论 id =" + comment.getId() + ", savePsnId=" + savePsnId + ",delPsnId=" + delPsnId,
            e);
      }
    }
  }

  /**
   * 处理动态分享.
   * 
   */
  private void dealDynamicShare(Long savePsnId, Long delPsnId) throws Exception {
    List<GroupDynamicShare> list = groupDynamicShareDao.getListByPsnId(delPsnId);
    if (list == null || list.size() == 0) {
      return;
    }
    for (GroupDynamicShare share : list) {
      try {
        // 保存备份记录
        String desc = "合并群组 ,处理动态赞   动态分享 id   V_GROUP_DYNAMIC_COMMENTS ";
        AccountsMergeData accountsMergeData =
            super.saveAccountsMergeData(savePsnId, delPsnId, desc, super.DEAL_TYPE_MERGE, share);
        // 修改赞人员
        share.setSharePsnId(savePsnId);
        groupDynamicShareDao.save(share);
        // 更新备份数据状态
        super.updateAccountsMergeDataStatus(accountsMergeData);
      } catch (Exception e) {
        logger.error(
            "帐号合并->群组合并->处理动态赞    动态分享 id =" + share.getId() + ", savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
        throw new Exception(
            "帐号合并->群组合并->处理动态赞    动态分享 id =" + share.getId() + ", savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
      }
    }
  }
}
