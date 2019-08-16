package com.smate.sie.center.task.service;

import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.smate.center.task.dao.sns.quartz.ArchiveFileDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.quartz.ArchiveFile;
import com.smate.center.task.service.sns.quartz.ArchiveFilesService;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.file.ArchiveFileUtil;
import com.smate.core.base.utils.file.FileService;
import com.smate.core.base.utils.file.FileUtils;
import com.smate.core.base.utils.file.FileUtils.FileNameExtension;
import com.smate.core.base.utils.image.im4java.gm.GMImageUtil;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.sie.center.task.dao.PubPdwhSieRealtionDao;
import com.smate.sie.center.task.dao.SiePatFullTextRefreshDao;
import com.smate.sie.center.task.dao.SiePatFulltextDao;
import com.smate.sie.center.task.model.PubPdwhSieRelation;
import com.smate.sie.center.task.model.SiePatFullTextRefresh;
import com.smate.sie.center.task.model.SiePatFulltext;
import com.smate.sie.center.task.pdwh.task.PubXmlDocument;
import com.smate.sie.core.base.utils.dao.pub.SiePatXmlDao;
import com.smate.sie.core.base.utils.dao.pub.SiePatentDao;
import com.smate.sie.core.base.utils.model.pub.SiePatXml;
import com.smate.sie.core.base.utils.model.pub.SiePatent;

/***
 * 专利全文缩略图生成
 * 
 * @author 叶星源
 * @Date 20180824
 */
@Service("SiePatFulltextToImageService")
@Transactional(rollbackOn = Exception.class)
public class SiePatFulltextToImageServiceImpl implements SiePatFulltextToImageService {

  @Autowired
  private ArchiveFileDao archiveFileDao;
  @Autowired
  private SiePatentDao siePatentDao;
  @Autowired
  private SiePatFulltextDao siePatFulltextDao;
  @Autowired
  private SiePatXmlDao siePatXmlDao;
  @Autowired
  private ArchiveFilesService archiveFilesService;
  @Autowired
  private SiePatFullTextRefreshDao siePatFullTextRefreshDao;

  @Value("${file.root}")
  private String fileRoot;
  @Value("${sie.file.root}")
  private String sieFileRoot;
  @Autowired
  private FileService fileService;
  @Autowired
  private PubPdwhSieRealtionDao pubPdwhSieRealtionDao;

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public List<SiePatFullTextRefresh> getNeedRefreshData(int size) {
    return siePatFullTextRefreshDao.loadNeedCountUnitId(size);
  }

  /**
   * 转换成功时的处理
   */
  @Override
  public void delPatFulltextImageRefresh(Long patId) {
    SiePatFullTextRefresh PatFullTextRefresh = siePatFullTextRefreshDao.get(patId);
    if (PatFullTextRefresh != null) {
      PatFullTextRefresh.setStatus(1);
    }
    siePatFullTextRefreshDao.save(PatFullTextRefresh);
  }

  @Override
  public void refreshData(SiePatFullTextRefresh siePatFullTextRefresh) {
    Long patId = siePatFullTextRefresh.getPatId();
    try {
      SiePatent siePatent = siePatentDao.get(patId);
      if (existPubFile(patId)) {
        // 首先删除专利全文图片，以免造成垃圾的附件.
        this.deletePatFulltextImageByPubId(patId);
        // 转换图片
        int pageIndex = 0;
        // 要检查SIE库的值
        String FulltextFileid = null;
        if (siePatent != null && StringUtils.isNotBlank(siePatent.getFulltextFileid())) {
          FulltextFileid = siePatent.getFulltextFileid();
        } else {
          // SIE库中没有FileId则查基准库的FileId值
          PubPdwhSieRelation pubPdwhSieRelation = pubPdwhSieRealtionDao.get(patId);
          FulltextFileid = pubPdwhSieRelation.getPdwhFileId() == null ? null : pubPdwhSieRelation.getPdwhFileId() + "";
        }
        if (StringUtils.isNoneEmpty(FulltextFileid)) {
          Map imageMap = this.fulltextConvertToImage(siePatFullTextRefresh.getPatId(), FulltextFileid, pageIndex);
          // 保存转换后的图片信息
          if (imageMap != null) {
            this.savePatFulltextImage(patId, FulltextFileid, pageIndex + 1, imageMap);
          }
        }
        delPatFulltextImageRefresh(patId);
      } else {
        // 专利中不存在全文的文件ID时：
        convertFail(siePatFullTextRefresh);
      }
    } catch (FileNotFoundException ex) {
      logger.error("文件没找到异常：转换SIE专利的全文fulltextFileId={}为图片时，没有找到文件：{}", siePatFullTextRefresh.getPatId(), ex);
      convertFail(siePatFullTextRefresh);
    } catch (Exception e) {
      logger.error("异常：刷新专利patId={}全文转换图片出现异常：{}", siePatFullTextRefresh.getPatId(), e);
      convertFail(siePatFullTextRefresh);
      throw new ServiceException(e);
    }

  }

