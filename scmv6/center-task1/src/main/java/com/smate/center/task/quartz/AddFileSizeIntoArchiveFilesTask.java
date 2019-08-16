package com.smate.center.task.quartz;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.model.sns.quartz.ArchiveFile;
import com.smate.center.task.service.sns.quartz.ArchiveFilesService;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 处理ArchiveFiles表中文件大小为空的数据
 * 
 * @author LIJUN
 *
 */
public class AddFileSizeIntoArchiveFilesTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 1000; // 每次处理的个数

  public AddFileSizeIntoArchiveFilesTask() {
    super();
  }

  public AddFileSizeIntoArchiveFilesTask(String beanName) {
    super(beanName);
  }

  @Autowired

  private ArchiveFilesService archiveFilesService;

  public void doRun() throws BatchTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========ChineseNameSplitTask已关闭==========");
      return;
    }

    List<ArchiveFile> emptySizeFile = archiveFilesService.getEmptySizeFile(SIZE);

    if (CollectionUtils.isEmpty(emptySizeFile)) {
      try {
        super.closeOneTimeTask();
      } catch (TaskException e) {
        logger.error("关闭AddFileSizeIntoArchiveFilesTask出错！", e);
      }
    }

    for (ArchiveFile archiveFile : emptySizeFile) {
      try {
        archiveFilesService.generateFileSize(archiveFile);
      } catch (Exception e) {
        logger.error("AddFileSizeIntoArchiveFilesTask获取文件大小发生错误：fileId:" + archiveFile.getFileId());
      }

    }

  }
}
