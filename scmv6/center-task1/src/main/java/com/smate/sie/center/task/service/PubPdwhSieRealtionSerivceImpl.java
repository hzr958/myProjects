package com.smate.sie.center.task.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.sie.center.task.dao.PubPdwhSieRealtionDao;
import com.smate.sie.center.task.model.PubPdwhSieRelation;

/**
 * 查询PUB_PDWH_SIE_RELATION 表
 * 
 * @author ztg
 *
 */
@Service("pubPdwhSieRealtionSerivce")
@Transactional(rollbackFor = Exception.class)
public class PubPdwhSieRealtionSerivceImpl implements PubPdwhSieRealtionSerivce {

  @Autowired
  private PubPdwhSieRealtionDao pubPdwhSieRealtionDao;

  @Override
  public PubPdwhSieRelation getBySiePubId(Long siePubId) {
    return pubPdwhSieRealtionDao.get(siePubId);
  }

}
