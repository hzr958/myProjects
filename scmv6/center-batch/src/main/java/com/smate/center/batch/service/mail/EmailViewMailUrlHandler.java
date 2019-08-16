package com.smate.center.batch.service.mail;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.mail.emailsrv.MailLogDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.core.base.utils.constant.EmailConstants;


/**
 * 
 * 生成邮件预览链接
 * 
 * @author zk
 * 
 */
@Service("emailViewMailUrlHandler")
@Transactional(rollbackFor = Exception.class)
public class EmailViewMailUrlHandler implements EmailHandlerService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private MailLogDao mailLogDao;

  @Resource(name = "emailTypeHandler")
  private EmailHandlerService emailHandler;

  @Autowired
  private EtemplateDealUrlMethod etemplateDealUrlMethod;

  /**
   * params 类型＼邮件数据＼来源节点
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public String handler(Object... params) throws ServiceException {

    Map mailMap = (Map) params[1];

    Long mailId = mailLogDao.getMailId();
    mailMap.put(EmailConstants.EMAIL_MAILID_KEY, mailId);
    String viewUrl = etemplateDealUrlMethod.getMailViewUrl(mailId);
    if (StringUtils.isNotBlank(viewUrl)) {
      mailMap.put(EmailConstants.EMAIL_VIEWURL_KEY, viewUrl);
    }
    return emailHandler.handler(params[0], mailMap, params[2]);
  }
}
