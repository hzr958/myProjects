package com.smate.web.psn.service.dynamic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.psn.dao.dynamic.DynamicDao;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.exception.ServiceException;

@Service("dynamicService")
@Transactional(rollbackFor = Exception.class)
public class DynamicServiceImpl implements DynamicService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private DynamicDao dynamicDao;

  @Override
  public void minusFriendVisible(Long psnId, Long friendId) throws ServiceException {
    // 1 -> 4;3 -> 2
    // 修改动态关系.
    this.syncRelation(0, psnId, friendId);
    // 修改对方动态关系.
    this.syncRelation(0, friendId, psnId);
  }

  @Override
  public void syncRelation(int syncFlag, Long producer, Long receiver) throws ServiceException {
    try {
      if (syncFlag == 1) {
        this.dynamicDao.updateDynamicRelation(2, 3, producer, receiver);
        this.dynamicDao.updateDynamicRelation(4, 1, producer, receiver);
      } else {
        this.dynamicDao.updateDynamicRelation(3, 2, producer, receiver);
        this.dynamicDao.updateDynamicRelation(1, 4, producer, receiver);
      }
    } catch (DaoException e) {
      logger.error("修改" + producer + "和" + receiver + "的动态关系时出错啦！", e);
      throw new ServiceException(e);
    }

  }

}
