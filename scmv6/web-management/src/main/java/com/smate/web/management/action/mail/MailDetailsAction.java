package com.smate.web.management.action.mail;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.management.model.mail.MailDetailsForm;
import com.smate.web.management.service.mail.MailDetailsService;

/**
 * 邮件详情Action
 * 
 * @author zzx
 *
 */
@Results({@Result(name = "mail_details", location = "/WEB-INF/mail_manage/mail_details.jsp"),})
public class MailDetailsAction extends ActionSupport implements ModelDriven<MailDetailsForm>, Preparable {
  private static final long serialVersionUID = 1L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private MailDetailsForm form;
  @Autowired
  private MailDetailsService mailDetailsService;

  /**
   * 邮件详情
   * 
   * @return
   */
  @Action("/scmmanagement/mail/details")
  public void showMailDetails() {
    try {
      String details = mailDetailsService.findMailDetails(form.getDes3MailId());
      if (StringUtils.isNotBlank(details)) {
        HttpServletResponse response = Struts2Utils.getResponse();
        response.setContentType("text/html; charset=utf-8");
        PrintWriter pw = response.getWriter();
        pw.write(details);
        pw.flush();
        pw.close();
      }
    } catch (Exception e) {
      logger.error("进入邮件详情出错", e);
    }
  }

  @Override
  public void prepare() throws Exception {
    form = new MailDetailsForm();
  }

  @Override
  public MailDetailsForm getModel() {
    return form;
  }
}
