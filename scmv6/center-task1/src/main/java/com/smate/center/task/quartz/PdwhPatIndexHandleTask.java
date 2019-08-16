package com.smate.center.task.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.service.sns.quartz.TaskMarkerService;
import com.smate.center.task.service.solrindex.AllIndexHandleService;
import com.smate.center.task.service.solrindex.IndexInfoVO;
import com.smate.core.base.utils.cache.CacheService;

public class PdwhPatIndexHandleTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private CacheService cacheService;
  @Autowired
  private TaskMarkerService taskMarkerService;
  @Autowired
  private AllIndexHandleService allIndexHandleService;


  private Long startId;
  private Long endId;
  private String patKeyValue;

  public PdwhPatIndexHandleTask() {
    super();
  }

  public PdwhPatIndexHandleTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========IndexInfoInitTask 已关闭==========");
      return;
    }
    if (taskMarkerService.getApplicationQuartzSettingValue("AllIndex_removePdwhPatCache") == 1) {
      cacheService.remove(patKeyValue, "last_pdwh_pat_id");
    }
    try {
      IndexInfoVO indexInfo = new IndexInfoVO();
      Long lastPatId = (Long) cacheService.get(patKeyValue, "last_pdwh_pat_id");
      if (lastPatId == null) {
        indexInfo.setLastPatId(startId);
      } else {
        indexInfo.setLastPatId(lastPatId);
      }
      indexInfo.setServiceType("pdwhPatIndex");
      indexInfo.setMaxPubId(endId);
      // 处理基准库专利索引
      allIndexHandleService.runIndex(indexInfo);
      this.cacheService.put(patKeyValue, 60 * 60 * 24, "last_pdwh_pat_id", indexInfo.getLastPatId());
    } catch (Exception e) {
      logger.debug("PdwhPat索引创建出错", e);
    }
  }

  public Long getStartId() {
    return startId;
  }

  public void setStartId(Long startId) {
    this.startId = startId;
  }

  public Long getEndId() {
    return endId;
  }

  public void setEndId(Long endId) {
    this.endId = endId;
  }

  public String getPatKeyValue() {
    return patKeyValue;
  }

  public void setPatKeyValue(String patKeyValue) {
    this.patKeyValue = patKeyValue;
  }
}
