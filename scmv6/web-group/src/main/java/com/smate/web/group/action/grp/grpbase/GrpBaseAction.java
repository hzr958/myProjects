package com.smate.web.group.action.grp.grpbase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.util.HtmlUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.consts.model.ConstKeyDisc;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.group.action.grp.form.GrpMainForm;
import com.smate.web.group.model.grp.grpbase.GrpBaseinfo;
import com.smate.web.group.model.grp.grpbase.GrpControl;
import com.smate.web.group.service.grp.manage.GrpBaseService;
import com.smate.web.group.service.grp.member.GrpMemberOptService;
import com.smate.web.group.service.grp.member.GrpRoleService;

/**
 * 群组主表action
 * 
 * @author AiJiangBin
 *
 */
@Results({@Result(name = "has_ivite_grp_list", location = "/WEB-INF/jsp/grp/grpmain/has_ivite_grp_list.jsp"),
    @Result(name = "grp_info_main", location = "/WEB-INF/jsp/grp/grpmain/grp_info_main.jsp"),
    @Result(name = "grp_info_not_exists", location = "/WEB-INF/jsp/grp/grpmain/grp_info_not_exists.jsp"),
    @Result(name = "grp_info_is_private", location = "/WEB-INF/jsp/grp/grpmain/grp_info_is_private.jsp"),
    @Result(name = "my_grp", location = "/WEB-INF/jsp/grp/grpmain/my_grp.jsp"),
    @Result(name = "select_my_grp", location = "/WEB-INF/jsp/grp/grpmain/select_grp_list.jsp"),
    @Result(name = "my_grp_list", location = "/WEB-INF/jsp/grp/grpmain/my_grp_list.jsp"),
    @Result(name = "second_discipline_list", location = "/WEB-INF/jsp/grp/grpmain/second_discipline_list.jsp"),
    @Result(name = "my_grp_sub", location = "/WEB-INF/jsp/grp/grpmain/my_grp_sub.jsp"),
    @Result(name = "main_grp_list", location = "/WEB-INF/jsp/grp/grpmain/main_grp_list.jsp"),
    @Result(name = "home_grpinvite_list", location = "/WEB-INF/jsp/grp/main/home_grpinvite_list.jsp"),
    @Result(name = "home_grpinvite", location = "/WEB-INF/jsp/grp/main/home_grpinvite.jsp"),
    @Result(name = "home_grpreq_list", location = "/WEB-INF/jsp/grp/main/home_grpreq_list.jsp"),
    @Result(name = "home_grpreq", location = "/WEB-INF/jsp/grp/main/home_grpreq.jsp")})
