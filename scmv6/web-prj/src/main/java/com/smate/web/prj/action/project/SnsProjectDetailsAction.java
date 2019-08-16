package com.smate.web.prj.action.project;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

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
import com.smate.core.base.project.model.Project;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.core.base.utils.struts2.Struts2MoveUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.prj.form.ProjectDetailsForm;
import com.smate.web.prj.model.common.PrjInfo;
import com.smate.web.prj.service.project.SnsProjectDetailsService;
import com.smate.web.prj.service.project.SnsProjectQueryService;

/**
 * 项目详情Action
 * 
 * @author zzx
 *
 */
@Results({@Result(name = "prj_detail", location = "/WEB-INF/jsp/prj/project/prj_detail.jsp"),
    @Result(name = "prj_detail_basicinfo", location = "/WEB-INF/jsp/prj/project/prj_detail_basicinfo.jsp"),
    @Result(name = "prj_detail_expenadd", location = "/WEB-INF/jsp/prj/project/prj_detail_expenadd.jsp"),
    @Result(name = "prj_detail_report", location = "/WEB-INF/jsp/prj/project/prj_detail_report.jsp"),
    @Result(name = "prj_detail_expenditure", location = "/WEB-INF/jsp/prj/project/prj_detail_expenditure.jsp"),
    @Result(name = "prj_detail_pubinfo", location = "/WEB-INF/jsp/prj/project/prj_detail_pubinfo.jsp"),
    @Result(name = "prj_report_list", location = "/WEB-INF/jsp/prj/project/prj_report_list.jsp"),
    @Result(name = "prj_pub_list", location = "/WEB-INF/jsp/prj/project/prj_pub_list.jsp"),
    @Result(name = "prj_pub_confirm_list", location = "/WEB-INF/jsp/prj/project/prj_pub_confirm_list.jsp"),
    @Result(name = "prj_pub_confirm", location = "/WEB-INF/jsp/prj/project/prj_pub_confirm.jsp"),
    @Result(name = "prjNotExit", location = "/WEB-INF/jsp/prj/project/prjNotExit.jsp"),
    @Result(name = "prjNoPrivacy", location = "/WEB-INF/jsp/prj/project/prjNoPrivacy.jsp"),
    @Result(name = "prj_outside_details", location = "/WEB-INF/jsp/prj/project/prj_outside_details.jsp"),
    @Result(name = "mobile_prj_reply", location = "/WEB-INF/jsp/prj/wechat/mobile_prj_reply.jsp"),
    @Result(name = "prj_comment", location = "/WEB-INF/jsp/prj/project/prj_details_comment.jsp")})
public class SnsProjectDetailsAction extends ActionSupport implements ModelDriven<ProjectDetailsForm>, Preparable {
  private static final long serialVersionUID = 7502217179720388833L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private ProjectDetailsForm form;
  @Autowired
  private SnsProjectDetailsService snsProjectDetailsService;
  @Autowired
  private SnsProjectQueryService snsProjectQueryService;
  @Value("${domainscm}")
  private String domainscm;

