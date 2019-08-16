package com.smate.center.task.v8pub.sorl.update;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.v8pub.pdwh.po.PdwhDataTaskPO;
import com.smate.core.base.utils.string.StringUtils;

public class PdwhPubSorlUpdateTask extends TaskAbstract {

  @Autowired
  private PdwhPubSorlService pdwhPubSorlService;

  public PdwhPubSorlUpdateTask() {
    super();
  }

  public PdwhPubSorlUpdateTask(String beanName) {
    super(beanName);
  }

  protected Logger logger = LoggerFactory.getLogger(getClass());

  private Long startId;

  private Long endId;

  private Integer SIZE = 500;

  public void doRun() {
    try {
      if (!super.isAllowExecution()) {
        return;
      }
      while (true) {
        List<PdwhDataTaskPO> pubList = pdwhPubSorlService.findPdwhId(startId, endId, SIZE);
        if (CollectionUtils.isNotEmpty(pubList)) {
          for (PdwhDataTaskPO pubData : pubList) {
            try {
              pdwhPubSorlService.updatePdwhPubSorl(pubData);
            } catch (Exception e) {
              logger.error("更新基准库sorl数据出错！" + pubData.getPubId(), e);
              pubData.setStatus(-2);
              pubData.setError(StringUtils.substring(e.getMessage(), 0, 500));
              pdwhPubSorlService.save(pubData);
            }
          }
        } else {
          break;
        }
      }
    } catch (Exception e) {
      logger.error("PdwhPubSorlUpdateTask运行异常", e);
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
