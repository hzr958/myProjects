package com.smate.center.task.quartz;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.snsbak.PubPdwhAddrInfoInit;
import com.smate.center.task.service.bdsp.BdspDataStatisticsService;
import com.smate.center.task.service.sns.quartz.TaskMarkerService;
import com.smate.core.base.utils.cache.CacheService;

public class PubPrCityAnalysisTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  public PubPrCityAnalysisTask() {
    super();
  }

  public PubPrCityAnalysisTask(String beanName) {
    super(beanName);
  }

  @Autowired
  private BdspDataStatisticsService bdspDataStatisticsService;

  @Autowired
  private TaskMarkerService taskMarkerService;

  @Autowired
  private CacheService cacheService;

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========PubPrCityAnalysisTask已关闭==========");
      return;
    }
    List<PubPdwhAddrInfoInit> pubList = this.bdspDataStatisticsService.getCityToHandleList();
    if (CollectionUtils.isEmpty(pubList)) {
      logger.info("=================PubPrCityAnalysisTask  没有获取到表数据!!!!============, time = " + new Date());
      return;
    }
    this.bdspDataStatisticsService.pubComparativeAnalysisByAreaCity(pubList);
  }

}
