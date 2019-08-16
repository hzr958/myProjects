package com.smate.web.v8pub.service.sns;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.v8pub.dao.sns.PubModifyHistoryDao;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubModifyHistory;

/**
 * 个人库成果修改历史记录服务实现类
 * 
 * @author yhx
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PubModifyHistoryServiceImpl implements PubModifyHistoryService {
  @Autowired
  private PubModifyHistoryDao pubModifyHistoryDao;

  @Override
  public PubModifyHistory findListByPubIdAndPsnId(Long pubId, Long psnId) throws ServiceException {
    PubModifyHistory pubHistory = pubModifyHistoryDao.findListByPubIdAndPsnId(pubId, psnId);
    return pubHistory;
  }

  @Override
  public void savePubModifyHistory(PubModifyHistory pubModifyHistory) throws ServiceException {
    pubModifyHistoryDao.save(pubModifyHistory);

  }

}
