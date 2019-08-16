package com.smate.sie.center.task.service;

import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.smate.center.task.exception.ServiceException;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.file.ArchiveFileUtil;
import com.smate.core.base.utils.file.FileService;
import com.smate.core.base.utils.file.FileUtils;
import com.smate.core.base.utils.file.FileUtils.FileNameExtension;
import com.smate.core.base.utils.image.im4java.gm.GMImageUtil;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.sie.center.task.dao.PubPdwhSieRealtionDao;
import com.smate.sie.center.task.dao.SieArchiveFileDao;
import com.smate.sie.center.task.dao.SiePrjFullTextRefreshDao;
import com.smate.sie.center.task.dao.SiePrjFulltextDao;
import com.smate.sie.center.task.model.PubPdwhSieRelation;
import com.smate.sie.center.task.model.SieArchiveFile;
import com.smate.sie.center.task.model.SiePrjFullTextRefresh;
import com.smate.sie.center.task.model.SiePrjFulltext;
import com.smate.sie.core.base.utils.dao.prj.SieProjectDao;
import com.smate.sie.core.base.utils.model.prj.SieProject;

/***
 * 项目全文缩略图生成
 * 
 * @author 叶星源
 * @Date 20180824
 */
@Service("SiePrjFulltextToImageService")
@Transactional(rollbackOn = Exception.class)
public class SiePrjFulltextToImageServiceImpl implements SiePrjFulltextToImageService {

  @Autowired
  private SieArchiveFileDao archiveFilesDao;
  @Autowired
  private SieProjectDao sieProjectDao;
  @Autowired
  private SiePrjFulltextDao siePrjFulltextDao;
  @Autowired
  private SieArchiveFilesService archiveFilesService;
  @Autowired
  private SiePrjFullTextRefreshDao siePrjFullTextRefreshDao;
  @Value("${sie.file.root}")
  private String sieFileRoot;
  @Autowired
  private FileService fileService;
  @Autowired
  private PubPdwhSieRealtionDao pubPdwhSieRealtionDao;

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public List<SiePrjFullTextRefresh> getNeedRefreshData(int size) {
    return siePrjFullTextRefreshDao.loadNeedCountUnitId(size);
  }

  @Override
  public void delPrjFulltextImageRefresh(Long prjId) {
    SiePrjFullTextRefresh SiePrjFullTextRefresh = siePrjFullTextRefreshDao.get(prjId);
    if (SiePrjFullTextRefresh != null) {
      SiePrjFullTextRefresh.setStatus(1);
      siePrjFullTextRefreshDao.save(SiePrjFullTextRefresh);
    }
  }

  @Override
  public void refreshData(SiePrjFullTextRefresh siePrjFullTextRefresh) {
    Long prjId = siePrjFullTextRefresh.getPrjId();
    try {
      SieProject sieProject = sieProjectDao.get(prjId);
      if (existPubFile(prjId)) {
        // 首先删除项目全文图片，以免造成垃圾的附件.
        this.deletePrjFulltextImageByPubId(prjId);
        // 转换图片
        int pageIndex = 0;
        // 要检查SIE库的值
        String FulltextFileid = null;
        if (sieProject != null && StringUtils.isNotBlank(sieProject.getFulltextFileid())) {
          FulltextFileid = sieProject.getFulltextFileid();
        }
        if (StringUtils.isNoneEmpty(FulltextFileid)) {
          Map imageMap = this.fulltextConvertToImage(siePrjFullTextRefresh.getPrjId(), FulltextFileid, pageIndex);
          // 保存转换后的图片信息
          if (imageMap != null) {
            this.savePrjFulltextImage(prjId, FulltextFileid, pageIndex + 1, imageMap);
          }
        }
      } else {
        // 项目中不存在全文的文件ID时：
        convertFail(siePrjFullTextRefresh);
      }
      delPrjFulltextImageRefresh(prjId);
    } catch (FileNotFoundException ex) {
      logger.error("文件没找到异常：转换SIE项目的全文fulltextFileId={}为图片时，没有找到文件：{}", siePrjFullTextRefresh.getPrjId(), ex);
      convertFail(siePrjFullTextRefresh);
    } catch (Exception e) {
      logger.error("刷新项目prjId={}全文转换图片出现异常：{}", siePrjFullTextRefresh.getPrjId(), e);
      convertFail(siePrjFullTextRefresh);
      throw new ServiceException(e);
    }

  }

