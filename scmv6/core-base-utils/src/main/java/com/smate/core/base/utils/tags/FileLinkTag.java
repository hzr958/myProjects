package com.smate.core.base.utils.tags;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 获取文件下载链接，如果fileCode为空，则直接返回不带code的链接.
 * 
 * @author zk
 * 
 */
public class FileLinkTag extends BaseFileLinkTag {

  @Value("${domainscm}")
  private String domain;
  /**
   * 
   */
  private static final long serialVersionUID = 3344878257452429652L;

  public String getDomain() throws JspException {
    /*
     * WebApplicationContext ctx = WebApplicationContextUtils
     * .getWebApplicationContext(pageContext.getServletContext()); SnsWebServiceLocator
     * snsServiceLocator = (SnsWebServiceLocator) ctx.getBean("snsWebServiceLocator"); String domain =
     * null;
     * 
     * Integer nodeId = null; if (NumberUtils.isDigits(super.getNodeId())) { nodeId =
     * Integer.valueOf(super.getNodeId()); } else { nodeId =
     * BasicWebServerConstants.SNS_WS_ID.intValue(); } try { domain = snsServiceLocator.getWebUrl(new
     * Long(nodeId)); } catch (Exception e) { throw new JspException(e); }
     */
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }


}
