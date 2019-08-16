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
import com.smate.sie.center.task.dao.PubFullTextRefreshDao;
import com.smate.sie.center.task.dao.PubPdwhSieRealtionDao;
import com.smate.sie.center.task.dao.SiePubFulltextDao;
import com.smate.sie.center.task.model.PubFullTextRefresh;
import com.smate.sie.center.task.model.PubPdwhSieRelation;
import com.smate.sie.center.task.model.SiePubFulltext;
import com.smate.sie.center.task.pdwh.task.PubXmlDocument;
import com.smate.sie.core.base.utils.dao.pub.PubXmlDao;
import com.smate.sie.core.base.utils.dao.pub.SiePublicationDao;
import com.smate.sie.core.base.utils.model.pub.SiePubXml;
import com.smate.sie.core.base.utils.model.pub.SiePublication;

/***
 * 成果全文缩略图生成
 * 
 * @author 叶星源
 * @Date 20180801
 */
@Service("SiePubFulltextToImageService")
@Transactional(rollbackOn = Exception.class)
public class SiePubFulltextToImageServiceImpl implements SiePubFulltextToImageService {

  @Autowired
  private ArchiveFileDao archiveFileDao;
  @Autowired
  private SiePublicationDao siePublicationDao;
  @Autowired
  private SiePubFulltextDao siePubFulltextDao;
  @Autowired
  private PubXmlDao siePubXmlDao;
  @Autowired
  private ArchiveFilesService archiveFilesService;
  @Autowired
  private PubFullTextRefreshDao siePubFullTextRefreshDao;

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
  public List<PubFullTextRefresh> getNeedRefreshData(int size) {
    return siePubFullTextRefreshDao.loadNeedCountUnitId(size);
  }

  @Override
  public void delPubFulltextImageRefresh(Long pubId) {
    PubFullTextRefresh pubFullTextRefresh = siePubFullTextRefreshDao.get(pubId);
    if (pubFullTextRefresh != null) {
      pubFullTextRefresh.setStatus(1);
      siePubFullTextRefreshDao.save(pubFullTextRefresh);
    }
  }

  @Override
  public void refreshData(PubFullTextRefresh pubFullTextRefresh) {
    Long pubId = pubFullTextRefresh.getPubId();
    try {
      SiePublication siePublication = siePublicationDao.get(pubId);
      if (existPubFile(pubId)) {
        // 首先删除成果全文图片，以免造成垃圾的附件.
        this.deletePubFulltextImageByPubId(pubId);
        // 转换图片
        int pageIndex = 0;
        // 要检查SIE库和基准库两个地方的值
        String FulltextFileid = null;
        if (siePublication != null && StringUtils.isNotBlank(siePublication.getFulltextFileid())) {
          FulltextFileid = siePublication.getFulltextFileid();
        } else {
          // SIE库中没有FileId则查基准库的FileId值
          PubPdwhSieRelation pubPdwhSieRelation = pubPdwhSieRealtionDao.get(pubId);
          FulltextFileid = pubPdwhSieRelation.getPdwhFileId() == null ? null : pubPdwhSieRelation.getPdwhFileId() + "";
        }
        if (StringUtils.isNoneEmpty(FulltextFileid)) {
          Map imageMap = this.fulltextConvertToImage(pubFullTextRefresh.getPubId(), FulltextFileid, pageIndex);
          // 保存转换后的图片信息
          if (imageMap != null) {
            this.savePubFulltextImage(pubId, FulltextFileid, pageIndex + 1, imageMap);
          }
        }

      } else {
        // 成果中不存在全文的文件ID时：
        this.deletePubFulltextByPubId(pubId);
        this.deletePubXmlFulltext(pubId);
      }
    } catch (FileNotFoundException ex) {
      this.deletePubFulltextByPubId(pubId);
      this.delPubFulltextImageRefresh(pubId);
    } catch (Exception e) {
      logger.error("刷新成果pubId={}全文转换图片出现异常：{}", pubFullTextRefresh.getPubId(), e);
      throw new ServiceException(e);
    }

  }

  private boolean existPubFile(Long pubId) {
    SiePublication siePublication = siePublicationDao.get(pubId);
    if (StringUtils.isNotBlank(siePublication.getFulltextFileid())) {
      return true;
    }
    PubPdwhSieRelation pubPdwhSieRelation = pubPdwhSieRealtionDao.get(pubId);
    if (pubPdwhSieRelation.getPdwhFileId() != null && !"".equals(pubPdwhSieRelation.getPdwhFileId())) {
      return true;
    }
    return false;
  }

