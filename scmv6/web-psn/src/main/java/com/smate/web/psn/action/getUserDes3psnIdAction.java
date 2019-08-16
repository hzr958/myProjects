package com.smate.web.psn.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.struts2.Struts2Utils;

/**
 * 获取当前人加密的psnId的Action
 * 
 * @author lhd
 *
 */
public class getUserDes3psnIdAction extends ActionSupport {

  private static final long serialVersionUID = -5054743590362465755L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private String currentDes3PsnId;// 当前人加密的psnId

  public static void main(String[] args) {
    String ss = ServiceUtil.encodeToDes3(null);
    System.out.println(ss);
  }

  /**
   * 获取当前人加密的psnId
   * 
   * @return
   */
  @Actions({@Action("/psnweb/mobile/ajaxgetdes3psnid"), @Action("/psnweb/timeout/ajaxtest")})
  public String getCurrentUserDes3psnId() {
    try {
      Map<String, String> map = new HashMap<String, String>();
      currentDes3PsnId = ServiceUtil.encodeToDes3(SecurityUtils.getCurrentUserId() + "");
      map.put("result", currentDes3PsnId);
      Struts2Utils.renderJson(map, "encoding:UTF-8");
    } catch (Exception e) {
      logger.error("获取当前人加密的psnId出错", "当前人psnid=" + SecurityUtils.getCurrentUserId());
    }
    return null;
  }

  public String getCurrentDes3PsnId() {
    return currentDes3PsnId;
  }

  public void setCurrentDes3PsnId(String currentDes3PsnId) {
    this.currentDes3PsnId = currentDes3PsnId;
  }


}
