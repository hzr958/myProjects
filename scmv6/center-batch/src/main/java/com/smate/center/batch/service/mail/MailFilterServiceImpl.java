package com.smate.center.batch.service.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.exception.pub.ServiceException;


/**
 * 
 * @author tsz 判断收件人是否接受此类邮件
 */
@Service("mailFilterService")
@Transactional(rollbackFor = Exception.class)
public class MailFilterServiceImpl implements MailFilterService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private ConstMailTypeTemplateRelService constMailTypeTemplateRelService;
  @Autowired
  private MailUnsubscribeService mailUnsubscribeService;

  // 根据模板name判断
  @Override
  public Boolean sendMailJurisdiction(Long receivePsnId, String templateName) throws ServiceException {

    // 根据模板得到模板id

    try {
      Integer tempId = mailUnsubscribeService.findTemplateCodeByName(templateName);
      if (tempId == 0) {
        return true;
      }
      Long typeId = constMailTypeTemplateRelService.getTypeidFromTemplateid(tempId);
      Long isreceive = constMailTypeTemplateRelService.getIsreceive(receivePsnId, typeId);
      if (isreceive == 0) {
        return false; // 不接收
      }
      return true;
    } catch (Exception e) {
      logger.error("邮件拦截出错！", e);
      throw new ServiceException(e);
    }
  }
}
