package com.smate.web.dyn.action.data;


import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.dyn.form.dynamic.DynamicForm;
import com.smate.web.dyn.service.dynamic.DynamicService;

/**
 * 首页动态相关数据接口
 * 
 * @author wsn
 * @date Jun 18, 2019
 */
public class DynamicDataAction extends ActionSupport implements ModelDriven<DynamicForm>, Preparable {

  private static final long serialVersionUID = 7162092015945914048L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private DynamicForm form;
  @Autowired
  private DynamicService dynamicService;

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


  /**
   * 获取资源删除状态
   */
  @Action("/dyndata/res/status")
  public void checkResDeleteStatus() {
    int status = 0;
    String resultStatus = "error";
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      if (NumberUtils.isNotNullOrZero(form.getResType()) && NumberUtils.isNotNullOrZero(form.getResId())) {
        status = dynamicService.findResDeleteStatus(form.getResType(), form.getResId());
        resultStatus = "success";
      } else {
        resultStatus = "params error";
      }
    } catch (Exception e) {
      logger.error("获取资源删除状态异常， resId={}", form.getDes3ResId(), e);
    }
    result.put("status", resultStatus);
    result.put("hasDeleted", status);
    Struts2Utils.renderJson(result, "encoding:UTF-8");
  }



}
