package com.smate.center.task.v8pub.task;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.v8pub.backups.service.PubSplitService;
import com.smate.center.task.v8pub.dao.sns.PubSnsDetailDAO;
import com.smate.center.task.v8pub.service.DataTaskService;
import com.smate.center.task.v8pub.sns.po.PubDataTaskPO;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.web.v8pub.dom.sns.PubSnsDetailDOM;

public class PubMongodbSpitTask extends TaskAbstract {

  public PubMongodbSpitTask() {
    super();
  }

  public PubMongodbSpitTask(String beanName) {
    super(beanName);
  }

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private DataTaskService dataTaskService;
  @Autowired
  private PubSnsDetailDAO pubSnsDetailDAO;
  @Autowired
  private PubSplitService pubSplitService;

  private Long startId;

  private Long endId;

  private Integer SIZE = 500;

  public void doRun() {
    try {
      if (!super.isAllowExecution()) {
        return;
      }
      while (true) {
        List<PubDataTaskPO> pubList = dataTaskService.findNeedDeal(startId, endId, SIZE);
        if (CollectionUtils.isNotEmpty(pubList)) {
          for (PubDataTaskPO pubData : pubList) {
            try {
              // 先根据pubId获取到mongodb中的数据
              PubSnsDetailDOM pubSnsDetailDOM = pubSnsDetailDAO.findByPubId(pubData.getPubId());
              pubSplitService.backUpSnsPubDoi(pubSnsDetailDOM);
              pubSplitService.backUpSnsPubJournal(pubSnsDetailDOM);
              pubSplitService.backUpSnsPubPatent(pubSnsDetailDOM);
              pubData.setStatus(1);
            } catch (Exception e) {
              logger.error("拆分mongodb数据出错！pubId=" + pubData.getPubId(), e);
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
      logger.error("PubMembersIdTask运行异常", e);
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
