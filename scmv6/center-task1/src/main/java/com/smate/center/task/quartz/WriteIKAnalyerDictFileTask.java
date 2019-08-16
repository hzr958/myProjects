package com.smate.center.task.quartz;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.service.sns.quartz.GenerateIKAnalyerDictService;

/**
 * 从VKeywordsSynonym读取关键词写入到字典文件
 * 
 * @author LIJUN
 *
 */
public class WriteIKAnalyerDictFileTask extends TaskAbstract {
  private String datesuffix;

  public String getDatesuffix() {
    return datesuffix;
  }

  public void setDatesuffix(String datesuffix) {
    this.datesuffix = datesuffix;
  }

  @Autowired
  private GenerateIKAnalyerDictService generateIKAnalyerDictService;

  public WriteIKAnalyerDictFileTask() {
    super();
  }

  public WriteIKAnalyerDictFileTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      return;
    }
    try {
      logger.info("开始写入到字典文件");
      generateIKAnalyerDictService.writeToDicFile(datesuffix);
      super.closeOneTimeTask();
      logger.info("写入到字典文件完毕");
    } catch (Exception e) {
      logger.error("WriteIKAnalyerDictFileTask出错！", e);
    }

  }

}
