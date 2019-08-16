package com.smate.center.batch.service.mail;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.core.base.utils.constant.EmailConstants;

/**
 * 特定邮件每日限制处理
 * 
 * @author zk
 *
 */
@Service("emailDailyEmailLimitHandler")
public class EmailDailyEmailLimitHandler implements EmailHandlerService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private DailyEmailLimitService dailyEmailLimitService;
  @Resource(name = "emailUnsubCheckHandler")
  private EmailHandlerService emailHandler;

  @SuppressWarnings("rawtypes")
  @Override
  public String handler(Object... params) throws ServiceException {

    try {
      List<String> tempNameList = dailyEmailLimitService.getDailyEmailLimitTempName();
      if (CollectionUtils.isNotEmpty(tempNameList)) {
        Map mailMap = (Map) params[1];
        String template = mailMap.get(EmailConstants.EMAIL_TEMPLATE_KEY).toString();
        template = template.replace("_zh_CN.ftl", "");
        template = template.replace("_en_US.ftl", "");
        template = template.replace(".ftl", "");
        if (tempNameList.contains(template)) {
          String email = mailMap.get(EmailConstants.EMAIL_RECEIVEEMAIL_KEY).toString();
          if (!dailyEmailLimitService.isSendToday(email, template)) {
            dailyEmailLimitService.saveSendRecored(email, template);
            logger.error("邮件信息" + email + "是否发送过" + dailyEmailLimitService.isSendToday(email, template));
          } else {
            logger.error("邮件信息" + email + "是否发送过" + dailyEmailLimitService.isSendToday(email, template));
            return null;
          }
        }
      }
    } catch (Exception e) {
      logger.error("特定邮件每日限制处理出错," + e);
    }
    return emailHandler.handler(params);
  }

}
