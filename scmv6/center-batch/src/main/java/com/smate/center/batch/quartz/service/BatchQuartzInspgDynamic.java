package com.smate.center.batch.quartz.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.connector.model.job.BatchQuartz;
import com.smate.center.batch.model.dynamic.InspgDynamicRefresh;
import com.smate.center.batch.service.dynamic.DynTaskService;
import com.smate.core.base.utils.exception.BatchTaskException;

public class BatchQuartzInspgDynamic implements BatchQuartzTaskService {

  Logger logger = LoggerFactory.getLogger(this.getClass());

  private final static Integer SIZE = 300; // 每次刷新获取的个数

  @Autowired
  private DynTaskService dynTaskService;

  @Override
  public void taskExecute(BatchQuartz task) throws BatchTaskException {
    try {
      logger.debug("===========================================BatchQuartzInspgDynamic=========开始1");
      List<InspgDynamicRefresh> list = dynTaskService.getRefreshListBySize(SIZE);
      logger.debug("===========================================BatchQuartzInspgDynamic  获取动态刷新列表=========2");

      if (list != null && list.size() > 0) {
        for (InspgDynamicRefresh refresh : list) {
          try {
            dynTaskService.executeDyn(refresh);
          } catch (Exception e) {
            // TODO 更改错误数据的状态字段,保存错误信息.
            refresh.setStatus(99);
            refresh.setErrorMsg(e.getMessage());
            dynTaskService.saveErrorData(refresh);
            logger.debug("动态刷新出错==============", e);
          }
        }
      } else {
        logger
            .debug("===========================================BatchQuartzInspgDynamic  获取动态刷新列表没有数据!!!!============3");
      }

    } catch (Exception e) {
      logger.error("BatchQuartzInspgDynamic运行异常", e);
      throw new BatchTaskException(e);
    }
  }

}
