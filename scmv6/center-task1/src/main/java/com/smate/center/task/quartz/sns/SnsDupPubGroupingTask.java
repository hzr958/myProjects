package com.smate.center.task.quartz.sns;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.pdwh.quartz.TmpTaskInfoRecord;
import com.smate.center.task.service.sns.pub.SnsDupPubGroupingService;

/**
 * sns 个人重复成果分组任务
 * 
 * @author LIJUN
 *
 */
public class SnsDupPubGroupingTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 5000; // 每次处理的个数

  public SnsDupPubGroupingTask() {
    super();
  }

  public SnsDupPubGroupingTask(String beanName) {
    super(beanName);
  }

  @Autowired
  private SnsDupPubGroupingService snsDupPubGroupingService;

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========SplitPatentCategoryTask已关闭==========");
      return;
    }

    // while (true) {
    // List<Long> batchGetNeedFillData =
    // snsDupPubGroupingService.batchGetNeedFillData(SIZE);
    //
    // if (CollectionUtils.isEmpty(batchGetNeedFillData)) {
    // logger.info("vPubSimpleHash数据同步完成！");
    // break;
    // }
    // for (Long pubId : batchGetNeedFillData) {
    // try {
    // snsDupPubGroupingService.fillVpubSimpleHash(pubId);
    // } catch (Exception e) {
    // logger.error("SnsDupPubGroupingTask补充vPubSimpleHash数据出错！", e);
    // }
    // }
    //
    // }

    List<TmpTaskInfoRecord> needTohandleList = null;
    try {
      needTohandleList = snsDupPubGroupingService.batchGetJobList(SIZE);
    } catch (Exception e1) {
      logger.error("SnsDupPubGroupingTask批量获取预处理数据出错！", e1);
    }

    for (TmpTaskInfoRecord job : needTohandleList) {
      try {
        snsDupPubGroupingService.startProcessing(job);
      } catch (Exception e) {
        logger.error("处理个人重复成果信息出错！pubId:" + job.getHandleId(), e);
        snsDupPubGroupingService.updateTaskStatus(job.getJobId(), 2, "处理个人重复成果信息出错！" + e.getMessage());// 处理失败
      }
    }
  }
}
