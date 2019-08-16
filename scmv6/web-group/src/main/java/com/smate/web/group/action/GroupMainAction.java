package com.smate.web.group.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.group.form.GroupInfoForm;
import com.smate.web.group.service.group.GroupInfoService;
import com.smate.web.group.service.group.GroupOptService;

/**
 * 群组MainAction
 * 
 * @author zk
 *
 */
@Results({@Result(name = "group_intro_main", location = "/WEB-INF/jsp/groupmodule/groupintromain.jsp"),
    @Result(name = "group_not_exist", location = "/WEB-INF/jsp/groupmodule/group_not_exist.jsp"),
    @Result(name = "group_file_main", location = "/WEB-INF/jsp/groupmodule/groupFileMain.jsp"),
    @Result(name = "group_file_list", location = "/WEB-INF/jsp/groupmodule/groupFileList.jsp"),
    @Result(name = "group_member_main", location = "/WEB-INF/jsp/groupmodule/groupMemberMain.jsp"),
    @Result(name = "group_member_list", location = "/WEB-INF/jsp/groupmodule/groupMemberList.jsp"),
    @Result(name = "group_pending_list", location = "/WEB-INF/jsp/groupmodule/groupPendingList.jsp")})
public class GroupMainAction extends ActionSupport implements Preparable, ModelDriven<GroupInfoForm> {

  private static final long serialVersionUID = -2627176744538549630L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private GroupInfoForm form; // 群组信息Form
  @Autowired
  private GroupInfoService groupInfoService;
  @Autowired
  private GroupOptService groupOptService;

  private String targetModule = "brief"; // 由url直接跳转至某个模块
  private String searchKey = "";// 邮件跳转到特定的列表

  /**
   * 群组简介
   * 
   * @return
   */
  @Action("/groupweb/groupmodule/ajaxintro")
  public String ajaxGroupIntro() {
    try {
      if (form.getGroupId() != null && form.getGroupId() != 0L) {
        groupInfoService.getGgroupIntroMain(form);
        return "group_intro_main";
      }
    } catch (Exception e) {
      logger.error("获取简介失败！", e);
    }
    return null;
  }

  /**
   * 用于判断group系统是否超时
   * 
   */
  @Action("/groupweb/ajaxtimeout")
  public String ajaxTimeout() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("result", "success");
    Struts2Utils.renderJson(JacksonUtils.mapToJsonStr(map), "encoding:UTF-8");
    return null;
  }

  /**
   * 群组文件模块
   * 
   * @return
   */

  @Action("/groupweb/groupmodule/ajaxgroupfile")
  public String ajaxGroupFile() {
    Long currentPsnId = SecurityUtils.getCurrentUserId();
    form.setPsnId(currentPsnId);
    form.setCurrentPsnGroupRoleStatus(groupOptService.getRelationWithGroup(form.getPsnId(), form.getGroupId()));
    return "group_file_main";
  }

  /**
   * 群组文件列表
   * 
   * @return
   */
  @Action("/groupweb/groupmodule/ajaxgroupfilelist")
  public String ajaxGroupFileList() {
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      groupInfoService.findGroupFile(form);
    } catch (Exception e) {
      logger.error("获取群组文件列表出错", e);
    }
    return "group_file_list";
  }

  /**
   * 群组成员模块
   * 
   * @return
   */
  @Action("/groupweb/groupmodule/ajaxmember")
  public String ajaxGroupMember() {
    try {
      if (form.getGroupId() != null && form.getGroupId() != 0L) {
        form.setPsnId(SecurityUtils.getCurrentUserId());
        groupInfoService.getCurrentPsnGroupRole(form);
      }
    } catch (Exception e) {
      logger.error("获取群组成员列表出错");
    }
    return "group_member_main";
  }

  /**
   * 群组成员列表
   * 
   * @return
   */
  @Action("/groupweb/groupmodule/ajaxmemberlist")
  public String ajaxMemberList() {
    try {
      if (form.getGroupId() != null && form.getGroupId() != 0L) {
        form.setCurrentPsnId(SecurityUtils.getCurrentUserId());
        groupInfoService.getMemberList(form);
      }
    } catch (Exception e) {
      logger.error("获取群组成员列表出错");
    }
    return "group_member_list";
  }

  /**
   * 群组成员待审核列表
   * 
   * @return
   */
  @Action("/groupweb/groupmodule/ajaxpendinglist")
  public String ajaxPendingList() {
    try {
      if (form.getGroupId() != null && form.getGroupId() != 0L) {
        // // 群组角色状态 0 陌生人 没有申请 ， 1 ，陌生人 已经申请 ， 2 是群组普通成员 3 群组管理成员 4.群主
        Integer relationWithGroup =
            groupOptService.getRelationWithGroup(SecurityUtils.getCurrentUserId(), form.getGroupId());
        if (relationWithGroup == 3 || relationWithGroup == 4) {
          groupInfoService.getPendingList(form);
        } else {
          logger.info("psnId=" + form.getPsnId() + "的用户没有权限访问groupId=" + form.getGroupId() + "的群组成员待审核列表");
          return null;
        }
      }
    } catch (Exception e) {
      logger.error("获取群组成员待审核列表出错");
    }
    return "group_pending_list";
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new GroupInfoForm();
    }
  }

  @Override
  public GroupInfoForm getModel() {
    return form;
  }

  public GroupInfoForm getForm() {
    return form;
  }

  public void setForm(GroupInfoForm form) {
    this.form = form;
  }

  public String getTargetModule() {
    return targetModule;
  }

  public void setTargetModule(String targetModule) {
    this.targetModule = targetModule;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

}
