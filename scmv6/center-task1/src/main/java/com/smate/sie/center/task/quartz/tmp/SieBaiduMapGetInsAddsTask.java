package com.smate.sie.center.task.quartz.tmp;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.exception.TaskException;
import com.smate.core.base.utils.model.consts.SieConstRegion;
import com.smate.sie.center.task.service.tmp.SieBaiduMapGetInsAddsService;

/**
 * 后台任务轮询TASK_INS_BAIDU_GET_ADDR， 跑出单位地址信息到本表字段
 * 
 * @author ztg
 *
 */
public class SieBaiduMapGetInsAddsTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 2000; // 每次处理的个数
  @Autowired
  SieBaiduMapGetInsAddsService sieBaiduMapGetInsAddsService;

  public SieBaiduMapGetInsAddsTask() {
    super();
  }

  public SieBaiduMapGetInsAddsTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {

    if (!super.isAllowExecution()) {
      logger.info("=========SieBaiduMapGetInsAddsTask已关闭==========");
      return;
    }

    List<Long> ids = sieBaiduMapGetInsAddsService.batchGetProcessedData(SIZE);

    if (CollectionUtils.isEmpty(ids)) {
      try {
        super.closeOneTimeTask();
      } catch (TaskException e) {
        logger.error("关闭SieBaiduMapGetInsAddsTask出错！", e);
      }
    }
    List<SieConstRegion> cnZhname = sieBaiduMapGetInsAddsService.getAllCNZhname();
    List<SieConstRegion> allName = sieBaiduMapGetInsAddsService.getAllName();
    for (Long id : ids) {
      try {
        sieBaiduMapGetInsAddsService.startProcessing(id, cnZhname, allName);
      } catch (Exception e) {
        logger.error("SieBaiduMapGetInsAddsTask出错,id:" + id, e);
        sieBaiduMapGetInsAddsService.updateStatusById(id, 2);
      }
    }

  }
}
