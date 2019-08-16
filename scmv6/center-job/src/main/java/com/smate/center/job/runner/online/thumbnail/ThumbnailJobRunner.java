package com.smate.center.job.runner.online.thumbnail;

import static com.smate.core.base.utils.file.FileUtils.getFileNamePrefix;

import com.smate.center.job.business.pub.service.SnsPubFulltextService;
import com.smate.center.job.framework.exception.PrecheckException;
import com.smate.center.job.framework.runner.BaseOnlineJobRunner;
import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.file.service.ArchiveFileService;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.file.ArchiveFileUtil;
import com.smate.core.base.utils.file.FileUtils;
import com.smate.core.base.utils.file.FileUtils.FileNameExtension;
import com.smate.core.base.utils.image.im4java.gm.GMImageUtil;
import com.smate.core.base.utils.image.im4java.gm.GMOperation.DensityUnitEnum;
import com.smate.core.base.utils.number.NumberUtils;
import java.util.Objects;
import java.util.Optional;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 缩略图处理任务
 *
 * @author houchuanjie
 * @date 2018/05/09 17:15
 */
@Component("thumbnailJob")
public class ThumbnailJobRunner extends BaseOnlineJobRunner {

  @Value("${file.root}")
  private String fileRoot;
  @Autowired
  private ArchiveFileService archiveFileService;
  @Autowired
  private SnsPubFulltextService snsPubFulltextService;
  private Long fileId;
  private FileTypeEnum fileType;

  @Override
  protected void process(JobDataMap jobDataMap) throws JobExecutionException {
    try {
      prepareParams(jobDataMap);
      logger.info("正在生成缩略图，文件id：{}，文件类型：{}", fileId, fileType.getDescription());
      switch (fileType) {
        case SNS_FULLTEXT:
          produceSnsFulltextFileThumbnail(jobDataMap);
          break;
        case PDWH_FULLTEXT:
          break;
        case PSN:
        case GROUP:
          // producePsnOrGroupFileThumbnail(fileId);
          break;
        default:
          logger.error("生成缩略图失败！原因：无法识别文件类型'{}'", fileType);
          throw new JobExecutionException("无法识别文件类型：'" + fileType);
      }
      logger.info("成功生成缩略图，文件id：{}，文件类型：{}", fileId, fileType.getDescription());
    } catch (Exception e) {
      logger.error("生成缩略图失败！", e);
      throw new JobExecutionException(e);
    }
  }

  /**
   * 处理个人库全文缩略图
   */
  private void produceSnsFulltextFileThumbnail(JobDataMap jobDataMap) throws Exception {
    Long pubId = Optional
        .ofNullable(NumberUtils.createLong(Objects.toString(jobDataMap.get("pub_id"), null)))
        .orElseThrow(() -> new JobExecutionException("pubId不能为null！"));
    ArchiveFile archiveFile = Optional.ofNullable(archiveFileService.getArchiveFileById(fileId))
        .orElseThrow(() -> new JobExecutionException("文件id=" + fileId + "的记录不存在！"));
    // pdf类型全文
    if (archiveFileService.isPDFile(archiveFile)) {
      // 源PDF相对路径，eg："/upfile/5a/cf/94/abcd.pdf"
      String srcRelativePath = getSrcRelativePath(archiveFile);
      // 得到没有文件名后缀的目标路径，eg："/pubFulltextImage/5a/cf/94/abcd"
      String destPathWithoutSuffix = getFileNamePrefix(srcRelativePath)
          .replace(ServiceConstants.DIR_UPFILE, ServiceConstants.DIR_PUB_FULLTEXT_IMAGE);
      // 生成高质量图片的路径，eg："/pubFulltextImage/5a/cf/94/abcd.png"
      String destRelativePath =
          destPathWithoutSuffix + FileUtils.SYMBOL_DOT + FileNameExtension.PNG;
      // 缩略图相对路径，eg："/pubFulltextImage/5a/cf/94/abcd_thumb.png"
      String destThumbRelativePath = destPathWithoutSuffix + ArchiveFileUtil.THUMBNAIL_SUFFIX;
      // 源图片绝对路径
      String srcPath = fileRoot + srcRelativePath + "[0]";
      String destPath = fileRoot + destRelativePath;
      String destThumbPath = fileRoot + destThumbRelativePath;
      GMImageUtil.convert(100, 300, DensityUnitEnum.PixelsPerInch, srcPath, destPath);
      GMImageUtil
          .sample(ArchiveFileUtil.THUMBNAIL_WIDTH_72, ArchiveFileUtil.THUMBNAIL_HEIGHT_92, false,
              destPath, destThumbPath);
      archiveFile.setFileType(ArchiveFileUtil.FILE_TYPE_PDF);
      archiveFileService.saveOrUpdate(archiveFile);
      // 更新全文图片
      snsPubFulltextService.updateFulltextImagePath(pubId, destThumbRelativePath);
    }
  }

  /**
   * 获取图片文件的源路径（相对地址）
   *
   * @author houchuanjie
   * @date 2018年1月11日 下午3:36:24
   */
  private String getSrcRelativePath(ArchiveFile archiveFile) {
    // 文件相对路径
    String relativeFilePath = archiveFile.getFileUrl();
    // 如果relativeFilePath是空的，那么根据filePath获取文件路径
    if (ArchiveFileUtil.FILE_URL_DEFAULT_VALUE.equals(relativeFilePath)) {
      relativeFilePath = FileUtils.SYMBOL_VIRGULE + ServiceConstants.DIR_UPFILE + ArchiveFileUtil
          .getFilePath(archiveFile.getFilePath());
    }
    if (relativeFilePath.charAt(0) != FileUtils.SYMBOL_VIRGULE_CHAR) {
      relativeFilePath = FileUtils.SYMBOL_VIRGULE + relativeFilePath;
    }
    archiveFile.setFileUrl(relativeFilePath);
    return relativeFilePath;
  }

  /**
   * 预处理文件相关参数
   */
  private void prepareParams(JobDataMap jobDataMap) throws JobExecutionException {
    // 文件id
    this.fileId = Optional
        .ofNullable(NumberUtils.createLong(Objects.toString(jobDataMap.get("file_id"), null)))
        .orElseThrow(() -> new JobExecutionException("文件id不能为null！"));

    // 类型：个人文件图片，群组文件图片，个人库全文图片，基准库全文图片
    Integer type = NumberUtils.createInteger(Objects.toString(jobDataMap.get("file_type"), null));
    try {
      this.fileType = FileTypeEnum.valueOf(type);
    } catch (Exception e) {
      throw new JobExecutionException(e);
    }
  }

  @Override
  public void preCheck(JobDataMap jobDataMap) throws PrecheckException {

  }
}
