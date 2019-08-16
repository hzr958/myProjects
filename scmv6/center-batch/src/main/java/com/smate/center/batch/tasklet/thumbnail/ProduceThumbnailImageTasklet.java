package com.smate.center.batch.tasklet.thumbnail;

import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.smate.center.batch.service.pub.archiveFiles.ArchiveFilesService;
import com.smate.center.batch.service.pubfulltexttoimage.PubFullTextService;
import com.smate.center.batch.tasklet.base.BaseTasklet;
import com.smate.center.batch.tasklet.base.DataVerificationStatus;
import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.file.ArchiveFileUtil;
import com.smate.core.base.utils.file.FileService;
import com.smate.core.base.utils.file.FileUtils;
import com.smate.core.base.utils.file.FileUtils.FileNameExtension;
import com.smate.core.base.utils.image.im4java.gm.GMImageUtil;

/**
 * 缩略图生成任务
 * 
 * @author houchuanjie
 * @date 2018年1月10日 下午6:13:45
 */
public class ProduceThumbnailImageTasklet extends BaseTasklet {
  @Autowired
  private PubFullTextService pubFullTextService;
  @Autowired
  private FileService fileService;
  @Autowired
  private ArchiveFilesService archiveFilesService;
  @Value("${file.root}")
  private String fileRoot;

  @Override
  public DataVerificationStatus dataVerification(String msgId) throws BatchTaskException {
    if (NumberUtils.toLong(msgId) > 0) {
      return DataVerificationStatus.TRUE;
    } else {
      return DataVerificationStatus.FALSE;
    }
  }

  @Override
  public void taskExecution(Map jobContentMap) throws BatchTaskException {
    // 文件id
    Long fileId = NumberUtils.createLong(Objects.toString(jobContentMap.get("msg_id"), null));
    // 类型：个人文件图片，群组文件图片，个人库全文图片，基准库全文图片
    Integer fileType = NumberUtils.createInteger(Objects.toString(jobContentMap.get("file_type"), null));
    FileTypeEnum fileTypeEnum = null;
    if (fileType != null) {
      fileTypeEnum = FileTypeEnum.valueOf(fileType);
    }
    logger.info("正在生成缩略图，文件id：{}，文件类型：{}", fileId, fileTypeEnum.getDescription());
    switch (fileTypeEnum) {
      case SNS_FULLTEXT:
        Long pubId = NumberUtils.createLong(Objects.toString(jobContentMap.get("pub_id"), null));
        produceSnsFulltextFileThumbnail(fileId, pubId);
        break;
      case PDWH_FULLTEXT:
        break;
      case PSN:
      case GROUP:
        producePsnOrGroupFileThumbnail(fileId);
        break;
      default:
        logger.error("无法生成缩略图！原因：无法识别文件类型'{}'", fileType);
        throw new BatchTaskException("无法生成缩略图！原因：无法识别文件类型'" + fileType + "'，请检查v_batch_jobs表相关记录的JOB_CONTEXT内容是否符合要求！");
    }
    logger.info("成功生成缩略图，文件id：{}，文件类型：{}", fileId, fileTypeEnum.getDescription());
  }

