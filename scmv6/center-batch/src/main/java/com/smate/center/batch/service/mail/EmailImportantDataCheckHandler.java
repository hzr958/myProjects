package com.smate.center.batch.service.mail;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.core.base.utils.constant.EmailConstants;

/**
 * 
 * 重要数据检查
 * 
 * EmailConstants.EMAIL_SENDEMAIL_KEY
 * 
 * EmailConstants.EMAIL_SUBJECT_KEY
 * 
 * EmailConstants.EMAIL_TEMPLATE_KEY
 * 
 * @author zk
 * 
 */
@Service("emailImportantDataCheckHandler")
@Transactional(rollbackFor = Exception.class)
public class EmailImportantDataCheckHandler implements EmailHandlerService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Resource(name = "emailDailyEmailLimitHandler")
  private EmailHandlerService emailHandler;

  /**
   * params 类型＼邮件数据＼来源节点
   */
  @SuppressWarnings("rawtypes")
  @Override
  public String handler(Object... params) throws ServiceException {

    String errorMsg = null;
    Map mailMap = (Map) params[1];

    Object receiveEmail = mailMap.get(EmailConstants.EMAIL_RECEIVEEMAIL_KEY);
    if (mactchEmail(ObjectUtils.toString(receiveEmail))) {
      errorMsg = "收件箱为空或不符合邮箱规则:" + receiveEmail;
      return errorMsg;
    }

    Object subject = mailMap.get(EmailConstants.EMAIL_SUBJECT_KEY);
    if (StringUtils.isBlank(ObjectUtils.toString(subject))) {
      errorMsg = "邮件主题为空";
      return errorMsg;
    }

    Object template = mailMap.get(EmailConstants.EMAIL_TEMPLATE_KEY);
    if (StringUtils.isBlank(ObjectUtils.toString(template))) {
      errorMsg = "邮件模板为空";
      return errorMsg;
    }

    return emailHandler.handler(params);
  }

  /**
   * 
   * 检查邮箱是否符合条件
   * 
   * @param email
   * @return
   * @throws ServiceException
   */
  private Boolean mactchEmail(String email) throws ServiceException {

    if (StringUtils.isBlank(email)) {
      return true;
    }
    String check =
        "^([a-z0-9A-Z])+([a-z0-9A-Z_\\-]*[\\.]?[a-z0-9A-Z_\\-]+)*@([a-z0-9A-Z]+(-[a-z0-9A-Z-]+)?\\.)+[a-zA-Z]{2,}$";
    Pattern regex = Pattern.compile(check);
    Matcher matcher = regex.matcher(email);
    return matcher.matches() == true ? false : true;
  }

}
