package com.smate.web.prj.action.project;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.service.psn.PsnSolrInfoModifyService;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.prj.form.ProjectQueryForm;
import com.smate.web.prj.service.project.SnsPrjOptService;
import com.smate.web.prj.service.project.SnsProjectQueryService;

/**
 * 项目
 * 
 * @author zX
 *
 */
@Results({@Result(name = "project", location = "/WEB-INF/jsp/prj/project/prj_list_main.jsp"),
    @Result(name = "prjlist", location = "/WEB-INF/jsp/prj/project/prj_list_main_sub.jsp"),
    @Result(name = "nofulltextprjlist", location = "/WEB-INF/jsp/prj/project/nofulltextlist.jsp"),
    @Result(name = "outsideprj", location = "/WEB-INF/jsp/prj/project/prj_outside_list.jsp"),
    @Result(name = "outsideshow", location = "/WEB-INF/jsp/prj/project/prj_outside_list_sub.jsp"),
    @Result(name = "prj_main", location = "/WEB-INF/jsp/prj/project/prj_main.jsp")})
public class SnsProjectAction extends ActionSupport implements ModelDriven<ProjectQueryForm>, Preparable {

  private static final long serialVersionUID = 8563038231920183493L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private ProjectQueryForm form;
  private Page page = new Page(10);
  @Autowired
  private SnsProjectQueryService snsProjectQueryService;
  @Autowired
  private PsnSolrInfoModifyService psnSolrInfoModifyService;
  @Autowired
  private SnsPrjOptService snsPrjOptService;

  /**
   * 项目主页
   */
  @Action("/prjweb/project/prjmain")
  public String prjMainShow() {
    try {
      Long currentId = SecurityUtils.getCurrentUserId();
      if (currentId != 0L) {
        form.setDes3CurrentId(Des3Utils.encodeToDes3(currentId.toString()));
      }
    } catch (Exception e) {
      logger.error(" 进入项目主页出错  form=" + form, e);
    }
    return "prj_main";
  }

  /**
   * 项目主页
   */
  @Action("/prjweb/project/ajaxshow")
  public String prjShow() {
    form.setCurrentYear(Calendar.getInstance().get(Calendar.YEAR));
    // 判断自己查看还是别人查看设置好权限
    buildForm(form);
    return "project";
  }

  /**
   * 项目列表
   */
  @Action("/prjweb/show/ajaxprjlist")
  public String prjShowList() {
    snsProjectQueryService.queryOutput(form, page);
    return "prjlist";

  }

  /**
   * 查询项目总数以及资金总数
   */
  @Action("/prjweb/remind/ajaxamount")
  public String ajaxRemindAmount() {
    buildForm(form);
    Map<String, Object> result = new HashMap<String, Object>();
    result = snsProjectQueryService.queryPrjNumAmount(form);
    // 为了解决中英文问题
    Locale locale = LocaleContextHolder.getLocale();
    if (Locale.US.equals(locale)) {
      result.put("locale", "en");
    } else {
      result.put("locale", "zh");
    }
    Struts2Utils.renderJson(result, "encoding:UTF-8");
    return null;
  }

  /**
   * 年份,资助机构数据回显
   */
  @Action("/prjweb/show/ajaxprjlistcallback")
  public String prjlistcallback() {
    try {
      // 查询资助机构
      buildForm(form);
      snsProjectQueryService.queryAgencyName(form);
      String jsonStr = snsProjectQueryService.getRecommendAgencyYear(form);
      Struts2Utils.renderJson(jsonStr, "encoding:UTF-8");
    } catch (Exception e) {
      logger.error("资助机构和年份的回显数据出错", e);
    }
    return null;
  }

  /**
   * 查询未上传全文数量
   */
  @Action("/prjweb/remind/ajaxfulltext")
  public String ajaxRemindFulltext() {
    List<Object> result = new ArrayList<Object>();
    Integer fulltextNum = snsProjectQueryService.findNoFullTextPrj();
    result.add(fulltextNum);
    // 为了解决中英文问题
    Locale locale = LocaleContextHolder.getLocale();
    if ("zh".equalsIgnoreCase(locale.getLanguage())) {
      result.add("zh");
    } else {
      result.add("en");
    }
    Struts2Utils.renderJson(result, "encoding:UTF-8");
    return null;
  }

  /**
   * ======= 资助机构列表
   */
  @Action("/prjweb/show/ajaxagencyname")
  public String prjAgencyName() {
    try {
      buildForm(form);
      snsProjectQueryService.queryAgencyName(form);
      Struts2Utils.renderJson(JacksonUtils.listToJsonStr(form.getAgencyNameList()), "encoding:utf-8");
    } catch (Exception e) {
      logger.error("资助机构列表查询出错,psnId= " + form.getPsnIds(), e);
    }
    return null;
  }

  /**
   * 站外项目主页
   */
  @Action("/prjweb/outside/ajaxshow")
  public String prjOutSideShow() {
    return "outsideprj";
  }

