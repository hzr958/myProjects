package com.smate.center.task.v8pub.task;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.v8pub.pdwh.po.PdwhDataTaskPO;
import com.smate.center.task.v8pub.service.PdwhAuthorNamesFormatService;
import com.smate.core.base.utils.string.StringUtils;

/**
 * 格式化基准库成果作者名数据任务
 * 
 * @author YJ
 *
 *         2019年5月9日
 */
public class PdwhAuthorNamesFormatTask extends TaskAbstract {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PdwhAuthorNamesFormatService pdwhAuthorNamesFormatService;

  private Long startId;

  private Long endId;

  private Integer SIZE = 500;

  public PdwhAuthorNamesFormatTask() {
    super();
  }

  public PdwhAuthorNamesFormatTask(String beanName) {
    super(beanName);
  }

  public void doRun() {
    try {
      if (!super.isAllowExecution()) {
        return;
      }
      while (true) {
        List<PdwhDataTaskPO> pubList = pdwhAuthorNamesFormatService.findPdwhNeedDeal(startId, endId, SIZE);
        if (CollectionUtils.isNotEmpty(pubList)) {
          for (PdwhDataTaskPO pubData : pubList) {
            try {
              pdwhAuthorNamesFormatService.formatAuthorNames(pubData);
              pubData.setStatus(1);
            } catch (Exception e) {
              logger.error("格式化基准库作者名数据出错！" + pubData.getPubId(), e);
              pubData.setStatus(-1);
              pubData.setError(StringUtils.substring(e.getMessage(), 0, 500));
            }
            pdwhAuthorNamesFormatService.save(pubData);
          }
        } else {
          break;
        }
      }
    } catch (Exception e) {
      logger.error("PdwhAuthorNamesFormatTask运行异常", e);
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
