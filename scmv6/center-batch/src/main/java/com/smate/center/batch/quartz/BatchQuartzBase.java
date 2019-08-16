package com.smate.center.batch.quartz;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.connector.model.job.BatchQuartz;
import com.smate.center.batch.constant.BatchConfConstant;
import com.smate.center.batch.quartz.service.BatchQuartzBaseService;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * batch定时任务统一管理 需要在v_batch_quartz中配置统一任务触发入口
 * 同时需要配置batch-chain.xml中的quartzTaskDispatchers对应的bean，其中的key值与v_batch_quartz中Strategy的值需要对应
 * 注意定时器中的period需要符合规范
 * 
 * @author hzr
 */
public class BatchQuartzBase {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  BatchQuartzBaseService batchQuartzBaseService;

  public final void run() throws BatchTaskException {
    logger.debug("====================================BatchQuartzTasks开始运行");

    if (isRun() == false) {
      logger.debug("====================================BatchQuartzTasks开关关闭");
      return;
    }

    List<BatchQuartz> taskList = batchQuartzBaseService.getTasksByStatus(1);

    if (CollectionUtils.isEmpty(taskList)) {
      logger.debug("=================所有BatchQuartz任务开关关闭");
      return;
    } else {
      for (BatchQuartz single : taskList) {
        // 任务运行次数开关
        Long taskId = single.getQuartzId();
        Integer executeCount = single.getExecuteCount();
        Date lastExecuteTime = single.getEndTime();
        String period = single.getPeriod();
        // 判断此任务是否应该执行：当前时间与lastExecuteTime之差大于period
        Date startTime = new Date();

        try {
          if (isThisTaskNeedToRun(lastExecuteTime, period)) {
            batchQuartzBaseService.execute(single);
            // 对小于99次数的任务，执行一次之后-1

            if (executeCount < 99) {
              executeCount--;
              single.setExecuteCount(executeCount);
              batchQuartzBaseService.saveQuartzJob(single);
            }

            batchQuartzBaseService.updateJobSuccess(taskId, startTime);
          } else {
            continue;
          }
        } catch (BatchTaskException e) {
          String errorMsg = "===TaskId：" + taskId + " , 任务运行异常===";
          logger.error(errorMsg, e);
          batchQuartzBaseService.updateJobError(taskId, e.getMessage(), startTime);
        } catch (Throwable e) {
          String errorMsg = "===TaskId：" + taskId + " , 任务运行异常===";
          logger.error(errorMsg, e);
          batchQuartzBaseService.updateJobError(taskId, e.getMessage(), startTime);
        }
      }
    }
  }

  // 任务开关
  public final boolean isRun() throws BatchTaskException {
    return true;
    // return false;
  }

  // 任务是否该执行
  public final boolean isThisTaskNeedToRun(Date lastExecuteTime, String period) throws BatchTaskException {
    // 校验period
    if (StringUtils.isBlank(period)) {
      throw new BatchTaskException("Quartz任务period为空");
    } else {
      Integer length = period.length();

      if (length <= 1) {
        throw new BatchTaskException("Quartz任务period格式不符合规范");
      }

      String unit = period.substring(length - 1);
      String num = period.substring(0, length - 1);

      if (StringUtils.isBlank(unit)) {
        throw new BatchTaskException("Quartz任务period格式不符合规范");
      } else {
        unit = unit.toLowerCase();
      }

      // 判断是否是数字只能包含小数点如6,6.5
      boolean isNum = num.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");

      if (!isNum) {
        throw new BatchTaskException("Quartz任务period格式不符合规范");
      }

      Long periodToSecond = convertPeriodToSeconds(unit, num);
      Date now = new Date();
      Long timeGap = (now.getTime() - lastExecuteTime.getTime()) / 1000;

      if (timeGap < 0) {
        throw new BatchTaskException("Quartz任务结束时间end_time不正确");
      }

      if (periodToSecond != 0L && timeGap > periodToSecond) {
        return true;
      } else {
        return false;
      }
    }
  }

  // 计算period对应秒数，s秒，m分钟，h小时，d天；但只能包含一种单位：比如1.5小时是1.5h或者90m，但是不能混写为1h30m
  public final Long convertPeriodToSeconds(String unit, String num) throws BatchTaskException {
    Long periodToSecond = 0L;

    switch (unit) {
      case BatchConfConstant.SECOND: {
        double num1 = Double.parseDouble(num);
        periodToSecond = Math.round(num1);
        break;
      }

      case BatchConfConstant.MINUTE: {
        double num1 = Double.parseDouble(num);
        periodToSecond = Math.round(num1 * 60);
        break;
      }

      case BatchConfConstant.HOUR: {
        double num1 = Double.parseDouble(num);
        periodToSecond = Math.round(num1 * 3600);
        break;
      }

      case BatchConfConstant.DAY: {
        double num1 = Double.parseDouble(num);
        periodToSecond = Math.round(num1 * 3600 * 24);
        break;
      }

      default: {
        throw new BatchTaskException("Quartz任务period格式不符合规范");
      }
    }
    return periodToSecond;
  }

}
