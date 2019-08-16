package com.smate.core.base.utils.app;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.smate.core.base.utils.constant.AppForm;
import com.smate.core.base.utils.constant.IOSHttpStatus;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;

/**
 * APP action工具类
 * 
 * @author LJ
 *
 *         2017年10月17日
 */
public class AppActionUtils {

  public static Object returnResult(AppForm form) {
    Map<String, Object> infoData = new HashMap<String, Object>();
    Map<String, Object> returnData = new HashMap<String, Object>();
    infoData.put("commentlist", form.getData() == null ? "" : form.getData());
    infoData.put("total", form.getAppTotal());
    returnData.put("results", infoData);
    returnData.put("status", form.getAppStatus());
    returnData.put("msg", form.getAppMsg());
    return returnData;
  }

  /**
   * @param data
   * @param total
   * @param status
   * @param msg
   */
  public static void doResult(AppForm form) {
    Map<String, Object> infoData = new HashMap<String, Object>();
    Map<String, Object> returnData = new HashMap<String, Object>();
    infoData.put("commentlist", form.getData());
    infoData.put("total", form.getAppTotal());
    returnData.put("results", infoData);
    returnData.put("status", form.getAppStatus());
    returnData.put("msg", form.getAppMsg());
    Struts2Utils.renderJsonNoNull(returnData, "encoding:utf-8");
  }

  /**
   * @param data
   * @param total
   * @param status
   * @param msg
   */
  public static void doResult(Object data, Integer total, String status, String msg) {
    Map<String, Object> infoData = new HashMap<String, Object>();
    Map<String, Object> returnData = new HashMap<String, Object>();
    infoData.put("commentlist", data);
    infoData.put("total", total);
    returnData.put("results", infoData);
    returnData.put("status", status);
    returnData.put("msg", msg);
    Struts2Utils.renderJsonNoNull(returnData, "encoding:utf-8");
  }

  /**
   * 构建返回给app端统一Json格式
   * 
   * @param data 返回数据
   * @param total 数目
   * @param status http 状态
   * @return
   */

  public static Map<String, Object> buildInfo(Object data, Integer total, String status) {
    Map<String, Object> infoData = new HashMap<String, Object>();
    Map<String, Object> returnData = new HashMap<String, Object>();
    infoData.put("commentlist", data);
    infoData.put("total", total);
    returnData.put("status", status);
    returnData.put("results", infoData);
    return returnData;

  }

  /**
   * 返回指定格式Json给app
   * 
   * @param data 返回数据
   * @param total 数目
   * @param status http 状态
   * @return
   */
  public static void renderAPPReturnJson(Object data, Integer total, String status) {
    Map<String, Object> infoData = new HashMap<String, Object>();
    Map<String, Object> returnData = new HashMap<String, Object>();
    infoData.put("commentlist", data);
    infoData.put("total", total);
    returnData.put("status", status);
    returnData.put("results", infoData);
    Struts2Utils.renderJsonNoNull(returnData, "encoding:utf-8");
  }

  /**
   * 返回指定格式Json给app(带HttpServletResponse)
   * 
   * @param data 返回数据
   * @param total 数目
   * @param status http 状态
   * @return
   */
  public static void renderAPPReturnJsonWithResponse(HttpServletResponse httpResponse, Object data, Integer total,
      String status) {
    Map<String, Object> infoData = new HashMap<String, Object>();
    Map<String, Object> returnData = new HashMap<String, Object>();
    infoData.put("commentlist", data);
    infoData.put("total", total);
    returnData.put("status", status);
    returnData.put("results", infoData);
    String jsonString = JacksonUtils.jsonObjectSerializerNoNull(returnData);
    Struts2Utils.renderwithResponse(httpResponse, "application/json", jsonString, "encoding:utf-8");
  }

  /**
   * 检查用户是否登陆
   * 
   * @return
   */
  public static boolean checkIsLogin() {
    if (SecurityUtils.getCurrentUserId() > 0L) {
      return true;
    }
    renderAPPReturnJson("Permission denied", 0, IOSHttpStatus.FORBIDDEN);
    return false;
  }

  /**
   * @param data
   * @param total
   * @param status
   * @param msg
   */
  public static String buildReturnInfo(Object data, Integer total, String status, String msg) {
    Map<String, Object> infoData = new HashMap<String, Object>();
    Map<String, Object> returnData = new HashMap<String, Object>();
    infoData.put("commentlist", data);
    infoData.put("total", total);
    returnData.put("results", infoData);
    returnData.put("status", status);
    returnData.put("msg", msg);
    return JacksonUtils.jsonObjectSerializerNoNull(returnData);
  }

  /**
   * @param data
   * @param total
   * @param status
   * @param msg
   */
  public static String buildReturnInfo(Object data, Integer total, String status) {
    Map<String, Object> infoData = new HashMap<String, Object>();
    Map<String, Object> returnData = new HashMap<String, Object>();
    infoData.put("commentlist", data);
    infoData.put("total", total);
    returnData.put("results", infoData);
    returnData.put("status", status);
    return JacksonUtils.jsonObjectSerializerNoNull(returnData);
  }

  /**
   * 转换返回值
   * 
   * @param result
   * @return
   */
  public static String changeResultStatus(String result) {
    switch (StringUtils.lowerCase(result)) {
      case "error":
        return IOSHttpStatus.INTERNAL_SERVER_ERROR;
      case "success":
        return IOSHttpStatus.OK;
      default:
        return IOSHttpStatus.INTERNAL_SERVER_ERROR;
    }
  }
}
