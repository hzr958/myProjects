package com.smate.center.task.service.pdwh.quartz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.pub.PublicationPdwhDao;
import com.smate.center.task.exception.ServiceException;

/**
 * 成果基准库id.
 * 
 * @author liqinghua
 * 
 */
@Service("publicationPdwhService")
@Transactional(rollbackFor = Exception.class)
public class PublicationPdwhServiceImpl implements PublicationPdwhService {

  /**
   * 
   */
  private static final long serialVersionUID = -2003867995465796950L;
  @Autowired
  private PublicationPdwhDao publicationPdwhDao;

  @Override
  public int getPubPdwhIdByPsnFriend(Long psnId, Long pdwhPubId, int dbid) throws ServiceException {
    return publicationPdwhDao.getPubPdwhIdByPsnFriend(psnId, pdwhPubId, dbid);
  }



}
