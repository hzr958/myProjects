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
import com.smate.web.management.model.mail.MailLinkForm;
import com.smate.web.management.service.mail.MailManageService;

/**
 * 邮件链接访问Action
 * 
 * @author yhx
 *
 */
@Results({@Result(name = "mail_link_sum_list", location = "/WEB-INF/mail_manage/mail_link_sum_list.jsp")})
public class MailLinkAction extends ActionSupport implements ModelDriven<MailLinkForm>, Preparable {
  private static final long serialVersionUID = 1L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private MailLinkForm form;
  @Autowired
  private MailManageService mailManageService;

  /**
   * 邮件链接访问列表
   * 
   * @return
   */
  @Action("/scmmanagement/mailmanage/findlinksumlist")
  public String findLinkSumList() {
    try {
      mailManageService.findLinkSumList(form);
    } catch (Exception e) {
      logger.error("邮件链接访问列表", e);
      e.printStackTrace();
    }
    return "mail_link_sum_list";

  }

  @Override
  public void prepare() throws Exception {
    form = new MailLinkForm();
    if (form.getPage() == null) {
      form.setPage(new Page());
    }

  }

  @Override
  public MailLinkForm getModel() {
    return form;
  }

}
