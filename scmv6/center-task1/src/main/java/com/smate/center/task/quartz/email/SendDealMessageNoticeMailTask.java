package com.smate.center.task.quartz.email;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.service.sns.psn.SendDealMessageNoticeMailService;
import com.smate.center.task.service.sns.quartz.TaskMarkerService;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.model.security.Person;

public class SendDealMessageNoticeMailTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 1000; // 每次刷新获取的个数

  @Autowired
  private SendDealMessageNoticeMailService sendDealMessageNoticeMailService;
  @Autowired
  private TaskMarkerService taskMarkerService;
  @Autowired
  private CacheService cacheService;
  public static String SEND_DEAL_MESSAGE_NOTICE_MAIL = "send_deal_message_notice_mail";

  public SendDealMessageNoticeMailTask() {
    super();
  }

  public SendDealMessageNoticeMailTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {

    if (!super.isAllowExecution()) {
      return;
    }
    if (taskMarkerService.getApplicationQuartzSettingValue("SendDealMessageNoticeMailTask_PsnId") == 1) {
      cacheService.remove(SEND_DEAL_MESSAGE_NOTICE_MAIL, "notice_mail_psn_id");
    }
    Long psnCount = sendDealMessageNoticeMailService.getPsnCount();
    int num = (int) (psnCount / SIZE) + 1;
    int i;
    for (i = 0; i < num; i++) {
      Long lastId = (Long) cacheService.get(SEND_DEAL_MESSAGE_NOTICE_MAIL, "notice_mail_psn_id");
      if (lastId == null) {
        lastId = 0L;
      }
      List<Person> personList = sendDealMessageNoticeMailService.getpsnIds(SIZE, lastId);
      try {
        if (CollectionUtils.isEmpty(personList)) {
          return;
        }
        for (Person person : personList) {
          if (person.getEmail() != null) {
            sendDealMessageNoticeMailService.sendNoticeMail(person);
          }
        }
        this.cacheService.put(SEND_DEAL_MESSAGE_NOTICE_MAIL, 60 * 60 * 24, "notice_mail_psn_id",
            personList.get(personList.size() - 1).getPersonId());
      } catch (Exception e) {
        logger.error("SendDealMessageNoticeMailTask处理出错", e);
      }
    }
  }
}
