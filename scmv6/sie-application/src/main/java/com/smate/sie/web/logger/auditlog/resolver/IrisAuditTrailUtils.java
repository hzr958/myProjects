/**
 * Licensed to IRIS-System co. Copyright (C) 2014 IRIS, The IRIS Systems (Shenzhen) Limited.
 */
package com.smate.sie.web.logger.auditlog.resolver;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;

import com.smate.sie.web.logger.auditlog.utils.IrisJsonCompletion;
import com.smate.sie.web.logger.auditlog.utils.JsonUtils;



/**
 * 审计日志解析类.
 */
public class IrisAuditTrailUtils {

  /**
   * 获取普通文本日志内容.
   * 
   * @param resource
   * @return
   */
  public static Map<String, Object> getNormalValue(String resource) {
    Map<String, Object> data = new HashMap<String, Object>();
    if (NumberUtils.isDigits(resource)) {
      data.put("value", Long.valueOf(resource));
    } else if (NumberUtils.isNumber(resource)) {
      data.put("value", new BigDecimal(resource));
    } else {
      data.put("value", resource);
    }
    return data;
  }

  /**
   * 从Map字符串获取数据，返回Map或List.
   * 
   * @param resource 原始日志
   * @return
   * @throws Exception
   */
  public static Object getDataFromMapStr(String resource) throws Exception {
    resource = JsonUtils.formatMapToJson(resource);
    resource = IrisJsonCompletion.completeExpect(resource);
    return JsonUtils.parseJSON(resource);
  }

  /**
   * 从Json字符串获取数据，返回Map或List.
   * 
   * @param resource 原始日志
   * @return
   * @throws Exception
   */
  public static Object getDataFromJsonStr(String resource) throws Exception {
    resource = IrisJsonCompletion.completeExpect(resource);
    return JsonUtils.parseJSON(resource);
  }

  /**
   * 获取登录日志内容.
   * 
   * @param resource
   * @return
   */
  public static Map<String, Object> parseByLogin(String resource) {
    Map<String, Object> data = new HashMap<String, Object>();

    String[] resArr = resource.split("\\+");
    data.put("login_name", resArr[0]);
    data.put("role_id", resArr.length == 2 ? resArr[1] : "");

    return data;
  }
}
