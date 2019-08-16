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
import com.smate.web.group.action.grp.form.GrpMemberForm;
import com.smate.web.group.service.grp.member.GrpMemberShowService;

/**
 * 群组站外-成员Action
 * 
 * @author zzx
 *
 */
@Results({
    @Result(name = "grp_outside_member_main", location = "/WEB-INF/jsp/grpoutside/member/grp_outside_member_main.jsp"),
    @Result(name = "grp_outside_members", location = "/WEB-INF/jsp/grpoutside/member/grp_outside_members.jsp"),
    @Result(name = "grp_outside_not_exists", location = "/WEB-INF/jsp/grp/grpmain/grp_info_not_exists.jsp")})
public class GrpOutsideMemberAction extends ActionSupport implements ModelDriven<GrpMemberForm>, Preparable {
  private static final long serialVersionUID = 1L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private GrpMemberForm form;
  @Autowired
  private GrpMemberShowService grpMemberShowService;

  /**
   * 显示群组成员主页面
   * 
   * @return
   */
  @Action("/groupweb/grpinfo/outside/ajaxshowgrpmembermain")
  public String showGrpMemberMain() {
    return "grp_outside_member_main";
  }

  /**
   * 显示群组成员列表
   * 
   * @return
   */
  @Action("/groupweb/grpinfo/outside/ajaxshowgrpmembers")
  public String showGrpMembers() {
    try {
      grpMemberShowService.getGrpMemberListForShow(form);
    } catch (Exception e) {
      logger.error("显示站外群组成员列表出错grpId=" + form.getGrpId(), e);
      return null;
    }
    return "grp_outside_members";
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new GrpMemberForm();
    }

  }

  @Override
  public GrpMemberForm getModel() {
    return form;
  }

}
