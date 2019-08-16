package com.smate.center.task.quartz.pdwh;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.pdwh.quartz.TemTaskPdwhBrief;
import com.smate.center.task.service.pdwh.quartz.RenamePdwhFulltextFileService;
import com.smate.center.task.service.pdwh.quartz.UpdatePdwhPubBrieService;
import com.smate.center.task.sys.quartz.service.QuartzCronExpressionService;

/**
 * 更新Brief字段
 * 
 * @author LTL
 *
 */
public class UpdatePdwhPubBriefTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 200; // 每次处理的个数
  private String triggerName = "";

  @Autowired
  private UpdatePdwhPubBrieService updatePdwhPubBrieService;
  @Autowired
  private QuartzCronExpressionService quartzCronExpressionService;

  public UpdatePdwhPubBriefTask() {
    super();
  }

  public UpdatePdwhPubBriefTask(String beanName) {
    super(beanName);
  }

  @Autowired
  RenamePdwhFulltextFileService renamePdwhFulltextFileService;

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========UpdateSnsPubBriefTask已关闭==========");
      return;
    }
    try {
      while (true) {
        List<TemTaskPdwhBrief> pubList = updatePdwhPubBrieService.getUpdatePubIdList(SIZE);
        if (CollectionUtils.isEmpty(pubList)) {
          quartzCronExpressionService.toStopScheduleJob(triggerName);
          return;
        }
        updatePdwhPubBrieService.updatePdwhPubBrie(pubList);
      }
    } catch (Exception e) {
      logger.error("更新基准库成果的brief_desc字段线程出错", e);
    }
  }

  public String getTriggerName() {
    return triggerName;
  }

  public void setTriggerName(String triggerName) {
    this.triggerName = triggerName;
  }

}
