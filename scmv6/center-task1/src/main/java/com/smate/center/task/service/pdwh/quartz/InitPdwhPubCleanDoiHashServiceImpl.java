package com.smate.center.task.service.pdwh.quartz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.pdwh.quartz.PdwhPubCleanDOIDao;
import com.smate.center.task.single.dao.solr.PdwhPubDupDao;
import com.smate.core.base.utils.file.FileUtils;
import com.smate.core.base.utils.pubHash.PubHashUtils;

@Service("initPdwhPubCleanDoiHashService")
@Transactional(rollbackFor = Exception.class)
public class InitPdwhPubCleanDoiHashServiceImpl implements InitPdwhPubCleanDoiHashService {
  @Autowired
  private PdwhPubCleanDOIDao pdwhPubCleanDOIDao;
  @Autowired
  private PdwhPubDupDao pdwhPubDupDao;

  @Override
  public void startProcessing(Long pubId) {
    String doi = pdwhPubDupDao.getDoiStringByPubId(pubId);
    String cleandoi = FileUtils.cleanArcFileName(doi);
    Long cleanDoiHash = PubHashUtils.cleanDoiHash(cleandoi);
    pdwhPubCleanDOIDao.updatedoihash(pubId, cleanDoiHash, cleandoi);
  }

  @Override
  public List<Long> batchGetPubIdList(Integer size) {
    return pdwhPubCleanDOIDao.getPubIdData(size);
  }

}
