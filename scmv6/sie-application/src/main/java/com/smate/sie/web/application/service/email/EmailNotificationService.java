package com.smate.sie.web.application.service.email;

import java.util.Map;

/**
 * 通知财务开发票邮件接口
 * 
 * @author xr
 *
 */
public interface EmailNotificationService {

  public void sendEmailInvoiceInformFinance(Map<String, Object> hashInput) throws Exception;

}
