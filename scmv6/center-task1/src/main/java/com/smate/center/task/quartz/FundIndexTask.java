package com.smate.center.task.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.service.sns.quartz.TaskMarkerService;
import com.smate.center.task.single.service.pub.FundIndexService;
import com.smate.core.base.utils.cache.CacheService;

public class FundIndexTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final static Integer SIZE = 2000; // 每次刷新获取的个数
  @Autowired
  private CacheService cacheService;
  @Autowired
  private FundIndexService fundIndexService;

  public static String INDEX_TYPE_FUND = "fund_index";

  public FundIndexTask() {
    super();
  }

  public FundIndexTask(String beanName) {
    super(beanName);
  }

  @Autowired
  private TaskMarkerService taskMarkerService;


  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========FundIndexTask已关闭==========");
      return;
    }

    if (taskMarkerService.getApplicationQuartzSettingValue("FundIndexTask_removeFundCache") == 1) {
      cacheService.remove(INDEX_TYPE_FUND, "last_fund_id");
    }
    try {
      fundIndexService.fundIndex(SIZE);
    } catch (Exception e) {
      logger.error("FundIndexTask建立solr出错：", e);
    }
  }

}
