package com.smate.sie.web.application.service.email;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;

/**
 * 发送邮件工具接口
 */
@SuppressWarnings("serial")
@Service("sieSendEmailBuildService")
@Transactional(rollbackFor = Exception.class)
public class SieSendEmailBuildServicempl implements SieSendEmailBuildService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Value(value = "${sendEmail.restful.url}")
  private String sendRestfulUrl;

  /**
   * @param emailParam 邮件参数
   * @param mailData 邮件内容参数
   */
  @SuppressWarnings("unchecked")
  public Map<String, Object> sendEmail(Map<String, Object> emailParam, Map<String, Object> mailData) {
    Map<String, Object> rerult = new LinkedHashMap<String, Object>();
    // 获取原始类型
    Object tempTitle = emailParam.get(EMAIL_MSG);
    Object tempTemplateCode = emailParam.get(EMAIL_TEMPLATE_CODE);
    Object tempReceive = emailParam.get(EMAIL_RECEIVE);
    Object tempReceiverPsnId = emailParam.get(EMAIL_RECEIVER_PSNID);
    Object tempSenderPsnId = emailParam.get(EMAIL_SENDER_PSNID);
    // 格式化数据类型
    String msg = tempTitle == null ? "" : tempTitle.toString();
    Integer templateCode = tempTemplateCode == null ? 0 : (int) tempTemplateCode;
    String receiveEmail = tempReceive == null ? "" : tempReceive.toString();
    Long receiverPsnId = tempReceiverPsnId == null ? 0L : (long) tempReceiverPsnId;
    Long senderPsnId = tempSenderPsnId == null ? 0L : (long) tempSenderPsnId;
    try {
      // 构建邮件json格式参数
      // 定义接口接收的参数
      Map<String, String> paramData = new HashMap<String, String>();
      // 构造paramData
      this.buildNecessaryParam(senderPsnId, templateCode, msg, receiveEmail, receiverPsnId, paramData);
      // 设置公有变量
      mailData.put("sie_ins_domain", Struts2Utils.getRequestDomin());
      paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
      logger.error("邮件调度请求数据:" + JacksonUtils.mapToJsonStr(paramData));
      Map<String, String> resultMap =
          (Map<String, String>) restTemplate.postForObject(this.sendRestfulUrl, paramData, Object.class);
      logger.error("邮件调度返回结果:" + JacksonUtils.mapToJsonStr(resultMap));
      rerult.put("sucess", receiveEmail + "send sucess");
    } catch (Exception e) {
      logger.error(msg + "发送时失败", e.getMessage());
      rerult.put("error", receiveEmail + "send error");
    }
    return rerult;
  }

  /**
   * @param senderPsnId 发送人id,0=非科研之友用户
   * @param priorLevel 优先级
   * @param templateCode 邮件模版编码
   * @param msg 描述
   * @param receiver 接收邮箱
   * @param receiverPsnId 接收人id,0=非科研之友用户
   */
  private void buildNecessaryParam(Long senderPsnId, Integer templateCode, String msg, String receiver,
      Long receiverPsnId, Map<String, String> paramData) {
    Map<String, Object> mailOriginalData = new HashMap<String, Object>();
    mailOriginalData.put("senderPsnId", senderPsnId);
    mailOriginalData.put("mailTemplateCode", templateCode);
    mailOriginalData.put("msg", msg);
    mailOriginalData.put("receiver", receiver);
    mailOriginalData.put("receiverPsnId", receiverPsnId);
    paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializer(mailOriginalData));
  }
}
