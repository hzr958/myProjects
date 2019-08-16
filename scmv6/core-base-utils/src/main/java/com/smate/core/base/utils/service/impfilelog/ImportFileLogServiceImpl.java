package com.smate.core.base.utils.service.impfilelog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.dao.impfilelog.ImportFileLogDao;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.model.impfilelog.ImportFileLog;

/**
 * 文件导入日志模块
 * 
 * @author jszhou
 */

@Service("importFileLogService")
@Transactional(rollbackFor = Exception.class)
public class ImportFileLogServiceImpl implements ImportFileLogService {

  @Autowired
  private ImportFileLogDao importFileLogDao;

  @Override
  public ImportFileLog saveImportFileLog(ImportFileLog importFileLog) throws SysServiceException {
    importFileLogDao.save(importFileLog);
    return importFileLog;

  }

}
