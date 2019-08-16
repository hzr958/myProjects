package com.smate.center.oauth.action.validatecode;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.smate.center.oauth.cache.OauthCacheService;
import com.smate.core.base.utils.constant.security.SecurityConstants;
import com.smate.core.base.utils.struts2.Struts2Utils;

public class ValidateCodeAction extends ActionSupport {
  private static final long serialVersionUID = 833847955216749899L;

  private String validateCode;

  @Autowired
  private OauthCacheService oauthCacheService;

  @Action("/oauth/validatecode/check")
  public void checkValidateCode() throws IOException {
    String validateCode =
        (String) oauthCacheService.get(SecurityConstants.OAUTH_VALIDATE_CODE, Struts2Utils.getSession().getId());
    if (StringUtils.equalsIgnoreCase(validateCode, this.validateCode)) {
      Struts2Utils.renderText("true");
    } else {
      Struts2Utils.renderText("false");
    }
  }

  public String getValidateCode() {
    return validateCode;
  }

  public void setValidateCode(String validateCode) {
    this.validateCode = validateCode;
  }
}
