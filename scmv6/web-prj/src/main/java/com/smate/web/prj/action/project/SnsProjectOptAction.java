package com.smate.web.prj.action.project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.consts.service.ConstDisciplineService;
import com.smate.core.base.exception.NoPermissionException;
import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.core.base.utils.struts2.Struts2MoveUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.core.web.annotation.RequestMethod;
import com.smate.web.prj.exception.ProjectNotExistException;
import com.smate.web.prj.form.ProjectOptForm;
import com.smate.web.prj.service.project.PrjInstitutionService;
import com.smate.web.prj.service.project.PrjSchemeAgencyService;
import com.smate.web.prj.service.project.PrjSchemeService;
import com.smate.web.prj.service.project.SnsPrjOptService;
import com.smate.web.prj.service.project.SnsPrjXmlService;
import com.smate.web.prj.vo.PrjInsVO;
import com.smate.web.prj.vo.PrjSchemeAgencyVO;
import com.smate.web.prj.vo.PrjSchemeVO;

/**
 * 项目新增、编辑操作页面控制器
 *
 * @author houchuanjie
 * @date 2018年3月20日 下午1:09:58
 */
@Results({@Result(name = "prjDiscipline", location = "/WEB-INF/jsp/prj/pc/v9edit/add_prj_discipline.jsp"),
    @Result(name = "prjEdit", location = "/WEB-INF/jsp/prj/pc/v9edit/edit.jsp"),
    @Result(name = "prjenter", location = "/WEB-INF/jsp/prj/pc/v9edit/edit.jsp"),
    @Result(name = "prjEdit2", location = "/WEB-INF/jsp/prj/pc/edit/edit.jsp"),
    @Result(name = "prjNotExit", location = "/WEB-INF/jsp/prj/project/prjNotExit.jsp"),
    @Result(name = "noPermission", location = "/WEB-INF/jsp/prj/project/notYourPrj.jsp"),
    @Result(name = "prjenter2", location = "/WEB-INF/jsp/prj/pc/enter/enter.jsp"),
    @Result(name = "ajaxLinkFile", location = "/WEB-INF/jsp/prj/project/file_choose_thickbox.jsp"),
    @Result(name = "saveSuccess", location = "${forwardUrl}",
        params = {"des3Id", "%{des3Id}", "saveSuccess", "%{saveSuccess}", "backType", "%{backType}"},
        type = "redirect")})
public class SnsProjectOptAction extends ActionSupport implements ModelDriven<ProjectOptForm>, Preparable {
  private static final long serialVersionUID = -7418511610193621173L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private ProjectOptForm form;
  private Page page;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private SnsPrjOptService snsPrjOptService;
  @Autowired
  private PrjSchemeAgencyService prjSchemeAgencyService;
  @Autowired
  private PrjSchemeService prjSchemeService;
  @Autowired
  private PrjInstitutionService prjInsService;
  @Autowired
  private SnsPrjXmlService snsPrjXmlService;
  @Autowired
  private ConstDisciplineService constDisciplineService;

  /**
   * 项目新增
   */
  @Action("/prjweb/prj/enter")
  public String enter() {
    try {
      // 方便返回上一步按钮的作用，应该将当前的访问的url存在session中
      String preUrl = Struts2Utils.getRequest().getHeader("Referer");
      if (StringUtils.isNotEmpty(preUrl) && preUrl.indexOf("/prjweb/prj/enter") == -1) {
        // 添加进栈中时，应该先清除栈中数据
        Struts2MoveUtils.reomvePreUrl(Struts2Utils.getRequest().getSession());
        // 保存之后仍然是在编辑界面，所以编辑界面的url不存在session中
        // 将上一个访问的url保存在session的栈中
        Struts2MoveUtils.cacheOwnerUrl(Struts2Utils.getRequest(), preUrl);
      }
      snsPrjOptService.bulidEnterFormData(form);
    } catch (Exception e) {
      logger.error("进入项目新增页面出错", e);
    }
    return "prjenter";
  }