  /**
   * 从SIE库和基准库中取全文文件，若是对应的项目存在全文，则返回true
   */
  private boolean existPubFile(Long prjId) {
    SieProject sieProject = sieProjectDao.get(prjId);
    if (StringUtils.isNotBlank(sieProject.getFulltextFileid())) {
      return true;
    }
    PubPdwhSieRelation pubPdwhSieRelation = pubPdwhSieRealtionDao.get(prjId);
    if (pubPdwhSieRelation.getPdwhFileId() != null && !"".equals(pubPdwhSieRelation.getPdwhFileId())) {
      return true;
    }
    return false;
  }

  @Override
  public void savePrjFulltextImageRefresh(SiePrjFullTextRefresh siePrjFullTextRefresh) {
    this.siePrjFullTextRefreshDao.save(siePrjFullTextRefresh);
  }

  private void savePrjFulltextImage(Long prjId, String fulltextFileid, int fulltextImagePageIndex, Map imageMap) {
    SiePrjFulltext siePrjFulltext = this.siePrjFulltextDao.get(prjId);
    if (siePrjFulltext == null) {
      siePrjFulltext = new SiePrjFulltext();
      siePrjFulltext.setPrjId(prjId);
    }
    siePrjFulltext.setFulltextFileId(NumberUtils.createLong(fulltextFileid));
    siePrjFulltext.setFulltextImagePageIndex(fulltextImagePageIndex);
    siePrjFulltext.setFulltextImagePath(imageMap.get("imagePath") == null ? null : (String) imageMap.get("imagePath"));
    siePrjFulltext.setFulltextFileExt(imageMap.get("fileExt") == null ? null : (String) imageMap.get("fileExt"));
    siePrjFulltextDao.save(siePrjFulltext);
  }

  @SuppressWarnings("rawtypes")
  private Map fulltextConvertToImage(Long pubid, String fulltextFileid, int pageIndex) throws FileNotFoundException {
    try {
      Map<String, Object> fileMap = new LinkedHashMap<String, Object>();
      SieArchiveFile archiveFile = archiveFilesDao.get(Long.valueOf(fulltextFileid));
      if (archiveFile != null) {
        if (isPDFile(archiveFile)) {
          // 处理PDF
          String pdfFilePath = ArchiveFileUtil.getFilePath(archiveFile.getFilePath());
          String srcFilePath = sieFileRoot + "/" + ServiceConstants.DIR_UPFILE + pdfFilePath;
          String fullTextImageExt = "jpeg";
          String fullTextImagePath = "/" + ServiceConstants.DIR_PRJ_FULLTEXT_IMAGE
              + fileService.getFilePath(archiveFile.getFileId() + "_img_" + 1 + "." + fullTextImageExt);
          String desImagePath = sieFileRoot + fullTextImagePath;
          GMImageUtil.thumbnail(ArchiveFileUtil.THUMBNAIL_WIDTH_72, ArchiveFileUtil.THUMBNAIL_HEIGHT_92, true,
              srcFilePath, desImagePath);
          // 缩略图的地址
          fileMap.put("imagePath", fullTextImagePath);
          fileMap.put("fileExt", ArchiveFileUtil.FILE_TYPE_PDF);
          if (!ArchiveFileUtil.FILE_TYPE_PDF.equalsIgnoreCase(archiveFile.getFileType())) {
            // 更新文件类型
            archiveFile.setFileType(ArchiveFileUtil.FILE_TYPE_PDF);
            archiveFilesService.saveArchiveFile(archiveFile);
          }
        } else if (isImageFile(archiveFile)) {
          // 源图片相对路径
          String srcRelativePath = getSrcRelativePath(archiveFile);
          // 缩略图相对路径
          String destRelativePath = FileUtils.getFileNamePrefix(srcRelativePath).replace(ServiceConstants.DIR_UPFILE,
              ServiceConstants.DIR_PRJ_FULLTEXT_IMAGE) + ArchiveFileUtil.THUMBNAIL_SUFFIX;
          // 源图片绝对路径
          String srcPath = sieFileRoot + srcRelativePath;
          // 缩略图绝对路径
          String destPath = sieFileRoot + destRelativePath;
          GMImageUtil.thumbnail(ArchiveFileUtil.THUMBNAIL_WIDTH_72, ArchiveFileUtil.THUMBNAIL_HEIGHT_92, true, srcPath,
              destPath);
          fileMap.put("imagePath", destRelativePath);
          fileMap.put("fileExt", ArchiveFileUtil.FILE_TYPE_IMG);
          if (!ArchiveFileUtil.FILE_TYPE_IMG.equalsIgnoreCase(archiveFile.getFileType())) {
            // 更新文件类型
            archiveFile.setFileType(ArchiveFileUtil.FILE_TYPE_IMG);
            archiveFilesService.saveArchiveFile(archiveFile);
          }
        }
      }
      return fileMap;
    } catch (FileNotFoundException ex) {
      throw new FileNotFoundException(ex.getMessage());
    } catch (Exception e) {
      logger.error("异常：转换SIE项目的全文fulltextFileId={}为图片出现异常：{}", NumberUtils.createLong(fulltextFileid), e);
      throw new ServiceException(e);
    }
  }

