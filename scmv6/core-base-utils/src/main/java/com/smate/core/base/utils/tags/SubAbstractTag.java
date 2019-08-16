package com.smate.core.base.utils.tags;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang.StringUtils;

public class SubAbstractTag extends BodyTagSupport {

  /**
   * 
   */
  private static final long serialVersionUID = 5985096939302902843L;

  private String value;

  private String lan;

  public int doStartTag() throws JspException {
    if (StringUtils.isNotBlank(value)) {
      if ("zh".equals(lan)) {
        String endChar = value.substring(value.length() - 1, value.length());
        if (!StringUtils.isAsciiPrintable(endChar)) {
          value = value.substring(0, value.length()) + "...";
        } else {
          int lastIndex = value.lastIndexOf(" ");
          if (lastIndex > 0) {
            value = value.substring(0, lastIndex) + "..." + extractHTMLTag(value.substring(lastIndex, value.length()));
          } else {
            value = value + "...";
          }
        }
      } else {
        int lastIndex = value.lastIndexOf(" ");
        if (lastIndex > 0) {
          value = value.substring(0, lastIndex) + "..." + extractHTMLTag(value.substring(lastIndex, value.length()));
        } else {
          int lastIndex1 = (value.lastIndexOf(",") >= 0 ? value.lastIndexOf(",") : value.lastIndexOf(".")) >= 0
              ? (value.lastIndexOf(",") >= 0 ? value.lastIndexOf(",") : value.lastIndexOf("."))
              : value.lastIndexOf("!");
          if (lastIndex1 > 0) {
            value =
                value.substring(0, lastIndex1) + "..." + extractHTMLTag(value.substring(lastIndex1, value.length()));
          } else {
            value = value + "...";
          }
        }
      }
    } else {
      value = "";
    }

    try {
      pageContext.getOut().write(value);
    } catch (IOException e) {

    }

    return SKIP_BODY;
  }

  private String extractHTMLTag(String str) {
    Pattern p = Pattern.compile("(<[^>]*>)");
    Matcher m = p.matcher(str);
    String htmlTagStr = "";
    while (m.find()) {
      htmlTagStr += m.group();
    }
    return htmlTagStr;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getLan() {
    return lan;
  }

  public void setLan(String lan) {
    this.lan = lan;
  }
}
