package com.smate.center.batch.service.pub.archiveFiles;

import java.io.FileNotFoundException;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 附件管理接口.
 * 
 * @author zk
 * 
 */

public interface ArchiveFilesService {

  /**
   * 通过哦路径删除图片，此路径不包含rooPath.
   * 
   * @param filePath
   * @throws ServiceException
   */
  void deleteFileByPath(String filePath) throws BatchTaskException;

  /**
   * 通过附件id获取附件
   * 
   * @param fileId
   * @return
   * @throws ServiceException
   */
  ArchiveFile getArchiveFileById(Long fileId) throws BatchTaskException;

  public void saveArchiveFile(ArchiveFile a);

  /**
   * 获取附件.
   */
  ArchiveFile getArchiveFile(Long id) throws BatchTaskException;


  String getFileRoot();



  /**
   * pdf指定页码转换成图片.
   * 
   * @param fileId
   * @param filePath
   * @param baseDir
   * @param scale
   * @param pageIndex
   * @return
   * @throws ServiceException
   */
  String pdfConvertToImage(Long fileId, String filePath, String baseDir, float scale, int pageIndex)
      throws Exception, FileNotFoundException;

  /**
   * 按比例缩小图片.
   * 
   * @param fileId
   * @param filePath
   * @param baseDir
   * @param ratio 小于0的数.
   * @return
   * @throws Exception
   * @throws ServiceException
   */
  String reduceImage(Long fileId, String filePath, String baseDir, String fileType, float ratio) throws Exception;

  public void updateArchiveFile(ArchiveFile af);
}
