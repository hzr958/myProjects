package com.smate.center.task.quartz;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.model.pdwh.quartz.TmpTaskInfoRecord;
import com.smate.center.task.service.pdwh.quartz.SplitPubAuthorInfoService;

/**
 * 拆分xml获取成果作者信息任务
 * 
 * @author LJ
 *
 *         2017年8月14日
 */
public class SplitPubAuthorInfoTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 5000; // 每次刷新获取的个数
  @Autowired
  private SplitPubAuthorInfoService splitPubAuthorInfoService;

  public SplitPubAuthorInfoTask() {
    super();
  }

  public SplitPubAuthorInfoTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {

    /**
     * 本任务执行前需要初始化数据到临时任务表（获取pubid保存到临时任务表）,sql eg:
     * <p>
     * insert into scmpdwh.TMP_TASK_INFO_RECORD (job_id,job_type,handle_id)
     * <p>
     * select scmpdwh.SEQ_TMP_TASK_INFO_RECORD.nextval,1,t.pub_id
     * <p>
     * from scmpdwh.pdwh_publication t ;
     * <p>
     */
    if (!super.isAllowExecution()) {
      return;
    }

    List<TmpTaskInfoRecord> needTohandleList = null;
    try {
      needTohandleList = splitPubAuthorInfoService.batchGetJobList(SIZE);
    } catch (Exception e1) {
      logger.error("SplitPubAuthorInfoTask批量获取预处理数据出错！", e1);
    }
    if (CollectionUtils.isEmpty(needTohandleList)) {
      try {
        super.closeOneTimeTask();
      } catch (TaskException e) {
        logger.error("关闭SplitPubAuthorInfoTask出错！", e);
      }
    }
    for (TmpTaskInfoRecord job : needTohandleList) {
      try {
        splitPubAuthorInfoService.startSplitInfo(job);
      } catch (Exception e) {
        logger.error("成果xml获取作者信息出错！pubId:" + job.getHandleId());
        splitPubAuthorInfoService.updateTaskStatus(job.getJobId(), 2, "获取xml作者信息出错！" + e.getMessage());// 处理失败
      }
    }

  }
}
