package com.smate.center.task.quartz;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.service.algorithm.AlgorithmFactory;
import com.smate.center.task.single.service.person.PersonManager;
import com.smate.core.base.utils.service.security.SysUserLoginService;

public class PsnRefRecommendTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private int batchSize = 50;
  @Autowired
  private PersonManager personManager;
  @Autowired
  private SysUserLoginService sysUserLoginService;

  @Autowired
  private AlgorithmFactory algorithmFactory;

  public PsnRefRecommendTask() {
    super();
  }

  public PsnRefRecommendTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========PsnRefRecommendTask 已关闭==========");
      return;
    }
    logger.info("=========PsnRefRecommendTask 已开启==========");
    try {
      while (true) {
        // 任务表配置的人员
        List<Long> psnIdList = personManager.findTaskPsnRefCommendIds(batchSize);
        if (CollectionUtils.isEmpty(psnIdList)) {
          psnIdList = sysUserLoginService.getPsnIdsDayBeforeByLogin();
        }
        if (CollectionUtils.isEmpty(psnIdList)) {
          return;
        }
        for (Long psnId : psnIdList) {
          algorithmFactory.runPsnRefRec(psnId);
          personManager.updateTaskPsnRefCommendIds(psnId);
        }
      }
    } catch (Exception e) {
      logger.error("sns给人员推荐基准文献出错", e);
    }
  }
}
