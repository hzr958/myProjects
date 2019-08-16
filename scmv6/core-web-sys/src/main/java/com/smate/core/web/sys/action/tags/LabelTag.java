package com.smate.core.web.sys.action.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * 根据当前语言显示字段内容.
 * 
 * @author zk
 */
public class LabelTag extends BodyTagSupport {


  /**
   * 
   */
  private static final long serialVersionUID = 2368834338342756359L;
  /**
   * 
   */
  private String zhText = "";
  private String enText = "";

  public int doStartTag() throws JspException {
    try {
      String html = render();

      html = html.replace("&lt;", "<");
      html = html.replace("&gt;", ">");

      html = html.replace("&amp;", "&");
      html = html.replace("&quot;", "\"");
      html = html.replace("&apos;", "\'");

      html = html.replace("&#034;", "\"");
      html = html.replace("&#038;", "&");
      html = html.replace("&#039;", "\'");
      html = html.replace("&#060;", "<");
      html = html.replace("&#062;", ">");

      pageContext.getOut().write(html.replace("<a", "<a class=\"Blue\""));

      return EVAL_BODY_INCLUDE;
    } catch (IOException ioe) {
      throw new JspException(ioe.getMessage());
    }
  }

  private String render() {

    String lang = LocaleContextHolder.getLocale().getLanguage();

    String result = "";
    if ("zh".equalsIgnoreCase(lang)) {
      String txt = this.getZhText();
      if (StringUtils.isBlank(txt))
        result = this.enText;
      else
        result = txt.replace("　", "").trim();
    } else if ("en".equalsIgnoreCase(lang)) {
      String txt = this.getEnText();
      if (StringUtils.isBlank(txt))
        result = this.zhText;
      else
        result = txt.replace("　", "").trim();
    }

    return result;

  }

  public int doEndTag() throws JspTagException {
    return EVAL_PAGE;
  }

  public int doAfterBody() throws JspTagException {
    return EVAL_PAGE;
  }

  /**
   * @return the zhText
   */
  public String getZhText() {
    return zhText;
  }

  /**
   * @param zhText the zhText to set
   */
  public void setZhText(String zhText) {
    this.zhText = zhText;
  }

  /**
   * @return the enText
   */
  public String getEnText() {
    return enText;
  }

  /**
   * @param enText the enText to set
   */
  public void setEnText(String enText) {
    this.enText = enText;
  }

}
