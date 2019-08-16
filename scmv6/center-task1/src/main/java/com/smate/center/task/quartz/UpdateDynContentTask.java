package com.smate.center.task.quartz;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.dyn.model.base.DynContentUpdateStatus;
import com.smate.center.task.dyn.service.UpdateDynContentService;
import com.smate.center.task.exception.SingleTaskException;

/***
 * SCM-13465 处理历史数据，用新模板更新动态内容
 * 
 * @author LJ
 *
 *         2017年7月21日
 */
public class UpdateDynContentTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private int batchSize = 300;
  @Autowired
  private UpdateDynContentService updateDynContentService;

  public UpdateDynContentTask() {
    super();
  }

  public UpdateDynContentTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {

    if (!super.isAllowExecution()) {
      return;
    }
    List<DynContentUpdateStatus> dynIdList = null;
    while (true) {
      // 获取需要更新的动态记录
      try {
        dynIdList = updateDynContentService.getDynMsgInfo(batchSize);
      } catch (Exception e1) {
        logger.error("批量获取dynId出错！", e1);
      }

      if (CollectionUtils.isEmpty(dynIdList)) {
        logger.info("dynIdList为空，动态内容更新完毕！");
        break;
      }
      for (DynContentUpdateStatus dynlist : dynIdList) {
        try {
          updateDynContentService.updateDynContentById(dynlist);
        } catch (Exception e) {
          logger.error("动态内容更新出错！dynid:" + dynlist.getDynId(), e);
        }
      }
    }
    try {
      if (updateDynContentService.getTaskStatus() == false) {
        super.closeOneTimeTask();
      }
    } catch (Exception e) {
      logger.error("关闭任务UpdateDynContentTask出错！");
    }

  }

}
