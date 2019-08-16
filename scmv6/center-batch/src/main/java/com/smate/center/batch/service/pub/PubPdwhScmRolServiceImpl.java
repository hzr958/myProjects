package com.smate.center.batch.service.pub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.PubPdwhScmRolDao;
import com.smate.center.batch.model.sns.pub.PubPdwhScmRol;

@Service("pubPdwhScmRolService")
@Transactional(rollbackFor = Exception.class)
public class PubPdwhScmRolServiceImpl implements PubPdwhScmRolService {

  @Autowired
  private PubPdwhScmRolDao pubPdwhScmRolDao;

  @Override
  public void saveTmpPdwhPub(Long pubId, int from) throws Exception {
    pubPdwhScmRolDao.save(new PubPdwhScmRol(pubId, from, 0, 0));
  }

}
