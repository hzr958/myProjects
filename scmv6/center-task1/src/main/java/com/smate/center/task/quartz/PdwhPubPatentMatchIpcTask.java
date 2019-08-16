package com.smate.center.task.quartz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.model.pdwh.quartz.PubCategoryPatentTemp;
import com.smate.center.task.service.pdwh.quartz.PdwhPubPatentMatchIpcService;

/**
 * 基准库成果-专利-匹配ipc
 * 
 * @author zzx
 *
 */
public class PdwhPubPatentMatchIpcTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private static int batchSize = 200;
  @Autowired
  private PdwhPubPatentMatchIpcService pdwhPubPatentMatchIpcService;

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      return;
    }
    List<PubCategoryPatentTemp> list = null;
    boolean status = true;
    Long count = 0L;
    do {
      count++;
      try {
        list = pdwhPubPatentMatchIpcService.getList(batchSize);
      } catch (Exception e) {
        logger.error("基准库成果-专利-匹配ipc任务查询列表出错", e);
      }
      if (list == null || list.size() <= 0) {
        status = false;
        try {
          super.closeOneTimeTask();
        } catch (TaskException e) {
          logger.error("关闭PdwhPubPatentMatchIpcTask出错！", e);
        }
      } else {
        for (PubCategoryPatentTemp one : list) {
          try {
            pdwhPubPatentMatchIpcService.doHandle(one);
          } catch (Exception e) {
            logger.error("基准库成果-专利-匹配ipc出错,irisPsnId=", e);
          }
        }
      }
    } while (status && count < 7777777);
  }

  public PdwhPubPatentMatchIpcTask() {
    super();
  }

  public PdwhPubPatentMatchIpcTask(String beanName) {
    super(beanName);
  }
}
