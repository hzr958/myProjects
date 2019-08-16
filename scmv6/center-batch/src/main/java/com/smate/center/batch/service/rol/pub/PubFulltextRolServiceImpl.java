package com.smate.center.batch.service.rol.pub;

import java.io.FileNotFoundException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.rol.pub.PubFulltextImageRefreshRolDao;
import com.smate.center.batch.dao.rol.pub.PubFulltextRolDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubFulltextImageRefreshRol;
import com.smate.center.batch.model.rol.pub.PubFulltextRol;
import com.smate.center.batch.model.sns.pub.PublicationXml;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.PublicationXmlService;
import com.smate.center.batch.service.pub.archiveFiles.ArchiveFilesService;
import com.smate.center.batch.util.pub.LogUtils;
import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.file.FileService;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.service.consts.SysDomainConst;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 成果全文ServiceImpl.
 * 
 * @author pwl
 * 
 */
@Service("pubFulltextRolService")
@Transactional(rollbackFor = Exception.class)
public class PubFulltextRolServiceImpl implements PubFulltextRolService {

  /**
   * 
   */
  private static final long serialVersionUID = -4429315862607704079L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubFulltextImageRefreshRolDao pubFulltextImageRefreshDao;
  @Autowired
  private PubFulltextRolDao pubFulltextRolDao;
  @Autowired
  private FileService fileService;
  @Autowired
  private SysDomainConst sysDomainConst;
  @Autowired
  private ArchiveFilesService archiveFilesService;
  @Autowired
  private PublicationXmlService publicationXmlService;

  @Override
  public void savePubFulltextImageRefresh(Long pubId, Long fulltextFileId, Integer fulltextNode, String fulltextFileExt)
      throws ServiceException {
    try {
      PubFulltextImageRefreshRol refresh =
          new PubFulltextImageRefreshRol(pubId, fulltextFileId, fulltextNode, fulltextFileExt);
      this.pubFulltextImageRefreshDao.save(refresh);
    } catch (Exception e) {
      logger.error("保存成果pubId={}全文转换成图片的刷新记录出现异常：{}", pubId, e);
    }
  }

  @Override
  public void savePubFulltextImageRefresh(PubFulltextImageRefreshRol pubFulltextImageRefresh) throws ServiceException {
    try {
      pubFulltextImageRefreshDao.save(pubFulltextImageRefresh);
    } catch (Exception e) {
      logger.error("保存成果全文转换图片刷新记录出现异常：", e);
    }
  }

  @Override
  public void delPubFulltextImageRefresh(Long id) throws ServiceException {
    try {
      this.pubFulltextImageRefreshDao.delete(id);
    } catch (Exception e) {
      logger.error("删除成果全文转换图片刷新记录出现异常：", e);
    }
  }

