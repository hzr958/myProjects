package com.smate.sie.center.task.service;

import org.hibernate.service.spi.ServiceException;

import com.smate.center.task.exception.InputFullTextTaskException;
import com.smate.sie.center.task.model.SieArchiveFile;

/**
 * SIE文件接口.
 * 
 * @author yxy
 * 
 */

public interface SieArchiveFilesService {

  public void saveArchiveFile(SieArchiveFile a);

  /**
   * 通过路径删除图片
   * 
   * @param filePath
   * @throws ServiceException
   */
  void deleteFileByPath(String filePath) throws InputFullTextTaskException;

}
