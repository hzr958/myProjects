package com.smate.core.base.utils.tags;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.smate.core.base.utils.constant.ConstDictionary;
import com.smate.core.base.utils.service.consts.SieConstDicManage;

/**
 * @author yxs
 * @descript 下拉框自定义标签
 */
public class SelectTag extends BodyTagSupport {

  private static final long serialVersionUID = 1L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private String key = null;
  private String code = null;

  public int doStartTag() throws JspException {
    try {

      String html = render();
      pageContext.getOut().write(html);

      return EVAL_BODY_INCLUDE;
    } catch (IOException ioe) {
      throw new JspException(ioe.getMessage());
    }
  }

  private String render() {
    String result = "";
    List<ConstDictionary> list = null;
    try {
      WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
      SieConstDicManage sieConstDicManage = (SieConstDicManage) ctx.getBean("sieConstDicManage");
      list = sieConstDicManage.getSieConstByGategory(key);
    } catch (Exception e) {
      logger.error("SelectTag:" + key + " code:" + code, e);
    }
    StringBuilder sb = new StringBuilder();
    StringBuilder sb2 = new StringBuilder();
    sb.append("<ul class=\"menu unitype2\">");
    sb.append("<li class=\"mt10\">");
    sb.append("<div class=\"select\">");
    if (StringUtils.isBlank(code)) {// 当默认code值为空时，直接遍历列表显示所有的选项
      sb.append("<span class=\"selectVal2\" onselectstart=\"return false\"  >" + "请选择 " + "</span>");
      sb.append("<input type=\"hidden\" name=\"codeName\"/>");
      sb.append("<input type=\"hidden\" name=\"code\" />");
      sb.append("<i class=\"xl-icon\"></i>");
      sb.append(" <div class=\"selectList selectList2\">");
      sb.append("<div class=\"listA-all\">");
      if (list != null && list.size() > 0) {
        for (int i = 0; i < list.size(); i++) {
          ConstDictionary dictionary = list.get(i);
          sb.append("<a href=\"javascript:;\" onclick=\"changeSetValue(" + dictionary.getKey().getCode() + ",'"
              + dictionary.getZhCnName() + "');\" class=\"listA\">" + dictionary.getZhCnName() + "</a>");
        }
      }
    } else {// 当默认code值不为空时，根据当前code的值默认选中一条记录，然后列出其余所有的数据
      if (list != null && list.size() > 0) {
        for (int i = 0; i < list.size(); i++) {
          ConstDictionary dictionary = list.get(i);
          if (dictionary.getKey().getCode().equals(code)) {
            sb.append("<span class=\"selectVal selectVal2\" onselectstart=\"return false\"  >"
                + dictionary.getZhCnName() + "</span>");
            sb.append("<input type=\"hidden\" name=\"codeName\" value=" + dictionary.getZhCnName() + ">");
            sb.append("<input type=\"hidden\" name=\"code\" value=" + dictionary.getKey().getCode() + ">");
            sb.append("<i class=\"xl-icon\"></i>");
            sb.append(" <div class=\"selectList selectList2\">");
            sb.append("<div class=\"listA-all\">");
          }
          // else {
          sb2.append("<a href=\"javascript:;\" onclick=\"changeSetValue(" + dictionary.getKey().getCode() + ",'"
              + dictionary.getZhCnName() + "');\" class=\"listA\">" + dictionary.getZhCnName() + "</a>");
          // }
          if (i == list.size() - 1) {
            sb.append(sb2.toString());
          }
        }
      }
    }
    sb.append("</div>");
    sb.append("</div>");
    sb.append("</div>");
    sb.append("</li>");
    sb.append("</ul>");
    result = sb.toString();
    return result;
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

}
