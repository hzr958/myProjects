package com.smate.center.task.quartz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.smate.center.task.base.AppSettingConstants;
import com.smate.center.task.base.AppSettingContext;
import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.sns.psn.PsnKnowWorkEdu;
import com.smate.center.task.service.sns.psn.KnowPsnWorkEduService;
import com.smate.center.task.service.sns.psn.RecommendService;
import com.smate.center.task.single.service.person.PersonManager;

public class KnowPsnWorkEduTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private int batchSize = 90;
  @Autowired
  private RecommendService recommendService;
  @Autowired
  private KnowPsnWorkEduService knowPsnWorkEduService;
  @Autowired
  private PersonManager personManager;

  public KnowPsnWorkEduTask() {
    super();
  }

  public KnowPsnWorkEduTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!isAllowExecution()) {
      logger.info("=========KnowPsnWorkEduTask 已关闭==========");
      return;
    }
    logger.info("=========KnowPsnWorkEduTask 已开启==========");
    if (!isEnabled())
      return;
    Long lastPsnId = 0L;
    try {
      recommendService.upAppSettingConstant(AppSettingConstants.SNS_PSN_WORK_EDU, 0);
      while (true) {
        List<Long> psnIds = personManager.findPersonKnowByPsnId(lastPsnId, batchSize);
        if (CollectionUtils.isEmpty(psnIds)) {
          recommendService.upAppSettingConstant(AppSettingConstants.SNS_PSN_WORK_EDU, 1);
          return;
        }
        lastPsnId = psnIds.get(psnIds.size() - 1);
        List<PsnKnowWorkEdu> psnWorkEduList = personManager.findKnowPsn(psnIds);
        knowPsnWorkEduService.matchKnowPsnWorkEdu(psnWorkEduList);
      }
    } catch (Exception e) {
      logger.error("Know匹配人员工作教育经历出错", e);
    }

  }

  private boolean isEnabled() {
    return AppSettingContext.getIntValue(AppSettingConstants.SNS_PSN_WORK_EDU) == 1;
  }
}
