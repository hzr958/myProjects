package com.smate.center.task.quartz.pdwh;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.service.search.SystemSearchService;

/**
 * 初始化系统未登录首页-index页面的站外检索结果信息.
 * 
 * @author mjg
 * 
 */
public class IndexInfoInitTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SystemSearchService systemSearchService;

  public IndexInfoInitTask() {
    super();
  }

  public IndexInfoInitTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========IndexInfoInitTask 已关闭==========");
      return;
    }
    logger.info("=========IndexInfoInitTask开始运行==========");
    try {
      Long startTime = new Date().getTime();
      systemSearchService.loadIndexPubInfo();// 成果.
      Long endTime = new Date().getTime();
      logger.error("============IndexInfoInitTask结束 花费时间为：" + (endTime - startTime) / 1000 + "秒");
      super.closeOneTimeTask();
    } catch (Exception e) {
      logger.error("IndexInfoInitTask,运行异常", e);
    }

  }

}
