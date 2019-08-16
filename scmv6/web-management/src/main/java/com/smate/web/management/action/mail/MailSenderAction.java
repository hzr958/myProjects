package com.smate.web.management.action.mail;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.model.Page;
import com.smate.web.management.model.mail.MailSenderForm;
import com.smate.web.management.service.mail.MailManageService;

/**
 * 发送账号Action
 * 
 * @author yhx
 *
 */
@Results({@Result(name = "mail_sender_list", location = "/WEB-INF/mail_manage/mail_sender_list.jsp")})
public class MailSenderAction extends ActionSupport implements ModelDriven<MailSenderForm>, Preparable {
  private static final long serialVersionUID = 1L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private MailSenderForm form;
  @Autowired
  private MailManageService mailManageService;

  /**
   * 发送账号
   * 
   * @return
   */
  @Action("/scmmanagement/mailmanage/findsenderlist")
  public String findSenderList() {
    try {
      mailManageService.findSenderList(form);
    } catch (Exception e) {
      logger.error("发送账号列表出错", e);
      e.printStackTrace();
    }
    return "mail_sender_list";

  }

  @Override
  public void prepare() throws Exception {
    form = new MailSenderForm();
    if (form.getPage() == null) {
      form.setPage(new Page());
    }

  }

  @Override
  public MailSenderForm getModel() {
    return form;
  }

}
