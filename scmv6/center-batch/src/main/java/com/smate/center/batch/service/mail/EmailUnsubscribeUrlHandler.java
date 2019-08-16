package com.smate.center.batch.service.mail;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.core.base.utils.constant.EmailConstants;

/**
 * 
 * 退订邮件链接
 * 
 * @author zk
 * 
 */

@Service("emailUnsubscribeUrlHandler")
@Transactional(rollbackFor = Exception.class)
public class EmailUnsubscribeUrlHandler implements EmailHandlerService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Resource(name = "emailViewMailUrlHandler")
  private EmailHandlerService emailHandler;

  @Autowired
  private MailUnsubscribeUrlService mailUnsubscribeUrlService;

  /**
   * params 类型＼邮件数据＼来源节点
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public String handler(Object... params) throws ServiceException {

    Map mailMap = (Map) params[1];

    if (mailMap.get(EmailConstants.EMAIL_RECEIVE_PSNID_KEY) != null) {
      // 能获取收件人id
      Long receivePsnId = Long.valueOf(ObjectUtils.toString(mailMap.get(EmailConstants.EMAIL_RECEIVE_PSNID_KEY)));
      String template = ObjectUtils.toString(mailMap.get(EmailConstants.EMAIL_TEMPLATE_KEY));
      String unsubUrl = getUnsubscribeUrl(receivePsnId, template);
      if (StringUtils.isBlank(unsubUrl)) {
        return null;
      }
      mailMap.put(EmailConstants.EMAIL_UNSUBURL_KEY, getUnsubscribeUrl(receivePsnId, template));
    } else {
      // 收件箱匹配不到收件人id的，改邮件地址没绑定帐号。 tsz
      String template = ObjectUtils.toString(mailMap.get(EmailConstants.EMAIL_TEMPLATE_KEY));
      String mailSetUrl = getUnsubscribeUrl(null, template);
      mailMap.put(EmailConstants.EMAIL_UNSUBURL_KEY, mailSetUrl);
    }
    return emailHandler.handler(params[0], mailMap, params[2]);
  }

  /**
   * 生成退订url
   * 
   * @param psnId
   * @param mailTemplate
   * @return
   * @throws ServiceException
   */

  private String getUnsubscribeUrl(Long psnId, String mailTemplate) throws ServiceException {
    try {
      return mailUnsubscribeUrlService.getUnsubscribeMailUrl(psnId, mailTemplate);
    } catch (Exception e) {
      logger.error("生成邮件退订链接出错", e);
      throw new ServiceException(e);
    }
  }
}
