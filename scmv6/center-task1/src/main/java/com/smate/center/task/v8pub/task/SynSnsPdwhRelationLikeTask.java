package com.smate.center.task.v8pub.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.v8pub.service.SynSnsPdwhRelationService;

/**
 * 关联成果个人库基准库赞记录同步
 * 
 * @author yhx
 *
 */
public class SynSnsPdwhRelationLikeTask extends TaskAbstract {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 100; // 每次刷新获取的个数

  private Long startId;

  private Long endId;

  @Autowired
  private SynSnsPdwhRelationService synSnsPdwhRelationService;

  public SynSnsPdwhRelationLikeTask() {
    super();
  }

  public SynSnsPdwhRelationLikeTask(String beanName) {
    super(beanName);
  }

  public void doRun() {
    if (!super.isAllowExecution()) {
      logger.info("=========SynSnsPdwhRelationLikeTask 已关闭==========");
      return;
    }
    try {
      while (true) {
        List<Long> list = synSnsPdwhRelationService.getPubPdwhIdList(startId, endId, SIZE);
        if (list != null && list.size() > 0) {
          for (Long pdwhPubId : list) {
            synSnsPdwhRelationService.synLike(pdwhPubId);
          }
        } else {
          super.closeOneTimeTask();
          return;
        }
      }
    } catch (Exception e) {
      logger.error("SynSnsPdwhRelationLikeTask运行异常", e);
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
