package com.smate.web.psn.xss.interceptor;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.xss.XssUtils;

/**
 * XSS安全拦截器.
 * 
 * @author mjg
 *
 */
public class XssInterceptor implements Interceptor {

  private static final long serialVersionUID = -7938928482003760748L;

  public void destroy() {

  }

  public void init() {

  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public String intercept(ActionInvocation invocation) throws Exception {
    // tsz注入参数 如果没有参数 就不拦截
    // 获取请求参数.
    Map paramMap = invocation.getInvocationContext().getParameters();
    if (paramMap != null && paramMap.size() > 0) {
      Iterator iterator = paramMap.keySet().iterator();
      while (iterator.hasNext()) {
        Object key = iterator.next().toString();
        Object o = paramMap.get(key);
        paramMap.put(key, transfer((String[]) o));
      }
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


  public static void main(String[] args) {
    String string = "{\"name\":\"ts&z\",\"data\":[{\"id\":\"12&3\"},{\"id\":\"45&6\"}]}";
    // String string="[{\"id\":\"123&&\"},{\"id\":\"456\"}]";

    System.out.println(transferJson(string));
  }

}
