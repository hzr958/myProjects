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
import com.smate.web.management.model.mail.MailBlackListForm;
import com.smate.web.management.service.mail.MailManageService;

/**
 * 黑名单Action
 * 
 * @author yhx
 *
 */
@Results({@Result(name = "mail_black_list", location = "/WEB-INF/mail_manage/mail_black_list.jsp")})
public class MailBlackListAction extends ActionSupport implements ModelDriven<MailBlackListForm>, Preparable {
  private static final long serialVersionUID = 1L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private MailBlackListForm form;
  @Autowired
  private MailManageService mailManageService;

  /**
   * 黑名单
   * 
   * @return
   */
  @Action("/scmmanagement/mailmanage/findblacklist")
  public String findBlackList() {
    try {
      mailManageService.findBlackList(form);
    } catch (Exception e) {
      logger.error("黑名单列表出错", e);
      e.printStackTrace();
    }
    return "mail_black_list";

  }

  @Override
  public void prepare() throws Exception {
    form = new MailBlackListForm();
    if (form.getPage() == null) {
      form.setPage(new Page());
    }

  }

  @Override
  public MailBlackListForm getModel() {
    return form;
  }

}