  /**
   * 根据项目ID删除全文中对应的项目文件和全文缩略图
   */
  private void deletePrjFulltextImageByPubId(Long prjId) {
    try {
      SiePrjFulltext siePrjFulltext = this.siePrjFulltextDao.get(prjId);
      if (siePrjFulltext != null && StringUtils.isNotBlank(siePrjFulltext.getFulltextImagePath())) {
        String fulltextImagePath = siePrjFulltext.getFulltextImagePath();
        siePrjFulltext.setFulltextImagePath(null);
        siePrjFulltextDao.save(siePrjFulltext);
        this.archiveFilesService.deleteFileByPath(fulltextImagePath);
      }
    } catch (Exception e) {
      logger.error("删除项目prjId={}的全文转换后的图片出现异常：{}", prjId, e);
      throw new ServiceException(e);
    }
  }

  /**
   * 根据项目ID删除全文中对应的全文缩略图
   */
  private void deletePrjFulltextByPubId(Long prjId) {
    try {
      SiePrjFulltext siePrjFulltext = this.siePrjFulltextDao.get(prjId);
      if (siePrjFulltext != null) {
        if (StringUtils.isNotBlank(siePrjFulltext.getFulltextImagePath())) {
          // 删除全文后，同时删除全文所转换成的缩略图
          this.archiveFilesService.deleteFileByPath(siePrjFulltext.getFulltextImagePath());
        }
        siePrjFulltextDao.delete(siePrjFulltext);
      }
    } catch (Exception e) {
      logger.error("删除项目prjId={}的全文出现异常：{}", prjId, e);
      throw new ServiceException(e);
    }

  }

  /**
   * 检查文件是否是图片类型的文件
   * 
   * @return 是图片返回true，不是返回false
   */
  private boolean isImageFile(SieArchiveFile archiveFiles) {
    // 文件实际类型
    String fileType = archiveFiles.getFileType();
    boolean flag = ArchiveFileUtil.FILE_TYPE_IMG.equalsIgnoreCase(fileType);
    if (!flag) {
      final String fileName = archiveFiles.getFileName();
      // 判定是否是图片类型的后缀枚举
      flag = FileNameExtension.isImageFileNameExtension(FileUtils.getFileNameExtension(fileName));
    }
    return flag;
  }

  /**
   * 检查文件是否是pdf文件
   * 
   * @return 是pdf返回true,不是返回false
   */
  private boolean isPDFile(SieArchiveFile archiveFiles) {
    // 文件实际类型
    final String fileType = archiveFiles.getFileType();
    boolean flag = FileNameExtension.PDF.toString().equalsIgnoreCase(fileType);
    if (!flag) {
      final String fileName = archiveFiles.getFileName();
      FileNameExtension ext = FileUtils.getFileNameExtension(fileName);
      // 判断是否是pdf类型的后缀
      flag = FileNameExtension.PDF.equals(ext);
    }
    return flag;
  }

  /**
   * 获取图片文件的源路径（相对地址）
   * 
   * @param archiveFiles
   */
  private String getSrcRelativePath(SieArchiveFile archiveFiles) {
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

  @Override
  public void convertFail(SiePrjFullTextRefresh siePrjFullTextRefresh) {
    this.deletePrjFulltextByPubId(siePrjFullTextRefresh.getPrjId());
    siePrjFullTextRefresh.setStatus(99);
    siePrjFullTextRefreshDao.save(siePrjFullTextRefresh);
  }
}