  // 清楚全文节点的数据信息
  private void deletePubXmlFulltext(Long pubId) {
    try {
      SiePubXml pubXml = this.siePubXmlDao.get(pubId);
      if (pubXml != null) {
        String xml = pubXml.getPubXml();
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
          pubXml.setPubId(pubId);
          pubXml.setPubXml(xmlDocument.getXmlString());
          siePubXmlDao.save(pubXml);
        }
      }
    } catch (Exception e) {
      logger.error("删除成果pubId={} xml的全文", pubId);
      throw new ServiceException(e);
    }

  }

  @Override
  public void savePubFulltextImageRefresh(PubFullTextRefresh pubFullTextRefresh) {
    this.siePubFullTextRefreshDao.save(pubFullTextRefresh);
  }

  private void savePubFulltextImage(Long pubId, String fulltextFileid, int fulltextImagePageIndex, Map imageMap) {
    SiePubFulltext siePubFulltext = this.siePubFulltextDao.get(pubId);
    if (siePubFulltext == null) {
      siePubFulltext = new SiePubFulltext();
      siePubFulltext.setPubId(pubId);
    }
    siePubFulltext.setFulltextFileId(NumberUtils.createLong(fulltextFileid));
    siePubFulltext.setFulltextImagePageIndex(fulltextImagePageIndex);
    siePubFulltext.setFulltextImagePath(imageMap.get("imagePath") == null ? null : (String) imageMap.get("imagePath"));
    siePubFulltext.setFulltextFileExt(imageMap.get("fileExt") == null ? null : (String) imageMap.get("fileExt"));
    siePubFulltextDao.save(siePubFulltext);
  }

  @SuppressWarnings("rawtypes")
  private Map fulltextConvertToImage(Long pubid, String fulltextFileid, int pageIndex) throws FileNotFoundException {
    try {
      Map<String, Object> fileMap = new LinkedHashMap<String, Object>();
      ArchiveFile archiveFile = archiveFileDao.get(Long.valueOf(fulltextFileid));
      if (archiveFile != null) {
        if (isPDFile(archiveFile)) {
          // 处理PDF
          String pdfFilePath = ArchiveFileUtil.getFilePath(archiveFile.getFilePath());
          String srcFilePath = fileRoot + "/" + ServiceConstants.DIR_UPFILE + pdfFilePath;
          String fullTextImageExt = "jpeg";
          String fullTextImagePath = "/" + ServiceConstants.DIR_PUB_FULLTEXT_IMAGE
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
              ServiceConstants.DIR_PUB_FULLTEXT_IMAGE) + ArchiveFileUtil.THUMBNAIL_SUFFIX;
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
        }
      }
      return fileMap;
    } catch (FileNotFoundException ex) {
      throw new FileNotFoundException(ex.getMessage());
    } catch (Exception e) {
      logger.error("转换SIE成果的全文fulltextFileId={}为图片出现异常：{}", NumberUtils.createLong(fulltextFileid), e);
      throw new ServiceException(e);
    }
  }

  // 根据成果ID删除全文中对应的成果文件
  private void deletePubFulltextImageByPubId(Long pubId) {
    try {
      SiePubFulltext siePubFulltext = this.siePubFulltextDao.get(pubId);
      if (siePubFulltext != null && StringUtils.isNotBlank(siePubFulltext.getFulltextImagePath())) {
        String fulltextImagePath = siePubFulltext.getFulltextImagePath();
        siePubFulltext.setFulltextImagePath(null);
        siePubFulltextDao.save(siePubFulltext);
        this.archiveFilesService.deleteFileByPath(fulltextImagePath);
      }
    } catch (Exception e) {
      logger.error("删除成果pubId={}的全文转换后的图片出现异常：{}", pubId, e);
      throw new ServiceException(e);
    }
  }

  // 根据成果ID删除全文中对应的全文缩略图
  private void deletePubFulltextByPubId(Long pubId) {
    try {
      SiePubFulltext siePubFulltext = this.siePubFulltextDao.get(pubId);
      if (siePubFulltext != null) {
        if (StringUtils.isNotBlank(siePubFulltext.getFulltextImagePath())) {
          // 删除全文后，同时删除全文附件所转换成的图片
          this.archiveFilesService.deleteFileByPath(siePubFulltext.getFulltextImagePath());
        }
        siePubFulltextDao.delete(siePubFulltext);
      }
    } catch (Exception e) {
      logger.error("删除成果pubId={}的全文出现异常：{}", pubId, e);
      throw new ServiceException(e);
    }

  }

  /**
   * 检查文件是否是图片类型的文件
   * 
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

  /**
   * 获取图片文件的源路径（相对地址）
   * 
   * @param archiveFile
   */
  private String getSrcRelativePath(ArchiveFile archiveFile) {
    // 文件相对路径
    String relativeFilePath = archiveFile.getFileUrl();
    // 如果relativeFilePath是空的，那么根据filePath获取文件路径
    if (ArchiveFileUtil.FILE_URL_DEFAULT_VALUE.equals(relativeFilePath)) {
      relativeFilePath = FileUtils.SYMBOL_VIRGULE_CHAR + ServiceConstants.DIR_UPFILE
          + ArchiveFileUtil.getFilePath(archiveFile.getFilePath());
    }
    if (relativeFilePath.charAt(0) != FileUtils.SYMBOL_VIRGULE_CHAR) {
      relativeFilePath = FileUtils.SYMBOL_VIRGULE_CHAR + relativeFilePath;
    }
    return relativeFilePath;
  }
}
