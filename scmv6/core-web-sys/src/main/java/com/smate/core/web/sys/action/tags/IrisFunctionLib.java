package com.smate.core.web.sys.action.tags;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;

/**
 * 取得字符窜中包含str字符串的最后的游标
 * 
 * @author zk
 *
 */
public class IrisFunctionLib {

  public static int lastIndexOf(String name, String str) throws JspException {
    if (StringUtils.isNotBlank(name)) {
      return name.lastIndexOf(str);
    } else {
      return -1;
    }
  }
}
