package com.smate.center.batch.service.emailsimplify;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.pub.mq.SyncMailToMailSrvMessage;
import com.smate.center.batch.service.pub.mq.SyncMailToMailSrvProducer;
import com.smate.core.base.utils.constant.EmailConstants;
import com.smate.core.base.utils.json.JacksonUtils;


public abstract class AbstractEmailSend implements EmailSimplify {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Resource(name = "syncMailToMailSrvProducer")
  private SyncMailToMailSrvProducer syncMailToMailSrvProducer;

  @Override
  public void syncEmailInfo(Object... params) {
    SyncMailToMailSrvMessage syncMessage = null;
    try {
      Map<String, Object> mailMap = invoke(params);
      if (MapUtils.isNotEmpty(mailMap)) {
        syncMessage = new SyncMailToMailSrvMessage();
        // 发送来源
        // syncMessage.setFromNodeId(SecurityUtils.getCurrentAllNodeId().get(0));
        // 邮件类型
        if (mailMap.get(EmailConstants.EMAIL_TYPE_KEY) != null) {
          syncMessage.setMailType(Integer.valueOf(mailMap.get(EmailConstants.EMAIL_TYPE_KEY).toString()));
        } else {
          syncMessage.setMailType(EmailConstants.COMMON_EMAIL);
        }
        // 邮件标识，成功或失败
        syncMessage.setMailFlag(EmailConstants.SUCCESS);
        // 邮件数据
        syncMessage.setMailJson(JacksonUtils.jsonObjectSerializer(mailMap));
        syncMailToMailSrvProducer.syncMessage(syncMessage);
      }
    } catch (Exception e) {
      try {
        logger.error("整理／同步邮件数据到邮件服务时出错", e);
        syncMessage = new SyncMailToMailSrvMessage();
        // syncMessage.setFromNodeId(ServiceConstants.SCHOLAR_NODE_ID_1);
        syncMessage.setMailFlag(EmailConstants.FAILURE);
        syncMessage.setMailJson(JacksonUtils.jsonObjectSerializer(params));
        syncMailToMailSrvProducer.syncMessage(syncMessage);
      } catch (Exception e1) {
        logger.error("整理／同步邮件数据到邮件服务出错时，同步记录到邮件服务出错", e1);
      }
    }
  }

  public abstract Map<String, Object> invoke(Object... params) throws ServiceException;

}
