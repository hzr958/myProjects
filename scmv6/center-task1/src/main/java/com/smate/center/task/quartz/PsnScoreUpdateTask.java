package com.smate.center.task.quartz;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.psn.model.PsnScoreRefresh;
import com.smate.center.task.service.sns.quartz.PsnScoreService;

public class PsnScoreUpdateTask extends TaskAbstract {
  private static final int Batch_size = 100;
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnScoreService psnScoreService;

  public PsnScoreUpdateTask() {
    super();
  }

  public PsnScoreUpdateTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      return;
    }
    try {
      List<PsnScoreRefresh> refreshlist = psnScoreService.getPsnScoreRefreshList(Batch_size);
      if (CollectionUtils.isNotEmpty(refreshlist)) {
        for (PsnScoreRefresh psnScoreRefresh : refreshlist) {
          try {
            // 处理更新人员检索计分.
            psnScoreService.handlerPsnScoreInit(psnScoreRefresh.getPsnId());
          } catch (Exception e) {
            psnScoreService.deletePsnScoreRefresh(psnScoreRefresh.getPsnId());
            logger.error("处理个人信息检索计分刷新时出错啦！", e);
          }
        }
      }
    } catch (Exception e) {
      logger.error("PsnScoreUpdateTask 运行异常", e);
    }
  }
}
