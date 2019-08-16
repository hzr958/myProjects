package com.smate.center.batch.service.mail;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.smate.center.batch.dao.mail.emailsrv.MailTemplateInfoDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.mail.emailsrv.MailLog;
import com.smate.center.batch.model.mail.emailsrv.MailTemplateInfo;
import com.smate.core.base.utils.constant.EmailConstants;

import freemarker.template.Configuration;

/**
 * 
 * 生成邮件
 * 
 * @author zk
 * 
 */
@Service("emailSaveHandler")
@Transactional(rollbackFor = Exception.class)
public class EmailSaveHandler implements EmailHandlerService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private Configuration freemarkerConfiguration;

  @Autowired
  private MailLogService mailLogService;
  @Autowired
  private MailTemplateInfoDao mailTemplateInfoDao;

  private static Map<String, MailTemplateInfo> tempCodeMap = new HashMap<String, MailTemplateInfo>();

  /**
   * 处理邮件相关
   * 
   * params 类型＼邮件数据＼来源节点
   */
  @SuppressWarnings({"rawtypes"})
  @Override
  public String handler(Object... params) throws ServiceException {
    try {
      Integer mailType = (Integer) params[0];
      Map mailMap = (Map) params[1];
      Integer nodeId = (Integer) params[2];
      generateEmail(mailType, mailMap, nodeId);
      return null;
    } catch (Exception e) {
      String errorMsg = "生成邮件出错!";
      logger.error(errorMsg, e);
      return errorMsg;
    }
  }

  /**
   * 
   * 封装邮件数据
   * 
   * @param mailMap
   * @param nodeId
   * @throws ServiceException
   */
  @SuppressWarnings("rawtypes")
  private void generateEmail(Integer mailType, Map mailMap, Integer nodeId) throws ServiceException {

    String context = generateContext(mailMap);
    if (StringUtils.isBlank(context)) {
      throw new ServiceException("生成邮件数据为空");
    }
    MailLog mailLog = new MailLog();
    // 邮件id
    mailLog.setId(Long.valueOf(ObjectUtils.toString(mailMap.get(EmailConstants.EMAIL_MAILID_KEY))));
    // 收件箱
    mailLog.setToAddr(ObjectUtils.toString(mailMap.get(EmailConstants.EMAIL_RECEIVEEMAIL_KEY)));
    // 主题
    mailLog.setSubject(ObjectUtils.toString(mailMap.get(EmailConstants.EMAIL_SUBJECT_KEY)));
    // 模板
    mailLog.setTemplate(ObjectUtils.toString(mailMap.get(EmailConstants.EMAIL_TEMPLATE_KEY)));
    // 模板编号，对应 maildispatch.MAIL_TEMPLATE_INFO.tempCode
    MailTemplateInfo tempInfo =
        this.getTempInfoByTempName(ObjectUtils.toString(mailMap.get(EmailConstants.EMAIL_TEMPLATE_KEY)));
    if (tempInfo != null) {
      mailLog.setTemplateCode(tempInfo.getTempCode());
      // 优先级
      mailLog.setPriorCode(tempInfo.getPriorCode());
    } else {
      mailLog.setPriorCode(0);
    }
    // 文件名
    mailLog.setFileNames(ObjectUtils.toString(mailMap.get(EmailConstants.EMAIL_FILENAME_KEY)));
    // 附件
    mailLog.setAttachment(ObjectUtils.toString(mailMap.get(EmailConstants.Email_ATTACHNAME_KEY)));
    // 收件人id
    if (mailMap.get(EmailConstants.EMAIL_RECEIVE_PSNID_KEY) != null) {
      mailLog.setPsnId(Long.valueOf(ObjectUtils.toString(mailMap.get(EmailConstants.EMAIL_RECEIVE_PSNID_KEY))));
    }
    // 来源节点
    mailLog.setNode(nodeId);
    // 初始状态
    mailLog.setStatus(Integer.valueOf(ObjectUtils.toString(mailMap.get(EmailConstants.EMAIL_STATUS_KEY))));

    // 内容
    // mailLog.setContext(context);
    mailLog.setCreateDate(new Date());
    mailLogService.saveMail(mailLog, context);

  }

  /**
   * 获取模板编号
   * 
   * @param tempName
   * @return
   * @throws ServiceException
   */
  private MailTemplateInfo getTempInfoByTempName(String tempName) throws ServiceException {
    try {
      MailTemplateInfo tempInfo = tempCodeMap.get(tempName);
      // 不在缓存中，则从数据库查
      if (tempInfo == null) {
        tempInfo = mailTemplateInfoDao.findTemplateByName(tempName);
        // 数据库也没有，则抛出异常
        if (tempInfo == null) {
          logger.error("获取模板编号为空,tempName=" + tempName);
          throw new ServiceException("tempName=" + tempName);
        }
        // 将数据保存在缓存中
        tempCodeMap.put(tempName, tempInfo);
      }
      return tempInfo;
    } catch (Exception e) {
      logger.error("获取模板编号出错", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 生成邮件数据出错
   * 
   * @param params
   * @return
   * @throws ServiceException
   */
  @SuppressWarnings("rawtypes")
  private String generateContext(Map params) throws ServiceException {

    try {
      String content = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate(
          ObjectUtils.toString(params.get(EmailConstants.EMAIL_TEMPLATE_KEY)), EmailConstants.ENCODING), params);
      return content;
    } catch (Exception e) {
      logger.error("生成邮件数据出错", e);
      throw new ServiceException(e);
    }
  }

}
