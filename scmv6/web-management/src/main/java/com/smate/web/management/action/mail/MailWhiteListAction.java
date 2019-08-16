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
import com.smate.web.management.model.mail.MailWhiteListForm;
import com.smate.web.management.service.mail.MailManageService;

/**
 * 白名单Action
 * 
 * @author yhx
 *
 */
@Results({@Result(name = "mail_white_list", location = "/WEB-INF/mail_manage/mail_white_list.jsp")})
public class MailWhiteListAction extends ActionSupport implements ModelDriven<MailWhiteListForm>, Preparable {
  private static final long serialVersionUID = 1L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private MailWhiteListForm form;
  @Autowired
  private MailManageService mailManageService;

  /**
   * 白名单
   * 
   * @return
   */
  @Action("/scmmanagement/mailmanage/findwhitelist")
  public String findBlackList() {
    try {
      mailManageService.findWhiteList(form);
    } catch (Exception e) {
      logger.error("白名单列表出错", e);
      e.printStackTrace();
    }
    return "mail_white_list";

  }

  @Override
  public void prepare() throws Exception {
    form = new MailWhiteListForm();
    if (form.getPage() == null) {
      form.setPage(new Page());
    }

  }

  @Override
  public MailWhiteListForm getModel() {
    return form;
  }

}
