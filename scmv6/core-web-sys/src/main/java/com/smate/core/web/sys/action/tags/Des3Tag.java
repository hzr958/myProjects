package com.smate.core.web.sys.action.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.acegisecurity.util.EncryptionUtils.EncryptionException;
import org.apache.commons.lang.StringUtils;

import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 加密编号.
 * 
 * @author zk
 *
 */
public class Des3Tag extends BodyTagSupport {

  private static final long serialVersionUID = 1L;

  private String code = "";

  public int doStartTag() throws JspException {

    String bar = "";

    if (StringUtils.isNotBlank(code)) {
      code = code.trim();
      try {
        bar = Des3Utils.encodeToDes3(code);
      } catch (EncryptionException e) {

      }
    }

    try {
      pageContext.getOut().write(bar);
    } catch (IOException e) {

    }
    return SKIP_BODY;
  }

  /**
   * @return code
   */
  public String getCode() {
    return code;
  }

  /**
   * @param code 要设置的 code
   */
  public void setCode(String code) {
    this.code = code;
  }

}
