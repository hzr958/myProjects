package com.smate.web.group.action.grp.outside;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.group.action.grp.form.GrpPubForm;
import com.smate.web.group.service.grp.manage.GrpBaseService;
import com.smate.web.group.service.grp.pub.GrpPubsService;

/**
 * 群组站外-成果Action
 * 
 * @author zzx
 *
 */
@Results({@Result(name = "grp_outside_pub_main", location = "/WEB-INF/jsp/grpoutside/pub/grp_outside_pub_main.jsp"),
    @Result(name = "grp_outside_pub_list", location = "/WEB-INF/jsp/grpoutside/pub/grp_outside_pub_list.jsp"),
    @Result(name = "grp_outside_not_exists", location = "/WEB-INF/jsp/grp/grpmain/grp_info_not_exists.jsp")})
public class GrpOutsidePubAction extends ActionSupport implements ModelDriven<GrpPubForm>, Preparable {
  private static final long serialVersionUID = 1L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private GrpPubForm form;
  @Autowired
  private GrpPubsService grpPubsService;
  @Autowired
  private GrpBaseService grpBaseService;

  /**
   * 群组成果主页
   * 
   * @return
   */
  @Action(value = "/groupweb/grpinfo/outside/ajaxshowgrppubsmain")
  public String ajaxShowGrpPubsMain() {
    return "grp_outside_pub_main";
  }

  /**
   * 获取群组成果列表
   * 
   * @return
   */
  @Action(value = "/groupweb/grpinfo/outside/ajaxgrppublist")
  public String ajaxGetGrpPubs() {
    try {
      Integer grpCategory = grpBaseService.getGrpCategory(form.getGrpId());
      form.setGrpCategory(grpCategory);
      if (grpCategory == 10) {
        form.setShowPrjPub(1);
        form.setShowRefPub(1);
      }
      if (NumberUtils.isNullOrZero(form.getPsnId())) {
        form.setPsnId(SecurityUtils.getCurrentUserId());
      }
      grpPubsService.getGrpPubs(form);
    } catch (Exception e) {
      logger.error("获取站外群组成果列表数据异常 psnId=" + form.getPsnId() + "; grpId=" + form.getGrpId(), e);
    }
    return "grp_outside_pub_list";
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new GrpPubForm();
    }
  }

  @Override
  public GrpPubForm getModel() {
    return form;
  }

}
