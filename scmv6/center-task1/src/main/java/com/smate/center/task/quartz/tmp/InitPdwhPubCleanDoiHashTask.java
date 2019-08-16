package com.smate.center.task.quartz.tmp;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.service.pdwh.quartz.InitPdwhPubCleanDoiHashService;

public class InitPdwhPubCleanDoiHashTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 5000; // 每次处理的个数

  public InitPdwhPubCleanDoiHashTask() {
    super();
  }

  public InitPdwhPubCleanDoiHashTask(String beanName) {
    super(beanName);
  }

  @Autowired
  private InitPdwhPubCleanDoiHashService initPdwhPubCleanDoiHashService;

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========InitPdwhPubCleanDoiHashTask已关闭==========");
      return;
    }

    List<Long> ids = null;
    try {
      ids = initPdwhPubCleanDoiHashService.batchGetPubIdList(SIZE);
    } catch (Exception e1) {
      logger.error("InitPdwhPubCleanDoiHashTask批量获取预处理数据出错！", e1);
    }
    if (CollectionUtils.isEmpty(ids)) {
      try {
        super.closeOneTimeTask();
      } catch (TaskException e) {
        logger.error("关闭InitPdwhPubCleanDoiHashTask出错！", e);
      }
    }
    for (Long pubId : ids) {
      try {
        initPdwhPubCleanDoiHashService.startProcessing(pubId);
      } catch (Exception e) {
        logger.error("生成doihash出错！pubId:" + pubId);
      }
    }

  }
}
