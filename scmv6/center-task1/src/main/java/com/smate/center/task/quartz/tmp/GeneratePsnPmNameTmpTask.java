package com.smate.center.task.quartz.tmp;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.service.pdwh.quartz.UpdatePdwhPubMatchInfoService;

/**
 * 临时任务生成人员别名
 * 
 * @author LIJUN
 * @date 2018年6月6日
 */
public class GeneratePsnPmNameTmpTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 2000; // 每次处理的个数
  @Autowired
  private UpdatePdwhPubMatchInfoService updatePdwhPubMatchInfoService;

  public GeneratePsnPmNameTmpTask() {
    super();
  }

  public GeneratePsnPmNameTmpTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      return;
    }
    while (true) {
      List<Long> psnIds = updatePdwhPubMatchInfoService.tmpGetNeedUpdatePsnConstId(SIZE);
      if (CollectionUtils.isEmpty(psnIds)) {
        break;
      }
      for (Long psnId : psnIds) {
        try {
          updatePdwhPubMatchInfoService.tmpGeneralPsnPmName(psnId);
          updatePdwhPubMatchInfoService.updateStatus(psnId, 9, 1);
        } catch (Exception e) {
          logger.error("生成人员别称出错，psnid：" + psnId, e);
          updatePdwhPubMatchInfoService.updateStatus(psnId, 9, 2);
        }
      }
    }
    try {
      super.closeOneTimeTask();
    } catch (TaskException e) {
      logger.error("关闭任务出错");
    }
  }
}
