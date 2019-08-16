package com.smate.center.merge.service.task.del;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.merge.dao.friend.FriendDao;
import com.smate.center.merge.model.sns.friend.Friend;
import com.smate.center.merge.model.sns.task.AccountsMergeData;
import com.smate.center.merge.service.task.MergeBaseService;

/**
 * 删除PSN_FRIEND个人好友信息服务.
 * 
 * @author yhx
 *
 * @date 2019年2月26日
 */
@Transactional(rollbackFor = Exception.class)
public class MergeDelPsnFriendServiceImpl extends MergeBaseService {
  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private FriendDao friendDao;

  @Override
  public boolean checkRun(Long savePsnId, Long delPsnId) throws Exception {
    List<Friend> friendList = friendDao.findByFriendPsnId(delPsnId);
    if (friendList != null && friendList.size() > 0) {
      return true;
    }
    return false;
  }

  @Override
  public boolean dealMerge(Long savePsnId, Long delPsnId) throws Exception {
    try {
      List<Friend> friendList = friendDao.findByFriendPsnId(delPsnId);
      for (Friend friend : friendList) {
        try {
          String desc = "删除个人好友信息 psn_friend ";
          AccountsMergeData accountsMergeData =
              super.saveAccountsMergeData(savePsnId, delPsnId, desc, super.DEAL_TYPE_DEL, friend);
          friendDao.delete(friend);
          super.updateAccountsMergeDataStatus(accountsMergeData);
        } catch (Exception e) {
          logger.error("帐号合并->删除个人好友信息出错 psn_friend , psnFriend=[" + friend + "], savePsnId=" + savePsnId + ",delPsnId="
              + delPsnId, e);
          throw new Exception("帐号合并->删除个人好友信息出错 psn_friend , psnFriend=[" + friend + "], savePsnId=" + savePsnId
              + ",delPsnId=" + delPsnId, e);
        }
      }
    } catch (Exception e) {
      logger.error("合并帐号->删除个人好友信息出错 psn_friend ,  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
      throw new Exception("合并帐号->删除个人好友信息出错 psn_friend ,  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
    }
    return true;
  }
}
