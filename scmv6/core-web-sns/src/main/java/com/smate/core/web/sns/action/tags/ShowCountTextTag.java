package com.smate.core.web.sns.action.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.number.NumberUtils;

/**
 * 显示统计数标签
 * 
 * @author wsn
 * @date May 16, 2019
 */
public class ShowCountTextTag extends BodyTagSupport {

  private String count; // 统计数
  private String preFix; // 前缀
  private String sufFix; // 后缀

  public int doStartTag() throws JspException {
    String text = "";
    if (StringUtils.isNotBlank(count) && NumberUtils.isCreatable(count.trim()) && !"0".equals(count.trim())) {
      text = count.trim();
      text = NumberUtils.toLong(text) > 999 ? "1K+" : text;
      if (StringUtils.isNotBlank(preFix) && StringUtils.isNotBlank(sufFix)) {
        text = preFix.trim() + text + sufFix.trim();
      }
    }
    try {
      pageContext.getOut().write(text);
    } catch (IOException e) {
    }
    return EVAL_BODY_BUFFERED;
  }

  public String getCount() {
    return count;
  }

  public void setCount(String count) {
    this.count = count;
  }

  public String getPreFix() {
    return preFix;
  }

  public void setPreFix(String preFix) {
    this.preFix = preFix;
  }

  public String getSufFix() {
    return sufFix;
  }

  public void setSufFix(String sufFix) {
    this.sufFix = sufFix;
  }


}
