package com.smate.center.task.v8pub.task;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.v8pub.service.DataTaskService;
import com.smate.center.task.v8pub.service.PubHandleService;
import com.smate.center.task.v8pub.sns.po.PubDataTaskPO;
import com.smate.core.base.utils.string.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class PubSnsDataInitTask extends TaskAbstract {

  public PubSnsDataInitTask() {
    super();
  }

  public PubSnsDataInitTask(String beanName) {
    super(beanName);
  }

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubHandleService pubHandleService;
  @Autowired
  private DataTaskService dataTaskService;

  private Long startId;

  private Long endId;

  private Integer SIZE = 500;

  public void doRun() {
    try {
      if (!super.isAllowExecution()) {
        return;
      }
      while (true) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<PubDataTaskPO> pubList = pubHandleService.findPubNeedDeal(startId, endId, SIZE);
        logger.info("PubSnsDataInitTask在 " + sdf.format(new Date()) + " 处理" + pubList.size() + "条记录!");
        if (CollectionUtils.isNotEmpty(pubList)) {
          for (PubDataTaskPO pubData : pubList) {
            try {
              pubHandleService.handlePub(pubData);
              pubData.setStatus(1);
            } catch (Exception e) {
              logger.error("初始化Sns库成果数据出错！" + pubData.getPubId(), e);
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
      logger.error("PubSnsDataInitTask运行异常", e);
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
