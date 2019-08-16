package com.smate.center.task.service.sns.quartz;

import java.io.FileNotFoundException;
import java.util.List;

import org.hibernate.service.spi.ServiceException;

import com.smate.center.task.exception.InputFullTextTaskException;
import com.smate.center.task.model.sns.quartz.ArchiveFile;

/**
 * 附件管理接口.
 * 
 * @author zk
 * 
 */

public interface ArchiveFilesService {

  /**
   * 通过路径删除图片，此路径不包含rooPath.
   * 
   * @param filePath
   * @throws ServiceException
   */
  void deleteFileByPath(String filePath) throws InputFullTextTaskException;

  /**
   * 通过附件id获取附件
   * 
   * @param fileId
   * @return
   * @throws ServiceException
   */
  ArchiveFile getArchiveFilesById(Long fileId) throws Exception;

  public void saveArchiveFile(ArchiveFile a);

  /**
   * 获取附件.
   */
  ArchiveFile getArchiveFiles(Long id) throws Exception;

  String getFileRoot();

  /**
   * 获取附件路径.
   * 
   * @param id
   * @param baseDir
   * @return
   * @throws ServiceException
   */
  String getArchiveFilePath(Long id, String baseDir) throws InputFullTextTaskException;

  /**
   * 单位LOGO裁剪，保存附件，此方法供本地调用，请勿用于远程调用.
   * 
   * @return
   * @throws ServiceException
   */
  String cutInsLogoImage(Long insId, Integer x, Integer y, Integer width, Integer height)
      throws InputFullTextTaskException;

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

  /**
   * 批量获取表中文件大小字段为空的数据
   * 
   * @param size
   * @return
   */
  List<ArchiveFile> getEmptySizeFile(Integer size);

  /**
   * 生成文件大小
   * 
   * @param archiveFiles
   * @throws Exception
   */
  void generateFileSize(ArchiveFile archiveFile) throws Exception;

}
