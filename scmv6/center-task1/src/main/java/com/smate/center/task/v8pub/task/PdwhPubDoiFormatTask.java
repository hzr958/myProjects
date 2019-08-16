package com.smate.center.task.v8pub.task;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.v8pub.pdwh.po.PdwhDataTaskPO;
import com.smate.center.task.v8pub.service.DataTaskService;
import com.smate.center.task.v8pub.service.PdwhPubDoiFormatService;
import com.smate.core.base.utils.string.StringUtils;

public class PdwhPubDoiFormatTask extends TaskAbstract {

  public PdwhPubDoiFormatTask() {
    super();
  }

  public PdwhPubDoiFormatTask(String beanName) {
    super(beanName);
  }

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private DataTaskService dataTaskService;
  @Autowired
  private PdwhPubDoiFormatService pdwhPubDoiFormatService;

  private Long startId;

  private Long endId;

  private Integer SIZE = 500;


  public void doRun() {
    try {
      if (!super.isAllowExecution()) {
        return;
      }
      while (true) {
        List<PdwhDataTaskPO> pubList = dataTaskService.findPdwhNeedDeal(startId, endId, SIZE);
        if (CollectionUtils.isNotEmpty(pubList)) {
          for (PdwhDataTaskPO pubData : pubList) {
            try {
              pdwhPubDoiFormatService.formatPubDoi(pubData);
              pubData.setStatus(1);
            } catch (Exception e) {
              logger.error("格式化基准库成果doi数据出错！" + pubData.getPubId(), e);
              pubData.setStatus(-1);
              pubData.setError(StringUtils.substring(e.getMessage(), 0, 500));
            }
            dataTaskService.save(pubData);
          }
        } else {
          break;
        }
      }
    } catch (Exception e) {
      logger.error("PdwhPubDoiFormatTask运行异常", e);
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
}
