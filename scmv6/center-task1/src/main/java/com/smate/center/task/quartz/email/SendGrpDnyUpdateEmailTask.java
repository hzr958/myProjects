package com.smate.center.task.quartz.email;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.service.email.GroupEmailService;

public class SendGrpDnyUpdateEmailTask extends TaskAbstract {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private GroupEmailService groupEmailService;

  public SendGrpDnyUpdateEmailTask() {}

  public SendGrpDnyUpdateEmailTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========SendPubAssignEmailTask已关闭==========");
      return;
    }
    try {
      // 获取拥有兴趣群组的psnId
      List<Long> psnIdList = groupEmailService.getInstGrpPsnId();
      if (CollectionUtils.isNotEmpty(psnIdList)) {
        for (Long psnId : psnIdList) {
          List<Long> grpIds = groupEmailService.getPsnInstGrpIds(psnId);
          // 获取前三个有群组动态更新的群组
          List<Long> sendMailGrpId = groupEmailService.getNeedSendMailGrpId(grpIds);
          if (!CollectionUtils.isEmpty(sendMailGrpId)) {
            groupEmailService.sendGrpDnyUpdateEmail(sendMailGrpId, psnId);
          }
        }
      }
    } catch (Exception e) {
      logger.error("=========SendPubAssignEmailTask出错==========", e);
    }
  }
}
