package com.smate.center.task.quartz.sns;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.model.pdwh.quartz.TmpTaskInfoRecord;
import com.smate.center.task.service.rol.quartz.SyncPmNameFromPersonService;

/**
 * 对比perosn表补充PSN_PM_ISINAME，PSN_PM_CNKINAME，psn_ins 数据
 * 
 * 1,第一步任务只从psn_ins补充数据到PSN_PM_ISINAME，PSN_PM_CNKINAME，暂没有从person补充
 * 
 * @author LIJUN
 *
 */
public class SyncPmNameFromPersonTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 5000; // 每次处理的个数
  @Autowired
  private SyncPmNameFromPersonService syncPmNameFromPersonService;

  public SyncPmNameFromPersonTask() {
    super();
  }

  public SyncPmNameFromPersonTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========SyncPmNameFromPersonTask已关闭==========");
      return;
    }
    List<TmpTaskInfoRecord> needTohandleList = null;
    try {
      needTohandleList = syncPmNameFromPersonService.batchGetNeedSyncData(SIZE);
    } catch (Exception e) {
      logger.error("SyncPmNameFromPersonTask批量获取预处理数据出错！", e);
    }
    if (CollectionUtils.isEmpty(needTohandleList)) {
      try {
        super.closeOneTimeTask();
      } catch (TaskException e) {
        logger.error("关闭SyncPmNameFromPersonTask出错！", e);
      }
    }
    for (TmpTaskInfoRecord job : needTohandleList) {
      try {
        syncPmNameFromPersonService.startFillData(job);
      } catch (Exception e) {
        logger.error("SyncPmNameFromPersonTask补充数据错误！");
        syncPmNameFromPersonService.updateTaskStatus(job.getJobId(), 2, "补充数据错误！" + e.getMessage());// 处理失败
      }
    }

  }
}
