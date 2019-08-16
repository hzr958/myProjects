package com.smate.core.base.file.service.impl;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.file.dao.ArchiveFileDao;
import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.file.service.ArchiveFileService;
import com.smate.core.base.statistics.dao.DownloadCollectStatisticsDao;
import com.smate.core.base.statistics.model.DownloadCollectStatistics;
import com.smate.core.base.utils.date.DateUtils;
import com.smate.core.base.utils.file.ArchiveFileUtil;
import com.smate.core.base.utils.file.FileUtils;
import com.smate.core.base.utils.file.FileUtils.FileNameExtension;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;

/**
 * 文件表服务类
 *
 * @author houchuanjie
 * @date 2018年1月3日 下午3:15:43
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class ArchiveFileServiceImpl implements ArchiveFileService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Value("${file.root}")
  private String fileRoot;
  @Autowired
  private DownloadCollectStatisticsDao downloadCollectStatisticsDao;
  @Autowired
  private ArchiveFileDao archiveFileDao;

  @Override
  public ArchiveFile getImgFile(Long fileId) {
    if (fileId != null && fileId != 0L) {
      ArchiveFile archiveFile = archiveFileDao.get(fileId);
      // 文件状态; 0--正常, 1--删除; 默认0
      if (archiveFile == null || (archiveFile.getStatus() != null && archiveFile.getStatus() != 0)) {
        return null;
      }
      if (isImageFile(archiveFile)) {
        return archiveFile;
      }
    }
    return null;
  }

  @Override
  public String getImgFileThumbUrl(String des3FileId) {
    Long fileId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3FileId));
    return getImgFileThumbUrl(fileId);
  }

  @Override
  public String getImgFileThumbUrl(Long fileId) {
    ArchiveFile imgFile = getImgFile(fileId);
    if (imgFile != null) {
      String thumbUrl = FileUtils.getFileNamePrefix(imgFile.getFileUrl()) + ArchiveFileUtil.THUMBNAIL_SUFFIX;
      if (thumbUrl.charAt(0) != FileUtils.SYMBOL_VIRGULE_CHAR) {
        thumbUrl = FileUtils.SYMBOL_VIRGULE_CHAR + thumbUrl;
      }
      return thumbUrl;
    }
    return "";
  }

  @Override
  public void deleteFileByPath(String filePath) throws ServiceException {
    try {
      String realPath = getFileRoot();
      File thumbfile = new File(realPath + filePath);
      if (thumbfile.exists()) {
        thumbfile.delete();
      }
      // 删除原图
      File srcfile = new File(realPath + filePath.replace("_img_1", "").replace("_img_0", ""));
      if (srcfile.exists()) {
        srcfile.delete();
      }
    } catch (Exception e) {
      logger.error("删除文件{}出现异常：{}", filePath + e);
      throw new ServiceException("删除文件{" + filePath + "}出错", e);
    }
  }

  @Override
  public ArchiveFile getArchiveFileById(Long fileId) throws ServiceException {
    return archiveFileDao.findArchiveFileById(fileId);
  }

  @Override
  public Long getArchiveFileOwner(Long id) throws ServiceException {
    try {
      return this.archiveFileDao.queryArchiveFileOwner(id);
    } catch (Exception e) {
      logger.error("查询附件fileId={}拥有人出现异常：{}", id, e);
      throw new ServiceException(e);
    }
  }

  public String getFileRoot() {
    return fileRoot;
  }

  public void setFileRoot(String fileRoot) {
    this.fileRoot = fileRoot;
  }

  @Override
  public void updateCollectStatistics(Long fulltextFileId) {
    try {
      Date nowDate = new Date();
      long formateDate = DateUtils.getDateTime(nowDate);
      DownloadCollectStatistics dcs =
          downloadCollectStatisticsDao.findRecord(0L, fulltextFileId, 1, SecurityUtils.getCurrentUserId(), formateDate);
      if (dcs == null) {
        dcs = new DownloadCollectStatistics();
        dcs.setActionKey(fulltextFileId);
        dcs.setActionType(1);
        dcs.setDcdDate(nowDate);
        dcs.setDcPsnId(SecurityUtils.getCurrentUserId());
        dcs.setFormateDate(formateDate);
        dcs.setPsnId(0L);
        dcs.setIp(Struts2Utils.getRemoteAddr());;
      } else {
        dcs.setDcdDate(nowDate);
        dcs.setFormateDate(formateDate);
      }
      dcs.setDcount(dcs.getDcount() + 1l);// 下载次数
      downloadCollectStatisticsDao.save(dcs);
    } catch (Exception e) {
      logger.error("保存基准库下载或收藏记录出错！PsnId=" + SecurityUtils.getCurrentUserId() + " dcPsnId=" + 0 + " actionKey="
          + fulltextFileId + " actionType=1", e);
    }
  }

  /**
   * 检查文件是否是图片类型的文件
   *
   * @author houchuanjie
   * @date 2018年1月10日 下午8:31:27
   * @param archiveFile
   * @return 是图片返回true，不是返回false
   */
  @Override
  public boolean isImageFile(final ArchiveFile archiveFile) {
    if (archiveFile == null) {
      return false;
    }
    // 文件实际类型
    String fileType = archiveFile.getFileType();
    boolean flag = ArchiveFileUtil.FILE_TYPE_IMG.equalsIgnoreCase(fileType);
    if (!flag) {
      final String fileName = archiveFile.getFileName();
      // 判定是否是图片类型的后缀枚举
      flag = FileNameExtension.isImageFileNameExtension(FileUtils.getFileNameExtension(fileName));
    }
    return flag;
  }

  /**
   * 检查文件是否是pdf文件
   *
   * @return 是pdf返回true, 不是返回false
   * @author houchuanjie
   * @date 2018年1月16日 上午10:50:33
   */
  @Override
  public boolean isPDFile(ArchiveFile archiveFile) {
    // 文件实际类型
    final String fileType = archiveFile.getFileType();
    boolean flag = StringUtils.equalsIgnoreCase(FileNameExtension.PDF.toString(), fileType);
    if (!flag) {
      final String fileName = archiveFile.getFileName();
      FileNameExtension ext = FileUtils.getFileNameExtension(fileName);
      // 判断是否是pdf类型的后缀
      flag = FileNameExtension.PDF.equals(ext);
    }
    return flag;
  }

  @Override
  public boolean isPDFile(Long fileId) {
    ArchiveFile archiveFile = archiveFileDao.get(fileId);
    return isPDFile(archiveFile);
  }

  /**
   * 检查文件是否是图片类型的文件
   *
   * @return 是图片返回true，不是返回false
   * @author houchuanjie
   * @date 2018年1月10日 下午8:31:27
   */
  @Override
  public boolean isImageFile(final Long fileId) {
    ArchiveFile archiveFile = archiveFileDao.get(fileId);
    return isImageFile(archiveFile);
  }

  @Override
  public void saveOrUpdate(ArchiveFile archiveFile) {
    archiveFileDao.save(archiveFile);
  }

  @Override
  public List<Long> getGrpPsnFileIdList(Long grpId) {
    // TODO Auto-generated method stub
    return archiveFileDao.getGrpPsnFileIdList(grpId);
  }
}
