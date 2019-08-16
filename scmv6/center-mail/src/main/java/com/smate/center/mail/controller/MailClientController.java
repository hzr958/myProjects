package com.smate.center.mail.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smate.center.mail.cache.MailCacheService;
import com.smate.center.mail.connector.mailenum.MailSendStatusEnum;
import com.smate.center.mail.service.MailRecordService;

/**
 * 邮件客户端专用控制器 主要有 更新缓存信息 更新邮件发送状态
 * 
 * @author tsz
 *
 */
@Controller
@RequestMapping(value = "/client")
public class MailClientController {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private MailCacheService mailCacheService;
  @Autowired
  private MailRecordService mailRecordService;

  /**
   * 更新客户端缓存信息
   */
  @RequestMapping(value = "updateclient", method = RequestMethod.POST)
  @ResponseBody
  public void updateClientCache(@RequestBody String jsonData) {
    try {
      assert StringUtils.isNoneBlank(jsonData);
      logger.info("更新客户端缓存信息" + jsonData.toString());
      mailCacheService.updateMailClientInfo(jsonData.toString());
    } catch (Exception e) {
      logger.error("更新客户端信息出错!" + jsonData, e);
    }
  }

  /**
   * 
   */
  @SuppressWarnings({})
  @RequestMapping(value = "updatestatus", method = RequestMethod.POST)
  @ResponseBody
  public void updateMailStatus(@RequestBody Map<String, Object> data) {
    try {
      assert data != null;
      logger.info("更新邮件发送状态" + data.toString());
      Long mailId = Long.parseLong(data.get("mailId").toString());
      String status = data.get("status").toString();
      assert status != null;
      String msg = data.get("msg") == null ? "" : data.get("msg").toString();
      assert MailSendStatusEnum.valueOf(status) != null;
      mailRecordService.updateMailRecordStatus(mailId, MailSendStatusEnum.valueOf(status), msg);
    } catch (NumberFormatException e) {
      logger.error("邮件服务,更新邮件发送状态出错" + data.toString(), e);
    }
  }

  /**
   * 获取 待发送数据
   */
  @RequestMapping(value = "gettobesend", method = RequestMethod.POST)
  @ResponseBody
  public List<String> getToBeSend(@RequestBody String client) {
    assert StringUtils.isNoneBlank(client);
    List<String> list = new ArrayList<String>();
    try {
      list = mailRecordService.getToBeSendMail(50, client);
    } catch (Exception e) {
      logger.error("获取待发送邮件出错" + client, e);
    }
    return list;
  }
}
