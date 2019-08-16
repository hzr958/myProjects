package com.smate.center.task.service.pdwh.quartz;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.publicpub.PubFulltextUploadLogDao;
import com.smate.center.task.dao.sns.quartz.ArchiveFileDao;
import com.smate.center.task.model.sns.quartz.ArchiveFile;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubFullTextDAO;
import com.smate.center.task.v8pub.dao.sns.PubFullTextDAO;
import com.smate.center.task.v8pub.pdwh.po.PdwhPubFullTextPO;
import com.smate.center.task.v8pub.sns.po.PubFullTextPO;
import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.file.ArchiveFileUtil;
import com.smate.core.base.utils.file.FileUtils;
import com.smate.core.base.utils.file.FileUtils.FileNameExtension;
import com.smate.core.base.utils.image.im4java.gm.GMImageUtil;
import com.smate.core.base.utils.image.im4java.gm.GMOperation.DensityUnitEnum;

@Service("fulltextPdfToImageService")
@Transactional(rollbackFor = Exception.class)
public class FulltextPdfToImageServiceImpl implements FulltextPdfToImageService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Value("${file.root}")
  private String fileRoot;
  @Autowired
  private ArchiveFileDao archiveFileDao;
  @Autowired
  private PubFulltextUploadLogDao pubFulltextUploadLogDao;
  @Autowired
  private PdwhPubFullTextDAO pdwhPubFullTextDAO;
  @Autowired
  private PubFullTextDAO pubFullTextDAO;

  @Override
  public List<Long> getPdwhToImageData(Integer size) {
    return pubFulltextUploadLogDao.getPdwhToImageData(size);
  }

  @Override
  public List<Long> getSnsToImageData(Integer size) {
    return pubFulltextUploadLogDao.getSnsToImageData(size);
  }

  @Override
  public PdwhPubFullTextPO getPdwhPubFulltext(Long pdwhPubId) {
    return pdwhPubFullTextDAO.getPdwhPubFulltext(pdwhPubId);
  }

  @Override
  public PubFullTextPO getSnsPubFulltext(Long snsPubId) {
    return pubFullTextDAO.getPubFullTextByPubId(snsPubId);
  }

  @Override
  public void deleteFileByPath(String filePath) {
    try {
      File thumbfile = new File(fileRoot + filePath);
      if (thumbfile.exists()) {
        thumbfile.delete();
      }
      // 删除原图
      File srcfile = new File(fileRoot + filePath.replace("_img_1", "").replace("_img_0", ""));
      if (srcfile.exists()) {
        srcfile.delete();
      }
    } catch (Exception e) {
      logger.error("删除文件{}出现异常：{}", filePath + e);
      throw new ServiceException("删除文件{" + filePath + "}出错", e);
    }
  }

  /**
   * 基准库全文生成图片
   */
  @Override
  public void ConvertPdwhPubFulltextPdfToimage(PdwhPubFullTextPO pubFulltext) {
    try {
      ArchiveFile archiveFile = archiveFileDao.findArchiveFileById(pubFulltext.getFileId());
      if (archiveFile == null) {
        this.updatePdwhToImageStatus(pubFulltext.getPdwhPubId(), 2, "文件不存在");
        return;
      }
      String fileName = archiveFile.getFileName();
      String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
      if (!"pdf".equalsIgnoreCase(fileType)) {
        logger.error("转换pdwh成果的全文包含非pdf文件，不处理!pdwhPubId" + pubFulltext.getPdwhPubId());
        this.updatePdwhToImageStatus(pubFulltext.getPdwhPubId(), 2, "转换pdwh成果的全文包含非pdf文件");
        return;
      }
      String filePath = "";
      try {
        filePath = getSrcRelativePath(archiveFile);
      } catch (Exception e) {
        this.updatePdwhToImageStatus(pubFulltext.getPdwhPubId(), 2, "ARCHIVE_FILES表的FILE_URL或者FILE_PATH为空");
        return;
      }
      String srcFilePath = fileRoot + "/" + filePath;
      File file = new File(srcFilePath);
      if (!file.exists()) {
        this.updatePdwhToImageStatus(pubFulltext.getPdwhPubId(), 2, "pdf文件不存在");
        return;
      }
      String imageExt = ".jpeg";
      String destImgpath = ArchiveFileUtil.getFilePath(pubFulltext.getFileId().toString());// 图片路径

      // 高清图：/pubFulltextImage/b1/b7/64/1000000265915.jpeg
      String desImageUrl = "/" + ServiceConstants.DIR_PUB_FULLTEXT_IMAGE + destImgpath + imageExt;// 高清图url,

      String desImagePath = fileRoot + desImageUrl;// 高清图路径

      // 缩略图：/pubFulltextImage/b1/b7/64/1000000265915_img_1.jpeg
      String destThumbUrl = "/" + ServiceConstants.DIR_PUB_FULLTEXT_IMAGE + destImgpath + "_img_1" + imageExt;// 缩略图url,

      String destThumbPath = fileRoot + destThumbUrl;// 缩略图路径
      // pdf生成高清图
      GMImageUtil.convert(100, 300, DensityUnitEnum.PixelsPerInch, srcFilePath, desImagePath);
      File srcimg = new File(desImagePath);
      if (!srcimg.exists()) {
        this.updatePdwhToImageStatus(pubFulltext.getPdwhPubId(), 2, "pdf生成原图出错，无法继续生成缩略图！");
        return;
      }
      // 高清图转缩略图
      GMImageUtil.sample(ArchiveFileUtil.PDWH_THUMBNAIL_WIDTH_178, ArchiveFileUtil.PDWH_THUMBNAIL_HEIGHT_239, false,
          desImagePath, destThumbPath);
      // 保存缩略图
      pubFulltext.setThumbnailPath(destThumbUrl);
      archiveFileDao.save(archiveFile);
      pubFulltext.setGmtModified(new Date());
      pdwhPubFullTextDAO.update(pubFulltext);
      this.updatePdwhToImageStatus(pubFulltext.getPdwhPubId(), 1, "pdf全文转换图片成功");
    } catch (Exception e) {
      logger.error("基准库pdf转图片离线任务处理时出现异常！该记录id={}", pubFulltext.getPdwhPubId());
      this.updatePdwhToImageStatus(pubFulltext.getPdwhPubId(), 3, "基准库pdf转图片离线任务处理时出现异常");
    }
  }

  private String getSrcRelativePath(ArchiveFile archiveFile) {
    // 文件相对路径
    String relativeFilePath = archiveFile.getFileUrl();
    // 如果relativeFilePath是空的，那么根据filePath获取文件路径
    if (ArchiveFileUtil.FILE_URL_DEFAULT_VALUE.equals(relativeFilePath)) {
      relativeFilePath = FileUtils.SYMBOL_VIRGULE + ServiceConstants.DIR_UPFILE
          + ArchiveFileUtil.getFilePath(archiveFile.getFilePath());
    }
    if (relativeFilePath.charAt(0) != FileUtils.SYMBOL_VIRGULE_CHAR) {
      relativeFilePath = FileUtils.SYMBOL_VIRGULE + relativeFilePath;
    }
    archiveFile.setFileUrl(relativeFilePath);
    return relativeFilePath;
  }

  /**
   * 个人库成果全文生成图片
   */
  @Override
  public void ConvertSnsPubFulltextPdfToimage(PubFullTextPO pubFulltext) {
    try {
      ArchiveFile archiveFile = archiveFileDao.findArchiveFileById(pubFulltext.getFileId());
      if (archiveFile == null) {
        this.updateSnsToImageStatus(pubFulltext.getPubId(), 2, "文件不存在");
        return;
      }
      String filePath = "";
      if (isImageFile(archiveFile)) {
        // 源图片相对路径
        String srcRelativePath = getSrcRelativePath(archiveFile);

        try {
          filePath = getSrcRelativePath(archiveFile);
        } catch (Exception e) {
          this.updateSnsToImageStatus(pubFulltext.getPubId(), 2, "ARCHIVE_FILES表的FILE_URL或者FILE_PATH为空");
          return;
        }
        // 缩略图相对路径
        String destRelativePath = FileUtils.getFileNamePrefix(srcRelativePath).replace(ServiceConstants.DIR_UPFILE,
            ServiceConstants.DIR_PUB_FULLTEXT_IMAGE) + ArchiveFileUtil.THUMBNAIL_SUFFIX;
        // 源图片绝对路径
        String srcPath = fileRoot + srcRelativePath;
        // 缩略图绝对路径
        String destPath = fileRoot + destRelativePath;
        GMImageUtil.sample(ArchiveFileUtil.PDWH_THUMBNAIL_WIDTH_178, ArchiveFileUtil.PDWH_THUMBNAIL_HEIGHT_239, false,
            srcPath, destPath);
        // 保存缩略图
        pubFulltext.setThumbnailPath(destRelativePath);
      } else if (isPDFile(archiveFile)) {
        try {
          filePath = getSrcRelativePath(archiveFile);
        } catch (Exception e) {
          this.updateSnsToImageStatus(pubFulltext.getPubId(), 2, "ARCHIVE_FILES表的FILE_URL或者FILE_PATH为空");
          return;
        }
        String srcFilePath = fileRoot + "/" + filePath;
        File file = new File(srcFilePath);
        if (!file.exists()) {
          this.updateSnsToImageStatus(pubFulltext.getPubId(), 2, "pdf文件不存在");
          return;
        }
        String imageExt = ".jpeg";
        String destImgpath = ArchiveFileUtil.getFilePath(pubFulltext.getFileId().toString());// 图片路径

        // 高清图：/pubFulltextImage/b1/b7/64/1000000265915.jpeg
        String desImageUrl = "/" + ServiceConstants.DIR_PUB_FULLTEXT_IMAGE + destImgpath + imageExt;// 高清图url,

        String desImagePath = fileRoot + desImageUrl;// 高清图路径

        // 缩略图：/pubFulltextImage/b1/b7/64/1000000265915_img_1.jpeg
        String destThumbUrl = "/" + ServiceConstants.DIR_PUB_FULLTEXT_IMAGE + destImgpath + "_img_1" + imageExt;// 缩略图url,

        String destThumbPath = fileRoot + destThumbUrl;// 缩略图路径
        // pdf生成高清图
        GMImageUtil.convert(100, 300, DensityUnitEnum.PixelsPerInch, srcFilePath, desImagePath);
        File srcimg = new File(desImagePath);
        if (!srcimg.exists()) {
          this.updateSnsToImageStatus(pubFulltext.getPubId(), 2, "pdf生成原图出错，无法继续生成缩略图！");
          return;
        }
        // 高清图转缩略图
        GMImageUtil.sample(ArchiveFileUtil.PDWH_THUMBNAIL_WIDTH_178, ArchiveFileUtil.PDWH_THUMBNAIL_HEIGHT_239, false,
            desImagePath, destThumbPath);
        // 保存缩略图
        pubFulltext.setThumbnailPath(destThumbUrl);
      } else {
        this.updateSnsToImageStatus(pubFulltext.getPubId(), 2, "全文既不是图片也不是pdf文件，不处理");
        return;
      }
      archiveFileDao.save(archiveFile);
      pubFulltext.setGmtModified(new Date());
      pubFullTextDAO.update(pubFulltext);
      this.updateSnsToImageStatus(pubFulltext.getPubId(), 1, "pdf全文转换图片成功");
    } catch (Exception e) {
      // logger.error("基准库pdf转图片离线任务处理时出现异常！该记录id={}",
      // pubFulltext.getPubId());
      this.updateSnsToImageStatus(pubFulltext.getPubId(), 3, "基准库pdf转图片离线任务处理时出现异常");
    }
  }

  @Override
  public void updatePdwhToImageStatus(Long pdwhPubId, int status, String msg) {
    pubFulltextUploadLogDao.updatePdwhToImageStatus(pdwhPubId, status, msg);
  }

  @Override
  public void updateSnsToImageStatus(Long pubId, int status, String msg) {
    pubFulltextUploadLogDao.updateSnsToImageStatus(pubId, status, msg);
  }

  /**
   * 检查文件是否是图片类型的文件
   * 
   * @author houchuanjie
   * @date 2018年1月10日 下午8:31:27
   * @param archiveFile
   * @return 是图片返回true，不是返回false
   */
  private boolean isImageFile(ArchiveFile archiveFile) {
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
   * @param archiveFile
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
