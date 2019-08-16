package com.smate.sie.center.task.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.service.sns.quartz.TaskMarkerService;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.sie.center.task.service.NsfcKeywordsTfCotfNService;

public class NsfcKeywordsTfCotfNTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 2000; // 每次刷新获取的个数
  @Autowired
  private NsfcKeywordsTfCotfNService nsfcKeywordsTfCotfNService;
  @Autowired
  private CacheService cacheService;

  public static String INDEX_TYPE_NSFC_KEYWORDS = "nsfc_keywords_index";

  public NsfcKeywordsTfCotfNTask() {
    super();
  }

  public NsfcKeywordsTfCotfNTask(String beanName) {
    super(beanName);
  }

  @Autowired
  private TaskMarkerService taskMarkerService;

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========NsfcKeywordsTfCotfNTask已关闭==========");
      return;
    }
    if (taskMarkerService.getApplicationQuartzSettingValue("NsfcKeywordsTfCotfNTask_removeNsfcKeywordsCache") == 1) {
      cacheService.remove(INDEX_TYPE_NSFC_KEYWORDS, "last_nsfc_keywords_id");
    }
    try {
      nsfcKeywordsTfCotfNService.nsfcKeywordsIndex(SIZE);
    } catch (Exception e) {
      logger.error("NsfcKeywordsTfCotfNTask建立solr出错：", e);
    }
  }
}
