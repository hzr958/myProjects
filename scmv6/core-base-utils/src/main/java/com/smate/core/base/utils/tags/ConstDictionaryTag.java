package com.smate.core.base.utils.tags;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.smate.core.base.utils.model.consts.ConstDictionary2;
import com.smate.core.base.utils.service.consts.ConstDicManage;


/**
 * 常量标签.
 * 
 * @author zk
 * 
 */
public class ConstDictionaryTag extends BodyTagSupport {

  /**
   * 
   */
  private static final long serialVersionUID = 8867949672935249715L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private String key = null;
  private String code = null;
  private String tagName = null;
  private String idprefix = null;
  private String onclick = null;
  private String tagclass = null;
  /**
   * 站点类似，SNS端为空，单位端为"scmwebrol".
   */
  private String webContextType = null;

  /*
   * @Autowired private ConstDicManage constDicManage;
   */

  public int doStartTag() throws JspException {
    try {

      String html = render();
      pageContext.getOut().write(html);

      return EVAL_BODY_INCLUDE;
    } catch (IOException ioe) {
      throw new JspException(ioe.getMessage());
    }
  }

  /**
   * 读取数据.
   * 
   * @return
   */
  private String render() {

    String result = "";
    List<ConstDictionary2> list = null;
    try {
      WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
      ConstDicManage constDicManage = (ConstDicManage) ctx.getBean("constDicManage");
      list = constDicManage.getConstByGategory(key);
    } catch (Exception e) {
      logger.error("ConstDictionaryTag获取远程服务失败key:" + key + " code:" + code, e);
    }

    if (list != null && list.size() > 0) {

      idprefix = tagName.replaceAll("\\W", "_");
      idprefix = idprefix.replaceAll("[_]+", "_");
      StringBuilder sb = new StringBuilder();
      Locale locale = LocaleContextHolder.getLocale();
      String lang = locale.getLanguage();
      for (int i = 0; i < list.size(); i++) {
        ConstDictionary2 cd = list.get(i);
        if ("zh".equalsIgnoreCase(lang)) {
          generTag(i + 1, cd.getKey().getCode(), cd.getZhCnName(), sb);
        } else if ("en".equalsIgnoreCase(lang)) {
          generTag(i + 1, cd.getKey().getCode(), cd.getEnUsName(), sb);
        }
      }
      result = sb.toString();
    }
    return result;
  }

  /**
   * 组织标签.
   * 
   * @param index
   * @param code
   * @param name
   * @param sb
   */
  private void generTag(int index, String keyCode, String name, StringBuilder sb) {

    sb.append("<label><input type='radio' id='").append(getIdprefix() + index).append("'  name='").append(tagName)
        .append("'  ");
    if (keyCode.equals(code)) {
      sb.append(" checked='true' ");
    }

    if (onclick != null) {
      sb.append(" onclick='").append(onclick).append("' ");
    }

    if (tagclass != null) {
      sb.append(" class='").append(tagclass).append("' ");
    }

    sb.append("value='").append(keyCode).append("' />").append(name).append("</label>\n");

  }

  public String getTagclass() {
    return tagclass;
  }

  public void setTagclass(String tagclass) {
    this.tagclass = tagclass;
  }

  public String getOnclick() {
    return onclick;
  }

  public void setOnclick(String onclick) {
    this.onclick = onclick;
  }

  public String getIdprefix() {

    return idprefix;
  }

  public int doEndTag() throws JspTagException {
    return EVAL_PAGE;
  }

  public int doAfterBody() throws JspTagException {
    return EVAL_PAGE;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getTagName() {
    return tagName;
  }

  public void setTagName(String tagName) {
    this.tagName = tagName;
  }

  public String getWebContextType() {
    return webContextType;
  }

  public void setWebContextType(String webContextType) {
    this.webContextType = webContextType;
  }

}
