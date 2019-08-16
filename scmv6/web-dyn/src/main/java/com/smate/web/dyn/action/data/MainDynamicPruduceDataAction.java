package com.smate.web.dyn.action.data;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.dyn.form.dynamic.DynamicForm;
import com.smate.web.dyn.service.dynamic.DynamicRealtimeService;
import com.smate.web.dyn.service.dynamic.DynamicShareService;
import com.smate.web.dyn.service.share.ShareStatisticsService;

/**
 * 首页动态数据接口
 * 
 * @author wsn
 * @date May 30, 2019
 */
public class MainDynamicPruduceDataAction implements Preparable, ModelDriven<DynamicForm> {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private DynamicForm form;
  @Autowired
  private DynamicRealtimeService dynamicRealtimeService;
  @Autowired
  private DynamicShareService dynamicShareService;
  @Autowired
  private ShareStatisticsService shareStatisticsService;


  /**
   * 实时动态
   */
  @Action("/dyndata/share/todyn")
  public void dynamicRealtime() {
    Map<String, Object> map = new HashMap<String, Object>();
    // TODO check param
    try {
      form.setPsnId(NumberUtils.parseLong(Des3Utils.decodeFromDes3(form.getDes3psnId()), 0L));
      dynamicRealtimeService.dynamicRealtime(form);
      map.put("status", "success");
    } catch (Exception e) {
      logger.error("生成动态数据出错,dynId=", e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
  }



  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new DynamicForm();
    }
  }

  @Override
  public DynamicForm getModel() {
    return form;
  }

}
