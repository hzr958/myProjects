package com.smate.sie.center.task.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.exception.ServiceException;
import com.smate.sie.center.task.dao.ImportThirdPsnsDao;
import com.smate.sie.center.task.model.ImportThirdPsns;

/**
 * 第三方人员信息实现.
 * 
 * @author xys
 *
 */
@Service("importThirdPsnsService")
@Transactional(rollbackFor = Exception.class)
public class ImportThirdPsnsServiceImpl implements ImportThirdPsnsService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ImportThirdPsnsDao importThirdPsnsDao;

  @Override
  public List<ImportThirdPsns> getThirdPsnsNeedImport(int maxSize) throws ServiceException {
    return this.importThirdPsnsDao.getThirdPsnsNeedImport(maxSize);
  }

  @Override
  public void saveImportThirdPsns(ImportThirdPsns importThirdPsns) throws ServiceException {
    try {
      this.importThirdPsnsDao.save(importThirdPsns);
    } catch (Exception e) {
      logger.error("保存第三方人员信息出错了喔,insId:{},email:{},zhName:{}", new Object[] {importThirdPsns.getPk().getInsId(),
          importThirdPsns.getPk().getEmail(), importThirdPsns.getZhName(), e});
      throw new ServiceException(e);
    }
  }

}
