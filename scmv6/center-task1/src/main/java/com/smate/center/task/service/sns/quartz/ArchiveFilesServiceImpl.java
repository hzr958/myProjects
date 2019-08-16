package com.smate.center.task.service.sns.quartz;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.exception.DataException;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.quartz.ArchiveFileDao;
import com.smate.center.task.exception.InputFullTextTaskException;
import com.smate.center.task.model.sns.quartz.ArchiveFile;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.file.ArchiveFileUtil;
import com.smate.core.base.utils.file.FileService;
import com.smate.core.base.utils.file.FileUtils;
import com.smate.core.base.utils.file.OperateImageUtils;
import com.smate.core.base.utils.file.PdfConvertToImageUtils;

/**
 * 附件管理.
 * 
 * @author zk
 */
@Service("archiveFilesService")
@Transactional(rollbackFor = Exception.class)
public class ArchiveFilesServiceImpl implements ArchiveFilesService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

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
  public ArchiveFile getArchiveFilesById(Long fileId) throws Exception {
    return archiveFileDao.findArchiveFileById(fileId);
  }

  @Override
  public void saveArchiveFile(ArchiveFile a) {
    archiveFileDao.save(a);
  }

  @Override
  public ArchiveFile getArchiveFiles(Long id) throws Exception {
    try {

      return archiveFileDao.findArchiveFileById(id);

    } catch (DataException e) {
      logger.error("getArchiveFiles获取附件失败 : ", e);
      throw new InputFullTextTaskException(e);
    }
  }

  @Override
  @Deprecated
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
      String srcFilePath = realPath + "/" + ServiceConstants.DIR_UPFILE + filePath;
      String desPath = fileService.getFilePath(fileId + "_img" + "." + fileType);
      String desImagePath = realPath + "/" + baseDir + desPath;
      OperateImageUtils.reduceImageEqualProportion(srcFilePath, desImagePath, ratio);

      return "/" + baseDir + desPath;
    } catch (Exception e) {
      logger.error("按比例缩小图片附件fileId={}出现异常：{}", fileId, e);
      String errorMsg = "比例缩小图片：" + e.getMessage();
      throw new Exception(errorMsg);
    }
  }

  public String getFileRoot() {
    return fileRoot;
  }

  public void setFileRoot(String fileRoot) {
    this.fileRoot = fileRoot;
  }

  @Override
  public String getArchiveFilePath(Long id, String baseDir) throws InputFullTextTaskException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String cutInsLogoImage(Long insId, Integer x, Integer y, Integer width, Integer height)
      throws InputFullTextTaskException {
    // TODO Auto-generated method stub
    return null;
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

  @Override
  public List<ArchiveFile> getEmptySizeFile(Integer size) {
    return archiveFileDao.batchGetEmptyFile(size);
  }

  @Override
  public void generateFileSize(ArchiveFile archiveFile) throws Exception {
    String path = fileRoot + FileUtils.SYMBOL_VIRGULE_CHAR + ServiceConstants.DIR_UPFILE
        + ArchiveFileUtil.getFilePath(archiveFile.getFilePath());
    File file = new File(path);
    Long size = null;
    if (file.exists() && file.isFile()) {
      size = file.length();
      if (size == 0) {
        archiveFile.setFileSize(-9L);
      } else {
        archiveFile.setFileSize(size);
      }
    } else {
      archiveFile.setFileSize(-1L);
      logger.error("文件不存在！fileId" + archiveFile.getFileId());
    }
    archiveFileDao.save(archiveFile);

  }

}
