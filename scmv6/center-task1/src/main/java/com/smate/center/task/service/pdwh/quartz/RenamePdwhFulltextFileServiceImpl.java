package com.smate.center.task.service.pdwh.quartz;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.base.TaskJobTypeConstants;
import com.smate.center.task.dao.pdwh.quartz.TmpTaskInfoRecordDao;
import com.smate.center.task.dao.sns.quartz.ArchiveFileDao;
import com.smate.center.task.model.pdwh.quartz.PdwhPublication;
import com.smate.center.task.model.pdwh.quartz.TmpTaskInfoRecord;
import com.smate.center.task.single.dao.solr.PdwhPublicationDao;
import com.smate.core.base.utils.file.FileUtils;

@Service("renamePdwhFulltextFileService")
@Transactional(rollbackFor = Exception.class)
public class RenamePdwhFulltextFileServiceImpl implements RenamePdwhFulltextFileService {

  private static Integer jobType = TaskJobTypeConstants.RenamePdwhFulltextFileTask;
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private TmpTaskInfoRecordDao tmpTaskInfoRecordDao;
  @Autowired
  private PdwhPublicationDao pdwhPublicationDao;
  @Autowired
  private ArchiveFileDao archiveFileDao;

  @Override
  public void startProcessing(TmpTaskInfoRecord job) throws Exception {
    Long fileId = job.getHandleId();
    Long pubId = job.getRelativeId();

    PdwhPublication pubTitleById = pdwhPublicationDao.getPubTitleById(pubId);
    String zhTitle = pubTitleById.getZhTitle();
    String enTitle = pubTitleById.getEnTitle();
    if (StringUtils.isNotBlank(zhTitle)) {
      archiveFileDao.updateFileNameById(fileId, FileUtils.cleanArcFileName(zhTitle) + ".pdf");
    } else {
      archiveFileDao.updateFileNameById(fileId, FileUtils.cleanArcFileName(enTitle) + ".pdf");
    }

    this.updateTaskStatus(job.getJobId(), 1, "");
  }

  @Override
  public List<TmpTaskInfoRecord> batchGetJobList(Integer size) throws Exception {
    return tmpTaskInfoRecordDao.getTaskInfoRecordList(size, jobType);
  }

  @Override
  public void updateTaskStatus(Long jobId, int status, String err) {
    try {
      tmpTaskInfoRecordDao.updateTaskStatusById(jobId, status, err);
    } catch (Exception e) {
      logger.error("更新任务状态记录出错！jobId " + jobId, e);
    }
  }
}
