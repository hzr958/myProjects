package com.smate.web.group.action.grp.outside;

import java.net.URLEncoder;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
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
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.group.action.grp.form.GrpMainForm;
import com.smate.web.group.service.grp.manage.GrpBaseService;
import com.smate.web.group.service.grp.member.GrpMemberOptService;
import com.smate.web.group.service.grp.member.GrpRoleService;

/**
 * 群组主表action
 * 
 * @author tsz
 *
 */
@Results({@Result(name = "grp_outside_main", location = "/WEB-INF/jsp/grpoutside/main/grp_outside_main.jsp"),
    @Result(name = "grp_outside_info_is_private", location = "/WEB-INF/jsp/grp/grpmain/grp_info_is_private.jsp"),
    @Result(name = "grp_outside_not_exists", location = "/WEB-INF/jsp/grp/grpmain/grp_info_not_exists.jsp")})
public class GrpOutsideMainAction extends ActionSupport implements ModelDriven<GrpMainForm>, Preparable {
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
  private String domain;
  private String realUrlParamet;

  /**
   * 进入群组详情
   * 
   * @return
   */
  @Actions({@Action("/groupweb/grpinfo/outside/main")})
  public String mainGrp() {
    try {
      // 短地址跳转
      if (StringUtils.isNotBlank(this.getRealUrlParamet())) {
        GrpMainForm obj = JacksonUtils.jsonObject(this.getRealUrlParamet(), GrpMainForm.class);
        if (obj != null) {
          form.setDes3GrpId(obj.getDes3GrpId());
          form.setGrpId(obj.getGrpId());
        }
      }
      // 判断 如果已经登陆 直接重定向到 站内群组链接
      if (SecurityUtils.getCurrentUserId() != null && !SecurityUtils.getCurrentUserId().equals(0L)) {
        String forwardUrl =
            domain + "/groupweb/grpinfo/main?des3GrpId=" + URLEncoder.encode(form.getDes3GrpId(), "utf-8");
        // 跳转到集体的模块
        if (StringUtils.isNotBlank(form.getModel())) {
          forwardUrl = forwardUrl + "&model=" + form.getModel();
        }
        Struts2Utils.getResponse().sendRedirect(forwardUrl);
        return null;
      }
      if ("P".equals(grpBaseService.getCurrGrp(form.getGrpId()).getOpenType())) {
        return "grp_outside_info_is_private";
      } ;
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
      // GrpControl grpControl = grpBaseService.getCurrGrpControl(form.getGrpId());
      // GrpBaseinfo grpBaseinfo = grpBaseService.getCurrGrp(form.getGrpId());
      // form.setGrpBaseinfo(grpBaseinfo);
      // form.setFlag(grpBaseService.grpIsShowIndexOpen(grpBaseinfo, grpControl));
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
        return "grp_outside_not_exists";
      }
    } catch (Exception e) {
      LOGGER.error("进入群组详情出错grpId=" + form.getGrpId(), e);
      return "grp_outside_not_exists";
    }
    return "grp_outside_main";
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
    if (form.getGrpShowInfo().getGrpKeywordList() != null && form.getGrpShowInfo().getGrpKeywordList().size() > 0) {
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

  public static void main(String[] args) {
    GrpMainForm form = new GrpMainForm();
    form.setGrpId(100000002332736L);
    System.out.println(JacksonUtils.jsonObjectSerializer(form));
    GrpMainForm obj = JacksonUtils.jsonObject(JacksonUtils.jsonObjectSerializer(form), GrpMainForm.class);
  }

  public String getRealUrlParamet() {
    return realUrlParamet;
  }

  public void setRealUrlParamet(String realUrlParamet) {
    this.realUrlParamet = realUrlParamet;
  }
}
