package com.smate.sie.center.task.pdwh.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.center.task.dao.sns.quartz.SnsArchiveFilesDao;
import com.smate.center.task.model.sns.quartz.SnsArchiveFiles;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubFullTextDAO;
import com.smate.center.task.v8pub.pdwh.po.PubPdwhPO;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.string.IrisNumberUtils;
import com.smate.sie.center.task.dao.ImportPdwhPubDao;
import com.smate.sie.center.task.dao.PatFullTextRefreshDao;
import com.smate.sie.center.task.dao.PubFullTextRefreshDao;
import com.smate.sie.center.task.dao.PubPdwhSieRealtionDao;
import com.smate.sie.center.task.dao.SieArchiveFileDao;
import com.smate.sie.center.task.model.ImportPdwhPub;
import com.smate.sie.center.task.model.PatFullTextRefresh;
import com.smate.sie.center.task.model.PubFullTextRefresh;
import com.smate.sie.center.task.model.PubPdwhSieRelation;
import com.smate.sie.center.task.model.SieArchiveFile;
import com.smate.sie.center.task.pdwh.json.service.PatSaveOrUpdateService;
import com.smate.sie.center.task.pdwh.json.service.PubDataService;
import com.smate.sie.center.task.pdwh.json.service.PubSaveOrUpdateService;
import com.smate.sie.core.base.utils.model.pub.SiePatent;
import com.smate.sie.core.base.utils.model.pub.SiePublication;
import com.smate.sie.core.base.utils.pub.service.PubJsonDTO;
import com.smate.sie.core.base.utils.pub.service.PubJsonPOService;

/**
 * 成果XML处理服务(导入、修改、新增).
 * 
 * @author jszhou
 */
@Transactional(rollbackFor = Exception.class)
@Service("publicationXmlManager")
public class PublicationXmlManagerImpl implements PublicationXmlManager {

  public static final String PubNewName = "isPubNew";
  public static final String PubUpdateName = "isPubUpdate";
  public static final String PatNewName = "isPatNew";
  public static final String PatUpdateName = "isPatUpdate";
  /**
   * 
   */
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SiePubDupFieldsService pubDupFieldsService;
  @Autowired
  private SiePatDupFieldsService patDupFieldsService;
  @Autowired
  private PubFullTextRefreshDao pubFullTextRefreshDao;
  @Autowired
  private SieArchiveFileDao sieArchiveFileDao;
  @Autowired
  private SnsArchiveFilesDao snsArchiveFilesDao;
  @Autowired
  private PdwhPubFullTextDAO pdwhPubFullTextDAO;
  @Autowired
  private PatFullTextRefreshDao patFullTextRefreshDao;
  @Autowired
  private PubPdwhSieRealtionDao pubPdwhSieRealtionDao;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Value("${initOpen.restful.url}")
  private String SERVER_URL;
  @Autowired
  private PubDataService pubDataService;
  @Autowired
  private PubSaveOrUpdateService pubSaveOrUpdateService;
  @Autowired
  private PubJsonPOService pubJsonPOService;
  @Autowired
  private PatSaveOrUpdateService patSaveOrUpdateService;
  @Autowired
  private ImportPdwhPubDao importPdwhPubDao;

  /**
   * 针对一条成果，判断专利，并做相应的操作记录成果，专利的新增和更新操作。
   */


