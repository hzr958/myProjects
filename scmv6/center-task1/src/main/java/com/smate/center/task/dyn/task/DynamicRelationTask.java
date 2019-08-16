package com.smate.center.task.dyn.task;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.dyn.model.base.DynamicMsg;
import com.smate.center.task.dyn.service.DynamicRelationService;

/**
 * 动态关系处理任务
 * 
 * @author zk
 *
 */
public class DynamicRelationTask extends TaskAbstract {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private DynamicRelationService dynamicRelationService;
  private Integer SIZE = 10;

  public DynamicRelationTask() {}

  public DynamicRelationTask(String beanName) {
    super(beanName);
  }

  public void run() {
    try {
      if (!super.isAllowExecution()) {
        return;
      }
      while (true) {
        List<DynamicMsg> msgList = dynamicRelationService.findDynNeedDeal(SIZE);
        logger.info("DynamicRelationTask在 " + (new Date().toLocaleString()) + " 处理" + msgList.size() + "条记录!");
        if (CollectionUtils.isNotEmpty(msgList)) {
          dynamicRelationService.handleDynRelation(msgList);
        } else {
          break;
        }
        Thread.sleep(1000);
      }
    } catch (Exception e) {
      logger.error("DynamicRelationTask--动态关系处理任务出错", e);
    }
  }
}
