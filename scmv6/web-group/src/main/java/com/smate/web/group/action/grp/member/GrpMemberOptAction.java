package com.smate.web.group.action.grp.member;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.group.action.grp.form.GrpMemberForm;
import com.smate.web.group.constant.grp.GrpConstant;
import com.smate.web.group.dao.grp.grpbase.GrpBaseInfoDao;
import com.smate.web.group.service.grp.member.GrpMemberOptService;
import com.smate.web.group.service.grp.member.GrpRoleService;
import com.smate.web.group.service.grp.rcmd.GrpRcmdService;

/**
 * 群组成员操作Action
 * 
 * @author zzx
 *
 */
@Results({@Result(name = "save_error", location = "/WEB-INF/jsp/error/register_error.jsp")})
public class GrpMemberOptAction extends ActionSupport implements ModelDriven<GrpMemberForm>, Preparable {

  private static final long serialVersionUID = 2560561113264765201L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private GrpMemberOptService grpMemberOptService;
  @Autowired
  private GrpBaseInfoDao grpBaseInfoDao;
  @Autowired
  private GrpRcmdService grpRcmdService;
  @Autowired
  private GrpRoleService grpRoleService;
  private GrpMemberForm form;

  /**
   * 群组邀请成员上传邮件Excel模版
   * 
   * @return
   */
  @Action("/groupweb/grpmember/ajaxuploademailexceltemp")
  public void uploadEmailExcelTemp() {
    try {
      form.setResultMap(new HashMap<String, Object>());
      form.setEmailList(new ArrayList<String>());
      grpMemberOptService.uploadEmailExcelTemp(form);
    } catch (Exception e) {
      logger.error("群组邀请成员上传邮件Excel模版出错！", e);
      form.getResultMap().put("result", "error");
    }
    HttpServletResponse response = Struts2Utils.getResponse();
    response.setHeader("Charset", "UTF-8");
    response.setContentType("text/html;charset=UTF-8");
    try {
      response.getWriter().print(JacksonUtils.jsonObjectSerializer(form.getResultMap()));
    } catch (IOException e) {
    }
    // Struts2Utils.renderJson(form.getResultMap(), "encoding:UTF-8");
  }

  /**
   * 群组邀请成员下载邮件Excel模版
   * 
   * @return
   */
  @Action("/groupweb/grpmember/ajaxdownloademailexceltemp")
  public void downloadEmailExcelTemp() {
    try {
      grpMemberOptService.downloadEmailExcelTemp(form);
    } catch (Exception e) {
      logger.error("下载邮件Excel模版出错！", e);
    }
  }

  /**
   * 注册回调-----------
   * 
   * 目前有的功能： tempType=1,群组邀请站外人员，注册自动加入群组并跳转群组页面；
   * 
   * 有类似功能添加步骤： 1、设计RegisterTemp，主要是tempType、param； 2、在grpMemberOptService.doTempBack添加自己的case实现；
   * 3、保持整体结构；
   * 
   * @return
   */
  @Action("/groupweb/grpmember/ajaxregosterback")
  public String addGrp() {
    try {
      grpMemberOptService.doRegosterBack(form);
      Struts2Utils.getResponse().sendRedirect(form.getTargetUrl());
    } catch (Exception e) {
      logger.error("注册回调出错！", e);
      return "save_error";
    }
    return "save_error";
  }

