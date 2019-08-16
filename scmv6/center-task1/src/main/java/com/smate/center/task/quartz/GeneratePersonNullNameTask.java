package com.smate.center.task.quartz;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.service.sns.psn.GeneratePersonNullNameService;

/**
 * 为中文姓名生成英文姓与名，为英文名拆分姓与名任务
 * 
 * @author LJ
 *
 *         2017年9月18日
 */
public class GeneratePersonNullNameTask extends TaskAbstract {
  private static final int BATCH_SIZE = 2000;
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  GeneratePersonNullNameService generatePersonNullNameService;

  public GeneratePersonNullNameTask() {
    super();
  }

  public GeneratePersonNullNameTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      return;
    }

    List<Long> needTohandleList = null;
    try {

      needTohandleList = generatePersonNullNameService.getNeedTohandleList(BATCH_SIZE);
    } catch (Exception e1) {
      logger.error("GeneratePersonNullNameTask批量获取预处理数据出错！", e1);
    }

    if (CollectionUtils.isEmpty(needTohandleList)) {
      try {
        super.closeOneTimeTask();
      } catch (TaskException e) {
        logger.error("关闭GeneratePersonNullNameTask出错！", e);
      }
    }
    for (Long psnId : needTohandleList) {
      try {
        generatePersonNullNameService.startGeneratePsnNames(psnId);
      } catch (Exception e) {
        logger.error("GeneratePersonNullNameTask出错！psnId:" + psnId, e);
        generatePersonNullNameService.updateTaskStatus(psnId, 2, "GeneratePersonNullNameTask出错！");// 处理失败
      }
    }

  }
}
