package com.smate.core.web.sys.security.xss;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.xss.XssUtils;

/**
 * xss 通过重写参数获取方法实现.
 * 
 * @author tsz
 *
 * @date 2018年9月8日
 */
public class XsslHttpServletRequestWrapper extends HttpServletRequestWrapper {
  HttpServletRequest xssRequest = null;

  public XsslHttpServletRequestWrapper(HttpServletRequest request) {
    super(request);
    xssRequest = request;
  }

  @Override
  public String getParameter(String name) {
    String value = super.getParameter(name);
    if (value != null) {
      value = xssReplace(value);
    }
    return value;
  }

  @Override
  public String[] getParameterValues(String name) {
    String[] values = super.getParameterValues(name);
    if (values != null && values.length > 0) {
      for (int i = 0; i < values.length; i++) {
        values[i] = xssReplace(values[i]);
      }
    }
    return values;
  }

  @Override
  public String getHeader(String name) {
    String value = super.getHeader(name);
    if (value != null) {
      value = xssReplace(value);
    }
    return value;
  }

  private String xssReplace(String value) {
    String reslut = "";
    if (JacksonUtils.isJsonObjectOrJsonArray(value)) {
      reslut = XssUtils.transferJson(value);
    } else {
      // 对参数值进行过滤.
      reslut = XssUtils.xssReplace(value);
    }
    return reslut;
  }
}
