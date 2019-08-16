package com.smate.web.data.action.pub;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.data.form.pub.PubEchartsForm;
import com.smate.web.data.model.pub.HKeywordsItem;
import com.smate.web.data.service.pub.PubEchartsService;

/**
 * 成果图表展示action
 * 
 * @author lhd
 *
 */
@Results({@Result(name = "pub_echarts", location = "/WEB-INF/jsp/pub/pub_echarts.jsp"),
    @Result(name = "pub_echarts_rcmd", location = "/WEB-INF/jsp/pub/pub_echarts_rcmd.jsp")})
public class PubEchartsAction extends ActionSupport implements ModelDriven<PubEchartsForm>, Preparable {

  private static final long serialVersionUID = -7667306929988859691L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private PubEchartsForm form;
  @Autowired
  private PubEchartsService pubEchartsService;
  @Value("${domainscm}")
  private String domainscm;

  /**
   * 成果关键词图谱展示
   * 
   * @return
   */
  @Action("/dataweb/pub/chartview")
  public String pubView() {
    try {
      if (form.getPubId() != null && form.getPubId() > 0L) {
        pubEchartsService.buildChartData(form);
      } else {
        Struts2Utils.getRequest().setAttribute("msg", "no");
        form.setShowMap(new HashMap<String, Object>());
      }
      Struts2Utils.getRequest().setAttribute("showMap", JacksonUtils.mapToJsonStr(form.getShowMap()));
    } catch (Exception e) {
      logger.error("成果关键词图谱展示出错:pubId= " + form.getPubId(), e);
    }
    return "pub_echarts";
  }

  /**
   * 成果关键词推荐图谱展示
   * 
   * @return
   */
  @Action("/dataweb/pub/pubrcmdview")
  public String pubRcmdView() {
    try {
      // 不允许在url中带参数直接访问,因为IE在url上带中文参数会乱码,而其他浏览器不会
      if (Struts2Utils.getParameter("devcode") == null) {
        form.setKeywords("");
      }
      if (StringUtils.isNotBlank(form.getKeywords())) {
        pubEchartsService.buildRcmdChartData(form);
      }
      if (CollectionUtils.isEmpty(form.getShowList())) {
        Struts2Utils.getRequest().setAttribute("msg", "no");
        form.setShowList(new ArrayList<HKeywordsItem>());
      }
      Struts2Utils.getRequest().setAttribute("showList", JacksonUtils.listToJsonStr(form.getShowList()));
    } catch (Exception e) {
      logger.error("成果关键词推荐图谱展示出错:keywords= " + form.getKeywords(), e);
    }
    return "pub_echarts_rcmd";
  }

  public String getDomainscm() {
    return domainscm;
  }

  public void setDomainscm(String domainscm) {
    this.domainscm = domainscm;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new PubEchartsForm();
    }

  }

  @Override
  public PubEchartsForm getModel() {
    return form;
  }

}
