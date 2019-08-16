package com.smate.center.task.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.AppSettingContext;
import com.smate.center.task.base.AppSettingService;
import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.service.solrindex.AllIndexHandleService;
import com.smate.center.task.service.solrindex.IndexInfoVO;

public class PdwhPaperIndexHandleTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private AllIndexHandleService allIndexHandleService;
  @Autowired
  private AppSettingService appSettingService;
  private Long startId;
  private Long endId;
  private String paperKeyValue;


  public PdwhPaperIndexHandleTask() {
    super();
  }

  public PdwhPaperIndexHandleTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========IndexInfoInitTask 已关闭==========");
      return;
    }

    try {
      IndexInfoVO indexInfo = new IndexInfoVO();
      Long lastPaperId = startPubId() == null ? startId : startPubId();
      if (lastPaperId == null) {
        indexInfo.setLastPaperId(startId);
      } else {
        indexInfo.setLastPaperId(lastPaperId);
      }
      indexInfo.setServiceType("pdwhPaperIndex");
      indexInfo.setMaxPubId(endId);
      // 处理基准库论文索引
      allIndexHandleService.runIndex(indexInfo);

      appSettingService.updateSetting(paperKeyValue, indexInfo.getLastPaperId().toString());
    } catch (Exception e) {
      logger.debug("Pdwhpaper索引创建出错", e);
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

  public String getPaperKeyValue() {
    return paperKeyValue;
  }

  public void setPaperKeyValue(String paperKeyValue) {
    this.paperKeyValue = paperKeyValue;
  }

  private Long startPubId() {
    return AppSettingContext.getLongValue(paperKeyValue);
  }

}