  @Override
  public Map<String, Object> backgroundImportPdwhPubJson(PubJsonDTO pubJson, PubPdwhPO pdwhPublications)
      throws SysServiceException {
    boolean isPubNew = false;
    boolean isPubUpdate = false;
    boolean isPatNew = false;
    boolean isPatUpdate = false;
    Map<String, Object> resultMap = new HashMap<String, Object>();
    try {
      ImportPdwhPub importPdwhPub = importPdwhPubDao.get(pubJson.insId);
      int pubType = pdwhPublications.getPubType();
      pubDataService.pubHandleByType(pubJson);// 不同类型调用不同process完善pubtypebean以及构造来源
      // 查重: 重复并且不是基准库同步回来的数据，则返回pubid，否则更新数据。
      Long dupPubId = 0L;
      // 成果
      if (pubType != 5) {
        SiePublication publication = getSingleDuplicatedPub(pubJson);
        if (publication != null) {
          dupPubId = publication.getPubId();
          // 匹配关系表，如果不存在关系则认为该条成果是通过其他途径录入sie库的，故不做updat操作
          PubPdwhSieRelation pdwhSieRelation = pubPdwhSieRealtionDao.isExitPublicationBySiePubId(dupPubId);
          if (pdwhSieRelation == null
              || pubNotUpdate(pdwhSieRelation.getSiePubId(), pdwhPublications.getGmtModified())) {
            resultMap.put("pubId", dupPubId);
            resultMap.put("pubType", pubType);
            return resultMap;
          } else {// 已存在则更新
            pubJson.pubId = dupPubId;
            pubJson.isEdit = false;
            publication = pubSaveOrUpdateService.updatePublication(pubJson, pdwhPublications);
            // 计算统计数
            if ((!importPdwhPub.getUpdateTime().equals(pdwhPublications.getGmtModified()))
                && publication.getPdwhImportStatus()) {
              isPubUpdate = true;
            }
          }
        } else {// 不存在则新增
          publication = pubSaveOrUpdateService.createPublication(pubJson, pdwhPublications);
          dupPubId = publication.getPubId();
          isPubNew = true;
        }
      } else {// 专利
        SiePatent patent = getSingleDuplicatedPat(pubJson);
        if (patent != null) {
          dupPubId = patent.getPatId();
          PubPdwhSieRelation pdwhSieRelation = pubPdwhSieRealtionDao.isExitPublicationBySiePubId(dupPubId);
          if (pdwhSieRelation == null) {
            resultMap.put("pubId", dupPubId);
            resultMap.put("pubType", pubType);
            return resultMap;
          } else {// 已存则即更新
            pubJson.pubId = dupPubId;
            pubJson.isEdit = false;
            patent = patSaveOrUpdateService.updatePatent(pubJson, pdwhPublications);
            if ((!importPdwhPub.getUpdateTime().equals(pdwhPublications.getGmtModified()))
                && patent.getPdwhImportStatus()) {
              isPatUpdate = true;
            }
          }
        } else {// 不存在则新增
          patent = patSaveOrUpdateService.createPatent(pubJson);
          dupPubId = patent.getPatId();
          isPatNew = true;
        }
      }

      // 保存关系表
      String pdwhFileId = this.savePubPdwhSieRelation(dupPubId, pdwhPublications);
      // 保存成果全文转换成图片刷新表
      if (pdwhFileId != null && !"".equals(pdwhFileId)) {
        this.saveArchiveFiles(IrisNumberUtils.createLong(pdwhFileId));
        if (pdwhPublications.getPubType() == 5) {
          this.savePatFullTextRefresh(dupPubId); // 专利
        } else {
          this.savePubFullTextRefresh(dupPubId); // 成果
        }
      }
      resultMap.put(PubNewName, isPubNew);
      resultMap.put(PubUpdateName, isPubUpdate);
      resultMap.put(PatNewName, isPatNew);
      resultMap.put(PatUpdateName, isPatUpdate);
      return resultMap;
    } catch (DocumentException e) {
      logger.error("backgroundImportPdwhPubJson导入Json,转换错误， pubId=" + pubJson.pubId, e);
      throw new SysServiceException("backgroundImportPdwhPubJson导入Json,转换错误,  pubId=" + pubJson.pubId);
    } catch (Exception e) {
      logger.error("backgroundImportPdwhPubJson导入Json错误,pubId=" + pubJson.pubId, e);
      String errMsg = getErrorMessage(e);
      throw new SysServiceException("基准库pubId=" + pdwhPublications.getPubId() + ", " + e.toString() + " --> " + errMsg);
    }
  }

  private String getErrorMessage(Exception e) {
    StackTraceElement stackTraceElement = e.getStackTrace()[0];
    int lineNum = stackTraceElement.getLineNumber();
    String errMsg = "异常类文件名： " + stackTraceElement.getFileName() + "异常方法名 ： " + stackTraceElement.getMethodName()
        + "，错误行号 ： " + lineNum;
    return errMsg;
  }