  /**
   * 站内项目详情页面
   */
  @Action("/prjweb/project/detailsshow")
  public String prjdetailsShow() {
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      // 方便返回上一步按钮的作用，应该将当前的访问的url存在session中
      String preUrl = Struts2Utils.getRequest().getHeader("Referer");
      if (StringUtils.isNotEmpty(preUrl) && preUrl.indexOf("/prjweb/prj/edit") == -1) {
        // 添加进栈中时，应该先清除栈中数据
        Struts2MoveUtils.reomvePreUrl(Struts2Utils.getRequest().getSession());
        // 保存之后仍然是在编辑界面，所以编辑界面的url不存在session中
        // 将上一个访问的url保存在session的栈中
        Struts2MoveUtils.cacheOwnerUrl(Struts2Utils.getRequest(), preUrl);
      }
      if (psnId == null || psnId == 0L) {
        // 说明用户未登录 需要直接跳转站外
        try {
          // 不编码字符串处理
          Struts2Utils.redirect(domainscm + "/prjweb/outside/project/detailsshow?des3PrjId="
              + URLEncoder.encode(form.getDes3PrjId().toString(), "utf-8"));
          return null;
        } catch (IOException e) {
          logger.error("跳转站外项目地址错误，prjId = ", form.getPrjId(), e);
        }
      }
      snsProjectDetailsService.showPrjDetails(form);
      // 设置页面的标题的title用于seo
      Locale currentLocale = LocaleContextHolder.getLocale();
      if (Locale.US.equals(currentLocale)) {
        String seoTitle = form.getTitle();
        seoTitle = HtmlUtils.htmlUnescape(seoTitle);
        seoTitle = seoTitle.replaceAll("<[^>]*>", "");
        form.setSeoTitle(StringUtils.trim(seoTitle) + " | SMate");
      } else {
        String seoTitle = form.getTitle();
        seoTitle = HtmlUtils.htmlUnescape(seoTitle);
        seoTitle = seoTitle.replaceAll("<[^>]*>", "");
        form.setSeoTitle(StringUtils.trim(seoTitle) + " | 科研之友");
      }
    } catch (Exception e) {
      logger.error("显示项目详情页面出错，prjId=", form.getPrjId(), e);
    }
    if ("notExists".equals(form.getResultMap().get("result"))) {
      return "prjNotExit";
    } else if ("noPrivacy".equals(form.getResultMap().get("result"))) {
      return "prjNoPrivacy";
    } else {
      return "prj_detail";
    }
  }

  /**
   * 加载项目详情界面的基本信息
   * 
   * @return
   */
  @Action("/prjweb/detail/ajaxprjinfo")
  public String loadDetailProjectInfo() {
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      form.setCurrentLocale(LocaleContextHolder.getLocale().toString());
      form.setResultMap(new HashMap<String, Object>());
      if (NumberUtils.isNullOrZero(form.getPrjId())) {
        form.getResultMap().put("msg", "error");
      } else {
        snsProjectDetailsService.loadDetailProjectInfo(form);
      }
    } catch (Exception e) {
      logger.error("加载项目基本信息出错！prjId={}", form.getPrjId(), e);
    }
    return "prj_detail_basicinfo";
  }

  /**
   * 加载项目详情界面的项目经费
   * 
   * @return
   */
  @Action("/prjweb/detail/ajaxprjexpenditure")
  public String loadDetailProjectExpenditure() {
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      form.setCurrentLocale(LocaleContextHolder.getLocale().toString());
      form.setResultMap(new HashMap<String, Object>());
      if (NumberUtils.isNullOrZero(form.getPrjId())) {
        form.getResultMap().put("msg", "error");
      } else {
        snsProjectDetailsService.loadDetailProjectExpenditure(form);
      }
    } catch (Exception e) {
      logger.error("加载项目经费列表出错！prjId={}", form.getPrjId(), e);
    }
    return "prj_detail_expenditure";
  }

  /**
   * 加载项目经费列表中的记一笔窗口
   * 
   * @return
   */
  @Action("/prjweb/detail/ajaxexpenadd")
  public String loadProjectExpenditureAdd() {
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      form.setCurrentLocale(LocaleContextHolder.getLocale().toString());
      if (NumberUtils.isNotNullOrZero(form.getPrjId())) {
        snsProjectDetailsService.loadProjectExpenditureAdd(form);
      }
    } catch (Exception e) {
      logger.error("加载项目经费列表中的记一笔窗口出错！prjId={}", form.getPrjId(), e);
    }
    return "prj_detail_expenadd";
  }

  /**
   * 加载支出记录列表
   */
  @Action("/prjweb/detail/ajaxloadexpenrecord")
  public void loadExpenRecord() {
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      if (NumberUtils.isNotNullOrZero(form.getPrjId())) {
        snsProjectDetailsService.loadExpenRecord(form);
      }
    } catch (Exception e) {
      logger.error("加载支出记录列表出错！prjId={}", form.getPrjId(), e);
    }
    Struts2Utils.renderJson(form.getExpenRecords(), "encoding:UTF-8");
  }

  /**
   * 删除支出记录列表
   */
  @Action("/prjweb/detail/ajaxdeleteexpenrecord")
  public void deleteExpenRecord() {
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      form.setResultMap(new HashMap<String, Object>());
      if (NumberUtils.isNotNullOrZero(form.getExpenRecordId())) {
        snsProjectDetailsService.deleteExpenRecord(form);
      } else {
        form.getResultMap().put("msg", "error");
      }
    } catch (Exception e) {
      logger.error("加载支出记录列表出错！prjId={}", form.getPrjId(), e);
      form.getResultMap().put("msg", "error");
    }
    Struts2Utils.renderJson(form.getResultMap(), "encoding:UTF-8");
  }

  /**
   * 记一笔窗口保存按钮
   * 
   * @return
   */
  @Action("/prjweb/detail/ajaxsaveexpen")
  public void saveProjectExpenditure() {
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      form.setResultMap(new HashMap<String, Object>());
      if (NumberUtils.isNullOrZero(form.getPrjId())) {
        form.getResultMap().put("msg", "error");
      } else {
        snsProjectDetailsService.saveProjectExpenditure(form);
        form.getResultMap().put("msg", "success");
      }
    } catch (Exception e) {
      logger.error("记一笔窗口保存操作出错！prjId={}", form.getPrjId(), e);
    }
    Struts2Utils.renderJson(form.getResultMap(), "encoding:UTF-8");
  }

  @Action("/prjweb/detail/ajaxexpenaccessory")
  public void loadExpenAccessory() {
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      form.setResultMap(new HashMap<String, Object>());
      if (NumberUtils.isNullOrZero(form.getExpenId())) {
        form.getResultMap().put("msg", "error");
      } else {
        snsProjectDetailsService.loadExpenAccessory(form);
      }
    } catch (Exception e) {
      logger.error("加载项目附件出错！expenId={}", form.getExpenId(), e);
    }
    Struts2Utils.renderJson(form.getAccessorys(), "encoding:UTF-8");
  }

  /**
   * 加载项目详情界面的项目成果
   * 
   * @return
   */
  @Action("/prjweb/detail/ajaxprjpubinfo")
  public String loadDetailProjectPubinfo() {
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      form.setCurrentLocale(LocaleContextHolder.getLocale().toString());
      form.setResultMap(new HashMap<String, Object>());
      if (NumberUtils.isNullOrZero(form.getPrjId())) {
        form.getResultMap().put("msg", "error");
      } else {
        snsProjectDetailsService.loadDetailProjectPubinfo(form);
      }
    } catch (Exception e) {
      logger.error("加载项目成果列表出错！prjId={}", form.getPrjId(), e);
    }
    return "prj_detail_pubinfo";
  }

  /**
   * 项目成果列表
   * 
   * @return
   */
  @Action("/prjweb/detail/ajaxprjpublist")
  public String loadDetailPrjPubList() {
    try {
      form.setCurrentLocale(LocaleContextHolder.getLocale().toString());
      form.setResultMap(new HashMap<String, Object>());
      if (NumberUtils.isNullOrZero(form.getPrjId())) {
        form.getResultMap().put("msg", "error");
      } else {
        form.setPsnId(SecurityUtils.getCurrentUserId());
        snsProjectDetailsService.loadDetailPrjPubList(form);
      }
    } catch (Exception e) {
      logger.error("加载项目报告列表出错！prjId={}", form.getPrjId(), e);
    }
    return "prj_pub_list";
  }

  @Action("/prjweb/detail/ajaxprjpubcount")
  public void loadDetailPrjPubCount() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (NumberUtils.isNullOrZero(form.getPrjId())) {
        map.put("msg", "error");
      } else {
        map = snsProjectDetailsService.loadDetailPrjPubCount(form);
      }
    } catch (Exception e) {
      logger.error("加载项目成果列表统计数出错！prjId={}", form.getPrjId(), e);
    }
    Struts2Utils.renderJson(map, "encoding:utf-8");
  }

  /**
   * 加载项目详情界面的项目报告
   * 
   * @return
   */
  @Action("/prjweb/detail/ajaxprjreport")
  public String loadDetailProjectReport() {
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      form.setCurrentLocale(LocaleContextHolder.getLocale().toString());
      form.setResultMap(new HashMap<String, Object>());
      if (NumberUtils.isNullOrZero(form.getPrjId())) {
        form.getResultMap().put("msg", "error");
      } else {
        snsProjectDetailsService.loadDetailProjectReport(form);
      }
    } catch (Exception e) {
      logger.error("加载项目报告列表出错！prjId={}", form.getPrjId(), e);
    }
    return "prj_detail_report";
  }

  @Action("/prjweb/detail/ajaxprjreportlist")
  public String loadDetailProjectReportList() {
    try {
      form.setCurrentLocale(LocaleContextHolder.getLocale().toString());
      form.setResultMap(new HashMap<String, Object>());
      if (NumberUtils.isNullOrZero(form.getPrjId())) {
        form.getResultMap().put("msg", "error");
      } else {
        snsProjectDetailsService.loadDetailProjectReportList(form);
      }
    } catch (Exception e) {
      logger.error("加载项目报告列表出错！prjId={}", form.getPrjId(), e);
    }
    return "prj_report_list";
  }

  @Action("/prjweb/detail/ajaxprjreportcount")
  public void loadDetailProjectReportCount() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (NumberUtils.isNullOrZero(form.getPrjId())) {
        map.put("msg", "error");
      } else {
        map = snsProjectDetailsService.loadDetailProjectReportCount(form);
      }
    } catch (Exception e) {
      logger.error("加载项目报告列表统计数出错！prjId={}", form.getPrjId(), e);
    }
    Struts2Utils.renderJson(map, "encoding:utf-8");
  }

  @Action("/prjweb/detail/ajaxupdatereportfile")
  public void ajaxUpdateReportFile() {
    Map<String, String> map = new HashMap<String, String>();
    try {
      Long reportId = form.getReportId();
      Long fileId = 0L;
      if (StringUtils.isNotBlank(form.getDes3FileId())) {
        fileId = NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3FileId()));
      }
      Long psnId = SecurityUtils.getCurrentUserId();
      if (reportId != 0L && fileId != 0L && psnId > 0L) {
        map = snsProjectDetailsService.uploadReportFile(reportId, fileId, psnId);
        map.put("result", "true");
      } else {
        map.put("result", "false");
        map.put("msg", "参数不正确");
      }
    } catch (Exception e) {
      logger.error("项目报告列表上传附件出错！prjId={}", form.getPrjId(), e);
      map.put("result", "false");
      map.put("downUrl", "");
    }
    Struts2Utils.renderJson(map, "encoding:utf-8");
  }

  @Action("/prjweb/detail/ajaxprjpubconfirm")
  public String loadDetailPrjPubConfirm() {
    try {
      form.setCurrentLocale(LocaleContextHolder.getLocale().toString());
      form.setResultMap(new HashMap<String, Object>());
      if (NumberUtils.isNullOrZero(form.getPrjId())) {
        form.getResultMap().put("msg", "error");
      } else {
        form.setPsnId(SecurityUtils.getCurrentUserId());
        snsProjectDetailsService.loadDetailPrjPubConfirm(form);
      }
    } catch (Exception e) {
      logger.error("加载项目成果列表成果匹配出错！prjId={}", form.getPrjId(), e);
    }
    if (form.getIsAll() == 1) {
      return "prj_pub_confirm_list";
    } else {
      return "prj_pub_confirm";
    }
  }

  @Action("/prjweb/detail/ajaxprjpubconfirmopt")
  public void ajaxPrjPubConfirmOpt() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (NumberUtils.isNullOrZero(form.getPrjId()) || NumberUtils.isNullOrZero(form.getPubId())) {
        map.put("result", "error");
      } else {
        map = snsProjectDetailsService.ajaxPrjPubConfirmOpt(form);
      }
    } catch (Exception e) {
      logger.error("项目成果列表成果匹配操作出错！prjId={},pubId={}", form.getPrjId(), form.getPubId(), e);
    }
    Struts2Utils.renderJson(map, "encoding:utf-8");
  }

  /**
   * 评论列表（站内站外均要使用）
   */
  @Action("/prjweb/project/ajaxcommentshow")
  public String prjCommentShow() {
    try {
      if (form.getPrjId() == null || form.getPrjId() == 0L) {
        form.setPrjId(Long.valueOf(Des3Utils.decodeFromDes3(form.getDes3PrjId())));
      }
      snsProjectDetailsService.showPrjComment(form);
    } catch (Exception e) {
      logger.error("显示项目评论页面出错，prjId=", form.getPrjId(), e);
    }
    return "prj_comment";
  }

  /**
   * mobile评论列表（站内站外均要使用）
   */
  @Action("/prjweb/outside/ajaxprjcommentshow")
  public String mobilePrjCommentShow() {
    try {
      if (form.getPrjId() == null || form.getPrjId() == 0L) {
        form.setPrjId(Long.valueOf(Des3Utils.decodeFromDes3(form.getDes3PrjId())));
      }
      snsProjectDetailsService.showPrjComment(form);
    } catch (Exception e) {
      logger.error("显示项目评论页面出错，prjId=", form.getPrjId(), e);
    }
    return "mobile_prj_reply";
  }

  /**
   * 添加评论
   */
  @Action("/prjweb/project/ajaxaddcomment")
  public void prjAddComment() {
    try {
      form.setResultMap(new HashMap<String, Object>());
      snsProjectDetailsService.prjAddComment(form);
    } catch (Exception e) {
      form.getResultMap().put("result", "error");
      logger.error("添加评论出错，prjId=", form.getPrjId(), e);
    }
    Struts2Utils.renderJson(form.getResultMap(), "encoding:UTF-8");
  }

  /**
   * 记录项目详情浏览记录
   */
  @Action("/prjweb/detail/ajaxsaveview")
  public void saveProjectViewRecord() {
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      snsProjectDetailsService.saveProjectView(form);
    } catch (Exception e) {
      logger.error("记录项目阅读记录出错，prjId=", form.getPrjId(), e);
    }
  }

  /**
   * 返回上一页的backUrl 目的在于：记住上一次访问的url，放置在session中，需要的时候往session中读取就可以
   */
  @Action("/prjweb/backurl")
  public void prjBackUrl() {
    String currentDomain = Struts2Utils.getRequest().getHeader("host");
    String defaultUrl = "/psnweb/homepage/show?module=prj";
    String tempDomain = domainscm;
    try {
      String forwardUrl = Struts2MoveUtils.backPreUrl(Struts2Utils.getRequest().getSession(), tempDomain + defaultUrl);
      // 页面的跳转
      Struts2Utils.redirect(forwardUrl);
    } catch (Exception e) {
      logger.error("返回上一页失败", "", e);
    }
  }

  /**
   * 站外项目详情页面
   */
  @Action("/prjweb/outside/project/detailsshow")
  public String prjoutsidedetailsShow() {
    try {
      snsProjectDetailsService.checkCurrentPsnLogin(form, domainscm);
      snsProjectDetailsService.showPrjDetails(form);
      // 设置页面的标题的title用于seo
      Locale currentLocale = LocaleContextHolder.getLocale();
      if (Locale.US.equals(currentLocale)) {
        String seoTitle = form.getTitle();
        seoTitle = HtmlUtils.htmlUnescape(seoTitle);
        seoTitle = seoTitle.replaceAll("<[^>]*>", "");
        form.setSeoTitle(StringUtils.trim(seoTitle) + " | SMate");
      } else {
        String seoTitle = form.getTitle();
        seoTitle = HtmlUtils.htmlUnescape(seoTitle);
        seoTitle = seoTitle.replaceAll("<[^>]*>", "");
        form.setSeoTitle(StringUtils.trim(seoTitle) + " | 科研之友");
      }
    } catch (Exception e) {
      logger.error("显示项目详情页面出错，prjId=", form.getPrjId(), e);
    }
    if ("notExists".equals(form.getResultMap().get("result"))) {
      return "prjNotExit";
    } else if ("noPrivacy".equals(form.getResultMap().get("result"))) {
      return "prjNoPrivacy";
    } else {
      return "prj_outside_details";
    }

  }

  /**
   * 获取项目详情(返回JSON)
   */
  @Action("/prjdata/project/getdetails")
  public void getDetailsPrj() {
    Long prjId = form.getPrjId();
    Map<String, String> result = new HashMap<String, String>();
    if (NumberUtils.isNotNullOrZero(prjId)) {
      try {
        Project project = snsProjectQueryService.getById(prjId);
        if (Objects.nonNull(project)) {
          result.put("title", project.getZhTitle());
          result.put("authorNames", project.getAuthorNames());
          result.put("briefDesc", project.getBriefDesc());
          result.put("status", "success");
          result.put("msg", "get data success");
        } else {
          result.put("status", "success");
          result.put("msg", "prj is not exists");
        }
      } catch (Exception e) {
        result.put("status", "error");
        result.put("msg", "system error");
      }
    } else {
      result.put("status", "error");
      result.put("msg", "prjId is null");
    }
    Struts2Utils.renderJson(result, "encoding:UTF-8");
  }

  /**
   * 获取与群组关联的项目信息
   */
  @Action("/prjdata/project/relationgrpinfo")
  public void getGrpRelationPrjInfo() {
    Map<String, Object> result = new HashMap<String, Object>();
    String status = "error";
    try {
      if (StringUtils.isNotBlank(form.getDes3GrpId())) {
        Long grpId = NumberUtils.parseLong(Des3Utils.decodeFromDes3(form.getDes3GrpId()), 0L);
        if (NumberUtils.isNotNullOrZero(grpId)) {
          PrjInfo prjInfo = snsProjectDetailsService.findGrpRelationPrjInfo(grpId);
          result.put("prjInfo", NumberUtils.isNullOrZero(prjInfo.getPrjId()) ? null : prjInfo);
          status = "success";
        }
      }
    } catch (Exception e) {
      logger.error("获取群组关联的项目信息异常， grpId={}, prjId={}, psnId={}", form.getDes3GrpId(), form.getPrjId(), form.getPsnId(),
          e);
    }
    result.put("status", status);
    Struts2Utils.renderJsonNoNull(result, "encoding:utf-8");
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new ProjectDetailsForm();
    }
  }

  @Override
  public ProjectDetailsForm getModel() {
    return form;
  }

}
