package com.smate.sie.web.application.action.analysis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.sie.web.application.cache.ApplicationCacheService;
import com.smate.sie.web.application.form.analysis.ProblemAnalysisForm;
import com.smate.sie.web.application.service.analysis.ProblemAnalysisService;
import com.smate.sie.web.application.service.analysis.Sie6InstitutionService;

/**
 * 开题分析
 * 
 * @author sjzhou
 *
 */
@Results({@Result(name = "to-maint", location = "/WEB-INF/jsp/problem-analysis/problem_analysis_main.jsp"),
    @Result(name = "to-analysis", location = "/WEB-INF/jsp/problem-analysis/problem_analysis_init_main.jsp")})
public class ProblemAnalysisAction extends ActionSupport implements ModelDriven<ProblemAnalysisForm>, Preparable {

  /**
   * 
   */
  private static final long serialVersionUID = -2988864848972807516L;
  private ProblemAnalysisForm form;
  @Autowired
  private ProblemAnalysisService problemAnalysisService;
  @Autowired
  private Sie6InstitutionService sie6InstitutionService;
  @Autowired
  private ApplicationCacheService applicationCacheService;

  /**
   * 进入页面.
   * 
   * @return
   * @throws Exception
   */
  @Action("/application/problem/analysis")
  public String initPage() throws Exception {
    return "to-analysis";
  }

  /**
   * 抽取关键词.
   * 
   * @return
   * @throws Exception
   */
  @Action("/application/problem/ajaxextract")
  public String extractKeywords() throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    List<Map<String, Object>> keyWordsList =
        problemAnalysisService.extractKeyWordsFormInfo(form.getTitle(), form.getSummary());
    if (keyWordsList != null) {
      applicationCacheService.remove("ANALYSIS_TITLE_SUMMARY_CACHE", "problem_analysis");
      applicationCacheService.put("ANALYSIS_TITLE_SUMMARY_CACHE", 60 * 60, "problem_analysis", form);
      map.put("keyWordsList", keyWordsList);
      form.setKeyWordsList(keyWordsList);
      map.put("result", SUCCESS);
    } else {
      map.put("result", NONE);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 跳到页面.
   * 
   * @return
   * @throws Exception
   */
  @Action("/application/problem/tomaint")
  public String extractKeywordsToMaint() throws Exception {
    if (StringUtils.isBlank(form.getTitle()) && StringUtils.isBlank(form.getSummary())) {
      form = (ProblemAnalysisForm) applicationCacheService.get("ANALYSIS_TITLE_SUMMARY_CACHE", "problem_analysis");
    }
    List<Map<String, Object>> keyWordsList =
        problemAnalysisService.extractKeyWordsFormInfo(form.getTitle(), form.getSummary());
    Struts2Utils.getRequest().setAttribute("title", form.getTitle());
    Struts2Utils.getRequest().setAttribute("summary", form.getSummary());
    Struts2Utils.getRequest().setAttribute("keyWordsList", keyWordsList);
    return "to-maint";
  }

  /**
   * 科研趋势.
   * 
   * @return
   * @throws Exception
   */
  @Action("/application/problem/ajaxtrend")
  public String researchTrendByKeyWords() throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> resultMap = problemAnalysisService.researchTrendByKeyWords(form.getKeyword());
    if (resultMap != null) {
      map.put("resultMap", resultMap);
      map.put("result", SUCCESS);
    } else {
      map.put("result", ERROR);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 相关学科.
   * 
   * @return
   * @throws Exception
   */
  @Action("/application/problem/ajaxrelateddis")
  public String relatedDisByKeyWords() throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> resultMap = problemAnalysisService.relatedDisByKeyWords(form.getKeyword());
    if (resultMap != null) {
      map.put("resultMap", resultMap);
      map.put("result", SUCCESS);
    } else {
      map.put("result", ERROR);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 相关学者.
   * 
   * @return
   * @throws Exception
   */
  @Action("/application/problem/ajaxresearchers")
  public String researchersByKeyWords() throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    List<Map<String, Object>> resultMap = problemAnalysisService.relatedResearchers(form.getKeyword());
    if (resultMap != null) {
      map.put("resultMap", resultMap);
      map.put("result", SUCCESS);
      form.setResearchersList(resultMap);
    } else {
      map.put("result", ERROR);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 相关单位.
   * 
   * @return
   * @throws Exception
   */
  @Action("/application/problem/ajaxintitutions")
  public String institutionsByKeyWords() throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> resultMap = problemAnalysisService.relatedIntitutions(form.getKeyword());
    if (resultMap != null) {
      map.put("resultMap", resultMap);
      map.put("result", SUCCESS);
    } else {
      map.put("result", ERROR);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 科研单位页签，跳转到对应单位
   * 
   * @return
   * @throws Exception
   */
  @Action("/application/problem/toinshomepage")
  public String toInsHomePage() throws Exception {
    String insName = Struts2Utils.getParameter("insNameStr");
    String domain = sie6InstitutionService.findInsPortalByInsName(insName);
    Struts2Utils.getResponse().sendRedirect("http://" + domain);
    return null;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new ProblemAnalysisForm();
    }
  }

  @Override
  public ProblemAnalysisForm getModel() {
    return form;
  }



}
