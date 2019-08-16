package com.smate.sie.center.task.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.exception.ServiceException;
import com.smate.sie.center.task.dao.ImportThirdUnitsDao;
import com.smate.sie.center.task.model.ImportThirdUnits;

/**
 * 第三方部门信息实现.
 * 
 * @author xys
 *
 */
@Service("importThirdUnitsService")
@Transactional(rollbackFor = Exception.class)
public class ImportThirdUnitsServiceImpl implements ImportThirdUnitsService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ImportThirdUnitsDao importThirdUnitsDao;

  @Override
  public List<ImportThirdUnits> getThirdUnitsNeedImport(int maxSize) throws ServiceException {
    return this.importThirdUnitsDao.getThirdUnitsNeedImport(maxSize);
  }

  @Override
  public void saveImportThirdUnits(ImportThirdUnits importThirdUnits) throws ServiceException {
    try {
      this.importThirdUnitsDao.save(importThirdUnits);
    } catch (Exception e) {
      logger.error("保存第三方部门信息出错了喔,insId:{},unitId:{},unitName:{},pid:{}",
          new Object[] {importThirdUnits.getPk().getInsId(), importThirdUnits.getPk().getUnitId(),
              importThirdUnits.getZhName(), importThirdUnits.getSuperUnitId(), e});
      throw new ServiceException(e);
    }
  }

}
