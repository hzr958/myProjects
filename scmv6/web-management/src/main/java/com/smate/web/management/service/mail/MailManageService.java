package com.smate.web.management.service.mail;

import com.smate.web.management.model.mail.MailBlackListForm;
import com.smate.web.management.model.mail.MailClientForm;
import com.smate.web.management.model.mail.MailEverydayStatisticFrom;
import com.smate.web.management.model.mail.MailLinkForm;
import com.smate.web.management.model.mail.MailManageForm;
import com.smate.web.management.model.mail.MailSenderForm;
import com.smate.web.management.model.mail.MailTemplateForm;
import com.smate.web.management.model.mail.MailWhiteListForm;

/**
 * 
 * @author zzx
 *
 */
public interface MailManageService {
  /**
   * 显示邮件管理主页
   * 
   * @param form
   */
  void showMain(MailManageForm form) throws Exception;

  /**
   * 查找邮件列表
   * 
   * @param form
   * @throws Exception
   */
  void findMailManageList(MailManageForm form) throws Exception;

  /**
   * 查找邮件链接列表
   * 
   * @param form
   * @throws Exception
   */
  void findMailLinkList(MailManageForm form) throws Exception;

  /**
   * 邮件退信列表
   * 
   * @param form
   * @throws Exception
   */
  void returnList(MailManageForm form) throws Exception;

  /**
   * 模板列表
   * 
   * @param form
   * @throws Exception
   */
  void findTemplateList(MailTemplateForm form) throws Exception;

  /**
   * 邮件链接访问列表
   * 
   * @param form
   * @throws Exception
   */
  void findLinkSumList(MailLinkForm form) throws Exception;

  /**
   * 发送账号列表
   * 
   * @param form
   */
  void findSenderList(MailSenderForm form) throws Exception;

  /**
   * 发送客户端列表
   * 
   * @param form
   * @throws Exception
   */
  void findClientList(MailClientForm form) throws Exception;

  /**
   * 黑名单列表
   * 
   * @param form
   * @throws Exception
   */
  void findBlackList(MailBlackListForm form) throws Exception;

  /**
   * 白名单列表
   * 
   * @param form
   * @throws Exception
   */
  void findWhiteList(MailWhiteListForm form) throws Exception;

  /**
   * 每天发送统计列表
   * 
   * @param form
   */
  void findEveryDayStatisticsList(MailEverydayStatisticFrom form) throws Exception;
}
