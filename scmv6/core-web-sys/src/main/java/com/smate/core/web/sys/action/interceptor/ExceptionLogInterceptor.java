package com.smate.core.web.sys.action.interceptor;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;

/**
 * 错误记录拦截器 记录用户操作出错信息 继承自AbstractInterceptor.
 * 
 * @author zk
 *
 */
public class ExceptionLogInterceptor extends AbstractInterceptor {

  private static final long serialVersionUID = -6153263768867359067L;

  protected final Log log = LogFactory.getLog(ExceptionLogInterceptor.class);

  @Override
  public String intercept(ActionInvocation invocation) throws Exception {

    // 返回结果
    String returnValue = null;
    try {
      returnValue = invocation.invoke();
    } catch (Exception e) {

      // 安全环境
      Long userId = SecurityUtils.getCurrentUserId();
      // 出错日志
      log.error(" Exception : " + e.getMessage() + " , user : [ " + userId + " ] , class : "
          + invocation.getProxy().getAction().getClass() + ", method :" + invocation.getProxy().getMethod()
          + ", time : " + (new Date()), e);

      String uri = Struts2Utils.getRequest().getRequestURI();
      Struts2Utils.getRequest().setAttribute("action_request_uri", uri);
      // 把错误抛出,由struts2 的错误处理框架完成后续处理
      throw e;
    }
    return returnValue;
  }


}
