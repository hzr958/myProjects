package com.smate.center.task.quartz.email;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.snsbak.NewYearGreetingEmail;
import com.smate.center.task.service.email.PromoteMailInitDataService;
import com.smate.center.task.single.service.person.PersonManager;
import com.smate.core.base.utils.cache.CacheService;

public class SendNewYearMailTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final static Integer SIZE = 2000; // 每次刷新获取的个数
  @Autowired
  private CacheService cacheService;
  public static String PSN_ID_CACHE = "New_Year_Mail_psn_id";
  @Autowired
  private PersonManager personManager;
  @Autowired
  private PromoteMailInitDataService promoteMailInitDataService;

  public SendNewYearMailTask() {
    super();
  }

  public SendNewYearMailTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========SendNewYearMailTask已关闭==========");
      return;
    }
    try {
      List<NewYearGreetingEmail> psnList = personManager.getToHandleEmail(SIZE);
      List<String> emailStr = new ArrayList<String>();
      if (CollectionUtils.isEmpty(psnList)) {
        logger.info("发给用户新年邮件任务完毕===");
      }
      for (NewYearGreetingEmail email : psnList) {
        try {
          Map<String, Object> params = new HashMap<String, Object>();
          params.put("email_receiveEmail", email.getEmail());
          params.put("email_subject", "科研之友致用户的一封信");
          params.put("email_Template", "2018New_year_greetings.ftl");
          promoteMailInitDataService.saveMailInitData(params);
          this.personManager.saveNewYearEmailStatus(email, 1);
        } catch (Exception e) {
          this.personManager.saveNewYearEmailStatus(email, 3);
          logger.error("发给用户新年邮件任务出错, 邮箱： " + email.getEmail(), e);
        }
      }
    } catch (Exception e) {
      logger.error("发给用户新年邮件任务出错========= ", e);
    }
  }

}