  /**
   * 站外查看项目列表
   */
  @Action("/prjweb/outside/ajaxprjlist")
  public String prjOutSide() {
    if (form.getDes3CurrentId() != null) {
      snsProjectQueryService.queryOutSidePrj(form, page);
    }
    return "outsideshow";
  }

  /**
   * >>>>>>> .merge-right.r139551 检查某个项目是否已经与某个群组建立了关系.
   * 
   * @return
   * @throws Exception
   */
  @Action("/prjweb/ajaxPrjGroupRel")
  public String ajaxPrjGroupRelation() throws Exception {
    Long groupId = snsProjectQueryService.findGroupIdByPrjId(form.getPrjId());
    if (groupId == null) {
      String result = "{\"result\":\"noRel\",\"msg\":\"" + "" + "\"}";
      Struts2Utils.renderJson(result, "encoding:UTF-8");
    } else {
      String result = "{\"result\":\"hasRel\",\"msg\":\"" + getText("project.group.hasRel.tip") + "\"}";
      Struts2Utils.renderJson(result, "encoding:UTF-8");
    }
    return null;
  }

  @Action("/prjweb/project/ajaxgetlanguage")
  public String ajaxgetLanguage() throws Exception {
    Locale locale = LocaleContextHolder.getLocale();
    String result = null;
    if ("zh".equalsIgnoreCase(locale.getLanguage())) {
      result = "zh";
    } else {
      result = "en";
    }
    Struts2Utils.renderJson(result, "encoding:UTF-8");
    return null;
  }

  /**
   * 项目和群组建立关联关系
   */
  @Action("/prjweb/ajaxPrjGrpRalation")
  public String ajaxPrjGrpGreateRalation() throws Exception {
    Map<String, String> map = new HashMap<String, String>();

    if (form.getGroupId() != null && form.getPrjId() != null) {
      String result = snsProjectQueryService.GreatePrjGrpRelation(form.getGroupId(), form.getPrjId());
      map.put("result", result);

    } else {
      map.put("result", "error");
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 获取项目全文下载权限
   * 
   * @return
   */
  @Action("/prjweb/project/ajaxfulltextauthority")
  public String getPrjFulltextAuthority() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (form.getPrjId() == null && StringUtils.isNotBlank(form.getDes3Id())) {
        String str = ServiceUtil.decodeFromDes3(form.getDes3Id());
        if (StringUtils.isNotBlank(str)) {
          form.setPrjId(Long.parseLong(str));
        }
      }
      if (form.getPrjId() != null && form.getPrjId() > 0l) {
        String authority = snsProjectQueryService.queryPrjFulltextAuthority(form.getPrjId());
        map.put("authority", authority);
      }
      Struts2Utils.renderJson(map, "encoding:UTF-8");
    } catch (Exception e) {
      logger.error("获取项目全文下载权限,出错, prjId");
    }
    return null;
  }

  /**
   * 项目删除
   */
  @Action("/prjweb/project/ajaxDelete")
  public String ajaxDelete() throws Exception {

    if (form.getPrjIds() != null) {

      snsProjectQueryService.deleteProject(form.getPrjIds());
      // geng
      snsPrjOptService.refreshPsnSolrInfoByTask(SecurityUtils.getCurrentUserId());
      String result = "{\"result\":\"success\",\"msg\":\"" + getText("project.remove.successful") + "\"}";
      Struts2Utils.renderJson(result, "encoding:UTF-8");
    } else {
      String result = "{\"result\":\"error\",\"msg\":\"" + getText("project.remove.failure") + "\"}";
      Struts2Utils.renderJson(result, "encoding:UTF-8");
    }

    return null;
  }

  /**
   * 项目删除
   */
  @Action("/prjweb/ajaxCheckPjr")
  public String ajaxCheckPjr() throws Exception {
    Map result = new HashMap();
    result.put("status", "true");
    if (form.getPrjId() != null) {
      try {
        Boolean flag = snsProjectQueryService.checkPrjExist(form);
        if (!flag) {
          result.put("status", "false");
        }
      } catch (Exception e) {
        logger.error("检查项目项目状态异常", e);
      }
    }
    Struts2Utils.renderJson(result, "encoding:UTF-8");

    return null;
  }

  /**
   * 构建参数
   * 
   * @param form
   */
  private void buildForm(ProjectQueryForm form) {
    Long psnId = SecurityUtils.getCurrentUserId();
    /* psnId=1000000733628L; */
    if (form.getDes3CurrentId() != null) {
      Long currentUserId = Long.valueOf(ServiceUtil.decodeFromDes3(form.getDes3CurrentId()));
      /* Long currentUserId= 1000000728363L; */
      if (!currentUserId.equals(psnId)) {
        form.setOthersSee(true);
      }
    }
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new ProjectQueryForm();
    }
  }

  @Override
  public ProjectQueryForm getModel() {
    // TODO Auto-generated method stub
    return form;
  }

  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
  }

}