  /**
   * 个人库成果全文图片类型文件生成缩略图
   * 
   * @author houchuanjie
   * @date 2018年1月11日 下午3:24:38
   * @param fileId
   * @throws BatchTaskException
   */
  private void produceSnsFulltextFileThumbnail(Long fileId, Long pubId) throws BatchTaskException {
    if (pubId == null || pubId.longValue() == 0) {
      logger.error("无法生成缩略图！原因：全文对应的pubId--{}不能为空！", pubId);
      throw new BatchTaskException("无法生成缩略图！原因：全文对应的pubId--" + pubId + "不能为空，因此无法生成缩略图！");
    }
    if (fileId == null || fileId.longValue() == 0L) {
      logger.error("无法生成缩略图！原因：文件id--{}不能为空！", fileId);
      throw new BatchTaskException("无法生成缩略图！原因：文件id--" + fileId + "不能为空，因此无法生成缩略图！");
    }
    ArchiveFile archiveFile = archiveFilesService.getArchiveFile(fileId);
    if (archiveFile == null) {
      logger.error("无法生成缩略图！原因：文件id={}的记录不存在！", fileId);
      throw new BatchTaskException("无法生成缩略图！原因：文件id=" + fileId + "的记录不存在，因此无法生成缩略图！");
    }
    // 处理图片类型全文
    if (isImageFile(archiveFile)) {
      // 源图片相对路径
      String srcRelativePath = getSrcRelativePath(archiveFile);
      // 缩略图相对路径
      String destRelativePath = FileUtils.getFileNamePrefix(srcRelativePath).replace(ServiceConstants.DIR_UPFILE,
          ServiceConstants.DIR_PUB_FULLTEXT_IMAGE) + ArchiveFileUtil.THUMBNAIL_SUFFIX;
      // 源图片绝对路径
      String srcPath = fileRoot + srcRelativePath;
      // 缩略图绝对路径
      String destPath = fileRoot + destRelativePath;
      try {
        GMImageUtil.thumbnail(ArchiveFileUtil.THUMBNAIL_WIDTH_72, ArchiveFileUtil.THUMBNAIL_HEIGHT_92, true, srcPath,
            destPath);
        // 更新全文图片
        pubFullTextService.updateSnsPubFulltextImage(pubId, destRelativePath, 1);
        if (!ArchiveFileUtil.FILE_TYPE_IMG.equalsIgnoreCase(archiveFile.getFileType())) {
          // 更新文件类型
          archiveFile.setFileType(ArchiveFileUtil.FILE_TYPE_IMG);
          archiveFilesService.saveArchiveFile(archiveFile);
        }
      } catch (Exception e) {
        logger.error("生成缩略图失败！", e);
        throw new BatchTaskException(e);
      }
    } // 处理pdf类型全文
    else if (isPDFile(archiveFile)) {
      // 先删除可能存在的全文记录
      pubFullTextService.deletePubFulltextByPubId(pubId);
      String filePath = ArchiveFileUtil.getFilePath(archiveFile.getFilePath());
      String srcFilePath = fileRoot + "/" + ServiceConstants.DIR_UPFILE + filePath;
      String imageExt = "jpeg";
      String ImagePath = "/" + ServiceConstants.DIR_PUB_FULLTEXT_IMAGE
          + fileService.getFilePath(fileId + "_img_" + 1 + "." + imageExt);
      String desImagePath = fileRoot + "/" + ImagePath;
      try {
        GMImageUtil.thumbnail(ArchiveFileUtil.THUMBNAIL_WIDTH_72, ArchiveFileUtil.THUMBNAIL_HEIGHT_92, true,
            srcFilePath, desImagePath);
        // 更新全文图片
        pubFullTextService.updateSnsPubFulltextImage(pubId, ImagePath, 1);
        if (!ArchiveFileUtil.FILE_TYPE_PDF.equalsIgnoreCase(archiveFile.getFileType())) {
          // 更新文件类型
          archiveFile.setFileType(ArchiveFileUtil.FILE_TYPE_PDF);
          archiveFilesService.saveArchiveFile(archiveFile);
        }
      } catch (Exception e) {
        logger.error("生成缩略图失败！", e);
        throw new BatchTaskException(e);
      }
      // pubFullTextService.sendFtRequestReplyMail(pubId);

    } // 其他类型
    else {
      /*
       * // 可能是其他文件，其他文件类型，直接发送站内信与邮件;由旧任务迁移 if (pubFullTextService.isOtherTypeFt(pubId)) {
       * pubFullTextService.sendFtRequestReplyMail(pubId); }
       */
      logger.error("无法生成缩略图！原因：文件{}不是图片或者PDF类型！", fileId);
      throw new BatchTaskException("无法生成缩略图！原因：文件" + fileId + "不是图片或者PDF类型的文件，因此无法生成缩略图！");
    }
  }

