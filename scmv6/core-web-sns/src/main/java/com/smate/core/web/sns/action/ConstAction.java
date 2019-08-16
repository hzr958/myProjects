package com.smate.core.web.sns.action;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.consts.service.ConstDisciplineService;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.core.web.sns.form.ConstForm;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

/**
 * 公共常量查询控制器
 * 
 * @author houchuanjie
 * @date 2018年3月21日 下午2:11:25
 */

@Results ({
    @Result(name = "homepage", location = "/WEB-INF/jsp/psnprofile/psn_homepage_main.jsp"),
})
public class ConstAction extends ActionSupport implements ModelDriven<ConstForm>, Preparable {
  private static final long serialVersionUID = -6124127622524731924L;
  private ConstForm form;
  @Autowired
  private ConstDisciplineService constDisciplineService;

  /**
   * 通过学科代码查询学科领域JSON数据
   *
   * @author houchuanjie
   * @date 2018年3月21日 下午2:15:01
   * @return
   * @throws Exception
   */
  @Actions({@Action("/prjweb/const/ajax-disciplines"), @Action("/pubweb/const/ajax-disciplines")})
  public String ajaxDiscipline() throws Exception {
    try {
      String jsonRes = constDisciplineService.findDiscJsonData(form.getDiscCode());
      Struts2Utils.renderJson(jsonRes, "encoding:UTF-8");
    } catch (Exception e) {
      String resultJson = "{'result':'error'}";
      Struts2Utils.renderJson(resultJson, "encoding:UTF-8");
      return null;
    }
    return null;
  }


  @Override
  public void prepare() throws Exception {
    this.form = Optional.ofNullable(form).orElse(new ConstForm());
  }

  @Override
  public ConstForm getModel() {
    return form;
  }

  /**
   * @return form
   */
  public ConstForm getForm() {
    return form;
  }

  /**
   * @param form 要设置的 form
   */
  public void setForm(ConstForm form) {
    this.form = form;
  }

}
