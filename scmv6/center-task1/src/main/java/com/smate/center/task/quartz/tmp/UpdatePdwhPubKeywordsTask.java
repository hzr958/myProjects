
package com.smate.center.task.quartz.tmp;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.model.pdwh.quartz.TmpTaskInfoRecord;
import com.smate.center.task.service.tmp.TmpPdwhPubTaskService;

/**
 * 临时任务更新pdwh关键词和作者
 * 
 * @author LIJUN
 * @date 2018年5月31日
 */
public class UpdatePdwhPubKeywordsTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 5000; // 每次处理的个数
  @Autowired
  private TmpPdwhPubTaskService tmpPdwhPubTaskService;

  public UpdatePdwhPubKeywordsTask() {
    super();
  }

  public UpdatePdwhPubKeywordsTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========UpdatePdwhPubKeywordsTask已关闭==========");
      return;
    }
    /**
     * 本任务执行前需要初始化数据到临时任务表（获取pubid保存到临时任务表）,sql eg:
     * <p>
     * insert into v3pdwh.TMP_TASK_INFO_RECORD (job_id,job_type,handle_id)
     * <p>
     * select v3pdwh.SEQ_TMP_TASK_INFO_RECORD.nextval,10,t.pub_id
     * <p>
     * from from v3pdwh.pdwh_pub_keywords t where length(t.keywords)>=200;
     * <p>
     */
    List<TmpTaskInfoRecord> needTohandleList = null;
    try {
      needTohandleList = tmpPdwhPubTaskService.batchGetJobList(SIZE);
    } catch (Exception e1) {
      logger.error("UpdatePdwhPubKeywordsTask批量获取预处理数据出错！", e1);
    }
    if (CollectionUtils.isEmpty(needTohandleList)) {
      try {
        super.closeOneTimeTask();
      } catch (TaskException e) {
        logger.error("关闭UpdatePdwhPubKeywordsTask出错！", e);
      }
    }
    for (TmpTaskInfoRecord job : needTohandleList) {
      try {
        tmpPdwhPubTaskService.startUpdateKeywords(job);
      } catch (Exception e) {
        logger.error("更新keywords或作者出错！pubId:" + job.getHandleId(), e);
        tmpPdwhPubTaskService.updateTaskStatus(job.getJobId(), 2, "更新keywords或作者出错！" + e.getMessage());// 处理失败
      }
    }

  }

}
