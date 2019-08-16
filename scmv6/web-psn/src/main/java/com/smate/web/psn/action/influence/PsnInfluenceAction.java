package com.smate.web.psn.action.influence;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.common.CommonUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.influence.InfluenceForm;
import com.smate.web.psn.service.psninfluence.PsnInfluenceService;

@Results({@Result(name = "main", location = "/WEB-INF/jsp/influence/pc/influence_main.jsp"),
    @Result(name = "psn_statistics", location = "/WEB-INF/jsp/influence/pc/psn_influence_statistics.jsp"),
    @Result(name = "read_line", location = "/WEB-INF/jsp/influence/pc/psn_influence_visit_trend.jsp"),
    @Result(name = "visit_ins", location = "/WEB-INF/jsp/influence/pc/psn_influence_visit_ins.jsp"),
    @Result(name = "visit_pos", location = "/WEB-INF/jsp/influence/pc/psn_influence_visit_pos.jsp"),
    @Result(name = "hindex", location = "/WEB-INF/jsp/influence/pc/psn_influence_hindex.jsp")})
public class PsnInfluenceAction extends ActionSupport implements ModelDriven<InfluenceForm>, Preparable {
  private static final long serialVersionUID = -7823428996759426986L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private InfluenceForm form;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private PsnInfluenceService psnInfluenceService;

  @Actions({@Action("/psnweb/influence/ajaxmain"), @Action("/psnweb/outside/influence/ajaxmain")})
  public String psnInfluenceMain() {
    form.setDomainscm(domainscm);
    return "main";
  }

  /**
   * 阅读人员分布
   * 
   * @return
   */
  @Actions({@Action("/psnweb/influence/ajaxvisitmap"), @Action("/psnweb/outside/influence/ajaxvisitmap")})
  public String ajaxPsnVisitInfoForMap() {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    if (form.getPsnId() != null) {
      try {
        psnInfluenceService.getVisitMapData(form.getPsnId(), resultMap);
        resultMap.put("result", "success");
      } catch (ServiceException e) {
        logger.error("获取阅读人数记录出错，psnId=" + form.getPsnId(), e);
      }
    }
    Struts2Utils.renderJson(JacksonUtils.mapToJsonStr(resultMap), "encoding:UTF-8");
    return null;
  }

  /**
   * 单位分布
   * 
   * @return
   */
  @Actions({@Action("/psnweb/influence/ajaxvisitins"), @Action("/psnweb/outside/influence/ajaxvisitins")})
  public String ajaxVisitIns() {
    if (form.getPsnId() != null) {
      try {
        psnInfluenceService.getVisitIns(form);
      } catch (ServiceException e) {
        logger.error("获取单位分布出错，psnId=" + form.getPsnId(), e);
      }
    }
    return "visit_ins";
  }

  /**
   * 职称分布
   * 
   * @return
   */
  @Actions({@Action("/psnweb/influence/ajaxvisitpos"), @Action("/psnweb/outside/influence/ajaxvisitpos")})
  public String ajaxVisitPos() {
    if (form.getPsnId() != null) {
      try {
        psnInfluenceService.getVisitPos(form);
      } catch (ServiceException e) {
        logger.error("获取职称分布出错，psnId=" + form.getPsnId(), e);
      }
    }
    return "visit_pos";
  }

