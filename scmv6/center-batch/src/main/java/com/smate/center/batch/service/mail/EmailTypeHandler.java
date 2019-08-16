package com.smate.center.batch.service.mail;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.core.base.utils.constant.EmailConstants;


/**
 * 
 * 邮件类型选择
 * 
 * @author zk
 * 
 */
@Service("emailTypeHandler")
@Transactional(rollbackFor = Exception.class)
public class EmailTypeHandler implements EmailHandlerService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Resource(name = "emailSaveHandler")
  private EmailHandlerService emailHandler;

  /**
   * params 类型＼邮件数据＼来源节点
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public String handler(Object... params) throws ServiceException {

    Integer mailType = (Integer) params[0];
    Map mailMap = (Map) params[1];
    Integer nodeId = (Integer) params[2];
    // 普通邮件
    if (EmailConstants.COMMON_EMAIL.equals(mailType)) {
      mailMap.put(EmailConstants.EMAIL_STATUS_KEY, EmailConstants.runStatus);
      return emailHandler.handler(mailType, mailMap, nodeId);
    } else if (EmailConstants.PROMOTE_EMAIL.equals(mailType)) {
      // 推广邮件
      mailMap.put(EmailConstants.EMAIL_STATUS_KEY, EmailConstants.PROMOTE_EMAIL);
      return emailHandler.handler(mailType, mailMap, nodeId);
    } else if (EmailConstants.PROMOTE_EMAIL_HT.equals(mailType)) {
      // 推广邮件后台任务
      mailMap.put(EmailConstants.EMAIL_STATUS_KEY, EmailConstants.PROMOTE_EMAIL_HT);
      return emailHandler.handler(mailType, mailMap, nodeId);
    } else {
      return null;
    }

  }
}
