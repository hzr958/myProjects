/**
 * Licensed to IRIS-System co. Copyright (C) 2014 IRIS, The IRIS Systems (Shenzhen) Limited.
 */
package com.smate.sie.web.logger.auditlog.resolver;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.inspektr.audit.spi.support.AbstractAuditResourceResolver;

import net.sf.json.JSONArray;

/**
 * Returns the parameters as an Json of strings.
 * 
 * @author Administrator
 */
public class IrisParametersAsJsonResourceResolver extends AbstractAuditResourceResolver {

  @Override
  protected String[] createResource(final Object[] args) {
    final List<String> stringArgsRet = new ArrayList<String>();
    final List<String> stringArgs = new ArrayList<String>();
    for (final Object arg : args) {
      boolean isString = arg instanceof String;
      if ((null != arg && !isString) || (isString && StringUtils.isNotBlank((String) arg))) {
        stringArgs.add(arg.toString());
      } else {
        stringArgs.add("null");
      }
    }
    stringArgsRet.add(JSONArray.fromObject(stringArgs).toString());
    if (stringArgsRet.size() == 0) {
      return new String[] {"null"};
    } else {
      return stringArgsRet.toArray(new String[stringArgsRet.size()]);
    }
  }
}
