package com.smate.core.web.sys.interceptor;

import java.lang.reflect.Method;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.core.web.annotation.RequestMethod;

/**
 * Struts2 自定义注解RequestMethod拦截器
 * 
 * @author ChuanjieHou
 * @date 2017年9月14日
 */
public class RequestMethodInterceptor extends AbstractInterceptor {
  private static final long serialVersionUID = -1359339863723956536L;

  @Override
  public String intercept(ActionInvocation actionInvocation) throws Exception {
    String methodName = actionInvocation.getProxy().getMethod();
    Method currentMethod = actionInvocation.getAction().getClass().getMethod(methodName, null);
    if (currentMethod.isAnnotationPresent(RequestMethod.class)) {
      RequestMethod reqMethodAnnotation = currentMethod.getAnnotation(RequestMethod.class);
      switch (reqMethodAnnotation.value()) {
        case RequestMethod.GET:
        case RequestMethod.HEAD:
        case RequestMethod.POST:
        case RequestMethod.PUT:
        case RequestMethod.DELETE:
        case RequestMethod.OPTIONS:
        case RequestMethod.PATCH:
        case RequestMethod.TRACE:
          if (reqMethodAnnotation.value().equals(Struts2Utils.getRequest().getMethod())) {
            return actionInvocation.invoke();
          } else {
            Struts2Utils.getResponse().sendError(405, "此方法只接收" + reqMethodAnnotation.value() + "请求！");
            return null;
          }
        case RequestMethod.DEFAULT:
          return actionInvocation.invoke();
        default:
          Struts2Utils.getResponse().sendError(405, "此方法只接收" + reqMethodAnnotation.value() + "请求！");
          return null;
      }
    }
    return actionInvocation.invoke();
  }

}
