package com.smate.sie.center.task.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.exception.ServiceException;
import com.smate.sie.center.task.dao.ImportThirdPsnsHistoryDao;
import com.smate.sie.center.task.model.ImportThirdPsns;
import com.smate.sie.center.task.model.ImportThirdPsnsHistory;

/**
 * 第三方人员信息处理历史记录实现.
 * 
 * @author xys
 *
 */
@Service("importThirdPsnsHistoryService")
@Transactional(rollbackFor = Exception.class)
public class ImportThirdPsnsHistoryServiceImpl implements ImportThirdPsnsHistoryService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ImportThirdPsnsHistoryDao importThirdPsnsHistoryDao;

  @Override
  public void saveImportThirdPsnsHistory(ImportThirdPsns importThirdPsns) throws ServiceException {
    try {
      ImportThirdPsnsHistory importThirdPsnsHistory = new ImportThirdPsnsHistory();
      importThirdPsnsHistory.setEmail(importThirdPsns.getPk().getEmail());
      importThirdPsnsHistory.setInsId(importThirdPsns.getPk().getInsId());
      importThirdPsnsHistory.setZhName(importThirdPsns.getZhName());
      importThirdPsnsHistory.setFirstName(importThirdPsns.getFirstName());
      importThirdPsnsHistory.setLastName(importThirdPsns.getLastName());
      importThirdPsnsHistory.setUnitId(importThirdPsns.getUnitId());
      importThirdPsnsHistory.setPosition(importThirdPsns.getPosition());
      importThirdPsnsHistory.setImportDate(importThirdPsns.getCreateDate());
      importThirdPsnsHistory.setSieUnitId(importThirdPsns.getSieUnitId());
      importThirdPsnsHistory.setCreateDate(new Date());
      importThirdPsnsHistory.setStatus(importThirdPsns.getStatus());
      this.importThirdPsnsHistoryDao.save(importThirdPsnsHistory);
    } catch (Exception e) {
      logger.error("保存第三方人员信息处理历史记录出错了喔,insId:{},email:{},zhName:{},importDate:{}",
          new Object[] {importThirdPsns.getPk().getInsId(), importThirdPsns.getPk().getEmail(),
              importThirdPsns.getZhName(), importThirdPsns.getCreateDate(), e});
      throw new ServiceException(e);
    }
  }

}
