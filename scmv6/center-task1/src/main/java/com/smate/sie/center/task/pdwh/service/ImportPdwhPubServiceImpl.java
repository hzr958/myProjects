package com.smate.sie.center.task.pdwh.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.sie.center.task.dao.ImportPdwhPubDao;
import com.smate.sie.center.task.model.ImportPdwhPub;

@Service("importPdwhPubService")
@Transactional(rollbackOn = Exception.class)
public class ImportPdwhPubServiceImpl implements ImportPdwhPubService {

  @Autowired
  private ImportPdwhPubDao importPdwhPubDao;

  @Override
  public List<ImportPdwhPub> getImportPdwhPubList(Integer size) {
    return importPdwhPubDao.getImportPdwhPubList(size);
  }

  @Override
  public void updateImportPdwhPub(ImportPdwhPub imp) throws SysServiceException {
    importPdwhPubDao.save(imp);
  }

  @Override
  public void updateAllImportPdwhPub() throws SysServiceException {
    importPdwhPubDao.updateStatus();
  }

  @Override
  public Long getInsCountByStatus() throws SysServiceException {
    return importPdwhPubDao.getInsCountByStatus();
  }
}
