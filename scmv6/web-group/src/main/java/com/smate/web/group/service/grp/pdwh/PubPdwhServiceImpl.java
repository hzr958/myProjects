package com.smate.web.group.service.grp.pdwh;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.enums.PubPdwhStatusEnum;
import com.smate.web.group.dao.grp.pub.PubPdwhDAO;

@Service
@Transactional
public class PubPdwhServiceImpl implements PubPdwhService {

  @Autowired
  private PubPdwhDAO PubPdwhDao;

  @Override
  public boolean checkPdwhIsDel(Long pubId, PubPdwhStatusEnum status) {
    // TODO Auto-generated method stub
    return PubPdwhDao.checkPdwhIsDel(pubId, status);
  }

}
