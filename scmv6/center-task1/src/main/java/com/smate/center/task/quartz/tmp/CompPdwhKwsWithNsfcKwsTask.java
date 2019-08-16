package com.smate.center.task.quartz.tmp;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.service.tmp.CompPdwhKwsWithNsfcKwsService;

/**
 * 
 * @author LIJUN
 * @date 2018年4月12日
 */
public class CompPdwhKwsWithNsfcKwsTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 2000; // 每次处理的个数
  @Autowired
  private CompPdwhKwsWithNsfcKwsService compPdwhKwsWithNsfcKwsService;

  public CompPdwhKwsWithNsfcKwsTask() {
    super();
  }

  public CompPdwhKwsWithNsfcKwsTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========CompPdwhKwsWithNsfcKwsTask已关闭==========");
      return;
    }

    try {
      compPdwhKwsWithNsfcKwsService.generatePubKeywordHash();
    } catch (Exception e1) {
      logger.error("初始化生成nsfc keyword hash出错", e1);
    }

    List<Long> ids = compPdwhKwsWithNsfcKwsService.batchGetHasKwsPdwhPubIds(SIZE);

    if (CollectionUtils.isEmpty(ids)) {
      try {
        super.closeOneTimeTask();
      } catch (TaskException e) {
        logger.error("CompPdwhKwsWithNsfcKwsTask关闭失败", e);
      }
    }
    for (Long id : ids) {
      try {
        compPdwhKwsWithNsfcKwsService.startMacthKeywords(id);
      } catch (Exception e) {
        logger.error("CompPdwhKwsWithNsfcKwsTask 匹配错误，关键词id:" + id, e);
        compPdwhKwsWithNsfcKwsService.updateMatchStatus(id, 3);// 匹配错误
      }
    }

  }

}
