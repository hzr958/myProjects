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
import com.smate.web.management.model.mail.MailTemplateForm;
import com.smate.web.management.service.mail.MailManageService;

/**
 * 邮件管理Action
 * 
 * @author yhx
 *
 */
@Results({@Result(name = "mail_template_list", location = "/WEB-INF/mail_manage/mail_template_list.jsp")})
public class MailTemplateAction extends ActionSupport implements ModelDriven<MailTemplateForm>, Preparable {
  private static final long serialVersionUID = 1L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private MailTemplateForm form;
  @Autowired
  private MailManageService mailManageService;

  /**
   * 模板列表
   * 
   * @return
   */
  @Action("/scmmanagement/mailmanage/findtemplatelist")
  public String findTemplateList() {
    try {
      mailManageService.findTemplateList(form);
    } catch (Exception e) {
      logger.error("模板列表出错", e);
      e.printStackTrace();
    }
    return "mail_template_list";

  }

  @Override
  public void prepare() throws Exception {
    form = new MailTemplateForm();
    if (form.getPage() == null) {
      form.setPage(new Page());
    }
  }

  @Override
  public MailTemplateForm getModel() {
    return form;
  }

}
