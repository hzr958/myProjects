package com.smate.center.mail.service.dispatch.sender;

import java.util.Random;

import com.smate.center.mail.service.dispatch.MailDispatchService;

/**
 * 调度中心->分配发送账号 接口
 * 
 * @author tsz
 *
 */
public interface MailDispatchSenderService extends MailDispatchService {

  public Random ran = new Random();
}