  /**
   * 获取该成果是否已经更新了，不更新则返回true
   */
  private boolean pubNotUpdate(Long siePubId, Date gmtModified) {

    return false;
  }

  @Override
  public boolean getRepeatPubStatus(Long pubid, int pubType, Long InsId) throws DocumentException {
    boolean flag = false;
    switch (pubType) {
      case 2:
      case 3:
      case 4:
        // 成果
        PubJsonDTO pubJson = pubJsonPOService.getPubJsonDTOByIdAndType(pubid, pubType);
        if (pubJson != null) {
          // 设置合并单位中成果查重（专利）的单位为主单位id(根据合并单位的成果（专利）内容计算的HASH值与主单位存在的 成果记录的HASH值进行比对)
          pubJson.insId = InsId;
          flag = this.pubDupFieldsService.getDupPubStatus(pubJson);
        }
        break;
      case 51:
      case 52:
      case 53:
      case 54:
        // 专利
        // SiePatXml siePatXml = siePatXmlDao.get(pubid);
        /*
         * SiePatXml siePatXml = null; if (siePatXml != null) { String importXml = siePatXml.getPatXml(); if
         * (importXml != null && !"".equals(importXml)) { Document doc =
         * DocumentHelper.parseText(importXml); Node pitem = doc.selectSingleNode("/data"); Element data =
         * (Element) pitem.selectSingleNode("publication"); if (data != null) { data.addAttribute("ctitle",
         * data.attributeValue("zh_title")); data.addAttribute("etitle", data.attributeValue("en_title"));
         * flag = this.patDupFieldsService.getDupPatStatus(data, InsId); } } }
         */
        break;
    }
    return flag;
  }

  /**
   * 查重
   * 
   * @param pubJson
   * @return
   * @throws SysServiceException
   */
  public SiePublication getSingleDuplicatedPub(PubJsonDTO pubJson) throws SysServiceException {
    Map<Integer, List<Long>> dupMap = this.pubDupFieldsService.getDupPubByImportPub(pubJson);
    if (MapUtils.isEmpty(dupMap)) {
      return null;
    }
    // 严格匹配
    if (dupMap.get(1) != null) {
      SiePublication publication = new SiePublication();
      publication.setPubId(dupMap.get(1).get(0));
      // 不允许插入
      publication.setIsJnlInsert(0);
      return publication;
    } else if (dupMap.get(2) != null) {
      SiePublication publication = new SiePublication();
      publication.setPubId(dupMap.get(2).get(0));
      // 允许插入
      publication.setIsJnlInsert(1);
      return publication;
    }
    return null;
  }

  /**
   * 返回xml节点重复的专利编号.
   * 
   * @param item
   * @param pubType
   * @return
   * @throws SysServiceException
   */
  public SiePatent getSingleDuplicatedPat(PubJsonDTO pubJson) throws SysServiceException {
    Map<Integer, List<Long>> dupMap = this.patDupFieldsService.getDupPatByImportPat(pubJson);
    if (MapUtils.isEmpty(dupMap)) {
      return null;
    }
    // 严格匹配
    if (dupMap.get(1) != null) {
      SiePatent patent = new SiePatent();
      patent.setPatId(dupMap.get(1).get(0));
      // 不允许插入
      patent.setIsJnlInsert(0);
      return patent;
    } else if (dupMap.get(2) != null) {
      SiePatent patent = new SiePatent();
      patent.setPatId(dupMap.get(2).get(0));
      // 允许插入
      patent.setIsJnlInsert(1);
      return patent;
    }
    return null;
  }



