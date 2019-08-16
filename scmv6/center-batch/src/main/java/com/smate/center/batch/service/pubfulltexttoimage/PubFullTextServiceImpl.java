package com.smate.center.batch.service.pubfulltexttoimage;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.constant.InsideMessageConstants;
import com.smate.center.batch.constant.MessageConstant;
import com.smate.center.batch.dao.mail.FullTextInboxDao;
import com.smate.center.batch.dao.pdwh.pub.PdwhFullTextImageDao;
import com.smate.center.batch.dao.pdwh.pub.PdwhPubFulltextImageRefreshDao;
import com.smate.center.batch.dao.sns.pub.PubDataStoreDao;
import com.smate.center.batch.dao.sns.pub.PubFulltextDao;
import com.smate.center.batch.dao.sns.pub.PubFulltextImageRefreshDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.mail.FullTextInBox;
import com.smate.center.batch.model.pdwh.pub.PdwhFullTextImage;
import com.smate.center.batch.model.pdwh.pub.PdwhPubFulltextImageRefresh;
import com.smate.center.batch.model.sns.pub.Message;
import com.smate.center.batch.model.sns.pub.PubDataStore;
import com.smate.center.batch.model.sns.pub.PubFulltext;
import com.smate.center.batch.model.sns.pub.PubFulltextImageRefresh;
import com.smate.center.batch.model.sns.pub.PublicationXml;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.WeChatMsgPsnService;
import com.smate.center.batch.service.mail.InsideMessageService;
import com.smate.center.batch.service.pub.PubFulltextPsnRcmdService;
import com.smate.center.batch.service.pub.PublicationService;
import com.smate.center.batch.service.pub.PublicationXmlService;
import com.smate.center.batch.service.pub.archiveFiles.ArchiveFilesService;
import com.smate.center.batch.service.pub.mq.PubSyncToPubFtSrvProducer;
import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.file.ArchiveFileUtil;
import com.smate.core.base.utils.file.FileService;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.SecurityUtils;

