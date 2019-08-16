package com.smate.center.mail.service.build;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.mail.model.MailDataInfo;
import com.smate.center.mail.service.MailBuildInfoService;

/**
 * 效验是否能发送邮件
 * 
 * @author zzx
 *
 */
public class MailBuildInfoExcuteCheckServiceImpl implements MailBuildInfoExcuteService {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private MailBuildInfoService mailBuildInfoService;

  @Override
  public void excute(MailDataInfo info) throws Exception {
    // 邮箱格式验证
    mailBuildInfoService.validateEmail(info);
    // 人员是否接收
    mailBuildInfoService.checkUsable(info);
    // 模版是否有限制
    mailBuildInfoService.checkTempLimit(info);
    // 发送者与接收者首要邮箱是否一致
    mailBuildInfoService.checkFristEmail(info);
  }

}
