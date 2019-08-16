package com.smate.web.v8pub.service.publics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.v8pub.dao.publics.PubFulltextUploadLogDao;
import com.smate.web.v8pub.po.PubFulltextUploadLog;

@Service(value = "pubfulltextUploadService")
@Transactional(rollbackFor = Exception.class)
public class PubfulltextUploadServiceImpl implements PubfulltextUploadService {
  @Autowired
  private PubFulltextUploadLogDao pubFulltextUploadLogDao;

  @Override
  public void saveOrUpdate(PubFulltextUploadLog pubFulltextUploadLog) {
    pubFulltextUploadLogDao.saveOrUpdate(pubFulltextUploadLog);
  }

  @Override
  public PubFulltextUploadLog getUploadLog(Long pdwhPubId) {
    return pubFulltextUploadLogDao.getUploadLog(pdwhPubId);
  }

  @Override
  public PubFulltextUploadLog getSnsUploadLog(Long snsPubId) {
    return pubFulltextUploadLogDao.getSnsUploadLog(snsPubId);
  }

}
