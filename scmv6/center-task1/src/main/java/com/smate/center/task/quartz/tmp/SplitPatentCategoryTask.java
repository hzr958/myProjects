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
import com.smate.center.task.service.pdwh.quartz.SplitPatentCategoryService;

/**
 * 获取基准库专利分类号
 * 
 * @author LIJUN
 *
 */
public class SplitPatentCategoryTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 5000; // 每次处理的个数

  public SplitPatentCategoryTask() {
    super();
  }

  public SplitPatentCategoryTask(String beanName) {
    super(beanName);
  }

  @Autowired
  SplitPatentCategoryService splitPatentCategoryService;

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========SplitPatentCategoryTask已关闭==========");
      return;
    }
    /**
     * 本任务执行前需要初始化数据到临时任务表（获取pubid保存到临时任务表）,sql eg:
     * <p>
     * insert into scmpdwh.TMP_TASK_INFO_RECORD (job_id,job_type,handle_id)
     * <p>
     * select scmpdwh.SEQ_TMP_TASK_INFO_RECORD.nextval,10,t.pub_id
     * <p>
     * from scmpdwh.pdwh_publication t where t.pub_type=5 ;
     * <p>
     */
    List<TmpTaskInfoRecord> needTohandleList = null;
    try {
      needTohandleList = splitPatentCategoryService.batchGetJobList(SIZE);
    } catch (Exception e1) {
      logger.error("SplitPatentCategoryTask批量获取预处理数据出错！", e1);
    }
    if (CollectionUtils.isEmpty(needTohandleList)) {
      try {
        super.closeOneTimeTask();
      } catch (TaskException e) {
        logger.error("关闭SplitPatentCategoryTask出错！", e);
      }
    }
    for (TmpTaskInfoRecord job : needTohandleList) {
      try {
        splitPatentCategoryService.startSplitInfo(job);
      } catch (Exception e) {
        logger.error("成果xml获取专利分类信息出错！pubId:" + job.getHandleId());
        splitPatentCategoryService.updateTaskStatus(job.getJobId(), 2, "获取xml专利分类信息出错！" + e.getMessage());// 处理失败
      }
    }

  }

}
