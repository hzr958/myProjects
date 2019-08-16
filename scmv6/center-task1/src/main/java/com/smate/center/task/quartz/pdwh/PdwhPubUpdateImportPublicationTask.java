package com.smate.center.task.quartz.pdwh;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.service.pdwh.quartz.PdwhPubUpdateImportPublicationService;
import com.smate.center.task.sys.quartz.service.QuartzCronExpressionService;
import com.smate.center.task.v8pub.pdwh.po.PubPdwhPO;

/**
 * 多线程执行 每个线程执行2000条 基准库成果更新之后导入pdwh_index_publication表
 * 
 * @author hht
 * @Time 2019年3月6日 下午3:25:41
 */
public class PdwhPubUpdateImportPublicationTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 5000; // 每次处理的个数
  @Autowired
  private PdwhPubUpdateImportPublicationService pdwhImportPublicationService;
  @Autowired
  private QuartzCronExpressionService quartzCronExpressionService;
  public Integer startIndex = 0;// 开始位置
  private String beanName;
  // 多线程 当前同步的次数
  public AtomicInteger currentSynTimes = new AtomicInteger(0);
  private boolean hasNotExist = true;

  public PdwhPubUpdateImportPublicationTask() {
    super();
  }

  public PdwhPubUpdateImportPublicationTask(String beanName) {
    super(beanName);
    this.beanName = beanName;
  }

  public void doRun() throws SingleTaskException {
    if (!isAllowExecution()) {
      logger.info("=========PdwhPubUpdateImportPublicationTask已关闭==========");
      return;
    }
    long t1 = System.currentTimeMillis();
    // 还有不存在的数据
    if (hasNotExist) {
      hasNotExist = false;
      pdwhImportPublicationService.deleteNotExist();
    }
    try {

      startIndex = currentSynTimes.getAndIncrement() * SIZE;
      List<PubPdwhPO> updatePdwhs = pdwhImportPublicationService.getUpdatePdwhMonth(startIndex, SIZE);
      if (updatePdwhs != null && updatePdwhs.size() > 0) {
        for (PubPdwhPO pubPdwhPO : updatePdwhs) {
          pdwhImportPublicationService.savePdwhIndexPublication(pubPdwhPO);
        }
      } else {
        long t2 = System.currentTimeMillis();
        super.closeOneTimeTask();
        logger.error("PdwhPubUpdateImportPublicationTask-----------------" + (t2 - t1));
      }
    } catch (Exception e) {
      logger.error("PdwhPubUpdateImportPublicationTask,运行异常,", e);
    }
  }
}
