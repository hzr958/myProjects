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
import com.smate.center.task.service.pdwh.quartz.PdwhPubAuthorMatchSnsService;

/**
 * 基准库成果作者信息与sns人员匹配任务SCM-14068
 * 
 * @author LJ
 *
 *         2017年9月6日
 */
public class PdwhPubAuthorMatchSnsTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 1000; // 每次刷新获取的个数

  @Autowired
  PdwhPubAuthorMatchSnsService pdwhPubAuthorMatchSnsService;

  public PdwhPubAuthorMatchSnsTask() {
    super();
  }

  public PdwhPubAuthorMatchSnsTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    /**
     * 本任务执行前需要初始化数据到临时任务表,sql eg:
     * <p>
     * insert into scmpdwh.TMP_TASK_INFO_RECORD(job_id,job_type,handle_id)
     * <p>
     * select scmpdwh.SEQ_TMP_TASK_INFO_RECORD.nextval,4,t.id
     * <p>
     * from scmpdwh.PDWH_PUB_AHUTHOR_INFO t where t.email is not null ;
     * <p>
     */

    if (!super.isAllowExecution()) {
      return;
    }

    List<TmpTaskInfoRecord> needTohandleList = null;
    try {
      needTohandleList = pdwhPubAuthorMatchSnsService.batchGetJobList(SIZE);
    } catch (Exception e1) {
      logger.error("SplitPatentCategoryTask批量获取预处理数据出错！", e1);
    }

    if (CollectionUtils.isEmpty(needTohandleList)) {
      try {
        super.closeOneTimeTask();
      } catch (TaskException e) {
        logger.error("PdwhPubAuthorMatchSnsTask close faild !");
      }
    }

    for (TmpTaskInfoRecord info : needTohandleList) {
      try {
        pdwhPubAuthorMatchSnsService.startMatching(info);
      } catch (Exception e) {
        logger.error("PdwhPubAuthorMatchSnsTask匹配出错！handleId:" + info.getHandleId());
        pdwhPubAuthorMatchSnsService.updateTaskStatus(info.getJobId(), 2, "匹配出错！" + e.getMessage());
      }
    }

  }
}