  @Override
  public List<PubFulltextImageRefreshRol> getNeedRefreshData(int maxSize) throws ServiceException {
    try {
      return this.pubFulltextImageRefreshDao.queryRefreshDataBatch(maxSize);
    } catch (Exception e) {
      logger.error("批量查询需要转换成图片的成果全文刷新数据出现异常：", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void refreshData(PubFulltextImageRefreshRol pubFulltextImageRefresh) throws ServiceException {

    Long pubId = pubFulltextImageRefresh.getPubId();
    try {
      if (pubFulltextImageRefresh.getFulltextFileId() != null) {
        Integer fulltextNode = pubFulltextImageRefresh.getFulltextNode();
        if (fulltextNode == null) {
          fulltextNode = SecurityUtils.getCurrentAllNodeId().get(0);
        }
        // 首先删除成果全文图片，以免造成垃圾的附件.
        this.deletePubFulltextImageByPubId(pubId);

        // 转换图片
        int pageIndex = 0;
        String imagePath = this.fulltextConvertToImage(pubFulltextImageRefresh.getFulltextFileId(), pageIndex);

        // 保存转换后的图片信息.
        if (StringUtils.isNotBlank(imagePath)) {
          this.savePubFulltextImage(pubId, pageIndex + 1, imagePath);
        }
      } else {
        this.deletePubFulltextByPubId(pubId);
        this.deletePubXmlFulltext(pubId);
      }
    } catch (FileNotFoundException ex) {
      this.deletePubFulltextByPubId(pubId);
      this.delPubFulltextImageRefresh(pubId);
    } catch (Exception e) {
      logger.error("刷新成果pubId={}全文转换图片出现异常：{}", pubFulltextImageRefresh.getPubId(), e);
      throw new ServiceException(e);
    }
  }

  private String fulltextConvertToImage(Long fulltextFileId, int pageIndex)
      throws ServiceException, FileNotFoundException {
    try {
      String imagePath = null;
      ArchiveFile archiveFile = archiveFilesService.getArchiveFile(fulltextFileId);
      if (archiveFile != null) {
        String fileName = archiveFile.getFileName();
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        String filePath = fileService.getFilePath(archiveFile.getFilePath());

        if ("pdf".equalsIgnoreCase(fileType)) {
          imagePath = archiveFilesService.pdfConvertToImage(fulltextFileId, filePath,
              ServiceConstants.DIR_PUB_FULLTEXT_IMAGE, 0.2F, pageIndex);
        } else if ("jpg".equalsIgnoreCase(fileType) || "jpeg".equalsIgnoreCase(fileType)
            || "gif".equalsIgnoreCase(fileType) || "png".equalsIgnoreCase(fileType)) {
          imagePath = archiveFilesService.reduceImage(fulltextFileId, filePath, ServiceConstants.DIR_PUB_FULLTEXT_IMAGE,
              fileType, 0.2F);
        }
      }
      return imagePath;
    } catch (FileNotFoundException ex) {
      throw new FileNotFoundException(ex.getMessage());
    } catch (Exception e) {
      logger.error("转换SCM成果的全文fulltextFileId={}为图片出现异常：{}", fulltextFileId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void savePubFulltext(Long pubId, Long fulltextFileId, Integer fulltextNode, String fulltextFileExt)
      throws ServiceException {
    try {
      PubFulltextRol pubFulltext = pubFulltextRolDao.get(pubId);
      if (pubFulltext == null) {
        pubFulltext = new PubFulltextRol(pubId);
      }
      Long oldFulltextFileId = pubFulltext.getFulltextFileId();
      pubFulltext.setFulltextFileId(fulltextFileId);
      pubFulltext.setFulltextNode(fulltextNode);
      pubFulltext.setFulltextFileExt(fulltextFileExt);
      pubFulltextRolDao.save(pubFulltext);

      // 添加全文图片刷新记录
      if ((oldFulltextFileId == null || (oldFulltextFileId != fulltextFileId))
          && (".pdf".equalsIgnoreCase(fulltextFileExt) || ".jpg".equalsIgnoreCase(fulltextFileExt)
              || ".jpeg".equalsIgnoreCase(fulltextFileExt) || ".gif".equalsIgnoreCase(fulltextFileExt)
              || ".png".equalsIgnoreCase(fulltextFileExt))) {
        this.savePubFulltextImageRefresh(pubId, fulltextFileId, fulltextNode, fulltextFileExt);
      }
    } catch (Exception e) {
      logger.error("保存成果pubId={}全文fulltextFileId={}信息出现异常：{}", pubId, fulltextFileId, e);
      throw new ServiceException(e);
    }
  }

  /**
   * 保存成果全文图片信息.
   * 
   * @throws ServiceException
   */
  private void savePubFulltextImage(Long pubId, Integer fulltextImagePageIndex, String fulltextImagePath)
      throws ServiceException {
    try {
      PubFulltextRol pubFulltext = pubFulltextRolDao.get(pubId);
      if (pubFulltext != null) {
        pubFulltext.setFulltextImagePageIndex(fulltextImagePageIndex);
        pubFulltext.setFulltextImagePath(fulltextImagePath);
        pubFulltextRolDao.save(pubFulltext);
      }
    } catch (Exception e) {
      logger.error("保存成果pubId={}全文图片信息出现异常：{}", pubId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public PubFulltextRol getPubFulltextByPubId(Long pubId) throws ServiceException {
    try {
      return this.pubFulltextRolDao.get(pubId);
    } catch (Exception e) {
      logger.error("获取成果pubId={}全文转换成图片的首张图片出现异常：{}", pubId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public String getFulltextImageUrl(Long pubId) throws ServiceException {
    String fulltextImageUrl = "";
    try {
      PubFulltextRol pubFulltext = this.pubFulltextRolDao.get(pubId);
      if (pubFulltext != null && StringUtils.isNotBlank(pubFulltext.getFulltextImagePath())) {
        fulltextImageUrl = sysDomainConst.getRolDomain() + pubFulltext.getFulltextImagePath();
      }
    } catch (Exception e) {
      logger.error("获取成果pubId={}的全文图片链接出现异常：{}", pubId, e);
    }

    return fulltextImageUrl;
  }

  @Override
  public void deletePubFulltextByPubId(Long pubId) throws ServiceException {
    try {
      PubFulltextRol pubFulltext = this.pubFulltextRolDao.get(pubId);
      if (pubFulltext != null) {
        this.pubFulltextRolDao.delete(pubFulltext);
        if (StringUtils.isNotBlank(pubFulltext.getFulltextImagePath())) {
          // 删除全文后，同时删除全文附件所转换成的图片
          this.archiveFilesService.deleteFileByPath(pubFulltext.getFulltextImagePath());
        }
      }
    } catch (Exception e) {
      logger.error("删除成果pubId={}的全文出现异常：{}", pubId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void dealPubFulltext(Long pubId, Long fulltextFileId, Integer fulltextNode, String fulltextFileExt)
      throws ServiceException {
    try {
      if (fulltextFileId == null || fulltextFileId.longValue() == 0) {
        this.deletePubFulltextByPubId(pubId);
      } else {
        this.savePubFulltext(pubId, fulltextFileId, fulltextNode, fulltextFileExt);
      }
    } catch (Exception e) {
      logger.error("处理成果pubId={}的全文fulltextFileId={}出现异常：{}", pubId, fulltextFileId, e);
      throw new ServiceException(e);
    }
  }

  /**
   * 删除成果全文图片.
   * 
   * @param pubId
   * @throws ServiceException
   */
  private void deletePubFulltextImageByPubId(Long pubId) throws ServiceException {
    try {
      PubFulltextRol pubFulltext = this.pubFulltextRolDao.get(pubId);
      if (pubFulltext != null && StringUtils.isNotBlank(pubFulltext.getFulltextImagePath())) {
        String fulltextImagePath = pubFulltext.getFulltextImagePath();
        pubFulltext.setFulltextImagePath(null);
        this.pubFulltextRolDao.save(pubFulltext);
        this.archiveFilesService.deleteFileByPath(fulltextImagePath);
      }
    } catch (Exception e) {
      logger.error("删除成果pubId={}的全文转换后的图片出现异常：{}", pubId, e);
      throw new ServiceException(e);
    }
  }

  private void deletePubXmlFulltext(Long pubId) throws ServiceException {

    try {
      PublicationXml pubXml = this.publicationXmlService.getById(pubId);
      String xml = pubXml.getXmlData();
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
        this.publicationXmlService.save(pubId, xmlDocument.getXmlString());
      }
    } catch (Exception e) {
      LogUtils.error(logger, e, "删除成果pubId={} xml的全文", pubId);
      throw new ServiceException(e);
    }
  }

  public static void main(String[] as) {
    System.out.println(ServiceUtil.encodeToDes3("21"));
    System.out.println(ServiceUtil.decodeFromDes3("/Ez3ScgHuXs="));
  }

}
