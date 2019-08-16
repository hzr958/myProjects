package com.smate.web.v8pub.service.sns.fulltext;

import java.util.AbstractMap.SimpleEntry;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.connector.dao.exception.CreateBatchJobException;
import com.smate.center.batch.connector.service.job.BatchJobsService;
import com.smate.core.base.file.dao.ArchiveFileDao;
import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.file.service.ArchiveFileService;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.v8pub.dao.sns.PubAccessoryPODao;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubAccessoryPO;
import com.smate.web.v8pub.po.sns.PubFullTextPO;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.sns.PubFullTextService;
import com.smate.web.v8pub.service.sns.PubSnsService;

@Service(value = "pubSnsFullTextManageService")
@Transactional(rollbackFor = Exception.class)
public class PubSnsFullTextManageServiceImpl implements PubSnsFullTextManageService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubSnsService pubSnsService;
  @Autowired
  private ArchiveFileDao archiveFileDao;
  @Autowired
  private PubAccessoryPODao pubAccessoryPODao;
  @Autowired
  private PubFullTextService pubFullTextService;
  @Autowired
  private ArchiveFileService archiveFileService;
  @Autowired
  private BatchJobsService batchJobsService;
  /*
   * @Autowired private JobService jobService;
   */

  @Override
  public String uploadPubFullText(PubDTO pubDTO) throws ServiceException {
    Map<String, String> resultMap = new HashMap<String, String>();
    // 保存成果全文信息
    savaFullText(pubDTO);

    // TODO 未添加的逻辑：对这条成果的全文推荐都自动确认为不是这条成果的全文.

    // 更新群组与个人库成果关系表的修改时间，用于拉成果数据
    // grpPubService.updateGrpPubsGmtModified(pubDTO.pubId);

    // 设置返回数据
    resultMap.put("msg", "success");
    // 设置成果全文缩略图图片
    resultMap.put("thumbnailPath", "");
    // 设置成果全文下载地址
    resultMap.put("downloadUrl", "");
    return JacksonUtils.jsonObjectSerializer(resultMap);
  }

  protected void savaFullText(PubDTO pubDTO) throws ServiceException {
    ArchiveFile archiveFile = archiveFileDao.get(pubDTO.pubId);
    PubFullTextPO pubFullTextPO = pubFullTextService.get(pubDTO.pubId);
    // 保存成果全文信息
    if (pubFullTextPO == null) {
      // 没有全文，添加全文记录
      pubFullTextPO = new PubFullTextPO();
      pubFullTextPO.setPubId(pubDTO.pubId);
      pubFullTextPO.setGmtCreate(new Date());
    }
    // 获取老的文件ID
    Long oldFulltextFileId = Optional.ofNullable(pubFullTextPO).map(pf -> pf.getFileId()).orElse(0L);
    // 没有全文，添加全文记录
    pubFullTextPO.setFileName(archiveFile.getFileName());
    pubFullTextPO.setFileId(archiveFile.getFileId());
    pubFullTextPO.setFileDesc(archiveFile.getFileDesc());
    // pubFullTextPO.setPermission(pubDTO.permission);
    pubFullTextPO.setGmtModified(new Date());
    pubFullTextService.saveOrUpdate(pubFullTextPO);
    // 开启生成成果全文缩略图的任务
    try {
      if (ObjectUtils.notEqual(pubFullTextPO.getFileId(), oldFulltextFileId)) {
        savaThumbnailJob(pubFullTextPO.getFileId(), pubFullTextPO.getPubId());
      }
    } catch (Exception e) {
      logger.error("保存成果全文缩略图任务出错，pubId={},fileId={}",
          new Object[] {pubFullTextPO.getPubId(), pubFullTextPO.getFileId()}, e);
      throw new ServiceException(e);
    }
  }

  protected void savaThumbnailJob(Long fulltextFileId, Long pubId) throws CreateBatchJobException {
    ArchiveFile archiveFile = archiveFileService.getArchiveFileById(fulltextFileId);
    // SCM-15887
    // 如果是图片类型或者pdf类型的文件，那么添加缩略图生成任务
    if (archiveFileService.isImageFile(archiveFile)) {
      // 增加pubId的参数
      SimpleEntry<String, Object> pubIdEntry = new SimpleEntry<>("pub_id", pubId);
      batchJobsService.createAndSaveThumbnailBatchJob(archiveFile, FileTypeEnum.SNS_FULLTEXT, pubIdEntry);
    }
    if (archiveFileService.isPDFile(archiveFile)) {
      // 增加pubId的参数
      // SimpleEntry<String, Object> pubIdEntry = new SimpleEntry<>("pub_id", pubId);
      /*
       * jobService.addJob( JobCreator.createThumbnailJob(archiveFile, FileTypeEnum.SNS_FULLTEXT,
       * pubIdEntry));
       */
    }
  }

  @Override
  public String getFulltextName(Long fileId) throws ServiceException {
    if (fileId == null) {
      return "";
    }
    return archiveFileDao.getArchiveFileName(fileId);
  }

  @Override
  public List<PubAccessoryPO> getPubAccessoryPOList(Long pubId) throws ServiceException {
    if (pubId == null) {
      return null;
    }
    return pubAccessoryPODao.getPubAccessoryPOList(pubId);
  }

}
