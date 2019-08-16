package com.smate.web.psn.action.cooperation;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.psn.model.cooperation.PsnKnowCopartnerForm;
import com.smate.web.psn.service.cooperation.CooperationService;

/**
 * 
 * 合作者列表信息
 * 
 * @author zx
 *
 */
@Results({@Result(name = "prjcooperation", location = "/WEB-INF/jsp/cooperation/prjCooperation.jsp"),
    @Result(name = "prjcooperation_sub", location = "/WEB-INF/jsp/cooperation/prjCooperation_sub.jsp"),
    @Result(name = "pub_cooperator", location = "/WEB-INF/jsp/cooperation/pub_cooperator.jsp"),
    @Result(name = "outside_pub_cooperator", location = "/WEB-INF/jsp/outsidecooperation/outside_pub_cooperator.jsp"),
    @Result(name = "outside_pub_cooperator_list",
        location = "/WEB-INF/jsp/outsidecooperation/outside_pub_cooperator_list.jsp"),
    @Result(name = "outside_prj_cooperator", location = "/WEB-INF/jsp/outsidecooperation/outside_prj_cooperator.jsp"),
    @Result(name = "outside_prj_cooperator_list",
        location = "/WEB-INF/jsp/outsidecooperation/outside_prj_cooperator_list.jsp"),
    @Result(name = "pub_cooperator_list", location = "/WEB-INF/jsp/cooperation/pub_cooperator_list.jsp"),
    @Result(name = "psn_cooperator", location = "/WEB-INF/jsp/cooperation/psn_cooperator.jsp"),
    @Result(name = "psn_cooperator_list", location = "/WEB-INF/jsp/cooperation/psn_cooperator_list.jsp")})
public class CooperationAction extends ActionSupport implements Preparable, ModelDriven<PsnKnowCopartnerForm> {

  private static final long serialVersionUID = 8528467571979038832L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private Page page = new Page();
  private PsnKnowCopartnerForm form;
  @Autowired
  private CooperationService cooperationService;

  /**
   * 项目合作者
   */
  @Action("/psnweb/ajaxprjcooperation")
  public String loadPrjCooperation() throws Exception {
    try {
      page = cooperationService.findPsnKnowCopartner(page, form, 2);
    } catch (Exception e) {
      logger.error("获取合作者出错", SecurityUtils.getCurrentUserId(), e);
    }
    if (form.getFirstPage() != true) {
      return "prjcooperation_sub";
    } else {
      return "prjcooperation";
    }
  }

  /**
   * 成果合作者
   * 
   * @return
   */
  @Actions({@Action("/psnweb/cooperation/ajaxpubcooperator")})
  public String loadPubCooperation() {
    try {
      if (form.getIsAll() == 1) {// 查看全部
        form.setFirstPage(false);
      }
      page = cooperationService.findPsnKnowCopartner(page, form, 1);
    } catch (Exception e) {
      logger.error("查询成果合作者,出错,psnId= " + form.getPsnId(), e);
    }
    if (form.getIsAll() == 1) {
      return "pub_cooperator_list";
    }
    return "pub_cooperator";
  }

  /**
   * 站外成果合作者
   * 
   * @return
   */
  @Actions({@Action("/psnweb/outside/ajaxpubcooperator"), @Action("/psnweb/outside/ajaxpubcooperatorAll")})
  public String outsidePubCooperation() {
    try {
      if (SecurityUtils.getCurrentUserId() == null || SecurityUtils.getCurrentUserId() == 0l) {
        form.setHasLogin(false);
      }
      if (form.getIsAll() == 1) {// 查看全部
        form.setFirstPage(false);
      }
      page = cooperationService.findPsnKnowCopartner(page, form, 1);
    } catch (Exception e) {
      logger.error("查询站外成果合作者,出错,psnId= " + form.getPsnId(), e);
    }
    if (form.getIsAll() == 1) {
      return "outside_pub_cooperator_list";
    }
    if (form.getShowList() == 1) {
      return "psn_cooperator";
    } else {
      return "outside_pub_cooperator";
    }
  }

  /**
   * 站外项目合作者
   * 
   * @return
   */
  @Actions({@Action("/psnweb/outside/ajaxprjcooperator"), @Action("/psnweb/outside/ajaxprjcooperatorAll")})
  public String outsidePrjCooperation() {
    try {
      if (SecurityUtils.getCurrentUserId() == null || SecurityUtils.getCurrentUserId() == 0l) {
        form.setHasLogin(false);
      }
      if (form.getIsAll() == 1) {// 查看全部
        form.setFirstPage(false);
      }
      page = cooperationService.findPsnKnowCopartner(page, form, 2);
    } catch (Exception e) {
      logger.error("查询站外成果合作者,出错,psnId= " + form.getPsnId(), e);
    }
    if (form.getIsAll() == 1) {
      return "outside_prj_cooperator_list";
    }
    if (form.getShowList() == 1) {
      return "psn_cooperator";
    } else {
      return "outside_prj_cooperator";
    }
  }

  /**
   * 合作者
   * 
   * @return
   */
  @Action("/psnweb/cooperation/ajaxshow")

  public String loadPsnCooperation() {
    try {
      if (form.getIsAll() == 1) {// 查看全部
        form.setFirstPage(false);
      }
      page = cooperationService.findPsnKnowCopartner(page, form, 1);
    } catch (Exception e) {
      logger.error("个人主页查询合作者出错,psnId= " + form.getPsnId(), e);
    }
    if (form.getIsAll() == 1) {
      return "psn_cooperator_list";
    }
    return "psn_cooperator";
  }

  public void prepare() throws Exception {
    if (form == null) {
      form = new PsnKnowCopartnerForm();
    }

  }

  public PsnKnowCopartnerForm getModel() {
    return form;
  }

  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
  }
}
