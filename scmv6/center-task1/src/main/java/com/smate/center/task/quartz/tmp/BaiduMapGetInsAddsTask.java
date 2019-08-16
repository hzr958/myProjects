package com.smate.center.task.quartz.tmp;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.model.sns.pub.ConstRegion;
import com.smate.center.task.service.tmp.BaiduMapGetInsAddsService;

/**
 * 使用百度地图API获取单位具体地址
 * 
 * @author LIJUN
 *
 */
public class BaiduMapGetInsAddsTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 2000; // 每次处理的个数
  @Autowired
  BaiduMapGetInsAddsService baiduMapGetInsAddsService;

  public BaiduMapGetInsAddsTask() {
    super();
  }

  public BaiduMapGetInsAddsTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========BaiduMapGetInsAddsTask已关闭==========");
      return;

    }
    List<Long> ids = baiduMapGetInsAddsService.batchGetProcessedData(SIZE);

    if (CollectionUtils.isEmpty(ids)) {
      try {
        super.closeOneTimeTask();
      } catch (TaskException e) {
        logger.error("关闭BaiduMapGetInsAddsTask出错！", e);
      }
    }
    List<ConstRegion> cnZhname = baiduMapGetInsAddsService.getAllCNZhname();
    List<ConstRegion> allName = baiduMapGetInsAddsService.getAllName();
    for (Long id : ids) {
      try {
        baiduMapGetInsAddsService.startProcessing(id, cnZhname, allName);
      } catch (Exception e) {
        logger.error("BaiduMapGetInsAddsTask出错,id:" + id, e);
        baiduMapGetInsAddsService.updateStatusById(id, 2);
      }
    }

  }

}
