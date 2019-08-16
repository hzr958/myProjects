package com.smate.center.batch.service.pub.archiveFiles;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.exception.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.core.base.file.dao.ArchiveFileDao;
import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.file.FileService;
import com.smate.core.base.utils.file.PdfConvertToImageUtils;

import net.coobird.thumbnailator.Thumbnails;

/**
 * 附件管理.
 * 
 * @author zk
 */
@Service("archiveFilesService")
@Transactional(rollbackFor = Exception.class)
public class ArchiveFilesServiceImpl implements ArchiveFilesService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private static final int THUMB_WIDTH = 72;
  private static final int THUMB_HEIGHT = 92;

  private String fileRoot;
  @Autowired
  private ArchiveFileDao archiveFileDao;
  @Autowired
  private FileService fileService;

  @Override
  public void deleteFileByPath(String filePath) throws ServiceException {
    try {
      String realPath = getFileRoot();
      File file = new File(realPath + filePath);
      if (file.exists()) {
        file.delete();
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
  public void saveArchiveFile(ArchiveFile a) {
    archiveFileDao.save(a);
  }

  public void updateArchiveFile(ArchiveFile af) {
    archiveFileDao.update(af);
  }

  @Override
  public ArchiveFile getArchiveFile(Long id) throws BatchTaskException {
    try {

      return archiveFileDao.findArchiveFileById(id);

    } catch (DataException e) {
      logger.error("getArchiveFiles获取附件失败 : ", e);
      throw new BatchTaskException(e);
    }
  }

  @Override
  public String pdfConvertToImage(Long fileId, String filePath, String baseDir, float scale, int pageIndex)
      throws InterruptedException, Exception, FileNotFoundException {
    try {
      String realPath = getFileRoot();
      String srcFilePath = realPath + "/" + ServiceConstants.DIR_UPFILE + filePath;
      String imageExt = "jpeg";
      String desPath = fileService.getFilePath(fileId + "_img_" + pageIndex + "." + imageExt);
      String desImagePath = realPath + "/" + baseDir + desPath;
      PdfConvertToImageUtils.convertToImage(srcFilePath, desImagePath, imageExt, scale, pageIndex);

      return "/" + baseDir + desPath;
    } catch (FileNotFoundException ex) {
      throw new FileNotFoundException(ex.getMessage());
    } catch (Exception e) {
      logger.error("pdf类型附件fileId={}转换成图片出现异常：{}", fileId, e);
      throw new Exception(e);
    }
  }

  @Override
  public String reduceImage(Long fileId, String filePath, String baseDir, String fileType, float ratio)
      throws Exception {
    try {
      String realPath = getFileRoot();
      String srcFilePath = realPath + "/" + ServiceConstants.DIR_UPFILE + fileService.getFilePath(filePath);
      // String desPath = fileService.getFilePath(fileId + "_img" + "." +
      // fileType);
      // String desImagePath = realPath + "/" + baseDir + desPath;
      // OperateImageUtils.reduceImageEqualProportion(srcFilePath,desImagePath,
      // ratio);
      // 取文件全路径包括文件后缀名，然后添加_thumb后缀
      int index = filePath.lastIndexOf(".");
      if (index == -1) {
        index = filePath.length();
      }
      String fileName = filePath.substring(0, index) + "_thumb." + fileType;
      String destPath = fileService.getFilePath(fileName);
      String destFilePath = "/" + baseDir + destPath;
      String thumbFilePath = realPath + destFilePath;
      File thumbFile = new File(thumbFilePath);
      if (!thumbFile.getParentFile().exists()) {
        thumbFile.getParentFile().mkdirs();
      }
      Thumbnails.of(srcFilePath).size(THUMB_WIDTH, THUMB_HEIGHT).keepAspectRatio(false).toFile(thumbFile);
      return destFilePath;
    } catch (Exception e) {
      logger.error("生成全文图片缩略图出现异常! fileId={} ", fileId, e);
      String errorMsg = "生成全文图片缩略图出现异常：" + e.getMessage();
      throw new Exception(errorMsg);
    }
  }

  public String getFileRoot() {
    return fileRoot;
  }

  public void setFileRoot(String fileRoot) {
    this.fileRoot = fileRoot;
  }

  public static void main(String[] args) {

    String fileName = "550b407718064e37896abb201d65fd87.pdf";// "91b9191493f94b97a6535f1f579d336c.pdf";//"3246d45337ba43bc85ad7abb639e3b37.pdf";//"64b387c0eee247649547aace0785935b.jpg";//"aa8b02717b1a414eabb6ab05661a4b4b.jpg";
    String secr = DigestUtils.md5Hex(fileName);
    StringBuilder sb = new StringBuilder("/");
    sb.append(secr.substring(1, 2)).append(secr.substring(3, 4)).append("/");
    sb.append(secr.substring(5, 6)).append(secr.substring(7, 8)).append("/");
    sb.append(secr.substring(9, 10)).append(secr.substring(11, 12)).append("/");
    String dir = sb.toString();
    String fileDir = dir + fileName;

    System.out.println(fileDir);
  }

}