  public String savePubPdwhSieRelation(Long pubId, PubPdwhPO pdwhPublications) throws Exception {
    PubPdwhSieRelation pdwhSieRelation = pubPdwhSieRealtionDao.isExitPublicationBySiePubId(pubId);
    Long pdwhFileId = pdwhPubFullTextDAO.findMaxFileIdByPubId(pdwhPublications.getPubId());
    String pdwhFileIdStr = "";
    if (pdwhFileId != null) {
      pdwhFileIdStr = pdwhFileId.toString();
    }
    if (null == pdwhSieRelation) {
      pdwhSieRelation = new PubPdwhSieRelation();
      pdwhSieRelation.setSiePubId(pubId);
      pdwhSieRelation.setPdwhPubId(pdwhPublications.getPubId());
      pdwhSieRelation.setPdwhFileId(pdwhFileIdStr);
    } else {
      // 先删除，再插入的方式更新
      sieArchiveFileDao.deleteArchiveFileByFileId(IrisNumberUtils.createLong(pdwhSieRelation.getPdwhFileId()));
      pdwhSieRelation.setPdwhPubId(pdwhPublications.getPubId());
      pdwhSieRelation.setPdwhFileId(pdwhFileIdStr);
    }
    try {
      pubPdwhSieRealtionDao.save(pdwhSieRelation);
    } catch (Exception e) {
      logger.error("保存SIE与基准库成果关系表报错, 基准库pubId=" + pdwhPublications.getPubId() + ", SIE库pubId=" + pubId, e);
      String errMsg = getErrorMessage(e);
      throw new SysServiceException(
          "保存SIE与基准库成果关系表报错, 基准库pubId=" + pdwhPublications.getPubId() + ", " + e.toString() + " --> " + errMsg);
    }
    return pdwhFileIdStr;
  }

  public void savePubFullTextRefresh(Long pubId) throws SysServiceException {
    try {
      PubFullTextRefresh refresh = pubFullTextRefreshDao.get(pubId);
      if (refresh == null) {
        refresh = new PubFullTextRefresh(pubId, 0);
      } else {
        refresh.setStatus(0);
      }
      pubFullTextRefreshDao.save(refresh);
    } catch (Exception e) {
      logger.error("保存成果全文转换成图片刷新表报错", e);
      String errMsg = getErrorMessage(e);
      throw new SysServiceException("保存成果全文转换成图片刷新表报错, pubId=" + pubId + ", " + e.toString() + " --> " + errMsg);
    }
  }

  public void savePatFullTextRefresh(Long patId) throws SysServiceException {
    try {
      PatFullTextRefresh refresh = patFullTextRefreshDao.get(patId);
      if (refresh == null) {
        refresh = new PatFullTextRefresh(patId, 0);
      } else {
        refresh.setStatus(0);
      }
      patFullTextRefreshDao.save(refresh);
    } catch (Exception e) {
      logger.error("保存成果全文转换成图片刷新表报错, patId=" + patId, e);
      String errMsg = getErrorMessage(e);
      throw new SysServiceException("保存成果全文转换成图片刷新表报错, patId=" + patId + ", " + e.toString() + " --> " + errMsg);
    }
  }

  /**
   * 保存全文信息 ARCHIVE_FILES
   */
  public void saveArchiveFiles(Long fileId) throws SysServiceException {
    try {
      SnsArchiveFiles snsFile = snsArchiveFilesDao.get(fileId);
      if (snsFile == null) {
        return;
      }
      SieArchiveFile sieFile = sieArchiveFileDao.findArchiveFileByFileId(fileId);
      if (sieFile != null) {
        return;
      } else {
        sieFile = new SieArchiveFile();
      }
      sieFile.setFileId(fileId);
      sieFile.setFileType(snsFile.getFileType());
      sieFile.setCreatePsnId(snsFile.getCreatePsnId());
      sieFile.setCreateTime(snsFile.getCreateTime());
      sieFile.setFileDesc(snsFile.getFileDesc());
      sieFile.setFileName(snsFile.getFileName());
      sieFile.setFilePath(snsFile.getFilePath());
      sieFile.setNodeId(snsFile.getNodeId());
      sieFile.setInsId(snsFile.getInsId());
      sieFile.setFileUUID(snsFile.getFileUUID());
      sieFile.setFileUrl(snsFile.getFileUrl());
      sieFile.setFileSize(snsFile.getFileSize());
      sieFile.setStatus(snsFile.getStatus());
      sieArchiveFileDao.save(sieFile);
      sieArchiveFileDao.getSession().flush();
    } catch (Exception e) {
      logger.error("保存成果全文信息报错, fileId=" + fileId, e);
      String errMsg = getErrorMessage(e);
      throw new SysServiceException("保存成果全文信息报错, fileId=" + fileId + ", " + e.toString() + " --> " + errMsg);
    }
  }


}
