package com.smate.core.base.file.service;

import java.util.List;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.file.model.ArchiveFile;

/**
 * 文件服务类
 * 
 * @author tsz
 *
 */
public interface ArchiveFileService {
  /**
   * 根据文件id获取图片类型的文件
   *
   * @author houchuanjie
   * @date 2018年1月2日 下午4:34:23
   * @param fileId
   * @return 文件被删除或者不存在或者不是图片类型的文件，返回null，否则返回该文件
   */
  ArchiveFile getImgFile(Long fileId);

  /**
   * 获取图片类型的文件缩略图url，不是图片类型或者没有缩略图返回空字符串
   *
   * @author houchuanjie
   * @date 2018年1月2日 下午8:56:26
   * @param des3FileId
   * @return
   */
  String getImgFileThumbUrl(String des3FileId);

  /**
   * 获取图片类型的文件缩略图url，不是图片类型或者没有缩略图返回空字符串
   *
   * @author houchuanjie
   * @date 2018年1月2日 下午8:56:26
   * @param fileId
   * @return 字符串
   */
  public String getImgFileThumbUrl(Long fileId);

  /**
   * 检查文件是否是图片类型的文件
   * 
   * @author houchuanjie
   * @date 2018年1月10日 下午8:31:27
   * @param archiveFile
   * @return 是图片返回true，不是返回false
   */
  public boolean isImageFile(final ArchiveFile archiveFile);

  /**
   * 检查文件是否是图片类型的文件
   * 
   * @author houchuanjie
   * @date 2018年1月10日 下午8:31:27
   * @param fileId
   * @return 是图片返回true，不是返回false
   */
  public boolean isImageFile(final Long fileId);

  /**
   * 检查文件是否是pdf文件
   *
   * @author houchuanjie
   * @date 2018年1月16日 上午10:50:33
   * @param af
   * @return 是pdf返回true,不是返回false
   */
  public boolean isPDFile(final ArchiveFile af);

  /**
   * 检查文件是否是pdf文件
   *
   * @param fileId
   * @return 是pdf返回true,不是返回false
   */
  public boolean isPDFile(final Long fileId);

  /**
   * 通过哦路径删除图片，此路径不包含rooPath.
   * 
   * @param filePath
   * @throws ServiceException
   */
  void deleteFileByPath(String filePath) throws ServiceException;

  /**
   * 通过附件id获取附件
   * 
   * @param fileId
   * @return
   * @throws ServiceException
   */
  ArchiveFile getArchiveFileById(Long fileId) throws ServiceException;

  /**
   * 查询附件的拥有者.
   * 
   * @param id
   * @return
   * @throws ServiceException
   */
  Long getArchiveFileOwner(Long id) throws ServiceException;

  void updateCollectStatistics(Long fulltextFileId);

  /**
   * 更新文件附件
   * 
   * @param archiveFile
   */
  void saveOrUpdate(ArchiveFile archiveFile);

  /**
   * 当前个人库已经存在在群组中的id
   */
  public List<Long> getGrpPsnFileIdList(Long grpId);
}
