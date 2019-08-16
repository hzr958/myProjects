package com.smate.web.mobile.v8pub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.dao.PsnPubDAO;
import com.smate.core.base.psn.dao.psncnf.PsnConfigDao;
import com.smate.core.base.psn.dao.psncnf.PsnConfigPubDao;

@Service
@Transactional(rollbackFor = Exception.class)
public class PubDataDetailsServiceImpl implements PubDataDetailsService {

  @Autowired
  private PsnPubDAO psnPubDao;

  @Autowired
  private PsnConfigPubDao psnConfigPubDao;

  @Autowired
  private PsnConfigDao psnConfigDao;

  @Override
  public Long getOwnerPsnId(Long pubId) throws Exception {
    // TODO Auto-generated method stub
    return psnPubDao.getOwnerPsnId(pubId);
  }

  @Override
  public Integer getAnyUser(Long pubId, Long cnfId) throws Exception {
    // TODO Auto-generated method stub
    return psnConfigPubDao.getAnyUser(cnfId, pubId);
  }

  @Override
  public Long getCnfId(Long psnId) {
    // TODO Auto-generated method stub
    return psnConfigDao.getPsnConfId(psnId);
  }

}