@Service("pubFullTextService")
@Transactional(rollbackFor = Exception.class)
public class PubFullTextServiceImpl implements PubFullTextService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Value("${domainscm}")
  private String domainscm;

  @Autowired
  private PubFulltextImageRefreshDao pubFulltextImageRefreshDao;
  @Autowired
  private PdwhPubFulltextImageRefreshDao pdwhPubFulltextImageRefreshDao;

  @Autowired
  private PubFulltextDao pubFulltextDao;

  @Autowired
  private PdwhFullTextImageDao pdwhFullTextImagedao;

  @Autowired
  private PubDataStoreDao pubDataStoreDao;

  @Autowired
  private FullTextInboxDao fullTextInBoxDao;

  @Autowired
  private ArchiveFilesService archiveFilesService;

  @Autowired
  private FileService fileService;

  @Autowired
  private PublicationXmlService publicationXmlService;

  @Autowired
  private WeChatMsgPsnService weChatMsgPsnService;

  @Autowired
  private PubSyncToPubFtSrvProducer pubSyncToPubFtSrvProducer;

  @Autowired
  private PubFulltextPsnRcmdService pubFulltextPsnRcmdService;

  @Autowired
  private CacheService cacheService;

  @Autowired
  private PublicationService publicationService;

  @Autowired
  private InsideMessageService insideMessageService;

  @Override
  public void savePubFulltextImageRefresh(Long pubId, Long fulltextFileId, Integer fulltextNode, String fulltextFileExt)
      throws BatchTaskException {
    try {
      PubFulltextImageRefresh refresh =
          new PubFulltextImageRefresh(pubId, fulltextFileId, fulltextNode, fulltextFileExt);
      this.pubFulltextImageRefreshDao.save(refresh);
    } catch (Exception e) {
      logger.error("保存成果pubId={}全文转换成图片的刷新记录出现异常：{}", pubId, e);
      throw new BatchTaskException(e);
    }
  }

  @Override
  public void savePubFulltextImageRefresh(PubFulltextImageRefresh pubFulltextImageRefresh) throws BatchTaskException {
    try {
      pubFulltextImageRefreshDao.save(pubFulltextImageRefresh);
    } catch (Exception e) {
      logger.error("保存成果全文转换图片刷新记录出现异常：", e);
      throw new BatchTaskException(e);
    }

  }

  @Override
  public void delPubFulltextImageRefresh(Long id) throws BatchTaskException {
    try {
      this.pubFulltextImageRefreshDao.delete(id);
    } catch (Exception e) {
      logger.error("删除成果全文转换图片刷新记录出现异常：", e);
      throw new BatchTaskException(e);
    }

  }

  @Override
  public List<PubFulltextImageRefresh> getNeedRefreshData(int maxSize) throws BatchTaskException {
    try {
      return this.pubFulltextImageRefreshDao.queryRefreshDataBatch(maxSize);
    } catch (Exception e) {
      logger.error("批量查询需要转换成图片的成果全文刷新数据出现异常：", e);
      throw new BatchTaskException(e);
    }
  }

  /**
   * pdwh的处理已经迁移到center-task
   */
  @Override
  public void convertPdftoImg(PdwhPubFulltextImageRefresh pdwhImage)
      throws InterruptedException, Exception, FileNotFoundException {
    Long pubId = pdwhImage.getPubId();
    try {
      if (pdwhImage.getFulltextFileId() != null) {
        Integer fulltextNode = pdwhImage.getFulltextNode();
        if (fulltextNode == null) {
          throw new Exception("pdwh全文图片转换，Refresh表 nodeId为空");
        }
        // 首先删除成果全文图片，以免造成垃圾的附件.（同一个事物中这里删除后面保存会有问题，已迁移任务暂未修改）
        this.delPdwhPubFulltextByPubId(pubId);

        // 转换图片
        int pageIndex = 0;
        String imagePath = this.fulltextConvertToImage(pdwhImage.getFulltextFileId(), pageIndex);

        // 保存转换后的图片信息.
        if (StringUtils.isNotBlank(imagePath)) {
          this.savePdwhPubFulltextImage(pubId, pageIndex + 1, imagePath, pdwhImage.getFulltextFileId(),
              pdwhImage.getFulltextFileExt());
        }
      } else {
        this.delPdwhPubFulltextByPubId(pubId);
      }
    } catch (FileNotFoundException ex) {
      throw new FileNotFoundException(ex.getMessage());
    } catch (Exception e) {
      logger.error("pdwh成果pubId={}全文转换图片出现异常：{}", pdwhImage.getPubId(), e);
      throw new Exception(e);
    }

  }

  @Override
  @Deprecated
  public void refreshData(PubFulltextImageRefresh pubFulltextImageRefresh)
      throws InterruptedException, Exception, FileNotFoundException {
    Long pubId = pubFulltextImageRefresh.getPubId();
    try {
      if (pubFulltextImageRefresh.getFulltextFileId() != null) {
        Integer fulltextNode = pubFulltextImageRefresh.getFulltextNode();
        if (fulltextNode == null) {
          fulltextNode = SecurityUtils.getCurrentAllNodeId().get(0);
        }
        // 首先删除成果全文图片，以免造成垃圾的附件.
        this.deleteSnsPubFulltextImageByPubId(pubId);

        // 转换图片
        int pageIndex = 0;
        String imagePath = this.fulltextConvertToImage(pubFulltextImageRefresh.getFulltextFileId(), pageIndex);

        // 保存转换后的图片信息.
        if (StringUtils.isNotBlank(imagePath)) {
          this.saveSnsPubFulltextImage(pubId, pageIndex + 1, imagePath);
        }
      } else {
        this.deletePubFulltextByPubId(pubId);
        this.deletePubXmlFulltext(pubId);
      }
    } /*
       * catch(InterruptedException ie){ throw new InterruptedException(ie.getMessage()); }
       */
    catch (FileNotFoundException ex) {
      throw new FileNotFoundException(ex.getMessage());
    } catch (Exception e) {
      logger.error("刷新成果pubId={}全文转换图片出现异常：{}", pubFulltextImageRefresh.getPubId(), e);
      throw new Exception(e);
    }

  }

  @Override
  public void updatePubFulltext(PubFulltext ptext) throws ServiceException {
    pubFulltextDao.save(ptext);
  }

  @Override
  public void savePubFulltext(Long pubId, Long fulltextFileId, Integer fulltextNode, String fulltextFileExt)
      throws BatchTaskException {
    // TODO Auto-generated method stub

  }

  @Override
  public PubFulltext getPubFulltextByPubId(Long pubId) throws BatchTaskException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getFulltextImageUrl(Long pubId) throws ServiceException {
    String fulltextImageUrl = "";
    try {
      PubFulltext pubFulltext = this.pubFulltextDao.get(pubId);
      if (pubFulltext != null && StringUtils.isNotBlank(pubFulltext.getFulltextImagePath())) {
        // FIXME 2015-10-29 取消远程调用 -done
        fulltextImageUrl = domainscm + "/" + pubFulltext.getFulltextImagePath();
      }
    } catch (Exception e) {
      logger.error("获取成果pubId={}的全文图片链接出现异常：{}", pubId, e);
    }

    return fulltextImageUrl;
  }

  @Override
  public void deletePubFulltextByPubId(Long pubId) throws BatchTaskException {
    try {
      PubFulltext pubFulltext = this.pubFulltextDao.get(pubId);
      if (pubFulltext != null && StringUtils.isNotBlank(pubFulltext.getFulltextImagePath())) {
        String fulltextImagePath = pubFulltext.getFulltextImagePath();
        pubFulltext.setFulltextImagePath(null);
        this.pubFulltextDao.save(pubFulltext);
        this.archiveFilesService.deleteFileByPath(fulltextImagePath);
      }
    } catch (Exception e) {
      logger.error("删除成果pubId={}的全文转换后的图片出现异常：{}", pubId, e);
      throw new BatchTaskException(e);
    }

  }

  /**
   * 删除pdwh fulltext image
   * 
   * @param pubId
   * @throws BatchTaskException
   */
  public void delPdwhPubFulltextByPubId(Long pubId) throws BatchTaskException {
    try {
      PdwhFullTextImage pubFulltext = this.pdwhFullTextImagedao.get(pubId);
      if (pubFulltext != null) {
        String fulltextImagePath = pubFulltext.getImagePath();
        this.pdwhFullTextImagedao.delete(pubId);
        if (StringUtils.isNotBlank(pubFulltext.getImagePath())) {
          this.archiveFilesService.deleteFileByPath(fulltextImagePath);
        }
      }
    } catch (Exception e) {
      logger.error("删除pdwh成果pubId={}的全文转换后的图片出现异常：{}", pubId, e);
      throw new BatchTaskException(e);
    }

  }

  @Override
  public void dealPubFulltext(Long pubId, Long psnId, Long fulltextFileId, Integer fulltextNode, String fulltextFileExt,
      int permission, Long groupId) throws ServiceException {
    try {
      if (fulltextFileId == null || fulltextFileId.longValue() == 0) {
        this.deletePubFulltextByPubId(pubId, psnId);
      } else {
        this.savePubFulltext(pubId, psnId, fulltextFileId, fulltextNode, fulltextFileExt, permission, groupId);
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
   * @throws BatchTaskException
   */
  @Override
  public void deleteSnsPubFulltextImageByPubId(Long pubId) throws BatchTaskException {
    try {
      PubFulltext pubFulltext = this.pubFulltextDao.get(pubId);
      if (pubFulltext != null && StringUtils.isNotBlank(pubFulltext.getFulltextImagePath())) {
        String fulltextImagePath = pubFulltext.getFulltextImagePath();
        pubFulltext.setFulltextImagePath(null);
        this.pubFulltextDao.save(pubFulltext);
        this.archiveFilesService.deleteFileByPath(fulltextImagePath);
      }
    } catch (Exception e) {
      logger.error("删除成果pubId={}的全文转换后的图片出现异常：{}", pubId, e);
      throw new BatchTaskException(e);
    }
  }

  /**
   * 删除全文信息，同时也会删除全文附件所转换的图片.
   * 
   * @param pubId
   * @param psnId
   * @throws ServiceException
   */
  private void deletePubFulltextByPubId(Long pubId, Long psnId) throws ServiceException {
    try {
      PubFulltext pubFulltext = this.pubFulltextDao.get(pubId);
      if (pubFulltext != null) {
        this.pubFulltextDao.delete(pubFulltext);
        if (StringUtils.isNotBlank(pubFulltext.getFulltextImagePath())) {
          // 删除全文后，同时删除全文附件所转换成的图片
          this.archiveFilesService.deleteFileByPath(pubFulltext.getFulltextImagePath());
        }
        // FIXME 2015-10-29 取消MQ -done
        this.pubSyncToPubFtSrvProducer.sendUpdateFulltextMessage(pubId, psnId, pubFulltext.getFulltextFileId(),
            pubFulltext.getPermission());
      }
    } catch (Exception e) {
      logger.error("删除成果pubId={}的全文出现异常：{}", pubId, e);
      throw new ServiceException(e);
    }
  }

  /**
   * 保存成果全文信息.
   * 
   * @param pubId
   * @param psnId
   * @param fulltextFileId
   * @param fulltextNode
   * @param fulltextFileExt
   * @param permission
   * @throws ServiceException
   */
  private void savePubFulltext(Long pubId, Long psnId, Long fulltextFileId, Integer fulltextNode,
      String fulltextFileExt, int permission, Long groupId) throws ServiceException {
    try {
      PubFulltext pubFulltext = pubFulltextDao.get(pubId);
      if (pubFulltext == null) {
        pubFulltext = new PubFulltext(pubId);
      }
      Long oldFulltextFileId = pubFulltext.getFulltextFileId();
      pubFulltext.setFulltextFileId(fulltextFileId);
      pubFulltext.setFulltextNode(fulltextNode);
      pubFulltext.setFulltextFileExt(fulltextFileExt);
      pubFulltext.setPermission(permission);
      pubFulltext.setGroupId(groupId);
      pubFulltextDao.save(pubFulltext);

      // 添加全文图片刷新记录
      if ((oldFulltextFileId == null || (oldFulltextFileId != fulltextFileId))
          && (".pdf".equalsIgnoreCase(fulltextFileExt) || ".jpg".equalsIgnoreCase(fulltextFileExt)
              || ".jpeg".equalsIgnoreCase(fulltextFileExt) || ".gif".equalsIgnoreCase(fulltextFileExt)
              || ".png".equalsIgnoreCase(fulltextFileExt))) {
        this.savePubFulltextImageRefresh(pubId, fulltextFileId, fulltextNode, fulltextFileExt);
      }
      // 成果上传全文后，对这条成果的全文推荐都自动确认为不是这条成果的全文.
      this.pubFulltextPsnRcmdService.updateStatusByPubId(pubId, 2);

      // 同步全文冗余数据
      // FIXME 2015-10-29 取消MQ -done
      this.pubSyncToPubFtSrvProducer.sendUpdateFulltextMessage(pubId, psnId, fulltextFileId, permission);
    } catch (Exception e) {
      logger.error("保存成果pubId={}全文fulltextFileId={}信息出现异常：{}", pubId, fulltextFileId, e);
      throw new ServiceException(e);
    }
  }

  private String fulltextConvertToImage(Long fulltextFileId, int pageIndex) throws Exception, FileNotFoundException {
    try {
      String imagePath = null;
      ArchiveFile archiveFile = archiveFilesService.getArchiveFile(fulltextFileId);
      if (archiveFile != null) {
        String fileName = archiveFile.getFileName();
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        String filePath = ArchiveFileUtil.getFilePath(archiveFile.getFilePath());

        if ("pdf".equalsIgnoreCase(fileType)) {
          imagePath = archiveFilesService.pdfConvertToImage(fulltextFileId, filePath,
              ServiceConstants.DIR_PUB_FULLTEXT_IMAGE, 0.3F, pageIndex);
        } /*
           * else if ("jpg".equalsIgnoreCase(fileType) || "jpeg".equalsIgnoreCase(fileType) ||
           * "gif".equalsIgnoreCase(fileType) || "png".equalsIgnoreCase(fileType)) { imagePath =
           * archiveFilesService.reduceImage(fulltextFileId, filePath,
           * ServiceConstants.DIR_PUB_FULLTEXT_IMAGE, fileType, 0.2F); }
           */
      }
      return imagePath;
    } catch (FileNotFoundException ex) {
      throw new FileNotFoundException(ex.getMessage());
    } catch (Exception e) {
      logger.error("转换SCM成果的全文fulltextFileId={}为图片出现异常：{}", fulltextFileId, e);
      throw new Exception(e);
    }
  }

  /**
   * 保存sns成果全文图片信息.
   * 
   * @throws BatchTaskException
   */
  public void saveSnsPubFulltextImage(Long pubId, Integer fulltextImagePageIndex, String fulltextImagePath)
      throws BatchTaskException {
    try {
      PubFulltext pubFulltext = pubFulltextDao.get(pubId);
      if (pubFulltext != null) {
        pubFulltext.setFulltextImagePageIndex(fulltextImagePageIndex);
        pubFulltext.setFulltextImagePath(fulltextImagePath);
        pubFulltextDao.save(pubFulltext);
      }
    } catch (Exception e) {
      logger.error("保存成果pubId={}全文图片信息出现异常：{}", pubId, e);
      throw new BatchTaskException(e);
    }
  }

  /**
   * 保存pdwh成果全文图片信息.
   * 
   * @throws BatchTaskException
   */
  private void savePdwhPubFulltextImage(Long pubId, Integer fulltextImagePageIndex, String fulltextImagePath,
      Long fileId, String fileExtend) throws BatchTaskException {
    PdwhFullTextImage pubFulltext;
    try {
      pubFulltext = pdwhFullTextImagedao.get(pubId);
      if (pubFulltext == null) {
        pubFulltext = new PdwhFullTextImage();
        pubFulltext.setPubId(pubId);
        pubFulltext.setFileId(fileId);
        pubFulltext.setImagePageIndex(fulltextImagePageIndex);
        pubFulltext.setImagePath(fulltextImagePath);
        pubFulltext.setFileExtend(fileExtend);
        pdwhFullTextImagedao.save(pubFulltext);
      } else {
        pubFulltext.setImagePageIndex(fulltextImagePageIndex);
        pubFulltext.setImagePath(fulltextImagePath);
        pubFulltext.setFileExtend(fileExtend);
        pdwhFullTextImagedao.save(pubFulltext);
      }
    } catch (Exception e) {
      logger.error("保存pdwh成果pubId={}全文图片信息出现异常：{}", pubId, e);
      throw new BatchTaskException(e);
    }
  }

  private void deletePubXmlFulltext(Long pubId) throws BatchTaskException {

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
      logger.error("删除成果pubId={} xml的全文", pubId, e);
      throw new BatchTaskException(e);
    }
  }

  @Override
  public PubFulltextImageRefresh getNeedRefreshDataById(Long id) throws BatchTaskException {

    PubFulltextImageRefresh one = pubFulltextImageRefreshDao.getPubFulltextImageRefreshById(id);

    return one;
  }

  @Override
  public void sendFtRequestReplyMail(Long pubId) {

    PubDataStore pubDataStore = pubDataStoreDao.get(pubId);

    if (pubDataStore == null || StringUtils.isEmpty(pubDataStore.getData()))
      return;

    try {

      PubXmlDocument xmlDocument = new PubXmlDocument(pubDataStore.getData());
      Element fullText = (Element) xmlDocument.getNode(PubXmlConstants.PUB_FULLTEXT_XPATH);
      if (fullText == null) {
        return;
      }
      String inboxId = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "inbox_id");
      if (StringUtils.isNotEmpty(inboxId) && !"0".equals(inboxId)) {
        Long id = Long.parseLong(inboxId);
        // 到这一步，说明在消息中心的全文请求是同意，即opStatus为1。 0为待处理，2为拒绝
        this.sendFtRequestReplyMail(id, 1);

      }

    } catch (Throwable e) {
      logger.error("发送全文请求回复邮件错误，pubId={}", pubId, e);
    }

  }

  @Override
  public boolean isOtherTypeFt(Long pubId) {
    PubDataStore pubDataStore = pubDataStoreDao.get(pubId);

    if (pubDataStore == null || StringUtils.isEmpty(pubDataStore.getData()))
      return false;
    try {
      PubXmlDocument xmlDocument = new PubXmlDocument(pubDataStore.getData());
      Element fullText = (Element) xmlDocument.getNode(PubXmlConstants.PUB_FULLTEXT_XPATH);
      String inboxId = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "inbox_id");
      String fileId = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "file_id");
      if (fullText == null || StringUtils.isEmpty(inboxId) || StringUtils.isEmpty(fileId)) {
        return false;
      }

      return true;

    } catch (Throwable e) {
      logger.error("发送全文请求回复邮件错误，pubId={}", pubId, e);
      return false;
    }
  }

  public void sendFtRequestReplyMail(Long inboxId, Integer opStatus) {

    try {
      FullTextInBox inbox = this.fullTextInBoxDao.get(inboxId);
      if (opStatus == 1) {// 同意
        this.sendFullTextInsideMsg(inbox.getParamJson(), inbox.getReceiverName(), inbox.getReceiverEnName(),
            inbox.getMailTitle(), inbox.getMailEnTitle(), inbox.getReceiverId());
      }
      inbox.setOpStatus(opStatus);
      this.fullTextInBoxDao.save(inbox);
      cacheService.remove(MessageConstant.MSG_TIP_CACHE_NAME,
          MessageConstant.MSG_TIP_CACHE_KEY + SecurityUtils.getCurrentUserId());
    } catch (Throwable e) {
      logger.error("error1=========================" + e);
    }

  }

  /**
   * 发送全文请求回复.
   * 
   * @param paramJson
   * @param mailTitle
   * @param mailEnTitle
   * @throws ServiceException
   */
  private void sendFullTextInsideMsg(String paramJson, String senderName, String senderEnName, String mailTitle,
      String mailEnTitle, Long senderId) throws ServiceException {

    Map<String, String> paramMap = JacksonUtils.jsonMapUnSerializer(paramJson);
    Map<String, String> fullTextMap = null;
    int resType = Integer.parseInt(paramMap.get("resType"));
    Long resId = Long.parseLong(paramMap.get("resId"));
    if (resType == 1 || resType == 2) {
      // 对于还没有跑完任务的全文，应该从PubDataStore中获取全文信息
      fullTextMap = this.getPubFullTextInfo(resId);
    }
    // 此处只考虑成果
    /*
     * else { fullTextMap = snsProjectService.getPrjFullTextInfo(resId); }
     */
    String fullTextId = fullTextMap.get("fullTextId");
    if (StringUtils.isNotBlank(fullTextId)) {
      Message message = new Message();
      message.setReceivers(paramMap.get("senderId"));
      message.setInsideType(12);
      message.setMsgType(InsideMessageConstants.MSG_TYPE_REPLAY_FULLTEXT_REQUEST);
      message.setTitle(mailTitle);
      message.setEnTitle(mailEnTitle);
      message.setJsonParam(paramJson);
      message.setPsnName(senderName);
      message.setPsnId(senderId);
      // 得到用户当时的语言环境
      message.setLocale(paramMap.get("locale"));
      // 因有现成英文名，就将就用psnLastName保存英文名
      message.setPsnLastName(senderEnName);
      insideMessageService.sendMessageAndMail(message);
    }
  }

  public Map<String, String> getPubFullTextInfo(Long pubId) {
    PubDataStore pubDataStore = pubDataStoreDao.get(pubId);
    Map<String, String> rtnMap = new HashMap<String, String>();
    try {
      PubXmlDocument xmlDocument = new PubXmlDocument(pubDataStore.getData());
      rtnMap.put("fullTextId", xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "file_id"));
      rtnMap.put("fullTextName", xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "file_name"));
      rtnMap.put("locale", xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_EXPAND_XPATH, "local"));
      return rtnMap;
    } catch (DocumentException e) {
      throw new ServiceException(e);
    }
  }

  /**
   * 获取基准库全文图片待处理数据
   * 
   * @param pubId
   * @return
   */
  @Override
  public PdwhPubFulltextImageRefresh getNeedRefreshPdwhDataById(Long pubId) {
    PdwhPubFulltextImageRefresh one = pdwhPubFulltextImageRefreshDao.getPdwhPubFulltextImageRefreshById(pubId);
    return one;
  }

  @Override
  public void savePdwhPubFulltextImageRefresh(PdwhPubFulltextImageRefresh pdwhImage) throws BatchTaskException {
    try {
      pdwhPubFulltextImageRefreshDao.save(pdwhImage);
    } catch (Exception e) {
      logger.error("保存pdwh成果全文转换图片刷新记录出现异常：", e);
      throw new BatchTaskException(e);
    }
  }

  @Override
  public void delPdwhPubFulltextImageRefresh(Long pubId) throws BatchTaskException {
    try {
      this.pdwhPubFulltextImageRefreshDao.delete(pubId);
    } catch (Exception e) {
      logger.error("删除pdwh成果全文转换图片刷新记录出现异常：", e);
      throw new BatchTaskException(e);
    }
  }

  /*
   * （非 Javadoc）
   * 
   * @see
   * com.smate.center.batch.service.pubfulltexttoimage.PubFullTextService#updateSnsPubFulltextImage(
   * java.lang.Long, java.lang.String, int)
   */
  @Override
  public void updateSnsPubFulltextImage(Long pubId, String destRelativePath, int index) throws BatchTaskException {
    saveSnsPubFulltextImage(pubId, index, destRelativePath);
  }

}
