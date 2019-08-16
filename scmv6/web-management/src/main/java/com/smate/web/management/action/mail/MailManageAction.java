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
import com.smate.web.management.model.mail.MailManageForm;
import com.smate.web.management.service.mail.MailManageService;

/**
 * 邮件管理Action
 * 
 * @author zzx
 *
 */
@Results({@Result(name = "mail_manage_main", location = "/WEB-INF/mail_manage/mail_manage_main.jsp"),
    @Result(name = "mail_manage_list", location = "/WEB-INF/mail_manage/mail_manage_list.jsp"),
    @Result(name = "mail_link_list", location = "/WEB-INF/mail_manage/mail_link_list.jsp"),
    @Result(name = "mail_return_list", location = "/WEB-INF/mail_manage/mail_return_list.jsp")})
public class MailManageAction extends ActionSupport implements ModelDriven<MailManageForm>, Preparable {
  private static final long serialVersionUID = 1L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private MailManageForm form;
  @Autowired
  private MailManageService mailManageService;

  /**
   * 邮件退信列表
   * 
   * @return
   */
  @Action("/scmmanagement/mailmanage/returnlist")
  public String showMailReturnList() {
    try {
      mailManageService.returnList(form);
    } catch (Exception e) {
      logger.error("读取邮件退信列表出错", e);
      return null;
    }
    return "mail_return_list";
  }

  /**
   * 邮件管理主页
   * 
   * @return
   */
  @Action("/scmmanagement/mailmanage/main")
  public String showMailManageMain() {
    try {
      mailManageService.showMain(form);
    } catch (Exception e) {
      logger.error("进入邮件管理主页出错", e);
      return null;
    }
    return "mail_manage_main";
  }

  /**
   * 邮件管理信息列表
   * 
   * @return
   */
  @Action("/scmmanagement/mailmanage/findlist")
  public String findMailManageList() {
    try {
      mailManageService.findMailManageList(form);
    } catch (Exception e) {
      logger.error("查找信息列表出错", e);
      return null;
    }
    return "mail_manage_list";
  }

  /**
   * 邮件链接信息列表
   * 
   * @return
   */
  @Action("/scmmanagement/mailmanage/findlinklist")
  public String findMailLinkList() {
    try {
      mailManageService.findMailLinkList(form);
    } catch (Exception e) {
      logger.error("邮件链接信息列表出错", e);
      return null;
    }
    return "mail_link_list";
  }


  @Override
  public void prepare() throws Exception {
    form = new MailManageForm();
    if (form.getPage() == null) {
      form.setPage(new Page());
    }
  }

  @Override
  public MailManageForm getModel() {
    return form;
  }

}
