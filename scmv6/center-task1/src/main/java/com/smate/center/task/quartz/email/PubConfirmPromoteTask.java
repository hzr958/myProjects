package com.smate.center.task.quartz.email;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.email.PubConfirmPromotePsn;
import com.smate.center.task.service.email.EmailSendTaskDBService;
import com.smate.center.task.service.email.PubConfirmPromoteService;
import com.smate.center.task.single.constants.EtemplateConstant;

/**
 * 成果认领推广邮件
 * 
 * 
 * @author zk
 **/
@Deprecated
public class PubConfirmPromoteTask extends TaskAbstract {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  PubConfirmPromoteService pubConfirmPromoteService;
  @Autowired
  EmailSendTaskDBService emailSendTaskDbService;

  public PubConfirmPromoteTask() {

  }

  public PubConfirmPromoteTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========PubConfirmPromoteTask 已关闭==========");
      return;
    }
    logger.info("=========PubConfirmPromoteTask 已开启==========");
    if (emailSendTaskDbService.canExecuteEmailTask(EtemplateConstant.PUB_CONFIRM_CODE)) {
      Integer size = 0;
      try {
        List<PubConfirmPromotePsn> pcppList = pubConfirmPromoteService.getPubConfirmPromotePsn(size);
        if (CollectionUtils.isNotEmpty(pcppList)) {
          for (PubConfirmPromotePsn pcpp : pcppList) {
            pubConfirmPromoteService.dealStatus(pcpp);
          }
          size += 1;
        }
      } catch (Exception e) {
        logger.error("成果推广邮件任务", e);
      }
    }
  }
}