  /**
   * 更新好友联系时间
   * 
   * @return
   */
  @Action("/groupweb/grpmember/ajaxupdateselecteddate")
  public String updateSelectedDate() {
    Map<String, String> map = new HashMap<String, String>();
    try {
      grpMemberOptService.updateSelectedDate(form);
      map.put("result", "success");
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("更新好友联系时间出错des3psnIds=" + form.getTargetPsnIds(), e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 设置群组角色
   * 
   * @return
   */
  @Action("/groupweb/grpmember/ajaxsetgrprole")
  public String setGrpRole() {
    Map<String, String> map = new HashMap<String, String>();
    try {
      Integer grpRole = grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId());
      if (grpRole == GrpConstant.GRP_ROLE_ADMIN || grpRole == GrpConstant.GRP_ROLE_OWNER) {
        grpMemberOptService.setGrpMemberRole(form);
        map.put("result", "success");
      } else {
        map.put("result", "error");
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("设置群组角色出错grpId=" + form.getGrpId() + ",psnId=" + form.getTargetPsnId(), e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 移除成员
   * 
   * @return
   */
  @Action("/groupweb/grpmember/ajaxdelgrpmember")
  public String delGrpMember() {
    Map<String, String> map = new HashMap<String, String>();
    boolean iszhCN = LocaleContextHolder.getLocale().toString().equals("zh_CN");
    try {
      if (grpRoleService.getGrpRoleForDelMember(form)) {
        if (form.getTargetRole() == GrpConstant.GRP_ROLE_OWNER) {
          map.put("result", iszhCN ? "不能移除群组拥有者" : "Can not remove group owner.");
        } else {
          grpMemberOptService.delGrpMember(form);
          grpMemberOptService.updateGrpMemberCount(form.getGrpId());
          map.put("result", "success");
        }
      } else {
        map.put("result", iszhCN ? "没有权限操作" : "You are not eligible to delete member.");
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("移除成员出错grpId=" + form.getGrpId() + ",psnId=" + form.getTargetPsnId(), e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;

  }

  /**
   * 退出群组
   * 
   * @return
   */
  @Action("/groupweb/grpmember/ajaxautoexitgrp")
  public String autoExitGrp() {
    Map<String, String> map = new HashMap<String, String>();
    boolean iszhCN = LocaleContextHolder.getLocale().toString().equals("zh_CN");
    try {
      Integer role = grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId());
      if (role == GrpConstant.GRP_ROLE_OWNER) {
        map.put("result", iszhCN ? "群组拥有者必须变更群组拥有者才能退出群组" : "You must change the group owner before quit.");
      } else if (role == 2 || role == 3) {
        form.setTargetPsnId(form.getPsnId());
        grpMemberOptService.delGrpMember(form);
        grpMemberOptService.updateGrpMemberCount(form.getGrpId());
        map.put("result", "success");
      } else {
        map.put("result", iszhCN ? "不是该群组成员" : "You are not the group member.");
      }

    } catch (Exception e) {
      map.put("result", "error");
      logger.error("退出群组出错grpId=" + form.getGrpId() + ",psnId=" + form.getTargetPsnId(), e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;

  }

  /**
   * 处理群组申请（接受/忽略）
   * 
   * @return
   */
  @Action("/groupweb/grpmember/ajaxdisposegrpapplication")
  public String disposeGrpApplication() {
    Map<String, String> map = new HashMap<String, String>();
    try {
      Integer role = grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId());
      if (role == GrpConstant.GRP_ROLE_OWNER || role == GrpConstant.GRP_ROLE_ADMIN) {
        grpMemberOptService.disposeGrpApplication(form);
        grpMemberOptService.updateGrpMemberCount(form.getGrpId());
        map.put("result", "success");
      } else {
        map.put("result", "error");
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("处理群组申请（接受/忽略）出错grpId=" + form.getGrpId() + ",psnId=" + form.getTargetPsnId(), e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;

  }

  /**
   * 处理群组邀请（接受/忽略）
   * 
   * @return
   */
  @Action("/groupweb/grpmember/ajaxivitegrpapplication")
  public String iviteGrpApplication() {
    Map<String, String> map = new HashMap<String, String>();
    try {
      grpMemberOptService.iviteGrpApplication(form);
      if (form.getDisposeType() == 1 && form.getGrpIds() != null && form.getGrpIds().size() > 0) {
        // 接受则更新群组人员数
        for (Long grpId : form.getGrpIds()) {
          grpMemberOptService.updateGrpMemberCount(grpId);
        }
      }
      map.put("result", "success");
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("处理群组邀请（接受/忽略）出错grpId=" + form.getGrpId() + ",psnId=" + form.getTargetPsnId(), e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;

  }

  /**
   * 邀请加入群组
   * 
   * @return
   */
  @Action("/groupweb/grpmember/ajaxinvitedjoingrp")
  public String invitedJoinGrp() {
    Map<String, String> map = new HashMap<String, String>();
    try {
      if (grpRoleService.IsisExistGrp(form.getGrpId())) {// 群组是否存在判断
        Integer role = grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId());
        if (role == GrpConstant.GRP_ROLE_ADMIN || role == GrpConstant.GRP_ROLE_MEMBER
            || role == GrpConstant.GRP_ROLE_OWNER) {// 判断邀请人权限
          grpMemberOptService.invitedJoinGrp(form);
          grpMemberOptService.updateGrpMemberCount(form.getGrpId());
          map.put("result", "success");
        }
      } else {
        map.put("result", "error");
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("邀请加入群组出错grpId=" + form.getGrpId() + ",psnId=" + form.getTargetPsnId(), e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");

    return null;
  }

  /**
   * 通过邮件邀请加入群组
   * 
   * @return
   */
  @Action("/groupweb/grpmember/ajaxinvitedjoingrpbyemail")
  public String invitedJoinGrpByEmail() {
    Map<String, String> map = new HashMap<String, String>();
    try {
      if (grpRoleService.IsisExistGrp(form.getGrpId())) {// 群组是否存在判断
        Integer role = grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId());
        if (role == 1 || role == 2 || role == 3) {// 判断邀请人权限
          grpMemberOptService.invitedJoinGrpByEmail(form);
          grpMemberOptService.updateGrpMemberCount(form.getGrpId());
          map.put("result", "success");
        }
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("通过邮件邀请加入群组出错grpId=" + form.getGrpId() + ",Emails=" + form.getEmails(), e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");

    return null;
  }

  /**
   * 申请/取消申请加入群组
   * 
   * @return
   */
  @Action("/groupweb/grpmember/ajaxapplyjoingrp")
  public String applyJoinGrp() {
    Map<String, String> map = new HashMap<String, String>();
    try {
      if (grpRoleService.IsisExistGrp(form.getGrpId())) {// 群组是否存在判断
        // isApplyJoinGrp 是否是申请加入群组1=申请加入群组，0=取消加群组
        if (form.getIsApplyJoinGrp() == 0) {
          grpMemberOptService.cancelJoinGrp(form);
        } else {
          grpMemberOptService.applyJoinGrp(form);
          grpMemberOptService.updateGrpMemberCount(form.getGrpId());
          map.put("role", grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId()).toString());
        }
        map.put("result", "success");
        map.put("addSuccess", form.getAddSuccess().toString());
      } else {
        map.put("result", "error");
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("申请/取消申请加入群组出错", e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
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
