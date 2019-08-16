package com.smate.center.task.service.sns.psn;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.quartz.FriendDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.quartz.Friend;

@Service("friendService")
@Transactional(rollbackFor = Exception.class)
public class FriendServiceImpl implements FriendService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private FriendDao friendDao;

  @Override
  public List<Friend> findFriendByAutoRecommend(List<Long> psnIds) throws ServiceException {
    return friendDao.findFriendByAutoRecommend(psnIds);
  }

  @Override
  public List<Friend> findRecommendFriend(Long psnId, Long freindPsnId) throws ServiceException {
    return friendDao.findFriendAutoRecommend(psnId, freindPsnId);
  }

  @Override
  public List<Friend> findFriend(Long psnId) throws ServiceException {
    return friendDao.findFriendAutoRecommend(psnId);
  }

  @Override
  public boolean isPsnFirend(Long curPsnId, Long psnId) throws ServiceException {
    try {
      Long count = friendDao.isFriend(curPsnId, psnId);
      if (count != null && count > 0)
        return true;
      else
        return false;
    } catch (Exception e) {
      throw new ServiceException(String.format("查询传入的psnId=%s,是否是登录者好友出错", psnId), e);
    }
  }

  /**
   * 获取某人所有好友psnid
   */

  @Override
  public List<Long> findFriendPsnId(Long psnId) throws ServiceException {
    try {
      return friendDao.findFriend(psnId);
    } catch (Exception e) {
      logger.error("获取某人所有好友psnid出错", e);
      throw new ServiceException(e);
    }

  }
}
