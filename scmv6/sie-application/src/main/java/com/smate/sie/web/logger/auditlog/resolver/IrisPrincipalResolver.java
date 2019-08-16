/**
 * Licensed to IRIS-System co. Copyright (C) 2014 IRIS, The IRIS Systems (Shenzhen) Limited.
 */
package com.smate.sie.web.logger.auditlog.resolver;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;

import com.github.inspektr.common.spi.PrincipalResolver;
import com.smate.core.base.utils.model.UserInfo;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * PrincipalResolver that can retrieve the username from either the Ticket or from the Credentials.
 * 
 * @author Administrator
 */
public final class IrisPrincipalResolver implements PrincipalResolver {
  @Override
  public String resolveFrom(JoinPoint auditTarget, Object returnValue) {
    return resolveFromInternal(auditTarget, returnValue);
  }

  @Override
  public String resolveFrom(JoinPoint auditTarget, Exception exception) {
    return resolveFromInternal(auditTarget);
  }

  @Override
  public String resolve() {
    return UNKNOWN_USER;
  }

  protected String resolveFromInternal(final JoinPoint joinPoint) {
    Long userId = SecurityUtils.getCurrentUserId();
    if (userId == null || userId == 0L) {
      return UNKNOWN_USER;
    } else {
      return userId.toString();
    }
  }

  protected String resolveFromInternal(final JoinPoint joinPoint, final Object returnValue) {
    Long userId = SecurityUtils.getCurrentUserId();
    if (userId == null || userId == 0L) {
      if (returnValue instanceof UserInfo) {
        return ((UserInfo) returnValue).getUsername();
      } else {
        Object arg1 = joinPoint.getArgs()[0];
        if (arg1 instanceof String) {
          if (((String) arg1).indexOf('├') > 0) {
            return StringUtils.left(StringUtils.split((String) arg1, '├')[0], 30);
          }
        }
        return UNKNOWN_USER;
      }
    } else {
      return userId.toString();
    }

  }
}