  /**
   * 项目编辑
   */
  @Action("/prjweb/prj/edit")
  public String edit() {
    try {
      // 方便返回上一步按钮的作用，应该将当前的访问的url存在session中
      String preUrl = Struts2Utils.getRequest().getHeader("Referer");
      if (StringUtils.isNotEmpty(preUrl) && preUrl.indexOf("/prjweb/prj/edit") == -1) {
        // 添加进栈中时，应该先清除栈中数据
        Struts2MoveUtils.reomvePreUrl(Struts2Utils.getRequest().getSession());
        // 保存之后仍然是在编辑界面，所以编辑界面的url不存在session中
        // 将上一个访问的url保存在session的栈中
        Struts2MoveUtils.cacheOwnerUrl(Struts2Utils.getRequest(), preUrl);
      }
      snsPrjOptService.bulidEditFormData(form);
      return "prjEdit";
    } catch (ProjectNotExistException e) {
      logger.warn("构建项目编辑xml失败！项目不存在！", e);
      return "prjNotExit";
    } catch (NoPermissionException e) {
      return "noPermission";
    } catch (Exception e) {
      logger.error("显示项目详情页面出错，prjId=", form.getPrjId(), e);
    }
    return null;
  }

  @Action("/prjweb/prj/save")
  public String save() {
    Map map = new HashMap();
    try {
      Map<String, String[]> parameterMap = new HashMap<String, String[]>(Struts2Utils.getRequest().getParameterMap());
      map = checkProjectParam(parameterMap);
      if (map.size() == 0) {
        snsPrjOptService.saveEditFormData(form, parameterMap);
        form.setForwardUrl(domainscm + "/prjweb/prj/edit");
        form.setDes3Id(Des3Utils.encodeToDes3(Objects.toString(form.getPrjId())));
        form.setSaveSuccess(true);
        form.setBackType(form.getBackType());
        map.put("result", "success");
        map.put("des3PrjId", form.getDes3Id());
      } else {
        map.put("result", "error");
      }
    } catch (ProjectNotExistException e) {
      // return "prjNotExit";
      map.put("result", "prjNotExit");
    } catch (NoPermissionException e) {
      // return "noPermission";
      map.put("result", "noPermission");
    } catch (ServiceException e) {
      // throw e;
      map.put("result", "error");
    } catch (Exception e) {
      map.put("result", "error");
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  public Map checkProjectParam(Map parameterMap) {
    Map map = new HashMap();
    String[] titleZh = (String[]) parameterMap.get("/project/@zh_title");
    String[] titleEn = (String[]) parameterMap.get("/project/@en_title");
    String[] insName = (String[]) parameterMap.get("/project/@ins_name");
    if (!StringUtils.IsNotBlankObject(titleZh[0]) && !StringUtils.IsNotBlankObject(titleEn[0])) {
      map.put("title", "blank");
    }
    if (!StringUtils.IsNotBlankObject(insName[0])) {
      map.put("insName", "blank");
    }
    return map;
  }

  /**
   * 查询科技领域
   *
   * @return
   * @throws Exception
   */
  @Actions({@Action("/prjweb/prj/ajaxadddisciplines")})
  public String ajaxAddDiscipline() throws Exception {
    try {
      Integer disciplineId = constDisciplineService.dealDisciplineId(form.getDiscipline());
      form.setDiscipline(disciplineId);
      Map<String, List<Map<String, String>>> discData = constDisciplineService.findDiscData(form.getDiscipline());
      form.setDisciplineMap(discData);
    } catch (Exception e) {
      logger.error("查询项目科技领域出错", e);
      return null;
    }
    return "prjDiscipline";
  }

  /**
   * 获取项目资助机构列表
   *
   * @author houchuanjie
   * @date 2018年3月26日 上午11:19:47
   */
  @RequestMethod(RequestMethod.POST)
  @Action("/prjweb/prj/ajax-scheme-agencies")
  public void getSchemeAgencies() {
    List<PrjSchemeAgencyVO> resultList =
        prjSchemeAgencyService.searchPrjSchemeAgencies(form.getQ(), form.getLimit(), form.getLang());
    Struts2Utils.renderJsonList(resultList);
  }

  /**
   * 获取项目资助类别
   *
   * @author houchuanjie
   * @date 2018年3月26日 上午11:21:06
   */
  @RequestMethod(RequestMethod.POST)
  @Action("/prjweb/prj/ajax-schemes")
  public void getSchemes() {
    List<PrjSchemeVO> resultList =
        prjSchemeService.searchPrjSchemes(form.getQ(), form.getAgencyId(), form.getLimit(), form.getLang());
    Struts2Utils.renderJsonList(resultList);
  }

  /**
   * 获取项目依托单位
   *
   * @author houchuanjie
   * @date 2018年3月26日 上午11:23:33
   */
  @RequestMethod(RequestMethod.POST)
  @Action("/prjweb/prj/ajax-psnIns")
  public void getInstitutions() {
    List<PrjInsVO> resultList =
        prjInsService.searchPrjIns(form.getQ(), form.getLimit(), form.getExcludeParam(), form.getPsnId());
    Struts2Utils.renderJsonList(resultList);
  }

  @Action("/prjweb/project/ajaxprjaddaward")
  public void ajaxAddAward() {
    String awardPsnContent = "";
    try {
      if (form.getId() == null || form.getId() == 0L) {
        form.setId(Long.valueOf(Des3Utils.decodeFromDes3(form.getDes3Id())));
      }

      awardPsnContent = snsPrjOptService.prjAddAward(form);
    } catch (Exception e) {
      logger.error("添加赞出错，prjId=", form.getPrjId(), e);
    }
    Struts2Utils.renderJson(awardPsnContent, "encoding:utf-8");
  }

  @Action("/prjweb/project/ajaxprjcancelaward")
  public void ajaxCancelAward() {
    String awardPsnContent = "";
    try {
      if (form.getId() == null || form.getId() == 0L) {
        form.setId(Long.valueOf(Des3Utils.decodeFromDes3(form.getDes3Id())));
      }
      awardPsnContent = snsPrjOptService.prjCancelAward(form);
    } catch (Exception e) {
      logger.error("添加赞出错，prjId=", form.getPrjId(), e);
    }
    Struts2Utils.renderJson(awardPsnContent, "encoding:utf-8");
  }

  @Action("/prjweb/project/ajaxLinkFile")
  public String ajaxProjectFileList() {
    try {
      if (page == null) {
        page = new Page();
      }
      page = snsPrjXmlService.getPsnFileListInGroup(page);
    } catch (Exception e) {
      logger.error("群组查询个人文件出错，psnId=", SecurityUtils.getCurrentUserId(), e);
    }
    return "ajaxLinkFile";
  }

  @Action("/prjweb/project/ajaxCopyFile")
  public void ajaxPojectReplaceFile() throws Exception {
    HashMap<String, String> rsMap = new HashMap<String, String>();
    try {
      String fileIdStr = Struts2Utils.getParameter("fileId");

    } catch (Exception e) {
      rsMap.put("result", "error");
      rsMap.put("msg", "获取对应文件错误");
    }
    HttpServletResponse response = Struts2Utils.getResponse();
    response.setHeader("Charset", "UTF-8");
    response.setContentType("text/html;charset=UTF-8");
    response.getWriter().print(JacksonUtils.jsonObjectSerializer(rsMap));
  }

  @Override
  public void prepare() throws Exception {
    this.form = Optional.ofNullable(form).orElse(new ProjectOptForm());
  }

  @Override
  public ProjectOptForm getModel() {
    return form;
  }

  /**
   * @return form
   */
  public ProjectOptForm getForm() {
    return form;
  }

  /**
   * @param form 要设置的 form
   */
  public void setForm(ProjectOptForm form) {
    this.form = form;
  }

  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
  }

}
