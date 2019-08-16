package com.smate.center.task.quartz.email;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.service.sns.psn.InviteEndorseResearchAreaService;

public class InviteEndorseResearchAreaTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private static final int SIZE = 100;
  @Autowired
  private InviteEndorseResearchAreaService inviteEndorseResearchAreaService;

  public InviteEndorseResearchAreaTask() {
    super();
  }

  public InviteEndorseResearchAreaTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========InviteEndorseResearchAreaTask已关闭==========");
      return;
    }
    try {
      List<Long> psnIds = inviteEndorseResearchAreaService.getUpdateRAPsnId(SIZE);
      if (CollectionUtils.isNotEmpty(psnIds)) {
        for (Long psnId : psnIds) {
          inviteEndorseResearchAreaService.sendEmail(psnId);
        }
      }
    } catch (Exception e) {
      logger.error("邀请认同研究领域邮件.", e);
    }

  }
}
