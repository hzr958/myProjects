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
import com.smate.web.management.model.mail.MailEverydayStatisticFrom;
import com.smate.web.management.service.mail.MailManageService;

/**
 * 每天发送统计记录Action
 * 
 * @author yhx
 *
 */
@Results({@Result(name = "mail_everyday_statistics_list",
    location = "/WEB-INF/mail_manage/mail_everyday_statistics_list.jsp")})
public class MailEveryDayStatisticAction extends ActionSupport
    implements ModelDriven<MailEverydayStatisticFrom>, Preparable {
  private static final long serialVersionUID = 1L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private MailEverydayStatisticFrom form;
  @Autowired
  private MailManageService mailManageService;

  /**
   * 每天发送统计记录
   * 
   * @return
   */
  @Action("/scmmanagement/mailmanage/findstatisticslist")
  public String findSenderList() {
    try {
      mailManageService.findEveryDayStatisticsList(form);
    } catch (Exception e) {
      logger.error("每天发送统计列表出错", e);
      e.printStackTrace();
    }
    return "mail_everyday_statistics_list";

  }

  @Override
  public void prepare() throws Exception {
    form = new MailEverydayStatisticFrom();
    if (form.getPage() == null) {
      form.setPage(new Page());
    }

  }

  @Override
  public MailEverydayStatisticFrom getModel() {
    return form;
  }

}