  /**
   * 转换失败的处理
   */
  @Override
  public void convertFail(SiePatFullTextRefresh siePatFullTextRefresh) {
    this.deletePatFulltextByPubId(siePatFullTextRefresh.getPatId());
    siePatFullTextRefresh.setStatus(99);
    this.savePatFulltextImageRefresh(siePatFullTextRefresh);
  }

  /**
   * 判断专利是否存在
   */
  private boolean existPubFile(Long patId) {
    SiePatent siePatent = siePatentDao.get(patId);
    if (StringUtils.isNotBlank(siePatent.getFulltextFileid())) {
      return true;
    }
    // 专利也是从专利关系表里取数据
    PubPdwhSieRelation pubPdwhSieRelation = pubPdwhSieRealtionDao.get(patId);
    if (pubPdwhSieRelation.getPdwhFileId() != null && !"".equals(pubPdwhSieRelation.getPdwhFileId())) {
      return true;
    }
    return false;
  }

  /**
   * 清楚全文节点的数据信息
   */
  private void deletePubXmlFulltext(Long patId) {
    try {
      SiePatXml siePatentXml = this.siePatXmlDao.get(patId);
      if (siePatentXml != null) {
        String xml = siePatentXml.getPatXml();
        if (StringUtils.isNotBlank(xml)) {
          PubXmlDocument xmlDocument = new PubXmlDocument(xml);
          Element fullText = (Element) xmlDocument.getNode(PubXmlConstants.PUB_FULLTEXT_XPATH);
          if (fullText == null) {
            xmlDocument.createElement(PubXmlConstants.PUB_FULLTEXT_XPATH);
          }
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "permission", "");
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "node_id", "");
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "file_id", "");
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "file_name", "");
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "file_ext", "");
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "file_desc", "");
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "upload_date", "");
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "ins_id", "");
          siePatentXml.setPatId(patId);
          siePatentXml.setPatXml(xmlDocument.getXmlString());
          siePatXmlDao.save(siePatentXml);
        }
      }
    } catch (Exception e) {
      logger.error("删除专利patId={} xml的全文", patId);
      throw new ServiceException(e);
    }

  }

  private void savePatFulltextImage(Long patId, String fulltextFileid, int fulltextImagePageIndex, Map imageMap) {
    SiePatFulltext siePatFulltext = this.siePatFulltextDao.get(patId);
    if (siePatFulltext == null) {
      siePatFulltext = new SiePatFulltext();
      siePatFulltext.setPatId(patId);
    }
    siePatFulltext.setFulltextFileId(NumberUtils.createLong(fulltextFileid));
    siePatFulltext.setFulltextImagePageIndex(fulltextImagePageIndex);
    siePatFulltext.setFulltextImagePath(imageMap.get("imagePath") == null ? null : (String) imageMap.get("imagePath"));
    siePatFulltext.setFulltextFileExt(imageMap.get("fileExt") == null ? null : (String) imageMap.get("fileExt"));
    siePatFulltextDao.save(siePatFulltext);
  }

  @SuppressWarnings("rawtypes")
  private Map fulltextConvertToImage(Long patId, String fulltextFileid, int pageIndex) throws FileNotFoundException {
    try {
      Map<String, Object> fileMap = new LinkedHashMap<String, Object>();
      ArchiveFile archiveFile = archiveFileDao.get(Long.valueOf(fulltextFileid));
      if (archiveFile != null) {
        if (isPDFile(archiveFile)) {
          // 处理PDF
          String pdfFilePath = ArchiveFileUtil.getFilePath(archiveFile.getFilePath());
          String srcFilePath = fileRoot + "/" + ServiceConstants.DIR_UPFILE + pdfFilePath;
          String fullTextImageExt = "jpeg";
          String fullTextImagePath = "/" + ServiceConstants.DIR_PAT_FULLTEXT_IMAGE
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
            archiveFile.setFileFrom("pdwh");
            archiveFilesService.saveArchiveFile(archiveFile);
          }
        } else if (isImageFile(archiveFile)) {
          // 源图片相对路径
          String srcRelativePath = getSrcRelativePath(archiveFile);
          // 缩略图相对路径
          String destRelativePath = FileUtils.getFileNamePrefix(srcRelativePath).replace(ServiceConstants.DIR_UPFILE,
              ServiceConstants.DIR_PAT_FULLTEXT_IMAGE) + ArchiveFileUtil.THUMBNAIL_SUFFIX;
          // 源图片绝对路径
          String srcPath = fileRoot + srcRelativePath;
          // 缩略图绝对路径
          String destPath = sieFileRoot + destRelativePath;
          GMImageUtil.thumbnail(ArchiveFileUtil.THUMBNAIL_WIDTH_72, ArchiveFileUtil.THUMBNAIL_HEIGHT_92, true, srcPath,
              destPath);
          fileMap.put("imagePath", destRelativePath);
          fileMap.put("fileExt", ArchiveFileUtil.FILE_TYPE_IMG);
          if (!ArchiveFileUtil.FILE_TYPE_IMG.equalsIgnoreCase(archiveFile.getFileType())) {
            // 更新文件类型
            archiveFile.setFileType(ArchiveFileUtil.FILE_TYPE_IMG);
            archiveFile.setFileFrom("pdwh");
            archiveFilesService.saveArchiveFile(archiveFile);
          }
        } else {
          // 不是系统支持的类型
          this.savePatFulltextImageRefresh(patId, 3);
        }
      }
      return fileMap;
    } catch (FileNotFoundException ex) {
      throw new FileNotFoundException(ex.getMessage());
    } catch (Exception e) {
      logger.error("转换SIE专利的全文fulltextFileId={}为图片出现异常：{}", NumberUtils.createLong(fulltextFileid), e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void savePatFulltextImageRefresh(SiePatFullTextRefresh prjFullTextRefresh) {
    this.siePatFullTextRefreshDao.save(prjFullTextRefresh);
  }

  private void savePatFulltextImageRefresh(Long patId, int status) {
    SiePatFullTextRefresh siePatFullTextRefresh = siePatFullTextRefreshDao.get(patId);
    if (siePatFullTextRefresh == null) {
      siePatFullTextRefresh = new SiePatFullTextRefresh(patId, null);
    }
    siePatFullTextRefresh.setStatus(status);
    siePatFullTextRefreshDao.save(siePatFullTextRefresh);
  }

  /**
   * 根据专利ID删除全文中对应的专利文件
   */
  private void deletePatFulltextImageByPubId(Long patId) {
    try {
      SiePatFulltext siePatFulltext = this.siePatFulltextDao.get(patId);
      if (siePatFulltext != null && StringUtils.isNotBlank(siePatFulltext.getFulltextImagePath())) {
        String fulltextImagePath = siePatFulltext.getFulltextImagePath();
        siePatFulltext.setFulltextImagePath(null);
        siePatFulltextDao.save(siePatFulltext);
        this.archiveFilesService.deleteFileByPath(fulltextImagePath);
      }
    } catch (Exception e) {
      logger.error("删除专利patId={}的全文转换后的图片出现异常：{}", patId, e);
      throw new ServiceException(e);
    }
  }

  /**
   * 根据专利ID删除全文中对应的全文缩略图
   */
  private void deletePatFulltextByPubId(Long patId) {
    try {
      SiePatFulltext siePatFulltext = this.siePatFulltextDao.get(patId);
      if (siePatFulltext != null) {
        if (StringUtils.isNotBlank(siePatFulltext.getFulltextImagePath())) {
          // 删除全文后，同时删除全文附件所转换成的图片
          this.archiveFilesService.deleteFileByPath(siePatFulltext.getFulltextImagePath());
        }
        siePatFulltextDao.delete(siePatFulltext);
      }
    } catch (Exception e) {
      logger.error("删除专利patId={}的全文出现异常：{}", patId, e);
      throw new ServiceException(e);
    }

  }

  /**
   * 检查文件是否是图片类型的文件
   * 
   * @return 是图片返回true，不是返回false
   */
  private boolean isImageFile(ArchiveFile archiveFiles) {
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
  private boolean isPDFile(ArchiveFile archiveFiles) {
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
}
