package com.smate.center.task.quartz.tmp;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.model.pdwh.quartz.TmpTaskInfoRecord;
import com.smate.center.task.service.pdwh.quartz.RenamePdwhFulltextFileService;

/**
 * 重命名pdwh全文文件名
 * 
 * @author LIJUN
 *
 */
public class RenamePdwhFulltextFileTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 5000; // 每次处理的个数

  public RenamePdwhFulltextFileTask() {
    super();
  }

  public RenamePdwhFulltextFileTask(String beanName) {
    super(beanName);
  }

  @Autowired
  RenamePdwhFulltextFileService renamePdwhFulltextFileService;

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========SplitPatentCategoryTask已关闭==========");
      return;
    }

    List<TmpTaskInfoRecord> needTohandleList = null;
    try {

      needTohandleList = renamePdwhFulltextFileService.batchGetJobList(SIZE);
    } catch (Exception e1) {
      logger.error("RenamePdwhFulltextFileTask批量获取预处理数据出错！", e1);
    }
    if (CollectionUtils.isEmpty(needTohandleList)) {
      try {
        super.closeOneTimeTask();
      } catch (TaskException e) {
        logger.error("关闭RenamePdwhFulltextFileTask出错！", e);
      }
    }
    for (TmpTaskInfoRecord job : needTohandleList) {
      try {
        renamePdwhFulltextFileService.startProcessing(job);
      } catch (Exception e) {
        logger.error("重命名pdwh全文文件名出错！pubId:" + job.getHandleId());
        renamePdwhFulltextFileService.updateTaskStatus(job.getJobId(), 2, "重命名pdwh全文文件名出错！" + e.getMessage());// 处理失败
      }
    }
  }

}