public class GrpBaseAction extends ActionSupport implements ModelDriven<GrpMainForm>, Preparable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
  private GrpMainForm form;
  @Autowired
  private GrpBaseService grpBaseService;
  @Autowired
  private GrpMemberOptService grpMemberOptService;
  @Autowired
  private GrpRoleService grpRoleService;
  @Value("${domainscm}")
  private String domainscm;

  /**
   * 加载待同意的被邀请群组列表
   * 
   * @return
   */
  @Action("/groupweb/mygrp/ajaxhasivitegrplist")
  public String showIviteGrp() {
    try {
      grpBaseService.getHasIviteGrpList(form);
    } catch (Exception e) {
      LOGGER.error("加载待同意的被邀请群组列表出错psnId=" + form.getPsnId(), e);
    }
    return "has_ivite_grp_list";
  }

  /**
   * 保存群组短地址
   * 
   * @return
   */
  @Action("/groupweb/mygrp/ajaxsavegrpshorturl")
  public String saveGrpShortUrl() {
    form.setResultMap(new HashMap<String, String>());
    boolean iszhCN = LocaleContextHolder.getLocale().toString().equals("zh_CN");
    try {
      Integer role = grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId());
      if (role == 1 || role == 2) {
        grpBaseService.saveGrpShortUrl(form);
      } else {
        form.getResultMap().put("result", "error");
        form.getResultMap().put("msg", iszhCN ? "没有权限操作" : "You are not eligible to operate group");
      }
    } catch (Exception e) {
      form.getResultMap().put("result", "error");
      form.getResultMap().put("msg", iszhCN ? "操作失败" : "Operate failed");
      LOGGER.error("保存群组短地址异常，form = " + form, e);
    }
    Struts2Utils.renderJson(form.getResultMap(), "encoding:UTF-8");
    return null;
  }

  /**
   * 显示我的群组页面
   * 
   * @return
   */
  @Action("/groupweb/mygrp/main")
  public String showMyGrp() {
    try {
      // 下面这个逻辑迁移至 ajaxmygrpsublist
      // grpBaseService.getHasGrpIviteInfo(form);
    } catch (Exception e) {
      LOGGER.error("显示我的群组页面出错psnId=" + form.getPsnId(), e);
    }
    return "my_grp";
  }

  /**
   * 我的群组模块
   */
  @Action("/groupweb/mygrp/ajaxmygrpsublist")
  public String ajaxMyGrpSubList() {
    try {
      grpBaseService.checkMyGrp(form);
      grpBaseService.getHasGrpIviteInfo(form);
    } catch (Exception e) {
      LOGGER.error("显示我的群组页面出错psnId=" + form.getPsnId(), e);
    }
    return "my_grp_sub";
  }

  /**
   * 获取我的群组列表
   * 
   * @return
   */
  @Action("/groupweb/mygrp/ajaxmygrplist")
  public String ajaxGetMyGrpList() {
    try {
      grpBaseService.getMyGrpInfoList(form);
    } catch (Exception e) {
      LOGGER.error("获取我的群组列表出错psnId=" + form.getPsnId(), e);
      return null;
    }
    return "my_grp_list";
  }

  /**
   * 群组置顶设置
   * 
   * @return
   */
  @Action("/groupweb/mygrp/ajaxmygrpsettop")
  public String ajaxMygrpSetTop() {
    Map<String, String> map = new HashMap<String, String>();
    try {
      grpBaseService.setGrpTop(form);
      map.put("result", "success");
    } catch (Exception e) {
      LOGGER.error("群组置顶设置grpId=" + form.getGrpId(), e);
      map.put("result", "error");
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 我的群组列表-回显
   * 
   * @return
   */
  @Action("/groupweb/mygrp/ajaxmygrplistcallback")
  public String ajaxGetMyGrpListCallBack() {
    try {
      grpBaseService.getMyGrpInfoListCallBack(form);
    } catch (Exception e) {
      LOGGER.error("我的群组列表-回显出错psnId=" + form.getPsnId(), e);
    }
    Struts2Utils.renderJson(form.getResult2Map(), "encoding:UTF-8");
    return null;
  }

  /**
   * 进入群组详情
   * 
   * @return
   */
  @Action("/groupweb/grpinfo/main")
  public String mainGrp() {
    try {
      if (form.getGrpId() == null) {// 兼容老的群组id--SCM-12519 --zzx --以后可以删除
        String des3GroupId = Struts2Utils.getParameter("des3GroupId");
        if (StringUtils.isNotBlank(des3GroupId)) {
          form.setGrpId(Long.parseLong(Des3Utils.decodeFromDes3(des3GroupId)));
        }
      }
      if (StringUtils.isNotBlank(form.getGroupCode())) {
        form.setGrpId(grpBaseService.getGrpIdByGroupCode(form.getGroupCode()));// 根据GroupCode获取Groupid
      }
      form.setRole(grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId()));
      GrpControl currGrpControl = grpBaseService.getCurrGrpControl(form.getGrpId());
      form.setGrpControl(currGrpControl);
      // 当前群组是否隐私
      GrpBaseinfo currGrpBaseInfo = grpBaseService.getCurrGrp(form.getGrpId());
      boolean flag = true;
      if (form.getRole() == 1 || form.getRole() == 2 || form.getRole() == 3) {
        flag = false;
      }
      if (flag) {
        if ("P".equals(currGrpBaseInfo.getOpenType())) {
          return "grp_info_is_private";
        }
      }
      if (grpRoleService.checkRoleVisitGrp(form.getPsnId(), form.getGrpId())) {
        // 更新访问时间
        grpMemberOptService.updateVisitDate(form.getPsnId(), form.getGrpId());
        // 获取群组详情信息
        form.setIsViewGrpDetail("1");
        grpBaseService.getGrpInfo(form);
        if (form.getGrpShowInfo().getRole() == 9) {// 群组外成员才判断开放权限
          if (form.getGrpShowInfo().getGrpControl().getIsIndexDiscussOpen().equals("1")) {
            form.setGrpDescription(grpBaseService.getGrpDesc(form.getGrpId()));
          }
        } else {
          form.setGrpDescription(grpBaseService.getGrpDesc(form.getGrpId()));
        }
        buildSeoKeywordsAndTitle(form);
      } else {// 返回群组不存在 或者没有权限访问页面
        return "grp_info_not_exists";
      }
    } catch (Exception e) {
      LOGGER.error("进入群组详情出错grpId=" + form.getGrpId(), e);
      return null;
    }
    return "grp_info_main";
  }

  /**
   * 删除群组
   * 
   * @return
   */
  @Action("/groupweb/mygrp/ajaxdelmygrp")
  public String ajaxDelMyGrp() {
    Map<String, String> map = new HashMap<String, String>();
    try {
      Integer role = grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId());
      if (role == 1) {
        grpBaseService.delMyGrp(form);
        map.put("result", "success");
      } else {
        map.put("result", "error");
      }
    } catch (Exception e) {
      map.put("result", "error");
      LOGGER.error("删除群组出错grpId=" + form.getGrpId(), e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 修改群组模块公开权限
   * 
   * @return
   */
  @Action("/groupweb/mygrp/ajaxmodifygrppermissions")
  public String ajaxModifyGrpPermissions() {
    Map<String, String> map = new HashMap<String, String>();
    try {
      Integer role = grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId());
      if (role == 1 || role == 2) {
        grpBaseService.modifyGrpPermissions(form);
        map.put("result", "success");
      } else {
        map.put("result", "error");
      }
    } catch (Exception e) {
      map.put("result", "error");
      LOGGER.error("修改群组模块公开权限出错grpId=" + form.getGrpId(), e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 分享 选择群组列表
   * 
   * @return
   */
  @Action("/groupweb/share/ajaxselectgrplist")
  public String selectGrpList() {
    try {
      grpBaseService.getAllMyGrp(form);
    } catch (Exception e) {
      LOGGER.error("加载选择群组列表出错", e);
      return null;
    }
    return "select_my_grp";
  }

  /**
   * 自动填充群组名字
   * 
   * @return
   */
  @Action("/groupweb/mygrp/ajaxautogrpnames")
  public String ajaxAutoGrpNames() {
    try {
      List<GrpBaseinfo> groupNames = grpBaseService.getGroupNames(form);
      if (groupNames != null && groupNames.size() > 0) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (GrpBaseinfo g : groupNames) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("code", Des3Utils.encodeToDes3(g.getGrpId().toString()));
          map.put("name", StringEscapeUtils.unescapeHtml4(g.getGrpName()));
          list.add(map);
        }
        Struts2Utils.renderJson(JacksonUtils.listToJsonStr(list), "encoding:UTF-8");
      }
    } catch (Exception e) {
      LOGGER.error("自动填充群组名字出错", e);
    }
    return null;
  }

  /**
   * 自动填充学科关键词
   * 
   * @return
   */
  @Action("/groupweb/mygrp/ajaxautoconstkeydiscs")
  public String ajaxAutoConstKeyDiscs() {
    try {
      List<ConstKeyDisc> constKeyDiscList = grpBaseService.getConstKeyDiscs(form);
      if (constKeyDiscList != null && constKeyDiscList.size() > 0) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (ConstKeyDisc c : constKeyDiscList) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("code", c.getDiscCodes());
          map.put("name", c.getKeyWord());
          map.put("keyId", c.getId().toString());
          list.add(map);
        }
        Struts2Utils.renderJson(JacksonUtils.listToJsonStr(list), "encoding:UTF-8");
      }
    } catch (Exception e) {
      LOGGER.error("自动填充学科关键词", e);
    }
    return null;
  }

  /**
   * 自动填充学科关键词
   * 
   * @return
   */
  @Action("/groupweb/mygrp/ajaxautoconstkeydiscscodeid")
  public String ajaxAutoConstKeyDiscsCodeId() {
    try {
      List<ConstKeyDisc> constKeyDiscList = grpBaseService.getConstKeyDiscs(form);
      if (constKeyDiscList != null && constKeyDiscList.size() > 0) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (ConstKeyDisc c : constKeyDiscList) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("code", c.getId().toString());
          map.put("name", c.getKeyWord());
          list.add(map);
        }
        Struts2Utils.renderJson(JacksonUtils.listToJsonStr(list), "encoding:UTF-8");
      }
    } catch (Exception e) {
      LOGGER.error("自动填充学科关键词", e);
    }
    return null;
  }

  /**
   * 获取一级研究领域
   * 
   * @return
   * @throws Exception
   */
  @Action(value = "/groupweb/mygrp/ajaxgetseconddiscipline")
  public String getDisciplineList() {
    try {
      grpBaseService.getSecondDisciplineListById(form);
    } catch (Exception e) {
      LOGGER.error("获取学科领域失败,fCategoryId=" + form.getfCategoryId(), e);
      return null;
    }
    return "second_discipline_list";
  }

  /**
   * 科研之友主页获取群组列表
   * 
   * @return
   */
  @Action(value = "/groupweb/mygrp/ajaxgrplistformain")
  public String grpListForMain() {
    try {
      grpBaseService.grpListForMain(form);
    } catch (Exception e) {
      LOGGER.error("科研之友主页获取群组列表出错", e);
    }
    return "main_grp_list";
  }

  /**
   * 首页-显示群组请求模块
   * 
   * @author zzx
   * @return
   */
  @Action("/groupweb/mainmodule/ajaxgetgrpreq")
  public String getTomoduleGrpReq() {
    form.setPsnId(SecurityUtils.getCurrentUserId());
    try {
      grpBaseService.queryGrpReq(form);
      if (CollectionUtils.isEmpty(form.getGrpShowInfoList())) {
        Struts2Utils.getRequest().setAttribute("randomModule", "no");
      }
    } catch (Exception e) {
      LOGGER.error("首页-显示好友请求模块出错,psnId= " + form.getPsnId(), e);
    }
    if (form.getIsAll() == 1) {
      return "home_grpreq_list";
    }
    return "home_grpreq";
  }

  /**
   * 首页-显示群组邀请模块
   * 
   * @author zzx
   * @return
   */
  @Action("/groupweb/mainmodule/ajaxgetgrpinvite")
  public String getTomoduleGrpInvite() {
    form.setPsnId(SecurityUtils.getCurrentUserId());
    try {
      grpBaseService.queryGrpInvite(form);
      if (CollectionUtils.isEmpty(form.getGrpShowInfoList())) {
        Struts2Utils.getRequest().setAttribute("randomModule", "no");
      }
    } catch (Exception e) {
      LOGGER.error("首页-显示好友请求模块出错,psnId= " + form.getPsnId(), e);
    }
    if (form.getIsAll() == 1) {
      return "home_grpinvite_list";
    }
    return "home_grpinvite";
  }

  /**
   * 动态首页需要用到的数据
   * 
   * @return
   */
  @Action("/groupweb/mygrp/ajaxFindGrpInviteRegInfo")
  public String ajaxFindGrpInviteRegInfo() {
    Map<String, String> map = new HashMap<>();
    try {
      grpBaseService.getHasGrpIviteInfo(form);
      map.put("hasReqGrp", form.getHasReqGrp());
      map.put("hasIvite", form.getHasIvite());
    } catch (Exception e) {
      LOGGER.error("显示我的群组页面出错psnId=" + form.getPsnId(), e);
    }
    Struts2Utils.renderJson(JacksonUtils.mapToJsonStr(map), "encoding:UTF-8");
    return null;
  }

  /**
   * 构建seo标签使用的关键词串
   * 
   * @param form
   */
  private void buildSeoKeywordsAndTitle(GrpMainForm form) {
    StringBuilder sb = new StringBuilder();
    Locale locale = LocaleContextHolder.getLocale();
    String splitTag = locale.equals(locale.US) ? ", " : "，";
    if (form.getGrpShowInfo() != null && form.getGrpShowInfo().getGrpKeywordList() != null
        && form.getGrpShowInfo().getGrpKeywordList().size() > 0) {
      for (String keywords : form.getGrpShowInfo().getGrpKeywordList()) {
        sb.append(splitTag + keywords);
      }
      form.setGrpKeywords(locale.equals(locale.US) ? (sb.toString()).substring(2) : (sb.toString()).substring(1));
    }
    String grpName = form.getGrpShowInfo().getGrpBaseInfo().getGrpName();
    if (StringUtils.isNoneBlank(grpName)) {
      form.setSeoTitle(
          HtmlUtils.htmlUnescape(grpName) + " | " + ("en_US".equals(locale.toString()) ? "Smate" : "科研之友"));
    }

  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new GrpMainForm();
    }
    if (form.getPage() == null) {
      form.setPage(new Page());
    }
  }

  @Override
  public GrpMainForm getModel() {
    return form;
  }
}
