package com.smate.core.base.utils.access.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;

/**
 * 记录系统访问日志
 * 
 * @author tsz
 *
 */
public class AccessLogInterceptor implements Interceptor {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 
   */
  private static final long serialVersionUID = 2485615646339819794L;

  @Override
  public void destroy() {

  }

  @Override
  public void init() {

  }

  @Override
  public String intercept(ActionInvocation invocation) throws Exception {

    try {
      // 获取请求参数. 注意不要修改到原始参数了。需要去掉引用传递
      Map<String, String[]> paramMap1 = Struts2Utils.getRequest().getParameterMap();
      Map<String, String[]> paramMap = new HashMap<String, String[]>();
      paramMap.putAll(paramMap1);
      // 参数超长就截断
      Map<String, Object> useMap = new HashMap<String, Object>();
      Map<String, Object> useParamMap = new HashMap<String, Object>();

      if (paramMap != null && paramMap.size() > 0) {
        Iterator<String> iterator = paramMap.keySet().iterator();
        while (iterator.hasNext()) {
          String key = iterator.next().toString();
          // 注意引用传递的问题
          String[] o = paramMap.get(key).clone();
          // 每个参数最长为200多了截断
          useParamMap.put(key, transfer(o));
        }
      }
      useMap.put("paramMap", useParamMap);
      // 请求连接
      String url = Struts2Utils.getRequest().getRequestURL().toString();
      String contextPath = Struts2Utils.getRequest().getContextPath();
      // 替换掉 上下文.因为真正的连接是 隐藏上下文的
      useMap.put("url", url.replace(contextPath, ""));
      // ip
      String userIP = Struts2Utils.getRemoteAddr();
      useMap.put("userIP", userIP);
      // currentpsnid 匿名用户为0
      Long currentPsnId = SecurityUtils.getCurrentUserId() == null ? 0L : SecurityUtils.getCurrentUserId();
      useMap.put("currentPsnId", currentPsnId.toString());
      // referer
      String referer = Struts2Utils.getHttpReferer();
      useMap.put("referer", referer);
      // 请求浏览器信息
      String userAgent = Struts2Utils.getRequest().getHeader("user-agent");
      useMap.put("userAgent", userAgent);
      // 请求方法
      String method = Struts2Utils.getRequest().getMethod();
      useMap.put("method", method); // get or post

      // 访问时间
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String date = formatter.format(new Date());
      useMap.put("accessDate", date);
      logger.info(JacksonUtils.mapToJsonStr(useMap));
    } catch (Exception e) {
      // 有异常就吃掉
    }

    return invocation.invoke();
  }

  @SuppressWarnings("static-access")
  private String[] transfer(String[] params) throws Exception {
    for (int i = 0; i < params.length; i++) {
      if (StringUtils.isEmpty(params[i]))
        continue;
      if (JacksonUtils.isJsonString(params[i])) {
        params[i] = this.transferJson(params[i]);
      } else {
        // 对参数值进行过滤.
        params[i] = dealParamt(params[i].trim());
      }

    }
    return params;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private void transferJson(Map map) {
    Iterator<Map.Entry> it = map.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry entry = it.next();
      Object object = entry.getValue();
      if (object instanceof java.util.List) {
        transferJson((List) object);
      } else {
        if (entry.getValue() != null) {
          map.put(entry.getKey(), dealParamt(entry.getValue().toString()));
        }
      }
    }
  }

  @SuppressWarnings("rawtypes")
  private void transferJson(List list) {
    for (Object object : list) {
      if (object instanceof java.util.Map) {
        transferJson((Map) object);
      } else {
        dealParamt(object.toString());
      }
    }
  }

  @SuppressWarnings("rawtypes")
  private String transferJson(String string) {
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

  /**
   * 处理参数 超过200就截断
   * 
   * @param str
   * @return
   */
  private String dealParamt(String o) {
    if (o == null) {
      return "";
    } else {
      return o.toString().length() > 200 ? o.toString().substring(0, 199) : o.toString();
    }
  }

}