  /**
   * 个人文件或群组图片类型文件生成缩略图
   * 
   * @author houchuanjie
   * @date 2018年1月10日 下午8:27:11
   * @param fileId
   * @throws BatchTaskException
   */
  private void producePsnOrGroupFileThumbnail(Long fileId) throws BatchTaskException {
    if (fileId == null || fileId.longValue() == 0L) {
      logger.error("无法生成缩略图！原因：文件id--{}不能为空！", fileId);
      throw new BatchTaskException("无法生成缩略图！原因：文件id--" + fileId + "不能为空，因此无法生成缩略图！");
    }
    ArchiveFile archiveFile = archiveFilesService.getArchiveFile(fileId);
    if (archiveFile == null) {
      logger.error("无法生成缩略图！原因：文件id={}的记录不存在！", fileId);
      throw new BatchTaskException("无法生成缩略图！原因：文件id=" + fileId + "的记录不存在，因此无法生成缩略图！");
    }
    // 排除非图片类型
    if (isImageFile(archiveFile)) {
      // 源图片相对路径
      String srcRelativePath = getSrcRelativePath(archiveFile);
      // 缩略图相对路径
      String destRelativePath = FileUtils.getFileNamePrefix(srcRelativePath) + ArchiveFileUtil.THUMBNAIL_SUFFIX;
      // 源图片绝对路径
      String srcPath = fileRoot + srcRelativePath;
      // 缩略图绝对路径
      String destPath = fileRoot + destRelativePath;
      try {
        GMImageUtil.thumbnail(ArchiveFileUtil.THUMBNAIL_WIDTH_72, ArchiveFileUtil.THUMBNAIL_HEIGHT_92, true, srcPath,
            destPath);
        boolean modified = false;
        // 如果获取到的源图片相对路径和表记录的路径不一致，更新表记录
        if (!srcRelativePath.equals(archiveFile.getFileUrl())) {
          archiveFile.setFileUrl(srcRelativePath);
          modified = true;
        }
        if (!ArchiveFileUtil.FILE_TYPE_IMG.equalsIgnoreCase(archiveFile.getFileType())) {
          // 更新文件类型
          archiveFile.setFileType(ArchiveFileUtil.FILE_TYPE_IMG);
          modified = true;
        }
        if (modified) {
          archiveFilesService.updateArchiveFile(archiveFile);
        }
      } catch (Exception e) {
        logger.error("生成缩略图失败！", e);
        throw new BatchTaskException(e);
      }
    } else {
      logger.error("无法生成缩略图！原因：文件{}不是图片类型！", fileId);
      throw new BatchTaskException("无法生成缩略图！原因：文件" + fileId + "不是图片类型的文件，因此无法生成缩略图！");
    }
  }

  /**
   * 获取图片文件的源路径（相对地址）
   *
   * @author houchuanjie
   * @date 2018年1月11日 下午3:36:24
   * @param archiveFiles
   * @return
   */
  private String getSrcRelativePath(ArchiveFile archiveFiles) {
    // 文件相对路径
    String relativeFilePath = archiveFiles.getFileUrl();
    // 如果relativeFilePath是空的，那么根据filePath获取文件路径
    if (ArchiveFileUtil.FILE_URL_DEFAULT_VALUE.equals(relativeFilePath)) {
      relativeFilePath = FileUtils.SYMBOL_VIRGULE_CHAR + ServiceConstants.DIR_UPFILE
          + ArchiveFileUtil.getFilePath(archiveFiles.getFilePath());
    }
    if (relativeFilePath.charAt(0) != FileUtils.SYMBOL_VIRGULE_CHAR) {
      relativeFilePath = FileUtils.SYMBOL_VIRGULE_CHAR + relativeFilePath;
    }
    return relativeFilePath;
  }

  /**
   * 检查文件是否是图片类型的文件
   * 
   * @author houchuanjie
   * @date 2018年1月10日 下午8:31:27
   * @param archiveFiles
   * @return 是图片返回true，不是返回false
   */
  private boolean isImageFile(final ArchiveFile archiveFile) {
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
   * @author houchuanjie
   * @date 2018年1月16日 上午10:50:33
   * @param archiveFiles
   * @return 是pdf返回true,不是返回false
   */
  private boolean isPDFile(ArchiveFile archiveFile) {
    // 文件实际类型
    final String fileType = archiveFile.getFileType();
    boolean flag = FileNameExtension.PDF.toString().equalsIgnoreCase(fileType);
    if (!flag) {
      final String fileName = archiveFile.getFileName();
      FileNameExtension ext = FileUtils.getFileNameExtension(fileName);
      // 判断是否是pdf类型的后缀
      flag = FileNameExtension.PDF.equals(ext);
    }
    return flag;
  }
}
