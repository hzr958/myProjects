package com.smate.center.batch.service.mail;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.core.base.utils.constant.EmailConstants;

/**
 * 
 * 退订判断
 * 
 * @author zk
 * 
 */
@Service("emailUnsubCheckHandler")
@Transactional(rollbackFor = Exception.class)
public class EmailUnsubCheckHandler implements EmailHandlerService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Resource(name = "emailUnsubscribeUrlHandler")
  private EmailHandlerService emailHandler;

  @Autowired
  private MailFilterService mailFilterService;

  /**
   * params 类型＼邮件数据＼来源节点
   */
  @SuppressWarnings("rawtypes")
  @Override
  public String handler(Object... params) throws ServiceException {

    Map mailMap = (Map) params[1];

    // 收件人不存在，
    if (mailMap.get(EmailConstants.EMAIL_RECEIVE_PSNID_KEY) != null) {

      // 收件人id
      Long psnId = Long.valueOf(ObjectUtils.toString(mailMap.get(EmailConstants.EMAIL_RECEIVE_PSNID_KEY)));
      // 模板
      String template = String.valueOf(mailMap.get(EmailConstants.EMAIL_TEMPLATE_KEY));
      // 获取是否已经退订
      Boolean sendJurisdiction = mailFilterService.sendMailJurisdiction(psnId, template);

      if (sendJurisdiction == false) {
        String errorMag = template + "邮件模板已被该用户退订";
        return errorMag; // 不接收 直接返回
      } else {
        return emailHandler.handler(params);
      }
    } else {
      // 收件人不存在，则直接发送邮件
      return emailHandler.handler(params);
    }
  }
}
