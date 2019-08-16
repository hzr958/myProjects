package com.smate.core.web.sys.security.authority;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.RequestMatcher;

import com.smate.core.base.utils.constant.security.SecurityConstants;

public class RequiresAuthenticationRequestMatcher implements RequestMatcher {


  @Override
  public boolean matches(HttpServletRequest request) {
    if (request.getParameter(SecurityConstants.FROM_OAUTH) == null) {
      return false;
    } else {
      return true;
    }
  }

}
