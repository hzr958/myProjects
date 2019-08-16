package com.smate.sie.center.task.quartz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.sie.center.task.model.SieMergeInsReflush;
import com.smate.sie.center.task.service.SieMergeInsMainService;

/**
 * SIE合并单位
 * 
 * @author 叶星源
 * @Date 201810
 */
public class SieMergeInsTask extends TaskAbstract {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private static final int BATCH_SIZE = 100;// 一次最多更新数量
  @Autowired
  private SieMergeInsMainService sieMergeInsMainService;

  public SieMergeInsTask() {
    super();
  }

  public SieMergeInsTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws Exception {
    if (!super.isAllowExecution()) {
      return;
    }
    try {
      while (true) {
        // 获取需要更新的数据
        List<SieMergeInsReflush> list = sieMergeInsMainService.getNeedRefreshData(BATCH_SIZE);
        if (list == null || list.size() == 0) {
          return;
        }
        for (SieMergeInsReflush sieMergeInsReflush : list) {
          try {
            sieMergeInsMainService.mergeInsMainMethod(sieMergeInsReflush);
            sieMergeInsMainService.deleteOriginalData(sieMergeInsReflush.getMergeInsId());
            sieMergeInsMainService.finishDoneMethod(sieMergeInsReflush, 1);
          } catch (Exception e) {
            sieMergeInsMainService.finishDoneMethod(sieMergeInsReflush, 99);
            logger.error("SIE合并单位(SieMergeInsTaskTriggers)数据错误：", e);
            e.printStackTrace();
          }
        }
      }
    } catch (Exception e) {
      logger.error("SIE获取合并单位(siedep4)数据错误：", e);
    }
  }
}
