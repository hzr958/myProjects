package com.smate.center.task.quartz;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.service.sns.quartz.AnalysisKeyWordsService;
import com.smate.center.task.service.sns.quartz.PsnKwEptSevice;

/**
 * 人员关键词任务
 * 
 * @author zjh
 *
 */
public class PsnKwEptTask extends TaskAbstract {

  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnKwEptSevice psnKwEptSevice;
  @Autowired
  private AnalysisKeyWordsService analysisKeyWordsService;

  public PsnKwEptTask() {
    super();
  }

  public PsnKwEptTask(String beanName) {
    super(beanName);
  }

  public void run() {
    try {
      logger.info("执行人员关键词任务");
      if (!super.isAllowExecution()) {
        return;
      }
      if (psnKwEptSevice.getKeyWordsCommendFlag() == 1) {
        Long StartId = 0L;
        while (true) {
          List<Long> psnKwEptRefreshList = psnKwEptSevice.getRefreshPsn(StartId);
          if (CollectionUtils.isEmpty(psnKwEptRefreshList)) {
            return;
          }
          for (Long psnId : psnKwEptRefreshList) {
            try {
              StartId = psnId;
              analysisKeyWordsService.analyzeKeyWords(psnId);
            } catch (Exception e) {
              logger.error("解析关键词失败", e);
            }
          }
        }

      }
    } catch (Exception e) {
      logger.error("PsnKwEptTask 人员关键词任务处理出错", e);
    }

  }

}
