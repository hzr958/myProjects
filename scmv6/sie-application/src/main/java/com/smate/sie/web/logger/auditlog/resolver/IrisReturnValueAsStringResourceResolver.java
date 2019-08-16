/**
 * Licensed to IRIS-System co. Copyright (C) 2014 IRIS, The IRIS Systems (Shenzhen) Limited.
 */
package com.smate.sie.web.logger.auditlog.resolver;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.aspectj.lang.JoinPoint;

import com.github.inspektr.audit.spi.AuditResourceResolver;

/**
 * Implementation of {@link com.github.inspektr.audit.spi.AuditResourceResolver} that uses the
 * toString version of the return value as the resource.
 * 
 * @author Administrator
 */
public final class IrisReturnValueAsStringResourceResolver implements AuditResourceResolver {

  @Override
  @SuppressWarnings("rawtypes")
  public String[] resolveFrom(final JoinPoint auditableTarget, final Object retval) {
    if (retval instanceof Collection) {
      final Collection c = (Collection) retval;
      final String[] retvals = new String[c.size()];
      int i = 0;
      for (final Iterator iter = c.iterator(); iter.hasNext() && i < c.size(); i++) {
        final Object o = iter.next();
        if (o != null) {
          if (o instanceof Map) {
            retvals[i] = o.toString();
          } else {
            retvals[i] = iter.next().toString();
          }
        }
      }
      return retvals;
    }

    if (retval instanceof Object[]) {
      final Object[] vals = (Object[]) retval;
      final String[] retvals = new String[vals.length];
      for (int i = 0; i < vals.length; i++) {
        retvals[i] = vals[i].toString();
      }
      return retvals;
    }
    return new String[] {retval == null ? "return value is null" : retval.toString()};
  }

  @Override
  public String[] resolveFrom(final JoinPoint auditableTarget, final Exception exception) {
    final String message = exception.getMessage();
    if (message != null) {
      return new String[] {message};
    }
    return new String[] {exception.toString()};
  }
}
