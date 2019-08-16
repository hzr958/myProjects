package com.smate.web.management.action;



import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.management.model.psn.PsnInfoForm;
import com.smate.web.management.service.psn.PsnInfoService;



@Results({@Result(name = "psnInfoMain", location = "/WEB-INF/management/researcher_main.jsp"),
    @Result(name = "psnInfoList", location = "/WEB-INF/management/ajax_researcher_list.jsp"),
    @Result(name = "psnEmail", location = "/WEB-INF/management/email_mail.jsp"),
    @Result(name = "psnEmailList", location = "/WEB-INF/management/ajax_email_list.jsp"),
    @Result(name = "addMerge", location = "/WEB-INF/management/merge_count_add.jsp")})
public class PsnInfoAction extends ActionSupport implements Preparable, ModelDriven<PsnInfoForm> {
  private static final long serialVersionUID = 2542151835894564124L;

  private PsnInfoForm form;
  @Autowired
  private PsnInfoService psnInfoService;

  @Action("/scmmanagement/psnInfo/main")
  public String psnInfoMain() {
    return "psnInfoMain";
  }

  @Action("/scmmanagement/psnInfo/ajaxPsnList")
  public String psnInfoList() throws Exception {
    psnInfoService.getPsnInfo(form);
    return "psnInfoList";
  }

  @Action("/scmmanagement/psnInfo/email")
  public String email() throws Exception {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    form.setNowDate(dateFormat.format(new Date()));
    return "psnEmail";
  }

  @Action("/scmmanagement/emailInfo/ajaxEmailInfoList")
  public String ajaxEmailInfoList() throws Exception {
    psnInfoService.getPsnEmailListInfo(form);
    return "psnEmailList";
  }

  /**
   * 进入添加合并帐号页面.
   * 
   * @return
   */
  @Action("/scmmanagement/setting/enterAddMerge")
  public String enterAddMerge() {
    return "addMerge";
  }

  @Action("/scmmanagement/setting/ajaxMergeCount")
  public String ajaxMergeCount() {
    String resultJson = psnInfoService.dealMergeCount(form);
    Struts2Utils.renderJson(resultJson, "encoding:UTF-8");
    return null;

  }

  @Override
  public PsnInfoForm getModel() {
    return form;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new PsnInfoForm();
    }
  }

}
