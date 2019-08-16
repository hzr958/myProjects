package com.smate.core.web.sys.security.xss;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.xss.XssUtils;

/**
 * xss拦截 spring mvc专用
 * 
 * @author tsz
 *
 * @date 2018年8月9日
 */
public class XssMVCInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {
    Map paramMap = request.getParameterMap();
    if (paramMap != null && paramMap.size() > 0) {
      Iterator iterator = paramMap.keySet().iterator();
      while (iterator.hasNext()) {
        Object key = iterator.next().toString();
        Object o = paramMap.get(key);
        request.setAttribute(key.toString(), transfer((String[]) o));
      }
    }
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {
    // TODO Auto-generated method stub

  }

  @SuppressWarnings("static-access")
  private String[] transfer(String[] params) throws Exception {
    for (int i = 0; i < params.length; i++) {
      if (StringUtils.isEmpty(params[i]))
        continue;
      if (JacksonUtils.isJsonObjectOrJsonArray(params[i])) {
        params[i] = this.transferJson(params[i]);
      } else {
        // 对参数值进行过滤.
        params[i] = XssUtils.filterByXssStr(params[i].trim());
      }

    }
    return params;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private static void transferJson(Map map) {
    Iterator<Map.Entry> it = map.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry entry = it.next();
      Object object = entry.getValue();
      if (object instanceof java.util.List) {
        transferJson((List) object);
      } else {
        if (entry.getValue() != null) {
          map.put(entry.getKey(), XssUtils.filterByXssStr(entry.getValue().toString()));
        }
      }
    }
  }

  @SuppressWarnings("rawtypes")
  private static void transferJson(List list) {
    for (Object object : list) {
      if (object instanceof java.util.Map) {
        transferJson((Map) object);
      } else {
        XssUtils.filterByXssStr(object.toString());
      }
    }
  }

  @SuppressWarnings("rawtypes")
  private static String transferJson(String string) {
    if (StringUtils.isEmpty(string)) {
      return string;
    }
    if (string.startsWith("[")) {
      List list = JacksonUtils.jsonToList(string);
      transferJson(list);
      return JacksonUtils.listToJsonStr(list);
    } else {
      Map map = JacksonUtils.jsonToMap(string);
      transferJson(map);
      return JacksonUtils.mapToJsonStr(map);
    }
  }

}
