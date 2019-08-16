package com.smate.center.task.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.service.sns.quartz.TaskMarkerService;
import com.smate.center.task.single.service.pub.PublicationIndexService;
import com.smate.core.base.utils.cache.CacheService;

public class KeywordsSuggestIndexTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final static Integer SIZE = 1000; // 每次刷新获取的个数
  @Autowired
  private CacheService cacheService;
  @Autowired
  PublicationIndexService publicationIndexService;

  public static String INDEX_TYPE_KEYWORDS = "keywords_index";

  public KeywordsSuggestIndexTask() {
    super();
  }

  public KeywordsSuggestIndexTask(String beanName) {
    super(beanName);
  }

  @Autowired
  private TaskMarkerService taskMarkerService;


  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========FundIndexTask已关闭==========");
      return;
    }

    if (taskMarkerService.getApplicationQuartzSettingValue("KeywordsSuggestIndexTask_removeKeywordsCache") == 1) {
      cacheService.remove(INDEX_TYPE_KEYWORDS, "last_kw_id");
    }
    try {
      publicationIndexService.indexKeywords();
    } catch (Exception e) {
      logger.error("FundIndexTask建立solr出错：", e);
    }
  }

}
