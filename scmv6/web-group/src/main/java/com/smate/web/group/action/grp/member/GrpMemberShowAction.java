package com.smate.web.group.action.grp.member;

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
import com.smate.web.group.action.grp.form.GrpMemberForm;
import com.smate.web.group.service.grp.member.GrpMemberShowService;
import com.smate.web.group.service.grp.member.GrpRoleService;

/**
 * 群组成员显示Action
 * 
 * @author zzx
 *
 */
@Results({@Result(name = "show_grp_member_pub_sum2", location = "/WEB-INF/jsp/grp/pub/grp_member_pub_sum_list2.jsp"),
    @Result(name = "show_grp_member_pub_sum", location = "/WEB-INF/jsp/grp/pub/grp_member_pub_sum_list.jsp"),
    @Result(name = "show_grp_member_list", location = "/WEB-INF/jsp/grp/pub/grp_member_list.jsp"),
    @Result(name = "show_grp_member_main", location = "/WEB-INF/jsp/grp/grpmember/show_grp_member_main.jsp"),
    @Result(name = "ajaxshowgrpmembers", location = "/WEB-INF/jsp/grp/grpmember/ajax_show_grp_members.jsp"),
    @Result(name = "ajaxshowgrppropose", location = "/WEB-INF/jsp/grp/grpmember/ajax_show_grp_propose.jsp"),
    @Result(name = "ajaxshowgrpproposesub", location = "/WEB-INF/jsp/grp/grpmember/ajax_show_grp_propose_sub.jsp"),
    @Result(name = "ajaxshowgrpproposes", location = "/WEB-INF/jsp/grp/grpmember/ajax_show_grp_proposes.jsp"),
    @Result(name = "ajaxshowgrpreferrers", location = "/WEB-INF/jsp/grp/grpmember/ajax_show_grp_referrers.jsp"),
    @Result(name = "ajax_show_friends", location = "/WEB-INF/jsp/grp/grpmember/ajax_show_friends.jsp"),
    @Result(name = "chatpsncard", location = "/WEB-INF/jsp/grp/grpmember/chat_psn_card.jsp")


})
public class GrpMemberShowAction extends ActionSupport implements ModelDriven<GrpMemberForm>, Preparable {

  private static final long serialVersionUID = 6794177953867898847L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private GrpMemberForm form;
  @Autowired
  private GrpRoleService grpRoleService;
  @Autowired
  private GrpMemberShowService grpMemberShowService;

  /**
   * 显示群组成员主页面
   * 
   * @return
   */
  @Action("/groupweb/grpmember/ajaxshowgrpmembermain")
  public String showGrpMemberMain() {
    form.setRole(grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId()));
    return "show_grp_member_main";
  }

  /**
   * 显示群组成员列表
   * 
   * @return
   */
  @Action("/groupweb/grpmember/ajaxshowgrpmembers")
  public String showGrpMembers() {
    try {
      grpMemberShowService.getGrpMemberListForShow(form);
    } catch (Exception e) {
      logger.error("显示群组成员列表出错grpId=" + form.getGrpId(), e);
      return null;
    }
    return "ajaxshowgrpmembers";
  }

  /**
   * 获取聊天人员卡片信息
   * 
   * @return
   */
  @Action("/groupweb/grpmember/ajaxgetchatpsncard")
  public String ajaxgetChatPsnCard() {
    try {
      grpMemberShowService.getChatPsnCard(form);
    } catch (Exception e) {
      logger.error("获取聊天人员卡片信息出错form=" + form, e);
      return null;
    }
    return "chatpsncard";
  }

  /**
   * 显示群组申请人员列表
   * 
   * @return
   */
  @Action("/groupweb/grpmember/ajaxshowgrpproposes")
  public String showGrpProposers() {
    // 1=第一次查显示单条申请人记录，2=非第一次查显示单条申请人记录，3=显示申请人列表
    if (form.getShowType() == 1) {
      form.getPage().setIgnoreMin(true);
      form.getPage().setPageSize(1);
    }
    try {
      Integer role = grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId());
      if (role == 2 || role == 1) {
        grpMemberShowService.getGrpProposerListForShow(form);
      } else {
        return null;
      }
    } catch (Exception e) {
      logger.error("显示群组申请人员列表出错grpId=" + form.getGrpId(), e);
      return null;
    }
    if (form.getShowType() == 1) {
      return "ajaxshowgrppropose";
    } else {
      return "ajaxshowgrpproposes";
    }
  }

  /**
   * 显示群组推荐人员列表
   * 
   * @return
   */
  @Action("/groupweb/grpmember/ajaxshowgrpreferrers")
  public String showGrpReferrers() {
    try {
      Integer role = grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId());
      if (role == 2 || role == 1 || role == 3) {
        grpMemberShowService.getGrpRecommendListForShow(form);
      } else {
        return null;
      }

    } catch (Exception e) {
      logger.error("显示群组推荐人员列表出错grpId=" + form.getGrpId(), e);
      return null;
    }

    return "ajaxshowgrpreferrers";
  }

  /**
   * 显示群组成员成果统计
   * 
   * @return
   */
  @Action("/groupweb/grpmember/ajaxshowmemberpubsum")
  public String showGrpMemberPubSum() {
    try {
      Integer role = grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId());
      if (role == 2 || role == 1 || role == 3) {
        grpMemberShowService.getGrpMemberPubSum(form);
        return "show_grp_member_pub_sum";
      }

    } catch (Exception e) {
      logger.error("显示群组成员成果统计出错grpId=" + form.getGrpId(), e);
    }

    return null;
  }

  @Action("/groupweb/grpmember/ajaxshowmemberpubsum2")
  public String showGrpMemberPubSum2() {
    try {
      grpMemberShowService.getGrpMemberPubSum(form);
      return "show_grp_member_pub_sum2";
    } catch (Exception e) {
      logger.error("显示群组成员成果统计出错grpId=" + form.getGrpId(), e);
    }

    return null;
  }

  @Action("/groupweb/grpmember/ajaxshowmemberpublist")
  public String showGrpMemberPub() {
    try {
      grpMemberShowService.findGrpPubPsnList(form);
      return "show_grp_member_list";
    } catch (Exception e) {
      logger.error("显示群组成员成果统计出错grpId=" + form.getGrpId(), e);
    }

    return null;
  }

  /**
   * 显示好友列表待选择邀请加入群组
   * 
   * @return
   */
  @Action("/groupweb/grpmember/ajaxshowfriends")
  public String ajaxShowFriends() {
    try {
      grpMemberShowService.getFriendListForShow(form);
    } catch (Exception e) {
      logger.error("显示群组成员成果统计出错grpId=" + form.getGrpId(), e);
      return null;
    }
    return "ajax_show_friends";
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new GrpMemberForm();
    }
    if (form.getPage() == null) {
      form.setPage(new Page());
    }

  }

  @Override
  public GrpMemberForm getModel() {
    // TODO Auto-generated method stub
    return form;
  }
}
