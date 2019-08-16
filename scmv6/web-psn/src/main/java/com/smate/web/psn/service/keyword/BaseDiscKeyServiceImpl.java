package com.smate.web.psn.service.keyword;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.psn.dao.keywork.KeyFriendVoteDao;
import com.smate.web.psn.dao.keywork.KeyFriendVoteRefreshDao;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.exception.ServiceException;

@Service("baseDiscKeyService")
@Transactional(rollbackFor = Exception.class)
public class BaseDiscKeyServiceImpl implements BaseDiscKeyService {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private KeyFriendVoteDao keyFriendVoteDao;

  @Autowired
  private KeyFriendVoteRefreshDao keyFriendVoteRefreshDao;

  @Override
  public void deleteSameVoteRecord(Long psnId, Long friendPsnId) throws ServiceException {
    try {
      keyFriendVoteDao.deleteKeyFriendVotesByPsn(psnId, friendPsnId);
      keyFriendVoteRefreshDao.deleteRefreshRecord(psnId, friendPsnId);
    } catch (DaoException e) {
      logger.error("删除相同关键词投票记录, psnId=" + psnId + ", friendId=" + friendPsnId, e);
      throw new ServiceException(e);
    }
  }
}
