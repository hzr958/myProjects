package com.smate.center.data.quartz;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.data.service.pub.PubSimpleHashService;
import com.smate.center.data.service.task.TaskMarkerService;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 更新成果哈希值任务
 * 
 * @author lhd
 *
 */
public class PubSimpleHashTask {
  private final static Logger logger = LoggerFactory.getLogger(PubSimpleHashTask.class);
  private final static Integer SIZE = 500; // 每次获取成果个数
  @Autowired
  private TaskMarkerService taskMarkerService;
  @Autowired
  private PubSimpleHashService pubSimpleHashService;
  private Long startId;
  private Long endId;
  private String taskName;

  public void run() throws BatchTaskException {
    if (isRun() == false) {
      return;
    } else {
      try {
        doRun();
      } catch (BatchTaskException e) {
        throw new BatchTaskException(e);
      }
    }
  }

  private void doRun() throws BatchTaskException {
    // for (Long i = startId; i < endId; i += SIZE) {
    while (startId < endId) {
      try {
        long s1 = Calendar.getInstance().getTimeInMillis();
        Long end = pubSimpleHashService.getPubSimpleHash(SIZE, startId, endId);
        if (end != null) {
          startId = end;
        }
        long e1 = Calendar.getInstance().getTimeInMillis();
        logger.error(taskName + "===" + SIZE + "条完成时间: " + (e1 - s1));
      } catch (Exception e) {
        try {
          Thread.sleep(300000);
          startId -= SIZE;
        } catch (InterruptedException e1) {
          logger.error(taskName + "线程休眠==================", e1);
        }
        logger.error(taskName + "处理出错,休眠下==================", e);
      }
    }
    taskMarkerService.closeQuartzApplication(taskName);
    logger.error(taskName + "数据处理完毕====================");
  }

  public boolean isRun() throws BatchTaskException {
    // 任务开关控制逻辑
    return taskMarkerService.getApplicationQuartzSettingValue(taskName) == 1;
  }

  public Long getStartId() {
    return startId;
  }

  public void setStartId(Long startId) {
    this.startId = startId;
  }

  public Long getEndId() {
    return endId;
  }

  public void setEndId(Long endId) {
    this.endId = endId;
  }

  public String getTaskName() {
    return taskName;
  }

  public void setTaskName(String taskName) {
    this.taskName = taskName;
  }

}
