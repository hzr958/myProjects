package com.smate.web.psn.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ActionSupport;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;

/**
 * 判断psn系统超时用
 * 
 * @author wsn
 *
 */
public class SessionTimeoutAction extends ActionSupport {

  private static final long serialVersionUID = 3797416237468889672L;

  /**
   * psn系统是否超时
   * 
   * @return
   */
  @Action("/psnweb/ajaxtimeout")
  public String sessionTimeout() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("result", "success");
    Struts2Utils.renderJson(JacksonUtils.mapToJsonStr(map), "encoding:UTF-8");
    return null;
  }
}
