package com.smate.core.base.file.service;

import java.io.File;

import com.smate.core.base.file.model.Sie6ArchiveFile;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.model.impfilelog.ImportFileLog;

/**
 * 文件服务类
 * 
 * @author tsz
 *
 */
public interface Sie6ArchiveFileService {

  /**
   * 
   * 
   * @param archiveFile
   */
  Sie6ArchiveFile saveSie6ArchiveFile(ImportFileLog importFileLog, File fileSource, String uuid,
      String filedataFileName, String savePathRoot) throws SysServiceException;

  public Sie6ArchiveFile getArchiveFile(Long id) throws SysServiceException;
}
