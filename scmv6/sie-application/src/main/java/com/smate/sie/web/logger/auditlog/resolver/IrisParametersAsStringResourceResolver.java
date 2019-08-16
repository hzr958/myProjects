/**
 * Licensed to IRIS-System co. Copyright (C) 2014 IRIS, The IRIS Systems (Shenzhen) Limited.
 */
package com.smate.sie.web.logger.auditlog.resolver;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.inspektr.audit.spi.support.AbstractAuditResourceResolver;

/**
 * Returns the parameters as an String of strings.
 * 
 * @author Administrator
 */
public class IrisParametersAsStringResourceResolver extends AbstractAuditResourceResolver {

  @Override
  protected String[] createResource(final Object[] args) {
    final List<String> stringArgs = new ArrayList<String>();
    for (final Object arg : args) {
      boolean isString = arg instanceof String;
      if ((null != arg && !isString) || (isString && StringUtils.isNotBlank((String) arg))) {
        stringArgs.add(arg.toString());
      } else {
        stringArgs.add("null");
      }
    }
    if (stringArgs.size() == 0) {
      return new String[] {"null"};
    } else {
      return stringArgs.toArray(new String[stringArgs.size()]);
    }
  }
}
