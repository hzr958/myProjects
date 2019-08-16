package com.smate.web.dyn.action.dynamic.group;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.web.dyn.form.dynamic.group.GroupDynShowForm;
import com.smate.web.dyn.service.dynamic.group.GroupDynamicShowService;
import com.smate.web.dyn.service.group.GroupOptService;

/**
 * 群组动态显示及相关操作Action
 * 
 * @author zzx
 *
 */
@Results({@Result(name = "groupdynlist", location = "/WEB-INF/jsp/dyn/group/group_dyn_list.jsp"),
    @Result(name = "groupdynmain", location = "/WEB-INF/jsp/dyn/group/group_dyn_main.jsp"),
    @Result(name = "dyncomment", location = "/WEB-INF/jsp/dyn/group/group_dyn_comment.jsp"),
    @Result(name = "dyncommentlist", location = "/WEB-INF/jsp/dyn/group/dyn_comment_list.jsp")})
public class GroupDynamicShowAction extends ActionSupport implements Preparable, ModelDriven<GroupDynShowForm> {
  private static final long serialVersionUID = -5050867127561568019L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private GroupDynShowForm form;
  @Autowired
  private GroupDynamicShowService groupDynamicShowService;
  @Resource
  private GroupOptService groupOptService;

  /**
   * 获取群组动态列表链接
   * 
   * @return
   */
  @Action("/dynweb/dynamic/groupdyn/ajaxshow")
  public String ajaxShowGroupDynList() {
    try {
      groupDynamicShowService.buildGroupDynList(form);
      // 群组角色状态 0 陌生人 没有申请 ， 1 ，陌生人 已经申请 ， 2 是群组普通成员 3 群组管理成员 4.群主
      Integer currentPsnRole = groupOptService.getRelationWithGroup(form.getCurrentPsnId(), form.getGroupId());
      form.setCurrentPsnRole(currentPsnRole);
    } catch (Exception e) {
      logger.error("获取群组动态列表出错,groupId=" + form.getGroupId(), e);
    }
    if ("more".equals(form.getFlag())) {
      return "groupdynlist";
    } else {
      return "groupdynmain";
    }

  }

  /**
   * 获取动态评论列表链接
   * 
   * @return
   */
  @Action("/dynweb/dynamic/groupdyn/ajaxcomments")
  public String ajaxGetCommentList() {
    try {
      groupDynamicShowService.getGroupDynCommentList(form);
    } catch (Exception e) {
      logger.error("获取群组动态评论列表列表出错,dynId=" + form.getDynId(), e);
    }
    return "dyncommentlist";
  }

  /**
   * 群组动态列表附带的评论
   * 
   * @return
   */
  @Action("/dynweb/dynamic/groupdyn/ajaxgetcommentsfordyn")
  public String ajaxGetCommentsForGroupDynList() {
    try {
      form.setMaxResults(1);
      groupDynamicShowService.getGroupDynCommentList(form);
    } catch (Exception e) {
      logger.error("获取群组动态列表附带的评论出错,groupId=" + form.getGroupId(), e);
    }
    return "dyncomment";
  }


  @Override
  public GroupDynShowForm getModel() {
    return form;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new GroupDynShowForm();
    }
  }
}
