package com.smate.core.base.utils.service.impfilelog;

import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.model.impfilelog.ImportFileLog;

/**
 * 文件导入日志模块
 * 
 * @author jszhou
 */
public interface ImportFileLogService {

  /**
   * @param importFileLog
   * @throws ServiceException
   */
  public ImportFileLog saveImportFileLog(ImportFileLog importFileLog) throws SysServiceException;
}
