package com.smate.web.psn.action.statistics;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.model.statistics.StatisticsForm;
import com.smate.web.psn.service.statistics.VistStatisticsService;

public class StatisticsAction extends ActionSupport implements ModelDriven<StatisticsForm>, Preparable {

  private static final long serialVersionUID = 523740667971457034L;
  private Logger logger = LoggerFactory.getLogger(getClass());
  private StatisticsForm form;

  @Autowired
  private VistStatisticsService vistStatisticsService;

  /**
   * 添加访问记录
   * 
   * @return
   * @throws Exception
   */
  @Action("/psnweb/outside/addVistRecord")
  public String addVistRecord() throws Exception {
    Map<String, String> result = new HashMap<String, String>();
    Long currentPsnId = SecurityUtils.getCurrentUserId();
    try {
      Long vistPsnId = null;
      Long actionKey = null;
      if (StringUtils.isNotBlank(form.getVistPsnDes3Id())) {
        vistPsnId = Long.valueOf(ServiceUtil.decodeFromDes3(form.getVistPsnDes3Id()));
      }
      if (StringUtils.isNotBlank(form.getActionDes3Key())) {
        actionKey = Long.valueOf(ServiceUtil.decodeFromDes3(form.getActionDes3Key()));
      }
      if (!currentPsnId.equals(vistPsnId) && vistPsnId != null && actionKey != null && form.getActionType() != null) {
        String ip = Struts2Utils.getRemoteAddr();
        vistStatisticsService.addVistRecord(currentPsnId, vistPsnId, actionKey, form.getActionType(), ip);
      }
      result.put("result", "success");
    } catch (Exception e) {
      logger.error("添加人员访问记录出错， currentPsnId = " + currentPsnId + ", 被查看人员ID = " + form.getVistPsnDes3Id()
          + ", 被查看资源ID = " + form.getActionDes3Key() + ", 被查看资源类型=" + form.getActionType(), e);
      result.put("result", "error");
    }
    Struts2Utils.renderJson(result, "encoding:UTF-8");
    return null;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new StatisticsForm();
    }
  }

  @Override
  public StatisticsForm getModel() {
    return form;
  }

}
