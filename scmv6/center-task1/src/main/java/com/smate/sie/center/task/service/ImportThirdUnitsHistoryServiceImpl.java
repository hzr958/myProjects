package com.smate.sie.center.task.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.exception.ServiceException;
import com.smate.sie.center.task.dao.ImportThirdUnitsHistoryDao;
import com.smate.sie.center.task.model.ImportThirdUnits;
import com.smate.sie.center.task.model.ImportThirdUnitsHistory;

/**
 * 第三方部门信息处理历史记录实现.
 * 
 * @author xys
 *
 */
@Service("importThirdUnitsHistoryService")
@Transactional(rollbackFor = Exception.class)
public class ImportThirdUnitsHistoryServiceImpl implements ImportThirdUnitsHistoryService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ImportThirdUnitsHistoryDao importThirdUnitsHistoryDao;

  @Override
  public void saveImportThirdUnitsHistory(ImportThirdUnits importThirdUnits) throws ServiceException {
    try {
      ImportThirdUnitsHistory importThirdUnitsHistory = new ImportThirdUnitsHistory();
      importThirdUnitsHistory.setUnitId(importThirdUnits.getPk().getUnitId());
      importThirdUnitsHistory.setInsId(importThirdUnits.getPk().getInsId());
      importThirdUnitsHistory.setZhName(importThirdUnits.getZhName());
      importThirdUnitsHistory.setEnName(importThirdUnits.getEnName());
      importThirdUnitsHistory.setSuperUnitId(importThirdUnits.getSuperUnitId());
      importThirdUnitsHistory.setImportDate(importThirdUnits.getCreateDate());
      importThirdUnitsHistory.setSieUnitId(importThirdUnits.getSieUnitId());
      importThirdUnitsHistory.setCreateDate(new Date());
      importThirdUnitsHistory.setStatus(importThirdUnits.getStatus());
      importThirdUnitsHistoryDao.save(importThirdUnitsHistory);
    } catch (Exception e) {
      logger.error("保存第三方部门信息处理历史记录出错了喔,insId:{},unitId:{},unitName:{},pid:{}",
          new Object[] {importThirdUnits.getPk().getInsId(), importThirdUnits.getPk().getUnitId(),
              importThirdUnits.getZhName(), importThirdUnits.getSuperUnitId(), e});
      throw new ServiceException(e);
    }
  }

}
