package com.smate.center.mail.connector.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.mail.connector.dao.MailContentDao;
import com.smate.center.mail.connector.dao.MailOriginalDataDao;
import com.smate.center.mail.connector.dao.MailTemplateDao;
import com.smate.center.mail.connector.exception.MailHandleDataException;
import com.smate.center.mail.connector.mailenum.MailHandleEnum;
import com.smate.center.mail.connector.model.MailOriginalData;
import com.smate.center.mail.connector.model.MailOriginalDataInfo;
import com.smate.center.mail.connector.model.MailTemplate;
import com.smate.center.mail.connector.mongodb.model.MailContent;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 邮件原始数据处理服务
 * 
 * @author zzx
 *
 */
@Service("mailHandleOriginalDataService")
@Transactional(rollbackFor = Exception.class)
public class MailHandleOriginalDataServiceImpl implements MailHandleOriginalDataService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private MailOriginalDataDao mailOriginalDataDao;
  @Autowired
  private MailContentDao mailContentDao;
  @Autowired
  private MailTemplateDao mailTemplateDao;

  public boolean checkEmailParam(Map<String, String> paramData) throws MailHandleDataException {
    if (paramData == null) {
      paramData = new HashMap<String, String>();
      paramData.put("msg", "构造邮件-参数为空");
      logger.error("构造邮件-参数为空");
      return false;
    }
    String mailOriginalData = paramData.get(MailHandleEnum.MAIL_ORIGINAL_DATA.toString());
    if (StringUtils.isBlank(mailOriginalData)) {
      paramData.put("msg", "构造邮件-原始数据为空");
      logger.error("构造邮件-原始数据为空");
      return false;
    }
    Map<String, Object> originalDataMap = JacksonUtils.jsonToMap(mailOriginalData);
    Object senderPsnId = originalDataMap.get(MailHandleEnum.SENDER_PSN_ID.toString());
    if (senderPsnId == null || StringUtils.isBlank(senderPsnId.toString())) {
      paramData.put("msg", "构造邮件-发送者id为空，系统邮件发送者id请设置为0");
      logger.error("构造邮件-发送者id为空，系统邮件发送者id请设置为0");
      return false;
    }
    Object receiverPsnId = originalDataMap.get(MailHandleEnum.RECEIVER_PSN_ID.toString());
    if (receiverPsnId == null || StringUtils.isBlank(receiverPsnId.toString())) {
      paramData.put("msg", "构造邮件-接收者id为空，邀请非科研之友人员接收者id请设置为0");
      logger.error("构造邮件-接收者id为空，邀请非科研之友人员接收者id请设置为0");
      return false;
    }
    Object receiverEmail = originalDataMap.get(MailHandleEnum.RECEIVER.toString());
    if (receiverEmail == null || StringUtils.isBlank(receiverEmail.toString())) {
      paramData.put("msg", "构造邮件-接收邮箱为空");
      logger.error("构造邮件-接收邮箱为空");
      return false;
    }
    Object mailTemplateCode = originalDataMap.get(MailHandleEnum.MAIL_TEMPLATE_CODE.toString());
    if (mailTemplateCode == null || StringUtils.isBlank(mailTemplateCode.toString())) {
      paramData.put("msg", "构造邮件-模版编码为空");
      logger.error("构造邮件-模版编码为空");
      return false;
    } else if (checkTempLimit(paramData, originalDataMap)) {
      return false;
    }
    Object mailData = paramData.get(MailHandleEnum.MAIL_DATA.toString());
    if (mailData == null || StringUtils.isBlank(mailData.toString())) {
      paramData.put("msg", "构造邮件-邮件模版参数为空");
      logger.error("构造邮件-邮件模版参数为空");
      return false;
    }
    return true;
  }

  private boolean checkTempLimit(Map<String, String> paramData, Map<String, Object> originalDataMap) {
    String mailTemplateCodeStr = originalDataMap.get(MailHandleEnum.MAIL_TEMPLATE_CODE.toString()).toString();
    Integer mailTemplateCode = Integer.parseInt(mailTemplateCodeStr);
    MailTemplate template = mailTemplateDao.get(mailTemplateCode);
    if (template == null) {
      paramData.put("msg", "构造邮件-模版不存在");
      return true;
    }
    if (template.getStatus() == 1) {
      paramData.put("msg", "构造邮件-模版不可用");
      return true;
    }

    return false;
  }

  public Map<String, String> doHandle(Map<String, String> paramData) {
    Map<String, String> resultMap = new HashMap<String, String>();
    try {
      // 效验
      if (!checkEmailParam(paramData)) {
        resultMap.put("result", "error");
        resultMap.put("msg", paramData.get("msg"));
      } else {
        // 保存数据
        saveEmailData(paramData, resultMap);
      }
    } catch (Exception e) {
      resultMap.put("result", "error");
      resultMap.put("msg", "保存邮件数据到MailOriginalData出错");
      logger.error("保存邮件数据到MailOriginalData出错", e);
    }
    return resultMap;

  }

  private void saveEmailData(Map<String, String> paramData, Map<String, String> resultMap) throws Exception {
    String mailOriginalData = paramData.get(MailHandleEnum.MAIL_ORIGINAL_DATA.toString());
    Map<String, Object> map = JacksonUtils.jsonToMap(mailOriginalData);
    Date date = new Date();
    MailOriginalData mod = new MailOriginalData();
    Integer templateCode = Integer.parseInt(map.get(MailHandleEnum.MAIL_TEMPLATE_CODE.toString()).toString());
    String priorLevel = mailTemplateDao.findPriorByTempCode(templateCode);
    mod.setSenderPsnId(Long.parseLong(map.get(MailHandleEnum.SENDER_PSN_ID.toString()).toString()));
    mod.setReceiverPsnId(Long.parseLong(map.get(MailHandleEnum.RECEIVER_PSN_ID.toString()).toString()));
    mod.setReceiver(map.get(MailHandleEnum.RECEIVER.toString()).toString());
    mod.setMailTemplateCode(templateCode);
    mod.setPriorLevel(priorLevel);
    mod.setCreateDate(date);
    mod.setUpdateDate(date);
    mod.setMsg(map.get(MailHandleEnum.MSG.toString()).toString());
    mod.setStatus(0);// 0=待构造的邮件
    mailOriginalDataDao.save(mod);

    MailContent mc = new MailContent();
    mc.setMailId(mod.getMailId());
    mc.setMailData(paramData.get(MailHandleEnum.MAIL_DATA.toString()));
    mc.setContent("");
    mailContentDao.save(mc);

    resultMap.put("mailId", mod.getMailId().toString());
    resultMap.put("result", "success");
    resultMap.put("msg", "处理成功");
  }

  public void buildNecessaryParam(String email, Long currentUserId, Long psnId, Integer templateCode, String msg,
      Map<String, String> paramData) {
    MailOriginalDataInfo info = new MailOriginalDataInfo();
    info.setReceiver(email);// 接收邮箱
    info.setSenderPsnId(currentUserId);// 发送人psnId，0=系统邮件
    info.setReceiverPsnId(psnId);// 接收人psnId，0=非科研之友用户
    info.setMailTemplateCode(templateCode);// 模版标识，参考V_MAIL_TEMPLATE
    info.setMsg(msg);// 描述
    paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializerNoNull(info));
  }

}
