package com.smate.web.group.action.grp.outside;

import java.util.HashMap;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.web.group.action.grp.form.GrpDiscussForm;
import com.smate.web.group.model.grp.grpbase.GrpBaseinfo;
import com.smate.web.group.model.grp.grpbase.GrpControl;
import com.smate.web.group.service.grp.manage.GrpBaseService;
import com.smate.web.group.service.grp.member.GrpMemberShowService;
import com.smate.web.group.service.grp.member.GrpRoleService;
import com.smate.web.group.service.grp.pub.GrpPubsService;

/**
 * 群组讨论action
 * 
 * @author tsz
 *
 */
@Results({@Result(name = "discuss_desc", location = "/WEB-INF/jsp/grp/discuss/grp_discuss_desc.jsp"),
    @Result(name = "discuss_member", location = "/WEB-INF/jsp/grpoutside/discuss/grp_outside_discuss_fivemember.jsp"),
    @Result(name = "discuss_pub", location = "/WEB-INF/jsp/grpoutside/discuss/grp_outside_discuss_fivepub.jsp"),
    @Result(name = "grp_discuss_main", location = "/WEB-INF/jsp/grpoutside/discuss/show_grp_outside_discuss_main.jsp"),
    @Result(name = "grp_discuss_other", location = "/WEB-INF/jsp/grp/discuss/grp_discuss_other.jsp")})
public class GrpOutsideDiscussAction extends ActionSupport implements ModelDriven<GrpDiscussForm>, Preparable {
  /**
   * 
   */
  private static final long serialVersionUID = -71478502471040800L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  GrpDiscussForm form;
  @Autowired
  GrpBaseService grpBaseService;
  @Autowired
  GrpMemberShowService grpMemberShowService;
  @Autowired
  GrpPubsService grpPubsService;
  @Autowired
  private GrpRoleService grpRoleService;

  /**
   * 进去讨论页面
   * 
   * @return
   */
  @Action(value = "/groupweb/grpinfo/outside/ajaxdiscussmain")
  public String grpMain() {
    form.setRole(grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId()));
    GrpBaseinfo grpBaseinfo = grpBaseService.getCurrGrp(form.getGrpId());
    GrpControl currGrpControl = grpBaseService.getCurrGrpControl(form.getGrpId());
    form.setGrpControl(currGrpControl);
    form.setOpenType(grpBaseinfo.getOpenType());
    form.setFlag(grpBaseService.grpIsShowIndexOpen(grpBaseinfo, currGrpControl));
    return "grp_discuss_main";
  }

  /**
   * 群组讨论加载群组简介
   * 
   * @return
   */
  @Action(value = "/groupweb/grpinfo/outside/ajaxgrpbrief")
  public String ajaxGrpBrief() {
    try {
      grpBaseService.getGrpDesc(form);
      return "discuss_desc";
    } catch (Exception e) {
      logger.error("加载群组简介异常：psnId=" + form.toString(), e);
    }
    return null;
  }

  /**
   * 群组讨论 加载群组成员
   * 
   * @return
   */
  @Action("/groupweb/grpinfo/outside/ajaxgrpdiscussmembers")
  public String ajaxGrpMembers() {
    try {
      if (form.getGrpId() != null && form.getGrpId() != 0L) {
        form.setMemberInfoList(grpMemberShowService.getFiveMemberForGrpDiscuss(form.getGrpId(), form.getPsnId()));
        return "discuss_member";
      }
    } catch (Exception e) {
      logger.error("加载群组成员异常：" + form.toString(), e);
    }
    return null;
  }

  @Action("/groupweb/grpinfo/outside/ajaxgrpotherinfo")
  public String ajaxGrpOtherInfo() {
    try {
      if (form.getGrpId() != null && form.getGrpId() != 0L) {
        HashMap<String, Object> resultMap = grpBaseService.getGrpPrjInfo(form.getGrpId() + "");
        form.setPrjInfo(grpBaseService.dealDate(resultMap));
        return "grp_discuss_other";
      }
    } catch (Exception e) {
      logger.error("加载群组讨论，其他出错 " + form.toString(), e);
    }
    return null;
  }

  /**
   * 检查群组id和psnId
   * 
   * @return
   */
  private boolean checkPsnIdGrpId() {
    if (form.getGrpId() != null && form.getGrpId() != 0L && form.getPsnId() != null && form.getPsnId() != 0L) {
      return true;
    }
    return false;
  }

  /**
   * 群组讨论 加载群组成果
   * 
   * @return
   */
  @Action("/groupweb/grpinfo/outside/ajaxgrpdiscusspubs")
  public String ajaxGrpPubs() {
    try {
      if (form.getGrpId() != null && form.getGrpId() != 0L) {
        form.setGrpPubShowInfoList(grpPubsService.getFiveGrpPubsForDiscuss(form.getGrpId()));
        return "discuss_pub";
      }
    } catch (Exception e) {
      logger.error("加载群组讨论，成果出错 " + form.toString(), e);
    }
    return null;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new GrpDiscussForm();
    }
  }

  @Override
  public GrpDiscussForm getModel() {
    return form;
  }
}
