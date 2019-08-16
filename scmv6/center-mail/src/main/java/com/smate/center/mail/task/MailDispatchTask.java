package com.smate.center.mail.task;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.mail.connector.mailenum.MailSendStatusEnum;
import com.smate.center.mail.exception.ExistsBlacklistException;
import com.smate.center.mail.exception.IncludeSensitiveWordsException;
import com.smate.center.mail.exception.InfoLockedException;
import com.smate.center.mail.exception.LowPriorException;
import com.smate.center.mail.exception.NotExistsClientException;
import com.smate.center.mail.exception.NotExistsSenderException;
import com.smate.center.mail.exception.NotExistsWhitelistException;
import com.smate.center.mail.exception.NotTemplateException;
import com.smate.center.mail.model.MailDispatchInfo;
import com.smate.center.mail.model.MailRecord;
import com.smate.center.mail.service.MailContentService;
import com.smate.center.mail.service.MailRecordService;
import com.smate.center.mail.service.dispatch.MailDispatchService;

/**
 * 邮件调度中心任务.主要是分派邮件给节点以及发送账号
 * 
 * @author tsz
 *
 */
public class MailDispatchTask {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  private static final int MAIL_DISPATCH_SIZE = 100;

  @Autowired
  private MailRecordService mailRecordService;
  @Autowired
  private List<MailDispatchService> dispatchProcess;
  @Autowired
  private MailContentService mailContentService;

  public void execute() {
    try {
      List<MailRecord> list = mailRecordService.getToBeAllocatedMail(MAIL_DISPATCH_SIZE);
      if (CollectionUtils.isEmpty(list)) {
        return;
      }
      for (MailRecord mailRecord : list) {
        try {
          MailDispatchInfo mailDispatchInfo = new MailDispatchInfo(mailRecord);
          for (MailDispatchService mailDispatchService : dispatchProcess) {
            mailDispatchService.excute(mailDispatchInfo);
          }
        } catch (NotExistsClientException e) {
          // 如果是没有发送账号 以及邮件客户端 错误 就return
          return;
        } catch (NotExistsSenderException e) {
          // 果是没有发送账号 以及邮件客户端 错误 就return
          return;
        } catch (LowPriorException e) {
          // 邮件优先级较低
          mailRecordService.updateMailRecordStatus(mailRecord.getMailId(), MailSendStatusEnum.STATUS_0, e.getMessage());
          continue;
        } catch (InfoLockedException e) {
          // 关键信息被锁定
          mailRecordService.updateMailRecordStatus(mailRecord.getMailId(), MailSendStatusEnum.STATUS_7, e.getMessage());
          continue;
        } catch (ExistsBlacklistException e) {
          // 黑名单 更新记录
          mailRecordService.updateMailRecordStatus(mailRecord.getMailId(), MailSendStatusEnum.STATUS_3, e.getMessage());
          continue;
        } catch (NotExistsWhitelistException e) {
          // 不在白名单
          mailRecordService.updateMailRecordStatus(mailRecord.getMailId(), MailSendStatusEnum.STATUS_5, e.getMessage());
          continue;
        } catch (IncludeSensitiveWordsException e) {
          // 邮件内容包含敏感词
          mailRecordService.updateMailRecordStatus(mailRecord.getMailId(), MailSendStatusEnum.STATUS_12,
              e.getMessage());
          continue;
        } catch (NotTemplateException e) {
          // 邮件模板不可用
          mailRecordService.updateMailRecordStatus(mailRecord.getMailId(), MailSendStatusEnum.STATUS_13,
              e.getMessage());
          continue;
        } catch (Exception e) {
          logger.error("邮件调度出错!" + mailRecord.toString(), e);
          mailRecordService.updateMailRecordStatus(mailRecord.getMailId(), MailSendStatusEnum.STATUS_9, e.getMessage());
          continue;
        }
      }
    } catch (Exception e) {
      logger.error("邮件调度中心服务-调度可发送邮件出错!", e);
    }
  }
}
