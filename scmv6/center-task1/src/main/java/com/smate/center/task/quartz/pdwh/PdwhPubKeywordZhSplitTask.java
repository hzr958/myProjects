package com.smate.center.task.quartz.pdwh;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.model.pdwh.pub.PdwhPubKeywords20180511;
import com.smate.center.task.service.pdwh.quartz.PdwhPublicationKeywordService;

/**
 * 基准库成果中文关键词，拆分任务
 * 
 * @author aijiangbin
 *
 */
@Deprecated
public class PdwhPubKeywordZhSplitTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 2000; // 每次处理的个数

  @Autowired
  private PdwhPublicationKeywordService pdwhPublicationKeywordService;

  public PdwhPubKeywordZhSplitTask() {
    super();
  }

  public PdwhPubKeywordZhSplitTask(String beanName) {
    super(beanName);
  }

  public void run() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========pdwhPubKeywordZhSplitTask已关闭==========");
      return;

    }
    Boolean canRun = true;
    while (canRun) {
      List<PdwhPubKeywords20180511> list = pdwhPublicationKeywordService.getNoDealPdwhPubKeywordsList(SIZE);
      if (CollectionUtils.isEmpty(list)) {
        canRun = false;
        logger.info("基准库成果关键词已经处理完，pdwh_pub_keywords！");
        try {
          super.closeOneTimeTask();
        } catch (TaskException e) {
          logger.error("关闭pdwhPubKeywordZhSplitTask任务出错", e);
        }
        return;
      }

      for (PdwhPubKeywords20180511 pdwhPubKeywords : list) {
        try {
          pdwhPublicationKeywordService.dealPdwhPubKeywords(pdwhPubKeywords);
        } catch (Exception e) {
          logger.error("基准库成果关键词 拆分异常，pubid:" + pdwhPubKeywords.getPubId(), e);
        }
      }
    }
  }

}