  /**
   * 获取人员各个统计数
   */
  @Actions({@Action("/psnweb/influence/ajaxstatistics"), @Action("/psnweb/outside/influence/ajaxstatistics")})
  public String ajaxFindPsnStatistics() {
    Map<String, String> result = new HashMap<String, String>();
    try {
      // TODO 校验人员ID等参数
      if (form.getPsnId() == null || CommonUtils.compareLongValue(form.getPsnId(), 0L)) {
        if (StringUtils.isNotBlank(form.getDes3PsnId())) {
          form.setPsnId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3PsnId())));
        }
      }
      psnInfluenceService.findPsnStatistics(form);
      result.put("result", "success");
      result.put("shareAndAwardSum", form.getAwardAndShareSum().toString());
      result.put("hindex", form.getHindex().toString());
      result.put("downLoadSum", form.getDownLoadSum().toString());
      result.put("visitSum", form.getVisitSum().toString());
      result.put("citedSum", form.getCitedSum().toString());
      result.put("frdSum", form.getFriendSum().toString());
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
  @Actions({@Action("/psnweb/influence/ajaxreadline"), @Action("/psnweb/outside/influence/ajaxreadline")})
  public String ajaxPsnReadLine() {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      // TODO 校验人员ID等参数
      if (form.getPsnId() == null || CommonUtils.compareLongValue(form.getPsnId(), 0L)) {
        if (StringUtils.isNotBlank(form.getDes3PsnId())) {
          form.setPsnId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3PsnId())));
        }
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
  @Actions({@Action("/psnweb/influence/ajaxpubcite"), @Action("/psnweb/outside/influence/ajaxpubcite")})
  public String pubCite() {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      // TODO 校验人员ID等参数
      if (form.getPsnId() == null || CommonUtils.compareLongValue(form.getPsnId(), 0L)) {
        if (StringUtils.isNotBlank(form.getDes3PsnId())) {
          form.setPsnId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3PsnId())));
        }
      }
      psnInfluenceService.findPubCiteTrend(form);
      result.put("result", "success");
      result.put("xAxisData", form.getXAxisData());
      result.put("yAxisData", form.getYAxisData());
      // result.put("citeSum", form.getCitedSum());
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

  /**
   * 获取人员hindex图所需信息
   * 
   * @return
   */
  @Actions({@Action("/psnweb/influence/ajaxhindex"), @Action("/psnweb/outside/influence/ajaxhindex")})
  public String ajaxPsnHindexInfo() {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      // TODO 校验人员ID等参数
      if (form.getPsnId() == null || CommonUtils.compareLongValue(form.getPsnId(), 0L)) {
        if (StringUtils.isNotBlank(form.getDes3PsnId())) {
          form.setPsnId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3PsnId())));
        }
      }
      psnInfluenceService.findPsnHindexInfo(form);
      result.put("result", "success");
      result.put("xAxisData", form.getXAxisData());
      result.put("yAxisData", form.getYAxisData());
      result.put("xHindex", form.getxHinex());
      result.put("yHindex", form.getyHindex());
      result.put("hindex", form.getHindex());
    } catch (Exception e) {
      logger.error("获取人员影响力页面H-index图数据出错， PsnId = " + form.getPsnId(), e);
      result.put("result", "error");
    }
    Struts2Utils.renderJson(result, "encoding:UTF-8");
    return null;
  }

  @Actions({@Action("/psnweb/influence/hindex/ajaxranking"), @Action("/psnweb/outside/influence/hindex/ajaxranking")})
  public String ajaxPsnHindexRanking() {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      // TODO 校验人员ID等参数
      if (form.getPsnId() == null || CommonUtils.compareLongValue(form.getPsnId(), 0L)) {
        if (StringUtils.isNotBlank(form.getDes3PsnId())) {
          form.setPsnId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3PsnId())));
        }
      }
      psnInfluenceService.findPsnHindexRanking(form);
      result.put("result", "success");
      result.put("ranking", form.getHindexRanking());
    } catch (Exception e) {
      logger.error("获取人员影响力页面hindex好友中排名出错， PsnId = " + form.getPsnId(), e);
      result.put("result", "error");
    }
    Struts2Utils.renderJson(result, "encoding:UTF-8");
    return null;
  }

  @Actions({@Action("/psnweb/influence/visit/ajaxranking"), @Action("/psnweb/outside/influence/visit/ajaxranking")})
  public String ajaxPsnVisitRanking() {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      // TODO 校验人员ID等参数
      if (form.getPsnId() == null || CommonUtils.compareLongValue(form.getPsnId(), 0L)) {
        if (StringUtils.isNotBlank(form.getDes3PsnId())) {
          form.setPsnId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3PsnId())));
        }
      }
      psnInfluenceService.findPsnVisitSumRanking(form);
      result.put("result", "success");
      result.put("ranking", form.getVisitSumRanking());
    } catch (Exception e) {
      logger.error("获取人员影响力页面访问数在好友中的排名出错， PsnId = " + form.getPsnId(), e);
      result.put("result", "error");
    }
    Struts2Utils.renderJson(result, "encoding:UTF-8");
    return null;
  }

  /**
   * 更新人员H-index
   * 
   * @return
   */
  @Action("/psnweb/outside/hindex/ajaxupdate")
  public String updatePsnHindex() {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      psnInfluenceService.updatePsnHindex(form);
      result.put("result", "success");
      result.put("hindex", form.getHindex());
    } catch (Exception e) {
      logger.error("检查更新人员H-index值出错, psnId=" + form.getPsnId(), e);
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
