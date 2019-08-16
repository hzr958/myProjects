package com.smate.center.task.email.restful;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smate.center.task.service.email.MailInitDataService;

/**
 * 初始邮件restful控制器
 * 
 * @author zk
 *
 */
@RestController
public class EmailInitDataController {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private MailInitDataService mailIniDataService;

  @RequestMapping("/storeemailinitdata")

  public int storeEmailInitData(@RequestBody Map<String, Object> mailData) {
    try {
      mailIniDataService.saveMailInitData(mailData);
    } catch (Exception e) {
      logger.error("初始邮件restful保存邮件数据时出错", e);
      return -1;
    }
    logger.info("初始邮件restful成功保存邮件数据");
    return 1;
  }

}
