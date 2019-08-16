package com.smate.center.mail.service.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.center.mail.connector.mongodb.model.MailContent;
import com.smate.center.mail.exception.IncludeSensitiveWordsException;
import com.smate.center.mail.model.MailDispatchInfo;

/**
 * 检查敏感词 情况服务
 * 
 * @author yhx
 *
 */
public class MailDispatchCheckSensitiveWordsServiceImpl implements MailDispatchService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void excute(MailDispatchInfo mailDispatchInfo) throws Exception {
    MailContent mailContent = mailDispatchInfo.getMailContent();
    if (mailContent != null) {
      Pattern pattern = Pattern.compile("招聘|代理|驻册|领取|提现|澳门|微信|QQ|提款");
      Matcher matcher = pattern.matcher(mailContent.getContent());
      if (matcher.find()) {
        logger.error("邮件内容包含敏感词!!");
        throw new IncludeSensitiveWordsException("邮件内容包含敏感词!!");
      }
    }
  }

}
