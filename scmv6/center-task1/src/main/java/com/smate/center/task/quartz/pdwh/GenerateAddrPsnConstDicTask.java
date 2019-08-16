package com.smate.center.task.quartz.pdwh;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.service.pdwh.quartz.GenerateAddrPsnConstDicService;

/**
 * 生成字典任务
 * 
 * @author LIJUN
 * @date 2018年3月31日
 */
public class GenerateAddrPsnConstDicTask extends TaskAbstract {

  public GenerateAddrPsnConstDicTask() {
    super();
  }

  public GenerateAddrPsnConstDicTask(String beanName) {
    super(beanName);
  }

  @Autowired
  private GenerateAddrPsnConstDicService generateAddrPsnConstDicService;

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      return;
    }
    try {
      generateAddrPsnConstDicService.generatePdwhPubMatchDic();
    } catch (Exception e) {
      logger.error("GenerateAddrPsnConstDicTask出错！", e);
    }

  }

}
