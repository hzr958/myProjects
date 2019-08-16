package com.smate.center.open.service.friend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.friend.FriendDao;

/**
 * 好友接口实现类.
 * 
 * @author lichangwen
 * 
 */
@Service("friendService")
@Transactional(rollbackFor = Exception.class)
public class FriendServiceImpl implements FriendService {

  @Autowired
  private FriendDao friendDao;

  @Override
  public boolean isPsnFirend(Long curPsnId, Long psnId) throws Exception {
    try {
      Long count = friendDao.isFriend(curPsnId, psnId);
      if (count != null && count > 0)
        return true;
      else
        return false;
    } catch (Exception e) {
      throw new Exception(String.format("查询传入的psnId=%s,是否是登录者好友出错", psnId), e);
    }
  }
}
