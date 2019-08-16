package com.smate.web.psn.action.mobile.influence;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.model.influence.InfluenceForm;
import com.smate.web.psn.service.psninfluence.PsnInfluenceService;

@Results({@Result(name = "main", location = "/WEB-INF/jsp/influence/mobile/mobile_psn_influence_main.jsp"),
    @Result(name = "psn_statistics", location = "/WEB-INF/jsp/influence/pc/psn_influence_statistics.jsp"),
    @Result(name = "read_line", location = "/WEB-INF/jsp/influence/pc/psn_influence_visit_trend.jsp"),
    @Result(name = "visit_ins", location = "/WEB-INF/jsp/influence/pc/psn_influence_visit_ins.jsp"),
    @Result(name = "visit_pos", location = "/WEB-INF/jsp/influence/pc/psn_influence_visit_pos.jsp"),
    @Result(name = "hindex", location = "/WEB-INF/jsp/influence/pc/psn_influence_hindex.jsp")})
public class MobilePsnInfluenceAction extends ActionSupport implements ModelDriven<InfluenceForm>, Preparable {

  private static final long serialVersionUID = -1197189579131344312L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private InfluenceForm form;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private PsnInfluenceService psnInfluenceService;

  @Action("/psnweb/outside/mobile/influence")
  public String psnInfluenceMain() {
    form.setDomainscm(domainscm);
    return "main";
  }

  /**
   * 获取人员各个统计数
   */
  @Action("/psnweb/outside/mobile/ajaxstatistics")
  public String ajaxFindPsnStatistics() {
    Map<String, String> result = new HashMap<String, String>();
    try {
      // TODO 校验人员ID等参数
      if (form.getPsnId() == null || form.getPsnId() == 0L) {
        form.setPsnId(SecurityUtils.getCurrentUserId());
      }
      psnInfluenceService.findPsnStatistics(form);
      result.put("result", "success");
      result.put("shareAndAwardSum", form.getAwardAndShareSum().toString());
      result.put("hindex", form.getHindex().toString());
      result.put("downLoadSum", form.getDownLoadSum().toString());
      result.put("visitSum", form.getVisitSum().toString());
      result.put("citedSum", form.getCitedSum().toString());
    } catch (Exception e) {
      logger.error("获取人员影响力页面统计数据出错， PsnId = " + form.getPsnId(), e);
      result.put("result", "error");
    }
    Struts2Utils.renderJson(result, "encoding:UTF-8");
    return null;
  }

  /**
   * 显示人员阅读趋势图
   * 
   * @return
   */
  @Action("/psnweb/outside/mobile/ajaxreadline")
  public String ajaxPsnReadLine() {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      // TODO 校验人员ID等参数
      if (form.getPsnId() == null || form.getPsnId() == 0L) {
        form.setPsnId(SecurityUtils.getCurrentUserId());
      }
      psnInfluenceService.findPsnResReadTrend(form);
      result.put("result", "success");
      result.put("xAxisData", form.getXAxisData());
      result.put("yAxisData", form.getYAxisData());
      result.put("maxCount", form.getMaxVisitCount());
    } catch (Exception e) {
      logger.error("获取人员影响力页面阅读趋势数据出错， PsnId = " + form.getPsnId(), e);
      result.put("result", "error");
    }
    Struts2Utils.renderJson(result, "encoding:UTF-8");
    return null;
  }

  /**
   * 显示成果引用数趋势图
   * 
   * @return
   */
  @Action("/psnweb/outside/mobile/ajaxpubcite")
  public String pubCite() {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      if (form.getPsnId() == null || form.getPsnId() == 0L) {
        form.setPsnId(SecurityUtils.getCurrentUserId());
      }
      psnInfluenceService.findPubCiteTrend(form);
      result.put("result", "success");
      result.put("xAxisData", form.getXAxisData());
      result.put("yAxisData", form.getYAxisData());
      result.put("citeSum", form.getCitedSum());
      result.put("frdSum", form.getFriendSum());
      result.put("citeRank", form.getCiteRank());
      result.put("hasCiteThead", form.getHasCiteThead());
      result.put("yMaxVal", form.getyMaxVal());
      result.put("yMinVal", form.getyMinVal());
    } catch (Exception e) {
      logger.error("获取显示成果引用数趋势图数据出错， psnId = " + form.getPsnId(), e);
      result.put("result", "error");
    }
    Struts2Utils.renderJson(result, "encoding:UTF-8");
    return null;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new InfluenceForm();
    }

  }

  @Override
  public InfluenceForm getModel() {
    return form;
  }

}
