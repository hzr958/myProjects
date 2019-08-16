package com.smate.web.file.service.record;

import java.util.Map;

import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.web.file.form.DownloadFileRes;
import com.smate.web.file.model.record.FileDownloadRecord;

/**
 * 文件下载记录服务接口
 * 
 * @author ChuanjieHou
 * @date 2017年9月14日
 */
public interface FileDownloadRecordService {
  /**
   * 记录单个文件下载记录
   *
   * @author houchuanjie
   * @date 2018年3月7日 下午2:18:45
   * @param fileType
   * @param fileId
   * @param ownerPsnId
   * @param downloadPsnId
   */
  public void saveRecord(FileTypeEnum fileType, Long fileId, Long ownerPsnId, Long downloadPsnId);

  public void saveOrUpdate(FileDownloadRecord record);

  /**
   * 记录多个文件下载记录
   * 
   * @author houchuanjie
   * @date 2018年3月7日 下午2:18:39
   * @param fileType
   * @param archiveFileResList
   * @param ownerPsnId
   * @param currentUserId
   */
  public void saveRecords(FileTypeEnum fileType, Map<Long, DownloadFileRes> downloadFileResMap, Long currentUserId);
}
