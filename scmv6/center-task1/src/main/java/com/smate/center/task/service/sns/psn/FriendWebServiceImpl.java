package com.smate.center.task.service.sns.psn;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.quartz.Friend;

@Service("friendWebService")
@Transactional(rollbackFor = Exception.class)
public class FriendWebServiceImpl implements FriendWebService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private FriendService friendService;

  @Override
  public int getMatchPsnFriendsCount(Long psnId, Long tempPsnId) throws ServiceException {
    int matchPsnFriendsCount = 0;
    try {
      List<Friend> friends = friendService.findFriend(psnId);
      if (CollectionUtils.isNotEmpty(friends)) {
        for (Friend friend : friends) {
          boolean isPsnFriend = friendService.isPsnFirend(friend.getFriendPsnId(), tempPsnId);
          if (isPsnFriend)
            matchPsnFriendsCount++;
        }
      }
      return matchPsnFriendsCount;
    } catch (Exception e) {
      logger.error("tempPsnId:{}匹配psnId:{}好友的好友个数出错", new Object[] {tempPsnId, psnId}, e);
      return 1;
    }
  }
}
