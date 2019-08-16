
package com.smate.center.task.quartz.email;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.sns.psn.SendmailPsnLog;
import com.smate.center.task.service.sns.pub.PubAssignLogService;

public class SendPubAssignEmailTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 2000; // 每次处理的个数
  @Autowired
  private PubAssignLogService pubAssignLogService;

  public SendPubAssignEmailTask() {
    super();
  }

  public SendPubAssignEmailTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========SendPubAssignEmailTask已关闭==========");
      return;
    }
    try {
      // 先取从未发过邮件的数据
      List<SendmailPsnLog> psnList = pubAssignLogService.getNeedSendMailPsnIds(SIZE);
      if (CollectionUtils.isNotEmpty(psnList)) {
        for (SendmailPsnLog sendmailPsnLog : psnList) {
          Integer result = pubAssignLogService.sendConfirmEmailToPsn(sendmailPsnLog);
          pubAssignLogService.UpdateMailSendStatus(sendmailPsnLog.getId(), result);
          /*
           * // 成果推荐微信消息推送 Long openId = pubAssignLogService.getUserOpenId(sendmailPsnLog.getPsnId(),
           * "00000000"); if (openId != null) { if (pubAssignLogService.getDataByOpenId(openId)) {
           * pubAssignLogService.saveWeChatMessagePsn(sendmailPsnLog.getPsnId()); } }
           */
        }
      } else {
        closeOneTimeTask();
      }
    } catch (Exception e) {
      logger.error("=========SendPubAssignEmailTask出错==========", e);
    }

  }
}
