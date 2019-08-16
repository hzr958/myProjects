package com.smate.center.task.quartz;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.service.sns.quartz.GenerateGroupCodeService;

/**
 * 生成GrpCode
 * 
 * @author JunLi
 *
 *         2017年11月1日
 */
public class GenerateGroupCodeTask extends TaskAbstract {
  private static final int BATCH_SIZE = 200;
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private GenerateGroupCodeService generateGroupCodeService;

  public GenerateGroupCodeTask() {
    super();
  }

  public GenerateGroupCodeTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      return;
    }

    List<Long> needTohandleList = null;
    try {

      needTohandleList = generateGroupCodeService.getNeedTohandleList(BATCH_SIZE);
    } catch (Exception e1) {
      logger.error("GenerateGroupCodeTask批量获取预处理数据出错！", e1);
    }

    if (CollectionUtils.isEmpty(needTohandleList)) {
      try {
        super.closeOneTimeTask();
      } catch (TaskException e) {
        logger.error("关闭GenerateGroupCodeTask出错！", e);
      }
    }
    for (Long grpId : needTohandleList) {
      try {
        generateGroupCodeService.startGenerateGrpCode(grpId);
      } catch (Exception e) {
        logger.error("GenerateGroupCodeTask出错！grpId:" + grpId, e);
        generateGroupCodeService.updateTaskStatus(grpId, 2, "GenerateGroupCodeTask出错！");// 处理失败
      }
    }

  }
}
