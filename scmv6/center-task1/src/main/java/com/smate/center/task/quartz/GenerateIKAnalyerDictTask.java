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
import com.smate.center.task.service.sns.quartz.GenerateIKAnalyerDictService;

/**
 * 拆分关键词任务
 * 
 * @author LIJUN
 *
 */
public class GenerateIKAnalyerDictTask extends TaskAbstract {
  private static final int BATCH_SIZE = 1000;
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private GenerateIKAnalyerDictService generateIKAnalyerDictService;

  public GenerateIKAnalyerDictTask() {
    super();
  }

  public GenerateIKAnalyerDictTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      return;
    }

    List<TmpTaskInfoRecord> needTohandleList = null;
    try {
      needTohandleList = generateIKAnalyerDictService.batchGetJobList(BATCH_SIZE);
    } catch (Exception e1) {
      logger.error("GenerateIKAnalyerDictTask批量获取预处理数据出错！", e1);
    }

    if (CollectionUtils.isEmpty(needTohandleList)) {
      try {
        super.closeOneTimeTask();
      } catch (TaskException e) {
        logger.error("关闭GenerateIKAnalyerDictTask出错！", e);
      }
    }
    for (TmpTaskInfoRecord job : needTohandleList) {
      try {
        generateIKAnalyerDictService.startJob(job);
      } catch (Exception e) {
        logger.error("GenerateIKAnalyerDictTask运行异常", e);
        generateIKAnalyerDictService.updateTaskStatus(job.getJobId(), 2,
            "GenerateIKAnalyerDictTask运行异常！" + e.getMessage());// 处理失败
      }

    }

  }

}
