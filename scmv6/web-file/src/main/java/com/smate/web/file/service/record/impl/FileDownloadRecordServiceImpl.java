package com.smate.web.file.service.record.impl;

import java.util.Date;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.file.dao.record.FileDownloadRecordDao;
import com.smate.web.file.form.DownloadFileRes;
import com.smate.web.file.model.record.FileDownloadRecord;
import com.smate.web.file.service.record.FileDownloadRecordService;

/**
 * 文件下载记录服务实现类
 * 
 * @author ChuanjieHou
 * @date 2017年9月14日
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class FileDownloadRecordServiceImpl implements FileDownloadRecordService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private FileDownloadRecordDao fileDownloadRecordDao;

  @Override
  public void saveRecord(FileTypeEnum fileType, Long fileId, Long ownerPsnId, Long downloadPsnId) {
    try {
      FileDownloadRecord record = new FileDownloadRecord();
      record.setDownloadPsnId(downloadPsnId);
      record.setOwnerPsnId(ownerPsnId);
      record.setFileId(fileId);
      record.setFileType(fileType);
      record.setDownloadDate(new Date());
      record.setDownloadSource(Struts2Utils.getHttpReferer());
      record.setDownloadIp(Struts2Utils.getRemoteAddr());
      saveOrUpdate(record);
    } catch (Exception e) {
      logger.error("文件下载记录出现异常！", e);
    }
  }

  @Override
  public void saveOrUpdate(FileDownloadRecord record) {
    fileDownloadRecordDao.save(record);
  }

  @Override
  public void saveRecords(FileTypeEnum fileType, Map<Long, DownloadFileRes> downloadFileResMap, Long currentUserId) {
    if (MapUtils.isEmpty(downloadFileResMap)) {
      return;
    }
    downloadFileResMap.values().forEach(res -> saveRecord(fileType, res.getId(), res.getOwnerPsnId(), currentUserId));
  }
}
