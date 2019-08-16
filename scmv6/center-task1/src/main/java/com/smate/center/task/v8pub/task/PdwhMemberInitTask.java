package com.smate.center.task.v8pub.task;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.v8pub.pdwh.po.PdwhDataTaskPO;
import com.smate.center.task.v8pub.service.DataTaskService;
import com.smate.center.task.v8pub.service.PubHandleService;
import com.smate.core.base.utils.string.StringUtils;

public class PdwhMemberInitTask extends TaskAbstract {

  public PdwhMemberInitTask() {
    super();
  }

  public PdwhMemberInitTask(String beanName) {
    super(beanName);
  }

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private DataTaskService dataTaskService;
  @Autowired
  private PubHandleService pubHandleService;

  private Long startId;

  private Long endId;

  private Integer SIZE = 500;

  public void doRun() {
    try {
      if (!super.isAllowExecution()) {
        return;
      }
      while (true) {
        List<PdwhDataTaskPO> pubList = pubHandleService.findPdwhNeedDeal(startId, endId, SIZE);
        if (CollectionUtils.isNotEmpty(pubList)) {
          for (PdwhDataTaskPO pubData : pubList) {
            try {
              pubHandleService.handlePdwhMember(pubData);
              pubData.setStatus(1);
            } catch (Exception e) {
              logger.error("初始化pdwh库成果作者表数据出错！" + pubData.getPubId(), e);
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
      logger.error("PdwhMemberInitTask运行异常", e);
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
