package com.smate.center.mail.service.dispatch.client;

import java.util.Random;

import com.smate.center.mail.service.dispatch.MailDispatchService;

/**
 * 邮件调度 ->分配客户端服务接口
 * 
 * @author tsz
 *
 */
public interface MailDispatchClientService extends MailDispatchService {

  public Random ran = new Random();

}
